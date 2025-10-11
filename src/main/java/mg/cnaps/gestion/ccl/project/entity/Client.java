package mg.cnaps.gestion.ccl.project.entity;

import lombok.Getter;
import lombok.Setter;
import mg.cnaps.gestion.ccl.framework.check.annotation.CheckField;
import mg.cnaps.gestion.ccl.framework.check.annotation.Checkable;
import mg.cnaps.gestion.ccl.framework.jpa.core.generator.IdGeneratorUtil;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Timestamp;


@Checkable(similarityDegree = 70.0)
@Getter
@Setter
@Entity
@Table(name = "CCL2_CLIENT")
public class Client {
    @Id
    @Column(name = "ID")
    private String id;
    @PrePersist
    public void assignId() {
        if (id == null) {
            this.setId(IdGeneratorUtil.generate("SEQ_CCL2_CLIENT" ,"CLT-",8  ,"oracle" ));
        }
    }

    @CheckField
    @Size(max = 255)
    @Column(name = "DESIGNATION_CLIENT")
    private String designationClient;


    @Size(max = 255)
    @Column(name = "FONCTION")
    private String fonction;


    @Size(max = 255)
    @Column(name = "EMAIL")
    private String email;

    @Size(max = 255)
    @Column(name = "CIN")
    private String cin;

    @Size(max = 255)
    @Column(name = "ADRESSE")
    private String adresse;

    @CheckField
    @Size(max = 255)
    @Column(name = "CONTACTS")
    private String contacts;

    @Size(max = 255)
    @Column(name = "DATE_INSERT")
    private Timestamp dateInsert;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_ETAT" )
    private Etat etat;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_TYPE_CLIENT")
    private TypeClient typeClient;

    public Client() {
    }


}