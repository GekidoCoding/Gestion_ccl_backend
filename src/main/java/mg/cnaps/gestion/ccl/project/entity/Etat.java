package mg.cnaps.gestion.ccl.project.entity;


import lombok.Data;
import mg.cnaps.gestion.ccl.framework.core.generator.IdGeneratorUtil;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "CCL2_ETAT")
public class Etat {
    @Id
    @Column(name = "ID")
    private String id;
    @PrePersist
    public void assignId() {
        if (id == null) {
            this.setId(IdGeneratorUtil.generate("SEQ_CCL2_ETAT" ,"ETAT-",8  ,"oracle" ));
        }
    }

    @Size(max = 255)
    @Column(name = "CCL2_ETAT")
    private String etat;

    @Column(name = "CODE")
    private Integer code;


}