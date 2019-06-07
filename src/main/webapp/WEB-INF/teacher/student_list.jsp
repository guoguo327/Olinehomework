<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	pageContext.setAttribute("basePath", basePath);
%>
<html>
<head>
<title>学生列表</title>
<meta charset="UTF-8">
<base href="<%=basePath%>">
<link rel="SHORTCUT ICON" href="images/icon.ico">
<link rel="BOOKMARK" href="images/icon.ico">
<link rel="stylesheet" type="text/css" href="bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="css/head.css">
<link rel="stylesheet" type="text/css" href="css/list_main.css">
<link rel="stylesheet" type="text/css" href="css/modal.css">
</head>
<body>
	<!--头部-->
	<jsp:include page="share/head.jsp"></jsp:include>

	<!--中间主体部分-->
	<div class="main">
		<!--学生-->
		<div class="list" id="student_list">
		
			
				
					<table class="table table-hover">
				<thead>
					<tr>
						<th width="15%">学号</th>
						<th width="15%">学生姓名</th>
						<th width="15%">班级</th>
						
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${pageBean.records}" var="student">
						<tr>
							<td>${student.id}</td>
							<td>${student.name}</td>
							<td>${student.clazz.grade.grade}级${student.clazz.major.name}${student.clazz.cno}班</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>	
			</div>	

</body>
<script type="text/javascript" src="script/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="bootstrap/js/bootstrap.min.js"></script>
<script src="script/admin/student.js"></script>
<script src="script/time.js"></script>
<script src="script/tips.js"></script>
</html>