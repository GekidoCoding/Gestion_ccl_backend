package mg.cnaps.gestion.ccl.project.repository;

import mg.cnaps.gestion.ccl.framework.core.repository.GenericRepository;
import mg.cnaps.gestion.ccl.project.entity.Client;
import mg.cnaps.gestion.ccl.project.entity.Mouvement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface MouvementRepo extends GenericRepository<Mouvement, String> {
    Page<Mouvement> getMouvementByClient_IdOrderByDhMouvementDesc(String client_id ,  Pageable pageable);
    List<Mouvement> getMouvementByClient_Id(String  client_id);
    List<Mouvement> getMouvementByInfrastructure_Id(String client_id);
    Page<Mouvement> getMouvementByInfrastructure_IdOrderByDhMouvementDesc(String infrastructureId, Pageable pageable);

    String client(Client client);
}