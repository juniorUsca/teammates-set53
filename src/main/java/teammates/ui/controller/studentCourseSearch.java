package teammates.ui.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import teammates.common.exception.EntityDoesNotExistException;
import teammates.common.util.Assumption;
import teammates.common.util.Const;
import teammates.logic.api.GateKeeper;
import teammates.logic.automated.SimpleEmailAutoJoin;
import teammates.storage.search.SearchCourse;

public class studentCourseSearch extends Action{
    private StudentCoursesPageData data;
    
    @Override
    public ActionResult execute() throws EntityDoesNotExistException {
        String newCourseId = getRequestParamValue(Const.ParamsNames.COURSE_ID);
        Assumption.assertNotNull(newCourseId);
        
        /* Check if user has the right to execute the action */
        new GateKeeper().verifyStudentPrivileges(account);
        
        String studentEmail = account.email;
        String comments="autojoin";
        String teamName="ninguno";
        String sectionName="";
        
        data = new StudentCoursesPageData(account);
        data.studentEmail = studentEmail;
        StudentAutoEnroll autojoin=new StudentAutoEnroll();
        
        SearchCourse buscarCurso =new SearchCourse();
        boolean existe=buscarCurso.BuscarCurso_porID(newCourseId);
        boolean esta_unido=autojoin.IsJoinCourse(newCourseId, account.googleId);
        
        String resultado=verificar(newCourseId,existe,esta_unido);
        if(resultado.equals(Const.ParamsNames.UNIDO_EXITOSAMENTE_AUTOJOIN)){
            autojoin.AutoJoin(studentEmail, account.name, account.googleId, comments, newCourseId, teamName, sectionName);
                    data.status_message = Const.ParamsNames.UNIDO_EXITOSAMENTE_AUTOJOIN;
                    data.courseToShow=buscarCurso.cursos_encontrados.get(0).getName();
                    
                    //Buscamos los instructores
                    autojoin.Instructores_IDcurso(newCourseId);
                    String curso=buscarCurso.cursos_encontrados.get(0).getName();
                    
                  //Enviamos Emails a los instructores del curso
                    for(Integer i=0;i<autojoin.contador_instr;i++)
                    {
                        String mailInstr= autojoin.list_instructor.get(i).getEmail();
                        String nameInstr=autojoin.list_instructor.get(i).getGoogleId();
                        SimpleEmailAutoJoin mail=new SimpleEmailAutoJoin(account.name,studentEmail,curso,mailInstr,nameInstr );
                        try {
                            mail.sendEmail();
                        } catch (UnsupportedEncodingException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
        }
        
            data.status_message = resultado;
            
            
        
        
        return createShowPageResult(Const.ViewURIs.STUDENT_COURSES, data);
    }
    
    public String verificar( String newCourseId,boolean existe, boolean esta_unido){
        
        //verificaremos si los campos estan vacios
        if(newCourseId.isEmpty()){
            return Const.ParamsNames.CAMPOS_VACIOS_AUTOJOIN;
        }
        else{//Comprobaremos si el curso esta en la base de datos
            if(existe){
            //Verificamos si esta unido al curso
                if(esta_unido){
                    return Const.ParamsNames.YA_ESTA_UNIDO_AUTOJOIN;
                }
                else{//Nos unimos alcurso
                    return Const.ParamsNames.UNIDO_EXITOSAMENTE_AUTOJOIN;
                    }
                
            }
            else{//el curso no esta en la base de datos
                return Const.ParamsNames.ID_NOMBRE_INCORRECTOS_AUTOJOIN;
            }
        }
    }
}