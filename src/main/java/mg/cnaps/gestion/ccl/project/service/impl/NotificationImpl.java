package mg.cnaps.gestion.ccl.project.service.impl;

import mg.cnaps.gestion.ccl.framework.jpa.core.service.implementation.GenericServiceImpl;
import mg.cnaps.gestion.ccl.project.entity.Notification;
import mg.cnaps.gestion.ccl.project.repository.NotificationRepo;
import mg.cnaps.gestion.ccl.project.service.NotificationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationImpl extends GenericServiceImpl<Notification, String , NotificationRepo> implements NotificationService {
    public NotificationImpl(NotificationRepo repo) {
        super(repo);
    }

    @Override
    public List<Notification> findAll() {
        try{
            List<Notification> notifications = this.findUnread();
            notifications.sort((n1, n2) -> {
                if (n1.isRead() != n2.isRead() ) {
                    return Boolean.compare(n1.isRead() , n2.isRead() );
                }

                return trierlevel(n1, n2);
            });
            return notifications;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }
    @Override
    public List<Notification> findUnread() {

        return repository.findAll()
                .stream()
                .filter(n -> !n.isRead()).sorted(this::trierlevel).collect(Collectors.toList());
    }

    private int trierlevel(Notification n1, Notification n2) {
        int level1 = n1.getTypeNotification().getNiveau();
        int level2 = n2.getTypeNotification().getNiveau();


        if (level1 != level2) {
            return Integer.compare(level2, level1);
        }

        if (n1.getMouvement() == null && n2.getMouvement() == null) {
            return 0;
        } else if (n1.getMouvement() == null) {
            return 1;
        } else if (n2.getMouvement() == null) {
            return -1;
        }

        LocalDateTime debut1 = n1.getMouvement().getPeriodeDebut().toLocalDateTime();
        LocalDateTime debut2 = n2.getMouvement().getPeriodeDebut().toLocalDateTime();

        return debut1.compareTo(debut2);
    }

    @Override
    public Notification markAsRead(String id) {
        Notification n = this.repository.findById(id).orElse(null);
        assert n != null;
        n.setRead(true);
        repository.save(n);
        return n;
    }
    @Override
    public List<Notification> markAllAsRead() {
        List<Notification> unread = this.findUnread();

        unread.forEach(n -> n.setRead(true));

        return repository.saveAll(unread);
    }




}
