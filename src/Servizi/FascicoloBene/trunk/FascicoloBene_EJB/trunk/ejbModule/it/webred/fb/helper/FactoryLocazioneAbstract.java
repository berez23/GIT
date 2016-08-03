package it.webred.fb.helper;

import it.webred.fb.data.model.DmBIndirizzo;
import it.webred.fb.data.model.DmBMappale;
import it.webred.fb.data.model.DmBTerreno;
import it.webred.fb.ejb.dto.Dato;
import it.webred.fb.ejb.dto.MappaleDTO;
import it.webred.fb.ejb.dto.locazione.DatiCatastali;
import it.webred.fb.ejb.dto.locazione.DatiLocalizzazione;
import it.webred.fb.helper.checks.IChecker;

import org.apache.log4j.Logger;

public abstract class FactoryLocazioneAbstract {

	protected String ente;

	
	protected abstract DatiCatastali buildDatiCatastaliImpl(MappaleDTO map);

	protected abstract DatiCatastali buildDatiTerrenoImpl(DmBTerreno map);
	
	protected abstract DatiLocalizzazione buildDatiLocalizzazioneImpl(DmBIndirizzo ind);
	
	protected static Logger logger = Logger.getLogger("fascicolobene.log");

	
	public Dato build(Object o) throws NoBuildImplementedForClassException, Exception {
		
		try {
				
			if (o instanceof MappaleDTO)
					return check(buildDatiCatastaliImpl((MappaleDTO)o));
			else if (o instanceof DmBIndirizzo)
				return check(buildDatiLocalizzazioneImpl((DmBIndirizzo)o));
			else if (o instanceof DmBTerreno)
				return check(buildDatiTerrenoImpl((DmBTerreno)o));
			else
				throw new NoBuildImplementedForClassException();
		} catch (Exception e) {
			throw new Exception("Problemi nella costruzione dei dati ", e);
		}
			
		
	}
	
	
	   private Dato check(Dato da) throws Exception {

				
				try {
						Class checkClass;
						checkClass = Class.forName("it.webred.fb.helper.checks." + da.getClass().getSimpleName()+ "Checker");
						IChecker<Dato> check = (IChecker) checkClass.getConstructor(String.class).newInstance(ente) ;
						check.setDato(da); 
						check.check();
				} catch (ClassNotFoundException e) {
					logger.warn("Nessun controllo possibile sui dati", e);
				} catch (Exception e) {
					logger.warn("Problemi nella verifica di congruenza dei dati", e);
					throw e;
				}
			
		   

			return da;

		    	 
		   }
	   
}
