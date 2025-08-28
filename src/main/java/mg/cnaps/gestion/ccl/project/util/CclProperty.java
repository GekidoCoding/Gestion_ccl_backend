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
    @Value("${etat.proformat.code}")
    private int proformatCode;
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

}
