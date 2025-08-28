package mg.cnaps.gestion.ccl.project.service;

import mg.cnaps.gestion.ccl.framework.core.service.GenericService;
import mg.cnaps.gestion.ccl.project.entity.Facture;
import mg.cnaps.gestion.ccl.project.entity.Mouvement;
import mg.cnaps.gestion.ccl.project.entity.dto.mouvement.MouvementCalendarDto;
import mg.cnaps.gestion.ccl.project.entity.dto.mouvement.MouvementDto;
import org.springframework.data.domain.Page;

import java.util.List;


public interface MouvementService extends GenericService<Mouvement, String> {
    List<Mouvement> getMouvementByClient_Id(String  client_id);
    List<Mouvement> getMouvementByInfrastructure_Id (String  client_id);
    Page<MouvementDto> getMouvementByInfrastructure_Id (String  client_id , int page , int pageSize);
    Page<MouvementDto> getMouvementByClient_Id(String client_id, int page , int pageSize);
    Page<MouvementDto> getAllPaginated( int page , int pageSize);
    Page<MouvementDto> getAllCriteria(int page , int pageSize , Mouvement criteria );
    List<MouvementCalendarDto> getMouvementCalendarDto();
    List<MouvementCalendarDto> getMouvementCalendarDtoByInfratructureId(String infratructureId);
    Mouvement accorderMouvement(String id);

    Mouvement classerMouvement(String id);
}
