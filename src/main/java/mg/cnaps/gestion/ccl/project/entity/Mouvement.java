package mg.cnaps.gestion.ccl.project.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import mg.cnaps.gestion.ccl.framework.jpa.core.generator.IdGeneratorUtil;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "CCL2_MOUVEMENT")
public class Mouvement {
    @Id
    @Column(name = "ID")
    private String id;
    @PrePersist
    public void assignId() {
        if (id == null) {
            this.setId(IdGeneratorUtil.generate("SEQ_CCL2_MOUVEMENT" ,"MVT-",8  ,"oracle" ));
        }
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_TYPE_MOUVEMENT")
    private TypeMouvement typeMouvement;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_CLIENT")
    private Client client;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "PERIODE_DEBUT")
    private Timestamp periodeDebut;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "PERIODE_FIN")
    private Timestamp periodeFin;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "DH_MOUVEMENT")
    private Timestamp dhMouvement;

    @Column(name = "NOMBRE")
    private Integer nombre ;

    @OneToMany(mappedBy = "mouvement", orphanRemoval = true)
    @JsonManagedReference
    private List<MouvementInfra> mouvementInfras;

    @Column(name = "OBSERVATION")
    private Integer observation ;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_ETAT")
    private Etat etat;



}