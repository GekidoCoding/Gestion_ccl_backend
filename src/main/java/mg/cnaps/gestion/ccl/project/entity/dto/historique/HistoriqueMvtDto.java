package mg.cnaps.gestion.ccl.project.entity.dto.historique;

import lombok.Getter;
import lombok.Setter;
import mg.cnaps.gestion.ccl.framework.excel.annotation.ExcelColorField;
import mg.cnaps.gestion.ccl.framework.excel.annotation.ExcelColumn;
import mg.cnaps.gestion.ccl.project.entity.HistoriqueMvt;
import mg.cnaps.gestion.ccl.project.repository.AgentRepo;
import mg.cnaps.gestion.ccl.project.repository.ClientRepo;

@Getter
@Setter
public class HistoriqueMvtDto {
    @ExcelColumn("Mouvement ID")
    private String mouvementId;

    @ExcelColumn("Client")
    private String client ;


    @ExcelColumn("contact(s) client")
    private String contact;

    @ExcelColumn(
            value = "Type de mouvement",
            colors = {
                    "Renseignement:VERT",
                    "Occupation:ROUGE",
                    "Réservation:JAUNE",
                    "Classement:GRIS"
            }
    )
    @ExcelColorField
    private String type;

    @ExcelColumn("Date d'action ")
    private String dhMouvement;


    @ExcelColumn("Matricule")
    private String matricule;

//
//    public HistoriqueMvtDto() {
//    }

    public HistoriqueMvtDto(HistoriqueMvt histo ) {
        this.setMatricule(histo.getGestionnaire().getMatriculeGestionnaire());
        this.setClient(histo.getMouvement().getClient().getDesignationClient());
        this.setMouvementId(histo.getMouvement().getId());
        this.setType(histo.getTypeMouvement().getNom());
        this.setContact(histo.getMouvement().getClient().getContacts());
        this.setDhMouvement( histo.getDhAction());
    }


}
