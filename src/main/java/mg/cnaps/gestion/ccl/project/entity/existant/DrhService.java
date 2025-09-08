package mg.cnaps.gestion.ccl.project.entity.existant;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Entity
@Table(name = "DRH_SERVICE" , schema = "DRH")
public class DrhService {
    @Id
    @Column(name = "CODE_SERVICE")
    private String codeService;

    @Column(name = "LIBELLE_SERVICE")
    private String libelleService;


}