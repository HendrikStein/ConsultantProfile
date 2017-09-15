package de.jastech.output;

import de.jastech.model.AggregatedIndustryMapEntry;
import de.jastech.model.AggregatedSkillMapEntry;
import de.jastech.model.Profile;
import de.jastech.model.Project;
import de.jastech.model.Skill;
import de.jastech.model.VitaEntry;
import de.jastech.service.ProfileService;
import de.jastech.utils.DateUtils;
import de.jastech.utils.WordUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Period;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;

/**
 * Word ouptput writer.
 * 
 * @author Hendrik Stein
 * 
 */
public class WordWriter extends OutputWriter {
  private Profile profile;
  private final ProfileService service;
  private ResourceBundle resourceBundle;
  private final XWPFDocument doc;
  private static final String FONT_FAMILY = "Verdana";
  private static final int FONT_SIZE_DOCUMENT_HEADER = 6;
  private static final int FONT_SIZE_DOCUMENT_FOOTER = 6;
  private static final int FONT_SIZE_HEADLINE = 12;
  private static final int FONT_SIZE_SUB_HEADLINE = 10;
  private static final int FONT_SIZE_TEXT = 8;
  private static final int MAX_LINES = 25;
  private int currentPageLines = 0;

  /**
   * Constructor.
   * 
   * @param profile
   *          {@link Profile}
   * @param locale
   *          {@link Locale}
   */
  public WordWriter(Profile profile, Locale locale) {
    this.profile = profile;
    this.service = new ProfileService(profile);
    this.resourceBundle = super.getResourceBundle(locale);
    this.doc = new XWPFDocument();
  }

  private void writeDocumentHeader() {
    XWPFHeader head = doc.createHeader(HeaderFooterType.DEFAULT);
    XWPFRun run = head.createParagraph().createRun();
    run.setFontFamily(FONT_FAMILY);
    run.setFontSize(FONT_SIZE_DOCUMENT_HEADER);

    run.setText(this.profile.getPerson().getCompany() + ", " + this.profile.getPerson().getSurname()
        + " " + this.profile.getPerson().getName());
    run.addCarriageReturn();
    run.setText(this.profile.getPerson().getPosition());
    run.addCarriageReturn();
  }

  private void writeDocumentFooter() {
    XWPFFooter foot = doc.createFooter(HeaderFooterType.DEFAULT);
    XWPFRun run = foot.createParagraph().createRun();
    run.setFontFamily(FONT_FAMILY);
    run.setFontSize(FONT_SIZE_DOCUMENT_FOOTER);

    run.setText(this.profile.getPerson().getAddress().getEmail());
    run.addCarriageReturn();
    run.setText(this.profile.getPerson().getAddress().getInternet());
  }

  /**
   * Write Project.
   *
   * @param filename file name
   */
  public void write(String filename) {
    this.writeDocumentHeader();
    this.writeDocumentFooter();
    super.write(filename);
  }

  @Override
  final String getFileType() {
    return ".docx";
  }

  @Override
  void writePersonalInfo() {
    // Look at Header / Footer
  }

  @Override
  void writeIntroduction() {
    XWPFParagraph p = doc.createParagraph();
    writeHeadline(p.createRun(), this.resourceBundle.getString("profile_header"));

    XWPFRun run = this.createRunForText(p);
    run.setText(this.profile.getPerson().getConclusion());
  }

  /**
   * Create a {@link XWPFRun} Object with fontsize and family.
   *
   * @param p
   *          {@link XWPFParagraph}
   * @return {@link XWPFRun}
   */
  XWPFRun createRunForText(XWPFParagraph p) {
    XWPFRun run = p.createRun();
    run.setFontSize(FONT_SIZE_TEXT);
    run.setFontFamily(FONT_FAMILY);
    return run;
  }

  /**
   * Write Headline.
   *
   * @param run
   *          Run Object
   * @param headline
   *          Headline
   */
  private void writeHeadline(XWPFRun run, String headline) {
    run.setFontSize(FONT_SIZE_HEADLINE);
    run.setFontFamily(FONT_FAMILY);
    run.setBold(true);
    run.setText(headline);
    run.addCarriageReturn();
  }

  /**
   * Write Subheadline.
   *
   * @param run
   *          Run Object
   * @param headline
   *          Headline
   */
  private void writeSubHeadline(XWPFRun run, String headline) {
    run.setFontSize(FONT_SIZE_SUB_HEADLINE);
    run.setFontFamily(FONT_FAMILY);
    run.setBold(true);
    run.setText(headline);
    run.addCarriageReturn();
  }

  /**
   * Write bold Text.
   *
   * @param run
   *          Run Object
   * @param text
   *          Headline
   * @param addCarriageReturn
   *          Add carriage return
   */
  private void writeBoldHeader(XWPFRun run, String text, boolean addCarriageReturn) {
    run.setFontSize(FONT_SIZE_TEXT);
    run.setFontFamily(FONT_FAMILY);
    run.setBold(true);
    run.setText(text);
    if (addCarriageReturn) {
      run.addCarriageReturn();
    }
  }

