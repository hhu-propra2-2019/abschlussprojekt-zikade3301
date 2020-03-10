package mops.module.database;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Data
public class Antrag {
    @Id
    @GeneratedValue
    private long id;

    private Modul modul;

    @LastModifiedDate
    private LocalDateTime createDate;

    private LocalDateTime approveDate;
}
