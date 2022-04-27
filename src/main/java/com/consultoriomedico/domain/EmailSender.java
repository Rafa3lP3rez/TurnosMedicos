package com.consultoriomedico.domain;

import lombok.Builder;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Builder
public class EmailSender {
    private static String host = "smtp.mailtrap.io";
    private static int port = 25;
    private static boolean debug = true;

    private static String username = "clinicakodigo@gmail.com";
    private static String password = "@projectKodigo2022";

    private static String senderEmail = "clinicakodigo@gmail.com";

    public void sendMail( String to, String subject, String content ) {

        // Set Properties
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };
        Session session = Session.getInstance(props, auth);

        try {

            MimeMessage message = new MimeMessage( session );

            message.addHeader("Content-type", "text/HTML; charset=UTF-8");
            message.addHeader("format", "flowed");
            message.addHeader("Content-Transfer-Encoding", "8bit");


            message.setFrom( new InternetAddress( senderEmail ) );
            message.setReplyTo( InternetAddress.parse( senderEmail ) );
            message.addRecipient( Message.RecipientType.TO, new InternetAddress( to ) );
            message.setSubject( subject, "UTF-8" );
            message.setContent( content, "text/html; charset=utf-8" );

            Transport.send( message );

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
