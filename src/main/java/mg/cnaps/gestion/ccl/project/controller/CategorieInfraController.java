package mg.cnaps.gestion.ccl.project.controller;

import mg.cnaps.gestion.ccl.framework.jpa.core.controller.GenericController;
import mg.cnaps.gestion.ccl.project.entity.CategorieInfra;
import mg.cnaps.gestion.ccl.project.service.CategorieInfraService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categorie_infra")
public class CategorieInfraController extends GenericController<CategorieInfra , String ,CategorieInfraService > {
    public CategorieInfraController(CategorieInfraService service ) {
        super(service);
    }
}