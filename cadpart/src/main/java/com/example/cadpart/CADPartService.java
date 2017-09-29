package com.example.cadpart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import at.smartpart.beans.MyCADPart;

import java.util.ArrayList;
import java.util.List;

@Component
public class CADPartService {

	List<String> mostWantedParts = new ArrayList<String>();

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<MyCADPart> findAll() {
		return jdbcTemplate.query(
				"SELECT id, prt_number, bbx_max, bbx_med, bbx_min, volume, surface, density, total_surfaces, plane_surfaces FROM fingerprint",
				(rs, rowNum) -> new MyCADPart(rs.getInt("id"), rs.getString("prt_number"), rs.getDouble("bbx_max"),
						rs.getDouble("bbx_med"), rs.getDouble("bbx_min"), rs.getDouble("volume"),
						rs.getDouble("surface"), rs.getDouble("density"), rs.getInt("total_surfaces"),
						rs.getInt("plane_surfaces")));
	}

	public void update(MyCADPart cadPart) {
		jdbcTemplate.update(
				"UPDATE fingerprint SET prt_number=?, bbx_max=?, bbx_med=?, bbx_min=?, volume=?, surface=?, density=?, total_surfaces=?, plane_surfaces=? WHERE id=?",
				cadPart.getPrtNumber(), cadPart.getMaxDim(), cadPart.getMedDim(), cadPart.getMinDim());

	}

	public List<MySQLSidePanelBean> fetchPanelContent(int id) {
		return jdbcTemplate.query("SELECT id, filename, thumbnail, version from thumbs WHERE id='" + id + "';",
				(rs, rowNum) -> new MySQLSidePanelBean(rs.getString("filename"), rs.getBinaryStream("thumbnail"),
						rs.getString("version")));

	}

	public List<MyCADPart> findByCategory(String category, String value) {
		List<MyCADPart> cadParts = new ArrayList<MyCADPart>();
		cadParts = jdbcTemplate.query(
				"SELECT id, prt_number, bbx_max, bbx_med, bbx_min, volume, surface, density, total_surfaces, plane_surfaces FROM fingerprint WHERE " + category + " like '"
						+ value + "';",
				(rs, rowNum) -> new MyCADPart(rs.getInt("id"), rs.getString("prt_number"), rs.getDouble("bbx_max"),
						rs.getDouble("bbx_med"), rs.getDouble("bbx_min"), rs.getDouble("volume"),
						rs.getDouble("surface"), rs.getDouble("density"), rs.getInt("total_surfaces"),
						rs.getInt("plane_surfaces")));

		addToList(value);

		return cadParts;
	}
	
	public List<MyCADPart> findByName(String prt_number, String version) {
		List<MyCADPart> cadParts = new ArrayList<MyCADPart>();
		cadParts = jdbcTemplate.query(
				"SELECT id, prt_number, bbx_max, bbx_med, bbx_min, volume, surface, density, total_surfaces, plane_surfaces FROM fingerprint WHERE prt_number like '"
						+ prt_number + "' AND version = '" + version +  "' ORDER by VERSION DESC;",
				(rs, rowNum) -> new MyCADPart(rs.getInt("id"), rs.getString("prt_number"), rs.getDouble("bbx_max"),
						rs.getDouble("bbx_med"), rs.getDouble("bbx_min"), rs.getDouble("volume"),
						rs.getDouble("surface"), rs.getDouble("density"), rs.getInt("total_surfaces"),
						rs.getInt("plane_surfaces")));

		return cadParts;
	}

	private void addToList(String prt_number) {

		if (mostWantedParts.size() >= 20) {

			List<String> mostWantedTempList = new ArrayList<String>(mostWantedParts.subList(mostWantedParts.size() - 19, mostWantedParts.size()));
			mostWantedTempList.add(prt_number);

			mostWantedParts = mostWantedTempList;

		}

		else {

			mostWantedParts.add(prt_number);

		}
	}

	public List<CADPartUserBean> getTokenForUser(String emailAddr) {
		return jdbcTemplate.query("SELECT email, token, logins from userdata WHERE email = '" + emailAddr + "';", (rs,
				rowNum) -> new CADPartUserBean(rs.getString("emailAdd"), rs.getString("token"), rs.getInt("logins")));
	}
	
	public List<PRT> getTreeForAsm(String prt_number) {
		return jdbcTemplate.query("SELECT filename, version, myasm from fingerprint.asms WHERE filename = '" + prt_number + "' ORDER BY version DESC;", (rs,
				rowNum) -> new PRT(rs.getString("filename"), rs.getString("version"), rs.getObject("myasm")));
	}
	
	public List<PRT> getDrwMemberList(String prt_number, String version) {
		return jdbcTemplate.query("SELECT filename, version, mydrw from fingerprint.drws WHERE filename like '" + prt_number + "' AND version = '" + version + "' ORDER BY VERSION ASC;", (rs,
				rowNum) -> new PRT(rs.getString("filename"), rs.getString("version"), rs.getObject("mydrw")));
	}

	public List<PRT> getPtcPrt(String preparedSelect) {
		return jdbcTemplate.query(preparedSelect, (rs,
				rowNum) -> new PRT(rs.getString("filename"), rs.getString("version"), rs.getObject(5)));
	}

	
}
