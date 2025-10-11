package mg.cnaps.gestion.ccl.project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import mg.cnaps.gestion.ccl.framework.jpa.core.controller.GenericController;
import mg.cnaps.gestion.ccl.framework.general.error.ErrorResponse;
import mg.cnaps.gestion.ccl.project.entity.*;
import mg.cnaps.gestion.ccl.project.entity.dto.mouvement.MouvementCalendarDto;
import mg.cnaps.gestion.ccl.project.entity.dto.mouvement.MouvementDto;
import mg.cnaps.gestion.ccl.project.service.MouvementService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/mouvement")
public class MouvementController extends GenericController<Mouvement , String ,MouvementService > {
    public MouvementController(MouvementService service ) {
        super(service);
    }


    @PutMapping("/update/new/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody @Valid Mouvement dto , HttpServletRequest request) throws JsonProcessingException {
        try {
            String matricule=request.getAttribute("matricule").toString();
            return ResponseEntity.ok(getService().update(dto, id , matricule));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }

    }
    @PostMapping("/create/new")
    public ResponseEntity<?> create(@RequestBody Mouvement dto , HttpServletRequest request)  {
        try {
            String matricule=request.getAttribute("matricule").toString();
            return ResponseEntity.ok(getService().save(dto , matricule));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
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
            @RequestParam(required = false) String id ,
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

//        ajouter infrastructure
        MouvementInfra mouvementInfra = new MouvementInfra();
        criteria.getMouvementInfras().add(mouvementInfra);

        TypeMouvement typeMouvement = new TypeMouvement();
        typeMouvement.setId(typeMouvementId);

        criteria.setTypeMouvement(typeMouvement);

        criteria.getTypeMouvement().setId(typeMouvementId);
        criteria.setId(id);
        Timestamp debutTs =(debut != null) ? Timestamp.valueOf(debut) : null;
        Timestamp finTs =(fin != null) ? Timestamp.valueOf(fin) : null;
        criteria.setPeriodeDebut(debutTs);
        criteria.setPeriodeFin(finTs);

        return ResponseEntity.ok(service.getAllCriteria(page , pageSize , criteria , catInfraId )) ;
    }

    @GetMapping("/calendar")
    public ResponseEntity<List<MouvementCalendarDto>> getMouvementCalendarDto(){
        return ResponseEntity.ok( service.getMouvementCalendarDto());
    }
    @GetMapping("/calendar/{infrastructureId}")
    public ResponseEntity<List<MouvementCalendarDto>> getMouvementCalendarDto(
            @PathVariable("infrastructureId") String infratructureId

    ){
        return ResponseEntity.ok( service.getMouvementCalendarDtoByInfratructureId(infratructureId)) ;
    }
    @GetMapping("/calendar/criteria")
    public ResponseEntity<List<MouvementCalendarDto>> getMouvementCalendarDtoCriteria(
            @RequestParam(value = "infrastructureId" ,required = false) String infratructureId ,
            @RequestParam(value = "modelesIds" , required = false) String[] modeles

    ){
        return ResponseEntity.ok( service.getMouvementCalendarDtoByCriteria(infratructureId , modeles)) ;
    }

    @PutMapping("/accorder/{id}")
    public ResponseEntity<Mouvement> accorderMouvement(@PathVariable String id){
        return ResponseEntity.ok(service.accorderMouvement(id) );
    }
    @PutMapping("/classer/{id}")
    public ResponseEntity<?> classerMouvement(@PathVariable String id , HttpServletRequest request){
        try{
            String matricule = request.getAttribute("matricule").toString();
            return ResponseEntity.ok(service.classerMouvement(id , matricule) );
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/verify/conflict")
    public ResponseEntity<List<MouvementDto>> getConflict(@RequestBody Mouvement mouvement){
        return ResponseEntity.ok(service.getListMouvementConflict(mouvement));
    }





}