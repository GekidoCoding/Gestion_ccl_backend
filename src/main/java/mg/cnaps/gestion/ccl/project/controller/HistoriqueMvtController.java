package mg.cnaps.gestion.ccl.project.controller;

import mg.cnaps.gestion.ccl.framework.core.controller.GenericController;
import mg.cnaps.gestion.ccl.project.entity.HistoriqueMvt;
import mg.cnaps.gestion.ccl.project.service.HistoriqueMvtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/cnaps/gestion/ccl/historique_mvt")
public class HistoriqueMvtController extends GenericController<HistoriqueMvt , String ,HistoriqueMvtService > {
    public HistoriqueMvtController(HistoriqueMvtService service ) {
        super(service);
    }

    @GetMapping("/mouvement/{id}")
    public ResponseEntity<List<HistoriqueMvt>>  findHistoriqueMvtByMouvement_Id(@PathVariable("id") String id){
        return ResponseEntity.ok(service.findHistoriqueMvtByMouvement_Id(id)) ;
    }

    @GetMapping("/mouvement/dashboard")
    public ResponseEntity<List<HistoriqueMvt>>  findHistoriqueMvtByMouvement_Id(@RequestParam(value = "id" , required = false) Integer year  ,
                                                                                @RequestParam(value="date1" , required = false) Date date1 ,
                                                                                @RequestParam(value="date2" ,  required = false) Date date2,
                                                                                @RequestParam(value="categorieInfraId" ,  required = false) String categorieInfraId,
                                                                                @RequestParam(value="typeMouvementId" ,  required = false) String typeMouvementId,
                                                                                @RequestParam(value = "modelesIds" , required = false) String[] modelesIds
    ){
        return ResponseEntity.ok(service.findAllCriteria(date1 ,date2 , year , categorieInfraId , typeMouvementId , modelesIds)) ;
    }
}
