package com.project.dao;

import org.slf4j.Logger; 
import org.slf4j.LoggerFactory;
import java.sql.Connection; 
import java.sql.PreparedStatement; 
import java.sql.ResultSet; 
import java.sql.SQLException; 
import java.sql.Statement; 
import java.time.LocalDate; 
import java.time.LocalDateTime;
import java.util.ArrayList; 
import java.util.List; 
import com.project.datasource.DataSource; 
import com.project.model.Projekt; 

public class ProjektDAOImpl implements ProjektDAO {

	private static final Logger logger = LoggerFactory.getLogger(ProjektDAOImpl.class); 
	
	@Override
	public Projekt getProjekt(Integer projektId) {
		
		String query = "SELECT * FROM projekt WHERE projekt_id = ?";
		
		try (Connection connect = DataSource.getConnection();
				PreparedStatement prepStmt = connect.prepareStatement(query)) {
			
			prepStmt.setInt(1, projektId);
			
			try (ResultSet rs = prepStmt.executeQuery()) {
				
				if (rs.next()) {
					Projekt projekt = new Projekt();
					projekt.setProjektId(rs.getInt("projekt_id")); 
	                projekt.setNazwa(rs.getString("nazwa")); 
	                projekt.setOpis(rs.getString("opis")); 
	                projekt.setDataCzasUtworzenia(rs.getObject("dataczas_utworzenia", LocalDateTime.class)); 
	                projekt.setDataOddania(rs.getObject("data_oddania", LocalDate.class)); 
	                return projekt;
				} else {
					logger.error("Nie znaleziono projektu o ID równym " + projektId);
					return null;
				}

			}
			
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
		
	}

	@Override
	public void setProjekt(Projekt projekt) {
		 boolean isInsert = projekt.getProjektId() == null; 
	      String query = isInsert ?  
	         "INSERT INTO projekt(nazwa, opis, dataczas_utworzenia, data_oddania) VALUES (?, ?, ?, ?)" 
	        : "UPDATE projekt SET nazwa = ?, opis = ?, dataczas_utworzenia = ?, data_oddania = ?" 
	              + " WHERE projekt_id = ?"; 
	      try (Connection connect = DataSource.getConnection(); 
	         PreparedStatement prepStmt = connect.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) { 
	         //Wstawianie do zapytania odpowiednich wartości w miejsce znaków '?' 
	         //Uwaga! Indeksowanie znaków '?' zaczyna się od 1! 
	         prepStmt.setString(1, projekt.getNazwa());  
	         prepStmt.setString(2, projekt.getOpis()); 
	         if(projekt.getDataCzasUtworzenia() == null)  
	            projekt.setDataCzasUtworzenia(LocalDateTime.now()); 
	         prepStmt.setObject(3,projekt.getDataCzasUtworzenia()); 
	         prepStmt.setObject(4, projekt.getDataOddania()); 
	         if(!isInsert) prepStmt.setInt(5, projekt.getProjektId()); 
	   //Wysyłanie zapytania i pobieranie danych 
	         int liczbaDodanychWierszy = prepStmt.executeUpdate(); 
	   //Pobieranie kluczy głównych, tylko dla nowo utworzonych projektów          
	         if (isInsert && liczbaDodanychWierszy > 0) {  
	            ResultSet keys = prepStmt.getGeneratedKeys(); 
	            if (keys.next()) { 
	               projekt.setProjektId(keys.getInt(1)); 
	            } 
	            keys.close(); 
	         } 
	      }catch(SQLException e) { 
	         throw new RuntimeException(e); 
	      } 
		
	}

	@Override
	public void deleteProjekt(Integer projektId) {

		String query = "DELETE FROM projekt WHERE projekt_id = ?";
		try (Connection connect = DataSource.getConnection();
			PreparedStatement prepStmt = connect.prepareStatement(query)) {
			
			prepStmt.setInt(1, projektId);
			
			int rowsAffected = prepStmt.executeUpdate();
			if (rowsAffected == 0) {
				logger.error("Nie znaleziono projektu o wskazanym id");
			}else {
				logger.info("Usunięto wybrany projekt");
			}
					
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
		
		
	}

	@Override
	public List<Projekt> getProjekty(Integer offset, Integer limit) {
	      List<Projekt> projekty = new ArrayList<>(); 
	      String query = "SELECT * FROM projekt ORDER BY dataczas_utworzenia DESC"  
	            + (offset != null ? " OFFSET ?" : "") 
	            + (limit != null ? " LIMIT ?" : ""); 
	      try (Connection connect = DataSource.getConnection(); 
	            PreparedStatement preparedStmt = connect.prepareStatement(query)) { 
	         int i = 1; 
	         if (offset != null) { 
	            preparedStmt.setInt(i, offset); 
	            i += 1; 
	         } 
	         if (limit != null) { 
	            preparedStmt.setInt(i, limit); 
	         } 
	         try (ResultSet rs = preparedStmt.executeQuery()) { 
	            while (rs.next()) { 
	               Projekt projekt = new Projekt(); 
	               projekt.setProjektId(rs.getInt("projekt_id")); 
	               projekt.setNazwa(rs.getString("nazwa")); 
	               projekt.setOpis(rs.getString("opis")); 
	               projekt.setDataCzasUtworzenia(rs.getObject("dataczas_utworzenia", LocalDateTime.class)); 
	               projekt.setDataOddania(rs.getObject("data_oddania", LocalDate.class)); 
	               projekty.add(projekt); 
	            } 
	         } 
	      }catch(SQLException e) { 
	         throw new RuntimeException(e); 
	      } 
	      return projekty; 
	}

	@Override
	public List<Projekt> getProjektyWhereNazwaLike(String nazwa, Integer offset, Integer limit) {
	    List<Projekt> projekty = new ArrayList<>();

	    if (nazwa == null) {
	        return projekty;
	    }

	    String query = "SELECT * FROM projekt WHERE nazwa LIKE ? ORDER BY dataczas_utworzenia"
	            + (offset != null ? " OFFSET ?" : "")
	            + (limit != null ? " LIMIT ?" : "");

	    try (Connection connect = DataSource.getConnection();
	         PreparedStatement preparedStmt = connect.prepareStatement(query)) {

	        int i = 1;

	        preparedStmt.setString(i++, "%" + nazwa + "%");

	        if (offset != null) {
	            preparedStmt.setInt(i++, offset);
	        }

	        if (limit != null) {
	            preparedStmt.setInt(i, limit);
	        }

	        try (ResultSet rs = preparedStmt.executeQuery()) {
	            while (rs.next()) {
	                Projekt projekt = new Projekt();
	                projekt.setProjektId(rs.getInt("projekt_id"));
	                projekt.setNazwa(rs.getString("nazwa"));
	                projekt.setOpis(rs.getString("opis"));
	                projekt.setDataCzasUtworzenia(rs.getObject("dataczas_utworzenia", LocalDateTime.class));
	                projekt.setDataOddania(rs.getObject("data_oddania", LocalDate.class));
	                projekty.add(projekt);
	            }
	        }

	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }

	    return projekty;
	}


	@Override
	public List<Projekt> getProjektyWhereDataOddaniaIs(LocalDate dataOddania, Integer offset, Integer limit) {
		List<Projekt> projekty = new ArrayList<>();

	    if (dataOddania == null) {
	        return projekty;
	    }

	    String query = "SELECT * FROM projekt WHERE data_oddania = ? ORDER BY dataczas_utworzenia"
	            + (offset != null ? " OFFSET ?" : "")
	            + (limit != null ? " LIMIT ?" : "");

	    try (Connection connect = DataSource.getConnection();
	         PreparedStatement preparedStmt = connect.prepareStatement(query)) {

	        int i = 1;

	        preparedStmt.setDate(i++, java.sql.Date.valueOf(dataOddania));

	        if (offset != null) {
	            preparedStmt.setInt(i++, offset);
	        }

	        if (limit != null) {
	            preparedStmt.setInt(i, limit);
	        }

	        try (ResultSet rs = preparedStmt.executeQuery()) {
	            while (rs.next()) {
	                Projekt projekt = new Projekt();
	                projekt.setProjektId(rs.getInt("projekt_id"));
	                projekt.setNazwa(rs.getString("nazwa"));
	                projekt.setOpis(rs.getString("opis"));
	                projekt.setDataCzasUtworzenia(rs.getObject("dataczas_utworzenia", LocalDateTime.class));
	                projekt.setDataOddania(rs.getObject("data_oddania", LocalDate.class));
	                projekty.add(projekt);
	            }
	        }

	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }

	    return projekty;
	}


	@Override
	public int getRowsNumber() {

	    String query = "SELECT COUNT(*) FROM projekt";

	    try (Connection connect = DataSource.getConnection();
	         PreparedStatement preparedStmt = connect.prepareStatement(query);
	         ResultSet rs = preparedStmt.executeQuery()) {

	        if (rs.next()) {
	            return rs.getInt(1);
	        }

	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }

	    return 0;
	}

	@Override
	public int getRowsNumberWhereNazwaLike(String nazwa) {

	    if (nazwa == null) {
	        return 0;
	    }

	    String query = "SELECT COUNT(*) FROM projekt WHERE nazwa LIKE ?";

	    try (Connection connect = DataSource.getConnection();
	         PreparedStatement preparedStmt = connect.prepareStatement(query)) {

	        preparedStmt.setString(1, "%" + nazwa + "%");

	        try (ResultSet rs = preparedStmt.executeQuery()) {

	            if (rs.next()) {
	                return rs.getInt(1);
	            }
	        }

	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }

	    return 0;
	}

	@Override
	public int getRowsNumberWhereDataOddaniaIs(LocalDate dataOddania) {

	    if (dataOddania == null) {
	        return 0;
	    }

	    String query = "SELECT COUNT(*) FROM projekt WHERE data_oddania = ?";

	    try (Connection connect = DataSource.getConnection();
	         PreparedStatement preparedStmt = connect.prepareStatement(query)) {

	        preparedStmt.setDate(1, java.sql.Date.valueOf(dataOddania));

	        try (ResultSet rs = preparedStmt.executeQuery()) {

	            if (rs.next()) {
	                return rs.getInt(1);
	            }
	        }

	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }

	    return 0;
	}

}
