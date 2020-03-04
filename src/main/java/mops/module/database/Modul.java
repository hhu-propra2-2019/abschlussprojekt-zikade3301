package mops.module.database;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import org.hibernate.annotations.Fetch;

@Entity
public class Modul {

  @Id
  @GeneratedValue
  private Long id;

  private String title;
  private String description;

  @ManyToMany()
  private List<Semester> semester;

  @OneToMany
  private List<Modul> requieredModul;

  @ManyToMany
  private List<Docent> dozenten;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "modul")
  private List<Courses> courses;

}
