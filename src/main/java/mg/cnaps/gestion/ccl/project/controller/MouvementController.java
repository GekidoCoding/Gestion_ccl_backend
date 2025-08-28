package mg.cnaps.gestion.ccl.project.controller;

import mg.cnaps.gestion.ccl.framework.core.controller.GenericController;
import mg.cnaps.gestion.ccl.project.entity.*;
import mg.cnaps.gestion.ccl.project.entity.dto.mouvement.MouvementCalendarDto;
import mg.cnaps.gestion.ccl.project.entity.dto.mouvement.MouvementDto;
import mg.cnaps.gestion.ccl.project.service.MouvementService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/cnaps/gestion/ccl/mouvement")
public class MouvementController extends GenericController<Mouvement , String ,MouvementService > {
    public MouvementController(MouvementService service ) {
        super(service);
    }

    @GetMapping("/pagination/order")
    public ResponseEntity<Page<MouvementDto>> getAllPaginated(@RequestParam("page") int page,@RequestParam("pageSize") int pageSize){
        return ResponseEntity.ok(service.getAllPaginated(page  , pageSize)) ;
    }
    @GetMapping("/client/{id}")
    public ResponseEntity<List<Mouvement>> getByClientId(@PathVariable("id") String id){
        return ResponseEntity.ok(service.getMouvementByClient_Id(id)) ;
    }
    @GetMapping("/client/pagination/{id}")
    public ResponseEntity<Page<MouvementDto>> getByClientId(@PathVariable("id") String id , @RequestParam("page") int page, @RequestParam("pageSize") int pageSize){
        return ResponseEntity.ok(service.getMouvementByClient_Id(id , page  , pageSize)) ;
    }

    @GetMapping("/infrastructure/{id}")
    public ResponseEntity<List<Mouvement>> getByInfraId(@PathVariable("id") String id){
        return ResponseEntity.ok(service.getMouvementByInfrastructure_Id(id)) ;
    }

    @GetMapping("/infrastructure/pagination/{id}")
    public ResponseEntity<Page<MouvementDto>> getByInfraId(@PathVariable("id") String id ,@RequestParam("page") int page ,  @RequestParam("pageSize") int pageSize ){
        return ResponseEntity.ok(service.getMouvementByInfrastructure_Id(id ,  page, pageSize )) ;
    }

    @GetMapping("/search/criteria")
    public ResponseEntity<Page<MouvementDto>> getAllCriteriaSearch(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String catInfraId ,
            @RequestParam(required = false) String typeMouvementId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin
    ){
        Mouvement criteria =new  Mouvement();
        CategorieInfra catInfra = new CategorieInfra();
        catInfra.setId(catInfraId);
        ModeleInfra modeleInfra = new ModeleInfra();
        modeleInfra.setCatInfra(catInfra);
        Infrastructure infra = new  Infrastructure();
        infra.setModeleInfra(modeleInfra);

        criteria.setInfrastructure(infra);

        TypeMouvement typeMouvement = new TypeMouvement();
        typeMouvement.setId(typeMouvementId);

        criteria.setTypeMouvement(typeMouvement);

        criteria.getInfrastructure().getModeleInfra().getCatInfra().setId(catInfraId);
        criteria.getTypeMouvement().setId(typeMouvementId);

        Timestamp debutTs =(debut != null) ? Timestamp.valueOf(debut) : null;
        Timestamp finTs =(fin != null) ? Timestamp.valueOf(fin) : null;
        criteria.setPeriodeDebut(debutTs);
        criteria.setPeriodeFin(finTs);

        return ResponseEntity.ok(service.getAllCriteria(page , pageSize , criteria )) ;
    }

    @GetMapping("/calendar")
    public ResponseEntity<List<MouvementCalendarDto>> getMouvementCalendarDto(){
        return ResponseEntity.ok( service.getMouvementCalendarDto());
    }
    @GetMapping("/calendar/{infrastructureId}")
    public ResponseEntity<List<MouvementCalendarDto>> getMouvementCalendarDto(@PathVariable("infrastructureId") String infratructureId){
        return ResponseEntity.ok( service.getMouvementCalendarDtoByInfratructureId(infratructureId)) ;
    }

    @PutMapping("/accorder/{id}")
    public ResponseEntity<Mouvement> accorderMouvement(@PathVariable String id){
        return ResponseEntity.ok(service.accorderMouvement(id) );
    }
    @PutMapping("/classer/{id}")
    public ResponseEntity<Mouvement> classerMouvement(@PathVariable String id){
        return ResponseEntity.ok(service.classerMouvement(id) );
    }


}