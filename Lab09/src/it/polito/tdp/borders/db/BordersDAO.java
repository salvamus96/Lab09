package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.CountryIdMap;

public class BordersDAO {

	public List<Country> loadAllCountries(CountryIdMap countryIdMap) {

		String sql = "SELECT ccode, StateAbb, StateNme FROM country ORDER BY StateAbb";
		List<Country> result = new ArrayList<Country>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Country c = new Country(rs.getInt("ccode"), rs.getString("StateAbb"), rs.getString("StateNme"));
				
				result.add(countryIdMap.get(c));
			}
			
			conn.close();
			return result;

		} catch (SQLException e) {
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Border> getCountryPairs(int anno, CountryIdMap countryIdMap) {
		
		String sql = "SELECT c1.ccode, c2.ccode " +
					 "FROM country as c1, country as c2, contiguity as c " +
					 "WHERE c1.CCode = c.state1no and c2.CCode = c.state2no and c.conttype = 1 and c.year <= ?";
		
		List<Border> result = new ArrayList <Border> ();
		
		try {
			
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();
			
			while (rs.next()) {
				Country c1 = countryIdMap.get(rs.getInt("c1.ccode"));
				Country c2 = countryIdMap.get(rs.getInt("c2.ccode"));
				
				if(c1 == null && c2 == null)
					throw new RuntimeException("Country not found");
				
				Border b = new Border (c1, c2);

				result.add(b);
			}
		
			conn.close();
			return result;

		} catch (SQLException e) {
			throw new RuntimeException("Error Connection Database");
		}
		
	}
}
