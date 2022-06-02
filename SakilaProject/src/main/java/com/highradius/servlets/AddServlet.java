package com.highradius.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.highradius.resource.DatabaseConnection;

@WebServlet(description = "Addform", urlPatterns = { "/AddData" })
public class AddServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	Connection conn = null;
	
    public AddServlet() {
    	try {
			DatabaseConnection databaseObj = new DatabaseConnection();
			conn = databaseObj.doDatabaseConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			addData(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	protected void addData(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String title = request.getParameter("title");
		String director = request.getParameter("director");
		String release = request.getParameter("release_year");
		String rating = request.getParameter("rating");
		String description = request.getParameter("description");
		String[] feature = request.getParameterValues("feature");
		String features = String.join(",", feature);
		String language = request.getParameter("language");
		String query="INSERT INTO film (`title`,`director`,`release_year`,`rating`,`description`,`special_features`,`language_id`,`last_update`) VALUES (?,?,?,?,?,?,(SELECT language_id FROM language where `name` = ?),NOW());";
		PreparedStatement preparedStatement = null;
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setString(1, title);
		preparedStatement.setString(2, director);
		preparedStatement.setString(3, release);
		preparedStatement.setString(4, rating);
		preparedStatement.setString(5, description);
		preparedStatement.setString(6, features);
		preparedStatement.setString(7, language);
		preparedStatement.executeUpdate();
	}
	
}
