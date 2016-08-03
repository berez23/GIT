package it.webred.fb.helper;

import it.webred.fb.data.model.DmBIndirizzo;
import it.webred.fb.data.model.DmBMappale;
import it.webred.fb.data.model.DmBTerreno;
import it.webred.fb.ejb.dto.DatoSpec;
import it.webred.fb.ejb.dto.MappaleDTO;
import it.webred.fb.ejb.dto.locazione.DatiCatastali;
import it.webred.fb.ejb.dto.locazione.DatiLocalizzazione;
import it.webred.fb.ejb.dto.locazione.DatiTerreni;

public class FactoryLocazione extends FactoryLocazioneAbstract {
	
	   public FactoryLocazione (String ente) {
		   this.ente = ente;
	   }
	
	protected DatiCatastali buildDatiCatastaliImpl(MappaleDTO map) {
		DatoSpec codInventario = new DatoSpec(map.getCodInventario());
		DatoSpec comuneDato = new DatoSpec(map.getDesComune()+" ("+map.getProv()+")");
		DatoSpec codComuneDato = new DatoSpec(map.getCodComune());
		DatoSpec sezDato =  new DatoSpec(map.getSezione());
		DatoSpec foglioDato =  new DatoSpec(map.getFoglio());
		DatoSpec mappaleDato =  new DatoSpec(map.getMappale());
		DatoSpec provenienzaCatDato =  new DatoSpec(map.getProvenienza());
		
		DatiCatastali dCatastali = new DatiCatastali(codInventario,comuneDato,codComuneDato, sezDato, foglioDato, mappaleDato, provenienzaCatDato);
		
		return dCatastali;
	}
	
	protected DatiLocalizzazione buildDatiLocalizzazioneImpl(DmBIndirizzo ind) {
		 DatoSpec codViaDato;
		 DatoSpec descrizioneDato;
		 DatoSpec codComuneDato;
		 DatoSpec comuneDato;
		 DatoSpec principaleDato;
		 DatoSpec provenienzaLocDato;
		 DatoSpec codInventario;
		
		codViaDato =  new DatoSpec(ind.getCodVia().toString());
		
		String tipoVia = ind.getTipoVia()!=null ? ind.getTipoVia().trim() : "";
		String via = ind.getDescrVia()!=null ? ind.getDescrVia().trim() : "";
		String civico = ind.getCivico()!=null ? ", "+ind.getCivico().trim() : "";
		descrizioneDato =  new DatoSpec(tipoVia + " " + via + civico);
		codComuneDato = new DatoSpec(ind.getCodComune());
		comuneDato =  new DatoSpec(ind.getDesComune()+" ("+ind.getProv()+")"); 
		principaleDato =  new DatoSpec(ind.getFlgPrincipale() == null ? "" : ind.getFlgPrincipale().longValue()==0?"":"Si" );
		provenienzaLocDato = new DatoSpec(ind.getProvenienza());
		codInventario = new DatoSpec(ind.getDmBBene().getCodChiave1());
		
		DatiLocalizzazione datiLoc = new DatiLocalizzazione( provenienzaLocDato);
		
		datiLoc.setCodInventario(codInventario);
		datiLoc.setCodComune(codComuneDato);
		datiLoc.setCodVia(codViaDato);
		datiLoc.setDescrizione(descrizioneDato);
		datiLoc.setPrincipale(principaleDato);
		datiLoc.setComune(comuneDato);
		
		datiLoc.setCivico( new DatoSpec(ind.getCivico()));
		datiLoc.setTipo(new DatoSpec( ind.getTipo()));
		datiLoc.setTipoVia(new DatoSpec(ind.getTipoVia()));
		
		return datiLoc;
		
	}

	@Override
	protected DatiTerreni buildDatiTerrenoImpl(DmBTerreno map) {
		DatoSpec codInventario = new DatoSpec(map.getDmBBene().getDmBBeneInv().getCodInventario());
		DatoSpec comuneDato = new DatoSpec(map.getCodComune());
		DatoSpec sezDato =  new DatoSpec(map.getSezione());
		DatoSpec foglioDato =  new DatoSpec(map.getFoglio());
		DatoSpec mappaleDato =  new DatoSpec(map.getMappale());
		DatoSpec provenienzaCatDato =  new DatoSpec(map.getProvenienza());
		
		DatiTerreni dCatastali = new DatiTerreni(codInventario,comuneDato, sezDato, foglioDato, mappaleDato, provenienzaCatDato);
		dCatastali.setDettaglio(map);
		return dCatastali;
	}
}
