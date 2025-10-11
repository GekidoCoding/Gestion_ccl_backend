package mg.cnaps.gestion.ccl.project.service;

import mg.cnaps.gestion.ccl.framework.jpa.core.service.GenericService;
import mg.cnaps.gestion.ccl.project.entity.CategorieInfra;
import mg.cnaps.gestion.ccl.project.entity.Infrastructure;
import mg.cnaps.gestion.ccl.project.entity.Localisation;
import mg.cnaps.gestion.ccl.project.entity.ModeleInfra;
import org.springframework.data.domain.Page;

import java.sql.Timestamp;
import java.util.List;


public interface InfrastructureService extends GenericService<Infrastructure, String> {
    List<Infrastructure> checkSimilarity(Infrastructure infrastructure);

    Infrastructure save(Infrastructure entity, String matricule);

    Infrastructure update(Infrastructure entity, String id, String matricule);

    void delete(String id , String observation) throws  Exception;
    Page<Infrastructure> findByCriteriaAll(int page, int pageSize,
                                           ModeleInfra[] modeles,
                                           Localisation[] localisations,
                                           Infrastructure criteria,
                                           CategorieInfra catInfra,
                                           Timestamp debut,
                                           Timestamp fin);
}