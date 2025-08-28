package mg.cnaps.gestion.ccl.project.entity.dto.mouvement;

import lombok.Data;
import mg.cnaps.gestion.ccl.project.entity.Client;
import mg.cnaps.gestion.ccl.project.entity.Infrastructure;
import mg.cnaps.gestion.ccl.project.entity.Mouvement;
import mg.cnaps.gestion.ccl.project.entity.TypeMouvement;
import mg.cnaps.gestion.ccl.project.util.TimestampUtil;

import java.util.ArrayList;
import java.util.List;

@Data
public class MouvementDto {

    private String id;

    private TypeMouvement typeMouvement;

    private Client client;

    private Infrastructure infrastructure;

//    private String periodeDebut;
//
//    private String periodeFin;

    private String dhMouvement;

    public MouvementDto(Mouvement m) {
        this.id = m.getId();
        this.typeMouvement = m.getTypeMouvement();
        this.dhMouvement = TimestampUtil.formatTimestamp(m.getDhMouvement());
        this.id=m.getId();
        this.client = m.getClient();
        this.infrastructure = m.getInfrastructure();

    }

    public MouvementDto() {
    }

    public  MouvementDto toMouvementDto(Mouvement mouvement){
        MouvementDto mouvementDto = new MouvementDto();
        mouvementDto.setId(mouvement.getId());
        mouvementDto.setTypeMouvement(mouvement.getTypeMouvement());
        mouvementDto.setClient(mouvement.getClient());
        mouvementDto.setInfrastructure(mouvement.getInfrastructure());
//        mouvementDto.setDhMouvement(TimestampUtil.formatTimestamp(mouvement.getDhMouvement()));
        return mouvementDto;
    }
    public  List<MouvementDto> toMouvementDto(List<Mouvement> mouvements){
        List<MouvementDto> mouvementDtos = new ArrayList<>();
        for (Mouvement mouvement : mouvements){
            mouvementDtos.add(toMouvementDto(mouvement));
        }
        return mouvementDtos;
    }



//    private Integer nombre ;

}
