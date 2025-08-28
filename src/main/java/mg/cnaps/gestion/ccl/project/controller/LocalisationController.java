package mg.cnaps.gestion.ccl.project.controller;

import mg.cnaps.gestion.ccl.framework.core.controller.GenericController;
import mg.cnaps.gestion.ccl.project.entity.Localisation;
import mg.cnaps.gestion.ccl.project.service.LocalisationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cnaps/gestion/ccl/localisation")
public class LocalisationController extends GenericController<Localisation , String ,LocalisationService > {
    public LocalisationController(LocalisationService service ) {
        super(service);
    }
}