package mg.cnaps.gestion.ccl.project.controller;

import mg.cnaps.gestion.ccl.framework.jpa.core.controller.GenericController;
import mg.cnaps.gestion.ccl.project.entity.ModeleInfra;
import mg.cnaps.gestion.ccl.project.service.ModeleInfraService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/modele_infra")
public class ModeleInfraController extends GenericController<ModeleInfra , String ,ModeleInfraService > {
    public ModeleInfraController(ModeleInfraService service ) {
        super(service);
    }

    @GetMapping("/colors")
    public ResponseEntity<?> getColors(){
        return ResponseEntity.ok(this.getService().getColorExist());
    }

}