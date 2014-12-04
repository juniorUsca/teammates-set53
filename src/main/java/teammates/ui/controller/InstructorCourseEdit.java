package teammates.ui.controller;

import java.util.ArrayList;
import java.util.List;

import teammates.common.datatransfer.CourseAttributes;
import teammates.common.datatransfer.EvaluationAttributes;
import teammates.common.datatransfer.FeedbackSessionAttributes;
import teammates.common.datatransfer.InstructorAttributes;
import teammates.common.exception.EntityDoesNotExistException;
import teammates.common.util.Assumption;
import teammates.common.util.Const;
import teammates.logic.api.GateKeeper;

public class InstructorCourseEdit extends InstructorCoursesPageAction {
    
    public ActionResult execute()
            throws EntityDoesNotExistException {
    
        String courseId = getRequestParamValue(Const.ParamsNames.COURSE_ID);
        Assumption.assertNotNull(courseId);
        String instructorEmail = getRequestParamValue(Const.ParamsNames.INSTRUCTOR_EMAIL);
        String index = getRequestParamValue(Const.ParamsNames.COURSE_EDIT_MAIN_INDEX);
        
        InstructorAttributes instructor = logic.getInstructorForGoogleId(courseId, account.googleId);
        CourseAttributes courseToEdit = logic.getCourse(courseId);
        
        new GateKeeper().verifyAccessible(instructor, courseToEdit);
        
        InstructorCourseEditPageData data = new InstructorCourseEditPageData(account);
        data.course = courseToEdit;
        if(instructorEmail == null) {
            data.instructorList = logic.getInstructorsForCourse(courseId);
            data.isAccessControlDisplayed = false;   
        } else {
            data.instructorList = new ArrayList<InstructorAttributes>();
            data.instructorList.add(logic.getInstructorForEmail(courseId, instructorEmail));
            data.index = Integer.parseInt(index);        
            data.isAccessControlDisplayed = true;
        }
        
        data.currentInstructor = instructor;
        data.sectionNames = logic.getSectionNamesForCourse(courseId);
        data.evalNames = new ArrayList<String>();
        data.feedbackNames = new ArrayList<String>();
      //colocamos falsepara que no se muestre el boton de Edit
        //pero se mostrara el boton save change
        data.display_courseEdit=false;
        
        List<EvaluationAttributes> evaluations = logic.getEvaluationsForCourse(courseId);
        for (EvaluationAttributes eval : evaluations) {
            data.evalNames.add(eval.name);
        }
        List<FeedbackSessionAttributes> feedbacks = logic.getFeedbackSessionsForCourse(courseId);
        for (FeedbackSessionAttributes feedback : feedbacks) {
            data.feedbackNames.add(feedback.feedbackSessionName);
        }
        
        
        
        ShowPageResult response = createShowPageResult(Const.ViewURIs.INSTRUCTOR_COURSE_EDIT, data);
        return response;
    }
}