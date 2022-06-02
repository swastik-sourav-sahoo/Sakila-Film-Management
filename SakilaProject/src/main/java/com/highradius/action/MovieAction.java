package com.highradius.action;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.highradius.manager.MovieManager;
import com.highradius.model.LanguageDetails;
import com.highradius.model.MovieDetails;

@SuppressWarnings("resource")
public class MovieAction {
	
	String limit;
	String start;
	String film_id;
	String title;
	String director;
	String release_year;
	String language;
	String rating;
	String description;
	String[] feature;
	
	Map<String, Object> responseData = new HashMap<>();

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getFilm_id() {
		return film_id;
	}

	public void setFilm_id(String film_id) {
		this.film_id = film_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getRelease_year() {
		return release_year;
	}
	public void setRelease_year(String release_year) {
		this.release_year = release_year;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Map<String, Object> getResponseData() {
		return responseData;
	}

	public void setResponseData(Map<String, Object> responseData) {
		this.responseData = responseData;
	}
	
	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String[] getFeature() {
		return feature;
	}

	public void setFeature(String[] feature) {
		this.feature = feature;
	}

	public String fetchData() throws Exception{
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		MovieManager movieManager = (MovieManager)context.getBean("movieManager");
		
		MovieDetails fetchObj = new MovieDetails();
		fetchObj.setTitle(getTitle());
		fetchObj.setDirector(getDirector());
		fetchObj.setRelease_year(getRelease_year());
		if(getLanguage() != null) {
			List<LanguageDetails> allLanguages = movieManager.getLanguages();
			for(LanguageDetails eachLanguage : allLanguages) {
				if(String.valueOf(eachLanguage.getLanguageId()).equals(getLanguage())) {
					fetchObj.setLanguageId(eachLanguage);
					break;
				}
			}
		}
		responseData = movieManager.fetchData(fetchObj, getLimit(), getStart());
		return "success";
	}
	
	public String addData() throws Exception{
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		MovieManager movieManager = (MovieManager)context.getBean("movieManager");
		
		MovieDetails addObj = new MovieDetails();
		addObj.setTitle(getTitle());
		addObj.setDirector(getDirector());
		addObj.setRelease_year(getRelease_year());
		addObj.setRating(getRating());
		addObj.setDescription(getDescription().length()==0 ? null : getDescription());
		String[]feature = getFeature();
		String features = String.join(",", feature);
		addObj.setFeature(features);
		List<LanguageDetails> allLanguages = movieManager.getLanguages();
		for(LanguageDetails eachLanguage : allLanguages) {
			if(String.valueOf(eachLanguage.getLanguageName()).equals(getLanguage())) {
				addObj.setLanguageId(eachLanguage);
				break;
			}
		}
		responseData = movieManager.addData(addObj);
		return "success";
	}
	
	public String editData() throws Exception{
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		MovieManager movieManager = (MovieManager)context.getBean("movieManager");
		
		MovieDetails editObj = new MovieDetails();
		editObj.setTitle(getTitle());
		editObj.setDirector(getDirector());
		editObj.setRelease_year(getRelease_year());
		editObj.setRating(getRating());
		editObj.setDescription(getDescription().length()==0 ? null : getDescription());
		String[]feature = getFeature();
		String features = String.join(",", feature);
		editObj.setFeature(features);
		editObj.setFilm_id(getFilm_id());
		List<LanguageDetails> allLanguages = movieManager.getLanguages();
		for(LanguageDetails eachLanguage : allLanguages) {
			if(String.valueOf(eachLanguage.getLanguageName()).equals(getLanguage())) {
				editObj.setLanguageId(eachLanguage);
				break;
			}
		}
		
		responseData = movieManager.editData(editObj);
		return "success";
	}
	
	public String deleteData() throws Exception{
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		MovieManager movieManager = (MovieManager)context.getBean("movieManager");
		
		MovieDetails deleteObj = new MovieDetails();
		
		String film_id = getFilm_id();
		String[] filmIds = film_id.split(",");
		for(String filmId : filmIds) {
			deleteObj.setFilm_id(filmId);
			responseData = null;
			responseData = movieManager.deleteData(deleteObj);
		}
		return "success";
	}
}