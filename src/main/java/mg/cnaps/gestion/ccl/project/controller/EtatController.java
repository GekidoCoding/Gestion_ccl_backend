package mg.cnaps.gestion.ccl.project.controller;

import mg.cnaps.gestion.ccl.framework.core.controller.GenericController;
import mg.cnaps.gestion.ccl.project.entity.Etat;
import mg.cnaps.gestion.ccl.project.service.EtatService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cnaps/gestion/ccl/etat")
public class EtatController extends GenericController<Etat , String ,EtatService > {
    public EtatController(EtatService service ) {
        super(service);
    }
}
