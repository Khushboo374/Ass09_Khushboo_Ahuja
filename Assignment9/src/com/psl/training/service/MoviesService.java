package com.psl.training.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Scanner;
import java.util.Map.Entry;

import com.psl.training.Category;
import com.psl.training.Language;
import com.psl.training.Movies;
import com.psl.training.dao.MoviesDao;

public class MoviesService {
	MoviesDao obj = new MoviesDao();
	public List <Movies> populateMovies(File file)
	{
		List<Movies> movieList = new ArrayList<>();
		try {
			Scanner sc = new Scanner(file);
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d/M/yy");
			while(sc.hasNext()) {
				String data[] = sc.nextLine().split(",");
				Movies movie = new Movies(Integer.parseInt(data[0]),data[1],Category.valueOf(data[2].replaceAll(" ", "_").toUpperCase()),Language.valueOf(data[3].toUpperCase()),Date.valueOf(LocalDate.parse(data[4],dateTimeFormatter)),List.of(data[5].split("-")),Double.parseDouble(data[6]),Double.parseDouble(data[7]));
				movieList.add(movie);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return movieList;
	}
	
	public boolean addAllMoviesInDb(List<Movies> movies) {
		return obj.addAllMoviesInDb(movies);
	}
	
	public void addMovie(Movies movie,List<Movies> movies) {
		movies.add(movie);
		obj.addMovie(movie);
	}
	
	public void serializeMovies(List<Movies> movies, String fileName) {
		File file = new File(fileName);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(movies);
			oos.close();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Movies> deserializeMovie(String filename){
		List <Movies> movies = new ArrayList<>();
		File file = new File(filename);
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			movies = (List<Movies>) ois.readObject();
			ois.close();
			fis.close();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return movies;
	}
	
	public List<Movies> getMoviesRealeasedInYear(List<Movies> movies,int year){
		List <Movies> moviesReleasedInYear = new ArrayList<>();
		for(Movies m:movies) {
			LocalDate date = m.getReleaseDate().toLocalDate();
			if(date.getYear() == year)
				moviesReleasedInYear.add(m);
		}
		return moviesReleasedInYear;
	}
	
	public List<Movies> getMoviesByActor(List<Movies> movies,String...actorNames){
		List<Movies> moviesByActor =  new ArrayList<>();
		for(Movies m:movies) {
			for(String actor:actorNames) {
				if(m.getCasting().contains(actor)) {
					moviesByActor.add(m);
					break;
				}
			}
		}
		return moviesByActor;
	}
	
	public void updateRatings(Movies movie,double rating,List<Movies> movies) {
		if(movies.contains(movie)) {
			movie.setRating(rating);
			obj.updateRatings(movie, rating);
			System.out.println("Rating Updated");
		}
		else {
			System.out.println("Movie does not exist in the list.");
		}
	}
	
	public void updateBusiness(Movies movie, double amount,List<Movies> movies) {
		if(movies.contains(movie)) {
			movie.setTotalBusinessDone(amount);
			obj.updateBusiness(movie, amount);
			System.out.println("Business Updated");
		}
		else {
			System.out.println("Movie does not exist in the list.");
		}
	}
	
	public Map<Language,Set<Movies>> businessDone(List<Movies> movies,double amount){
		Set <Movies> movieSet = new TreeSet<>();
		Map <Language,Set<Movies>> movieMap = new HashMap<>();
		for(Movies movie:movies) {
			if(movie.getTotalBusinessDone() > amount) {
				movieSet.add(movie);
				if(movieMap.containsKey(movie.getLanguage())) {
					movieMap.get(movie.getLanguage()).add(movie);
				}
				else {
					movieMap.put(movie.getLanguage(), movieSet);
				}
			}
		}
		
//		Set<Entry<Language,Set<Movies>>> entries = movieMap.entrySet();
//		for(Entry<Language,Set<Movies>> e:entries) {
//			System.out.println(e.getKey() + " : " + e.getValue());
//		}
		return movieMap;
	}
	
}
