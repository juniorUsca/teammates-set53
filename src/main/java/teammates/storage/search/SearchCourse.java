package teammates.storage.search;

import teammates.storage.datastore.Datastore;
import teammates.storage.entity.Course;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class SearchCourse{
    
    public List<Course> cursos_encontrados;
    public Integer cantidadCursos=0;
    
    //Constructor
    public SearchCourse(){
        //
    }
    
    //Solo busscamos loscrsosque tenga el mismo name a name_curso
    //Llenamos los atributos anteriores y verificamos si el curso se encontro
    public boolean BuscarCurso(String name_curso){
        PersistenceManager pm = Datastore.getPersistenceManager();
        Query q = pm.newQuery(Course.class);
        q.setFilter("name == parametro");
        q.declareParameters("String parametro");
        try{
            cursos_encontrados = (List<Course>) q.execute(name_curso);
            cantidadCursos=cursos_encontrados.size();
            
        }catch(Exception e){
            
        }finally{
             q.closeAll();
        }
        pm.close();
        if(cantidadCursos>0){
            return true;
        }else{
            return false;
        }
    }
    
  //Verificamos si el id_curso es el mismo de la entidad Course
    public boolean Verificar_IDCourse(Course curso,String id_curso){
        return id_curso.equals(curso.getUniqueId());
    }
    
    //Verificaremos si el nombre e IDcourse se encuentran en la base de datos
    //Sirve para el AutoJoin
    public boolean verificarName_IDcourse(String name_curso,String ID_course){
        PersistenceManager pm = Datastore.getPersistenceManager();
        Integer tmp = null;
        Query q = pm.newQuery(Course.class);
        q.setFilter("name == parametro && ID== parametro2");
        q.declareParameters("String parametro, String parametro2");
        try{
            cursos_encontrados = (List<Course>) q.execute(name_curso,ID_course);
            tmp=cursos_encontrados.size();
            
        }catch(Exception e){
            
        }finally{
             q.closeAll();
        }
        pm.close();
        if(tmp>0){
            return true;
        }else{
            return false;
        }
    }
    
    //sacaremos la informacion del curso, que sera buscado solo por el id
    public boolean BuscarCurso_porID(String id_curso){
        PersistenceManager pm = Datastore.getPersistenceManager();
        Query q = pm.newQuery(Course.class);
        q.setFilter("ID == parametro");
        q.declareParameters("String parametro");
        try{
            cursos_encontrados = (List<Course>) q.execute(id_curso);
            cantidadCursos=cursos_encontrados.size();
            
        }catch(Exception e){
            
        }finally{
             q.closeAll();
        }
        pm.close();
        if(cantidadCursos>0){
            return true;
        }else{
            return false;
        }
    }
}