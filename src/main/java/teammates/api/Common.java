package teammates.api;


import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Class that stores variables and methods that are widely used across classes
 *
 */

//TODO: create a subclass (e.g., InternalUtil) and move all internal utility 
//      functions to that sub class. It should be in util package.
public class Common {
	
	public final static String EOL = System.getProperty("line.separator");
	public static final int UNINITIALIZED_INT = -9999;
	public static final double UNINITIALIZED_DOUBLE = -9999.0;
	public final static String ENCODING = "UTF8";
	public final static String VERSION = "4.17.02";
	public final static String TEST_DATA_FOLDER = "src/test/resources/data";
	public final static String TEST_PAGES_FOLDER = "src/test/resources/pages";
	
	// Hover messages
	public final static String HOVER_MESSAGE_ENROLL = "Enroll student into the course";
	public final static String HOVER_MESSAGE_VIEW_COURSE = "View, edit and send registration keys to the students in the course";
	public final static String HOVER_MESSAGE_DELETE_COURSE = "Delete the course and its corresponding students and evaluations";
	public final static String HOVER_MESSAGE_ADD_EVALUATION = "Add an evaluation for the course";
	public final static String HOVER_MESSAGE_CLAIMED = "This is student own estimation of his/her contributions to the project";
	public final static String HOVER_MESSAGE_PERCEIVED = "This is the average of what other team members think this student contributed to the project";
	public final static String HOVER_MESSAGE_PERCEIVED_CLAIMED = "Difference between claimed and perceived contribution points";
	public final static String HOVER_MESSAGE_STUDENT_VIEW_COURSE = "View course details";
	
	public final static String HOVER_MESSAGE_EVALUATION_STATUS_AWAITING = "The evaluation is created but has not yet started";
	public final static String HOVER_MESSAGE_EVALUATION_STATUS_OPEN = "The evaluation has started and students can submit feedback until the closing time";
	public final static String HOVER_MESSAGE_EVALUATION_STATUS_CLOSED = "The evaluation has finished but the results have not been made available to the students";
	public final static String HOVER_MESSAGE_EVALUATION_STATUS_PUBLISHED = "The evaluation has finished and the results have been made available to students";
	
	public final static String HOVER_MESSAGE_EVALUATION_VIEW = "View the current results of the evaluation";
	public final static String HOVER_MESSAGE_EVALUATION_EDIT = "Edit evaluation details";
	public final static String HOVER_MESSAGE_EVALUATION_REMIND = "Send e-mails to remind students who have not submitted their evaluations to do so";
	public final static String HOVER_MESSAGE_EVALUATION_DELETE = "Delete the evaluation";
	public final static String HOVER_MESSAGE_EVALUATION_PUBLISH = "Publish evaluation results for students to view";
	public final static String HOVER_MESSAGE_EVALUATION_UNPUBLISH = "Make results not visible to students";
	
	// Evaluation status
	public final static String EVALUATION_STATUS_AWAITING = "AWAITING";
	public final static String EVALUATION_STATUS_OPEN = "OPEN";
	public final static String EVALUATION_STATUS_CLOSED = "CLOSED";
	public final static String EVALUATION_STATUS_PUBLISHED = "PUBLISHED";
	
	// IDs used as div tag "id" attribute
	public final static String COURSE_ID = "courseid";
	public final static String COURSE_NAME = "coursename";
	public final static String COURSE_NUMBEROFTEAMS = "coursenumberofteams";
	public final static String COURSE_TOTALSTUDENTS = "coursetotalstudents";
	public final static String COURSE_UNREGISTERED = "courseunregistered";
	public final static String COURSE_STATUS = "coursestatus";
	
