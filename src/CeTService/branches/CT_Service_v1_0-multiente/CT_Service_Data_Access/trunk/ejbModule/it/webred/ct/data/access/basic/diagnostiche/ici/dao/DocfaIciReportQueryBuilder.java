package it.webred.ct.data.access.basic.diagnostiche.ici.dao;

import it.webred.ct.data.access.basic.diagnostiche.dto.RicercaDocfaReportDTO;
import it.webred.ct.data.access.basic.diagnostiche.dto.RicercaDocfaReportSoggDTO;
import it.webred.ct.data.model.diagnostiche.DocfaIciReport;
import it.webred.ct.data.model.diagnostiche.DocfaIciReportSogg;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DocfaIciReportQueryBuilder {
	
	private RicercaDocfaReportDTO criteri ;
	
	private String SQL_DocfaIciReport = "FROM DocfaIciReport d WHERE 1 = 1 ";
	private String SQL_DocfaIciReport_SOGG = "FROM DocfaIciReport d, DocfaIciReportSogg s WHERE s.id.fkIdRep = d.idRep ";
	private final static String ORDER_BY  =" ORDER BY d.fornitura, d.foglio, d.particella, d.subalterno ";
	
	public DocfaIciReportQueryBuilder(RicercaDocfaReportDTO criteri) {
		this.criteri = criteri;
	}
	
	public String createQuery(boolean isCount) {
		String sql = "";
		if (criteri != null){
	
			if (isCount)
				sql = "SELECT COUNT(DISTINCT d) ";
			else sql = "SELECT distinct d ";
			
			if(criteri.getTitolare().notEmpty())
				sql += SQL_DocfaIciReport_SOGG;
			else sql += SQL_DocfaIciReport;
			
			sql += getSQLCriteria();
			
			sql += ORDER_BY;
		
		}
		System.out.println("SQL ["+sql+"]");
		return sql;
	}
	
	private String getSQLCriteria() {
		String sqlCriteria = "";		
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
		
		sqlCriteria = (criteri.getIdRepDaEscludere() == null  || criteri.getIdRepDaEscludere().equals("") ? sqlCriteria : sqlCriteria + " AND d.idRep != '" + criteri.getIdRepDaEscludere() + "'");
		
		//Parametri catastali immobile DOCFA
		sqlCriteria = (criteri.getFoglio() == null  || criteri.getFoglio().equals("") ? sqlCriteria : sqlCriteria + " AND d.foglio=LPAD('" + criteri.getFoglio() + "',4,'0')");
		sqlCriteria = (criteri.getParticella() == null  || criteri.getParticella().equals("") ? sqlCriteria : sqlCriteria + " AND d.particella=LPAD('" + criteri.getParticella()+ "',5,'0')");
		sqlCriteria = (criteri.getUnimm() == null  || criteri.getUnimm().equals("") ? sqlCriteria : (criteri.getUnimm().equals("null")? sqlCriteria + " AND d.subalterno IS NULL": sqlCriteria + " AND d.subalterno=LPAD('" + criteri.getUnimm()+ "',4,'0')"));
	
		//Parametri dichiarazione DOCFA
		sqlCriteria = (criteri.getFornitura() == null  ? sqlCriteria : sqlCriteria + " AND d.fornitura =TO_DATE('" + f.format(criteri.getFornitura()) + "', 'yyyyMMdd')");
		sqlCriteria = (criteri.getProtocolloDocfa() == null  || criteri.getProtocolloDocfa().equals("") ? sqlCriteria : sqlCriteria + " AND UPPER(d.protocolloDocfa) ='" + criteri.getProtocolloDocfa().toUpperCase() + "'");
		sqlCriteria = (criteri.getDataRegistrazioneDocfa() == null ? sqlCriteria : sqlCriteria + " AND d.dataDocfa = '"+ f.format(criteri.getDataRegistrazioneDocfa())+ "'");
		sqlCriteria = (criteri.getTipoOperDocfa() == null  || criteri.getTipoOperDocfa().equals("") ? sqlCriteria : sqlCriteria + " AND d.tipoOperDocfa ='" + criteri.getTipoOperDocfa() + "'");
		if(criteri.getCausaleDocfa() != null  && !criteri.getCausaleDocfa().equals("")){
			String inCau = "";
			for(String cau: criteri.getCausaliDocfa()){
				inCau += ",'" + cau + "'";
			}
			sqlCriteria = sqlCriteria + " AND UPPER(d.causaleDocfa) IN (" + inCau.replaceFirst(",", "") + ")";
		}
		if(criteri.getCategoriaDocfa() != null  && !criteri.getCategoriaDocfa().equals("")){
			String inCat = "";
			for(String cat: criteri.getCategorieDocfa()){
				inCat += ",'" + cat + "'";
			}
			sqlCriteria = sqlCriteria + " AND UPPER(d.categoriaDocfa) IN (" + inCat.replaceFirst(",", "") + ")";
		}
		sqlCriteria = (criteri.getCivicoDocfa()== null  || criteri.getCivicoDocfa().equals("") ? sqlCriteria : sqlCriteria + " AND UPPER(d.civiciDocfa) ='" + criteri.getCivicoDocfa().toUpperCase() + "'");
		sqlCriteria = (criteri.getViaDocfa() == null  || criteri.getViaDocfa().equals("") ? sqlCriteria : sqlCriteria + " AND UPPER(d.viaDocfa) LIKE '%" + criteri.getViaDocfa().replace("'","''").toUpperCase() + "%'");
		
		//Parametri Flag Elaborazione
		sqlCriteria = (criteri.getFlgElaborati()== null  || criteri.getFlgElaborati().equals("") ? sqlCriteria : sqlCriteria + " AND d.flgElaborato ='" + criteri.getFlgElaborati() + "'");
		sqlCriteria = (criteri.getFlgAnomali()== null  || criteri.getFlgAnomali().equals("") ? sqlCriteria : sqlCriteria + " AND d.flgAnomalie ='" + criteri.getFlgAnomali() + "'");
		sqlCriteria = (criteri.getFlgAnomaliClasse()== null  || criteri.getFlgAnomaliClasse().equals("") ? sqlCriteria : sqlCriteria + " AND d.flgAnomaliaClasse ='" + criteri.getFlgAnomaliClasse() + "'");
		sqlCriteria = (criteri.getFlgPresPreDocfa()== null  || criteri.getFlgPresPreDocfa().equals("") ? sqlCriteria : sqlCriteria + " AND d.flgIciAnteDocfa ='" + criteri.getFlgPresPreDocfa() + "'");
		sqlCriteria = (criteri.getFlgPresPostDocfa()== null  || criteri.getFlgPresPostDocfa().equals("") ? sqlCriteria : sqlCriteria + " AND d.flgIciPostDocfa ='" + criteri.getFlgPresPostDocfa() + "'");
		sqlCriteria = (criteri.getFlgPresCatDataDocfa()== null  || criteri.getFlgPresCatDataDocfa().equals("") ? sqlCriteria : sqlCriteria + " AND d.flgUiuCatasto ='" + criteri.getFlgPresCatDataDocfa() + "'");
		sqlCriteria = (criteri.getFlgDocfaSopprNoCostit()== null  || criteri.getFlgDocfaSopprNoCostit().equals("") ? sqlCriteria : sqlCriteria + " AND d.flgDocfaSNoC ='" + criteri.getFlgDocfaSopprNoCostit() + "'");
		sqlCriteria = (criteri.getFlgVarCategoria()== null  || criteri.getFlgVarCategoria().equals("") ? sqlCriteria : sqlCriteria + " AND d.flgVarAnteCategoria ='" + criteri.getFlgVarCategoria() + "'");
		sqlCriteria = (criteri.getFlgVarClasse()== null  || criteri.getFlgVarClasse().equals("") ? sqlCriteria : sqlCriteria + " AND d.flgVarAnteClasse ='" + criteri.getFlgVarClasse() + "'");
		sqlCriteria = (criteri.getFlgVarRendita()== null  || criteri.getFlgVarRendita().equals("") ? sqlCriteria : sqlCriteria + " AND d.flgVarAnteRendita ='" + criteri.getFlgVarRendita() + "'");
		sqlCriteria = (criteri.getTipoAnomalia() == null  || criteri.getTipoAnomalia().equals("")? sqlCriteria : sqlCriteria + " AND d.codAnomalie LIKE '%" + criteri.getTipoAnomalia() + "%'");

		//Ricerca per parametri del soggetto titolare
		RicercaDocfaReportSoggDTO titolare = criteri.getTitolare();
		if(titolare != null){
			
			sqlCriteria = (titolare.getCognome() == null  || titolare.getCognome().equals("") ? sqlCriteria : sqlCriteria + " AND UPPER(s.ragiSoci) LIKE '" + titolare.getCognome().replace("'","''").toUpperCase() + "%'");
			sqlCriteria = (titolare.getNome() == null  || titolare.getNome().equals("") ? sqlCriteria : sqlCriteria + " AND UPPER(s.nome) LIKE '" + titolare.getNome().replace("'","''").toUpperCase() + "%'");
			sqlCriteria = (titolare.getParIva() == null  || titolare.getParIva().equals("") ? sqlCriteria : sqlCriteria + " AND UPPER(s.codiPiva) = '" + titolare.getParIva().toUpperCase() + "'");
			sqlCriteria = (titolare.getCodFis() == null  || titolare.getCodFis().equals("") ? sqlCriteria : sqlCriteria + " AND UPPER(s.codiFisc) = '" + titolare.getCodFis().toUpperCase() + "'");
			sqlCriteria = (titolare.getFlgTitolareDataDocfa() == null  || titolare.getFlgTitolareDataDocfa().equals("") ? sqlCriteria : sqlCriteria + " AND s.flgTitolareDataDocfa = '" + titolare.getFlgTitolareDataDocfa() + "'");
			sqlCriteria = (titolare.getFlgResidIndCatAllaData() == null  || titolare.getFlgResidIndCatAllaData().equals("") ? sqlCriteria : sqlCriteria + " AND s.flgResidIndCat3112 = '" + titolare.getFlgResidIndCatAllaData() + "'");
			sqlCriteria = (titolare.getFlgResidIndDocfaAllaData() == null  || titolare.getFlgResidIndDocfaAllaData().equals("") ? sqlCriteria : sqlCriteria + " AND s.flgResidIndDcf3112 = '" + titolare.getFlgResidIndDocfaAllaData() + "'");
			sqlCriteria = (titolare.getFlgAnomali() == null  || titolare.getFlgAnomali().equals("") ? sqlCriteria : sqlCriteria + " AND s.flgAnomalia = '" + titolare.getFlgAnomali() + "'");
			sqlCriteria = (titolare.getFlgPresUiuPreDocfa() == null  || titolare.getFlgPresUiuPreDocfa().equals("") ? sqlCriteria : sqlCriteria + " AND s.flgTitIciUiuAnte = '" + titolare.getFlgPresUiuPreDocfa() + "'");
			sqlCriteria = (titolare.getFlgPresUiuPostDocfa() == null  || titolare.getFlgPresUiuPostDocfa().equals("") ? sqlCriteria : sqlCriteria + " AND s.flgTitIciUiuPost = '" + titolare.getFlgPresUiuPostDocfa() + "'");
			sqlCriteria = (titolare.getFlgPresCivPreDocfa() == null  || titolare.getFlgPresCivPreDocfa().equals("") ? sqlCriteria : sqlCriteria + " AND s.flgTitIciCivAnte = '" + titolare.getFlgPresCivPreDocfa() + "'");
			sqlCriteria = (titolare.getFlgPresCivPostDocfa() == null  || titolare.getFlgPresCivPostDocfa().equals("") ? sqlCriteria : sqlCriteria + " AND s.flgTitIciCivPost = '" + titolare.getFlgPresCivPostDocfa() + "'");
			sqlCriteria = (titolare.getFlgPresUndPreDocfa() == null  || titolare.getFlgPresUndPreDocfa().equals("") ? sqlCriteria : sqlCriteria + " AND s.flgTitIciAnte = '" + titolare.getFlgPresUndPreDocfa() + "'");
			sqlCriteria = (titolare.getFlgPresUndPostDocfa() == null  || titolare.getFlgPresUndPostDocfa().equals("") ? sqlCriteria : sqlCriteria + " AND s.flgTitIciPost = '" + titolare.getFlgPresUndPostDocfa() + "'");

		}
		
		return sqlCriteria;
	}
	
	
}
