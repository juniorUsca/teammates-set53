package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import teammates.common.util.Const;
import teammates.storage.entity.Account;
import teammates.storage.entity.Course;
import teammates.storage.entity.Instructor;
import teammates.storage.entity.Student;
import teammates.ui.controller.AdminEstadisticasConsultas;

public class AdminEstadisticasTestJunit {

    public AdminEstadisticasConsultas query= new AdminEstadisticasConsultas();
    /*      PRUEBAS UNITARIAS PARA EL CASO DE PRUEBA N°1        */
    @Test
    public void CA001_N1() {
        String result=query.verificar_anio("");
        assertEquals(result,Const.StatusMessages.ESTADISTICAS_ANIO_VACIO);
    }
    @Test
    public void CA001_N2() {
        String result=query.verificar_anio("absd#$");
        assertEquals(result,Const.StatusMessages.ESTADISTICAS_ANIO_INCORRECTO);
    }
    @Test
    public void CA001_N3() {
        String result=query.verificar_anio("-2014");
        assertEquals(result,Const.StatusMessages.ESTADISTICAS_ANIO_INCORRECTO);
    }
    @Test
    public void CA001_N4() {
        String result=query.verificar_anio("1999");
        assertEquals(result,Const.StatusMessages.ESTADISTICAS_ANIO_NORESULT);
    }
    @Test
    public void CA001_N5() {
        String result=query.verificar_anio("2014");
        assertEquals(result,Const.StatusMessages.ESTADISTICAS_ANIO_OK);
    }
    @Test
    public void CA001_N6() {
        String result=query.verificar_anio("2015");
        assertEquals(result,Const.StatusMessages.ESTADISTICAS_ANIO_FUTURO);
    }
    @Test
    public void CA001_N7() {
        List<Course> cursosList=new ArrayList<Course>();
        
        List<Integer> result=query.datosCursoXmes("2014", cursosList);
        assertNotNull(result);
    }
    
    /*      PRUEBAS UNITARIAS PARA EL CASO DE PRUEBA N°2        */
    @Test
    public void CA002_N1() {
        List<Course> cursosList=new ArrayList<Course>();
        List<Student> estudList=new ArrayList<Student>();;
        
        List<Integer> result=query.datosEstudiantesXcurso(cursosList, estudList);
        assertNotNull(result);
    }
    /*      PRUEBAS UNITARIAS PARA EL CASO DE PRUEBA N°3        */
    @Test
    public void CA003_N1() {
        List<Account> cuentasList=new ArrayList<Account>();
        List<Instructor> instructorList=new ArrayList<Instructor>();
        List<Student> studentList=new ArrayList<Student>();
        
        List<Integer> result=query.datosEstudiantesXinstructor(cuentasList, instructorList, studentList);
        assertNotNull(result);
    }
    /*      PRUEBAS UNITARIAS PARA EL CASO DE PRUEBA N°4        */
    @Test
    public void CA004_N1() {
        List<Account> cuentasList=new ArrayList<Account>();
        List<Instructor> instructorList=new ArrayList<Instructor>();
        
        List<Integer> result=query.datosCursosXinstructor(cuentasList, instructorList);
        assertNotNull(result);
    }


}
