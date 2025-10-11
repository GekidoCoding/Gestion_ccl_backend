package mg.cnaps.gestion.ccl.project.repository;

import mg.cnaps.gestion.ccl.framework.jpa.core.repository.GenericRepository;
import mg.cnaps.gestion.ccl.project.entity.Notification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepo extends GenericRepository<Notification, String> {
    List<Notification> findByIsReadFalse();
    @Query(nativeQuery = true,value = "SELECT n FROM Notification n JOIN n.typeNotification t ORDER BY n.isRead ASC, t.level DESC, n.dhCreation DESC")
    List<Notification> findAllOrdered();
}
