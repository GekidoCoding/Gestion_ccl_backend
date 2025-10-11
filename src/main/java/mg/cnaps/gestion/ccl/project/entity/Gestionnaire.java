package mg.cnaps.gestion.ccl.project.entity;

import lombok.Getter;
import lombok.Setter;
import mg.cnaps.gestion.ccl.framework.jpa.core.generator.IdGeneratorUtil;

import javax.persistence.*;


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


    @JoinColumn(name = "MATRICULE_GESTIONNAIRE")
    private String  matriculeGestionnaire;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_ETAT")
    private Etat etat;

}