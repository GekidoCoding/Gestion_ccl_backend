package mg.cnaps.gestion.ccl.project.controller;

import mg.cnaps.gestion.ccl.framework.core.controller.GenericController;
import mg.cnaps.gestion.ccl.project.entity.TypeMouvement;
import mg.cnaps.gestion.ccl.project.service.TypeMouvementService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cnaps/gestion/ccl/type_mouvement")
public class TypeMouvementController extends GenericController<TypeMouvement , String ,TypeMouvementService > {
    public TypeMouvementController(TypeMouvementService service ) {
        super(service);
    }
}