  @Override
  void writeVita() {
    XWPFParagraph p = doc.createParagraph();
    writeHeadline(p.createRun(), this.resourceBundle.getString("work_experience"));
    List<VitaEntry> vitae = this.service.sortVitaByDateDesc(VitaEntry::getEnd);

    for (VitaEntry entry : vitae) {
      XWPFRun run = this.createRunForText(p);
      run.setText(DateUtils.format(entry.getStart()) + " - " + DateUtils.format(entry.getEnd()));
      run.addTab();
      run.setText(entry.getStation());
      run.addCarriageReturn();
    }
  }

  @Override
  void writeProjects() {
    XWPFParagraph p = doc.createParagraph();
    this.writePageBreak(p.createRun());
    writeHeadline(p.createRun(), this.resourceBundle.getString("project_experience"));
    this.service.sortProjectsByDateDesc(Project::getEnd).forEach(this::writeProject);
  }

  @Override
  void writeProject(Project project) {
    XWPFParagraph p = doc.createParagraph();
    this.writeSubHeadline(p.createRun(), project.getName());
    this.writeProjectDescription(project.getDescription());
    this.writeProjectHeader(project, this.createRunForText(p));
    this.writeTechnologyStack(project.getTechStack());
    this.writeProjectTasks(project.getTasks());
    this.writePageBreak(p.createRun());
  }

  /**
   * Write project description.
   *
   * @param description
   *          description
   */
  private void writeProjectDescription(String description) {
    XWPFParagraph p = this.doc.getLastParagraph();
    XWPFRun run = this.createRunForText(p);
    run.addCarriageReturn();
    run.setText(description);
    run.addCarriageReturn();
    run.addCarriageReturn();
  }

  /**
   * Write project tasks.
   *
   * @param tasks
   *          List of {@link String}
   */
  private void writeProjectTasks(List<String> tasks) {
    XWPFParagraph p = this.doc.getLastParagraph();
    this.writeBoldHeader(this.createRunForText(p), this.resourceBundle.getString("tasks"), true);
    XWPFRun run = this.createRunForText(p);
    for (String task : tasks) {
      run.setText("- " + task);
      run.addCarriageReturn();
    }
  }

  /**
   * Add page break.
   *
   * @param run
   *          {@link XWPFRun}
   */
  private void writePageBreak(XWPFRun run) {
    run.addBreak(BreakType.PAGE);
    this.currentPageLines = 0;
  }

  /**
   * Write Project Header.
   *
   * @param project
   *          {@link Project}
   * @param run
   *          {@link XWPFRun}
   */
  private void writeProjectHeader(Project project, XWPFRun run) {
    run.setText(this.resourceBundle.getString("duration"));
    this.writeTabs(WordUtils.calcTabsForProjectSummary(this.resourceBundle.getString("duration")),
        run);
    Period duration = DateUtils.calculatePeriod(project.getStart(), project.getEnd());
    run.setText(DateUtils.format(project.getStart()) + " - " + DateUtils.format(project.getEnd())
        + " (" + DateUtils.format(duration) + ")");
    run.addCarriageReturn();

    run.setText(this.resourceBundle.getString("industry"));
    this.writeTabs(WordUtils.calcTabsForProjectSummary(this.resourceBundle.getString("industry")),
        run);
    run.setText(project.getIndustry());
    run.addCarriageReturn();

    run.setText(this.resourceBundle.getString("role"));
    this.writeTabs(WordUtils.calcTabsForProjectSummary(this.resourceBundle.getString("role")), run);
    run.setText(project.getRole());
    run.addCarriageReturn();

    run.setText(this.resourceBundle.getString("involved_persons"));
    this.writeTabs(
        WordUtils.calcTabsForProjectSummary(this.resourceBundle.getString("involved_persons")),
        run);
    run.setText(Integer.toString(project.getTeamSize()));
    run.addCarriageReturn();
    run.addCarriageReturn();
  }

  @Override
  void writeTechnologyStack(List<Skill> skills) {
    Map<String, List<Skill>> groupedSkills = skills.stream()
        .collect(Collectors.groupingBy(Skill::getCat));
    XWPFParagraph p = this.doc.getLastParagraph();
    this.writeBoldHeader(p.createRun(), this.resourceBundle.getString("technology"), true);

    for (Map.Entry<String, List<Skill>> entry : groupedSkills.entrySet()) {
      XWPFRun run = this.createRunForText(p);
      run.setText(entry.getKey());
      this.writeTabs(WordUtils.calcTabsForProjectSummary(entry.getKey()), run);
      run.setText(Skill.joinToString(", ", entry.getValue()));
      run.addCarriageReturn();
    }
    this.createRunForText(p).addCarriageReturn();
  }

