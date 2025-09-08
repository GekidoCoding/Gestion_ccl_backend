package mg.cnaps.gestion.ccl.project.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import mg.cnaps.gestion.ccl.framework.core.generator.IdGeneratorUtil;

import javax.persistence.*;


@Getter
@Setter
@Entity
@Table(name = "CCL2_MOUVEMENT_INFRA")
public class MouvementInfra {
    @Id
    @Column(name = "ID")
    private String id;
    @PrePersist
    public void assignId() {
        if (id == null) {
            this.setId(IdGeneratorUtil.generate("SEQ_CCL2_MOUVEMENT_INFRA" ,"MINF-",8  ,"oracle" ));
        }
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_MOUVEMENT")
    @JsonBackReference
    private Mouvement mouvement;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_INFRASTRUCTURE")
    private Infrastructure infrastructure;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_FREQUENCE")
    private Frequence frequence;

}