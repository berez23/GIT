package it.webred.indice.fastsearch;

import java.util.Vector;

import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.webred.indice.fastsearch.bean.TipoRicercaBean;

public class GenericFastSearchServlet extends EscServlet implements EscService {
	
	private static final long serialVersionUID = 1L;

	
	protected EscElementoFiltro getFiltroTipoRicerca(){
		
		Vector<TipoRicercaBean> vctTipoRicerca = new Vector<TipoRicercaBean>();
		//lista fissa
		vctTipoRicerca.add(new TipoRicercaBean("SOLO_RIF", "Fonte dati di riferimento"));
		vctTipoRicerca.add(new TipoRicercaBean("ALL_F", "Tutte le fonti dati"));
		
		EscElementoFiltro elementoFiltro = new EscElementoFiltro();
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Ricerca in ");
		elementoFiltro.setAttributeName("SOLO_RIF");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(null);
		elementoFiltro.setListaValori(vctTipoRicerca);
		elementoFiltro.setValore("SOLO_RIF");
		elementoFiltro.setCampoFiltrato("SOLO_RIF");
		
		return elementoFiltro;
		
	}
	
	protected boolean verificaSearchSoloUnico(){
		
		/*Inizio gestione ricerca in UNICO o TOTALE*/
		boolean unico = false;
		Object objUnico = null; 
		for(Object o : vettoreRicerca){
			EscElementoFiltro en = (EscElementoFiltro) o;
			if(en.getAttributeName().equalsIgnoreCase("SOLO_RIF")){
				objUnico = o;
				log.debug("valore " +en.getValore());
				if("SOLO_RIF".equalsIgnoreCase(en.getValore())){
					log.debug("Ricerco Solo in UNICO");
					unico = true;
				}else if("ALL_F".equalsIgnoreCase(en.getValore()))
					log.debug("Ricerco in TUTTE le fonti");
				
			}
		}
		
		if(objUnico!=null)
			vettoreRicerca.remove(objUnico);
		/*Fine gestione ricerca in UNICO o TOTALE*/
		
		return unico;
	}
	
	

}
