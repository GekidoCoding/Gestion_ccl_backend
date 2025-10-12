package mg.cnaps.gestion.ccl.project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import mg.cnaps.gestion.ccl.framework.jpa.core.controller.GenericController;
import mg.cnaps.gestion.ccl.framework.general.error.ErrorResponse;
import mg.cnaps.gestion.ccl.project.config.CclPropertyService;
import mg.cnaps.gestion.ccl.project.entity.*;
import mg.cnaps.gestion.ccl.project.entity.dto.EmailRequest;
import mg.cnaps.gestion.ccl.project.entity.dto.facture.FactureDto;
import mg.cnaps.gestion.ccl.project.entity.dto.mouvement.MouvementDto;
import mg.cnaps.gestion.ccl.project.service.EmailService;
import mg.cnaps.gestion.ccl.project.service.FactureService;
import mg.cnaps.gestion.ccl.project.service.PaiementService;
import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/facture")
public class FactureController extends GenericController<Facture , String ,FactureService > {
    @Autowired
    private JavaMailSender emailSender;
    private final EmailService emailService;
    private final PaiementService paiementService;
    private final CclPropertyService cclPropertyService;
    public FactureController(FactureService service, EmailService emailService, PaiementService paiementService, CclPropertyService cclPropertyService) {
        super(service);
        this.emailService = emailService;
        this.paiementService = paiementService;
        this.cclPropertyService = cclPropertyService;
    }

    @PostMapping("/create/new")
    public ResponseEntity<?> create(@RequestBody Facture dto , HttpServletRequest request) {
        String matricule =request.getAttribute("matricule").toString();
        return ResponseEntity.status(HttpStatus.CREATED).body(getService().save(dto,matricule));
    }

    @PutMapping("/update/new/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody @Valid Facture dto , HttpServletRequest request) throws JsonProcessingException {
        String matricule =request.getAttribute("matricule").toString();
        return ResponseEntity.ok(getService().update(dto, id , matricule));
    }


    @GetMapping("/config/remise")
    public ResponseEntity<Double> getFactureRemise(){
        return ResponseEntity.ok( cclPropertyService.getFactureRemise());
    }

    @GetMapping("/id/{idFacture}")
    public ResponseEntity<Facture> getFactureRemise(@PathVariable("idFacture") String idFacture){
        return ResponseEntity.ok( this.service.findById(idFacture) );
    }

    @GetMapping("/mouvement/{id}")
    public ResponseEntity<?> getFacturesByMouvement_Id(@PathVariable String id){
        return ResponseEntity.ok( service.getFacturesByMouvement_Id(id));
    }
    @GetMapping("/default/proforma/{idMouvement}")
    public ResponseEntity<?> getDefaultFactureProforma(@PathVariable("idMouvement") String idMouvement) {
        try {
            return ResponseEntity.ok(service.getDefaultFactureRelleByMouvement_Id(idMouvement));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }



    private ResponseEntity<byte[]> buildPdfResponse(byte[] pdfBytes, String filename) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + filename)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @PostMapping("/export-proforma/exist/{idFacture}")
    public ResponseEntity<?> generateProformaNotDefault(@PathVariable("idFacture") String idFacture )  {
        try{
            return getBuildFacture(idFacture);

        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

//    @PostMapping("/email/send")
//    public ResponseEntity<String> sendEmail(
//            @RequestParam("destinataires") String[] destinataires
//    )  {
//        try {
//            emailService.sendEmailToMultipleRecipients( destinataires,"sujet test", "yess lesy eh mety lty eh");
//            return ResponseEntity.status(HttpStatus.CREATED).body("Email envoyé avec succès ");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'envoi de l'email : " + e.getMessage());
//        }
//
//    }

    @PostMapping("/sendEmail/facture")
    public ResponseEntity<?> sendEmailFactureToClientWithCopy(
            @RequestBody EmailRequest emailRequest
          ) {
        String idFacture= emailRequest.getIdFacture();
        String[]destinataires = emailRequest.getDestinataires();
        if (destinataires == null || destinataires.length == 0) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Au moins un destinataire doit être fourni.", HttpStatus.BAD_REQUEST.value())
            );
        }
        try {
            service.sendEmailForFactureWithPdf(idFacture, destinataires);
            return ResponseEntity.ok(Map.of(
                    "status", 200,
                    "message", "Email envoyé avec succès aux destinataires."));
        } catch (JRException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erreur lors de la génération du PDF : " + e.getMessage(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value()));
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erreur lors de l'envoi de l'email : " + e.getMessage(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Une erreur est survenue : " + e.getMessage(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }


    private ResponseEntity<byte[]> getBuildFacture(String idFacture) throws JRException {
        return buildPdfResponse(
                this.service.buildFacturePdf(idFacture)
                , "proforma.pdf");
    }

    private ResponseEntity<byte[]> getBuildFactureWithFacture(Facture facture) throws JRException {
        return buildPdfResponse(
                this.service.buildFacturePdfWithFacture(facture)
                , "proforma.pdf");
    }

    @PostMapping("/export-proforma/{idMouvement}/{enregistrer}")
    public ResponseEntity<byte[]> generateProforma(@PathVariable("idMouvement") String idMouvement , @PathVariable("enregistrer") boolean enregistrer) throws JRException {
        Facture newFacture = this.service.getDefaultFactureProformaByMouvement_Id(idMouvement);
        if(enregistrer){
            newFacture = this.service.createAndSaveFacture(idMouvement);
        }

        return getBuildFactureWithFacture( newFacture);
    }

    @PostMapping("/export-facture/{idPaiement}")
    public ResponseEntity<byte[]> generateFacture(@PathVariable("idPaiement") String idPaiement ) throws JRException {
        byte[] pdfBytes = this.service.getBytePdfPaiement(idPaiement);
        return buildPdfResponse(pdfBytes, "facture.pdf");
    }

    @GetMapping("/dto/{id}")
    public ResponseEntity<FactureDto> getFactureDto(@PathVariable("id") String id) {
        try {
            return ResponseEntity.ok(this.service.findByIdWithReste(id)) ;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }



}
