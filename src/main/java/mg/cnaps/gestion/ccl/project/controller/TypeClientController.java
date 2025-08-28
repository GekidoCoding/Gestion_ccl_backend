package mg.cnaps.gestion.ccl.project.controller;

import mg.cnaps.gestion.ccl.framework.core.controller.GenericController;
import mg.cnaps.gestion.ccl.project.config.CclPropertyService;
import mg.cnaps.gestion.ccl.project.entity.TypeClient;
import mg.cnaps.gestion.ccl.project.service.TypeClientService;
import mg.cnaps.gestion.ccl.project.util.CclProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/cnaps/gestion/ccl/type_client")
public class TypeClientController extends GenericController<TypeClient , String ,TypeClientService > {
    private final CclPropertyService servicePropertyService;
    public TypeClientController(TypeClientService service , CclPropertyService servicePropertyService) {
        super(service);
        this.servicePropertyService = servicePropertyService;
    }

    @GetMapping("/personne/id")
    public ResponseEntity<String> getPersonneId() {
        System.out.println("getPersonneId:"+servicePropertyService.getPersonneId());
        return ResponseEntity.ok(servicePropertyService.getPersonneId());
    }

    @GetMapping("/entreprise/id")
    public ResponseEntity<String> getEntrepriseId() {
        return ResponseEntity.ok(new CclProperty().getEntrepriseId());
    }
}