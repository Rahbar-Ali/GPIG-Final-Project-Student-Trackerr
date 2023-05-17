package com.student.web.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private StudentDbUtil studentDbUtil;
	
	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;
	
	@Override
	public void init() throws ServletException {
		super.init();
		
		// Create our Student db Util and pass the connection pool 
		try {
			studentDbUtil = new StudentDbUtil(dataSource);
		}
		catch (Exception exc) {
			throw new ServletException(exc);
		}
	}



	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
		//Read the "command" parameter
		String theCommand = request.getParameter("command");
			
		
		// if "command" is missing
		if(theCommand == null) {
			theCommand = "LIST";
		}
		
		// Route to the method
		switch (theCommand) {
		
		case "LIST":
			ListStudents(request, response);
			break;
		case "ADD":
			addStudent(request, response);
			break;
		case "LOAD":
			loadStudent(request, response);
			break;
		case "UPDATE":
			updateStudent(request, response);
			break;
		case "DELETE":
			deleteStudent(request, response);
			break;
		default:
			ListStudents(request, response);
		

		}
		// List student in MVC	
		ListStudents(request, response);
		}
		catch (Exception exc) {
			throw new ServletException(exc);
		}
	}



	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) 
		throws Exception{
		
		// read student ID from form Data
		String theStudentId = request.getParameter("studentId");
		
		// delete student from database
		studentDbUtil.deleteStudent(theStudentId);
		
		//send back to list student
		ListStudents(request, response);
		
		
		
	}



	private void updateStudent(HttpServletRequest request, HttpServletResponse response) 
		throws Exception{
		
		// read student info
		int id = Integer.parseInt(request.getParameter("studentId"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		
		// create a new student obj
		Student theStudent = new Student(id, firstName, lastName, email);
		
		// perform update on database
		studentDbUtil.updateStudent(theStudent);
		
		
		// send back to list student page
		ListStudents(request, response);
		
	}



	private void loadStudent(HttpServletRequest request, HttpServletResponse response) 
		throws Exception {
		
		// read student id from Form Data
		String theStudentId = request.getParameter("studentId");
		
		// get student from database (DBUtil)
		Student theStudent = studentDbUtil.getStudent(theStudentId);
		
		// place student in the request attribute
		request.setAttribute("THE_STUDENT", theStudent);
		
		// send to JSP page
		RequestDispatcher dispatcher = 
				request.getRequestDispatcher("/update-student-form.jsp");
		dispatcher.forward(request, response);
		
	}



	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// read student info from HTML FORM
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		//create a new student object
		Student theStudent = new Student(firstName, lastName, email);
		
		
		//add the student to the DATABASE
		studentDbUtil.addStudent(theStudent);
		
		// send back to the main page (JSP)		
		ListStudents(request,response);
		
	}



	private void ListStudents(HttpServletRequest request, HttpServletResponse response) 
	
		throws Exception{
		
		// get student from db util
		List<Student> students = studentDbUtil.getStudents();
		
		
		// add to request object
		request.setAttribute("STUDENT_LIST", students);
		
		
		
		// send to jsp view page
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
		dispatcher.forward(request, response);
		
		
	}

}
