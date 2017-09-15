package de.jastech.model;

import lombok.Data;

/**
 * Address model.
 * 
 * @author Hendrik Stein
 */
@Data
public class Address {
  private String street;
  private String zip;
  private String city;
  private String phone;
  private String email;
  private String internet;
}
