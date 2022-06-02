package com.highradius.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.highradius.model.LanguageDetails;
import com.highradius.model.MovieDetails;
import com.mysql.cj.util.StringUtils;

public class MovieDaoImpl implements MovieDao{
	@SuppressWarnings("deprecation")
	static SessionFactory factory = new Configuration().configure().buildSessionFactory();
	String totalCount=null;
	
	public Map<String, Object> fetchData(MovieDetails obj, String limit, String offset) throws Exception{
		Map<String, Object> responseData = new HashMap<>();
		String title = obj.getTitle();
		String director = obj.getDirector();
		String release = obj.getRelease_year();
		LanguageDetails language = obj.getLanguageId() == null ? null : obj.getLanguageId();
		//String language = obj.getLang() == null ? null : obj.getLang().getLanguage_id();
		
		Session session = factory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(MovieDetails.class);
			if(!StringUtils.isNullOrEmpty(title)) {
				criteria.add(Restrictions.eq("title", title));
			}
			if(!StringUtils.isNullOrEmpty(director)) {
				criteria.add(Restrictions.eq("director", director));
			}
			if(!StringUtils.isNullOrEmpty(release)) {
				criteria.add(Restrictions.eq("release_year", release));
			}
			if(language !=null) {
				criteria.add(Restrictions.eq("languageId", language));
				//criteria.add(Restrictions.eq("lang", language));
			}
			criteria.setFirstResult(Integer.parseInt(offset));
			criteria.setMaxResults(Integer.parseInt(limit));
			@SuppressWarnings("unchecked")
			List<MovieDetails> resultList = (List<MovieDetails>) criteria.list();
			Iterator<MovieDetails> itr = resultList.iterator();
			ArrayList<MovieDetails> movieList = new ArrayList<>();
			while(itr.hasNext()) {
				MovieDetails element = itr.next();
				MovieDetails movieObj = new MovieDetails();
				movieObj.setFilm_id(element.getFilm_id());
				movieObj.setTitle(element.getTitle());
				movieObj.setDescription(element.getDescription());
				movieObj.setRelease_year(element.getRelease_year().substring(0,4));
				movieObj.setLanguage(element.getLanguageId().getLanguageName());
				movieObj.setRating(element.getRating());
				movieObj.setFeature(element.getFeature());
				movieObj.setDirector(element.getDirector());
				movieList.add(movieObj);
			}
			criteria.setFirstResult(0);
			int count = Integer.parseInt(String.valueOf((long) criteria.setProjection(Projections.rowCount()).uniqueResult()));
			transaction.commit();
			responseData.put("film", movieList);
			responseData.put("total", count);
			responseData.put("success", true);
		}catch(Exception e) {
			if (transaction!=null)
				transaction.rollback();
			System.out.println("Error in fetchData");
			responseData.put("failure", true);
		}
		session.close();
		return responseData;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<LanguageDetails> getLanguages() throws Exception{
		Session session = factory.openSession();
		System.out.println(session);
		Transaction transaction = null;
		ArrayList<LanguageDetails> allLanguage = null;
		try{
			transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(LanguageDetails.class);
			allLanguage = (ArrayList<LanguageDetails>) criteria.list();
			transaction.commit();
		}catch(Exception e){
			if (transaction!=null)
				transaction.rollback();
			System.out.println("Error in getLanguages");
		}
		return allLanguage;
	}
	
	public Map<String, Object> addData(MovieDetails obj) throws Exception{
		Map<String, Object> responseData = new HashMap<>();
		Session session = factory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.save(obj);
			transaction.commit();
			responseData.put("success", true);
		}catch(Exception e) {
			if (transaction!=null)
				transaction.rollback();
			System.out.println("Error in addData");
			responseData.put("failure", true);
		}
		session.close();
		return responseData;
	}
	
	public Map<String, Object> editData(MovieDetails obj) throws Exception{
		Map<String, Object> responseData = new HashMap<>();
		Transaction transaction = null;
		Session session = factory.openSession();
		try {
			transaction = session.beginTransaction();
			session.update(obj);
			transaction.commit();
			responseData.put("success", true);
		}catch(Exception e) {
			if (transaction!=null)
				transaction.rollback();
			System.out.println("Error in editData");
			responseData.put("failure", true);
		}
		session.close();
		return responseData;
	}
	
	public Map<String, Object> deleteData(MovieDetails obj) throws Exception{
		Map<String, Object> responseData = new HashMap<>();
		Transaction transaction = null;
		Session session = factory.openSession();
		try {
			transaction = session.beginTransaction();
			session.delete(obj);
			transaction.commit();
			responseData.put("success", true);
		}catch(Exception e) {
			if (transaction!=null)
				transaction.rollback();
			System.out.println("Error in deleteData");
			responseData.put("failure", true);
		}
		session.close();
		return responseData;
	}
}