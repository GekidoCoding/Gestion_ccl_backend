package mg.cnaps.gestion.ccl.project.controller;

import mg.cnaps.gestion.ccl.framework.excel.service.GenericExcelService;
import mg.cnaps.gestion.ccl.framework.jpa.core.controller.GenericController;
import mg.cnaps.gestion.ccl.project.entity.HistoriqueMvt;
import mg.cnaps.gestion.ccl.project.entity.dto.historique.HistoriqueMvtDto;
import mg.cnaps.gestion.ccl.project.service.HistoriqueMvtService;
import net.sf.jasperreports.repo.InputStreamResource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/historique_mvt")
public class HistoriqueMvtController extends GenericController<HistoriqueMvt , String ,HistoriqueMvtService > {

    private final GenericExcelService excelService;

    public HistoriqueMvtController(HistoriqueMvtService service, GenericExcelService excelService) {
        super(service);
        this.excelService = excelService;
    }

    @GetMapping("/mouvement/{id}")
    public ResponseEntity<List<HistoriqueMvt>>  findHistoriqueMvtByMouvement_Id(@PathVariable("id") String id){
        return ResponseEntity.ok(service.findHistoriqueMvtByMouvement_Id(id)) ;
    }

    @GetMapping("/mouvement/dashboard")
    public ResponseEntity<List<HistoriqueMvt>>  findHistoriqueMvtByMouvement_Id(@RequestParam(value = "id" , required = false) Integer year  ,
                                                                                @RequestParam(value="date1" , required = false) Date date1 ,
                                                                                @RequestParam(value="date2" ,  required = false) Date date2,
                                                                                @RequestParam(value="categorieInfraId" ,  required = false) String categorieInfraId,
                                                                                @RequestParam(value="typeMouvementId" ,  required = false) String typeMouvementId,
                                                                                @RequestParam(value = "modelesIds" , required = false) String[] modelesIds
    ){
        return ResponseEntity.ok(service.findAllCriteria(date1 ,date2 , year , categorieInfraId , typeMouvementId , modelesIds)) ;
    }

    @PostMapping("/mouvement/dashboard/exportExcel")
    public ResponseEntity<?>  exportHistoriqueMvtByMouvement_Id(@RequestParam(value = "id" , required = false) Integer year  ,
                                                                                @RequestParam(value="date1" , required = false) Date date1 ,
                                                                                @RequestParam(value="date2" ,  required = false) Date date2,
                                                                                @RequestParam(value="categorieInfraId" ,  required = false) String categorieInfraId,
                                                                                @RequestParam(value="typeMouvementId" ,  required = false) String typeMouvementId,
                                                                                @RequestParam(value = "modelesIds" , required = false) String[] modelesIds
    ){
        try{
            List<HistoriqueMvtDto> historiqueMvts =
                    service.findAllCriteria(date1, date2, year, categorieInfraId, typeMouvementId, modelesIds)
                            .stream()
                            .map(HistoriqueMvtDto::new)
                            .collect(Collectors.toList());
            ByteArrayInputStream excelFile = excelService.exportToExcel(historiqueMvts, HistoriqueMvtDto.class);
            String filename = "Historique_Mvt.xlsx";
            ByteArrayResource resource = new ByteArrayResource(excelFile.readAllBytes());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }



}
