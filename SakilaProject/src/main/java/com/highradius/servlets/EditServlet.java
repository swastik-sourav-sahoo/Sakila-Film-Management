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

@WebServlet(description = "Editform", urlPatterns = { "/EditData" })
public class EditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	Connection conn = null;
	
    public EditServlet() {
    	try {
			DatabaseConnection databaseObj = new DatabaseConnection();
			conn = databaseObj.doDatabaseConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			editData(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	protected void editData(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String filmId = request.getParameter("film_id");
		String title = request.getParameter("title");
		String director = request.getParameter("director");
		String release = request.getParameter("release_year");
		String rating = request.getParameter("rating");
		String description = request.getParameter("description");
		String[] feature = request.getParameterValues("feature");
		String features = String.join(",", feature);
		String language = request.getParameter("language");
		String query = "UPDATE film SET title = ? , description = ? , release_year = ? , language_id = (SELECT language_id from language where name = ?) , rating = ? , special_features = ? , last_update = NOW() , director = ? WHERE film_id = ? ;";
		PreparedStatement preparedStatement = null;
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setString(1,title);
		preparedStatement.setString(2,description);
		preparedStatement.setString(3,release);
		preparedStatement.setString(4,language);
		preparedStatement.setString(5,rating);
		preparedStatement.setString(6,features);
		preparedStatement.setString(7,director);
		preparedStatement.setString(8,filmId);
		preparedStatement.executeUpdate();
	}
	
}
