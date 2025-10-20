package mg.cnaps.gestion.ccl.project.entity.dto.infrastructure;

import lombok.Getter;
import lombok.Setter;
import mg.cnaps.gestion.ccl.project.entity.InfraTarif;
import mg.cnaps.gestion.ccl.project.entity.Infrastructure;
import mg.cnaps.gestion.ccl.project.entity.MouvementInfra;

@Getter
@Setter
public class InfrastructureVisuDto {
    private String nom;
    private Double tarif;
    private String frequence;
    private String modele;
    public InfrastructureVisuDto(MouvementInfra infrastructure) {
        this.setFrequence(infrastructure.getFrequence().getLibelle());
        this.setNom(infrastructure.getInfrastructure().getNom()+" - "+infrastructure.getInfrastructure().getNumero());
        this.setModele(infrastructure.getInfrastructure().getModeleInfra().getNom());
        Double tarif = 0.0;
        for(InfraTarif infraTarif : infrastructure.getInfrastructure().getInfraTarifs()){
            if(infraTarif.getFrequence().getId().equals(infrastructure.getFrequence().getId())){
                tarif=infraTarif.getTarifInfra();
            }
        }
        this.setTarif(tarif);
    }
}
