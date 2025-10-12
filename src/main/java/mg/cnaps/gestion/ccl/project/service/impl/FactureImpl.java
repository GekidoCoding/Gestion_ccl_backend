package mg.cnaps.gestion.ccl.project.service.impl;

import mg.cnaps.gestion.ccl.framework.jpa.core.service.implementation.GenericServiceImpl;
import mg.cnaps.gestion.ccl.project.config.CclPropertyService;
import mg.cnaps.gestion.ccl.project.entity.*;
import mg.cnaps.gestion.ccl.project.entity.dto.facture.FactureDto;
import mg.cnaps.gestion.ccl.project.entity.dto.mouvement.MouvementDto;
import mg.cnaps.gestion.ccl.project.repository.EtatRepo;
import mg.cnaps.gestion.ccl.project.repository.FactureRepo;
import mg.cnaps.gestion.ccl.project.repository.HistoFactureRepo;
import mg.cnaps.gestion.ccl.project.repository.MouvementRepo;
import mg.cnaps.gestion.ccl.project.service.EmailService;
import mg.cnaps.gestion.ccl.project.service.FactureService;
import mg.cnaps.gestion.ccl.project.service.PaiementService;
import mg.cnaps.gestion.ccl.project.util.Doubleutil;
import mg.cnaps.gestion.ccl.project.util.GestionnaireUtil;
import mg.cnaps.gestion.ccl.project.util.TimestampUtil;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FactureImpl extends GenericServiceImpl<Facture, String ,FactureRepo> implements FactureService {
    private final HistoFactureRepo histoFactureRepo;
    private final MouvementRepo mouvementRepo;
    private final CclPropertyService cclPropertyService;
    private final PaiementService paiementService;
    private final EtatRepo etatRepo;
    private final GestionnaireUtil gestionnaireUtil;
    private final EmailService emailService;
    public FactureImpl(FactureRepo repo, HistoFactureRepo histoFactureRepo, MouvementRepo mouvementRepo, CclPropertyService cclPropertyService, PaiementService paiementService, EtatRepo etatRepo, GestionnaireUtil gestionnaireUtil, EmailService emailService) {
        super(repo);
        this.histoFactureRepo = histoFactureRepo;
        this.mouvementRepo = mouvementRepo;
        this.cclPropertyService = cclPropertyService;
        this.paiementService = paiementService;
        this.etatRepo = etatRepo;
        this.gestionnaireUtil = gestionnaireUtil;
        this.emailService = emailService;
    }
    @Override
    public void delete(String factureId) {
        try{
            List<Paiement> paiements = this.paiementService.findPaiementByFacture_Id(factureId);
            for (Paiement paiement : paiements) {
                this.paiementService.delete(paiement.getId());
            }
            List<HistoFacture> histoFactures = this.histoFactureRepo.getHistoFactureByFacture_Id(factureId);
            histoFactureRepo.deleteAll(histoFactures);
            this.repository.deleteById(factureId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Facture> findAll(){
        return this.repository.findAll().stream().filter( f-> !Objects.equals(f.getEtat().getCode(), cclPropertyService.getInactifCode())).collect(Collectors.toList());
    }

    @Override
    public List<Facture> getFacturesByMouvement_Id_findAll(String mouvementId){
        List<Facture> factures = this.findAll();
        List<Facture> newFactures = new ArrayList<>();
        for (Facture facture : factures) {
            if(facture.getMouvement().getId().equals(mouvementId)){
                newFactures.add(facture);
            }
        }
        return newFactures;
    }


    @Override
    public List<FactureDto> getFacturesReellePayeByMouvement_Id(String mouvementId) {
        List<Facture> factures = repository.getFacturesByMouvement_Id(mouvementId);

        return factures.stream()
                .filter(f -> f.getEtat() != null &&
                        (f.getEtat().getCode().equals(cclPropertyService.getReelleCode()) ||
                                f.getEtat().getCode().equals(cclPropertyService.getPayeCode())))
                .map(facture -> {
                    FactureDto dto = new FactureDto(facture);

                    Double totalDu = dto.getTotalDu();
                    Double totalAcompte = this.paiementService.getTotalAcompteByFacture_Id(facture.getId());

                    double reste = totalDu - totalAcompte;
                    dto.setReste(Math.max(0, reste));
                    dto.setTotalAcompte(totalAcompte);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public byte[] getBytePdfPaiement(String idPaiement) {
        try{
            Paiement paiement = this.paiementService.findById(idPaiement);
            JasperReport jasperReport = this.loadReportTemplate(cclPropertyService.getFactureReelleExportPath());
            Map<String, Object> params = this.buildReportParametersRelle(paiement);
            List<Map<String, Object>> dataList = this.buildReportData(new MouvementDto(paiement.getFacture().getMouvement()));
            JasperPrint jasperPrint = this.fillReport(jasperReport, params, dataList);
            return this.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
   }

    @Override
    public List<FactureDto> getFacturesByMouvement_Id(String mouvementId) {
        List<Facture> factures = repository.getFacturesByMouvement_Id(mouvementId);

        return factures.stream()
                .map(facture -> {
                    FactureDto dto = new FactureDto(facture);

                    Double totalDu = dto.getTotalDu();
                    Double totalAcompte = this.paiementService.getTotalAcompteByFacture_Id(facture.getId());

                    double reste = totalDu - totalAcompte;
                    dto.setReste(Math.max(0, reste));
                    dto.setTotalAcompte(totalAcompte);

                    return dto;
                })
                .collect(Collectors.toList());
    }



    @Override
    public Facture findById(String id){
        List<Facture> factures = repository.findAll();
        return factures.stream().filter(f -> f.getId().equals(id)).findFirst().orElse(null);
    }


    @Override
    public FactureDto findByIdWithReste(String id) {
        try{
            List<Facture> factures = repository.findAll();
            Facture facture = factures.stream()
                    .filter(f -> f.getId().equals(id))
                    .findFirst()
                    .orElse(null);

            assert facture != null;
            FactureDto factureDto = new FactureDto(facture);

            Double totalDu = factureDto.getTotalDu();
            Double totalAcompte = this.paiementService.getTotalAcompteByFacture_Id(id);

            double reste = totalDu - totalAcompte;
            factureDto.setReste(Math.max(0, reste));


            return factureDto;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    @Override
    public Facture save(Facture facture, String matricule){
        Gestionnaire gestionnaire = this.gestionnaireUtil.getGestionnaireHisto(matricule);
        facture.setDhCreation(Timestamp.valueOf(LocalDateTime.now()));
        facture = repository.save(facture);
        facture.setRefFacture(facture.getRefFacture());
        facture = repository.save(facture);

        HistoFacture histoFacture= this.getHistoFacture(facture , gestionnaire);

        histoFactureRepo.save(histoFacture);

        return facture;
    }
    @Override
    public List<HistoFacture> getHistoFactureByFacture_Id(String id) {
        return histoFactureRepo.findAll()
                .stream()
                .filter(h -> h.getFacture() != null && id.equals(h.getFacture().getId()))
                .collect(Collectors.toList());
    }



    @Override
    public Facture update(Facture facture, String id, String matricule){
        Gestionnaire gestionnaire = this.gestionnaireUtil.getGestionnaireHisto(matricule);
        if(!repository.existsById(id)){
            throw new RuntimeException("Not Found ID ");
        }
        facture.setDhCreation(Timestamp.valueOf(LocalDateTime.now()));
        facture = repository.save(facture);

        HistoFacture isExist = null;
        List<HistoFacture> historiqueMvts = this.getHistoFactureByFacture_Id(id);
        for (HistoFacture histo: historiqueMvts ) {
            if(histo.getEtat().getId().equals(facture.getEtat().getId()) ) {
                isExist = histo;
                break;
            }
        }
        if(isExist==null){
            HistoFacture histoFacture= this.getHistoFacture(facture , gestionnaire);
            histoFactureRepo.save(histoFacture);
        }
        return facture;
    }
    private HistoFacture getHistoFacture(Facture facture , Gestionnaire gestionnaire){
        HistoFacture  histoFacture = new HistoFacture();
        histoFacture.setFacture(facture);
        histoFacture.setEtat(facture.getEtat());
        histoFacture.setRefFacture(facture.getRefFacture());
        histoFacture.setMontant(facture.getMontantTotal());
        histoFacture.setGestionnaire(gestionnaire);
        histoFacture.setDhAction(Timestamp.valueOf(LocalDateTime.now()));
        histoFacture.setTotalDu(facture.getTotalDu());
        histoFacture.setRemise(facture.getRemise());
        return histoFacture;
    }

    @Override
    public Double calculateDuree(Timestamp periodeDebut, Timestamp periodeFin, String frequenceId){
        return TimestampUtil.caculateDifferenceTimestamp(cclPropertyService, periodeDebut , periodeFin, frequenceId);
    }

    @Override
    public Facture getDefaultFactureProformaByMouvement_Id(String mouvementId) {
        return buildDefaultFacture(mouvementId, true);
    }

    @Override
    public Facture getDefaultFactureRelleByMouvement_Id(String mouvementId) {
        return buildDefaultFacture(mouvementId, false);
    }

    private Facture buildDefaultFacture(String mouvementId, boolean isProforma) {
        Facture factureDefault = new Facture();
        Mouvement mouvement = mouvementRepo.findById(mouvementId).orElse(null);
        assert mouvement != null;

        if (mouvement.getTypeMouvement().getId().equals(cclPropertyService.getReservationId())
                || mouvement.getTypeMouvement().getId().equals(cclPropertyService.getOccupationId())) {

            double remise = cclPropertyService.getFactureRemise();
            Timestamp periodeDebut = mouvement.getPeriodeDebut();
            Timestamp periodeFin = mouvement.getPeriodeFin();
            double montantTotal = 0.0;

            for (MouvementInfra mvtInfra : mouvement.getMouvementInfras()) {
                Double tarifUnitaire = getTarifUnitaire(mvtInfra);
                Double duree;


                duree = calculateDuree(periodeDebut, periodeFin, mvtInfra.getFrequence().getId());


                montantTotal += duree * tarifUnitaire;
            }

            double acompteVerse = montantTotal - (montantTotal * (remise / 100));

            factureDefault.setDhCreation(Timestamp.valueOf(LocalDateTime.now()));
            factureDefault.setMouvement(mouvement);
            factureDefault.setTotalDu(acompteVerse);
            factureDefault.setMontantTotal(montantTotal);
            factureDefault.setRemise(remise);

            Integer codeEtat = isProforma ? cclPropertyService.getProformaCode()
                    : cclPropertyService.getReelleCode();
            factureDefault.setEtat(etatRepo.getEtatByCode(codeEtat));

        }

        return factureDefault;
    }



    @Override
    public Double getTarifUnitaire(MouvementInfra mvtInfra) {
        Frequence frequence = mvtInfra.getFrequence();
        List<InfraTarif> tarifs=  mvtInfra.getInfrastructure().getInfraTarifs();
        Double tarifUnitaire=0.0;
        for (InfraTarif infraTarif:tarifs){
            if(infraTarif.getFrequence().getId().equals(frequence.getId() )){
                tarifUnitaire = infraTarif.getTarifInfra();
                break;
            }
        }

        return tarifUnitaire;
    }

    @Override
    public Facture getFactureReelleByMouvementId(String mouvementId){
        List<Facture> factures= this.repository.getFacturesByMouvement_Id(mouvementId);
        for (Facture facture:factures){
            if(!facture.getEtat().getCode().equals(cclPropertyService.getProformaCode())){
                return facture;
            }
        }
        return null;
    }

    @Override
    public JasperReport loadReportTemplate(String path) throws JRException {
        InputStream reportStream = getClass().getResourceAsStream(path);
        if (reportStream == null) {
            throw new JRException("Report file not found: " + path);
        }
        return JasperCompileManager.compileReport(reportStream);
    }
    @Override
    public Facture createAndSaveFacture(String idMouvement) {
        Facture factureDefault = this.getDefaultFactureProformaByMouvement_Id(idMouvement);
        return this.save(factureDefault);
    }
    @Override
    public Map<String, Object> buildReportParameters(Facture facture) {
        MouvementDto factureMouvement = new MouvementDto(facture.getMouvement());

        Map<String, Object> params = new HashMap<>();
        params.put("dateFacture", facture.getDhCreationNoHours());
        params.put("denomination", facture.getMouvement().getClient().getDesignationClient());
        params.put("cin", facture.getMouvement().getClient().getCin());
        params.put("adresse", facture.getMouvement().getClient().getAdresse());
        params.put("telephone", facture.getMouvement().getClient().getContacts());
        params.put("email", facture.getMouvement().getClient().getEmail());
        params.put("reff", facture.getRefFacture());
        params.put("dateDebutReservation", factureMouvement.getPeriodeDebut());
        params.put("dateFinReservation", factureMouvement.getPeriodeFin());
        params.put("somme", facture.getTotalDu());
        params.put("montantEnLettre", Doubleutil.convert(facture.getTotalDu().longValue()));
        return params;
    }

    @Override
    public Map<String, Object> buildReportParametersRelle(Paiement paiement) {
        Map<String , Object> params= this.buildReportParameters(paiement.getFacture());
        params.put("acompte" , paiement.getAcompteVerse());
        params.put("modePaiement" , paiement.getModePaiement().getLibelle());
        double reste =paiement.getFacture().getTotalDu()- this.paiementService.getTotalAcompteByFacture_Id(paiement.getFacture().getId());
        reste = Math.max(reste, 0);
        double remise =paiement.getFacture().getRemise();
        params.put("reste" , reste  );
        params.put("remise" ,remise );
        return params;
    }
    @Override
    public List<Map<String, Object>> buildReportData(MouvementDto factureMouvement) {
        List<Map<String, Object>> dataList = new ArrayList<>();

        for (MouvementInfra detail : factureMouvement.getMouvementInfras()) {
            Double tarifUnitaire = this.getTarifUnitaire(detail);
            Double duree = this.calculateDuree(
                    detail.getMouvement().getPeriodeDebut(),
                    detail.getMouvement().getPeriodeFin(),
                    detail.getFrequence().getId()
            );
            Map<String, Object> line = new HashMap<>();
            line.put("designation", detail.getInfrastructure().getNom() + " - " + detail.getInfrastructure().getNumero());
            line.put("coeffQuantite", duree);
            line.put("frequence", detail.getFrequence().getLibelle());
            line.put("prixUnitaire", tarifUnitaire);
            line.put("prixTotal", tarifUnitaire*duree);

            dataList.add(line);
        }
        return dataList;
    }

    @Override
    public List<Map<String, Object>> buildReportDataReelle(Facture facture) {
        List<Map<String, Object>> dataList = new ArrayList<>();
        for (MouvementInfra detail : facture.getMouvement().getMouvementInfras()) {
            Double tarifUnitaire = this.getTarifUnitaire(detail);
            Double duree = this.calculateDuree(
                    detail.getMouvement().getPeriodeDebut(),
                    detail.getMouvement().getPeriodeFin(),
                    detail.getFrequence().getId()
            );

            Map<String, Object> line = new HashMap<>();
            line.put("designation", detail.getInfrastructure().getNom() + " - " + detail.getInfrastructure().getNumero());
            line.put("coeffQuantite", duree);
            line.put("frequence", detail.getFrequence().getLibelle());
            line.put("prixUnitaire", tarifUnitaire);
            line.put("prixTotal", tarifUnitaire*duree);

            dataList.add(line);
        }
        return dataList;
    }

    @Override
    public JasperPrint fillReport(JasperReport jasperReport, Map<String, Object> params, List<Map<String, Object>> dataList) throws JRException {
        JRDataSource dataSource = new JRBeanCollectionDataSource(dataList);
        return JasperFillManager.fillReport(jasperReport, params, dataSource);
    }
    @Override
    public byte[] exportReportToPdf(JasperPrint jasperPrint) throws JRException {
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    @Override
    public byte[] buildFacturePdf( String idFacture) throws JRException {
        Facture newFacture = this.findById(idFacture);
        return getBytes(newFacture);

    }
    @Override
    public byte[] buildFacturePdfWithFacture(Facture newFacture) throws JRException {
        return getBytes(newFacture);

    }

    private byte[] getBytes(Facture newFacture) throws JRException {
        JasperReport jasperReport ;
        if(newFacture.getEtat().getCode().equals(cclPropertyService.getProformaCode())){
            jasperReport = this.loadReportTemplate(cclPropertyService.getFactureProformaExportPath());
        }else {
            jasperReport = this.loadReportTemplate(cclPropertyService.getFactureReelleExportPath());
        }

        Map<String, Object> params = this.buildReportParameters(newFacture);

        List<Map<String, Object>> dataList = this.buildReportData(new MouvementDto(newFacture.getMouvement()));

        JasperPrint jasperPrint = this.fillReport(jasperReport, params, dataList);

        return this.exportReportToPdf(jasperPrint);
    }


    @Override
    public void sendEmailForFactureWithPdf(String idFacture, String[] destinataires)  {
        try{

            Facture newFacture = this.findById(idFacture);
            byte[] pdfFacture = this.getBytes(newFacture);
            String objet = "";
            String message = "";
            String filename = "";

            if (newFacture.getEtat().getCode().equals(cclPropertyService.getProformaCode())) {
                objet = "Envoi de votre facture proforma pour réservation";
                filename = "proforma.pdf";
                message = "Bonjour,\n\nVeuillez trouver ci-joint la facture proforma correspondant à votre réservation.\n\n" +
                        "Nous vous remercions de votre confiance et restons à votre disposition pour toute question.\n\n" +
                        "Cordialement,\nL'équipe de CNaPS Madagascar";
            }

            if (newFacture.getEtat().getCode().equals(cclPropertyService.getReelleCode())) {
                objet = "Envoi de votre facture définitive pour réservation";
                filename = "facture.pdf";
                message = "Bonjour,\n\nVeuillez trouver ci-joint la facture définitive correspondant à votre réservation.\n\n" +
                        "Nous vous remercions pour votre règlement et restons à votre disposition pour toute question.\n\n" +
                        "Cordialement,\nL'équipe de CNaPS Madagascar";
            }

              this.emailService.sendEmailWithPdfBytes(destinataires, objet, message, pdfFacture, filename);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


}