	// JSP Parameter names
	public final static String PARAM_ACTION = "action";
	public final static String PARAM_COURSE_ID = "courseid";
	public final static String PARAM_COURSE_NAME = "coursename";
	public final static String PARAM_STUDENTS_ENROLLMENT_INFO = "enrollstudents";
	public final static String PARAM_EVALUATION_NAME = "evalname";
	public final static String PARAM_STATUS_MESSAGE = "message";
	public final static String PARAM_ERROR = "error";
	public final static String PARAM_NEXT_URL = "next";
	public static final String PARAM_USER_ID = "user";
	
	//JSP actions
	public static final String ACTION_ADD_COURSE = "addcourse";
	
	
	// JSP pages links (most are links to the servlet, since the JSP are made inaccessible directly)
	public final static String JSP_COORD_HOME = "/page/coordHome";
	public final static String JSP_COORD_COURSE = "/page/coordCourse";
	public final static String JSP_COORD_COURSE_DELETE = "/page/coordCourseDelete";
	public final static String JSP_COORD_COURSE_DETAILS = "/page/coordCourseDetails";
	public final static String JSP_COORD_COURSE_STUDENT_DETAILS = "/page/coordCourseStudentDetails";
	public final static String JSP_COORD_COURSE_STUDENT_EDIT = "/page/coordCourseStudentEdit";
	public final static String JSP_COORD_COURSE_ENROLL = "/page/coordCourseEnroll";
	public final static String JSP_COORD_TFS = "/page/coordTFS";
	public final static String JSP_COORD_TFS_MANAGE = "/page/coordTFSManage";
	public final static String JSP_COORD_TFS_CHANGE_TEAM = "/page/coordTFSChangeTeam";
	public final static String JSP_COORD_TFS_LOGS = "/page/coordTFSLogs";
	public final static String JSP_COORD_EVAL = "/page/coordEval";
	public final static String JSP_COORD_EVAL_DELETE = "/page/coordEvalDelete";
	public final static String JSP_COORD_EVAL_VIEW = "/page/coordEvalView";
	public final static String JSP_COORD_EVAL_EDIT = "/page/coordEvalEdit";
	public final static String JSP_COORD_EVAL_RESULTS = "/page/coordEvalResults";
	public final static String JSP_COORD_EVAL_SUBMISSION_EDIT = "/page/coordEvalSubmissionEdit";
	
	public final static String JSP_STUDENT_HOME = "/page/studentHome";
	public final static String JSP_STUDENT_COURSE = "/page/studentHome";
	public final static String JSP_STUDENT_COURSE_DETAILS = "/page/studentCourseDetails";
	public final static String JSP_STUDENT_TFS_MANAGE = "/page/studentTFSManage";
	public final static String JSP_STUDENT_EVAL = "/page/studentEval";
	public final static String JSP_STUDENT_EVAL_EDIT = "/page/studentEvalEdit";
	public final static String JSP_STUDENT_EVAL_RESULTS = "/page/studentEvalResults";
	
	public final static String JSP_LOGOUT = "/logout.jsp";
	public final static String JSP_UNAUTHORIZED = "/unauthorized.jsp";
	
	//status messages
	public final static String MESSAGE_COURSE_ADDED = "The course has been added. Click the 'Enroll' link in the table below to add students to the course.";
	public final static String MESSAGE_COURSE_EXISTS = "The course already exists.";
	public final static String MESSAGE_COURSE_MISSING_FIELD = "Course ID and Course Name are compulsory fields.";
	public final static String MESSAGE_COURSE_INVALID_ID = "Please use only alphabets, numbers, dots, hyphens, underscores and dollars in course ID.";
	public final static String MESSAGE_COURSE_DELETED = "The course has been deleted.";
	
	// DIV tags for HTML testing
	public final static String HEADER_TAG = "<div id=\"frameTop\">";
	public final static String FOOTER_TAG = "<div id=\"frameBottom\">";

	//data field sizes
	public static int COURSE_NAME_MAX_LENGTH = 38;
	public static int COURSE_ID_MAX_LENGTH = 21;
	public static final int STUDENT_NAME_MAX_LENGTH = 40;
	public static final int TEAM_NAME_MAX_LENGTH = 25;
	public static final int COMMENT_MAX_LENGTH = 500;
	
