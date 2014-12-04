package teammates.ui.controller;

import javax.jdo.PersistenceManager;

import teammates.common.exception.EntityDoesNotExistException;
import teammates.common.util.Const;
import teammates.storage.datastore.Datastore;
import teammates.storage.entity.Course;

public class AdminEstadisticasCursoXanio extends Action {
    
    @Override
    protected ActionResult execute() throws EntityDoesNotExistException {
        
        String anio=getRequestParamValue("anioactual");
        
        AdminEstadisticasPageData data =new AdminEstadisticasPageData(account);
        data.mostrar_cursoXmes=true;
        data.anioDefault=anio;
        
        AdminEstadisticasConsultas consultas=new AdminEstadisticasConsultas();
        String anio_ok=consultas.verificar_anio(anio);
        
        if(anio_ok.equals(Const.StatusMessages.ESTADISTICAS_ANIO_OK)){
            data.anio_ok=true;
            data.mostrarGraf_cursoXmes=true;
            
        }else{
            data.anio_ok=false;
            statusToUser.add(anio_ok);
        }
        
        ShowPageResult response = createShowPageResult(Const.ViewURIs.ADMIN_ESTADISTICAS, data);
        return response;
    }
    
}

