package teammates.ui.controller;


import java.util.List;

import teammates.common.datatransfer.AccountAttributes;
import teammates.storage.entity.Course;
import teammates.storage.entity.Instructor;
import teammates.storage.entity.Student;

public class AdminEstadisticasPageData extends PageData {

   
  public boolean mostrar_cursoXmes;
  public String anioDefault;
  public boolean mostrarGraf_cursoXmes;
  public boolean anio_ok;
  
  public boolean mostrarGraf_estudXcurso;
  
  public boolean mostrarGraf_estudXinstructor;
  
  public boolean mostrarGraf_cursoXinstruc;

    public AdminEstadisticasPageData(AccountAttributes account) {
        super(account);
        
    }

   

}