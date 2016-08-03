package it.escsolution.escwebgis.successioni.logic;

import java.util.Vector;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscLogic;

public class SuccessioniLogic extends EscLogic{

	public SuccessioniLogic(EnvUtente eu) {
		super(eu);
		// TODO Auto-generated constructor stub
	}
	
	
	protected Vector verificaParamAnnoRicerca(Vector listaPar){
		
		EscElementoFiltro eAnno = this.getParametroRicerca(listaPar, "ANNO");
		String anno = eAnno.getValore();
		if(anno!=null && anno.length()>2)
			anno = anno.substring(anno.length()-2, anno.length());
		
		//log.debug("Anno successioni ricerca: " + eAnno.getValore() + " " +anno);
		
		eAnno.setValore(anno);
		
		 listaPar = this.aggiornaValoreParametro(listaPar, "ANNO", eAnno);
		 
		 return listaPar;
	}
	

}
