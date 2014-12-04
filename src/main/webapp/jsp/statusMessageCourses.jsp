<%@ page import="teammates.ui.controller.PageData"%>
<%@ page import="teammates.common.util.Const"%>

<%@ page import="teammates.ui.controller.StudentCoursesPageData"%>
<% 
 StudentCoursesPageData data = (StudentCoursesPageData)request.getAttribute("data");
 
 
                                    if(data.status_message==Const.ParamsNames.NO_MOSTRAR_NADA_AUTOJOIN){%>
<div id="statusMessage" style="display: none;"></div>
<%  }
                                %>

<% 
                                    if(data.status_message==Const.ParamsNames.CAMPOS_VACIOS_AUTOJOIN){%>
<div id="statusMessage" class="alert alert-danger">
	Course ID cannot be empty!<br>
</div>

<%  }
                                %>
<% 
                                    if(data.status_message==Const.ParamsNames.YA_ESTA_UNIDO_AUTOJOIN){%>
<div id="statusMessage" class="alert alert-warning">
	You already joined this course!<br>
</div>
<%  }
                                %>
<% 
                                    if(data.status_message==Const.ParamsNames.ID_NOMBRE_INCORRECTOS_AUTOJOIN){%>
<div id="statusMessage" class="alert alert-danger">
	Course ID is incorrect!<br>
</div>
<%  }
                                %>
<% 
                                    if(data.status_message==Const.ParamsNames.UNIDO_EXITOSAMENTE_AUTOJOIN){%>
<div id="statusMessage" class="alert alert-warning">
	You have joined the course <span style="color: blue"> <%=data.courseToShow %>
	</span> successfully!<br>
</div>
<%  }
                                %>


<script type="text/javascript">
                                    document.getElementById( 'statusMessage' ).scrollIntoView();
                               </script>

<% //Este es para el boton Search
                                    if(data.status_message==Const.ParamsNames.CURSO_ENCONTRADO_AUTOJOIN){%>
Curso encontrado!
<br>
<%  }
                                %>



