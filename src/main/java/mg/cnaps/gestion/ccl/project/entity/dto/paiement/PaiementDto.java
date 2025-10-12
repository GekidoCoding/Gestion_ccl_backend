package mg.cnaps.gestion.ccl.project.entity.dto.paiement;

import lombok.Getter;
import lombok.Setter;
import mg.cnaps.gestion.ccl.project.entity.Paiement;

@Getter
@Setter
public class PaiementDto {
    private double  acompteVerse;
    private double monnaie;
    private double  reste;
    private String modePaiement;
    private byte[] pdfPaiement;
    private String datePaiement;

    public PaiementDto() {
    }

    public PaiementDto(Paiement paiement) {
        this.setAcompteVerse(paiement.getAcompteVerse());
        this.setMonnaie(paiement.getMonnaie());
        this.setReste(paiement.getReste());
        this.setModePaiement(paiement.getModePaiement().getLibelle());
        this.setDatePaiement(paiement.getDatePaiement());
    }
}
