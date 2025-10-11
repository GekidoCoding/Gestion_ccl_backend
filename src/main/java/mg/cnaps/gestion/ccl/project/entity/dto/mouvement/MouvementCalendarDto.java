package mg.cnaps.gestion.ccl.project.entity.dto.mouvement;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import mg.cnaps.gestion.ccl.project.entity.Client;
import mg.cnaps.gestion.ccl.project.entity.Infrastructure;
import mg.cnaps.gestion.ccl.project.entity.Mouvement;
import mg.cnaps.gestion.ccl.project.entity.TypeMouvement;
import mg.cnaps.gestion.ccl.project.util.TimestampUtil;

@Getter
@Setter
public class MouvementCalendarDto {
    private String id;
    private TypeMouvement typeMouvement;
    private Infrastructure infrastructure;
    private Client client;
    private String periodeDebut;
    private String periodeFin;

    public MouvementCalendarDto(Mouvement m ,  Infrastructure infrastructure) {
        this.setId(m.getId());
        this.setInfrastructure(infrastructure);

        Client client = new Client();
        String nom = m.getClient() != null ? m.getClient().getDesignationClient() : null;
        client.setDesignationClient(nom );


        this.setClient(client);

        this.setPeriodeDebut(TimestampUtil.formatTimestampWithT(m.getPeriodeDebut()));
        this.setPeriodeFin(TimestampUtil.formatTimestampWithT(m.getPeriodeFin()));

        TypeMouvement typeMouvement = new TypeMouvement();
        typeMouvement.setNom(m.getTypeMouvement() != null ? m.getTypeMouvement().getNom() : "N/A");
        this.setTypeMouvement(typeMouvement);
    }

    public MouvementCalendarDto(Mouvement m) {
        this.setId(m.getId());

        Infrastructure infrastructure = new Infrastructure();
        if (m.getMouvementInfras() != null && !m.getMouvementInfras().isEmpty()) {
            String infraNames = m.getMouvementInfras().stream()
                    .map(minf -> {
                        Infrastructure infra = minf.getInfrastructure();
                        if (infra != null) {
                            String nom = infra.getNom() != null ? infra.getNom() : "";
                            String numero = infra.getNumero() != null ? infra.getNumero() : "";
                            return "●"+nom + " - " + numero;
                        }
                        return "";
                    })
                    .filter(s -> !s.isEmpty())
                    .reduce((s1, s2) -> s1 + "\n " + s2)
                    .orElse("N/A");
            infrastructure.setNom(infraNames);
        } else {
            infrastructure.setNom("N/A");
        }
        this.setInfrastructure(infrastructure);

        Client client = new Client();
        String nom = m.getClient() != null ? m.getClient().getDesignationClient() : null;
        client.setDesignationClient(nom );


        this.setClient(client);

        this.setPeriodeDebut(TimestampUtil.formatTimestampWithT(m.getPeriodeDebut()));
        this.setPeriodeFin(TimestampUtil.formatTimestampWithT(m.getPeriodeFin()));

        TypeMouvement typeMouvement = new TypeMouvement();
        typeMouvement.setNom(m.getTypeMouvement() != null ? m.getTypeMouvement().getNom() : "N/A");
        this.setTypeMouvement(typeMouvement);
    }

}
