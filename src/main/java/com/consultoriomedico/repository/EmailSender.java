package com.consultoriomedico.repository;


import com.consultoriomedico.domain.Cita;
import com.consultoriomedico.domain.Doctor;
import com.consultoriomedico.domain.PropertiesConfig;
import com.consultoriomedico.domain.Usuario;
import lombok.Builder;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Builder
public class EmailSender implements IConfirmadorCitas {

    private static int port = 25;
    private static boolean debug = true;
    private static final String MAIL_CONFIRMATION_PATH = "src/main/resources/confirmationMail.html";
    private static final String MAIL_APPOINTMENT_CONFIRMATION_PATH = "src/main/resources/confirmationAppointmentMail.html";
    private static final Logger log = Logger.getLogger(EmailSender.class);
    private static final DateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private static final String FORMAT_CHARSET = "UTF-8";
    private static String senderEmail = "clinicakodigo@gmail.com";

    private void sendMail(String usuario, String subject, String content) {
        PropertiesConfig properties = new PropertiesConfig();
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");


        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(properties.getPropertyConfig("username"), properties.getPropertyConfig("password"));
            }
        };
        Session session = Session.getInstance(props, auth);

        try {

            MimeMessage message = new MimeMessage(session);

            message.addHeader("Content-type", "text/HTML; charset=UTF-8");
            message.addHeader("format", "flowed");
            message.addHeader("Content-Transfer-Encoding", "8bit");


            message.setFrom(new InternetAddress(senderEmail));
            message.setReplyTo(InternetAddress.parse(senderEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(usuario));
            message.setSubject(subject, FORMAT_CHARSET);

            message.setContent(content, "text/html; charset=utf-8");

            Transport.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enviarConfirmacionUsuario(Usuario usuario) throws IOException {
        File flHtml = new File(MAIL_CONFIRMATION_PATH);
        Document doc = Jsoup.parse(flHtml, FORMAT_CHARSET, "");
        Element p = doc.getElementById("idNombre");
        if (p != null) {
            p.append(usuario.getNombre());
        }
        p = doc.getElementById("idIdentificacion");
        if (p != null) {
            p.append(String.valueOf(usuario.getId()));
        }
        p = doc.getElementById("idDireccion");
        if (p != null) {
            p.append(usuario.getDireccion());
        }
        p = doc.getElementById("idTelefono");
        if (p != null) {
            p.append(usuario.getTelefono());
        }
        String content = doc.html();
        sendMail(usuario.getEmail(), "Confirmación de creación de usuario", content);
    }

    public void enviarConfirmacionCita(Usuario usuario, Cita cita, Doctor doctor) throws IOException {
        File flHtml = new File(MAIL_APPOINTMENT_CONFIRMATION_PATH);
        Document doc = Jsoup.parse(flHtml, FORMAT_CHARSET, "");
        Element p = doc.getElementById("idNombre");
        if (p != null) {
            p.append(usuario.getNombre());
        }
        p = doc.getElementById("idIdentificacion");
        if (p != null) {
            p.append(String.valueOf(usuario.getId()));
        }
        p = doc.getElementById("idFecha");
        if (p != null) {
            p.append(dt1.format(cita.getFecha()));
        }
        p = doc.getElementById("idEspecialidad");
        if (p != null) {
            p.append(doctor.getEspecialidad());
        }
        p = doc.getElementById("idNombreDoctor");
        if (p != null) {
            p.append(doctor.getNombre());
        }
        String content = doc.html();
        sendMail(usuario.getEmail(), "Confirmación de cita", content);
    }

    public boolean enviarConfirmacion(Usuario usuario, Cita cita) {
        //TODO
        return true;
    }
}
