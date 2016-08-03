package it.webred.ct.service.carContrib.data.access.catasto;

import it.webred.ct.data.access.basic.catasto.dto.RicercaSoggettoCatDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.RicercaDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.SoggettoDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.SitPatrimImmobileDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.SitPatrimTerrenoDTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface CatastoCarContribServiceModel {
	//PER ICI
	public List<SitPatrimImmobileDTO> getImmobiliPosseduti(RicercaSoggettoCatDTO rs, String visInScheda);
	public List<SitPatrimTerrenoDTO> getTerreniPosseduti(RicercaSoggettoCatDTO rs);
	public List<SitPatrimImmobileDTO> getImmobiliCeduti(RicercaSoggettoCatDTO rs);
	public List<SitPatrimTerrenoDTO> getTerreniCeduti(RicercaSoggettoCatDTO rs);

}
