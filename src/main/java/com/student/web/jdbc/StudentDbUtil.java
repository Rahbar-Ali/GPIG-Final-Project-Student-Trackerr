package com.student.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentDbUtil {
	
	private DataSource dataSource;
	
	public StudentDbUtil(DataSource theDataSource) {
		dataSource = theDataSource;
	}

	public List<Student> getStudents() throws Exception {
		
		List<Student> students = new ArrayList<>();
		
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		
		try {
			// get a Connection
			myConn = dataSource.getConnection();
			
			// create sql statement
			String sql = "select * from student order by last_name";
			
			myStmt = myConn.createStatement();
			
			// excute query
			myRs = myStmt.executeQuery(sql);
			
			// process result set
			while (myRs.next()) {
					// retrieve data from result set row
					
					int id = myRs .getInt("id");
					String firstName = myRs.getString("first_name");
					String lastName = myRs.getString("last_name");
					String email = myRs.getString("email");
					
					
					// create new student object
					Student tempStudent = new Student(id, firstName, lastName, email);
					
					// add it to the list of students
					students.add(tempStudent);
			}
			
			return students;
		}
		finally {
			// Close HSBC object
			close(myConn, myStmt, myRs);
		}
	
	}

	private void close(Connection myConn, Statement myStmt, ResultSet myRs) {
		
		try {
			if (myRs != null) {
				myRs.close();
			}
			if (myStmt != null) {
				myStmt.close();	
			}
			if (myConn != null) {
				myConn.close();
			}
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public void addStudent(Student theStudent) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
		// get DB connection
			myConn = dataSource.getConnection();
			
		// create SQL for insert
		String sql = "insert into student "
					+ "(first_name, last_name, email) "
					+ "values (?,?,?)";
			
		myStmt = myConn.prepareStatement(sql);
		// set parameter value for the student
		myStmt.setString(1, theStudent.getFirstName());
		myStmt.setString(2, theStudent.getLastName());
		myStmt.setString(3, theStudent.getEmail());
		
		
		
		// excute sql insert
		myStmt.execute();
		
		
		}
		finally {
		// clean up JSBC object
			close(myConn, myStmt, null);
		}
		}

	public Student getStudent(String theStudentId) throws Exception {
		
		Student theStudent = null;
		 
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		int studentId;
		
		try {
			// convert student id to int
			studentId	=	Integer.parseInt(theStudentId);
			
			// get connection to DB
			myConn = dataSource.getConnection();
			
			// create SQL
			String sql = "select * from student where id=?";
			
			
			// create PreparedStatement
			myStmt = myConn.prepareStatement(sql);
			
			// set parameter
			myStmt.setInt(1, studentId);
			
			// excute SQL Querry
			myRs = myStmt.executeQuery();
			
			// retrieve data From result set
			if (myRs.next()) {
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");
			
				// use the StudentId during construction
				theStudent = new Student(studentId, firstName, lastName, email);
				
			} else {
				throw new Exception("Could not find Student Id" + studentId);
			}
			
			
		return theStudent;
		}
		finally
		{
			// Clean up JDBS
			close(myConn, myStmt, myRs);
		}		
	}

	public void updateStudent(Student theStudent) 
		throws Exception{
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
			// get db connection
			myConn = dataSource.getConnection();
			
			// create SQL update statement
			String sql = "update student "
						+ "set first_name=?, last_name=?, email=? "
						+ "where id=?";
			
			// prepare statement
			myStmt = myConn.prepareStatement(sql);
			
			// set params
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());
			myStmt.setInt(4, theStudent.getId());
			
			// execute SQL statement
			myStmt.execute();
		}
		finally {
			// clean up JDBC objects
			close(myConn, myStmt, null);
		}
	}

	public void deleteStudent(String theStudentId) throws Exception{
	
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
			// Convert Student Id to int
			int studentId = Integer.parseInt(theStudentId);
			
			
			// get connection to db
			myConn = dataSource.getConnection();
			
			//create sql to delete student
			String sql = "delete from student where id=?";
			
			
			// prepare statement
			myStmt = myConn.prepareStatement(sql);
			
			
			// set param
			myStmt.setInt(1, studentId);
			
			
			// excute sql statement
			myStmt.execute();
		}
		finally {
			// clean up JDBC
			close(myConn, myStmt, null);
		}
		
		
	}
}



