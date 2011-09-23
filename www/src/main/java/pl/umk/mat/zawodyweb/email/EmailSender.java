package pl.umk.mat.zawodyweb.email;

import java.util.Date;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author faramir
 */
public class EmailSender {

    private static final Log log = LogFactory.getLog(EmailSender.class);
    private static final String smtpHost;
    private static final String smtpUser;
    private static final String smtpPassword;
    private static final int smtpPort;
    private static final String addressFrom;
    private static final String subjectPrefix;

    static {
        Properties properties = EmailConfiguration.getProperties();
        smtpHost = properties.getProperty("email.host");
        smtpPort = Integer.parseInt(properties.getProperty("email.port"));
        smtpUser = properties.getProperty("email.user");
        smtpPassword = properties.getProperty("email.password");
        addressFrom = properties.getProperty("email.from");
        subjectPrefix = properties.getProperty("email.subject");

        log.info("EMAIL host     = " + smtpHost);
        log.info("EMAIL port     = " + smtpPort);
        log.info("EMAIL user     = " + smtpUser);
        log.info("EMAIL password = " + StringUtils.repeat("*", smtpPassword == null ? 0 : smtpPassword.length()));
        log.info("EMAIL from     = " + addressFrom);
        log.info("EMAIL subject  = " + subjectPrefix);
    }

    public static void send(String address, String subject, String text) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props);

        try {
            Address[] addresses = InternetAddress.parse(address);

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(addressFrom));
            message.setRecipients(Message.RecipientType.TO, addresses);
            message.setSubject(subjectPrefix + subject);
            message.setSentDate(new Date());
            message.setText(text);

            Transport transport = session.getTransport("smtp");
            transport.connect(smtpHost, smtpPort, smtpUser, smtpPassword);

            transport.sendMessage(message, addresses);

            transport.close();
        } catch (MessagingException ex) {
            log.error(ex);
        }
    }
}
