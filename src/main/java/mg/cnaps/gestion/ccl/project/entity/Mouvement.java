package mg.cnaps.gestion.ccl.project.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import mg.cnaps.gestion.ccl.framework.core.generator.IdGeneratorUtil;
import mg.cnaps.gestion.ccl.project.util.TimestampUtil;

import javax.persistence.*;
import java.sql.Timestamp;
@Data
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_INFRASTRUCTURE")
    private Infrastructure infrastructure;

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



}