package mg.cnaps.gestion.ccl.project.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "CCL2_ADMIN")
public class Admin {
    @Id
    @Size(max = 255)
    @Column(name = "ID", nullable = false)
    private String id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_GESTIONNAIRE", nullable = false)
    private Gestionnaire gestionnaire;


}