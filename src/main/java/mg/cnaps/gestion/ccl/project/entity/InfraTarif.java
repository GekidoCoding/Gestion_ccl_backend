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
@Table(name = "CCL2_INFRA_TARIF")
public class InfraTarif {

    @Id
    @Column(name = "ID")
    private String id;
    @PrePersist
    public void assignId() {
        if (id == null) {
            this.setId(IdGeneratorUtil.generate("SEQ_CCL2_INFRA_TARIF" ,"INFT-",8  ,"oracle" ));
        }
    }

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_INFRASTRUCTURE", nullable = false)
    @JsonBackReference
    private Infrastructure infrastructure;


    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_FREQUENCE", nullable = false)
    private Frequence frequence;

    @Column(name = "TARIF_INFRA")
    private Double tarifInfra;

}