package mg.cnaps.gestion.ccl.project.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import mg.cnaps.gestion.ccl.framework.jpa.core.generator.IdGeneratorUtil;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Timestamp;


@Getter
@Setter
@Entity
@Table(name = "CCL2_HISTORIQUE_INFRA")
public class HistoriqueInfra {
    @Id
    @Column(name = "ID")
    private String id;
    @PrePersist
    public void assignId() {
        if (id == null) {
            this.setId(IdGeneratorUtil.generate("SEQ_CCL2_HISTORIQUE_INFRA" ,"HIN-",8  ,"oracle" ));
        }
    }

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "DH_ACTION")
    private Timestamp dhAction;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_INFRA")
    private Infrastructure infrastructure;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_ETAT")
    private Etat etat;

    @Size(max = 255)
    @Column(name = "OBSERVATION")
    private String observation;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_GESTIONNAIRE")
    private Gestionnaire gestionnaire;

}