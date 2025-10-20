package mg.cnaps.gestion.ccl.project.service;

import mg.cnaps.gestion.ccl.framework.jpa.core.service.GenericService;
import mg.cnaps.gestion.ccl.project.entity.Facture;
import mg.cnaps.gestion.ccl.project.entity.HistoFacture;
import mg.cnaps.gestion.ccl.project.entity.MouvementInfra;
import mg.cnaps.gestion.ccl.project.entity.Paiement;
import mg.cnaps.gestion.ccl.project.entity.dto.facture.FactureDto;
import mg.cnaps.gestion.ccl.project.entity.dto.mouvement.MouvementDto;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import javax.mail.MessagingException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;


public interface FactureService extends GenericService<Facture, String> {
    List<Facture> getFacturesByMouvement_Id_findAll(String mouvementId);

    List<FactureDto> getFacturesReellePayeByMouvement_Id(String mouvementId);

    byte[] getBytePdfPaiement(String idPaiement);

    List<FactureDto> getFacturesByMouvement_Id(String mouvementId);

     FactureDto findByIdWithReste(String id);

    Facture save(Facture facture, String matricule);

    List<HistoFacture> getHistoFactureByFacture_Id(String id);

    Facture update(Facture facture, String id, String matricule);

    Double calculateDuree(Timestamp periodeDebut, Timestamp periodeFin, String frequenceId);

     Facture getDefaultFactureProformaByMouvement_Id(String mouvementId);

     Facture getDefaultFactureRelleByMouvement_Id(String mouvementId);

     Double getTarifUnitaire(MouvementInfra mvtInfra);

     Facture getFactureReelleByMouvementId(String mouvementId);

     JasperReport loadReportTemplate(String path) throws JRException;

     Facture createAndSaveFacture(String idMouvement);

     Map<String, Object> buildReportParameters(Facture facture);


     Map<String, Object> buildReportParametersRelle(Paiement paiement);

     List<Map<String, Object>> buildReportData(MouvementDto factureMouvement);

     List<Map<String, Object>> buildReportDataReelle(Facture facture);

     JasperPrint fillReport(JasperReport jasperReport, Map<String, Object> params, List<Map<String, Object>> dataList) throws JRException;

     byte[] exportReportToPdf(JasperPrint jasperPrint) throws JRException;


    byte[] buildFacturePdf( String idFacture) throws JRException;

    byte[] buildFacturePdfWithFacture(Facture newFacture) throws JRException;

    byte[] buildContratPdf(String idFacture) throws JRException;

    void sendEmailForFactureWithPdf(String idFacture, String[] destinataires) ;

    void sendEmailForPaiementWithPdf(String idPaiement, String[] destinataires);

    void sendEmailForContratWithPdf(String idFacture, String[] destinataires);
}