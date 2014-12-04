package teammates.ui.controller;


import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import teammates.common.exception.EntityDoesNotExistException;
import teammates.common.util.Const;
import teammates.storage.datastore.Datastore;
import teammates.storage.entity.Course;
import teammates.storage.entity.Instructor;
import teammates.storage.entity.Student;

public class AdminEstadisticasPageAction extends Action {
    
    private AdminEstadisticasPageData data;

    @Override
    protected ActionResult execute() throws EntityDoesNotExistException {
        
        data= new AdminEstadisticasPageData(account);
        data.mostrar_cursoXmes=false;  
        data.anioDefault="2014";
        data.anio_ok=true;
        data.mostrarGraf_cursoXmes=false;
        data.mostrarGraf_estudXcurso=false;
        data.mostrarGraf_estudXinstructor=false;
        data.mostrarGraf_cursoXinstruc=false;
                
        return createShowPageResult(Const.ViewURIs.ADMIN_ESTADISTICAS,data);
        
        
    }

    
    
}