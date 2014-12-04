package teammates.ui.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import teammates.storage.datastore.Datastore;
import teammates.storage.entity.Instructor;
import teammates.storage.entity.Student;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class StudentAutoEnroll{
    
    public List<Instructor> list_instructor;
    public Integer contador_instr;
    
    public StudentAutoEnroll(){
        
    }
    
    /*DATOS NECESARIOS
     * 
     * (String email, String name, String googleId, String comments,
            String courseId, String teamName, String sectionName)
     * 
     *  1. -ID/Name : googleId
        2. -comments : OPCIONAL se puede dejar en blanco
        3. -courseID : ----------------
        4. -email: ---------------------
        . -lastName:-----------------(en el mismo Student se saca SETNAME() )
        5. -name: --------------------(viene junto al apellido-lastname)
        6. -SectionName:
        7. -teamName
    */
    public void AutoJoin(String email, String name, String googleId, String comments,
            String courseId, String teamName, String sectionName){
        
        PersistenceManager pm=Datastore.getPersistenceManager();
        Student newstudent= new Student(email,name,googleId,comments,courseId,teamName,sectionName);
        try{
            pm.makePersistent(newstudent);
           
        }catch(Exception e){
            
        }finally{
            pm.close();
        }
        
    }
    
    //Verificaremos si un estudiante ya esta inscrito en un curso
    public boolean IsJoinCourse(String cursoId,String googleId){
        Integer cantidad=0;
        List<Student> listStudent;
        PersistenceManager pm = Datastore.getPersistenceManager();
        Query q = pm.newQuery(Student.class);
        q.setFilter("courseID==param1 && ID==param2");
        q.declareParameters("String param1, String param2");
        try{
            listStudent = (List<Student>) q.execute(cursoId,googleId);
            cantidad=listStudent.size();
            
        }catch(Exception e){
            
        }finally{
             q.closeAll();
        }
        pm.close();
        if(cantidad==0){
            return false;
        }else{
            return true;
        }
    }
    
    
    //Sacaremos los datos de los instructores que posean el mismo id de un curso
    public void Instructores_IDcurso(String IDCourse){
        PersistenceManager pm = Datastore.getPersistenceManager();
        Query q = pm.newQuery(Instructor.class);
        q.setFilter("courseId==param1");
        q.declareParameters("String param1");
        try{
            list_instructor = (List<Instructor>) q.execute(IDCourse);
            contador_instr=list_instructor.size();
            
        }catch(Exception e){
            
        }finally{
             q.closeAll();
        }
        pm.close();
    }
}