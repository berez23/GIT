package it.webred.ct.service.comma336.web.bean.util.suggestion;

import java.util.Arrays;
import java.util.List;

import javax.faces.model.SelectItem;

import it.webred.ct.data.access.basic.c336.dto.C336PraticaDTO;
import it.webred.ct.service.comma336.web.Comma336BaseBean;

public class StatoPraticaSuggestionBean extends Comma336BaseBean {
	
	private final List<SelectItem> listaStati= Arrays.asList(
			new SelectItem(C336PraticaDTO.COD_STATO_NON_ESAMINATA, C336PraticaDTO.STATI.get(C336PraticaDTO.COD_STATO_NON_ESAMINATA)),
			new SelectItem(C336PraticaDTO.COD_STATO_ISTRUTTORIA_IN_CORSO, C336PraticaDTO.STATI.get(C336PraticaDTO.COD_STATO_ISTRUTTORIA_IN_CORSO)),
			new SelectItem(C336PraticaDTO.COD_STATO_ISTRUTTORIA_CONCLUSA, C336PraticaDTO.STATI.get(C336PraticaDTO.COD_STATO_ISTRUTTORIA_CONCLUSA))

	);

	public List<SelectItem> getListaStati() {
		return listaStati;
	}
	
}