	//TeammatesServlet responses
	public static final String COORD_ADD_COURSE_RESPONSE_ADDED = "<status>course added</status>";
	public static final String COORD_ADD_COURSE_RESPONSE_EXISTS = "<status>course exists</status>";
	public static final String COORD_ADD_COURSE_RESPONSE_INVALID = "<status>course input invalid</status>";
	public static final String COORD_DELETE_COURSE_RESPONSE_DELETED = "<status>course deleted</status>";
	public static final String COORD_DELETE_COURSE_RESPONSE_NOT_DELETED = "<status>course not deleted</status>";
	
	//APIServlet responses
	public static final String BACKEND_STATUS_SUCCESS = "[BACKEND_STATUS_SUCCESS]";
	public static String BACKEND_STATUS_FAILURE = "[BACKEND_STATUS_FAILURE]";
	
	//General Error codes
	public static final String ERRORCODE_EMPTY_STRING = "ERRORCODE_EMPTY_STRING";
	public static final String ERRORCODE_NULL_PARAMETER = "ERRORCODE_NULL_PARAMETER";
	public static final String ERRORCODE_INCORRECTLY_FORMATTED_STRING = "ERRORCODE_INCORRECTLY_FORMATTED_STRING";
	public static final String ERRORCODE_INVALID_CHARS = "ERRORCODE_IVALID_CHARS";
	public static final String ERRORCODE_INVALID_EMAIL = "ERRORCODE_INVALID_EMAIL";
	public static final String ERRORCODE_LEADING_OR_TRAILING_SPACES = "ERRORCODE_LEADING_OR_TRAILING_SPACES";
	public static final String ERRORCODE_STRING_TOO_LONG = "ERRORCODE_STRING_TOO_LONG";

	
	
	@SuppressWarnings("unused")
	private void ____VALIDATE_parameters___________________________________() {
	}

	//TODO: add more checks and write unit tests
	public static void validateTeamName(String teamName) throws InvalidParametersException {
		if(teamName==null){
			return;
		}
		if(teamName.length()>TEAM_NAME_MAX_LENGTH){
			throw new InvalidParametersException(ERRORCODE_STRING_TOO_LONG, "Team name cannot be longer than "+TEAM_NAME_MAX_LENGTH);
		}
	}

	//TODO: add more checks and write unit tests
	public static void validateStudentName(String studentName) throws InvalidParametersException {
		if(!studentName.trim().equals(studentName)) {
			throw new InvalidParametersException(ERRORCODE_INCORRECTLY_FORMATTED_STRING, "Student name should not have leading or trailing spaces");
		}
		if(studentName.equals("")){
			throw new InvalidParametersException(ERRORCODE_EMPTY_STRING, "Student name should not be empty");
		}
		if(studentName.length()>STUDENT_NAME_MAX_LENGTH){
			throw new InvalidParametersException(ERRORCODE_STRING_TOO_LONG, "Student name cannot be longer than "+STUDENT_NAME_MAX_LENGTH);
		}
	}

	//TODO: add more checks and write unit tests
	public static void validateEmail(String email) throws InvalidParametersException {
		verifyNotNull(email, "email");
		verifyNoLeadingAndTrailingSpaces(email, "email");
		verifyNotAnEmptyString(email, "email");
		if(!email.contains("@")){
			throw new InvalidParametersException(ERRORCODE_INVALID_EMAIL, "Email address should contain '@'");
		}
		
	}
	


	//TODO: add more checks and write unit tests
	public static void validateGoogleId(String googleId) throws InvalidParametersException {
		verifyNotNull(googleId, "Google ID");
		verifyNoLeadingAndTrailingSpaces(googleId, "Google ID");
		verifyNotAnEmptyString(googleId, "Google ID");
		verifyContainsNoSpaces(googleId, "Google ID");
		
	}

