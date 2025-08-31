package mg.cnaps.gestion.ccl.project.service.impl;

import mg.cnaps.gestion.ccl.framework.core.service.implementation.GenericServiceImpl;
import mg.cnaps.gestion.ccl.project.config.CclPropertyService;
import mg.cnaps.gestion.ccl.project.entity.Client;
import mg.cnaps.gestion.ccl.project.entity.Etat;
import mg.cnaps.gestion.ccl.project.entity.Infrastructure;
import mg.cnaps.gestion.ccl.project.entity.Mouvement;
import mg.cnaps.gestion.ccl.project.repository.ClientRepo;
import mg.cnaps.gestion.ccl.project.repository.EtatRepo;
import mg.cnaps.gestion.ccl.project.repository.MouvementRepo;
import mg.cnaps.gestion.ccl.project.service.ClientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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
    public Client save (Client entity ){
        System.out.println(entity.toString());
        if (entity.getEtat() == null  || entity.getEtat().getId() == null) {
            Etat etatDefaut = etatRepo.getEtatByCode(cclPropertyService.getActifCode());
            System.out.println("etat Defaut :"+ etatDefaut );
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
            Infrastructure infra = mouvement.getInfrastructure();
            if (infra != null && infra.getId() != null && infraIds.contains(infra.getId())) {
                return true;
            }
        }

        return false;
    }



    @Override
    public Page<Client> findByCriteriaPaginated(int page, int pageSize, Client criteria, List<Infrastructure> infrastructures) {
        Page<Client> clients = this.findPaginated(page, pageSize);

        List<Client> filteredClients = clients.getContent().stream()
                .filter(client -> {
                    boolean matches = true;

                    if (criteria.getNom() != null && !criteria.getNom().isEmpty()) {
                        boolean nameMatch = (client.getNom() != null && client.getNom().contains(criteria.getNom()))
                                || (client.getPrenom() != null && client.getPrenom().contains(criteria.getNom()))
                                || (client.getRaisonSociale() != null && client.getRaisonSociale().contains(criteria.getNom()));
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

        return new PageImpl<>(filteredClients, PageRequest.of(page, pageSize), filteredClients.size());
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
}