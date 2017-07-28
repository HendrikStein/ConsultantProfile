package de.jastech.input;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import de.jastech.ProjectFileFilter;
import de.jastech.model.Person;
import de.jastech.model.Profile;
import de.jastech.model.Project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hendrik Stein
 */
public class InputReader {
    private Gson gson = new Gson();

    private InputReader() {
    }

    /**
     * Read profile data.
     *
     * @param basePath profiles basePath
     * @return {@link Profile}
     * @throws FileNotFoundException if file not found
     */
    public static Profile readProfile(String basePath) throws FileNotFoundException {
        InputReader reader = new InputReader();
        // Read input Data
        Person person = reader.readPersonData(basePath);
        List<Project> projects = reader.readProjects(basePath);
        Profile profile = Profile.of(person, projects);
        return profile;
    }

    /**
     * Read personal data from input.
     *
     * @param basePath Person basePath
     * @return {@link Person}
     * @throws FileNotFoundException when input data is not found
     */
    private Person readPersonData(String basePath) throws FileNotFoundException {
        JsonReader reader = new JsonReader(new FileReader(basePath + "//person.json"));
        return this.gson.fromJson(reader, Person.class);
    }

    /**
     * Read all project input data.
     *
     * @param basePath Person basePath
     * @return Array from {@link Person}
     * @throws FileNotFoundException when input data is not found
     */
    private List<Project> readProjects(String basePath) throws FileNotFoundException {
        List<Project> projects = new ArrayList<>();
        File folder = new File(basePath);
        for (File file : folder.listFiles(new ProjectFileFilter())) {
            try {
                projects.add(this.readProject(file));
            } catch (RuntimeException ex) {
                System.err.println("Error while reading " + file.getAbsolutePath());
                throw (ex);
            }

        }
        return projects;
    }

    /**
     * Read project input data
     *
     * @param file project input file
     * @return {@link Person}
     * @throws FileNotFoundException when input data is not found
     */
    private Project readProject(File file) throws FileNotFoundException {
        JsonReader reader = new JsonReader(new FileReader(file));
        return this.gson.fromJson(reader, Project.class);
    }
}
