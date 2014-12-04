package teammates.ui.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import teammates.common.datatransfer.CourseAttributes;
import teammates.common.datatransfer.InstructorAttributes;
import teammates.common.datatransfer.StudentAttributes;
import teammates.common.exception.EntityDoesNotExistException;
import teammates.common.exception.InvalidParametersException;
import teammates.common.util.Assumption;
import teammates.common.util.Const;
import teammates.common.util.Utils;
import teammates.logic.api.GateKeeper;

/**
 * Action: delete all students for course
 */
public class InstructorCourseDeleteAllStudentsAction extends InstructorCoursesPageAction {
    protected static final Logger log = Utils.getLogger();
    
    @Override
    public ActionResult execute() throws EntityDoesNotExistException{
        
        String courseId = getRequestParamValue(Const.ParamsNames.COURSE_ID);
        Assumption.assertNotNull(courseId);
        
        List<StudentAttributes> students = logic.getStudentsForCourse(courseId);
        Assumption.assertNotNull(students);
        String studentEmail;
        
        InstructorAttributes instructor = logic.getInstructorForGoogleId(courseId, account.googleId);
        new GateKeeper().verifyAccessible(
                instructor, logic.getCourse(courseId), Const.ParamsNames.INSTRUCTOR_PERMISSION_MODIFY_STUDENT);
        
        if(!deleteAll(students)){
        for(int i=0;i<students.size();i++){
            studentEmail = students.get(i).email;
            logic.deleteStudent(courseId, studentEmail);
        }
        statusToUser.add(Const.StatusMessages.ALL_STUDENTS_DELETED);
        statusToAdmin = "All Students in Course <span class=\"bold\">[" + courseId + "]</span> deleted.";
        }

        RedirectResult result = createRedirectResult(Const.ActionURIs.INSTRUCTOR_COURSE_DETAILS_PAGE);
        result.addResponseParam(Const.ParamsNames.COURSE_ID, courseId);
        return result;
        
    }
    
    public boolean deleteAll(List<StudentAttributes> students_){
        if(students_.isEmpty()){
            return true;
        }else{
            return false;
        }
        
    }
}