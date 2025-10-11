package mg.cnaps.gestion.ccl.project.entity;

import lombok.Getter;
import lombok.Setter;
import mg.cnaps.gestion.ccl.framework.jpa.core.generator.IdGeneratorUtil;
import mg.cnaps.gestion.ccl.project.util.TimestampUtil;

import javax.persistence.*;
import java.sql.Timestamp;


@Getter
@Setter
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