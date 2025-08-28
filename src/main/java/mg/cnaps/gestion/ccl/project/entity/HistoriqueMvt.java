package mg.cnaps.gestion.ccl.project.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import mg.cnaps.gestion.ccl.framework.core.generator.IdGeneratorUtil;
import mg.cnaps.gestion.ccl.project.util.TimestampUtil;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "CCL2_HISTORIQUE_MVT")
public class HistoriqueMvt {
    @Id
    @Column(name = "ID")
    private String id;
    @PrePersist
    public void assignId() {
        if (id == null) {
            this.setId(IdGeneratorUtil.generate("SEQ_CCL2_HISTORIQUE_MVT" ,"HMV-",8  ,"oracle" ));
        }
    }

    @Column(name = "DH_ACTION")
    private Timestamp dhAction;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_GESTIONNAIRE")
    private Gestionnaire gestionnaire;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_MOUVEMENT")
    private Mouvement mouvement;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_TYPE_MOUVEMENT")
    private TypeMouvement typeMouvement;

    public Timestamp getDhActionTimestamp() {
        return dhAction;
    }
    public String getDhAction() {
        return TimestampUtil.formatTimestamp(dhAction);
    }
}