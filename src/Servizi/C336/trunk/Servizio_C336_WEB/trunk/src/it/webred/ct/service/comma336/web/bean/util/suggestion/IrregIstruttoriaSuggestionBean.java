package it.webred.ct.service.comma336.web.bean.util.suggestion;

import it.webred.ct.data.model.c336.C336DecodIrreg;
import it.webred.ct.service.comma336.web.Comma336BaseBean;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

public class IrregIstruttoriaSuggestionBean extends Comma336BaseBean {
	private List<SelectItem> listaIrregolarita;

	public List<SelectItem> getListaIrregolarita() {
		doCarica();
		return listaIrregolarita;
	}

	public void setListaIrregolarita(List<SelectItem> listaIrregolarita) {
		this.listaIrregolarita = listaIrregolarita;
	}
	
	public void doCarica() {
		if (listaIrregolarita == null) {
			listaIrregolarita = new ArrayList<SelectItem>();
			try {
				CeTBaseObject obj = new CeTBaseObject();
				this.fillEnte(obj);
				List<C336DecodIrreg> lista = praticaService.getListaIrregolarita(obj);
				for (C336DecodIrreg ele: lista) {
					logger.debug("doCarica listaIrreg-ele: " + ele.getDesIrreg());
					SelectItem item = new SelectItem(ele.getCodIrreg(), ele.getDesIrreg());
					listaIrregolarita.add(item);
				}

			} catch (Throwable t) {
				super.addErrorMessage("suggestion.error", t.getMessage());
				logger.error(t.getMessage(), t);
			}
		}
	}
	
}
