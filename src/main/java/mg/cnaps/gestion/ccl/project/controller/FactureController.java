package mg.cnaps.gestion.ccl.project.controller;

import mg.cnaps.gestion.ccl.framework.core.controller.GenericController;
import mg.cnaps.gestion.ccl.project.config.CclPropertyService;
import mg.cnaps.gestion.ccl.project.entity.Facture;
import mg.cnaps.gestion.ccl.project.service.EmailService;
import mg.cnaps.gestion.ccl.project.service.FactureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cnaps/gestion/ccl/facture")
public class FactureController extends GenericController<Facture , String ,FactureService > {
    @Autowired
    private JavaMailSender emailSender;

    private final CclPropertyService cclPropertyService;

    public FactureController(FactureService service,  CclPropertyService cclPropertyService) {
        super(service);
        this.cclPropertyService = cclPropertyService;
    }

    @GetMapping("/config/remise")
    public ResponseEntity<Double> getFactureRemise(){
        return ResponseEntity.ok( cclPropertyService.getFactureRemise());
    }

    @GetMapping("/mouvement/{id}")
    public ResponseEntity<List<Facture>> getFacturesByMouvement_Id(@PathVariable String id){
        return ResponseEntity.ok( service.getFacturesByMouvement_Id(id));
    }

    @PostMapping("/email/send")
    public ResponseEntity<String> sendEmail(
            @RequestParam("message") String message ,
            @RequestParam("destinataires") String[] destinataires
    )  {
        try {
            EmailService.sendEmail(emailSender, destinataires, message);
            return ResponseEntity.status(HttpStatus.CREATED).body("Email envoyé avec succès ✅");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'envoi de l'email ❌ : " + e.getMessage());
        }

    }
}
