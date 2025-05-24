package brainacadorg.ticketapp.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendTicketEmail(String to, byte[] ticketPdf, String fileName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("Ваш квиток на подію");
            helper.setText("Дякуємо за покупку! У вкладенні ваш електронний квиток.");

            helper.addAttachment(fileName, new ByteArrayResource(ticketPdf));

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Помилка надсилання квитка на Email", e);
        }
    }
}
