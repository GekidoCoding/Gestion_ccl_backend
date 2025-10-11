package mg.cnaps.gestion.ccl.project.controller;

import mg.cnaps.gestion.ccl.framework.jpa.core.controller.GenericController;
import mg.cnaps.gestion.ccl.project.config.CclPropertyService;
import mg.cnaps.gestion.ccl.project.entity.Etat;
import mg.cnaps.gestion.ccl.project.repository.EtatRepo;
import mg.cnaps.gestion.ccl.project.service.EtatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/etat")
public class EtatController extends GenericController<Etat , String ,EtatService > {
    private final EtatRepo etatRepo;
    public EtatController(EtatService service, EtatRepo etatRepo, CclPropertyService cclPropertyService) {
        super(service);
        this.etatRepo = etatRepo;
        this.cclPropertyService = cclPropertyService;
    }
    private final CclPropertyService cclPropertyService;

    @GetMapping("/proforma")
    public ResponseEntity<Etat> getEtatProforma(){
        return ResponseEntity.ok(etatRepo.getEtatByCode(cclPropertyService.getProformaCode() ));
    }
    @GetMapping("/reelle")
    public ResponseEntity<Etat> getEtatReelle(){
        return ResponseEntity.ok(etatRepo.getEtatByCode(cclPropertyService.getReelleCode() ));
    }
    @GetMapping("/facture")
    public ResponseEntity<List<Etat>> getEtatsFacture(){
        return ResponseEntity.ok((service.getEtatsFacture() ));
    }
    @GetMapping("/autre")
    public ResponseEntity<List<Etat>> getEtatsAutre(){
        return ResponseEntity.ok((service.getEtatsAutre() ));
    }
}
