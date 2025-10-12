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
@Table(name = "CCL2_PAIEMENT")
public class Paiement {
    @Id
    @Column(name = "ID")
    private String id;
    @PrePersist
    public void assignId() {
        if (id == null) {
            this.setId(IdGeneratorUtil.generate("SEQ_CCL2_PAIEMENT" ,"PAI-",8  ,"oracle" ));
        }
    }

    @Column(name = "ACOMPTE_VERSE")
    private Double acompteVerse;

    @Column(name = "DATE_PAIEMENT")
    private Timestamp datePaiement;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_FACTURE")
    private Facture facture;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_MODE_PAIEMENT")
    private ModePaiement modePaiement;

    @JoinColumn(name = "MONNAIE")
    private Double monnaie;

    @Column(name = "RESTE")
    private Double reste;

    public String getDatePaiement() {
        return TimestampUtil.formatTimestamp(datePaiement);
    }
}