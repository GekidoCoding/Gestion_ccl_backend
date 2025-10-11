package mg.cnaps.gestion.ccl.project.controller;

import mg.cnaps.gestion.ccl.framework.jpa.core.controller.GenericController;
import mg.cnaps.gestion.ccl.project.entity.InfraTarif;
import mg.cnaps.gestion.ccl.project.service.InfraTarifService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/infra_tarif")
public class InfraTarifController extends GenericController<InfraTarif , String ,InfraTarifService > {
    public InfraTarifController(InfraTarifService service ) {
        super(service);
    }
}