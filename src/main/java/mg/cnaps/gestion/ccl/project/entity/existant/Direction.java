package mg.cnaps.gestion.ccl.project.entity.existant;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Getter
@Setter
@Entity
@Table(name = "DRH_DIRECTION" ,  schema = "DRH")
public class Direction {
    @Id
    @Column(name = "CODE_DIRECTION")
    private String codeDirection;

    @Column(name = "LIBELLE_DIRECTION")
    private String libelleDirection;


}
