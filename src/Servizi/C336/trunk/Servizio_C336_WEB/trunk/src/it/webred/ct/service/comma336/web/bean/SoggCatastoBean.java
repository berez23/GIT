package it.webred.ct.service.comma336.web.bean;

import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.catasto.dto.SoggettoDTO;
import it.webred.ct.service.comma336.web.Comma336BaseBean;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SoggCatastoBean extends Comma336BaseBean{

	private List<SoggettoDTO> soggetti;
	private String foglio;
	private String particella;
	private BigDecimal pkIdUiu;
	private String dataFine;
	
	private SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd"); 
	
	public void doCaricaSoggTerreno(){
		RicercaOggettoCatDTO roc = new RicercaOggettoCatDTO();
		fillEnte(roc);
		roc.setFoglio(foglio);
		roc.setParticella(particella);
		try {
			roc.setDtVal(yyyyMMdd.parse(dataFine));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
		}
		roc.setAltriSoggetti(true);
		soggetti = new ArrayList<SoggettoDTO>();
		soggetti.addAll(catastoService.getListaSoggettiAttualiTerreno(roc));
		soggetti.addAll(catastoService.getListaSoggettiStoriciTerreno(roc));
	}
	
	public void doCaricaSoggImmobile(){
		RicercaOggettoCatDTO roc = new RicercaOggettoCatDTO();
		fillEnte(roc);
		roc.setIdUiu(pkIdUiu.toString());
		soggetti = new ArrayList<SoggettoDTO>();
		soggetti = catastoService.getListaSoggettiImmobile(roc);
	}
	
	public List<SoggettoDTO> getSoggetti() {
		return soggetti;
	}
	
	public void setSoggetti(List<SoggettoDTO> soggetti) {
		this.soggetti = soggetti;
	}
	
	public String getFoglio() {
		return foglio;
	}
	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}
	public String getParticella() {
		return particella;
	}
	public void setParticella(String particella) {
		this.particella = particella;
	}

	public BigDecimal getPkIdUiu() {
		return pkIdUiu;
	}

	public void setPkIdUiu(BigDecimal pkIdUiu) {
		this.pkIdUiu = pkIdUiu;
	}

	public String getDataFine() {
		return dataFine;
	}

	public void setDataFine(String dataFine) {
		this.dataFine = dataFine;
	}

}