	//TODO: add more checks and write unit tests
	public static void validateCoordName(String coordName) throws InvalidParametersException {
		verifyNoLeadingAndTrailingSpaces(coordName, "Coordinator name");
		verifyNotAnEmptyString(coordName, "Coordinator name");
	}
	
	//TODO: add more checks and write unit tests
	public static void validateCourseId(String courseId) throws InvalidParametersException {
		verifyContainsNoSpaces(courseId, "Course ID");
		
	}
	
	//TODO: add more checks and write unit tests
	public static void validateCourseName(String stringToCheck) throws InvalidParametersException {
		verifyNoLeadingAndTrailingSpaces(stringToCheck, "Course name");
		verifyNotAnEmptyString(stringToCheck, "Course name");
	}
		
	//TODO: add more checks and write unit tests
	public static void validateComment(String comment) throws InvalidParametersException {
		if (comment==null){return;}
		if(comment.length()>COMMENT_MAX_LENGTH){
			throw new InvalidParametersException(ERRORCODE_STRING_TOO_LONG, "Comment cannot be longer than "+STUDENT_NAME_MAX_LENGTH);
		}
	}
	
	private static void verifyNotNull(String stringToCheck, String nameOfString) throws InvalidParametersException {
		if(stringToCheck==null){
			throw new InvalidParametersException(ERRORCODE_NULL_PARAMETER, stringToCheck+" cannot be null");
		}
		
	}
	
	@SuppressWarnings("unused")
	private void ____ASSERT_more_things_____________________________________() {
	}
	
	/**
	 * Asserts that the superstringActual contains the exact occurence of substringExpected.
	 * Display the difference between the two on failure (in Eclipse).
	 * @param message
	 * @param substringExpected
	 * @param superstringActual
	 */
	public static void assertContains(String substringExpected, String superstringActual){
		if(!superstringActual.contains(substringExpected)){
			assertEquals(substringExpected, superstringActual);
		}
	}
	
	/**
	 * Asserts that the superstringActual contains the exact occurence of substringExpected.
	 * Display the difference between the two on failure (in Eclipse) with the specified message.
	 * @param message
	 * @param substringExpected
	 * @param superstringActual
	 */
	public static void assertContains(String message, String substringExpected, String superstringActual){
		if(!superstringActual.contains(substringExpected)){
			assertEquals(message, substringExpected, superstringActual);
		}
	}
	
	/**
	 * Asserts that the stringActual contains the occurence regexExpected.
	 * Replaces occurences of {*} at regexExpected to match anything in stringActual.
	 * Tries to display the difference between the two on failure (in Eclipse).
	 * @param message
	 * @param regexExpected
	 * @param stringActual
	 */
	public static void assertContainsRegex(String regexExpected, String stringActual){
		String processedRegex = Pattern.quote(regexExpected).replaceAll(Pattern.quote("{*}"), "\\\\E.*\\\\Q");
		if(!stringActual.matches("(?s)(?m).*"+processedRegex+".*")){
			assertEquals(regexExpected,stringActual);
		}
	}
	
	/**
	 * Asserts that the stringActual contains the occurence regexExpected.
	 * Replaces occurences of {*} at regexExpected to match anything in stringActual.
	 * Tries to display the difference between the two on failure (in Eclipse) with the specified message.
	 * @param message
	 * @param regexExpected
	 * @param stringActual
	 */
	public static void assertContainsRegex(String message, String regexExpected, String stringActual){
		String processedRegex = Pattern.quote(regexExpected).replaceAll(Pattern.quote("{*}"), "\\\\E.*\\\\Q");
		if(!stringActual.matches("(?s)(?m).*"+processedRegex+".*")){
			assertEquals(message,regexExpected,stringActual);
		}
	}
	
