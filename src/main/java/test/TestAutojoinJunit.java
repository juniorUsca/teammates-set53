package test;

import static org.junit.Assert.*;

import org.junit.Test;

import teammates.common.util.Const;
import teammates.ui.controller.studentCourseSearch;

public class TestAutojoinJunit
{
    public String id="";
    public boolean exist = true;
    public boolean unido = true;
    public studentCourseSearch query = new studentCourseSearch();
    
    
    @Test
    public void es_vacio_Test()
    {
        String resul = query.verificar(id, exist, unido);
        assertEquals(resul,Const.ParamsNames.CAMPOS_VACIOS_AUTOJOIN);
    }
    
    @Test
    public void id_existe_Test()
    {
        id="p_1";
        exist=false;
        String resul = query.verificar(id, exist, unido);
        assertEquals(resul,Const.ParamsNames.ID_NOMBRE_INCORRECTOS_AUTOJOIN);
    }
    
    @Test
    public void id_unido_Test()
    {
        id="p_1";
        exist=true;
        String result = query.verificar(id, exist, unido);
        assertEquals(result,Const.ParamsNames.YA_ESTA_UNIDO_AUTOJOIN);
    }
    
    @Test
    public void id_unir_Test()
    {
        id="p_2";
        exist=true;
        unido=false;
        String result = query.verificar(id, exist, unido);
        assertEquals(result,Const.ParamsNames.UNIDO_EXITOSAMENTE_AUTOJOIN);
    }
}
