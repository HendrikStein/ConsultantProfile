package de.jastech.output;

import de.jastech.model.AggregatedSkillMapEntry;
import de.jastech.model.Profile;
import de.jastech.model.Project;
import de.jastech.model.Skill;
import de.jastech.model.VitaEntry;
import de.jastech.service.ProfileService;
import de.jastech.utils.DateUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import net.steppschuh.markdowngenerator.list.UnorderedList;
import net.steppschuh.markdowngenerator.rule.HorizontalRule;
import net.steppschuh.markdowngenerator.table.Table;
import net.steppschuh.markdowngenerator.text.Text;
import net.steppschuh.markdowngenerator.text.heading.Heading;

/**
 * Markdown writer.
 * 
 * @author Hendrik Stein
 */
public class MarkdownWriter extends OutputWriter {
  private Profile profile;
  private ProfileService service;
  private StringBuilder output;
  private ResourceBundle resourceBundle;

  /**
   * Constructor.
   * 
   * @param profile
   *          {@link Profile}
   * @param locale
   *          {@link Locale}
   */
  public MarkdownWriter(Profile profile, Locale locale) {
    this.output = new StringBuilder();
    this.profile = profile;
    this.service = new ProfileService(profile);
    this.resourceBundle = super.getResourceBundle(locale);
  }

  @Override
  public void write(String filename) {
    this.output.append("[![Build Status](https://travis-ci.org/HendrikStein/ConsultantProfile.svg?branch=master)](https://travis-ci.org/HendrikStein/ConsultantProfile) \n");
    super.write(filename);
  }
  
  @Override
  protected void writeFile(String filename) throws IOException {
    BufferedWriter bw = new BufferedWriter(new FileWriter(filename + this.getFileType()));
    bw.append(this.output);
    bw.close();
  }

  @Override
  final String getFileType() {
    return ".md";
  }

  @Override
  protected void writePersonalInfo() {
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    this.output
        .append(new Heading(profile.getPerson().getSurname() + " " + profile.getPerson().getName()))
        .append("\n");
    Table.Builder tableBuilder = new Table.Builder().withAlignments(Table.ALIGN_CENTER).addRow("",
        "");

    tableBuilder.addRow(this.resourceBundle.getString("birthday"),
        sdf.format(this.profile.getPerson().getBirthday()));

    tableBuilder.addRow(this.resourceBundle.getString("title"),
        String.join(", ", this.profile.getPerson().getTitle()));

    tableBuilder.addRow(this.resourceBundle.getString("email"),
        this.profile.getPerson().getAddress().getEmail());

    tableBuilder.addRow(this.resourceBundle.getString("internet"),
        this.profile.getPerson().getAddress().getInternet());
    this.output.append(tableBuilder.build()).append("\n\n");
  }

  @Override
  protected void writeIntroduction() {
    this.output.append(new Heading(this.resourceBundle.getString("profile_header"))).append("\n");
    this.output.append(new Text(this.profile.getPerson().getConclusion())).append("\n\n");
  }

  @Override
  protected void writeVita() {
    this.output.append(new Heading(this.resourceBundle.getString("work_experience"), 2))
        .append("\n");
    this.output.append(new UnorderedList<>(this.service.sortVitaByDateDesc(VitaEntry::getEnd)))
        .append("\n\n");
  }

  @Override
  protected void writeProjects() {
    this.output.append(new Heading(this.resourceBundle.getString("project_experience"), 2))
        .append("\n");
    this.service.sortProjectsByDateDesc(Project::getEnd).forEach(this::writeProject);
  }

  @Override
  protected void writeProject(Project project) {
    this.output.append(new Heading(project.getName(), 3)).append("\n");
    Table.Builder tableBuilder = new Table.Builder().withAlignments(Table.ALIGN_CENTER).addRow(
        this.resourceBundle.getString("start"), this.resourceBundle.getString("end"),
        this.resourceBundle.getString("duration"), this.resourceBundle.getString("industry"),
        this.resourceBundle.getString("role"), this.resourceBundle.getString("involved_persons"));

    Period duration = DateUtils.calculatePeriod(project.getStart(), project.getEnd());

    tableBuilder.addRow(DateUtils.format(project.getStart()), DateUtils.format(project.getEnd()),
        DateUtils.format(duration), project.getIndustry(), project.getRole(),
        project.getTeamSize());

    this.output.append(tableBuilder.build()).append("\n\n");
    this.writeTechnologyStack(project.getTechStack());
    this.output.append(new Text(project.getDescription())).append("\n");
    this.output.append(new Heading(this.resourceBundle.getString("tasks"), 4)).append("\n");
    this.output.append(new UnorderedList<>(project.getTasks())).append("\n\n");
    this.output.append(new HorizontalRule()).append("\n\n");
  }

  @Override
  protected void writeTechnologyStack(List<Skill> skills) {
    this.output.append(new Heading(this.resourceBundle.getString("technology"), 4)).append("\n");

    Table.Builder tableBuilder = new Table.Builder().withAlignments(Table.ALIGN_CENTER).addRow(
        this.resourceBundle.getString("name"), this.resourceBundle.getString("versions"),
        this.resourceBundle.getString("category"));

    // Sort Skills
    skills.stream().sorted(Comparator.comparing(Skill::getCat)).forEach(row -> {
      tableBuilder.addRow(row.getEntry(), String.join(", ", row.getVersions()), row.getCat());
    });

    this.output.append(tableBuilder.build()).append("\n\n");
  }

  @Override
  protected void writeSkillMatrix() {
    this.output.append(new Heading(this.resourceBundle.getString("qualification"), 2)).append("\n");
    this.service.getAggregatedSkillMapByCategories().entrySet().forEach(this::writeSkillCategory);
    this.output.append("\n");
  }

  @Override
  protected void writeSkillCategory(Map.Entry<String, List<AggregatedSkillMapEntry>> category) {
    this.output.append(new Heading(category.getKey(), 3)).append("\n");
    Table.Builder tableBuilder = new Table.Builder().withAlignments(Table.ALIGN_CENTER).addRow(
        this.resourceBundle.getString("name"), this.resourceBundle.getString("versions"),
        this.resourceBundle.getString("duration"), this.resourceBundle.getString("project_count"));

    category.getValue().forEach(row -> {
      tableBuilder.addRow(row.getEntry(), String.join(", ", row.getVersions()),
          DateUtils.format(row.getPeriod()), row.getProjectAmount());
    });
    this.output.append(tableBuilder.build()).append("\n");
  }

  @Override
  protected void writeIndustrySkillMatrix() {
    this.output.append(new Heading(this.resourceBundle.getString("industry_qualification"), 2))
        .append("\n");
    Table.Builder tableBuilder = new Table.Builder().withAlignments(Table.ALIGN_CENTER).addRow(
        this.resourceBundle.getString("industry"), this.resourceBundle.getString("duration"),
        this.resourceBundle.getString("project_count"));

    this.service.getAggregatedIndustryMap().entrySet().forEach(entry -> {
      tableBuilder.addRow(entry.getKey(), DateUtils.format(entry.getValue().getPeriod()),
          entry.getValue().getProjectAmount());
    });
    this.output.append(tableBuilder.build()).append("\n");
  }

}
