package mg.cnaps.gestion.ccl.project.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import mg.cnaps.gestion.ccl.framework.core.generator.IdGeneratorUtil;
import mg.cnaps.gestion.ccl.project.util.TimestampUtil;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.time.Instant;


@Getter
@Setter
@Entity
@Table(name = "CCL2_HISTO_FACTURE")
public class HistoFacture {
    @Id
    @Size(max = 255)
    @Column(name = "ID")
    private String id;
    @PrePersist
    public void assignId() {
        if (id == null) {
            this.setId(IdGeneratorUtil.generate("SEQ_CCL2_HISTO_FACTURE" ,"HFA-",8  ,"oracle" ));
        }
    }

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_FACTURE")
    private Facture facture;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_ETAT")
    private Etat etat;

    @Column(name = "DH_ACTION")
    private Timestamp dhAction;

    @Size(max = 255)
    @Column(name = "REF_FACTURE")
    private String refFacture;

    @Column(name = "MONTANT")
    private Double montant;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_GESTIONNAIRE")
    private Gestionnaire gestionnaire;

    public String getDhAction(){
        return TimestampUtil.formatTimestamp(this.dhAction);
    }

}