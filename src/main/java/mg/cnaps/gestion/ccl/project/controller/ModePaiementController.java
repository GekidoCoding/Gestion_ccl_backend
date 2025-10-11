package mg.cnaps.gestion.ccl.project.controller;

import mg.cnaps.gestion.ccl.framework.jpa.core.controller.GenericController;
import mg.cnaps.gestion.ccl.project.entity.ModePaiement;
import mg.cnaps.gestion.ccl.project.service.ModePaiementService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mode_paiement")
public class ModePaiementController extends GenericController<ModePaiement , String ,ModePaiementService > {
    public ModePaiementController(ModePaiementService service ) {
        super(service);
    }
}