	@SuppressWarnings("unused")
	private void ____MISC_utility_methods___________________________________() {
	}
	
	/**
	 * This creates a Gson object that can handle the Date format we use in the Json file
	 * technique found in http://code.google.com/p/google-gson/source/browse/trunk/gson/src/test/java/com/google/gson/functional/DefaultTypeAdaptersTest.java?spec=svn327&r=327
	 */
	public static Gson getTeammatesGson(){
		return new GsonBuilder().setDateFormat(DateFormat.FULL).setDateFormat("yyyy-MM-dd h:mm a").setPrettyPrinting().create();
	}
	
	public static void println(String message) {
		System.out.println(String.format("[%d - %s] %s", Thread.currentThread()
				.getId(), Thread.currentThread().getName(), message));
	}

	public static Date convertToDate(String date, int time) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar calendar = Calendar.getInstance();

		Date newDate = new Date();

		// Perform date manipulation 
		try {
			newDate = sdf.parse(date);
			calendar.setTime(newDate);

			if (time == 24) {
				calendar.set(Calendar.HOUR, 23);
				calendar.set(Calendar.MINUTE, 59);
			} else {
				calendar.set(Calendar.HOUR, time);
			}

			return calendar.getTime();
		} catch (Exception e) {
			return null;
		}

	}
	
	public static Date convertToExactDateTime(String date, int time) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar calendar = Calendar.getInstance();

		Date newDate = new Date();

		// Perform date manipulation
		try {
			newDate = sdf.parse(date);
			calendar.setTime(newDate);

			if (time == 24) {
				calendar.set(Calendar.HOUR, 23);
				calendar.set(Calendar.MINUTE, 59);
			} else {
				calendar.set(Calendar.HOUR, time / 100);
				calendar.set(Calendar.MINUTE, time % 100);
			}

			return calendar.getTime();
		} catch (Exception e) {
			return null;
		}
	}

	public static Date getDateOffsetToCurrentTime(int offsetDays) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(cal.getTime());
		cal.add(Calendar.DATE, +offsetDays);
		return cal.getTime();
	}
	
	/**
	 * Read a file content and return a String
	 * @param filename
	 * @return
	 */
	public static String readFile(String filename) throws FileNotFoundException{
		String ans = new Scanner(new FileReader(filename)).useDelimiter("\\Z").next();
		return ans;
	}
	
	/**
	 * Reads from a stream and returns the string
	 * @param reader
	 * @return
	 */
	public static String readStream(InputStream stream){
		return new Scanner(stream).useDelimiter("\\Z").next();
	}

	public static boolean isWhiteSpace(String string) {
		return string.trim().isEmpty();
	}
	
	//TODO: write unit tests
	public static String generateStringOfLength(int length) {
		assert(length>=0);
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<length; i++){
			sb.append('a');
		}
		return sb.toString();
	}
	
	@SuppressWarnings("unused")
	private void ____PRIVATE_helper_methods_________________________________() {
	}
	
	private static void verifyContainsNoSpaces(String stringToCheck, String nameOfString)
			throws InvalidParametersException {
		if(stringToCheck.split(" ").length>1){
			throw new InvalidParametersException(ERRORCODE_INVALID_CHARS, nameOfString+" cannot contain spaces");
		}
	}

	private static void verifyNotAnEmptyString(String stringToCheck, String nameOfString)
			throws InvalidParametersException {
		if(stringToCheck.equals("")){
			throw new InvalidParametersException(ERRORCODE_EMPTY_STRING, nameOfString+" should not be empty");
		}
	}

	private static void verifyNoLeadingAndTrailingSpaces(String stringToCheck, String nameOfString)
			throws InvalidParametersException {
		if(!stringToCheck.trim().equals(stringToCheck)) {
			throw new InvalidParametersException(ERRORCODE_LEADING_OR_TRAILING_SPACES, nameOfString+" should not have leading or trailing spaces");
		}
	}
	

}