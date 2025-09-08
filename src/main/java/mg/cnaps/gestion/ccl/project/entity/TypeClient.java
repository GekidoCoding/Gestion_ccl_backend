package mg.cnaps.gestion.ccl.project.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import mg.cnaps.gestion.ccl.framework.core.generator.IdGeneratorUtil;


import javax.persistence.*;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "CCL2_TYPE_CLIENT")
public class TypeClient {
    @Id
    @Column(name = "ID")
    private String id;
    @PrePersist
    public void assignId() {
        if (id == null) {
            this.setId(IdGeneratorUtil.generate("SEQ_CCL2_TYPE_CLIENT" ,"TPC-",8  ,"oracle" ));
        }
    }

    @Size(max = 255)
    @Column(name = "TYPE_CLIENT")
    private String typeClient;

}