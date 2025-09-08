package mg.cnaps.gestion.ccl.project.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import mg.cnaps.gestion.ccl.framework.core.generator.IdGeneratorUtil;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "CCL2_MODELE_INFRA")
public class ModeleInfra {
    @Id
    @Column(name = "ID")
    private String id;
    @PrePersist
    public void assignId() {
        if (id == null) {
            this.setId(IdGeneratorUtil.generate("SEQ_CCL2_MODELE_INFRA" ,"MOD-",8  ,"oracle" ));
        }
    }


    @Size(max = 255)
    @Column(name = "NOM_MDL_INFRA")
    private String nom;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_CAT_INFRA", nullable = false)
    private CategorieInfra catInfra;

}