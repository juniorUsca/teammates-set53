package teammates.ui.controller;


import teammates.common.exception.EntityDoesNotExistException;
import teammates.common.exception.UnauthorizedAccessException;
import teammates.common.util.Const;
import teammates.logic.api.GateKeeper;

/**
 * Action: showing the profile page for a student in a course
 */
public class StudentCoursesPageAction extends Action {

    private StudentCoursesPageData data;
    private String studentEmail;
    
    @Override
    protected ActionResult execute() throws EntityDoesNotExistException {
       
        //check accessibility without courseId
        verifyBasicAccessibility();
        
        
        
        studentEmail = account.email;
        
        
        data = new StudentCoursesPageData(account);
        data.studentEmail= studentEmail;
        data.status_message = Const.ParamsNames.NO_MOSTRAR_NADA_AUTOJOIN;
        
        

        return createShowPageResult(Const.ViewURIs.STUDENT_COURSES, data);
    }
    
    private void verifyBasicAccessibility() {
        new GateKeeper().verifyLoggedInUserPrivileges();
        if(regkey != null) { 
            // unregistered users cannot view the page
            throw new UnauthorizedAccessException("User is not registered");
        }
    }
    
   
}
