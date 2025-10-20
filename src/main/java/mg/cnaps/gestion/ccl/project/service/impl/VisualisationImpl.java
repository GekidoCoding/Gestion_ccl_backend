package mg.cnaps.gestion.ccl.project.service.impl;

import mg.cnaps.gestion.ccl.project.entity.*;
import mg.cnaps.gestion.ccl.project.entity.dto.historique.HistoFactureDto;
import mg.cnaps.gestion.ccl.project.entity.dto.infrastructure.InfrastructureVisuDto;
import mg.cnaps.gestion.ccl.project.entity.dto.mouvement.MouvementVisualisationDto;
import mg.cnaps.gestion.ccl.project.entity.dto.paiement.PaiementDto;
import mg.cnaps.gestion.ccl.project.repository.MouvementRepo;
import mg.cnaps.gestion.ccl.project.service.FactureService;
import mg.cnaps.gestion.ccl.project.service.HistoFactureService;
import mg.cnaps.gestion.ccl.project.service.HistoriqueMvtService;
import mg.cnaps.gestion.ccl.project.service.PaiementService;
import mg.cnaps.gestion.ccl.project.util.TimestampUtil;
import net.sf.jasperreports.engine.JRException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VisualisationImpl {
    private final FactureService factureService;
    private final MouvementRepo mouvementRepo;
    private final HistoFactureService histoFactureService;
    private final HistoriqueMvtService historiqueMvtService;
    private final PaiementService paiementService;
    public VisualisationImpl(FactureService factureService, MouvementRepo mouvementRepo, HistoFactureService histoFactureService, HistoriqueMvtService historiqueMvtService, PaiementService paiementService) {
        this.factureService = factureService;
        this.mouvementRepo = mouvementRepo;
        this.histoFactureService = histoFactureService;
        this.historiqueMvtService = historiqueMvtService;
        this.paiementService = paiementService;
    }

    public MouvementVisualisationDto findVisualisationByMouvementId(String mouvementId) throws JRException {
        MouvementVisualisationDto mouvementClasseDto = new MouvementVisualisationDto();
        Mouvement mouvement = this.mouvementRepo.findById(mouvementId).orElse(null);
        assert mouvement != null;
        mouvementClasseDto.setClient(mouvement.getClient().getDesignationClient()+" - " + mouvement.getClient().getContacts());
        mouvementClasseDto.setObservation(mouvement.getObservation());
        mouvementClasseDto.setNombrePersonne(mouvement.getNombre());
        List<InfrastructureVisuDto> infrastructures  = new ArrayList<>();

        for (MouvementInfra mvtInfra:mouvement.getMouvementInfras()){
            infrastructures.add (new InfrastructureVisuDto( mvtInfra));
        }
        mouvementClasseDto.setInfrastructures(infrastructures);
        mouvementClasseDto.setTypeMouvement(mouvement.getTypeMouvement().getNom());
        mouvementClasseDto.setPeriodeDebut(TimestampUtil.formatTimestamp(mouvement.getPeriodeDebut()));
        mouvementClasseDto.setPeriodeFin(TimestampUtil.formatTimestamp(mouvement.getPeriodeFin()));

        List<HistoFacture> histoFactures = histoFactureService.getHistoByMouvementId(mouvement.getId());
        List<HistoFactureDto> histoFactureDtos = new ArrayList<>();

        for (HistoFacture hf : histoFactures){
            HistoFactureDto dto = new HistoFactureDto(hf);
            dto.setPdfProforma(this.factureService.buildFacturePdf(hf.getFacture().getId()));

            List<Paiement> paiements = this.paiementService.findPaiementByFacture_Id(hf.getFacture().getId());
            List<PaiementDto> paiementDtos = new ArrayList<>();
            for (Paiement paiement :paiements){
                PaiementDto paiementDto = new PaiementDto(paiement);
                paiementDto.setPdfPaiement(this.factureService.getBytePdfPaiement(paiement.getId()));
                paiementDtos.add(paiementDto);
            }
            dto.setPaiements(paiementDtos);
            histoFactureDtos.add(dto);
        }
        mouvementClasseDto.setHistoFactures(histoFactureDtos);
        mouvementClasseDto.setHistoriqueMvts(this.historiqueMvtService.findHistoriqueMvtByMouvement_Id(mouvement.getId()));

        return mouvementClasseDto;

    }

}
