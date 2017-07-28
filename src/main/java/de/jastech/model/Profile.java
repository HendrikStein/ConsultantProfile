package de.jastech.model;

import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * @author Hendrik Stein
 */
@Data
public class Profile {
	private Person person;
	private List<Project> projects;

	private Profile(){
		
	}
	
	public static Profile of(Person person, List<Project> projects){
        Profile profile = new Profile();
        Objects.requireNonNull(person);
        Objects.requireNonNull(projects);
        profile.setPerson(person);
        profile.setProjects(projects);
        return profile;
    }

}
