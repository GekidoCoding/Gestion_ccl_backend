package mg.cnaps.gestion.ccl.project.service.schedule;

import mg.cnaps.gestion.ccl.project.config.CclPropertyService;
import mg.cnaps.gestion.ccl.project.entity.Mouvement;
import mg.cnaps.gestion.ccl.project.entity.Notification;
import mg.cnaps.gestion.ccl.project.entity.TypeNotification;
import mg.cnaps.gestion.ccl.project.repository.NotificationRepo;
import mg.cnaps.gestion.ccl.project.service.MouvementService;
import mg.cnaps.gestion.ccl.project.service.NotificationService;
import mg.cnaps.gestion.ccl.project.util.TimestampUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduleNotificationService {

    private final MouvementService mouvementService;
    private final NotificationRepo notificationRepo;
    private final NotificationService notificationService;
    private final CclPropertyService cclPropertyService;

    public ScheduleNotificationService(
            MouvementService mouvementService,
            NotificationRepo notificationRepo,
            NotificationService notificationService,
            CclPropertyService cclPropertyService
    ) {
        this.mouvementService = mouvementService;
        this.notificationRepo = notificationRepo;
        this.notificationService = notificationService;
        this.cclPropertyService = cclPropertyService;
    }

    @Scheduled(cron = "0 1 0 * * *")
    public void notifyPayageLate() {
        List<Mouvement> mouvementsNotifier = mouvementService.getOccupationNotPayedInDelai();

        for (Mouvement mouvement : mouvementsNotifier) {
            double duree = TimestampUtil.caculateDifferenceTimestamp(
                    cclPropertyService,
                    mouvement.getPeriodeDebut(),
                    mouvement.getPeriodeFin(),
                    cclPropertyService.getFrequenceJourId()
            );

            String notifMessage = "Paiement en retard : réservation #" + mouvement.getId() +
                    " de " + mouvement.getClient().getDesignationClient() +
                    " (retard de " + String.format("%.0f", duree) + " jour(s))";

            saveNotification(mouvement, notifMessage);
        }
    }

    @Scheduled(cron = "0 6 0 * * *")
    public void notifyPreparation() {
        List<Mouvement> mouvementsNotifier = mouvementService.getOccupationPayedInDelai();

        for (Mouvement mouvement : mouvementsNotifier) {
            double duree = TimestampUtil.caculateDifferenceTimestamp(
                    cclPropertyService,
                    mouvement.getPeriodeDebut(),
                    mouvement.getPeriodeFin(),
                    cclPropertyService.getFrequenceJourId()
            );

            String notifMessage = "Préparation requise : réservation #" + mouvement.getId() +
                    " de " + mouvement.getClient().getDesignationClient() +
                    " dans " + String.format("%.0f", duree) + " jour(s).";

            saveNotification(mouvement, notifMessage);
        }
    }

    @Scheduled(cron = "0 25 0 * * *")
    public void markAsRead() {
        notificationService.markAllAsRead();
    }

    private void saveNotification(Mouvement mouvement, String message) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setMouvement(mouvement);
        notification.setDhCreation(Timestamp.valueOf(LocalDateTime.now()));

        TypeNotification type = new TypeNotification();
        type.setId(cclPropertyService.getTypeNotificationDangerId());
        notification.setTypeNotification(type);
        notification.setRead(false);

        notificationRepo.save(notification);
    }
}
