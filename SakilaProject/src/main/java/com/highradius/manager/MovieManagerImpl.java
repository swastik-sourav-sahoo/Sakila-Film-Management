package com.highradius.manager;

import java.util.ArrayList;
import java.util.Map;

import com.highradius.dao.MovieDao;
import com.highradius.model.LanguageDetails;
import com.highradius.model.MovieDetails;

public class MovieManagerImpl implements MovieManager{

	private MovieDao movieDao;
	
	public MovieDao getMovieDao() {
		return movieDao;
	}

	public void setMovieDao(MovieDao movieDao) {
		this.movieDao = movieDao;
	}

	public Map<String, Object> fetchData(MovieDetails obj, String limit, String offset) throws Exception{
		return movieDao.fetchData(obj, limit, offset);
	}
	
	public Map<String, Object> addData(MovieDetails obj) throws Exception{
		return movieDao.addData(obj);
	}
	
	public ArrayList<LanguageDetails> getLanguages() throws Exception{
		return movieDao.getLanguages();
	}
	
	public Map<String, Object> editData(MovieDetails obj) throws Exception{
		return movieDao.editData(obj);
	}
	
	public Map<String, Object> deleteData(MovieDetails obj) throws Exception{
		return movieDao.deleteData(obj);
	}
	
}
