package mg.cnaps.gestion.ccl.project.entity;


import lombok.Getter;
import lombok.Setter;
import mg.cnaps.gestion.ccl.framework.jpa.core.generator.IdGeneratorUtil;

import javax.persistence.*;
import javax.validation.constraints.Size;


@Getter
@Setter
@Entity
@Table(name = "CCL2_TYPE_MOUVEMENT")
public class TypeMouvement {
    @Id
    @Column(name = "ID")
    private String id;
    @PrePersist
    public void assignId() {
        if (id == null) {
            this.setId(IdGeneratorUtil.generate("SEQ_CCL2_TYPE_MOUVEMENT" ,"TPM-",8  ,"oracle" ));
        }
    }

    @Size(max = 255)
    @Column(name = "NOM_TYPE_MVT")
    private String nom;

    @Column(name = "NIVEAU_PROCESSUS")
    private Integer niveauProcessus;


}