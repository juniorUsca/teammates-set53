package teammates.ui.controller;


import teammates.common.datatransfer.AccountAttributes;

/**
 * PageData: the data used in the StudentCoursesPage
 */
public class StudentCoursesPageData extends PageData {

    
    public String studentEmail;
    public String courseToShow;
    public String status_message;
    
    public StudentCoursesPageData(AccountAttributes account) {
        super(account);
        // TODO Auto-generated constructor stub
    }
    
    
}
