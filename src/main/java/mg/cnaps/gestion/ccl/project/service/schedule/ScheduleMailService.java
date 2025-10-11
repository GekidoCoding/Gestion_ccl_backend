package mg.cnaps.gestion.ccl.project.service.schedule;

import mg.cnaps.gestion.ccl.project.config.CclPropertyService;
import mg.cnaps.gestion.ccl.project.entity.Mouvement;
import mg.cnaps.gestion.ccl.project.service.EmailService;
import mg.cnaps.gestion.ccl.project.service.GestionnaireService;
import mg.cnaps.gestion.ccl.project.service.MouvementService;
import mg.cnaps.gestion.ccl.project.util.TimestampUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ScheduleMailService {

    private final MouvementService mouvementService;
    private final CclPropertyService cclPropertyService;
    private final GestionnaireService gestionnaireService;
    private final EmailService emailService;

    public ScheduleMailService(
            MouvementService mouvementService,
            CclPropertyService cclPropertyService,
            GestionnaireService gestionnaireService, EmailService emailService
    ) {
        this.mouvementService = mouvementService;
        this.cclPropertyService = cclPropertyService;
        this.gestionnaireService = gestionnaireService;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 07 0 * * *")
    public void sendMailPayageLateToday() throws MessagingException {
        String[] destinataires = gestionnaireService.getEmailGestionnaire();
        List<Mouvement> mouvementsNotifier = mouvementService.getOccupationNotPayedInDelai();
        String dateNow = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        StringBuilder messageMail = new StringBuilder();
        messageMail.append("Bonjour,\n\n")
                .append("Nous sommes le ").append(dateNow).append(".\n\n")
                .append("Veuillez trouver ci-dessous la liste des réservations impayées ")
                .append("dont l’échéance est dans 3 jours ou moins :\n\n");

        int index = 1;
        for (Mouvement mouvement : mouvementsNotifier) {
            double duree = TimestampUtil.caculateDifferenceTimestamp(
                    cclPropertyService,
                    mouvement.getPeriodeDebut(),
                    mouvement.getPeriodeFin(),
                    cclPropertyService.getFrequenceJourId()
            );
            messageMail.append(index++)
                    .append(". Réservation #")
                    .append(mouvement.getId())
                    .append(" de ")
                    .append(mouvement.getClient().getDesignationClient())
                    .append(" — du ")
                    .append(TimestampUtil.formatTimestamp(mouvement.getPeriodeDebut()))
                    .append(" au ")
                    .append(TimestampUtil.formatTimestamp(mouvement.getPeriodeFin()))
                    .append(" (échéance dans ")
                    .append(String.format("%.0f", duree))
                    .append(" jour(s))\n");
        }

        if (mouvementsNotifier.isEmpty()) {
            messageMail.append("Aucune réservation impayée à signaler aujourd’hui.\n");
        }

        messageMail.append("\nMerci de vérifier ces paiements dans les plus brefs délais.\n\n")
                .append("Cordialement,\n")
                .append("CNaPS Madagascar\n\n")
                .append("──────────────────────────────\n")
                .append("Avertissement : Ceci est un message automatique, merci de ne pas y répondre.\n");

        String objet = "Réservations impayées à moins de 3 jours de l’échéance – " + dateNow;
        this.emailService.sendEmailToMultipleRecipients(destinataires,objet , messageMail.toString());
    }

    @Scheduled(cron = "0 15 0 * * *")
    public void sendMailPreparationToday() throws MessagingException {
        String[] destinataires = gestionnaireService.getEmailGestionnaire();
        List<Mouvement> mouvementsNotifier = mouvementService.getOccupationPayedInDelai();
        String dateNow = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        StringBuilder messageMail = new StringBuilder();
        messageMail.append("Bonjour,\n\n")
                .append("Nous sommes le ").append(dateNow).append(".\n\n")
                .append("Voici la liste des réservations à venir dans 3 jours ou moins :\n\n");

        int index = 1;
        for (Mouvement mouvement : mouvementsNotifier) {
            double duree = TimestampUtil.caculateDifferenceTimestamp(
                    cclPropertyService,
                    mouvement.getPeriodeDebut(),
                    mouvement.getPeriodeFin(),
                    cclPropertyService.getFrequenceJourId()
            );
            messageMail.append(index++)
                    .append(". Réservation #")
                    .append(mouvement.getId())
                    .append(" de ")
                    .append(mouvement.getClient().getDesignationClient())
                    .append(" — du ")
                    .append(TimestampUtil.formatTimestamp(mouvement.getPeriodeDebut()))
                    .append(" au ")
                    .append(TimestampUtil.formatTimestamp(mouvement.getPeriodeFin()))
                    .append(" (prévue dans ")
                    .append(String.format("%.0f", duree))
                    .append(" jour(s))\n");
        }

        if (mouvementsNotifier.isEmpty()) {
            messageMail.append("Aucune réservation prévue dans les 3 prochains jours.\n");
        }

        messageMail.append("\nMerci de préparer les infrastructures nécessaires.\n\n")
                .append("Cordialement,\n")
                .append("CNaPS Madagascar\n\n")
                .append("──────────────────────────────\n")
                .append("Avetissement : Ceci est un message automatique, merci de ne pas y répondre.\n");

        String objet = "Préparation des réservations à venir – " + dateNow;
        this.emailService.sendEmailToMultipleRecipients(destinataires,objet, messageMail.toString());
    }
}
