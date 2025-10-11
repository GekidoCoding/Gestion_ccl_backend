package mg.cnaps.gestion.ccl.project.controller;

import mg.cnaps.gestion.ccl.framework.jpa.core.controller.GenericController;
import mg.cnaps.gestion.ccl.project.entity.Notification;
import mg.cnaps.gestion.ccl.project.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
public class NotificationController extends GenericController<Notification , String ,NotificationService > {
    public NotificationController(NotificationService service ) {
        super(service);
    }

    @GetMapping("/unread")
    public ResponseEntity<?> getAllUnread() {
       return ResponseEntity.ok(service.findUnread());
    }
    @GetMapping("/unread/total")
    public ResponseEntity<?> getNumberAllUnread() {
        try{
            return ResponseEntity.ok(service.findUnread().size());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/mark_as_read/{id}")
    public ResponseEntity<?> markAsRead(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok(service.markAsRead(id));
    }
    @PostMapping("/mark_all_as_read")
    public ResponseEntity<?> markAllAsRead() {
        return ResponseEntity.ok(service.markAllAsRead());
    }
}