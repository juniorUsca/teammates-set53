package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import teammates.common.datatransfer.StudentAttributes;
import teammates.common.util.Const;
import teammates.ui.controller.AdminEstadisticasConsultas;
import teammates.ui.controller.InstructorCourseDeleteAllStudentsAction;
import teammates.ui.controller.InstructorCourseEditSave;

public class MultiplyTest {

    
    public InstructorCourseEditSave Edit= new InstructorCourseEditSave();
    @Test
    public void VerificationCourseEditSaveIsEmpty() 
    {
        String result=Edit.verificarNameCurse("");
        assertEquals(result,Const.StatusMessages.COURSE_NAME_EMPTY);
    }
    @Test
    public void VerificationCourseEditSaveEspecialCharacter() {
        String result=Edit.verificarNameCurse("#&%$/");
        assertEquals(result,Const.StatusMessages.COURSE_INSTRUCTOR_ESPECIALCHARACTER);
    }
    @Test
    public void VerificationCourseEditSave() 
    {
        String result=Edit.verificarNameCurse("Course01");
        assertEquals(result,Const.StatusMessages.COURSE_INSTRUCTOR_EDITED_SAVE);
    }
    
    
    
    /*@Test
    public void testMultiply() {
        junit test=new junit();
        int result = test.multiply(3, 4);
        assertEquals(12,result);
    }
    @Test
    public void testMultiply2() {
        junit test=new junit();
        int result = test.multiply(3, 4);
        assertEquals(13,result);
    }
    
    
    @Test
    public void test_sacarEstuudiantes() {
        junit test=new junit();
        boolean t=test.isNumeric("");
        List<Integer> aux=new ArrayList<Integer>();
        for(int i=0;i<10;i++){
            aux.add(i);
        }
        int a=9;
        aux.set(9, 10);
        
        InstructorCourseDeleteAllStudentsAction  tt=new InstructorCourseDeleteAllStudentsAction();
        List<StudentAttributes> stud=new ArrayList<StudentAttributes>();
        StudentAttributes aaa = new StudentAttributes("12","sd", "as","",
                "as", "fg", "vbvn");
        stud.add(aaa);
        boolean rpt=tt.deleteAll(stud);
        
        boolean num= test.isNumeric("fh12");
        //System.out.println (aux.get(9));
        
        Pattern p = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher("asd23#");
        boolean b = m.find();
        
        
        
        assertEquals(b,true);
    }*/

}
