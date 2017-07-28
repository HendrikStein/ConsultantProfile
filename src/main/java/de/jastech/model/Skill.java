package de.jastech.model;

import lombok.Data;

import java.util.Set;

/**
 * @author Hendrik Stein
 */
@Data
public class Skill {
    private String entry;
    private String cat;
    private Set<String> versions;

    @Override
    public String toString() {
        return "Skill{" +
                "entry='" + entry + '\'' +
                ", cat='" + cat + '\'' +
                ", versions=" + versions +
                '}';
    }

    public Skill(String entry, String cat, Set<String> versions) {
        this.entry = entry;
        this.cat = cat;
        this.versions = versions;
    }

    public Skill(){
        // Empty constructor
    }

   

}
