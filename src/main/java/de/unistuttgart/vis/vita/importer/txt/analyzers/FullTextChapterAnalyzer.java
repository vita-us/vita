package de.unistuttgart.vis.vita.importer.txt.analyzers;

import java.util.List;

import de.unistuttgart.vis.vita.importer.util.ChapterPosition;
import de.unistuttgart.vis.vita.importer.util.Line;

/**
 * Not really a Chapter Analyzer, because it does not analyze the text. It simply puts the whole
 * text into one chapter without a heading.
 */
public class FullTextChapterAnalyzer extends AbstractChapterAnalyzer {

  /**
   * Instantiates a new FullTextChapterAnalyzer and sets the lines to analyze.
   *
   * @param chapterArea ArrayList of Line - The lines containing the chapters. Should not be null.
   * @throws IllegalArgumentException If input is null.
   */
  public FullTextChapterAnalyzer(List<Line> chapterArea) {
    super(chapterArea);
  }

  @Override
  public int getStartOfAnalysis() {
    return 0;
  }

  @Override
  protected ChapterPosition useRule() {
    ChapterPosition positions = new ChapterPosition();
    positions.addChapter(0, 0, chapterArea.size() - 1);
    return positions;
  }
}
