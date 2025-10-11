package mg.cnaps.gestion.ccl.project.entity.dto.mouvement;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import mg.cnaps.gestion.ccl.project.entity.*;
import mg.cnaps.gestion.ccl.project.util.TimestampUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class MouvementDto {

    private String id;

    private TypeMouvement typeMouvement;

    private Client client;

    private Infrastructure infrastructure;

    private List<MouvementInfra> mouvementInfras;
    private String periodeDebut;

    private String periodeFin;

    private String dhMouvement;

    public MouvementDto(Mouvement m) {
        this.id = m.getId();
        this.typeMouvement = m.getTypeMouvement();
        this.dhMouvement = TimestampUtil.formatTimestamp(m.getDhMouvement());
        this.id=m.getId();
        this.client = m.getClient();
        this.mouvementInfras=m.getMouvementInfras();
        this.periodeDebut=TimestampUtil.formatTimestamp(m.getPeriodeDebut());
        this.periodeFin=TimestampUtil.formatTimestamp(m.getPeriodeFin());
    }

    public MouvementDto() {
    }


    public  List<MouvementDto> toMouvementDtos(List<Mouvement> mouvements){
        List<MouvementDto> mouvementDtos = new ArrayList<>();
        for (Mouvement mouvement : mouvements){
            mouvementDtos.add(new MouvementDto(mouvement) );
        }
        return mouvementDtos;
    }




}
