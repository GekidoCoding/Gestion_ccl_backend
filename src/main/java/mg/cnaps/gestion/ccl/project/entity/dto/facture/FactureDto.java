package mg.cnaps.gestion.ccl.project.entity.dto.facture;


import lombok.Getter;
import lombok.Setter;
import mg.cnaps.gestion.ccl.project.entity.Etat;
import mg.cnaps.gestion.ccl.project.entity.Facture;
import mg.cnaps.gestion.ccl.project.entity.Gestionnaire;
import mg.cnaps.gestion.ccl.project.entity.Mouvement;

import java.sql.Timestamp;

@Getter
@Setter
public class FactureDto {

    private String id;

    private String dhCreation;

    private Gestionnaire gestionnaire;

    private Mouvement mouvement;

    private Etat etat;

    private String refFacture;

    private Double montantTotal;

    private Double remise;

    private Double totalDu;

    private Double reste;
    private Double totalAcompte;

    public FactureDto() {
    }

    public FactureDto(Facture facture) {
        this.id = facture.getId();
        this.dhCreation=facture.getDhCreation();
        this.gestionnaire=facture.getGestionnaire();
        this.mouvement=facture.getMouvement();
        this.etat=facture.getEtat();
        this.refFacture=facture.getRefFacture();
        this.montantTotal=facture.getMontantTotal();
        this.remise=facture.getRemise();
        this.totalDu=facture.getTotalDu();


    }
}
