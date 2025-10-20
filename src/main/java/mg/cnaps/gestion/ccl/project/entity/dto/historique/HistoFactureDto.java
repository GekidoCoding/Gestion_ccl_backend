package mg.cnaps.gestion.ccl.project.entity.dto.historique;

import lombok.Getter;
import lombok.Setter;
import mg.cnaps.gestion.ccl.project.entity.Facture;
import mg.cnaps.gestion.ccl.project.entity.HistoFacture;
import mg.cnaps.gestion.ccl.project.entity.dto.paiement.PaiementDto;

import java.util.List;


@Getter
@Setter
public class HistoFactureDto {
    private String ref;
    private Double totalDu;
    private Double remise;
    private String etatFacture;
    private  String matricule;
    private byte[] pdfProforma;
    private List<PaiementDto> paiements;
    private String dhCreation;

    public HistoFactureDto() {
    }

    public HistoFactureDto(HistoFacture hf) {
        this.setRef(hf.getRefFacture());
        this.setRemise(hf.getRemise());
        this.setEtatFacture(hf.getEtat().getEtat());
        this.setMatricule(hf.getGestionnaire().getMatriculeGestionnaire());
        this.setDhCreation(hf.getDhAction());
        this.setTotalDu(hf.getTotalDu());

    }
}
