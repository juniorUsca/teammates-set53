package teammates.ui.controller;

import teammates.common.exception.EntityDoesNotExistException;
import teammates.common.util.Const;

public class StudentNotificacionPageAction extends Action {

    @Override
    protected ActionResult execute() throws EntityDoesNotExistException {
        ShowPageResult response = createShowPageResult(
                Const.ViewURIs.STUDENT_COURSE_NOTIFICACION,null);
        return response;
    }

}