  /**
   * Add tab to {@link XWPFRun}.
   *
   * @param amount
   *          tab amount
   * @param run
   *          {@link XWPFRun}
   */
  private void writeTabs(int amount, XWPFRun run) {
    for (int i = 0; i < amount; i++) {
      run.addTab();
    }
  }

  @Override
  void writeSkillMatrix() {
    XWPFParagraph p = this.doc.getLastParagraph();
    writeHeadline(p.createRun(), this.resourceBundle.getString("qualification"));
    this.service.getAggregatedSkillMapByCategories().entrySet().forEach(this::writeSkillCategory);
  }

  @Override
  void writeSkillCategory(Map.Entry<String, List<AggregatedSkillMapEntry>> category) {
    XWPFParagraph p = this.doc.createParagraph();
    int tableSpaceOnPage = category.getValue().size() + 2; // +2: LineBreak and Header

    // I did not find a way to find out the current lines on a page
    if (this.currentPageLines + tableSpaceOnPage > MAX_LINES) {
      this.writePageBreak(p.createRun());
    } else {
      this.currentPageLines += tableSpaceOnPage;
      p.createRun().addCarriageReturn();
      this.currentPageLines++;
    }

    this.writeBoldHeader(p.createRun(), category.getKey(), false);

    XWPFTable table = this.doc.createTable(category.getValue().size() + 1, 4);
    this.setHeaderCellForTable(0, 0, table, this.resourceBundle.getString("name"));
    this.setHeaderCellForTable(0, 1, table, this.resourceBundle.getString("project_count"));
    this.setHeaderCellForTable(0, 2, table, this.resourceBundle.getString("duration"));
    this.setHeaderCellForTable(0, 3, table, this.resourceBundle.getString("versions"));

    for (int i = 1; i <= category.getValue().size(); i++) {
      this.setContentCellForTable(i, 0, table, category.getValue().get(i - 1).getEntry());
      this.setContentCellForTable(i, 1, table,
          Integer.toString(category.getValue().get(i - 1).getProjectAmount()));
      this.setContentCellForTable(i, 2, table,
          DateUtils.format(category.getValue().get(i - 1).getPeriod()));
      this.setContentCellForTable(i, 3, table,
          String.join(", ", category.getValue().get(i - 1).getVersions()));
    }
  }

  @Override
  void writeIndustrySkillMatrix() {
    XWPFParagraph p = this.doc.createParagraph();
    this.writePageBreak(p.createRun());
    writeHeadline(p.createRun(), this.resourceBundle.getString("industry_qualification"));
    Map<String, AggregatedIndustryMapEntry> skills = this.service.getAggregatedIndustryMap();
    XWPFTable table = this.doc.createTable(skills.size() + 1, 3);
    this.setHeaderCellForTable(0, 0, table, this.resourceBundle.getString("industry"));
    this.setHeaderCellForTable(0, 1, table, this.resourceBundle.getString("duration"));
    this.setHeaderCellForTable(0, 2, table, this.resourceBundle.getString("project_count"));
    int counter = 1;

    for (Map.Entry<String, AggregatedIndustryMapEntry> entry : skills.entrySet()) {
      this.setContentCellForTable(counter, 0, table, entry.getKey());
      this.setContentCellForTable(counter, 1, table,
          DateUtils.format(entry.getValue().getPeriod()));
      this.setContentCellForTable(counter, 2, table,
          Integer.toString(entry.getValue().getProjectAmount()));
      counter++;
    }
  }

  /**
   * Set a header cell for a table.
   *
   * @param row
   *          number of the row
   * @param cell
   *          number of the cell
   * @param table
   *          {@link XWPFTable}
   * @param text
   *          Text to set
   */
  private void setHeaderCellForTable(int row, int cell, XWPFTable table, String text) {
    XWPFParagraph p = table.getRow(row).getCell(cell).getParagraphs().get(0);
    XWPFRun run = this.createRunForText(p);
    run.setBold(true);
    run.setText(text);
  }

  /**
   * Set a content cell for a table.
   *
   * @param row
   *          number of the row
   * @param cell
   *          number of the cell
   * @param table
   *          {@link XWPFTable}
   * @param text
   *          Text to set
   */
  private void setContentCellForTable(int row, int cell, XWPFTable table, String text) {
    XWPFParagraph p = table.getRow(row).getCell(cell).getParagraphs().get(0);
    p.setAlignment(ParagraphAlignment.CENTER);
    XWPFRun run = this.createRunForText(p);
    run.setText(text);
  }

  @Override
  void writeFile(String filename) throws IOException {
    OutputStream os = new FileOutputStream(new File(filename + this.getFileType()));
    this.doc.write(os);
    os.close();
    this.doc.close();
  }
}
