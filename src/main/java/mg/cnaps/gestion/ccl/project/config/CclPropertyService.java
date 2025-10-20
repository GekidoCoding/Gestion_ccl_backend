package mg.cnaps.gestion.ccl.project.config;

import mg.cnaps.gestion.ccl.project.util.CclProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CclPropertyService {
    private final CclProperty cclPropertyService;

    @Autowired
    public CclPropertyService(CclProperty cclPropertyService) {
        this.cclPropertyService = cclPropertyService;
    }

    public Integer getInactifCode(){
        return cclPropertyService.getInactifCode();
    }

    public Integer getProformaCode(){
        return cclPropertyService.getProformaCode();
    }
    public Integer getReelleCode(){
        return cclPropertyService.getReelleCode();
    }
    public Integer getPayeCode(){
        return cclPropertyService.getPayeCode();
    }

    public Integer getActifCode(){
        return cclPropertyService.getActifCode();
    }

    public String getPersonneId(){
        return cclPropertyService.getPersonneId();
    }
    public String getOccupationId(){
        return cclPropertyService.getOccupationId();
    }
    public String getRenseignementId(){
        return cclPropertyService.getRenseignementId();
    }
    public String getClassementId(){
        return cclPropertyService.getClassementId();
    }
    public double getFactureRemise(){
        return cclPropertyService.getFactureRemise();
    }
    public double getAvancePaiement(){
        return cclPropertyService.getAvancePaiement();
    }
    public int getDelaiPaiementJour(){
        return cclPropertyService.getDelaiPaiementJour();
    }
    public int getPreparationSignalementJour(){
        return cclPropertyService.getPreparationSignalementJour();
    }
    public String getMyMail(){
        return cclPropertyService.getMyMail();
    }

    public double getHeurePaiementRemise(){
        return cclPropertyService.getHeurePaiementRemise();
    }
    public String getFrequenceDefaultId(){
        return cclPropertyService.getFrequenceDefaultId();
    }
    public String getReservationId(){
        return cclPropertyService.getReservationId();
    }
    public String getFrequenceJourId(){
        return cclPropertyService.getFrequenceJourId();
    }
    public String getFrequenceHeureId(){
        return cclPropertyService.getFrequenceHeureId();
    }
    public String getFrequenceNuitId(){
        return cclPropertyService.getFrequenceNuitId();
    }
    public String getFrequenceMoisId(){
        return cclPropertyService.getFrequenceMoisId();
    }
    public String getTypeNotificationDangerId(){
        return cclPropertyService.getTypeNotificationDangerId();
    }
    public String getTypeNotificationSignalementId(){
        return cclPropertyService.getTypeNotificationSignalementId();
    }

    public String getScheduleHour(){
        return cclPropertyService.getScheduleHour();
    }
    public String getFactureProformaExportPath(){
        return cclPropertyService.getFactureProformaExportPath();
    }
    public String getFactureReelleExportPath(){
        return cclPropertyService.getFactureReelleExportPath();
    }
    public String getContratExportPath(){
        return cclPropertyService.getContratExportPath();

    }

}
