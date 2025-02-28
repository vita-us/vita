package de.unistuttgart.vis.vita.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import de.unistuttgart.vis.vita.model.document.Occurrence;
import com.google.common.collect.Iterables;


/**
 * The information about an entity that can be collected in the first pass
 * <p>
 * This class can not be persisted and should be converted to Entity for that.
 */
public class BasicEntity {

  private String displayName;
  private EntityType type;
  private String entityId;

  private SortedSet<Attribute> nameAttributes;
  private List<Occurrence> occurrences;

  /**
   * Creates a new instance of Basic entity generating an UUID.
   */
  public BasicEntity() {
    nameAttributes = new TreeSet<>(new AttributeComparator());
    occurrences = new ArrayList<>();
    entityId = UUID.randomUUID().toString();
  }

  /**
   * Gets the name under which this entity will be shown
   *
   * @return the name under which this entity will be shown
   */
  public String getDisplayName() {
    return displayName;
  }

  /**
   * Sets the name under which this entity is shown in the graphical user interface.
   *
   * @param newDisplayName - the new name under which this entity should be shown
   */
  public void setDisplayName(String newDisplayName) {
    this.displayName = newDisplayName;
  }

  /**
   * Gets all the names under which the entity is known
   *
   * @return the names under which the entity is known
   */
  public SortedSet<Attribute> getNameAttributes() {
    return nameAttributes;
  }

  /**
   * Sets the names under which the entity is known
   *
   * @param nameAttributes the names under which the entity is known
   */
  public void setNameAttributes(Set<Attribute> nameAttributes) {
    this.nameAttributes = new TreeSet<>(new AttributeComparator());
    this.nameAttributes.addAll(nameAttributes);
  }

  /**
   * Gets all occurrences of this entity in the document
   *
   * @return Set of all occurrences of this entity in the document
   */
  public List<Occurrence> getOccurrences() {
    return occurrences;
  }

  /**
   * Sets the occurrences for this entity.
   *
   * @param newOccurrences - a set of new occurrences for this entity
   */
  public void setOccurrences(List<Occurrence> newOccurrences) {
    this.occurrences = newOccurrences;
  }

  /**
   * Indicates of which type this entity is
   *
   * @return either PERSON or PLACE
   */
  public EntityType getType() {
    return type;
  }

  /**
   * Sets of which type this entity is
   *
   * @param type either PERSON or PLACE
   */
  public void setType(EntityType type) {
    this.type = type;
  }

  /**
   * Gets the entityId under which this entity is known
   *
   * @return the entityId under which this entity is known
   */
  public String getEntityId() {
    return entityId;
  }

  @Override
  public String toString() {
    return "BasicEntity{" + "type=" + type + ", displayName='" + displayName + '\''
        + ", occurrences=" + StringUtils.join(occurrences, ", ") + ", namedAttributes="
        + StringUtils.join(nameAttributes, ", ") + '}';
  }

  /**
   * Merges the names and occurrences of the other entity into this one.
   *
   * @param other - the other entity, whose names and occurrences should be merged into this one
   */
  public void merge(BasicEntity other) {
    setNameAttributes(Attribute.merge(Iterables.concat(getNameAttributes(),
        other.getNameAttributes())));
    occurrences.addAll(other.getOccurrences());
  }
}


/**
 * Comparator for the names of the entity. Sorts them by the occurrence size, descending.
 */
class AttributeComparator implements Comparator<Attribute>, Serializable {

  /**
   * auto generated
   */
  private static final long serialVersionUID = -3862453893785126236L;

  @Override
  public int compare(Attribute o1, Attribute o2) {
    return o1.getOccurrences().size() > o2.getOccurrences().size() ? -1 : 1;
  }
}