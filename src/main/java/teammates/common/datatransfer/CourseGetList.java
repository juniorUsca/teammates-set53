package teammates.common.datatransfer;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import teammates.storage.datastore.Datastore;
import teammates.storage.entity.Course;

//Ya no me sirve -Eliminar este archivo
public class CourseGetList{
   
    public String prueba;
    public List<Course> cursos;
    public String prueba2="nada";
    public Integer cantidad=0;
    
    public CourseGetList(String prueba){
        this.prueba=prueba;
        PersistenceManager pm = Datastore.getPersistenceManager();
        //PersistenceManager pm = PMF.get().getPersistenceManager();
        Query q = pm.newQuery(Course.class);
        try{
            cursos = (List<Course>) q.execute();
            cantidad=cursos.size();
            
        }catch(Exception e){
            
        }finally{
             q.closeAll();
        }
        pm.close();
    }
}