package mg.cnaps.gestion.ccl.project.service;

import mg.cnaps.gestion.ccl.project.config.CclPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javamailSender;

    private final CclPropertyService cclPropertyService;

    public EmailService(CclPropertyService cclPropertyService) {
        this.cclPropertyService = cclPropertyService;
    }

    public void sendEmailToMultipleRecipients(String[] recipients, String subject, String text) throws MessagingException {
        MimeMessage message = javamailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(cclPropertyService.getMyMail());
        helper.setSubject(subject);
        helper.setText(text, true);
        helper.setBcc(recipients);

        javamailSender.send(message);
        System.out.println(" Email envoyé à " + recipients.length + " destinataires.");
    }

    public void sendEmailWithPdfBytes(String[] recipients, String subject, String text, byte[] pdfBytes, String fileName) throws MessagingException {
        MimeMessage message = javamailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(cclPropertyService.getMyMail());
        helper.setSubject(subject);
        helper.setText(text, true);
        helper.setBcc(recipients);

        if (pdfBytes != null && pdfBytes.length > 0) {
            helper.addAttachment(fileName, new ByteArrayResource(pdfBytes));
        }

        javamailSender.send(message);
    }

}
