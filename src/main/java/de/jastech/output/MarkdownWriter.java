package de.jastech.output;

import de.jastech.model.*;
import de.jastech.service.ProfileService;
import de.jastech.utils.DateUtils;
import net.steppschuh.markdowngenerator.list.UnorderedList;
import net.steppschuh.markdowngenerator.rule.HorizontalRule;
import net.steppschuh.markdowngenerator.table.Table;
import net.steppschuh.markdowngenerator.text.Text;
import net.steppschuh.markdowngenerator.text.heading.Heading;

import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.*;

/**
 * @author Hendrik Stein
 */
public class MarkdownWriter extends BaseOutputWriter implements OutputWriter {
	private final Profile profile;
	private final ProfileService service;
	private final ResourceBundle resourceBundle;
	private final String outputDocName;

	public MarkdownWriter(Profile profile, Locale locale, String outputDocName) {
		this.profile = profile;
		this.service = new ProfileService(profile);
		this.resourceBundle = super.getResourceBundle(locale);
		this.outputDocName = outputDocName;
	}

	@Override
	public void write() {
		this.writePersonalInfo();
		this.writeIntroduction();
		this.writeVita();
		this.writeProjects();
		this.writeSkillMatrix();
		this.writeIndustrySkillMatrix();
		super.writeFile(this.outputDocName + ".md");
	}

	private void writePersonalInfo() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		this.output.append(new Heading(profile.getPerson().getSurname() + " " + profile.getPerson().getName()))
				.append("\n");
		Table.Builder tableBuilder = new Table.Builder().withAlignments(Table.ALIGN_CENTER).addRow("", "");

		tableBuilder.addRow(this.resourceBundle.getString("birthday"),
				sdf.format(this.profile.getPerson().getBirthday()));

		tableBuilder.addRow(this.resourceBundle.getString("title"),
				String.join(", ", this.profile.getPerson().getTitle()));

		tableBuilder.addRow(this.resourceBundle.getString("email"), this.profile.getPerson().getAddress().getEmail());

		tableBuilder.addRow(this.resourceBundle.getString("internet"),
				this.profile.getPerson().getAddress().getInternet());
		this.output.append(tableBuilder.build()).append("\n\n");
	}

	/**
	 * Write the introduction.
	 */
	private void writeIntroduction() {
		this.output.append(new Heading(this.resourceBundle.getString("profile_header"))).append("\n");
		this.output.append(new Text(this.profile.getPerson().getConclusion())).append("\n\n");
	}

	/**
	 * Write Vita.
	 */
	private void writeVita() {
		this.output.append(new Heading(this.resourceBundle.getString("work_experience"), 2)).append("\n");
		this.output.append(new UnorderedList<>(this.service.sortVitaByDateDesc(VitaEntry::getEnd))).append("\n\n");
	}

	/**
	 * Write projects.
	 */
	private void writeProjects() {
		this.output.append(new Heading(this.resourceBundle.getString("project_experience"), 2)).append("\n");
		this.service.sortProjectsByDateDesc(Project::getEnd).forEach(this::writeProject);
	}

	/**
	 * Write project.
	 *
	 * @param project
	 *            {@link Project}
	 */
	private void writeProject(Project project) {
		this.output.append(new Heading(project.getName(), 3)).append("\n");
		Table.Builder tableBuilder = new Table.Builder().withAlignments(Table.ALIGN_CENTER).addRow(
				this.resourceBundle.getString("start"), this.resourceBundle.getString("end"),
				this.resourceBundle.getString("duration"), this.resourceBundle.getString("industry"),
				this.resourceBundle.getString("role"), this.resourceBundle.getString("involved_persons"));

		Period duration = DateUtils.calculatePeriod(project.getStart(), project.getEnd());

		tableBuilder.addRow(DateUtils.format(project.getStart()), DateUtils.format(project.getEnd()),
				DateUtils.format(duration), project.getIndustry(), project.getRole(), project.getTeamSize());

		this.output.append(tableBuilder.build()).append("\n\n");
		this.writeTechnologyStack(project.getTechStack());
		this.output.append(new Text(project.getDescription())).append("\n");
		this.output.append(new Heading(this.resourceBundle.getString("tasks"), 4)).append("\n");
		this.output.append(new UnorderedList<>(project.getTasks())).append("\n\n");
		this.output.append(new HorizontalRule()).append("\n\n");
	}

	/**
	 * Write technology stack.
	 *
	 * @param skills
	 *            {@link Skill} Skill List
	 */
	private void writeTechnologyStack(List<Skill> skills) {
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

	/**
	 * Write skill matrix.
	 */
	private void writeSkillMatrix() {
		this.output.append(new Heading(this.resourceBundle.getString("qualification"), 2)).append("\n");
		this.service.getAggregatedSkillMapByCategories().entrySet().forEach(this::writeSkillCategory);
		this.output.append("\n");
	}

	/**
	 * Write skill category
	 *
	 * @param category
	 */
	private void writeSkillCategory(Map.Entry<String, List<AggregatedSkillMapEntry>> category) {
		this.output.append(new Heading(category.getKey(), 3)).append("\n");
		Table.Builder tableBuilder = new Table.Builder().withAlignments(Table.ALIGN_CENTER).addRow(
				this.resourceBundle.getString("name"), this.resourceBundle.getString("versions"),
				this.resourceBundle.getString("duration"), this.resourceBundle.getString("project_count"));

		category.getValue().forEach(row -> {
			tableBuilder.addRow(row.getEntry(), String.join(", ", row.getVersions()), DateUtils.format(row.getPeriod()),
					row.getProjectAmount());
		});
		this.output.append(tableBuilder.build()).append("\n");
	}

	/**
	 * Write industry skill matrix
	 */
	private void writeIndustrySkillMatrix() {
		this.output.append(new Heading(this.resourceBundle.getString("industry_qualification"), 2)).append("\n");
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
