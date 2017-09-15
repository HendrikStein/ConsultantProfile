package de.jastech.output;

import de.jastech.model.AggregatedSkillMapEntry;
import de.jastech.model.Project;
import de.jastech.model.Skill;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Hendrik Stein
 */
public abstract class OutputWriter {

    /**
     * Write Project
     *
     * @param filename
     */
    public void write(String filename) {
        try {
            this.writePersonalInfo();
            this.writeIntroduction();
            this.writeVita();
            this.writeProjects();
            this.writeSkillMatrix();
            this.writeIndustrySkillMatrix();
            this.writeFile(filename);
            System.out.println("Document " + filename + this.getFileType() + " created");
        } catch (IOException ex) {
            System.err.print(ex);
        }

    }

    /**
     * Get the {@link ResourceBundle}.
     *
     * @param locale {@link Locale}
     * @return the {@link ResourceBundle}
     */
    protected final ResourceBundle getResourceBundle(Locale locale) {
        return ResourceBundle.getBundle("Profile", locale);
    }

    /**
     * Get file Type for writer
     *
     * @return File type
     */
    abstract String getFileType();

    /**
     * Write personal info
     */
    abstract void writePersonalInfo();

    /**
     * Write personal introduction
     */
    abstract void writeIntroduction();

    /**
     * Write vitae
     */
    abstract void writeVita();

    /**
     * Write projects
     */
    abstract void writeProjects();

    /**
     * Write project.
     *
     * @param project {@link Project}
     */
    abstract void writeProject(Project project);

    /**
     * Write technology stack.
     *
     * @param skills {@link Skill} Skill List
     */
    abstract void writeTechnologyStack(List<Skill> skills);

    /**
     * Write skill matrix.
     */
    abstract void writeSkillMatrix();

    /**
     * Write skill category
     *
     * @param category
     */
    abstract void writeSkillCategory(Map.Entry<String, List<AggregatedSkillMapEntry>> category);

    /**
     * Write industry skill matrix
     */
    abstract void writeIndustrySkillMatrix();

    /**
     * Write file.
     *
     * @param filename Filename
     * @throws IOException
     */
    abstract void writeFile(String filename) throws IOException;
}
