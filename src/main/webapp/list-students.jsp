<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>

<head>
	<title> Student Tracker App </title>
	<link type="text/css" rel="stylesheet" href="css/style.css">
</head>

<body>

	<div id="wrapper">
		<div id="header">
			<h2>GPIG Latifabad, Hyderabad</h2>
		</div> 
	</div>

	<div id="container">
	
		<div id="content">
			
			<input type="button" value="Add Student" 
				onclick="window.location.href='add-student-form.jsp'"
				class="add-student-button"
			/>
			
			<table class="table">
				<thead>
				<tr> 
					<th> First Name </th>
					<th> Last Name </th>
					<th> Email </th>
					<th> Action </th>
				</tr>
				</thead>
				<c:forEach var="tempStudent" items="${STUDENT_LIST }">	
					<!-- set up link for Each Student -->
					<c:url var="deleteLink" value="StudentControllerServlet"> 
						<c:param name="command" value="DELETE" />
						<c:param name="studentId" value="${tempStudent.id }" />
					</c:url>
					
					<!--  set up a link to delete a student -->
					<c:url var="tempLink" value="StudentControllerServlet"> 
						<c:param name="command" value="LOAD" />
						<c:param name="studentId" value="${tempStudent.id }" />
					</c:url>
					
					<tbody>
					<tr> 
						<td> ${tempStudent.firstName}  </td>
						<td> ${tempStudent.lastName}  </td>
						<td> ${tempStudent.email}  </td>
						<td> 
							<a href="${tempLink}">Update </a>  
							|
							<a href="${deleteLink}"
							onclick="if (!(confirm('Are you sure you want to delete?'))) return false">Delete </a> 
						</td>
					</tr>
					</tbody>
				</c:forEach>
			
			</table>
		
		</div>
	
	</div>
</body>


</html>