package mg.cnaps.gestion.ccl.project.service;

import mg.cnaps.gestion.ccl.framework.jpa.core.service.GenericService;
import mg.cnaps.gestion.ccl.project.entity.Mouvement;
import mg.cnaps.gestion.ccl.project.entity.dto.mouvement.MouvementCalendarDto;
import mg.cnaps.gestion.ccl.project.entity.dto.mouvement.MouvementDto;
import org.springframework.data.domain.Page;

import java.util.List;


public interface MouvementService extends GenericService<Mouvement, String> {
    Mouvement update(Mouvement entity, String id, String matricule);

    Mouvement save(Mouvement entity, String matricule);

    List<Mouvement> getMouvementByClient_Id(String  client_id);
    List<Mouvement> getMouvementByInfrastructure_Id (String  client_id);
    Page<MouvementDto> getMouvementByInfrastructure_Id (String  client_id , int page , int pageSize);
    Page<MouvementDto> getMouvementByClient_Id(String client_id, int page , int pageSize);
    Page<MouvementDto> getAllPaginated( int page , int pageSize);
    Page<MouvementDto> getAllCriteria(int page , int pageSize , Mouvement criteria , String catInfraId );
    List<MouvementCalendarDto> getMouvementCalendarDto();
    List<MouvementCalendarDto> getMouvementCalendarDtoByInfratructureId(String infratructureId);
    Mouvement accorderMouvement(String id);

    Mouvement classerMouvement(String id, String matricule);

    List<MouvementDto> getListMouvementConflict(Mouvement mouvement);
    List<MouvementCalendarDto> getMouvementCalendarDtoByCriteria(String infraId , String[] modelesIds);

    List<Mouvement> getOccupationNotPayedInDelai();

    List<Mouvement> getOccupationPayedInDelai();
}
