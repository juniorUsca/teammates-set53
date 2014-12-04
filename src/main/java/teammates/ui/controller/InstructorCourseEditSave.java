package teammates.ui.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import teammates.storage.datastore.Datastore;
import teammates.storage.entity.Course;
import teammates.common.exception.EntityDoesNotExistException;
import teammates.common.util.Assumption;
import teammates.common.util.Const;

public class InstructorCourseEditSave extends Action {
   
    @Override
    protected ActionResult execute() throws EntityDoesNotExistException {
        
        String courseId = getRequestParamValue(Const.ParamsNames.COURSE_ID);
        String newNameCourse =getRequestParamValue(Const.ParamsNames.COURSE_NAME);
        
     
        String rpt=verificarNameCurse(newNameCourse);
        if(rpt.equals(Const.StatusMessages.COURSE_INSTRUCTOR_EDITED_SAVE)){
            cambiarNombre_Curso(courseId,newNameCourse);
        }
        statusToUser.add(rpt);
        
        /* Create redirection to 'Edit' page with corresponding course id */
        RedirectResult result = createRedirectResult(Const.ActionURIs.INSTRUCTOR_COURSE_EDIT_PAGE);
        result.addResponseParam(Const.ParamsNames.COURSE_ID, courseId);
        return result;
    }
    
  //Actualizaremos el nombredelcurso
    //Funcion no testeable con Junit, ya que hace llamado a la Base de Datos
    public void cambiarNombre_Curso(String courseID,String newNameCourse){
        PersistenceManager pm = Datastore.getPersistenceManager();
        try {
            Course curso=pm.getObjectById(Course.class, courseID);
            curso.setName(newNameCourse);
        }catch(Exception e){
            
        } finally {
            pm.close();
        }
    }
    //Funcion PRINCIPAL para hacer pruebas unitarias JUnit
    public String verificarNameCurse(String pal){
        if(pal.isEmpty()){
            return Const.StatusMessages.COURSE_NAME_EMPTY;
        }
        Pattern p = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(pal);
        if(m.find()){
            return Const.StatusMessages.COURSE_INSTRUCTOR_ESPECIALCHARACTER;
        }else{
            return Const.StatusMessages.COURSE_INSTRUCTOR_EDITED_SAVE;
        }
        
    }
}

