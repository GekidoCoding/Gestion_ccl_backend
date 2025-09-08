package mg.cnaps.gestion.ccl.project.service;

import mg.cnaps.gestion.ccl.framework.core.service.GenericService;
import mg.cnaps.gestion.ccl.project.entity.HistoriqueMvt;

import java.util.Date;
import java.util.List;

public interface HistoriqueMvtService extends GenericService<HistoriqueMvt, String> {
    List<HistoriqueMvt> findHistoriqueMvtByMouvement_Id(String mouvementId);
    List<HistoriqueMvt> findHistoriqueMvtsByTypeMouvement_Id(String typeMouvementId);

    List<Integer> getTotalDashboard(Date date1, Date date2, Integer year,
                                    String categorieInfraId, String typeMouvementId, String[] modelesIds);

    List<Double> getPourcentages(Date date1, Date date2, Integer year,
                                 String categorieInfraId, String typeMouvementId, String[] modelesIds);

    List<List<Integer>> getMonthlyData(Date date1, Date date2, Integer year,
                                       String categorieInfraId, String typeMouvementId, String[] modelesIds);

    List<HistoriqueMvt> findAllCriteria(Date date1, Date date2, Integer year,
                                        String categorieInfraId, String typeMouvementId, String[] modelesIds);
}
