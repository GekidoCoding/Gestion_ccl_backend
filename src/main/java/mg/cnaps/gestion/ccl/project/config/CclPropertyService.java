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
        System.out.println("classementId:"+cclPropertyService.getClassementId());
        return cclPropertyService.getClassementId();
    }
    public double getFactureRemise(){
        return cclPropertyService.getFactureRemise();
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

}
