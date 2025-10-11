package mg.cnaps.gestion.ccl.project.controller.config;

import lombok.RequiredArgsConstructor;
import mg.cnaps.gestion.ccl.project.config.CclPropertyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/config")
public class ConfigController {
    private final CclPropertyService cclPropertyService;

    @GetMapping("/type_reservation/id")
    public ResponseEntity<String> getReservationId() {
        return ResponseEntity.ok(cclPropertyService.getReservationId());
    }
    @GetMapping("/type_occupation/id")
    public ResponseEntity<String> getOccupationId() {
        return ResponseEntity.ok(cclPropertyService.getOccupationId());
    }

    @GetMapping("/type_renseignement/id")
    public ResponseEntity<String> getRenseignementId() {
        return ResponseEntity.ok(cclPropertyService.getRenseignementId());
    }
    @GetMapping("/type_classement/id")
    public ResponseEntity<String> getClassementId() {
        return ResponseEntity.ok(cclPropertyService.getClassementId());
    }

    @GetMapping("/etat_code/reelle")
    public ResponseEntity<Integer> getCodeReelle() {
        return ResponseEntity.ok(cclPropertyService.getReelleCode());
    }


    @GetMapping("/etat_code/payee")
    public ResponseEntity<Integer> getCodePayee() {
        return ResponseEntity.ok(cclPropertyService.getPayeCode());
    }
}
