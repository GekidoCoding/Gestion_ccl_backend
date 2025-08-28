package mg.cnaps.gestion.ccl.project.entity;

import lombok.Data;
import mg.cnaps.gestion.ccl.framework.core.generator.IdGeneratorUtil;
import mg.cnaps.gestion.ccl.project.util.TimestampUtil;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Data
@Entity
@Table(name = "CCL2_FACTURE")
public class Facture {
    @Id
    @Column(name = "ID")
    private String id;
    @PrePersist
    public void assignId() {
        if (id == null) {
            this.setId(IdGeneratorUtil.generate("SEQ_CCL2_FACTURE" ,"FAC-",8  ,"oracle" ));
        }
    }

    @Column(name = "DH_CREATION")
    private Timestamp dhCreation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_GESTIONNAIRE")
    private Gestionnaire gestionnaire;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_MOUVEMENT")
    private Mouvement mouvement;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_ETAT")
    private Etat etat;

    @Size(max = 255)
    @Column(name = "REF_FACTURE")
    private String refFacture;

    @Size(max = 255)
    @Column(name = "MONTANT_TOTAL")
    private Double montantTotal;

    @Column(name = "REMISE")
    private Double remise;

    @Column(name = "ACOMPTE_VERSE")
    private Double acompteVerse;

    public String getDhCreation() {
      return TimestampUtil.formatTimestamp(dhCreation);
    }
}