package com.psl.training.dao;

import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.psl.training.Category;
import com.psl.training.Language;
import com.psl.training.Movies;
import com.psl.training.util.DBConnectionUtil;

public class MoviesDao {
	public boolean addAllMoviesInDb(List<Movies> movies) {
		Connection con  = DBConnectionUtil.getConnection();
		PreparedStatement pstmt = null;
		
		for(Movies m : movies) {
			try {
				pstmt = con.prepareStatement("insert into movies values (?,?,?,?,?,?,?)");
				pstmt.setInt(1, m.getMovieId());
				pstmt.setString(2, m.getMovieName());
				pstmt.setString(3, m.getMovieType().name());
				pstmt.setString(4, m.getLanguage().name());
				pstmt.setDate(5, m.getReleaseDate());
				pstmt.setDouble(6, m.getRating());
				pstmt.setDouble(7, m.getTotalBusinessDone());
				pstmt.executeUpdate();
				
				for(String s:m.getCasting()) {
				     pstmt = con.prepareStatement("insert into casting values (?,?)");
				     pstmt.setInt(1,m.getMovieId());
				     pstmt.setString(2, s);
				     pstmt.executeUpdate();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}
}