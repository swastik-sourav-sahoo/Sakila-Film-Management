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

/**
 * Servlet implementation class DeleteServlet
 */
@WebServlet(description = "Deleteform", urlPatterns = { "/DeleteData" })
public class DeleteServlet extends HttpServlet {
private static final long serialVersionUID = 1L;
    
	Connection conn = null;
	
    public DeleteServlet() {
    	try {
			DatabaseConnection databaseObj = new DatabaseConnection();
			conn = databaseObj.doDatabaseConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			deleteData(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	protected void deleteData(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String filmId = request.getParameter("film_id");
		String[] filmIds = filmId.split(",");
		for(String eachFilmId : filmIds) {
			String query = "DELETE FROM film WHERE film_id = ? ;";
			PreparedStatement preparedStatement = null;
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1,eachFilmId);
			preparedStatement.executeUpdate();
		}
	}
	
}
