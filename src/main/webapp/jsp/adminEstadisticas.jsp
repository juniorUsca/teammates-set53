<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="teammates.common.util.Const"%>
<%@ page import="teammates.ui.controller.AdminHomePageData"%>
<%@ page
	import="static teammates.ui.controller.PageData.sanitizeForHtml"%>
<%@ page import="teammates.ui.controller.AdminEstadisticasPageData"%>
<%@ page import="teammates.ui.controller.AdminEstadisticasConsultas"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.text.DateFormat"%>

<%@ page import="java.util.List;"%>

<%
AdminEstadisticasPageData data = (AdminEstadisticasPageData)request.getAttribute("data");
AdminEstadisticasConsultas query=new AdminEstadisticasConsultas();

query.sacar_Cursos();
query.sacar_Estudiantes();
query.sacar_Instructores();
query.sacar_cuentas_soloInstructor(data.account.institute);

Date ahora = new Date();
SimpleDateFormat formateador = new SimpleDateFormat("yyyy");

%>

<!DOCTYPE html>
<html>
<head>
<link rel="shortcut icon" href="/favicon.png">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>TEAMMATES - Administrator</title>
<!-- Bootstrap core CSS -->
<link href="/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<!-- Bootstrap theme -->
<link href="/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet">
<link rel="stylesheet" href="/stylesheets/teammatesCommon.css"
	type="text/css" media="screen">
<script type="text/javascript" src="/js/googleAnalytics.js"></script>
<script type="text/javascript" src="/js/jquery-minified.js"></script>
<script type="text/javascript" src="/js/common.js"></script>
<script type="text/javascript" src="/js/administrator.js"></script>
<jsp:include page="../enableJS.jsp"></jsp:include>
<!-- Bootstrap core JavaScript ================================================== -->
<script src="/bootstrap/js/bootstrap.min.js"></script>
<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
<script type="text/javascript" src="/js/highcharts.js"></script>
<script type="text/javascript" src="/js/exporting.js"></script>
</head>

