package it.webred.ct.diagnostics.service.data.dto;

import it.webred.ct.diagnostics.service.data.model.DiaTestata;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class DiaTestataDTO extends CeTBaseObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String dataRifShort;
	private String desSqlShort;
	private DiaTestata objTestata;
	
	//attributi per gestione paginazione
	private int start;
	private int numberRecord;
	
	
	public DiaTestataDTO(DiaTestata objTestata,String enteId, String userId){
		this.objTestata = objTestata;
		super.setEnteId(enteId);
		super.setUserId(userId);
	}
	
	
	public String getDataRifShort() {
		if (dataRifShort != null)
			return dataRifShort;
		if (this.objTestata.getDataRif() == null) {
			dataRifShort = "";
		}
		else {
			dataRifShort = this.objTestata.getDataRif().trim();
			if (dataRifShort.indexOf("Fonte") > -1) {
				StringBuffer sb = new StringBuffer();
				String[] fonti = dataRifShort.split("Fonte");
				for (String fonte : fonti) {				
					fonte = fonte.trim();					
					if (fonte.equals("")) {
						continue;
					}
					fonte = "Fonte" + fonte;
					if (!sb.toString().equals("")) {
						fonte = "<br />" + fonte;
					}
					fonte = fonte.replace("data rifer. a", "a");
					fonte = fonte.replace("da:", "da");
					fonte = fonte.replace("a:", "a");
					//fonte = fonte.replace(" data agg.:", ", data agg.");
					//tolgo data agg.
					if (fonte.indexOf("data agg.") > -1) {
						fonte = fonte.substring(0, fonte.indexOf("data agg.")).trim();
					}					
					//////////////////////////////////////
					int idxDa = -1;
					int idxA = fonte.indexOf("]") + 1;
					sb.append(fonte.substring(0, idxA).trim());
					fonte = fonte.substring(idxA).trim();
					while ((idxDa = fonte.indexOf("[")) > -1 && (idxA = fonte.indexOf("]")) > -1) {
						if (!fonte.startsWith(",")) {
							sb.append(" ");
						}						
						sb.append(fonte.substring(0, idxDa).trim());
						sb.append(" ");
						String dataOra = fonte.substring(idxDa + 1, idxA);
						try {
							String data = new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataOra));
							sb.append(data);
						} catch (Exception e) {
							sb.append(dataOra);
						}
						fonte = fonte.substring(idxA + 1).trim();
					}					
				}
				dataRifShort = sb.toString();
			}			
		}
		
		return dataRifShort;
	}


	public void setDataRifShort(String dataRifShort) {
		this.dataRifShort = dataRifShort;
	}


	public String getDesSqlShort() {
		if (this.objTestata.getDesSql() == null) {
			desSqlShort = "";
		}
		else if(this.objTestata.getDesSql().length() > 50) {
			desSqlShort = this.objTestata.getDesSql().substring(0, 50) + "..."; 
		}
		else {
			desSqlShort = this.objTestata.getDesSql();
		}
		
		return desSqlShort;
	}

	public void setDesSqlShort(String desSqlShort) {
		this.desSqlShort = desSqlShort;
	}
	

	public DiaTestata getObjTestata() {
		return objTestata;
	}


	public void setObjTestata(DiaTestata objTestata) {
		this.objTestata = objTestata;
	}


	public int getStart() {
		return start;
	}


	public void setStart(int start) {
		this.start = start;
	}


	public int getNumberRecord() {
		return numberRecord;
	}


	public void setNumberRecord(int numberRecord) {
		this.numberRecord = numberRecord;
	}

		
}
