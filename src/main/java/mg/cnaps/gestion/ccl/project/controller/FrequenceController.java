package mg.cnaps.gestion.ccl.project.controller;

import mg.cnaps.gestion.ccl.framework.core.controller.GenericController;
import mg.cnaps.gestion.ccl.project.config.CclPropertyService;
import mg.cnaps.gestion.ccl.project.entity.Frequence;
import mg.cnaps.gestion.ccl.project.service.FrequenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cnaps/gestion/ccl/frequence")
public class FrequenceController extends GenericController<Frequence , String ,FrequenceService > {
    private final CclPropertyService cclPropertyService;

    public FrequenceController(FrequenceService service, CclPropertyService cclPropertyService) {
        super(service);
        this.cclPropertyService = cclPropertyService;
    }
    @GetMapping("/defaut")
    public ResponseEntity<Frequence> getFrequenceDefault(){
        return ResponseEntity.ok(service.findById(cclPropertyService.getFrequenceDefaultId()));
    }
}
