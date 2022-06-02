package com.highradius.manager;

import java.util.ArrayList;
import java.util.Map;

import com.highradius.model.LanguageDetails;
import com.highradius.model.MovieDetails;

public interface MovieManager {

	public Map<String, Object> fetchData(MovieDetails obj, String limit, String offset) throws Exception;
	
	public Map<String, Object> addData(MovieDetails obj) throws Exception;
	
	public Map<String, Object> editData(MovieDetails obj) throws Exception;
	
	public Map<String, Object> deleteData(MovieDetails obj) throws Exception;
	
	public ArrayList<LanguageDetails> getLanguages() throws Exception;
	
}
