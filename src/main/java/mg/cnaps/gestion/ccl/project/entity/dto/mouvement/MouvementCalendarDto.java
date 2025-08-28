package mg.cnaps.gestion.ccl.project.entity.dto.mouvement;

import lombok.Data;
import mg.cnaps.gestion.ccl.project.entity.Client;
import mg.cnaps.gestion.ccl.project.entity.Infrastructure;
import mg.cnaps.gestion.ccl.project.entity.Mouvement;
import mg.cnaps.gestion.ccl.project.entity.TypeMouvement;
import mg.cnaps.gestion.ccl.project.util.TimestampUtil;

@Data
public class MouvementCalendarDto {
    private String id;
    private TypeMouvement typeMouvement;
    private Infrastructure infrastructure;
    private Client client;
    private String periodeDebut;
    private String periodeFin;

    public MouvementCalendarDto(Mouvement m) {
        this.setId(m.getId());
        Infrastructure infrastructure = new Infrastructure();
        infrastructure.setNom(m.getInfrastructure().getNom() +" ● "+m.getInfrastructure().getNumero());
        this.setInfrastructure(infrastructure);
        Client client = new Client();
        String nom = m.getClient().getNom();
        String prenom = m.getClient().getPrenom();
        String raisonSociale = m.getClient().getRaisonSociale();

        if (nom == null && prenom == null) {
            client.setNom(raisonSociale != null ? raisonSociale : "N/A");
        } else {
            nom = (nom != null) ? nom : "";
            prenom = (prenom != null) ? prenom : "";
            client.setNom(nom + " " + prenom);
        }
        this.setClient(client);
        this.setPeriodeDebut(TimestampUtil.formatTimestampWithT(m.getPeriodeDebut()));
        this.setPeriodeFin(TimestampUtil.formatTimestampWithT(m.getPeriodeFin()));
        TypeMouvement typeMouvement = new TypeMouvement();
        typeMouvement.setNom(m.getTypeMouvement().getNom());
        this.setTypeMouvement(typeMouvement);
    }
}
