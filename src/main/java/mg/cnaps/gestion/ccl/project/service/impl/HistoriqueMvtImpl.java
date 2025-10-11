package mg.cnaps.gestion.ccl.project.service.impl;

import mg.cnaps.gestion.ccl.framework.jpa.core.service.implementation.GenericServiceImpl;
import mg.cnaps.gestion.ccl.project.config.CclPropertyService;
import mg.cnaps.gestion.ccl.project.entity.Client;
import mg.cnaps.gestion.ccl.project.entity.HistoriqueMvt;
import mg.cnaps.gestion.ccl.project.entity.Infrastructure;
import mg.cnaps.gestion.ccl.project.repository.ClientRepo;
import mg.cnaps.gestion.ccl.project.repository.HistoriqueMvtRepo;
import mg.cnaps.gestion.ccl.project.repository.InfrastructureRepo;
import mg.cnaps.gestion.ccl.project.service.HistoriqueMvtService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HistoriqueMvtImpl extends GenericServiceImpl<HistoriqueMvt, String , HistoriqueMvtRepo> implements HistoriqueMvtService {
    private final  CclPropertyService cclPropertyService;
    private final InfrastructureRepo infrastructureRepo;
    private final ClientRepo clientRepo;
    public HistoriqueMvtImpl(HistoriqueMvtRepo repo, CclPropertyService cclPropertyService, InfrastructureRepo infrastructureRepo, ClientRepo clientRepo) {
        super(repo);
        this.cclPropertyService = cclPropertyService;
        this.infrastructureRepo = infrastructureRepo;
        this.clientRepo = clientRepo;
    }

    @Override
    public List<HistoriqueMvt> findHistoriqueMvtByMouvement_Id(String mouvementId) {
        return repository.findHistoriqueMvtByMouvement_Id(mouvementId);
    }
    @Override
    public List<HistoriqueMvt> findHistoriqueMvtsByTypeMouvement_Id(String typeMouvementId) {
        return repository.findHistoriqueMvtsByTypeMouvement_Id(typeMouvementId);
    }


    private List<HistoriqueMvt> filterHistoriqueMvts(List<HistoriqueMvt> historiqueMvts,
                                                     Date date1, Date date2, Integer year,
                                                     String categorieInfraId, String typeMouvementId, String[] modelesIds) {

        int finalYear = (year != null) ? year : LocalDate.now().getYear();

        return historiqueMvts.stream()
                .filter(histo -> {
                    Timestamp dhAction = histo.getDhActionTimestamp();
                    if (dhAction == null) return false;

                    // Vérif année
                    boolean yearMatch = dhAction.toLocalDateTime().getYear() == finalYear;

                    // Vérif plage de dates
                    boolean dateMatch = true;
                    if (date1 != null) {
                        dateMatch = !dhAction.before(new Timestamp(date1.getTime()));
                    }
                    if (date2 != null) {
                        dateMatch = dateMatch && !dhAction.after(new Timestamp(date2.getTime()));
                    }

                    // Vérif catégorie
                    boolean correctCategorie = (categorieInfraId == null || categorieInfraId.isEmpty())
                            || histo.getMouvement().getMouvementInfras().stream()
                            .anyMatch(minf -> categorieInfraId.equals(
                                    minf.getInfrastructure().getModeleInfra().getCatInfra().getId()
                            ));

                    // Vérif modèles
                    boolean correctModelesId = (modelesIds == null || modelesIds.length == 0)
                            || Arrays.stream(modelesIds).anyMatch(modelesId ->
                            histo.getMouvement().getMouvementInfras().stream()
                                    .anyMatch(mouvementInfra ->
                                            modelesId.equals(
                                                    mouvementInfra.getInfrastructure().getModeleInfra().getId()
                                            )
                                    )
                    );

                    // Vérif type mouvement
                    boolean correctTypeMouvement = (typeMouvementId == null || typeMouvementId.isEmpty())
                            || typeMouvementId.equals(histo.getMouvement().getTypeMouvement().getId());

                    return yearMatch && dateMatch && correctCategorie && correctModelesId && correctTypeMouvement;
                })
                .collect(Collectors.toList());
    }


    @Override
    public List<HistoriqueMvt> findAllCriteria(Date date1, Date date2, Integer year,
                                               String categorieInfraId, String typeMouvementId, String[] modelesIds) {
        List<HistoriqueMvt> historiqueMvts = this.findAll();
        if (year == null) {
            year = LocalDate.now().getYear();
        }
        Integer finalYear = year;
        return this.filterHistoriqueMvts(historiqueMvts, date1, date2, finalYear, categorieInfraId, typeMouvementId, modelesIds);
    }

    @Override
    public List<Integer> getTotalDashboard(Date date1, Date date2, Integer year,
                                           String categorieInfraId, String typeMouvementId, String[] modelesIds) {
        List<HistoriqueMvt> histoReservations = this.findHistoriqueMvtsByTypeMouvement_Id(cclPropertyService.getReservationId());
        List<HistoriqueMvt> histoRenseignements = this.findHistoriqueMvtsByTypeMouvement_Id(cclPropertyService.getRenseignementId());
        List<Infrastructure> infrastructures = this.infrastructureRepo.findAll();
        List<Client> clients = this.clientRepo.findAll();

        histoReservations = filterHistoriqueMvts(histoReservations, date1, date2, year, categorieInfraId, typeMouvementId, modelesIds);
        histoRenseignements = filterHistoriqueMvts(histoRenseignements, date1, date2, year, categorieInfraId, typeMouvementId, modelesIds);

        List<Integer> totals = new ArrayList<>();
        totals.add(histoReservations.size());
        totals.add(histoRenseignements.size());
        totals.add(infrastructures.size());
        totals.add(clients.size());

        return totals;
    }

    @Override
    public List<Double> getPourcentages(Date date1, Date date2, Integer year,
                                        String categorieInfraId, String typeMouvementId, String[] modelesIds) {
        List<HistoriqueMvt> historiqueMvts = this.findAll();
        List<HistoriqueMvt> histoReservations = this.findHistoriqueMvtsByTypeMouvement_Id(cclPropertyService.getReservationId());
        List<HistoriqueMvt> histoRenseignements = this.findHistoriqueMvtsByTypeMouvement_Id(cclPropertyService.getRenseignementId());
        List<HistoriqueMvt> histoClassements = this.findHistoriqueMvtsByTypeMouvement_Id(cclPropertyService.getClassementId());
        List<HistoriqueMvt> histoOccupations = this.findHistoriqueMvtsByTypeMouvement_Id(cclPropertyService.getOccupationId());

        historiqueMvts = filterHistoriqueMvts(historiqueMvts, date1, date2, year, categorieInfraId, typeMouvementId, modelesIds);
        histoReservations = filterHistoriqueMvts(histoReservations, date1, date2, year, categorieInfraId, typeMouvementId, modelesIds);
        histoRenseignements = filterHistoriqueMvts(histoRenseignements, date1, date2, year, categorieInfraId, typeMouvementId, modelesIds);
        histoClassements = filterHistoriqueMvts(histoClassements, date1, date2, year, categorieInfraId, typeMouvementId, modelesIds);
        histoOccupations = filterHistoriqueMvts(histoOccupations, date1, date2, year, categorieInfraId, typeMouvementId, modelesIds);

        double totalMvts = historiqueMvts.size();
        List<Double> percentages = new ArrayList<>();

        if (totalMvts == 0) {
            percentages.add(0.00);
            percentages.add(0.00);
            percentages.add(0.00);
            percentages.add(0.00);
        } else {
            percentages.add((double) Math.round(histoClassements.size() / totalMvts * 100.0));
            percentages.add((double) Math.round(histoRenseignements.size() / totalMvts * 100.0));
            percentages.add((double) Math.round(histoReservations.size() / totalMvts * 100.0));
            percentages.add((double) Math.round(histoOccupations.size() / totalMvts * 100.0));

            double sum = percentages.stream().mapToDouble(Double::doubleValue).sum();
            if (sum != 100.0) {
                double diff = 100.0 - sum;
                percentages.set(percentages.size() - 1, percentages.get(percentages.size() - 1) + diff);
            }
        }

        return percentages;
    }

    @Override
    public List<List<Integer>> getMonthlyData(Date date1, Date date2, Integer year,
                                              String categorieInfraId, String typeMouvementId, String[] modelesIds) {
        if (year == null) {
            year = LocalDate.now().getYear();
        }
        int finalYear = year;

        List<Integer> reservations = new ArrayList<>(Collections.nCopies(12, 0));
        List<Integer> renseignements = new ArrayList<>(Collections.nCopies(12, 0));
        List<Integer> clients = new ArrayList<>(Collections.nCopies(12, 0));

        List<HistoriqueMvt> histoReservations = this.findHistoriqueMvtsByTypeMouvement_Id(cclPropertyService.getReservationId());
        List<HistoriqueMvt> histoRenseignements = this.findHistoriqueMvtsByTypeMouvement_Id(cclPropertyService.getRenseignementId());
        List<Client> allClients = this.clientRepo.findAll();

        histoReservations = filterHistoriqueMvts(histoReservations, date1, date2, year, categorieInfraId, typeMouvementId, modelesIds);
        histoRenseignements = filterHistoriqueMvts(histoRenseignements, date1, date2, year, categorieInfraId, typeMouvementId, modelesIds);

        histoReservations.forEach(histo -> {
            int month = histo.getDhActionTimestamp().toLocalDateTime().getMonthValue() - 1;
            reservations.set(month, reservations.get(month) + 1);
        });

        histoRenseignements.forEach(histo -> {
            int month = histo.getDhActionTimestamp().toLocalDateTime().getMonthValue() - 1;
            renseignements.set(month, renseignements.get(month) + 1);
        });

        allClients.stream()
                .filter(client -> {
                    Timestamp dateInsert = client.getDateInsert();
                    if (dateInsert == null) return false;

                    boolean yearMatch = dateInsert.toLocalDateTime().getYear() == finalYear;

                    boolean dateMatch = true;
                    if (date1 != null) {
                        Timestamp startDate = new Timestamp(date1.getTime());
                        dateMatch = !dateInsert.before(startDate);
                    }
                    if (date2 != null) {
                        Timestamp endDate = new Timestamp(date2.getTime());
                        dateMatch = dateMatch && !dateInsert.after(endDate);
                    }

                    return yearMatch && dateMatch;
                })
                .forEach(client -> {
                    int month = client.getDateInsert().toLocalDateTime().getMonthValue() - 1;
                    clients.set(month, clients.get(month) + 1);
                });

        List<List<Integer>> result = new ArrayList<>();
        result.add(reservations);
        result.add(renseignements);
        result.add(clients);

        return result;
    }


}