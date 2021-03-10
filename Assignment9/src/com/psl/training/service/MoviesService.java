package com.psl.training.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
	
	
//	public static void main(String [] args) {
//		MoviesService service = new MoviesService();
//		File file = new File("C:\\Users\\HP\\git\\Ass09_Khushboo_Ahuja\\Assignment9\\Movies.txt");
//		List<Movies> movies = new ArrayList<>();
//		movies.addAll(service.populateMovies(file));
//		for(Movies m : movies)
//			System.out.println(m);
//		service.addAllMoviesInDb(movies);
//	}
}
