package teammates.ui.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import teammates.common.util.Const;
import teammates.storage.datastore.Datastore;
import teammates.storage.entity.Account;
import teammates.storage.entity.Course;
import teammates.storage.entity.Instructor;
import teammates.storage.entity.Student;


public class AdminEstadisticasConsultas {
    
    public List<Course> list_cursos;
    public List<Instructor> list_instructores;
    public List<Student> list_estudiante;
    public List<Account> list_cuentas;
    
    public Integer cant_curso=0;
    public Integer cant_estud=0;
    public Integer cant_instruc=0;
    public Integer cant_cuentas=0;
    
    
    
    public AdminEstadisticasConsultas(){
        
    }
  //Las funciones con llamadas a la Base de datos no se pueden hacer TEst con JUnit
    public void sacar_Cursos(){
        PersistenceManager pm = Datastore.getPersistenceManager();
        Query q = pm.newQuery(Course.class);
        try{
            list_cursos = (List<Course>) q.execute();
            cant_curso=list_cursos.size();
        }catch(Exception e){
            
        }finally{
             q.closeAll();
        }
        pm.close();
    }
  //Las funciones con llamadas a la Base de datos no se pueden hacer TEst con JUnit
    public void sacar_Estudiantes(){
        PersistenceManager pm = Datastore.getPersistenceManager();
        Query q = pm.newQuery(Student.class);
        try{
            list_estudiante = (List<Student>) q.execute();
            cant_estud=list_estudiante.size();
        }catch(Exception e){
            
        }finally{
             q.closeAll();
        }
        pm.close();
    }
  //Las funciones con llamadas a la Base de datos no se pueden hacer TEst con JUnit
    public void sacar_Instructores(){
        PersistenceManager pm = Datastore.getPersistenceManager();
        Query q = pm.newQuery(Instructor.class);
        try{
            list_instructores = (List<Instructor>) q.execute();
            cant_instruc=list_instructores.size();
        }catch(Exception e){
            
        }finally{
             q.closeAll();
        }
        pm.close();
    }
    //Las funciones con llamadas a la Base de datos no se pueden hacer TEst con JUnit
    public void sacar_cuentas_soloInstructor(String instituto){
        PersistenceManager pm = Datastore.getPersistenceManager();
        Query q = pm.newQuery(Account.class);
        q.setFilter("isInstructor == true && institute== parametro");
        q.declareParameters("String parametro");
        try{
            list_cuentas = (List<Account>) q.execute(instituto);
            cant_cuentas=list_cuentas.size();
        }catch(Exception e){
            
        }finally{
             q.closeAll();
        }
        pm.close();
    }
    //Comprobamos si un string es un numero
    public boolean isNumeric(String cad){
        try{
            Integer.parseInt(cad);
            return true;
        }catch(NumberFormatException nfe){
            return false;
        }
    }
    //verificamos si el anio es un anio valido
    public String verificar_anio(String anio){
        
        if(anio.equals("")){
            return Const.StatusMessages.ESTADISTICAS_ANIO_VACIO;
        }
        if(isNumeric(anio)){
            Integer aux=Integer.parseInt(anio);
            if(aux<0){
                return Const.StatusMessages.ESTADISTICAS_ANIO_INCORRECTO;
            }
            if(aux>=0 && aux<2000){
                return Const.StatusMessages.ESTADISTICAS_ANIO_NORESULT;
            }
            Date ahora = new Date();
            SimpleDateFormat formateador = new SimpleDateFormat("yyyy");
            if(aux>Integer.parseInt(formateador.format(ahora))){
                return Const.StatusMessages.ESTADISTICAS_ANIO_FUTURO;
            }else{
                return Const.StatusMessages.ESTADISTICAS_ANIO_OK;
            }
        }else{
            return Const.StatusMessages.ESTADISTICAS_ANIO_INCORRECTO;
        }
        
    }
    
    //para Cursos por mes noes necesario sacar sus categorias por que siempre seran los mese del año
    //Sacaremos los datos de curso por mes
    public List<Integer> datosCursoXmes(String anio_,List<Course> cursosList){
        List<Integer> datos =  new ArrayList<Integer>();
        
      //llenamos nuestra lista de datos con purso ceros
        //El tamaño es 12 porque solo son doce meses
         for(Integer i=0;i<12;i++){ 
             datos.add(0);
         }
        
        SimpleDateFormat anio = new SimpleDateFormat("yyyy");
        SimpleDateFormat mes = new SimpleDateFormat("MM");
        for(int i=0;i<cursosList.size();i++){ 
            Date fecha = cursosList.get(i).getCreatedAt();
            if(anio_.equals(anio.format(fecha))){
                datos.set(Integer.parseInt(mes.format(fecha))-1, datos.get(Integer.parseInt(mes.format(fecha))-1)+1);
                
            }
        }
        return datos;
    }
    
  //Sacaremos las categorias de estudiantesXcurso
    //Sacaremos los datos de estudiantesXcurso
    public List<Integer> datosEstudiantesXcurso(List<Course> cursosList,List<Student> estudList){
        List<Integer> datos =  new ArrayList<Integer>();
      //llenamos nuestra lista de datos con purso ceros
         for(Integer i=0;i<cursosList.size();i++){
             datos.add(0);
         }
       //Actualizamos los datos
         for(Integer i=0;i<cursosList.size();i++){
             for(Integer j=0;j<estudList.size();j++){
                 if(estudList.get(j).getCourseId().equals(cursosList.get(i).getUniqueId())){
                     datos.set(i, datos.get(i)+1);
                }
            }
         }
        return datos;
    }
    
    //Sacaremos las categorias de estudiantesXinstructor
    //Sacaremos los datos de estudiantesXinstructor
    public List<Integer> datosEstudiantesXinstructor(List<Account> cuentasList,List<Instructor> instructorList,List<Student> studentList){
        List<Integer> datos =  new ArrayList<Integer>();
      //llenamos nuestra lista de datos con purso ceros
         for(Integer i=0;i<cuentasList.size();i++){
             datos.add(0);
         }
       //Actualizamos los datos
         for(Integer i=0;i<cuentasList.size();i++){
             for(Integer j=0;j<instructorList.size();j++){
                   if(cuentasList.get(i).getName().equals(instructorList.get(j).getName())){
                    
                    for(Integer k=0;k<studentList.size();k++){
                       if(instructorList.get(j).getCourseId().equals(studentList.get(k).getCourseId())){ 
                       //list_datos[<%=i%>]=list_datos[<%=i%>]+1;
                           datos.set(i, datos.get(i)+1);
               }
                }
              }
            }
         }
        return datos;
    }
    
  //Sacaremos las categorias de cursosXinstructor
    //Sacaremos los datos de cursosXinstructor
    public List<Integer> datosCursosXinstructor(List<Account> cuentasList,List<Instructor> instructorList){
        List<Integer> datos =  new ArrayList<Integer>();
      //llenamos nuestra lista de datos con purso ceros
         for(Integer i=0;i<cuentasList.size();i++){
             datos.add(0);
         }
       //Actualizamos los datos
          for(Integer i=0;i<cuentasList.size();i++){
             for(Integer j=0;j<instructorList.size();j++){
                 if(cuentasList.get(i).getName().equals(instructorList.get(j).getName())){
                 //list_datos[<%=i%>]=list_datos[<%=i%>]+1; 
                     datos.set(i, datos.get(i)+1);
                 }
             }
         } 
         return datos;
     }
}