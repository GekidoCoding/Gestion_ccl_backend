package mg.cnaps.gestion.ccl.project.controller;

import mg.cnaps.gestion.ccl.framework.jpa.core.controller.GenericController;
import mg.cnaps.gestion.ccl.project.entity.HistoFacture;
import mg.cnaps.gestion.ccl.project.service.HistoFactureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/histo_facture")
public class HistoFactureController extends GenericController<HistoFacture, String , HistoFactureService> {


    public HistoFactureController(HistoFactureService service) {
        super(service);
    }
    @GetMapping("/facture/{id}")
    public ResponseEntity<List<HistoFacture>> getHistoFactureByFacture_Id(@PathVariable String id) {
        return ResponseEntity.ok(service.getHistoByFactureId(id));
    }

}
