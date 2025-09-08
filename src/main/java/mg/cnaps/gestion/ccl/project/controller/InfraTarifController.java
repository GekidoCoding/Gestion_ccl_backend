package mg.cnaps.gestion.ccl.project.controller;

import mg.cnaps.gestion.ccl.framework.core.controller.GenericController;
import mg.cnaps.gestion.ccl.project.entity.InfraTarif;
import mg.cnaps.gestion.ccl.project.service.InfraTarifService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cnaps/gestion/ccl/infra_tarif")
public class InfraTarifController extends GenericController<InfraTarif , String ,InfraTarifService > {
    public InfraTarifController(InfraTarifService service ) {
        super(service);
    }
}