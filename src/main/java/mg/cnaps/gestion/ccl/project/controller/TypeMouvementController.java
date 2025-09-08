package mg.cnaps.gestion.ccl.project.controller;

import mg.cnaps.gestion.ccl.framework.core.controller.GenericController;
import mg.cnaps.gestion.ccl.project.config.CclPropertyService;
import mg.cnaps.gestion.ccl.project.entity.TypeMouvement;
import mg.cnaps.gestion.ccl.project.service.TypeMouvementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cnaps/gestion/ccl/type_mouvement")
public class TypeMouvementController extends GenericController<TypeMouvement , String ,TypeMouvementService > {
    private final CclPropertyService cclPropertyService;
    public TypeMouvementController(TypeMouvementService service, CclPropertyService cclPropertyService) {
        super(service);
        this.cclPropertyService = cclPropertyService;
    }

    @GetMapping("/adding/trier")
    public ResponseEntity<List<TypeMouvement>> getTypeMouvementAddingMouvement(){
        return ResponseEntity.ok(service.getTypeMouvementAddingMouvement());
    }
    @GetMapping("/reservation/id")
    public ResponseEntity<TypeMouvement> getReservationId(){
        TypeMouvement typeMouvement = new TypeMouvement();
        typeMouvement.setId(cclPropertyService.getReservationId());
        return ResponseEntity.ok(typeMouvement);
    }
}
