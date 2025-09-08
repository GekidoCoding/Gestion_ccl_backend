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
    @Query("SELECT m FROM Mouvement m WHERE m.client.id = :clientId")
    List<Mouvement> getMouvementByClient_Id(@Param("clientId") String  clientId);

    @Query("SELECT m FROM Mouvement m JOIN m.mouvementInfras mi WHERE mi.infrastructure.id = :infraId")
    List<Mouvement> getMouvementByInfrastructureId(@Param("infraId") String infraId);

    @Query("SELECT m FROM Mouvement m JOIN m.mouvementInfras mi WHERE mi.infrastructure.id = :infraId ORDER BY m.dhMouvement DESC")
    Page<Mouvement> getMouvementByInfrastructureId(@Param("infraId") String infraId, Pageable pageable);

}