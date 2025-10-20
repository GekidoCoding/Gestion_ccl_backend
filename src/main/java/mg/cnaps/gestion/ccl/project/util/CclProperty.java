package mg.cnaps.gestion.ccl.project.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Getter
@Configuration
public class CclProperty {
    @Value("${etat.inactif.code}")
    private int inactifCode;
    @Value("${etat.actif.code}")
    private int actifCode;
    @Value("${etat.proforma.code}")
    private int proformaCode;

    @Value("${etat.reelle.code}")
    private int reelleCode;

    @Value("${etat.paye.code}")
    private int payeCode;

    @Value("${type.mouvement.classement.id}")
    private String classementId;
    @Value("${type.mouvement.renseignement.id}")
    private String renseignementId;
    @Value("${type.mouvement.reservation.id}")
    private String reservationId;
    @Value("${type.mouvement.occupation.id}")
    private String occupationId;

    @Value("${type.client.personne.id}")
    private String personneId;
    @Value("${type.client.entreprise.id}")
    private String entrepriseId;

    @Value("${facture.remise}")
    private double factureRemise;

    @Value("${avance.paiement}")
    private double avancePaiement;

    @Value("${delai.paiement.jour}")
    private int delaiPaiementJour;



    @Value("${my.mail}")
    private String myMail;

    @Value("${preparation.signalement.jour}")
    private int preparationSignalementJour;


    @Value("${heure.paiement.delai}")
    private double heurePaiementRemise;



    @Value("${frequence.default.id}")
    private String frequenceDefaultId;


    @Value("${frequence.jour.id}")
    private String frequenceJourId;


    @Value("${frequence.heure.id}")
    private String frequenceHeureId;

    @Value("${frequence.mois.id}")
    private String frequenceMoisId;

    @Value("${type.notification.danger.id}")
    private String typeNotificationDangerId;

    @Value("${type.notification.signalement.id}")
    private String typeNotificationSignalementId;

    @Value("${schedule.hour}")
    private String scheduleHour;


    @Value("${facture.proforma.export.path}")
    private String factureProformaExportPath;


    @Value("${facture.reelle.export.path}")
    private String factureReelleExportPath;

    @Value("${contrat.export.path}")
    private String contratExportPath;


    @Value("${frequence.nuit.id}")
    private String frequenceNuitId;
}
