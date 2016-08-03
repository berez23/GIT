package it.webred.ct.service.spclass.web.bean.util;

import it.webred.ct.service.spclass.web.SpProfClassamentoBaseBean;

import java.util.Arrays;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

public class CommonDataBean extends SpProfClassamentoBaseBean {

	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(CommonDataBean.class.getName());

	private final List<SelectItem> listaSiNo = Arrays.asList(new SelectItem("SI", "SI"), new SelectItem("NO", "NO"));
	
	private final List<SelectItem> listaTipologiaIntervento = Arrays.asList(new SelectItem("1","Nuova Costruzione"), new SelectItem("2", "Ristrutturazione"));

	public List<SelectItem> getListaSiNo() {
		return listaSiNo;
	}

	public List<SelectItem> getListaTipologiaIntervento() {
		return listaTipologiaIntervento;
	}
	
}
