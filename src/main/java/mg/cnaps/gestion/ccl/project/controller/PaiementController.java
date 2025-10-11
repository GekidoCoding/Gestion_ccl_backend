package mg.cnaps.gestion.ccl.project.controller;

import mg.cnaps.gestion.ccl.framework.jpa.core.controller.GenericController;
import mg.cnaps.gestion.ccl.framework.general.error.ErrorResponse;
import mg.cnaps.gestion.ccl.project.entity.Paiement;
import mg.cnaps.gestion.ccl.project.service.PaiementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/paiement")
public class PaiementController extends GenericController<Paiement , String ,PaiementService > {
    public PaiementController(PaiementService service) {
        super(service);
    }

    @Override
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Paiement dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(getService().save(dto));

        } catch (Exception e) {
            e.printStackTrace();
            ErrorResponse error = new ErrorResponse(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }

    }
    @GetMapping("/facture/{factureId}")
    public ResponseEntity<?> getByFactureId(@PathVariable("factureId") String factureId) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(this.service.findPaiementByFacture_Id(factureId));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }

    }
}