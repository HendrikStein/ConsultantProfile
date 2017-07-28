package de.jastech.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @autor Hendrik Stein
 */
@Data
public class Project {
	private String industry;
	private String name;
	private String customer;
	private String role;
	private int teamSize;
	private String ref;
	private Date start;
	private Date end;
	private String description;
	private List<Skill> techStack;
	private List<String> tasks;

	public Date getEnd() {
		if (this.end == null) {
			this.end = new Date();
		}
		return this.end;
	}
}
