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


    @Value("${frequence.default.id}")
    private String frequenceDefaultId;


    @Value("${frequence.jour.id}")
    private String frequenceJourId;


    @Value("${frequence.heure.id}")
    private String frequenceHeureId;

    @Value("${frequence.mois.id}")
    private String frequenceMoisId;

    @Value("${frequence.nuit.id}")
    private String frequenceNuitId;
}
