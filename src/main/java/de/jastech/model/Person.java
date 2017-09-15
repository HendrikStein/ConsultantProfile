package de.jastech.model;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * Person model.
 * @author Hendrik Stein
 */
@Data
public class Person {
  private String name;
  private String surname;
  private String pic;
  private List<String> title;
  private Date birthday;
  private String company;
  private String position;
  private String conclusion;
  private Address address;
  private List<VitaEntry> vita;
}
