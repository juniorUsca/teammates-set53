package teammates.logic.automated;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.*;


public class SimpleEmailAutoJoin {
    
    public String student;
    public String emailStudent;
    public String course;
    public String emailInstructor;
    public String nameInstructor;
    
    public SimpleEmailAutoJoin(String estudiante, String email, String curso, String emailInstr, String nameInstr){
        this.student=estudiante;
        this.emailStudent=email;
        this.course=curso;
        this.emailInstructor=emailInstr;
        this.nameInstructor=nameInstr;
    }
    
    //el correo del administrador sera: teammatesvalfaset53@gmail.com
    
    public SimpleEmailAutoJoin(String nameInstr){
        this.nameInstructor=nameInstr;
    }
    
    public void sendEmail() throws UnsupportedEncodingException 
    {
     
        String tema = "Auto Join ";
        String mensaje = "El alumno a podido unirse a este curso mediante la opcion de AutoJoin. Esta opcion solo requiere la id del curso.";
        String titulo_mensaje= "Un Alumno se unio al curso  "+ course;
        
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        String msgBody = "Un Alumno se unio al curso  "+ course+ "\n";
        msgBody += "El alumno "+ student +" se unio al curso " + course+ "\n";
        msgBody += "correo: "+ emailStudent +"\n";
        msgBody += tema +"\n";
        msgBody += mensaje;        
        try {
            //se crea un objeto para javamail
            Message msg = new MimeMessage(session);
            //se coloca datos del remitente (correo, nombre)
            msg.setFrom(new InternetAddress("teammatesvalfaset53@gmail.com", "TEAMMATESV-ALFA"));
            //se coloca datos del destinatario
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(emailInstructor, nameInstructor));
            //Se coloca el titulo del mensaje 
            msg.setSubject(titulo_mensaje);
            msg.setText(msgBody);
            Transport.send(msg);

        } catch (AddressException e) {
            System.out.println(e);
          } catch (MessagingException e) {
            System.out.println(e);
         }
           
    }
    //correo del Admin original: admin@teammatesv-alfa.appspotmail.com
    //Pero tomo como teammatesvalfaset53@gmail.com nuestroadmin actual 
  //Enviaremos un correo al administrador, diciendo que el instructor acepto ser instructor
    public void sendEmail_Instructor() throws UnsupportedEncodingException 
    {
        String emaiAdmin="teammatesvalfaset53@gmail.com";
        String nameAdmin="Set";
        String tema = "Se unio un instructor mas! ";
        String mensaje = "Se unio un instructor mas a TEAMMATESV-ALFA! ";
        String titulo_mensaje= "Se unio un instructor mas a TEAMMATESV-ALFA!";
        
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        String msgBody = nameInstructor+".@gmail.com acepto la invitación para formar parte del grupo de instructores."+ "\n";
        msgBody += "Cada vez mas instructores se unen, eso es una buena noticia." +"\n";
        msgBody += tema +"\n";
        msgBody += mensaje;        
        try {
            //se crea un objeto para javamail
            Message msg = new MimeMessage(session);
            //se coloca datos del remitente (correo, nombre)
            msg.setFrom(new InternetAddress("admin@teammatesv-alfa.appspotmail.com", "TEAMMATESV-ALFA"));
            //se coloca datos del destinatario
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(emaiAdmin, nameAdmin));
            //Se coloca el titulo del mensaje 
            msg.setSubject(titulo_mensaje);
            msg.setText(msgBody);
            Transport.send(msg);

        } catch (AddressException e) {
            System.out.println(e);
          } catch (MessagingException e) {
            System.out.println(e);
         }
           
    } 
    
}