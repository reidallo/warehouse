package app.warehouse.system.service.implementation;

import app.warehouse.system.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Override
    public void sendEmailDeclinedOrder(String firstName, String lastName, String orderNumber, String message, String email,
                                       String subject) {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "utf-8");

        Map<String, Object> model = new HashMap<>();
        model.put("firstName", firstName);
        model.put("lastName", lastName);
        model.put("orderNumber", orderNumber);
        model.put("message", message);

        Context context = new Context();
        context.setVariables(model);

        try {
            messageHelper.setTo(email);
            messageHelper.setSubject("Warehouse Team - " + subject);
            messageHelper.setText(templateEngine.process("template-declined-order", context), true);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        mailSender.send(mimeMessage);
    }

    @Override
    public void sendEmailUnderDelivery(String firstName, String lastName, String orderNumber, String email, String date,
                                       String subject) {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "utf-8");

        Map<String, Object> model = new HashMap<>();
        model.put("firstName", firstName);
        model.put("lastName", lastName);
        model.put("orderNumber", orderNumber);
        model.put("date", date);

        Context context = new Context();
        context.setVariables(model);

        try {
            messageHelper.setTo(email);
            messageHelper.setSubject("Warehouse Team - " + subject);
            messageHelper.setText(templateEngine.process("template-under-delivery", context), true);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        mailSender.send(mimeMessage);
    }
}
