package mg.cnaps.gestion.ccl.project.controller;

import mg.cnaps.gestion.ccl.framework.core.controller.GenericController;
import mg.cnaps.gestion.ccl.project.entity.HistoriqueInfra;
import mg.cnaps.gestion.ccl.project.service.HistoriqueInfraService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cnaps/gestion/ccl/historique_infra")
public class HistoriqueInfraController extends GenericController<HistoriqueInfra , String ,HistoriqueInfraService > {
    public HistoriqueInfraController(HistoriqueInfraService service ) {
        super(service);
    }

    @GetMapping("/infrastructure/{id}")
    public ResponseEntity<List<HistoriqueInfra>>  findHistoriqueInfraByMouvement_Id(@PathVariable("id") String id){
        return ResponseEntity.ok(service.findHistoriqueInfraByInfrastructure_Id(id)) ;
    }
}
