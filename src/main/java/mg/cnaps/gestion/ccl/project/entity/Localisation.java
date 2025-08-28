package mg.cnaps.gestion.ccl.project.entity;

import lombok.Data;

import mg.cnaps.gestion.ccl.framework.core.generator.IdGeneratorUtil;

import javax.persistence.*;
import javax.validation.constraints.Size;
@Data
@Entity
@Table(name = "CCL2_LOCALISATION")
public class Localisation {
    @Id
    @Column(name = "ID")
    private String id;
    @PrePersist
    public void assignId() {
        if (id == null) {
            this.setId(IdGeneratorUtil.generate("SEQ_CCL2_LOCALISATION" ,"LOC-",8  ,"oracle" ));
        }
    }

    @Size(max = 255)
    @Column(name = "NOM_LOCALISATION")
    private String nom;

}