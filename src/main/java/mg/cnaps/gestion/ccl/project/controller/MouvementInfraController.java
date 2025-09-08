package mg.cnaps.gestion.ccl.project.controller;

import mg.cnaps.gestion.ccl.framework.core.controller.GenericController;
import mg.cnaps.gestion.ccl.project.entity.MouvementInfra;
import mg.cnaps.gestion.ccl.project.service.MouvementInfraService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cnaps/gestion/ccl/mouvement_infra")
public class MouvementInfraController extends GenericController<MouvementInfra , String ,MouvementInfraService > {
    public MouvementInfraController(MouvementInfraService service ) {
        super(service);
    }
}