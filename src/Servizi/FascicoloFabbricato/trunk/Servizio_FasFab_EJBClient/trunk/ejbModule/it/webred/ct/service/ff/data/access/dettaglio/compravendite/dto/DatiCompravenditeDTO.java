package it.webred.ct.service.ff.data.access.dettaglio.compravendite.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.webred.ct.data.access.basic.compravendite.dto.SoggettoCompravenditeDTO;
import it.webred.ct.data.model.compravendite.MuiNotaTras;

public class DatiCompravenditeDTO extends MuiNotaTras implements Serializable{
	
	public DatiCompravenditeDTO(MuiNotaTras nota){
		this.setIid(nota.getIid());
		this.setIdNota(nota.getIdNota());
		this.setIidFornitura(nota.getIidFornitura());
		this.setDataValiditaAttoDate(nota.getDataValiditaAttoDate());
		this.setNumeroRepertorio(nota.getNumeroRepertorio());
		this.setCognomeNomeRog(nota.getCognomeNomeRog());
		this.setCodFiscRog(nota.getCodFiscRog());
		
		
	}
	
	private String elencoUI;
	
	private List<String> listaSoggettiString;
	private List<SoggettoCompravenditeDTO> listaSoggetti;
	private String stringListaSoggetti;

	public List<SoggettoCompravenditeDTO> getListaSoggetti() {
		return listaSoggetti;
	}

	public void setListaSoggetti(List<SoggettoCompravenditeDTO> listaSoggetti) {
		this.listaSoggetti = listaSoggetti;
	}

	public List<String> getListaSoggettiString() {
		
		List<String> listaSoggettiString=new ArrayList<String>();
		if (listaSoggetti != null){
			for (SoggettoCompravenditeDTO sogg: listaSoggetti){
				String soggString="";
				if (sogg.getFlagTipoTitolContro()!= null && sogg.getFlagTipoTitolContro().equals("C"))
					soggString="CONTRO: ";
				else if (sogg.getFlagTipoTitolFavore()!= null && sogg.getFlagTipoTitolFavore().equals("F"))
					soggString="A FAVORE: ";
				
				if (sogg.getCognome()!= null && !sogg.getCognome().equals(""))
					soggString = soggString + sogg.getCognome()+" "+ sogg.getNome() + " - ";
				else if (sogg.getDenominazione()!= null && !sogg.getDenominazione().equals(""))
					soggString = soggString + sogg.getDenominazione() + " - ";
				
				if (sogg.getCodiceFiscale()!= null &&  !sogg.getCodiceFiscale().equals(""))
					soggString= soggString + sogg.getCodiceFiscale();
				else if (sogg.getCodiceFiscaleG()!= null &&  !sogg.getCodiceFiscaleG().equals(""))
					soggString= soggString + sogg.getCodiceFiscaleG();
				
				listaSoggettiString.add(soggString);
			}
		}
		
		return listaSoggettiString;
	}

	public String getElencoUI() {
		return elencoUI;
	}

	public void setElencoUI(String elencoUI) {
		this.elencoUI = elencoUI;
	}

	public String getStringListaSoggetti() {
		
		String stringListaSoggetti="";
		
		if (this.getListaSoggettiString()!= null){
		for (int i =0; i< this.getListaSoggettiString().size(); i++ ){
			String sogg= this.getListaSoggettiString().get(i);
			stringListaSoggetti= stringListaSoggetti+ sogg;
			
			if (i!= this.getListaSoggettiString().size()-1)
				stringListaSoggetti= stringListaSoggetti + " ; "	;
			
			}
		}
		return stringListaSoggetti;
	}

	public void setStringListaSoggetti(String stringListaSoggetti) {
		this.stringListaSoggetti = stringListaSoggetti;
	}

	

	
}
