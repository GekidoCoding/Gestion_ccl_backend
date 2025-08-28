package mg.cnaps.gestion.ccl.project.controller;

import mg.cnaps.gestion.ccl.framework.core.controller.GenericController;
import mg.cnaps.gestion.ccl.project.entity.ModeleInfra;
import mg.cnaps.gestion.ccl.project.service.ModeleInfraService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cnaps/gestion/ccl/modele_infra")
public class ModeleInfraController extends GenericController<ModeleInfra , String ,ModeleInfraService > {
    public ModeleInfraController(ModeleInfraService service ) {
        super(service);
    }
}