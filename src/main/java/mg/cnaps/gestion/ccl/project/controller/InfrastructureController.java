package mg.cnaps.gestion.ccl.project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import mg.cnaps.gestion.ccl.framework.jpa.core.controller.GenericController;
import mg.cnaps.gestion.ccl.framework.general.error.ErrorResponse;
import mg.cnaps.gestion.ccl.project.entity.CategorieInfra;
import mg.cnaps.gestion.ccl.project.entity.Infrastructure;
import mg.cnaps.gestion.ccl.project.entity.Localisation;
import mg.cnaps.gestion.ccl.project.entity.ModeleInfra;
import mg.cnaps.gestion.ccl.project.service.InfrastructureService;
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
@RequestMapping("/infrastructure")
public class InfrastructureController extends GenericController<Infrastructure, String ,InfrastructureService > {

    public InfrastructureController(InfrastructureService service  ) {
        super(service);
    }

    @DeleteMapping("/delete/observation/{id}/{observation}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id , @PathVariable("observation") String observation) throws Exception {
        getService().delete(id , observation);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/check/similarity")
    public ResponseEntity<?> checkSimilarity(@RequestBody Infrastructure infrastructure) {
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(getService().checkSimilarity(infrastructure));
        } catch (Exception e) {
            e.printStackTrace();
            ErrorResponse error = new ErrorResponse(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping("/create/new")
    public ResponseEntity<?> create(@RequestBody Infrastructure dto , HttpServletRequest request)  {
        try{
            String matricule = request.getAttribute("matricule").toString();

            return ResponseEntity.status(HttpStatus.CREATED).body(getService().save(dto ,matricule));
        } catch (Exception e) {
            e.printStackTrace();
            ErrorResponse error = new ErrorResponse(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }


    }
    @PutMapping("/update/new/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody @Valid Infrastructure dto , HttpServletRequest request) throws JsonProcessingException {
        try{
            String matricule = request.getAttribute("matricule").toString();
            return ResponseEntity.status(HttpStatus.CREATED).body(getService().update(dto,id ,matricule));
        } catch (Exception e) {
            e.printStackTrace();
            ErrorResponse error = new ErrorResponse(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/search/criteria")
    public Page<Infrastructure> searchInfrastructures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String numero,
            @RequestParam(required = false) List<String> localisationIds,
            @RequestParam(required = false) List<String> modeleIds,
            @RequestParam(required = false) String categorieInfraId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin
    ) {


        Infrastructure criteria = new Infrastructure();
        if (nom != null && !nom.isEmpty()) {
            criteria.setNom(nom);
        }
        if (numero != null && !numero.isEmpty()) {
            criteria.setNumero(numero);
        }
        Localisation[] localisations = new Localisation[0];

        if (localisationIds != null && !localisationIds.isEmpty()) {
            localisations = localisationIds.stream().map(id -> {
                Localisation loc = new Localisation();
                loc.setId(id);
                return loc;
            }).toArray(Localisation[]::new);
        }


        ModeleInfra[] modelesArray = null;
        if (modeleIds != null && !modeleIds.isEmpty()) {
            modelesArray = modeleIds.stream().map(id -> {
                ModeleInfra m = new ModeleInfra();
                m.setId(id);
                return m;
            }).toArray(ModeleInfra[]::new);
        }

        CategorieInfra catInfra = null;
        if (categorieInfraId != null && !categorieInfraId.isEmpty()) {
            catInfra = new CategorieInfra();
            catInfra.setId(categorieInfraId);
        }

        Timestamp tsDebut = (debut != null) ? Timestamp.valueOf(debut) : null;
        Timestamp tsFin = (fin != null) ? Timestamp.valueOf(fin) : null;

        return service.findByCriteriaAll(page, pageSize, modelesArray,localisations, criteria, catInfra, tsDebut, tsFin);
    }
}