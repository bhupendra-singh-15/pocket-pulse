package app.pocketpulse.service;



import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {


    private final  JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    private String fromEmail;

    /**
     * Sends a simple text email
     *
     * @param to Recipient email address
     * @param subject Email subject
     * @param body Email body content
     */
    public void sendSimpleEmail(String to, String subject, String body) {
       try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);  // Must match your Brevo SMTP login
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            System.out.println("Email sent successfully to " + to);
        } catch (Exception ex){
           throw new IllegalStateException("Failed to send email");
       }
    }

    public void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            mailSender.send(message);

        } catch (Exception e) {
            throw new IllegalStateException("Failed to send email: " + e.getMessage());
        }
    }


}
