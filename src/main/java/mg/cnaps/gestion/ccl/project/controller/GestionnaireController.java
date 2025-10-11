package mg.cnaps.gestion.ccl.project.controller;

import mg.cnaps.gestion.ccl.framework.jpa.core.controller.GenericController;
import mg.cnaps.gestion.ccl.project.entity.Gestionnaire;
import mg.cnaps.gestion.ccl.project.service.GestionnaireService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gestionnaire")
public class GestionnaireController extends GenericController<Gestionnaire , String ,GestionnaireService > {
    public GestionnaireController(GestionnaireService service ) {
        super(service);
    }


}