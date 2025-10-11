package mg.cnaps.gestion.ccl.project.entity;

import lombok.Getter;
import lombok.Setter;
import mg.cnaps.gestion.ccl.framework.jpa.core.generator.IdGeneratorUtil;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "CCL2_NOTIFICATION")
public class Notification {
    @Id
    @Column(name = "ID")
    private String id;
    @PrePersist
    public void assignId() {
        if (id == null) {
            this.setId(IdGeneratorUtil.generate("SEQ_CCL2_NOTIFICATION" ,"NTF-",8  ,"oracle" ));
        }
    }

    @Size(max = 255)
    @Column(name = "MESSAGE")
    private String message;

    @Size(max = 255)
    @Column(name = "DH_CREATION")
    private Timestamp dhCreation;

    @Column(name = "IS_READ")
    private boolean isRead;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_MOUVEMENT")
    private Mouvement mouvement;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_TYPE_NOTIFICATION")
    private TypeNotification typeNotification;

}