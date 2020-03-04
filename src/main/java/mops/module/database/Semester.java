package mops.module.database;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Semester {

  @Id
  @GeneratedValue
  private Long id;

  private String semestertype;

  @ManyToMany
  private List<Modul> module;

}
