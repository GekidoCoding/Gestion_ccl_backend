package mg.cnaps.gestion.ccl.project.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import mg.cnaps.gestion.ccl.framework.core.generator.IdGeneratorUtil;
import mg.cnaps.gestion.ccl.project.entity.existant.Agent;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@Entity
@Table(name = "CCL2_GESTIONNAIRE")
public class Gestionnaire {
    @Id
    @Column(name = "ID")
    private String id;
    @PrePersist
    public void assignId() {
        if (id == null) {
            this.setId(IdGeneratorUtil.generate("SEQ_CCL2_GESTIONNAIRE" ,"GST-",8  ,"oracle" ));
        }
    }

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER , optional = false)
    @JoinColumn(name = "MATRICULE_GESTIONNAIRE" , nullable = false)
    private Agent  agent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_ETAT")
    private Etat etat;

}