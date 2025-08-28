package mg.cnaps.gestion.ccl.project.entity;

import lombok.Data;
import mg.cnaps.gestion.ccl.framework.core.generator.IdGeneratorUtil;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "CCL2_INFRASTRUCTURE")
public class Infrastructure {
    @Id
    @Column(name = "ID")
    private String id;
    @PrePersist
    public void assignId() {
        if (id == null) {
            this.setId(IdGeneratorUtil.generate("SEQ_CCL2_INFRASTRUCTURE" ,"INF-",8  ,"oracle" ));
        }
    }

    @Size(max = 255)
    @Column(name = "NUMERO")
    private String numero;

    @Size(max = 255)
    @Column(name = "NOM_INFRA")
    private String nom;

    @Column(name = "CAPACITE")
    private Long capacite;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_LOCALISATION", nullable = false)
    private Localisation localisation;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_MODELE_INFRA", nullable = false)
    private ModeleInfra modeleInfra;

    @Size(max = 255)
    @Column(name = "ELEMENTS")
    private String elements;

    @Column(name = "TARIF")
    private Double prix;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_ETAT", nullable = false)
    private Etat etat;

}