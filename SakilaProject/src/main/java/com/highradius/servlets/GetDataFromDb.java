package com.highradius.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.google.gson.Gson;
import com.highradius.resource.DatabaseConnection;

@WebServlet(description = "GetDataFromDb", urlPatterns = { "/GetDataFromDb" })
public class GetDataFromDb extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	String totalCount=null;
	Connection conn = null;
	
	public GetDataFromDb() {
		try {
			DatabaseConnection databaseObj = new DatabaseConnection();
			conn = databaseObj.doDatabaseConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		try {
			getTotalCount();
			fetchData(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doGet(request, response);
	}
	
	protected void fetchData(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String limit = request.getParameter("limit");
		String offset = request.getParameter("start");
		String title = request.getParameter("title");
		String director = request.getParameter("director");
		String release = request.getParameter("release_year");
		String language = request.getParameter("language");
		
		String query="";
		int para=0;
		
		if((!"".equals(title) && title!=null) || (!"".equals(director) && director!=null) || (!"".equals(release) && release!=null) || (!"".equals(language)&& language!=null)) {
			String query1 = "SELECT * FROM film";
			String query3 = " limit ? offset ?";
			String query2=" WHERE";
			if(!"".equals(title) && title!=null) {
				query2 = query2 + " title = '" + title + "'";
				para++;
			}
			if(!"".equals(director) && director!=null) {
				if(para>0)
					query2 = query2 + " AND director = '" + director + "'";
				else
					query2 = query2 + " director = '" + director + "'";
				para++;
			}
			if(!"".equals(release) && release!=null) {
				String [] releaseParts = release.split("-");
				if(para>0)
					query2 = query2 + " AND release_year = '" + releaseParts[0] + "'";
				else
					query2 = query2 + " release_year = '" + releaseParts[0] + "'";
				para++;
			}
			if(!"".equals(language) && language!=null) {
				if(para>0)
					query2 = query2 + " AND language_id = '" + language + "'";
				else
					query2 = query2 + " language_id = '" + language + "'";
				para++;
			}
			query = query1 + query2 + query3;
		} else {
			query = "SELECT * FROM film limit ? offset ?";
		}

		PreparedStatement preparedStatement = null;
		preparedStatement = conn.prepareStatement(query);
		preparedStatement.setInt(1, Integer.parseInt(limit));
		preparedStatement.setInt(2, Integer.parseInt(offset));
		ResultSet rs = preparedStatement.executeQuery();

		ArrayList<MovieDetail> movieList = new ArrayList<>();
		while(rs.next()){
			MovieDetail movieObj = new MovieDetail();
			movieObj.setFilm_id(rs.getString("film_id"));
			movieObj.setTitle(rs.getString("title"));
			movieObj.setDescription(rs.getString("description"));
			movieObj.setRelease_year(rs.getString("release_year").substring(0,4));
			movieObj.setLanguage(getLanguageName(rs.getString("language_id")));
			movieObj.setRating(rs.getString("rating"));
			movieObj.setFeature(rs.getString("special_features"));
			movieObj.setDirector(rs.getString("director"));
			movieList.add(movieObj);
		}
		String jsonResponse = new Gson().toJson(movieList);
		jsonResponse = "{\"success\":true,\"total\":" + totalCount + ",\"film\":" + jsonResponse + "}";
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		out.print(jsonResponse);
		out.flush();
	}
	
	protected void getTotalCount() throws Exception{
		String query = "SELECT COUNT(*) from film";
		PreparedStatement preparedStatement = null;
		preparedStatement = conn.prepareStatement(query);
		ResultSet rs = preparedStatement.executeQuery();
		while(rs.next()) {
			totalCount = rs.getString("COUNT(*)");
		}
	}
	
	protected String getLanguageName(String id) throws Exception{
		String query = "SELECT `name` from language where language_id = " + id;
		String name = null;
		PreparedStatement preparedStatement = null;
		preparedStatement = conn.prepareStatement(query);
		ResultSet rs = preparedStatement.executeQuery();
		while(rs.next()) {
			name = rs.getString("name");
		}
		return name;
	}
}
