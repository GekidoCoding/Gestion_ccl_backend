package mg.cnaps.gestion.ccl.project.service.impl;

import mg.cnaps.gestion.ccl.framework.core.service.implementation.GenericServiceImpl;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
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


    private List<HistoriqueMvt> filterHistoriqueMvts(List<HistoriqueMvt> historiqueMvts, Date date1, Date date2, Integer year ,  String categorieInfraId , String typeMouvementId) {
        if (year == null) {
            year = LocalDate.now().getYear();
        }
        int finalYear = year;

        return historiqueMvts.stream()
                .filter(histo -> {
                    Timestamp dhAction = histo.getDhActionTimestamp();
                    if (dhAction == null) return false;

                    boolean yearMatch = dhAction.toLocalDateTime().getYear() == finalYear;
                    boolean correctCategorie=false;
                    boolean correctTypeMouvement=false;
                    boolean dateMatch = true;
                    if (date1 != null) {
                        Timestamp startDate = new Timestamp(date1.getTime());
                        dateMatch = !dhAction.before(startDate);
                    }
                    if (date2 != null) {
                        Timestamp endDate = new Timestamp(date2.getTime());
                        dateMatch = dateMatch && !dhAction.after(endDate);
                    }
                    if(categorieInfraId==null || !categorieInfraId.isEmpty() && categorieInfraId.equals(histo.getMouvement().getInfrastructure().getModeleInfra().getCatInfra().getId())){
                        correctCategorie=true;
                    }
                    if(typeMouvementId==null || !typeMouvementId.isEmpty() && typeMouvementId.equals(histo.getMouvement().getTypeMouvement().getId() )){
                        correctTypeMouvement=true;
                    }

                    return yearMatch && dateMatch && correctCategorie && correctTypeMouvement;
                })
                .collect(Collectors.toList());
    }
    @Override
    public List<HistoriqueMvt> findAllCriteria(Date date1, Date date2, Integer year ,  String categorieInfraId , String typeMouvementId) {
        List<HistoriqueMvt> historiqueMvts =this.findAll();
        if (year == null) {
            year = LocalDate.now().getYear();
        }
        Integer finalYear = year;
        historiqueMvts = this.filterHistoriqueMvts(historiqueMvts, date1, date2, finalYear , categorieInfraId , typeMouvementId);
        return historiqueMvts;
    }


    @Override
    public List<Integer> getTotalDashboard(Date date1, Date date2, Integer year) {
        List<HistoriqueMvt> histoReservations = this.findHistoriqueMvtsByTypeMouvement_Id(cclPropertyService.getReservationId());
        System.out.println("histoReservations nombre: " + histoReservations.size());
        List<HistoriqueMvt> histoRenseignements = this.findHistoriqueMvtsByTypeMouvement_Id(cclPropertyService.getRenseignementId());
        System.out.println("histoRenseignements nombre: " + histoRenseignements.size());
        List<Infrastructure> infrastructures = this.infrastructureRepo.findAll();
        List<Client> clients = this.clientRepo.findAll();

        histoReservations = filterHistoriqueMvts(histoReservations, date1, date2, year , null , null);
        histoRenseignements = filterHistoriqueMvts(histoRenseignements, date1, date2, year ,  null , null);

        List<Integer> totals = new ArrayList<>();
        totals.add(histoReservations.size());
        totals.add(histoRenseignements.size());
        totals.add(infrastructures.size());
        totals.add(clients.size());

        return totals;
    }

    @Override
    public List<Double> getPourcentages(Date date1, Date date2, Integer year) {
        List<HistoriqueMvt> historiqueMvts = this.findAll();
        System.out.println("historiqueMvts.size(): " + historiqueMvts.size());
        List<HistoriqueMvt> histoReservations = this.findHistoriqueMvtsByTypeMouvement_Id(cclPropertyService.getReservationId());
        List<HistoriqueMvt> histoRenseignements = this.findHistoriqueMvtsByTypeMouvement_Id(cclPropertyService.getRenseignementId());
        List<HistoriqueMvt> histoClassements = this.findHistoriqueMvtsByTypeMouvement_Id(cclPropertyService.getClassementId());
        List<HistoriqueMvt> histoOccupations = this.findHistoriqueMvtsByTypeMouvement_Id(cclPropertyService.getOccupationId());

        historiqueMvts = filterHistoriqueMvts(historiqueMvts, date1, date2, year ,  null , null );
        histoReservations = filterHistoriqueMvts(histoReservations, date1, date2, year ,  null , null);
        histoRenseignements = filterHistoriqueMvts(histoRenseignements, date1, date2, year ,  null , null);
        histoClassements = filterHistoriqueMvts(histoClassements, date1, date2, year ,  null , null);
        histoOccupations = filterHistoriqueMvts(histoOccupations, date1, date2, year ,  null , null);

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
    public List<List<Integer>> getMonthlyData(Date date1, Date date2, Integer year) {
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

        histoReservations = filterHistoriqueMvts(histoReservations, date1, date2, year , null , null);
        histoRenseignements = filterHistoriqueMvts(histoRenseignements, date1, date2, year , null , null);

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