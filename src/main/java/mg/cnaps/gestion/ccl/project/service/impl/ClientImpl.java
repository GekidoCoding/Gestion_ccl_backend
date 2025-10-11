package mg.cnaps.gestion.ccl.project.service.impl;

import mg.cnaps.gestion.ccl.framework.check.util.ObjectComparator;
import mg.cnaps.gestion.ccl.framework.jpa.core.service.implementation.GenericServiceImpl;
import mg.cnaps.gestion.ccl.project.config.CclPropertyService;
import mg.cnaps.gestion.ccl.project.entity.*;
import mg.cnaps.gestion.ccl.project.repository.ClientRepo;
import mg.cnaps.gestion.ccl.project.repository.EtatRepo;
import mg.cnaps.gestion.ccl.project.repository.MouvementRepo;
import mg.cnaps.gestion.ccl.project.service.ClientService;
import mg.cnaps.gestion.ccl.project.util.PageUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClientImpl extends GenericServiceImpl<Client, String , ClientRepo> implements ClientService {
    private final EtatRepo etatRepo;
    private final CclPropertyService cclPropertyService;
    private final MouvementRepo mouvementRepo;
    public ClientImpl(ClientRepo repo , EtatRepo etatRepo , CclPropertyService cclPropertyService, MouvementRepo mouvementRepo) {
        super(repo);
        this.etatRepo = etatRepo;
        this.cclPropertyService = cclPropertyService;
        this.mouvementRepo = mouvementRepo;
    }

    @Override
    public List<Client> findAll() {
        List<Client> clients = repository.findAll();
        clients.sort(Comparator.comparing(Client::getDateInsert).reversed());
        return clients;
    }

    public List<Client> checkSimilarity(Client client){
        List<Client> clients = this.findAll()  ;
        List<Client> infraSimilaire = new ArrayList<>();
        for (Client infra : clients) {
            if(ObjectComparator.isDegreeAtteint(infra , client)){
                infraSimilaire.add(infra);
            }
        }
        return infraSimilaire;
    }
    @Override
    public Client save (Client entity ){
        if (entity.getEtat() == null  || entity.getEtat().getId() == null) {
            Etat etatDefaut = etatRepo.getEtatByCode(cclPropertyService.getActifCode());
            entity.setEtat(etatDefaut);
        }
        entity.setDateInsert( Timestamp.valueOf(LocalDateTime.now()));
        return  repository.save(entity);
    }

    private boolean ifParticipateInfrastructures(String clientId, List<Infrastructure> infrastructures) {
        if (infrastructures == null || infrastructures.isEmpty()) {
            return false;
        }

        Set<String> infraIds = infrastructures.stream()
                .map(Infrastructure::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        List<Mouvement> mouvements = mouvementRepo.getMouvementByClient_Id(clientId);

        for (Mouvement mouvement : mouvements) {
            if (mouvement.getMouvementInfras() != null) {
                boolean match = mouvement.getMouvementInfras().stream()
                        .map(MouvementInfra::getInfrastructure)
                        .filter(Objects::nonNull)
                        .map(Infrastructure::getId)
                        .anyMatch(infraIds::contains);
                if (match) {
                    return true;
                }
            }
        }

        return false;
    }


    @Override
    public Page<Client> findByCriteriaPaginated(int page, int pageSize, Client criteria, List<Infrastructure> infrastructures) {

        List<Client> allFiltered = this.findAll().stream()
                .filter(Objects::nonNull)
                .filter(client -> {
                    boolean matches = true;

                    if (criteria.getDesignationClient() != null && !criteria.getDesignationClient().isEmpty()) {
                        boolean nameMatch = (client.getDesignationClient() != null && client.getDesignationClient().contains(criteria.getDesignationClient()));
                        matches &= nameMatch;
                    }

                    if (criteria.getEmail() != null && !criteria.getEmail().isEmpty()) {
                        matches &= (client.getEmail() != null && client.getEmail().contains(criteria.getEmail()));
                    }

                    if (criteria.getCin() != null && !criteria.getCin().isEmpty()) {
                        matches &= (client.getCin() != null && client.getCin().contains(criteria.getCin()));
                    }

                    if (criteria.getFonction() != null && !criteria.getFonction().isEmpty()) {
                        matches &= (client.getFonction() != null && client.getFonction().contains(criteria.getFonction()));
                    }

                    if (criteria.getContacts() != null && !criteria.getContacts().isEmpty()) {
                        matches &= (client.getContacts() != null && client.getContacts().contains(criteria.getContacts()));
                    }

                    if (criteria.getTypeClient() != null && criteria.getTypeClient().getId() != null
                            && !criteria.getTypeClient().getId().isEmpty()) {
                        matches &= (client.getTypeClient() != null && client.getTypeClient().getId() != null
                                && client.getTypeClient().getId().contains(criteria.getTypeClient().getId()));
                    }

                    if (infrastructures != null && !infrastructures.isEmpty()) {
                        matches &= ifParticipateInfrastructures(client.getId(), infrastructures);
                    }

                    return matches;
                })
                .collect(Collectors.toList());

        // Pagination
        List<Client> paged = new PageUtil<Client>().paginateList(page, pageSize, allFiltered);

        // Retourner la page avec totalElements correct
        return new PageImpl<>(paged, PageRequest.of(page, pageSize), allFiltered.size());
    }


    @Override
    public Integer getTotalPersonnes(String clientId){
        List<Mouvement> mvts = mouvementRepo.getMouvementByClient_Id(clientId).stream().filter(mouvement -> mouvement.getClient().getId().equals(clientId)
        ).collect(Collectors.toList());
        Integer totalPersonnes = 0;
        for (Mouvement mouvement : mvts) {
            totalPersonnes+=mouvement.getNombre();
        }
        return totalPersonnes;
    }
    @Override
    public Integer getTotalMouvements(String clientId){
        return mouvementRepo.getMouvementByClient_Id(clientId).size();
    }
    @Override
    public Page<Client> findPaginated(int page, int pageSize) {
        List<Client> allFiltered = this.findAll();

        List<Client> paged = new PageUtil<Client>().paginateList(page, pageSize, allFiltered);

        return new PageImpl<>(paged, PageRequest.of(page, pageSize), allFiltered.size());

    }
}