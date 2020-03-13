package mops.module.database;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Getter
@Setter
public class Antrag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String modul;

    private Long modulid;

    @CreatedDate
    private LocalDateTime createDate;

    private LocalDateTime approveDate;
}
