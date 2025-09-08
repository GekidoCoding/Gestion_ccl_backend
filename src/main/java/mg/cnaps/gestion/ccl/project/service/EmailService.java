package mg.cnaps.gestion.ccl.project.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailService {

    public static void sendEmail(JavaMailSender emailSender, String[] destinataire, String messageText) throws Exception {
        if (destinataire != null && destinataire.length != 0) {
            try {
                MimeMessage message = emailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setFrom(new InternetAddress(" no-reply@cnaps.mg ", "CNaPS no-reply"));
                helper.setTo(destinataire);
                helper.setSubject("Envoi de facture proforma");
                helper.setText(messageText);
                emailSender.send(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
