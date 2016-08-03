package it.webred.indice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DatiFonte extends Fonte implements Serializable {
	
	
	private List<OggettoIndice> oggettoIndiceList;
	private long count;
	private boolean isTree;
	
	private String sortTypes;
	
	public List<OggettoIndice> getOggettoIndiceList() {
		return oggettoIndiceList;
	}
	public void setOggettoIndiceList(List<OggettoIndice> oggettoIndiceList) {
		this.oggettoIndiceList = oggettoIndiceList;
	}
	
	public long getCount() {
		int c=0;
		
		if (!this.isTree){
			if (oggettoIndiceList != null)
			  return oggettoIndiceList.size();
		}
		else
		{
			for (int x=0; x < oggettoIndiceList.size(); x++) {
				OggettoIndice oi = oggettoIndiceList.get(x);
				List<OggettoIndice> subList= oi.getSubList();
				if (subList!= null)
					c= c+ subList.size();
		}
			return c;
		}
		return 0;
	}
	public boolean isTree() {
		return isTree;
	}
	public void setTree(boolean isTree) {
		this.isTree = isTree;
	}
	
	//N.B. i  prime due tipi si riferiscono alle colonne FONTE e DESCRIZIONE che sono FISSE
	
public List<String> getListaNomiAttributiSoggetti() {
		
		List<String> listaNomiAttributi= new ArrayList<String>();
		
					
			if (this.getId() != null && this.getId().equals("1")){
				listaNomiAttributi.add("COD.FISC / P.I.");
				listaNomiAttributi.add("DATA NASCITA");
				listaNomiAttributi.add("POSIZIONE ANAGRAFICA");
				listaNomiAttributi.add("COMUNE NASCITA");
				listaNomiAttributi.add("PROVINCIA NASCITA");
				listaNomiAttributi.add("DATA INIZIO VALIDITA'");
				listaNomiAttributi.add("DATA FINE VALIDITA'");
				
				
				this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"Date\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\"]");
			}
			else if (this.getId() != null && this.getId().equals("2")){
				listaNomiAttributi.add("COD.FISC / P.I.");
				listaNomiAttributi.add("DATA NASCITA");
				listaNomiAttributi.add("INDIRIZZO");
				listaNomiAttributi.add("NUM. CIV.");
				listaNomiAttributi.add("COMUNE RESIDENZA");
				listaNomiAttributi.add("PROVINCIA");
				
				this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"Date\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\"]");
			}
			else if (this.getId() != null && this.getId().equals("10")){
				listaNomiAttributi.add("COD.FISC / P.I.");
				listaNomiAttributi.add("DATA NASCITA");
				listaNomiAttributi.add("COMUNE");
				listaNomiAttributi.add("PROVINCIA");
				
				this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"Date\", \"CaseInsensitiveString\", \"CaseInsensitiveString\"]");
			}
			else if (this.getId() != null && this.getId().equals("12")){
				listaNomiAttributi.add("COD.FISC / P.I.");
				listaNomiAttributi.add("DATA NASCITA");
				listaNomiAttributi.add("ANNO");
				listaNomiAttributi.add("IDENTIFICATIVO UTENZA");
				listaNomiAttributi.add("INDIRIZZO");
				listaNomiAttributi.add("SPESA CONSUMO NETTO");
				listaNomiAttributi.add("N MESI FATT.");
				
				this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"Date\", \"Number\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"Number\", \"Number\"]");
			}
			else if (this.getId() != null && this.getId().equals("4")){
				listaNomiAttributi.add("COD.FISC / P.I.");
				listaNomiAttributi.add("DATA NASCITA");
				listaNomiAttributi.add("COMUNE");
				
				this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"Date\", \"CaseInsensitiveString\"]");
			}
			else if (this.getId() != null && this.getId().equals("5")){
				listaNomiAttributi.add("COD.FISC / P.I.");
				listaNomiAttributi.add("DATA NASCITA");
				listaNomiAttributi.add("INDIRIZZO");
				listaNomiAttributi.add("CIVICO");
				listaNomiAttributi.add("COMUNE");
				listaNomiAttributi.add("PROVINCIA");
				listaNomiAttributi.add("UFFICIO");
				listaNomiAttributi.add("ANNO");
				listaNomiAttributi.add("SERIE");
				listaNomiAttributi.add("NUMERO");
				listaNomiAttributi.add("PROGRESSIVO");
				
				this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"Date\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\",\"Number\", \"CaseInsensitiveString\",\"Number\",\"Number\"]");
			}
			else if (this.getId() != null && this.getId().equals("3")){
				listaNomiAttributi.add("COD.FISC / P.I.");
				listaNomiAttributi.add("DATA NASCITA");
				listaNomiAttributi.add("INDIRIZZO");
				listaNomiAttributi.add("COMUNE");
				listaNomiAttributi.add("PROVINCIA");
				
				this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"Date\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\"]");
			}
			else if (this.getId() != null && this.getId().equals("6")){
				listaNomiAttributi.add("COD.FISC / P.I.");
				listaNomiAttributi.add("DATA NASCITA");
				listaNomiAttributi.add("INDIRIZZO");
				listaNomiAttributi.add("COMUNE");
				listaNomiAttributi.add("PROVINCIA");
				listaNomiAttributi.add("UFFICIO");
				listaNomiAttributi.add("ANNO");
				listaNomiAttributi.add("VOLUME");
				listaNomiAttributi.add("NUMERO");
				listaNomiAttributi.add("SOTTONUMERO");
				listaNomiAttributi.add("COMUNE");
				listaNomiAttributi.add("PROGRESSIVO");
				
				this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"Date\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\",\"Number\", \"Number\",\"Number\",\"Number\", \"CaseInsensitiveString\",\"Number\"]");

			}
			else if (this.getId() != null && this.getId().equals("13")){
				listaNomiAttributi.add("COD.FISC / P.I.");
				listaNomiAttributi.add("DATA NASCITA");
				listaNomiAttributi.add("INDIRIZZO");
				listaNomiAttributi.add("CIVICO");
				listaNomiAttributi.add("COMUNE");
				listaNomiAttributi.add("PROVINCIA");
				
				this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"Date\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\"]");
			}
			else if (this.getId() != null && this.getId().equals("11")){
				listaNomiAttributi.add("COD.FISC / P.I.");
				listaNomiAttributi.add("DATA NASCITA");
				listaNomiAttributi.add("ANNO");
				listaNomiAttributi.add("TIPO MODELLO");
				
				this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"Date\", \"Number\", \"CaseInsensitiveString\"]");
			}
			else if (this.getId() != null && this.getId().equals("9") ){
				listaNomiAttributi.add("COD.FISC / P.I.");
				listaNomiAttributi.add("DATA NASCITA");
				listaNomiAttributi.add("INDIRIZZO");
				listaNomiAttributi.add("CIVICO");
				listaNomiAttributi.add("COMUNE");
				listaNomiAttributi.add("PROVINCIA");
				
				this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"Date\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\"]");
			}
			else if (this.getId() != null && this.getId().equals("7")){
				listaNomiAttributi.add("COD.FISC / P.I.");
				listaNomiAttributi.add("DATA NASCITA");
				
				this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"Date\"]");
			}
			else if (this.getId() != null && this.getId().equals("14")){
				listaNomiAttributi.add("COD.FISC / P.I.");
				listaNomiAttributi.add("DATA NASCITA");
				
				this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"Date\"]");
			}
			else if (this.getId() != null && this.getId().equals("17")){
				listaNomiAttributi.add("COD.FISC / P.I.");
				listaNomiAttributi.add("DATA NASCITA");
				
				this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"Date\"]");
			}
			else if (this.getId() != null && this.getId().equals("18")){
				listaNomiAttributi.add("COD.FISC / P.I.");
				listaNomiAttributi.add("DATA NASCITA");
				
				this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"Date\"]");
			}
			else if (this.getId() != null && this.getId().equals("28")){
				listaNomiAttributi.add("COD.FISC / P.I.");
				listaNomiAttributi.add("DATA NASCITA");
				
				this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"Date\"]");
			}
			else if (this.getId() != null && this.getId().equals("30")){
				listaNomiAttributi.add("COD.FISC / P.I.");
				listaNomiAttributi.add("DATA NASCITA");
				
				this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"Date\"]");
			}
			else if (this.getId() != null && this.getId().equals("33")){
				listaNomiAttributi.add("ANNO RIF.");   //Field11
				listaNomiAttributi.add("COD.TRIBUTO"); //Field10
				listaNomiAttributi.add("TIPO IMPOSTA");//Field12
				listaNomiAttributi.add("DATA BONIFICO");//Field5
				listaNomiAttributi.add("PROG.DELEGA");//Field6
				listaNomiAttributi.add("PROG.RIGA");//Field7
				listaNomiAttributi.add("TIPO SOGGETTO");//Field15 + Field19
				listaNomiAttributi.add("FORNITURA");//Field1 + Field2
				listaNomiAttributi.add("RIPARTIZIONE");//Field3 + Field4
				
				this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\",\"CaseInsensitiveString\",\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\",\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"Date\"]");
			}
			else if (this.getId() != null && this.getId().equals("35")){
				
				listaNomiAttributi.add("Num.Prot.Gen.");
				listaNomiAttributi.add("Anno Prot.Gen.");
				listaNomiAttributi.add("Num.Prot.Sett.");
				listaNomiAttributi.add("Tipo Atto");
				listaNomiAttributi.add("Num.Atto");
				listaNomiAttributi.add("Data Documento");
				
				this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\",\"CaseInsensitiveString\",\"CaseInsensitiveString\",\"CaseInsensitiveString\",\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"Date\"]");
			}
			else if (this.getId() != null && this.getId().equals("37")){
				listaNomiAttributi.add("Cod.Fisc/P.IVA");
				listaNomiAttributi.add("Data Versamento");
				listaNomiAttributi.add("Importo");
				
				this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\"]");
			}
			else if (this.getId() != null && (this.getId().equals("39") || this.getId().equals("40"))){
				listaNomiAttributi.add("Cod.Fisc/P.IVA");
				listaNomiAttributi.add("Anno");
				listaNomiAttributi.add("Tipo");
				
				this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\"]");
			}
			else if (this.getId() != null && this.getId().equals("45")){	//Soggetti
				listaNomiAttributi.add("Data Prot");
				listaNomiAttributi.add("Identificativo Pratica");
				
				this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\"]");
			}
			
		return listaNomiAttributi;
	}//-------------------------------------------------------------------------

	public List<String> getListaNomiAttributiOggetti() {
	
	List<String> listaNomiAttributi= new ArrayList<String>();
		
		if (this.getId() != null && this.getId().equals("2")){
			listaNomiAttributi.add("FOGLIO");
			listaNomiAttributi.add("PARTICELLA");
			listaNomiAttributi.add("SUB");
			listaNomiAttributi.add("ANNO");
			listaNomiAttributi.add("NUM. DENUNCIA");
			listaNomiAttributi.add("INDIRIZZO");
			listaNomiAttributi.add("NUM. CIVICO");
			listaNomiAttributi.add("POSS. 31/12");
			listaNomiAttributi.add("ABIT. PRINC. 31/12");
			listaNomiAttributi.add("ACQ.");
			listaNomiAttributi.add("CSS.");
			listaNomiAttributi.add("CLASSE");
			listaNomiAttributi.add("TIPO OGGETTO");
			
			this.setSortTypes("[\"CaseInsensitiveString\",\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"Number\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\"]");
			
			
		}
		else if (this.getId() != null && this.getId().equals("3")){
			listaNomiAttributi.add("FOGLIO");
			listaNomiAttributi.add("PARTICELLA");
			listaNomiAttributi.add("SUB");
			listaNomiAttributi.add("TIPO");
			
			this.setSortTypes("[\"CaseInsensitiveString\",\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\",\"CaseInsensitiveString\"]");
		}
		else if (this.getId() != null && this.getId().equals("4")){
			listaNomiAttributi.add("FOGLIO");
			listaNomiAttributi.add("PARTICELLA");
			listaNomiAttributi.add("SUB");
			
			this.setSortTypes("[\"CaseInsensitiveString\",\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\"]");
			
		}
		else if (this.getId() != null && this.getId().equals("5")){
			listaNomiAttributi.add("FOGLIO");
			listaNomiAttributi.add("PARTICELLA");
			listaNomiAttributi.add("SUB");
			listaNomiAttributi.add("UFFICIO");
			listaNomiAttributi.add("ANNO");
			listaNomiAttributi.add("SERIE");
			listaNomiAttributi.add("NUMERO");
			listaNomiAttributi.add("PROGRESSIVO");
			listaNomiAttributi.add("DATA FINE");
			listaNomiAttributi.add("DATA STIPULA");
			listaNomiAttributi.add("IMPORTO CANONE");
			listaNomiAttributi.add("OGGETTO LOCAZ.");
			listaNomiAttributi.add("INDIRIZZO");
			
			this.setSortTypes("[\"CaseInsensitiveString\",\"CaseInsensitiveString\",\"CaseInsensitiveString\",\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"Number\", \"CaseInsensitiveString\", \"Date\", \"Date\",\"Number\", \"CaseInsensitiveString\",\"CaseInsensitiveString\",\"CaseInsensitiveString\"]");
		}
		else if (this.getId() != null && this.getId().equals("6")){
			listaNomiAttributi.add("FOGLIO");
			listaNomiAttributi.add("PARTICELLA");
			listaNomiAttributi.add("SUB");
			listaNomiAttributi.add("NATURA");
			listaNomiAttributi.add("INDIRIZZO");
			
			this.setSortTypes("[\"CaseInsensitiveString\",\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\",\"CaseInsensitiveString\",\"CaseInsensitiveString\"]");
		}
		else if (this.getId() != null && this.getId().equals("7")){
			listaNomiAttributi.add("FOGLIO");
			listaNomiAttributi.add("PARTICELLA");
			listaNomiAttributi.add("SUB");
			listaNomiAttributi.add("INDIRIZZO");
			listaNomiAttributi.add("NUM. CIVICO");
			
			this.setSortTypes("[\"CaseInsensitiveString\",\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\",\"CaseInsensitiveString\",\"CaseInsensitiveString\"]");
		}
		else if (this.getId() != null && this.getId().equals("9")){
			listaNomiAttributi.add("FOGLIO");
			listaNomiAttributi.add("PARTICELLA");
			listaNomiAttributi.add("SUB");
			listaNomiAttributi.add("INDIRIZZO");
			listaNomiAttributi.add("NUM. CIVICO");
			
			this.setSortTypes("[\"CaseInsensitiveString\",\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\",\"CaseInsensitiveString\",\"CaseInsensitiveString\"]");
		}
		else if (this.getId() != null && this.getId().equals("10")){
			listaNomiAttributi.add("FOGLIO");
			listaNomiAttributi.add("PARTICELLA");
			listaNomiAttributi.add("SUB");
			listaNomiAttributi.add("ANNO");
			listaNomiAttributi.add("TIPO UTENZA");
			listaNomiAttributi.add("INDIRIZZO");
			listaNomiAttributi.add("MESI FATT.");
			listaNomiAttributi.add("KWH FATT.");
			listaNomiAttributi.add("SPESA");
		
		this.setSortTypes("[\"CaseInsensitiveString\",\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\",\"Number\", \"CaseInsensitiveString\", \"CaseInsensitiveString\",\"Number\",\"Number\",\"Number\"]");
		
		}
		else if (this.getId() != null && this.getId().equals("30")){
			listaNomiAttributi.add("FOGLIO");
			listaNomiAttributi.add("PARTICELLA");
			listaNomiAttributi.add("SUB");
			
			this.setSortTypes("[\"CaseInsensitiveString\",\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\"]");
		}
		else if (this.getId() != null && this.getId().equals("40")){
			listaNomiAttributi.add("Foglio");
			listaNomiAttributi.add("Particella");
			listaNomiAttributi.add("Sub");
			listaNomiAttributi.add("Anno");
			listaNomiAttributi.add("Tipo");
			listaNomiAttributi.add("Contribuente");
			listaNomiAttributi.add("Cod.Fisc/P.IVA");
			
			this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\"]");
		}
		else if (this.getId() != null && this.getId().equals("45")){	//Oggetti
			listaNomiAttributi.add("Sub");
			listaNomiAttributi.add("Data Prot");
			listaNomiAttributi.add("Identificativo Pratica");
			
			this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\"]");
		}
		
	return listaNomiAttributi;
	
	}//-------------------------------------------------------------------------


	public List<String> getListaNomiAttributiVieCivici() {
	
	List<String> listaNomiAttributi= new ArrayList<String>();
	
		if (this.getId() != null && this.getId().equals("3")){
			listaNomiAttributi.add("NUM. CONCESSIONE");
			listaNomiAttributi.add("DATA PROTOCOLLO");
			listaNomiAttributi.add("TIPO INTERVENTO");
			listaNomiAttributi.add("OGGETTO");
			listaNomiAttributi.add("DATA RILASCIO");
			
			this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\",\"CaseInsensitiveString\",\"Date\",\"CaseInsensitiveString\", \"CaseInsensitiveString\",\"Date\"]");
		}
	
		if (this.getId() != null && this.getId().equals("4")){
			listaNomiAttributi.add("FOGLIO");
			listaNomiAttributi.add("PARTICELLA");
			listaNomiAttributi.add("SUB");
			listaNomiAttributi.add("GRAFFATO");
			
			this.setSortTypes("[\"CaseInsensitiveString\",\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\",\"CaseInsensitiveString\",\"CaseInsensitiveString\"]");
		}
		else if (this.getId() != null && this.getId().equals("5")){
			listaNomiAttributi.add("UFFICIO");
			listaNomiAttributi.add("ANNO");
			listaNomiAttributi.add("SERIE");
			listaNomiAttributi.add("NUMERO");
			listaNomiAttributi.add("PROGRESSIVO");
			listaNomiAttributi.add("DATA FINE");
			listaNomiAttributi.add("DATA STIPULA");
			listaNomiAttributi.add("IMPORTO CANONE");
			listaNomiAttributi.add("OGGETTO LOCAZ.");
			listaNomiAttributi.add("INDIRIZZO");
			
			this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"Number\", \"CaseInsensitiveString\", \"Number\", \"Number\", \"Date\", \"Date\",\"Number\", \"CaseInsensitiveString\",\"CaseInsensitiveString\"]");
		}
		else if (this.getId() != null && this.getId().equals("6")){
			listaNomiAttributi.add("FOGLIO");
			listaNomiAttributi.add("PARTICELLA");
			listaNomiAttributi.add("SUB");
			listaNomiAttributi.add("NATURA");
			listaNomiAttributi.add("UFFICIO");
			listaNomiAttributi.add("ANNO");
			listaNomiAttributi.add("VOLUME");
			listaNomiAttributi.add("NUMERO");
			listaNomiAttributi.add("SOTTONUMERO");
			listaNomiAttributi.add("COMUNE");
			listaNomiAttributi.add("PROGRESSIVO");
			
			this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"Number\", \"Number\",\"Number\", \"Number\",\"CaseInsensitiveString\", \"Number\"]");
		}
		else if (this.getId() != null && this.getId().equals("7")){
			listaNomiAttributi.add("FOGLIO");
			listaNomiAttributi.add("PARTICELLA");
			listaNomiAttributi.add("SUB");
			listaNomiAttributi.add("NUMERO NOTA");
			listaNomiAttributi.add("ANNO");
			listaNomiAttributi.add("NOMINATIVO ROG.");
			listaNomiAttributi.add("CATEGORIA");
			listaNomiAttributi.add("CLASSE");
			listaNomiAttributi.add("RENDITA");
			listaNomiAttributi.add("COMUNE");
			listaNomiAttributi.add("PROVINCIA");
			listaNomiAttributi.add("CAP");
			
			this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"Number\", \"Number\", \"Number\", \"CaseInsensitiveString\", \"Number\", \"CaseInsensitiveString\", \"CaseInsensitiveString\",\"CaseInsensitiveString\", \"Number\",\"CaseInsensitiveString\", \"CaseInsensitiveString\",\"Number\"]");
		}
		else if (this.getId() != null && this.getId().equals("9")){
			listaNomiAttributi.add("FOGLIO");
			listaNomiAttributi.add("PARTICELLA");
			listaNomiAttributi.add("SUB");
			listaNomiAttributi.add("PROTOCOLLO");
			listaNomiAttributi.add("FORNITURA");
			
			this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"Date\"]");
		}
		else if (this.getId() != null && this.getId().equals("10")){
			listaNomiAttributi.add("DENOMINAZIONE");
			listaNomiAttributi.add("COD. FISC./P.I.");
			listaNomiAttributi.add("ANNO");
			
			this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"Number\"]");
		}
		else if (this.getId() != null && this.getId().equals("11")){
			listaNomiAttributi.add("COD.FISC / P.I.");
			listaNomiAttributi.add("COGNOME");
			listaNomiAttributi.add("NOME");
			listaNomiAttributi.add("DENOMINAZIONE");
			listaNomiAttributi.add("COMUNE NASCITA");
			listaNomiAttributi.add("DATA NASCITA");
			listaNomiAttributi.add("ANNO");
			listaNomiAttributi.add("TIPO MODELLO");
			
			this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\",\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\",  \"CaseInsensitiveString\",\"Date\", \"Number\", \"CaseInsensitiveString\"]");
		}
		else if (this.getId() != null && this.getId().equals("12")){
			listaNomiAttributi.add("COGNOME");
			listaNomiAttributi.add("NOME");
			listaNomiAttributi.add("RAGIONE SOCIALE");
			listaNomiAttributi.add("INDIRIZZO UTENZA");
			listaNomiAttributi.add("IDENTIFICATIVO UTENZA");
			listaNomiAttributi.add("ANNO");
			
			this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"Number\"]");
		}
		else if (this.getId() != null && this.getId().equals("13")){
			listaNomiAttributi.add("NUMERO");
			listaNomiAttributi.add("RAGIONE SOCIALE");
			listaNomiAttributi.add("TIPOLOGIA");
			listaNomiAttributi.add("CIVICO");
			listaNomiAttributi.add("DATA VALIDITA'");
			listaNomiAttributi.add("DATA RILASCIO");
			
			this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"Date\", \"Date\"]");
		}
		else if (this.getId() != null && this.getId().equals("14")){
			listaNomiAttributi.add("ANNO DOCUMENTO");
			listaNomiAttributi.add("TIPO OCCUPAZIONE");
			listaNomiAttributi.add("IMPORTO CANONE");
			
			this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"Number\", \"CaseInsensitiveString\", \"Number\"]");
		}else if (this.getId() != null && this.getId().equals("17")){
			listaNomiAttributi.add("Data");
			listaNomiAttributi.add("Targa");
			listaNomiAttributi.add("Marca");
			listaNomiAttributi.add("Modello");
			this.setSortTypes("[\"CaseInsensitiveString\",\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\",\"CaseInsensitiveString\",\"CaseInsensitiveString\"]");
		}
		else if (this.getId() != null && this.getId().equals("27")){
			listaNomiAttributi.add("Tipo Pratica");
			listaNomiAttributi.add("Num. Pratica");
			listaNomiAttributi.add("Anno");
			listaNomiAttributi.add("Classe");
			listaNomiAttributi.add("Oggetto");
			listaNomiAttributi.add("Data Inizio");
			listaNomiAttributi.add("Data Fine");
			listaNomiAttributi.add("Indirizzo");
			
			this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\",  \"Number\",  \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"Date\", \"Date\", \"CaseInsensitiveString\"]");
		}
		else if (this.getId() != null && this.getId().equals("29")){
			listaNomiAttributi.add("Indirizzo");
			listaNomiAttributi.add("Foglio");
			listaNomiAttributi.add("Particella");
			
			this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"Number\", \"CaseInsensitiveString\", \"Number\"]");
		}
		else if (this.getId() != null && this.getId().equals("30")){
			listaNomiAttributi.add("Denominazione");
			
			this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\"]");
		}
		else if (this.getId() != null && this.getId().equals("35")){
			
			listaNomiAttributi.add("Num.Prot.Gen.");
			listaNomiAttributi.add("Anno Prot.Gen.");
			listaNomiAttributi.add("Num.Prot.Sett.");
			listaNomiAttributi.add("Tipo Atto");
			listaNomiAttributi.add("Num.Atto");
			listaNomiAttributi.add("Data Documento");
			
			this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\",\"CaseInsensitiveString\",\"CaseInsensitiveString\",\"CaseInsensitiveString\",\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"Date\"]");
		}	
		else if(this.getId() != null && (this.getId().equals("39")||this.getId().equals("40"))){
			listaNomiAttributi.add("Anno");
			listaNomiAttributi.add("Tipo");
			listaNomiAttributi.add("Contribuente");
			listaNomiAttributi.add("Cod.Fisc/P.IVA");
			
			this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\"]");

		}
		else if(this.getId() != null && (this.getId().equals("42"))){
			listaNomiAttributi.add("Comune");
			listaNomiAttributi.add("Prov.");
			listaNomiAttributi.add("Principale");
			
			this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\"]");

		}
		else if (this.getId() != null && this.getId().equals("45")){	//via e civici
			listaNomiAttributi.add("Data Prot");
			listaNomiAttributi.add("Identificativo Pratica");
			
			this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\"]");
		}
		
	
		return listaNomiAttributi;
	}//-------------------------------------------------------------------------


	public List<String> getListaNomiAttributiFabbricati() {
	
	List<String> listaNomiAttributi= new ArrayList<String>();
	
	
		
		if (this.getId() != null && this.getId().equals("2")){
			listaNomiAttributi.add("SEZIONE");
			listaNomiAttributi.add("FOGLIO");
			listaNomiAttributi.add("PARTICELLA");
			listaNomiAttributi.add("SUBALTERNO");
			listaNomiAttributi.add("ANNO");
			listaNomiAttributi.add("NUM. DENUNCIA");
			listaNomiAttributi.add("INDIRIZZO");
			listaNomiAttributi.add("NUM. CIVICO");
			listaNomiAttributi.add("POSS. 31/12");
			listaNomiAttributi.add("ABIT. PRINC. 31/12");
			listaNomiAttributi.add("ACQ.");
			listaNomiAttributi.add("CSS.");
			listaNomiAttributi.add("CLASSE");
			listaNomiAttributi.add("TIPO OGGETTO");
			listaNomiAttributi.add("DATA INIZIO");
			listaNomiAttributi.add("DATA FINE");
			
			this.setSortTypes("[\"CaseInsensitiveString\",\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"Number\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\"]");
		}
		else if (this.getId() != null && this.getId().equals("3")){
			listaNomiAttributi.add("SEZIONE");
			listaNomiAttributi.add("FOGLIO");
			listaNomiAttributi.add("PARTICELLA");	
			listaNomiAttributi.add("SUBALTERNO");
			
			this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\",\"CaseInsensitiveString\"]");
		}
		else if (this.getId() != null && this.getId().equals("4")){
			listaNomiAttributi.add("SEZIONE");
			listaNomiAttributi.add("FOGLIO");
			listaNomiAttributi.add("PARTICELLA");
			listaNomiAttributi.add("SUBALTERNO");
			listaNomiAttributi.add("DATA INIZIO");
			listaNomiAttributi.add("DATA FINE");
			
			this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\",\"CaseInsensitiveString\"]");
		}
		else if (this.getId() != null && this.getId().equals("5")){
			listaNomiAttributi.add("SEZ.");
			listaNomiAttributi.add("FOGLIO");
			listaNomiAttributi.add("PARTICELLA");
			listaNomiAttributi.add("SUB");
			listaNomiAttributi.add("UFFICIO");
			listaNomiAttributi.add("ANNO");
			listaNomiAttributi.add("SERIE");
			listaNomiAttributi.add("NUMERO");
			listaNomiAttributi.add("PROGRESSIVO");
			listaNomiAttributi.add("DATA FINE");
			listaNomiAttributi.add("DATA STIPULA");
			listaNomiAttributi.add("IMPORTO CANONE");
			listaNomiAttributi.add("OGGETTO LOCAZ.");
			//listaNomiAttributi.add("INDIRIZZO");
			
			this.setSortTypes("[\"CaseInsensitiveString\",\"CaseInsensitiveString\",\"CaseInsensitiveString\",\"CaseInsensitiveString\",\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"Number\", \"CaseInsensitiveString\", \"Date\", \"Date\",\"Number\", \"CaseInsensitiveString\",\"CaseInsensitiveString\"]");
		}
		else if (this.getId() != null && this.getId().equals("6")){
			listaNomiAttributi.add("SEZIONE");
			listaNomiAttributi.add("FOGLIO");
			listaNomiAttributi.add("PARTICELLA");
			listaNomiAttributi.add("SUBALTERNO");
			listaNomiAttributi.add("NATURA");
			listaNomiAttributi.add("INDIRIZZO");
		
			this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\",\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\",\"CaseInsensitiveString\"]");
		}
		else if (this.getId() != null && this.getId().equals("7")){
			listaNomiAttributi.add("SEZIONE");
			listaNomiAttributi.add("FOGLIO");
			listaNomiAttributi.add("PARTICELLA");
			listaNomiAttributi.add("SUBALTERNO");
			listaNomiAttributi.add("INDIRIZZO");
			listaNomiAttributi.add("NUM. CIVICO");
			
			this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\",\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\",\"CaseInsensitiveString\"]");
		}
		else if (this.getId() != null && this.getId().equals("9")){
			listaNomiAttributi.add("SEZIONE");
			listaNomiAttributi.add("FOGLIO");
			listaNomiAttributi.add("PARTICELLA");
			listaNomiAttributi.add("SUBALTERNO");
			listaNomiAttributi.add("INDIRIZZO");
			listaNomiAttributi.add("NUM. CIVICO");
			
			this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\",\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\",\"CaseInsensitiveString\"]");
		}
		else if (this.getId() != null && this.getId().equals("10")){
			listaNomiAttributi.add("SEZIONE");
			listaNomiAttributi.add("FOGLIO");
			listaNomiAttributi.add("PARTICELLA");
			listaNomiAttributi.add("SUBALTERNO");
			listaNomiAttributi.add("ANNO");
			listaNomiAttributi.add("TIPO UTENZA");
			listaNomiAttributi.add("INDIRIZZO");
			listaNomiAttributi.add("MESI FATT.");
			listaNomiAttributi.add("KWH FATT.");
			listaNomiAttributi.add("SPESA");
		
			
			this.setSortTypes("[\"CaseInsensitiveString\",\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\",\"Number\", \"CaseInsensitiveString\", \"CaseInsensitiveString\",\"Number\",\"Number\",\"Number\"]");
		}
		else if (this.getId() != null && this.getId().equals("14")){
			listaNomiAttributi.add("ANNO DOCUMENTO");
			listaNomiAttributi.add("TIPO OCCUPAZIONE");
			listaNomiAttributi.add("IMPORTO CANONE");
			
			
			this.setSortTypes("[\"CaseInsensitiveString\",\"Number\", \"CaseInsensitiveString\", \"Number\"]");
		}
		else if (this.getId() != null && this.getId().equals("30")){
			listaNomiAttributi.add("SEZIONE");
			listaNomiAttributi.add("FOGLIO");
			listaNomiAttributi.add("PARTICELLA");
			listaNomiAttributi.add("SUBALTERNO");
			
			
			this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\",\"CaseInsensitiveString\"]");
		}
		else if (this.getId() != null && this.getId().equals("40")){
			listaNomiAttributi.add("Sez.");
			listaNomiAttributi.add("Foglio");
			listaNomiAttributi.add("Particella");
			listaNomiAttributi.add("Sub");
			listaNomiAttributi.add("Anno");
			listaNomiAttributi.add("Tipo");
			listaNomiAttributi.add("Contribuente");
			listaNomiAttributi.add("Cod.Fisc/P.IVA");
			
			this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\"]");
		}
		else if (this.getId() != null && this.getId().equals("42")){
			listaNomiAttributi.add("Sez.");
			listaNomiAttributi.add("Foglio");
			listaNomiAttributi.add("Particella");
			
			this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\"]");
		}
		else if (this.getId() != null && this.getId().equals("45")){	//Fabbricati
			listaNomiAttributi.add("Sub");
			listaNomiAttributi.add("Data Prot");
			listaNomiAttributi.add("Identificativo Pratica");
			
			this.setSortTypes("[\"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\", \"CaseInsensitiveString\"]");
		}
		
	return listaNomiAttributi;
	
	}//-------------------------------------------------------------------------


	public String getSortTypes() {
		return sortTypes;
	}
	public void setSortTypes(String sortTypes) {
		this.sortTypes = sortTypes;
	}

	

}
