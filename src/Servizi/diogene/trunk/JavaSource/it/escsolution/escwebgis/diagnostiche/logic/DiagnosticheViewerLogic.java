package it.escsolution.escwebgis.diagnostiche.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.diagnostiche.util.DiaBridge;
import it.webred.ct.diagnostics.service.data.dto.DiaValueKeysDTO;

public class DiagnosticheViewerLogic extends EscLogic {
	
	private String appoggioDataSource;
	
	public DiagnosticheViewerLogic(EnvUtente eu) {
		super(eu);
		appoggioDataSource = eu.getDataSource();
	}
	
	public ArrayList<HashMap<String, String>> getDiaIntestazioni(String tipi, String fonti, String key) throws Exception {
		try {
			ArrayList<HashMap<String, String>> diaIntestazioni = DiaBridge.getDiaIntestazioni(tipi, fonti, key);
			for (HashMap<String, String> intestazione : diaIntestazioni) {
				intestazione.put(DiaBridge.FONTE, getDescFonte(intestazione.get(DiaBridge.ID_FONTE)));
			}
			return diaIntestazioni;
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
	}
	
	public ArrayList<HashMap<Long,List<DiaValueKeysDTO[]>>> getDiaDati(String tipi, String fonti, String key) throws Exception {
		try {
			String ente = this.getEnvUtente().getEnte();
			String name = this.getEnvUtente().getUtente().getName();
			ArrayList<HashMap<Long,List<DiaValueKeysDTO[]>>> diaDati = DiaBridge.getDiaDati(ente, name, tipi, fonti, key);
			return diaDati;
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
	}
	
	private String getDescFonte(String idFonte) throws Exception {
		Connection conn = null;
		try {
			conn = this.getConnectionDiogene();
			String sql = "SELECT DESCRIZIONE FROM AM_FONTE WHERE ID = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, Long.parseLong(idFonte));
			ResultSet rs = ps.executeQuery();
			String descFonte = "";
			while (rs.next()) {
				descFonte = rs.getString("DESCRIZIONE");
			}
			rs.close();
			ps.close();
			return descFonte;
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (conn != null)
				if (!conn.isClosed())
					conn.close();
		}		
	}

}
