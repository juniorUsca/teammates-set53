<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="teammates.common.util.Const"%>
<%@ page import="teammates.ui.controller.PageData"%>
<%@ page import="teammates.ui.controller.StudentCoursesPageData"%>
<%
StudentCoursesPageData data = (StudentCoursesPageData)request.getAttribute("data");

boolean isUnregistered = data.account.googleId == null 
|| (data.student != null && !data.student.isRegistered());
%>
<%@ page import="teammates.common.datatransfer.CourseAttributes"%>
<%@ page import="teammates.storage.search.SearchCourse"%>
<%@ page import="teammates.ui.controller.StudentAutoEnroll"%>
<%
CourseAttributes CA= new CourseAttributes(true);
    Integer cantidad = CA.cantidadCursos;
%>

<!DOCTYPE html>
<html>
<head>
<link rel="shortcut icon" href="/favicon.png">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>TEAMMATES - Student</title>
<link rel="stylesheet" href="/bootstrap/css/bootstrap.min.css"
	type="text/css">
<link rel="stylesheet" href="/bootstrap/css/bootstrap-theme.min.css"
	type="text/css">
<link rel="stylesheet" href="/stylesheets/teammatesCommon.css"
	type="text/css">

<script type="text/javascript" src="/js/googleAnalytics.js"></script>
<script type="text/javascript" src="/js/jquery-minified.js"></script>
<script type="text/javascript" src="/js/common.js"></script>
<script src="/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/js/additionalQuestionInfo.js"></script>
<script type="text/javascript" src="/js/student.js"></script>
<script type="text/javascript" src="/js/studentCourses.js"></script>
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
      <![endif]-->
<jsp:include page="../enableJS.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="<%=Const.ViewURIs.STUDENT_HEADER%>" />
	<div id="frameBodyWrapper" class="container theme-showcase">
		<div id="topOfPage"></div>
		<div id="headerOperation">
			<h1>Auto Join</h1>
		</div>

		<div class="panel panel-primary">
			<div class="panel-body fill-plain">
				<form method="get"
					action="<%=Const.ActionURIs.STUDENT_COURSE_SEARCH%>"
					name="form_searchcourse" class="form form-horizontal">


					<div class="form-group">
						<label class="col-sm-3 control-label">Course ID:</label>
						<div class="col-sm-3">
							<input class="form-control" type="text"
								name="<%=Const.ParamsNames.COURSE_ID%>"
								id="<%=Const.ParamsNames.COURSE_ID%>" />
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-3 col-sm-9">
							<input id="btnSearchCourse" type="submit" class="btn btn-primary"
								onclick="" value="AUTOJOIN">
						</div>
					</div>
				</form>
			</div>
		</div>

		<!-- Aqui se mostrara el resultado -->
		<br>
		<jsp:include page="<%=Const.ViewURIs.STATUS_MESSAGE_COURSES%>" />
		<br>


		<!--------------------------------------------------------------------------------------->




		<jsp:include page="<%=Const.ViewURIs.FOOTER%>" />
</body>
</html>