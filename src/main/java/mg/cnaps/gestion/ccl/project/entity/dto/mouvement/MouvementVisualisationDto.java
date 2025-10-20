package mg.cnaps.gestion.ccl.project.entity.dto.mouvement;

import lombok.Getter;
import lombok.Setter;
import mg.cnaps.gestion.ccl.project.entity.HistoriqueMvt;
import mg.cnaps.gestion.ccl.project.entity.Infrastructure;
import mg.cnaps.gestion.ccl.project.entity.dto.historique.HistoFactureDto;
import mg.cnaps.gestion.ccl.project.entity.dto.infrastructure.InfrastructureVisuDto;

import java.util.List;

@Getter
@Setter
public class MouvementVisualisationDto {
    private String client;
    private String typeMouvement;
    private List<InfrastructureVisuDto> infrastructures;
    private String periodeDebut;
    private String periodeFin;
    private Integer nombrePersonne;
    private String observation;
    private List<HistoriqueMvt> historiqueMvts;
    private List<HistoFactureDto> histoFactures;
}