<body>
	<div id="frameTop">
		<jsp:include page="<%=Const.ViewURIs.ADMIN_HEADER%>" />
	</div>
	<div id="frameBody">
		<div id="frameBodyWrapper" class="container">
			<div id="topOfPage"></div>


			<!-- ------------------------------------- -->
            <table>
            <td>
			<form action="<%=Const.ActionURIs.ADMIN_ESTADISTICAS_ESTUDXCURSO%>"
				method="post" class="form form-horizontal">

				<input type="submit" class="btn btn-primary" id="estud_curso"
					name="estud_curso" value="Estudiantes por Curso">

			</form>
			</td>
			<!-- ------------------------------------- -->
			<td>
			<form action="<%=Const.ActionURIs.ADMIN_ESTADISTICAS_ESTUDXINSTRUC%>"
				method="post" class="form form-horizontal">

				<input type="submit" class="btn btn-primary" id="estud_instruct"
					name="estud_instruct" value="Estudiantes por instructor">

			</form>
            </td>
			<!-- ------------------------------------------------------------------------------ -->
			<td>
			<form
				action="<%=Const.ActionURIs.ADMIN_ESTADISTICAS_CURSOSXINSTRUC%>"
				method="post" class="form form-horizontal">

				<input type="submit" class="btn btn-primary" id="curso_instruct"
					name="curso_instruct" value="Cursos por instructor">

			</form>
            </td>


			<!-- ---------------------------------------------------------------------------- -->
			<td>
			<form action="<%=Const.ActionURIs.ADMIN_ESTADISTICAS_CURSOXANIO%>"
				method="post" class="form form-horizontal">

				<input type="submit" class="btn btn-primary" id="cursos"
					name="cursos" value="Cursos por Mes"> <input type="text"
					name="anioactual" id="anioactual" <% if(data.mostrar_cursoXmes){ %>
					style="display: inline;" <%}else{ %> style="display: none;" <%}%>
					value='<%=data.anioDefault %>'>

			</form>
			</td>
            </table>


			<!-- Contenedor de las Graficas -->
			<div id="container"
				style="min-width: 310px; height: 400px; margin: 0 auto"></div>

			<!-- Contenedor de los mensajes -->
			<br>
			<jsp:include page="<%=Const.ViewURIs.STATUS_MESSAGE%>" />
			<br>
		</div>
	</div>

	<script type="text/javascript">
    //Utilizaremos HightChart para hacer las graficas
    function graficar_estadistica(list_categorias,list_datos,titulo,nombreY,leyenda) {
        $('#container').highcharts({
            chart: {
           type: 'column'//Tipo de grafico 
            },
            title: {
                text: titulo
            },
            xAxis: {
                categories: list_categorias
                    //Datos que deben ser de eje x ejemplo nombre de cursos
            },        yAxis: {
                min: 0,
                title: {
                    text: nombreY
                },//Funciones de estilo de grafico color tamaño
                stackLabels: {
                    enabled: true,
                    style: {
                        fontWeight: 'bold',
                        color: (Highcharts.theme && Highcharts.theme.textColor) || 'black'
                    }
                }
            },
            legend: {
                align: 'right',
                x: -70,
                verticalAlign: 'top',
                y: 20,
                floating: true,
                backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || 'white',
                borderColor: '#CCC',
                borderWidth: 1,
                shadow: false
            },
            tooltip: {
                formatter: function () {
                    return '<b>' + this.x + '</b><br/>' +
                        this.series.name + ': ' + this.y + '<br/>' +
                        'Total: ' + this.point.stackTotal;
                }
            },
            plotOptions: {
                column: {
                    stacking: 'normal',
                    dataLabels: {
                        enabled: true,
                        color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white',
                        style: {
                            textShadow: '0 0 3px black, 0 0 3px black'
                        }
                    }
                }
            },//Aqui se encuentra los valores que necesita para la graiica como tamaño de cada valor del eje x
        series: [ {
                name: leyenda,
                data: list_datos
            }]
        });
    };
    
    function graficar_cursosXmes(){
        var anio_input=document.getElementById("anioactual").value;
        var titulo="Numero de Cursos por Mes - "+anio_input;
        var nombreY="Numero de Cursos";
        var leyenda="Cursos";
        var list_categorias= ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
        var list_datos=[];
      
       //Actualizamos list datos
        <% if(data.mostrar_cursoXmes){
           List<Integer> Ldatos=query.datosCursoXmes(data.anioDefault, query.list_cursos);
           for(int i=0;i<Ldatos.size();i++){%>
           list_datos[<%=i %>]=<%=Ldatos.get(i) %>;
          <% }
        }
        %>     
        
      //GRAFICAMOS
        graficar_estadistica(list_categorias,list_datos,titulo,nombreY,leyenda);
    }
    
    function graficar_estudiantesXinstructor(){
        //document.getElementById("anioactual").style.display="none";
        var titulo="Numero de Estudiantes por Instructor";
        var nombreY="Numero de Estudiantes";
        var leyenda="Estudiantes";
        var list_categorias= [];
        var list_datos=[];
        
      //llenaremos nuestra lista de categorias
        <% for(Integer i=0;i<query.cant_cuentas;i++){ %>
             list_categorias.push('<%=query.list_cuentas.get(i).getName() %>');
        <%}%>
      //Actualizamos list datos
        <% if(data.mostrarGraf_estudXinstructor){
           List<Integer> Ldatos2=query.datosEstudiantesXinstructor(query.list_cuentas, query.list_instructores, query.list_estudiante);
           for(int i=0;i<Ldatos2.size();i++){%>
           list_datos[<%=i %>]=<%=Ldatos2.get(i) %>;
          <% }
        }
        %>  
               
        
      //GRAFICAMOS
        graficar_estadistica(list_categorias,list_datos,titulo,nombreY,leyenda);
    }
    
    //utilizo el nombre de la cuenta y delinstructor para compararalos
    //No uso google id porque no todos verificaron su cuenta
    function graficar_cursosXinstructor(){
        document.getElementById("anioactual").style.display="none";
        var titulo="Numero de Cursos por Instructor";
        var nombreY="Numero de Cursos";
        var leyenda="Cursos";
        var list_categorias= [];
        var list_datos=[];
       //llenaremos nuestra lista de categorias
        <% for(Integer i=0;i<query.cant_cuentas;i++){ %>
             list_categorias.push('<%=query.list_cuentas.get(i).getName() %>');
        <%}%>
      //Actualizamos list datos
        <% if(data.mostrarGraf_cursoXinstruc){
           List<Integer> Ldatos3=query.datosCursosXinstructor(query.list_cuentas, query.list_instructores);
           for(int i=0;i<Ldatos3.size();i++){%>
           list_datos[<%=i %>]=<%=Ldatos3.get(i) %>;
          <% }
        }
        %> 
         
        //GRAFICAMOS
        graficar_estadistica(list_categorias,list_datos,titulo,nombreY,leyenda);
    }
  
    
    function graficar_estudiantesXcurso(){
        document.getElementById("anioactual").style.display="none";
        var titulo="Numero de Estudiantes por Curso";
        var nombreY="Numero de Estudiantes";
        var leyenda="Estudiantes";
        var list_categorias= [];
        var list_datos=[];
        //llenaremos nuestra lista de categorias
        <% for(Integer i=0;i<query.cant_curso;i++){ %>
             list_categorias.push('<%=query.list_cursos.get(i).getName() %>');
        <%}%>
      //Actualizamos list datos
        <% if(data.mostrarGraf_estudXcurso){
           List<Integer> Ldatos4=query.datosEstudiantesXcurso(query.list_cursos, query.list_estudiante);
           for(int i=0;i<Ldatos4.size();i++){%>
           list_datos[<%=i %>]=<%=Ldatos4.get(i) %>;
          <% }
        }
        %>  
         
         //GRAFICAMOS
         graficar_estadistica(list_categorias,list_datos,titulo,nombreY,leyenda);
    }
    
    function prueba(){
        document.getElementById("anioactual").style.display="none";
        var titulo="Numero de Estudiantes por Curso";
        var nombreY="Numero de Estudiantes";
        var leyenda="Estudiantes";
        var list_categorias= ['a','b'];
        var list_datos=[6,3];
      //GRAFICAMOS
        graficar_estadistica(list_categorias,list_datos,titulo,nombreY,leyenda);
    }
    
  //Inicializaremos algunos eventos relacionados con botones y funciones USAMOS JQUERY
    $(document).ready(inicializarEventos);
    function inicializarEventos(){
        //document.getElementById("anioactual").style.display="none";
        
        
    	<%if(data.mostrarGraf_estudXcurso){ %>
    	graficar_estudiantesXcurso();
         <%}%>
              
        
        <%if(data.mostrarGraf_cursoXinstruc){ %>
        graficar_cursosXinstructor();
         <%}%>
        
        <%if(data.mostrarGraf_estudXinstructor){ %>
        graficar_estudiantesXinstructor();
         <%}%>
        
        <%if(data.anio_ok && data.mostrarGraf_cursoXmes){ %>
              graficar_cursosXmes();
        <%}%>
        
    }
    </script>

	<div id="frameBottom">
		<jsp:include page="<%=Const.ViewURIs.FOOTER%>" />
	</div>
</body>
</html>