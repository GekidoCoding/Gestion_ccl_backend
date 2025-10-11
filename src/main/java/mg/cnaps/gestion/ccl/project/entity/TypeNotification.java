package mg.cnaps.gestion.ccl.project.entity;

import lombok.Getter;
import lombok.Setter;
import mg.cnaps.gestion.ccl.framework.jpa.core.generator.IdGeneratorUtil;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "CCL2_TYPE_NOTIFICATION")
public class TypeNotification {
    @Id
    @Column(name = "ID")
    private String id;
    @PrePersist
    public void assignId() {
        if (id == null) {
            this.setId(IdGeneratorUtil.generate("SEQ_CCL2_TYPE_NOTIFICATION" ,"TPNF-",8  ,"oracle" ));
        }
    }

    @Size(max = 255)
    @Column(name = "LIBELLE")
    private String libelle;

    @Column(name = "NIVEAU")
    private Integer niveau;
}