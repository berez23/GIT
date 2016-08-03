package it.webred.ct.service.tsSoggiorno.web.bean.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import sun.security.action.LoadLibraryAction;

import it.webred.ct.service.tsSoggiorno.data.access.DichiarazioneService;
import it.webred.ct.service.tsSoggiorno.data.access.dto.DataInDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.DichiarazioneSearchCriteria;
import it.webred.ct.service.tsSoggiorno.data.access.dto.SocietaDTO;
import it.webred.ct.service.tsSoggiorno.data.model.IsClasse;
import it.webred.ct.service.tsSoggiorno.data.model.IsPeriodo;
import it.webred.ct.service.tsSoggiorno.web.bean.TsSoggiornoBaseBean;

public class ReportBaseBean extends TsSoggiornoBaseBean {

	protected String codFiscale;
	protected DichiarazioneSearchCriteria criteria = new DichiarazioneSearchCriteria();
	private List<SelectItem> listaPeriodi = new ArrayList<SelectItem>();
	private List<SelectItem> listaClassi = new ArrayList<SelectItem>();
	private List<SelectItem> listaCodSocieta = new ArrayList<SelectItem>();

	public DichiarazioneSearchCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(DichiarazioneSearchCriteria criteria) {
		this.criteria = criteria;
	}

	public List<SelectItem> getListaPeriodi() {
		if (listaPeriodi == null || listaPeriodi.size() == 0)
			loadPeriodi();
		return listaPeriodi;
	}

	public void setListaPeriodi(List<SelectItem> listaPeriodi) {
		this.listaPeriodi = listaPeriodi;
	}

	public List<SelectItem> getListaClassi() {
		if (listaClassi == null || listaClassi.size() == 0)
			loadClassi();
		return listaClassi;
	}

	public void setListaClassi(List<SelectItem> listaClassi) {
		this.listaClassi = listaClassi;
	}

	public List<SelectItem> getListaCodSocieta() {
		if (listaCodSocieta == null || listaCodSocieta.size() == 0)
			loadCodSocieta();
		return listaCodSocieta;
	}

	public void setListaCodSocieta(List<SelectItem> listaCodSocieta) {
		this.listaCodSocieta = listaCodSocieta;
	}

	public void loadPeriodi() {

		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);
		List<IsPeriodo> lista = getDichiarazioneService().getPeriodi(dataIn);
		for (IsPeriodo c : lista) {
			listaPeriodi.add(new SelectItem(c.getId(), c.getDescrizione()));
		}
	}

	private void loadClassi() {

		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);
		List<IsClasse> lista = getAnagraficaService().getClassi(dataIn);
		for (IsClasse c : lista) {
			listaClassi.add(new SelectItem(c.getCodice(), c.getDescrizione()));
		}
	}

	private void loadCodSocieta() {

		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);
		dataIn.setCodFiscale(codFiscale);
		List<SocietaDTO> lista = super.getAnagraficaService()
				.getSocietaByCodFis(dataIn);
		for (SocietaDTO s : lista) {
			if (s.getSocieta().getPi() != null)
				listaCodSocieta.add(new SelectItem(s.getSocieta().getPi(), s.getSocieta().getPi()));
			else
				listaCodSocieta.add(new SelectItem(s.getSocieta().getCf(), s.getSocieta().getCf()));
		}
	}

	protected void exportXLSReport(HSSFWorkbook workbook, String nomeReport) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {

			workbook.write(baos);

			if (baos != null) {
				FacesContext faces = FacesContext.getCurrentInstance();
				HttpServletResponse response = (HttpServletResponse) faces
						.getExternalContext().getResponse();

				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control",
						"must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				response.setContentType("application/vnd.ms-excel");
				response.addHeader("Content-disposition",
						"attachment;filename=\"" + nomeReport + ".xls");

				ServletOutputStream out = response.getOutputStream();
				baos.writeTo(out);
				baos.flush();

				response.setContentLength(baos.size());
				out.close();
				faces.responseComplete();
			}

		} catch (Exception e) {
			super.addErrorMessage("elaborazione.error", e.getMessage());
			getLogger().error("Eccezione: " + e.getMessage(), e);
		} finally {
			try {
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
