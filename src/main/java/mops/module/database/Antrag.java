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
    private Long id;

    private String modul;

    private Long modulid;

    @LastModifiedDate
    private LocalDateTime createDate;

    private LocalDateTime approveDate;
}
