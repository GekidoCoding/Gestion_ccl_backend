package mg.cnaps.gestion.ccl.project.entity.existant;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;


@Getter
@Setter
@Entity
@Table(name = "VIEW_ANNUAIRE_ELECTRONIQUE" , schema = "INTRANET")
public class Agent {
    @Id
    @Column(name = "MATRICULE")
    private String matricule;

    @Column(name = "NOM")
    private String nom;

    @Column(name = "PRENOMS")
    private String prenoms;

    @Column(name = "CODE_DIRECTION")
    private String codeDirection;

    @Column(name = "LIBELLE_DIRECTION")
    private String libelleDirection;

    @Column(name = "CODE_SERVICE")
    private String codeService;

    @Column(name = "LIBELLE_SERVICE")
    private String libelleService;

    @Column(name = "MAIL")
    private String mail;

    @Column(name="DATE_ENTREE")
    private Date dateEntree;



}