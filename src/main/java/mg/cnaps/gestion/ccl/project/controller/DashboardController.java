package mg.cnaps.gestion.ccl.project.controller;

import mg.cnaps.gestion.ccl.project.service.HistoriqueMvtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/cnaps/gestion/ccl/dashboard")
public class DashboardController {
    private final HistoriqueMvtService historiqueMvtService;

    public DashboardController(HistoriqueMvtService historiqueMvtService) {
        this.historiqueMvtService = historiqueMvtService;
    }

    @GetMapping("/total")
    public ResponseEntity<List<Integer>> findTotalByCriteria(
            @RequestParam(value = "date1", required = false) Date date1,
            @RequestParam(value = "date2", required = false) Date date2,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "categorieInfraId", required = false) String categorieInfraId,
            @RequestParam(value = "typeMouvementId", required = false) String typeMouvementId,
            @RequestParam(value = "modelesIds", required = false) String[] modelesIds
    ) {
        return ResponseEntity.ok().body(
                historiqueMvtService.getTotalDashboard(date1, date2, year, categorieInfraId, typeMouvementId, modelesIds)
        );
    }

    @GetMapping("/pourcentage")
    public ResponseEntity<List<Double>> findPourcentageByCriteria(
            @RequestParam(value = "date1", required = false) Date date1,
            @RequestParam(value = "date2", required = false) Date date2,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "categorieInfraId", required = false) String categorieInfraId,
            @RequestParam(value = "typeMouvementId", required = false) String typeMouvementId,
            @RequestParam(value = "modelesIds", required = false) String[] modelesIds
    ) {
        return ResponseEntity.ok().body(
                historiqueMvtService.getPourcentages(date1, date2, year, categorieInfraId, typeMouvementId, modelesIds)
        );
    }

    @GetMapping("/lineChart")
    public ResponseEntity<List<List<Integer>>> getMonthlyData(
            @RequestParam(value = "date1", required = false) Date date1,
            @RequestParam(value = "date2", required = false) Date date2,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "categorieInfraId", required = false) String categorieInfraId,
            @RequestParam(value = "typeMouvementId", required = false) String typeMouvementId,
            @RequestParam(value = "modelesIds", required = false) String[] modelesIds
    ) {
        return ResponseEntity.ok().body(
                historiqueMvtService.getMonthlyData(date1, date2, year, categorieInfraId, typeMouvementId, modelesIds)
        );
    }
}
