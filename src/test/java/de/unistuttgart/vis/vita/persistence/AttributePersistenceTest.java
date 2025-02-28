package de.unistuttgart.vis.vita.persistence;

import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.TypedQuery;

import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.Test;

import de.unistuttgart.vis.vita.data.AttributeTestData;
import de.unistuttgart.vis.vita.data.DocumentTestData;
import de.unistuttgart.vis.vita.data.PersonTestData;
import de.unistuttgart.vis.vita.model.document.Chapter;
import de.unistuttgart.vis.vita.model.document.Document;
import de.unistuttgart.vis.vita.model.document.Occurrence;
import de.unistuttgart.vis.vita.model.document.Sentence;
import de.unistuttgart.vis.vita.model.document.TextPosition;
import de.unistuttgart.vis.vita.model.document.Range;
import de.unistuttgart.vis.vita.model.entity.Attribute;
import de.unistuttgart.vis.vita.model.entity.Person;

/**
 * Performs tests whether instances of Attribute can be persisted correctly.
 */
public class AttributePersistenceTest extends AbstractPersistenceTest {

  private AttributeTestData testData;
  
  @Override
  public void setUp() {
    super.setUp();
    
    testData = new AttributeTestData();
  }
  
  @Test
  public void testPersistOneAttribute() {
    
    // first set up an Attribute
    Attribute attribute = testData.createTestAttribute(1);

    // persist this Attribute
    em.persist(attribute);
    startNewTransaction();

    // read persisted attributes from database
    List<Attribute> attributes = readAttributesFromDb();

    // check whether data is correct
    assertEquals(1, attributes.size());
    Attribute readAttribute = attributes.get(0);
    testData.checkData(readAttribute, 1);
  }

  /**
   * Reads Attributes from database and returns them.
   * 
   * @return list of attributes
   */
  private List<Attribute> readAttributesFromDb() {
    TypedQuery<Attribute> query = em.createQuery("from Attribute", Attribute.class);
    List<Attribute> result = query.getResultList();
    return result;
  }

  /**
   * Check whether all Named Queries of Attribute are working correctly.
   */
  @Test
  public void testNamedQueries() {
    Attribute testAttribute = testData.createTestAttribute(1);

    em.persist(testAttribute);
    startNewTransaction();

    // check Named Query finding all Attributes
    TypedQuery<Attribute> allQ =
        em.createNamedQuery("Attribute.findAllAttributes", Attribute.class);
    List<Attribute> allAttributes = allQ.getResultList();

    assertTrue(allAttributes.size() > 0);
    Attribute readAttribute = allAttributes.get(0);
    testData.checkData(readAttribute, 1);

    String id = readAttribute.getId();
    
    // check Named Query finding attributes of an entity
    Person testPerson = new PersonTestData().createTestPerson(1);
    Attribute entityAttribute = testData.createTestAttribute(2);
    testPerson.getAttributes().add(entityAttribute);
    
    String entityId = testPerson.getId();
    
    em.persist(entityAttribute);
    em.persist(testPerson);
    startNewTransaction();
    
    TypedQuery<Attribute> entityQ = em.createNamedQuery("Attribute.findAttributesForEntity", 
                                                          Attribute.class);
    entityQ.setParameter("entityId", entityId);
    List<Attribute> entityAttributes = entityQ.getResultList();
    assertEquals(1, entityAttributes.size());
    testData.checkData(entityAttributes.get(0), 2);

    // check Named Query finding attributes by id
    TypedQuery<Attribute> idQ = em.createNamedQuery("Attribute.findAttributeById", Attribute.class);
    idQ.setParameter("attributeId", id);
    Attribute idAttribute = idQ.getSingleResult();

    testData.checkData(idAttribute, 1);

    // check Named Query finding attributes by type
    TypedQuery<Attribute> typeQ =
        em.createNamedQuery("Attribute.findAttributeByType", Attribute.class);
    typeQ.setParameter("attributeType", AttributeTestData.TEST_ATTRIBUTE_1_TYPE);
    List<Attribute> typeAttributes = typeQ.getResultList();

    assertTrue(typeAttributes.size() > 0);
    Attribute typeAttribute = typeAttributes.get(0);
    testData.checkData(typeAttribute, 1);
  }

  @Test
  public void testOcurrencesAreSorted() {
    Document doc = new Document();
    Chapter chapter = new Chapter();
    
    TextPosition pos1 = TextPosition.fromGlobalOffset(10, DocumentTestData.TEST_DOCUMENT_CHARACTER_COUNT);
    TextPosition pos2 = TextPosition.fromGlobalOffset(20, DocumentTestData.TEST_DOCUMENT_CHARACTER_COUNT);
    TextPosition pos3 = TextPosition.fromGlobalOffset(30, DocumentTestData.TEST_DOCUMENT_CHARACTER_COUNT);
    TextPosition pos4 = TextPosition.fromGlobalOffset(40, DocumentTestData.TEST_DOCUMENT_CHARACTER_COUNT);
    
    Range sentenceRange1 = new Range(pos1, pos4);
    Range sentenceRange2 = new Range(pos3, pos4) ;
    Sentence sentence1 = new Sentence(sentenceRange1, chapter, 0);
    Sentence sentence2 = new Sentence(sentenceRange2, chapter, 1);
    
    Range range1 = new Range(pos1, pos4);
    Range range2 = new Range(pos2, pos4);
    Range range3 = new Range(pos3, pos4);

    Attribute attribute = testData.createTestAttribute(1);
    
    Occurrence occurrence1 = new Occurrence(sentence1, range1);
    Occurrence occurrence2 = new Occurrence(sentence1, range2);
    Occurrence occurrence3 = new Occurrence(sentence2, range3);
    
    // Add the occurrences in an order that is neither the correct one, nor the reverse
    attribute.getOccurrences().add(occurrence2);
    attribute.getOccurrences().add(occurrence1);
    attribute.getOccurrences().add(occurrence3);

    em.persist(doc);
    em.persist(chapter);
    em.persist(occurrence1);
    em.persist(occurrence3);
    em.persist(occurrence2);
    em.persist(attribute);
    startNewTransaction();

    Attribute dbAttribute = em.createQuery("from Attribute", Attribute.class).getSingleResult();
    assertThat(dbAttribute.getOccurrences(),
        IsIterableContainingInOrder.contains(occurrence1, occurrence2, occurrence3));
  }
}
