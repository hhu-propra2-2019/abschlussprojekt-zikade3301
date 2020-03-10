package mops.module.database;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Data
public class Antrag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String modul;

    private long modulid;

    @LastModifiedDate
    private LocalDateTime createDate;

    private LocalDateTime approveDate;
}
