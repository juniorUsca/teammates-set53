package teammates.ui.controller;

import teammates.common.exception.EntityDoesNotExistException;
import teammates.common.util.Const;

public class AdminEstadisticasEstudXinstructor extends Action {
    
    @Override
    protected ActionResult execute() throws EntityDoesNotExistException {
              
        AdminEstadisticasPageData data =new AdminEstadisticasPageData(account);
        data.anioDefault="2014";
        data.mostrarGraf_estudXinstructor=true;
       // AdminEstadisticasConsultas consultas=new AdminEstadisticasConsultas();
        
        
        ShowPageResult response = createShowPageResult(Const.ViewURIs.ADMIN_ESTADISTICAS, data);
        return response;
    }
    
}