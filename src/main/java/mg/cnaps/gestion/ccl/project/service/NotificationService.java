package mg.cnaps.gestion.ccl.project.service;

import mg.cnaps.gestion.ccl.framework.jpa.core.service.GenericService;
import mg.cnaps.gestion.ccl.project.entity.Notification;

import java.util.List;


public interface NotificationService extends GenericService<Notification, String> {
    List<Notification> findUnread();

    Notification markAsRead(String id);

    List<Notification> markAllAsRead();
}