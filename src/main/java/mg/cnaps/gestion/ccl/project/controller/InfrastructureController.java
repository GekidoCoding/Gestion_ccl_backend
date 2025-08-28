package mg.cnaps.gestion.ccl.project.controller;

import mg.cnaps.gestion.ccl.framework.core.controller.GenericController;
import mg.cnaps.gestion.ccl.project.entity.CategorieInfra;
import mg.cnaps.gestion.ccl.project.entity.Infrastructure;
import mg.cnaps.gestion.ccl.project.entity.Localisation;
import mg.cnaps.gestion.ccl.project.entity.ModeleInfra;
import mg.cnaps.gestion.ccl.project.service.InfrastructureService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/cnaps/gestion/ccl/infrastructure")
public class InfrastructureController extends GenericController<Infrastructure, String ,InfrastructureService > {

    public InfrastructureController(InfrastructureService service  ) {
        super(service);
    }

    @DeleteMapping("/delete/observation/{id}/{observation}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id , @PathVariable("observation") String observation) throws Exception {
        getService().delete(id , observation);
        return ResponseEntity.noContent().build();
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