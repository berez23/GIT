package it.webred.cs.csa.web.manbean.fascicolo.colloquio;

import it.webred.amprofiler.ejb.anagrafica.AnagraficaService;
import it.webred.amprofiler.model.AmAnagrafica;
import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.csa.ejb.dto.IterDTO;
import it.webred.cs.csa.web.manbean.fascicolo.FascicoloCompBaseBean;
import it.webred.cs.data.model.CsACaso;
import it.webred.cs.data.model.CsACasoOpeTipoOpe;
import it.webred.cs.data.model.CsCDiarioConchi;
import it.webred.cs.data.model.CsCDiarioDove;
import it.webred.cs.data.model.CsCTipoColloquio;
import it.webred.cs.data.model.CsDColloquio;
import it.webred.cs.data.model.CsDDiario;
import it.webred.cs.data.model.CsOOperatoreSettore;
import it.webred.cs.data.model.CsTbTipoDiario;
import it.webred.cs.jsf.bean.DatiColloquioBean;
import it.webred.cs.jsf.interfaces.IColloquio;
import it.webred.dto.utility.KeyValuePairBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class ColloquioBean extends FascicoloCompBaseBean implements IColloquio {

/*********** Start Colloquio ******************/
	protected String username;
	private List<DatiColloquioBean> listaColloquios;
	private DatiColloquioBean selectedColloquio;
	private DatiColloquioBean newColloquio = null;

	private List<CsCTipoColloquio> tipoColloquios;
	private List<CsCDiarioDove> diarioDoves;
	private List<CsCDiarioConchi> diarioConchis;
	
	private Long diarioDoveSelected;
	private boolean isSoggetto;
	
	private List<KeyValuePairBean<Long, String>> nameTipoColloquios = new LinkedList<KeyValuePairBean<Long, String>>();
	private List<KeyValuePairBean<Long, String>> nameDiarioDoves = new LinkedList<KeyValuePairBean<Long, String>>();
	private List<KeyValuePairBean<Long, String>> nameDiarioConchis = new LinkedList<KeyValuePairBean<Long, String>>();

	@Override
	public DatiColloquioBean getSelectedColloquio() {
		return selectedColloquio;
	}
	
	public void setSelectedColloquio(DatiColloquioBean datiColloquio) throws Exception {
		OnSelectedColloquio( datiColloquio );
	}

	@Override
	public List<KeyValuePairBean<Long, String>> getNameTipoColloquios() {
		return nameTipoColloquios;
	}

	@Override
	public List<KeyValuePairBean<Long,String>> getNameDiarioDoves() {
		return nameDiarioDoves;
	}

	@Override
	public List<KeyValuePairBean<Long,String>> getNameDiarioConchis() {
		return nameDiarioConchis;
	}
	
	@Override
	public Long getDiarioDoveSelected() {
		return diarioDoveSelected;
	}

	public void setDiarioDoveSelected(Long diarioDoveSelected) {
		this.diarioDoveSelected = diarioDoveSelected;
	}

	@Override
	public List<DatiColloquioBean> getListaColloquios() {
		return listaColloquios;
	}

	@Override
	public boolean isSoggetto() {
		isSoggetto = getSession().getAttribute("soggetto") == null ? true :false;
		return isSoggetto;
	}

	protected void loadListaColloqui(BaseDTO dto) throws Exception {
		
		listaColloquios = new ArrayList<DatiColloquioBean>();
				
		List<CsDColloquio> listaColl = diarioService.getColloquios(dto);
	
		if(listaColl != null) {
			for(CsDColloquio coll: listaColl) {
				DatiColloquioBean bean = new DatiColloquioBean();
				String usernameOp = coll.getUsrMod();
				if( StringUtils.isEmpty(usernameOp) )
					usernameOp = coll.getUserIns();
				bean.Initialize(coll, usernameOp );
				listaColloquios.add(bean);
			}
		}
	}

	protected void OnSelectedColloquio(DatiColloquioBean datiColloquio) throws Exception{
		selectedColloquio = datiColloquio;
		
		if(datiColloquio.getColloquio().getCsCDiarioDove() == null)
			diarioDoveSelected = null;		 
		else 
			diarioDoveSelected =  selectedColloquio.getColloquio().getCsCDiarioDove().getId();
	}

	@Override
	public void OnNewColloquio() throws Exception{
		
		BaseDTO dto = new BaseDTO();
		fillEnte(dto); 
 
//		tipoColloquios = diarioService.getTipoColloquios(dto);
//		diarioDoves = diarioService.getDiarioDoves(dto);
//		diarioConchis = diarioService.getDiarioConchis(dto);

		newColloquio = new DatiColloquioBean();
		CsDColloquio csColl = new CsDColloquio();
		
		csColl.setRiservato("0");
		csColl.setUserIns( dto.getUserId() );
		csColl.setCsCTipoColloquio(tipoColloquios.get(0));
		csColl.setCsCDiarioConchi(diarioConchis.get(0));
		csColl.setCsCDiarioDove(diarioDoves.get(0));

		newColloquio.Initialize(csColl, dto.getUserId());
		
		OnSelectedColloquio(newColloquio);
	}

	@Override
	public void initializeData() {
		
		try{
			
			BaseDTO dto = new BaseDTO();
			fillEnte(dto);
			
			if(csASoggetto != null)
				dto.setObj(csASoggetto.getAnagraficaId());
			
			tipoColloquios = diarioService.getTipoColloquios(dto);
			for (CsCTipoColloquio item : tipoColloquios)
				nameTipoColloquios.add(new KeyValuePairBean<Long,String>(item.getId(), item.getDescrizione()));
			
			diarioDoves = diarioService.getDiarioDoves(dto);
			for (CsCDiarioDove item : diarioDoves)
				nameDiarioDoves.add(new KeyValuePairBean<Long,String>(item.getId(), item.getDescrizione()));
			
			diarioConchis = diarioService.getDiarioConchis(dto);
			for (CsCDiarioConchi item : diarioConchis)
				nameDiarioConchis.add(new KeyValuePairBean<Long,String>(item.getId(), item.getDescrizione()));
		
			loadListaColloqui(dto);
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			addErrorFromProperties("caricamento.error");
		}
	}


	@Override
	public void saveColloquio()  {
		try {

			BaseDTO colloquioDto = new BaseDTO();
			fillEnte(colloquioDto);
			
//			tipoColloquios = diarioService.getTipoColloquios(colloquioDto);
//			diarioDoves = diarioService.getDiarioDoves(colloquioDto);
//			diarioConchis = diarioService.getDiarioConchis(colloquioDto);
//			
			CsCTipoColloquio tipoColloquio = null;
			CsCDiarioDove diarioDove = null; 
			CsCDiarioConchi diarioConchi = null; 

			// Set Tipo colloquio selected into colloquio
			for (CsCTipoColloquio item : tipoColloquios) {
				if (item.getId() == selectedColloquio.getColloquio().getCsCTipoColloquio().getId()) {
					tipoColloquio = item;
					break;
				}
			}

			// Set diario Dove selected into colloquio
			for (CsCDiarioDove item : diarioDoves) {
				if (item.getId() == diarioDoveSelected) {
					diarioDove = item;
					break;
				}
			}

			// Set diario Conchi selected into colloquio
			for (CsCDiarioConchi item : diarioConchis) {
				if (item.getId() == selectedColloquio.getColloquio().getCsCDiarioConchi().getId()) {
					diarioConchi = item;
					break;
				}
			}

			if (diarioDove != null ) 	selectedColloquio.getColloquio().setCsCDiarioDove(diarioDove);
			else                     	selectedColloquio.getColloquio().setCsCDiarioDove(null);
			if (diarioConchi != null ) 	selectedColloquio.getColloquio().setCsCDiarioConchi(diarioConchi);
			if (tipoColloquio != null ) selectedColloquio.getColloquio().setCsCTipoColloquio(tipoColloquio);

			if( selectedColloquio.isAbilitato4riservato() ) {
				selectedColloquio.getColloquio().setTestoDiario(selectedColloquio.getCampoTesto());
				selectedColloquio.getColloquio().setRiservato(selectedColloquio.isRiservato()?"1":"0");
			}

			// New Colloquio
			if (selectedColloquio.getColloquio().getDiarioId() == null) {

				IterDTO itDto = new IterDTO();
				fillEnte(itDto);
				itDto.setIdCaso(csASoggetto.getCsACaso().getId());

				CsACaso caso = casoService.findCasoById(itDto);

				BaseDTO tipoDiarioIdDTO = new BaseDTO();
				fillEnte(tipoDiarioIdDTO);

				// Set Id of TipoDiario: 5 = Colloqui/Incontri
				long TipoDiarioId = 5;
				tipoDiarioIdDTO.setObj(TipoDiarioId);

				CsTbTipoDiario tipoDiario = diarioService.findTipoDiarioById(tipoDiarioIdDTO);
				CsOOperatoreSettore opSettore = (CsOOperatoreSettore) getSession().getAttribute("operatoresettore");
				
				CsDDiario diario = new CsDDiario();
				diario.setCsACaso(caso);
				diario.setCsTbTipoDiario(tipoDiario);
				diario.setCsOOperatoreSettore(opSettore);
				selectedColloquio.getColloquio().setCsDDiario(diario);
				selectedColloquio.getColloquio().setDiarioId(diario.getId());

				selectedColloquio.getColloquio().setUserIns(tipoDiarioIdDTO.getUserId());
				selectedColloquio.getColloquio().setDtIns(new Date());
				colloquioDto.setObj(selectedColloquio.getColloquio());
				diarioService.saveDiarioChild(colloquioDto);
				
				listaColloquios.add(selectedColloquio);
				
			} else {
				selectedColloquio.getColloquio().setUsrMod(colloquioDto.getUserId());
				selectedColloquio.getColloquio().setDtMod(new Date());
				colloquioDto.setObj(selectedColloquio.getColloquio());

				diarioService.updateColloquio(colloquioDto);
			}
			
			selectedColloquio.Update();
			
			//loadListaColloqui(colloquioDto);
			addInfo("Info", "Salvataggio Diario avvenuto con successo.");
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			addError("Error", "Errore nel salvataggio del Diario (Colloquio)!");
		}
	}
	
}


