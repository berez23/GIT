package it.bod.application.backing;

import java.math.BigDecimal;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import it.bod.application.beans.BodBean;
import it.bod.application.beans.CategoriaBean;
import it.bod.application.beans.CausaleBean;
import it.bod.application.beans.DatiGeneraliBean;
import it.bod.application.beans.DestinazioneFunzionaleBean;
import it.bod.application.beans.DichiaranteBean;
import it.bod.application.beans.UiuBean;
import it.bod.application.common.FilterItem;
import it.bod.application.common.Info;
import it.bod.application.common.MasterClass;
import it.bod.application.common.Schiavo;
import it.bod.business.service.BodLogicService;
import it.bod.business.service.CategoriaService;
import it.bod.business.service.CausaleService;
import it.bod.business.service.DatiGeneraliService;
import it.bod.business.service.DestinazioneFunzionaleService;
import it.bod.business.service.DichiaranteService;
import it.bod.business.service.UiuService;
import it.doro.tools.Character;
import it.persistance.common.SqlHandler;
import it.webred.permessi.GestionePermessi;
import it.webred.rich.common.CalendarBoxRch;
import it.webred.rich.common.ComboBoxRch;
import it.webred.rich.common.SuggestionBoxRch;

import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.ListableBeanFactory;

public class RicercaFiltroBck extends MasterClass{

	private static final long serialVersionUID = 8540799659915353493L;
	private static Logger logger = Logger.getLogger("it.bod.application.backing.RicercaFiltroBck");
	
	private CategoriaService categoriaService = null;
	private CausaleService causaleService = null;
	private BodLogicService bodLogicService = null;
	private DatiGeneraliService datiGeneraliService = null;
	private DestinazioneFunzionaleService destinazioneFunzionaleService = null;
	private DichiaranteService dichiaranteService = null;
	private UiuService uiuService = null;
	private String selCausale = "";
	private ArrayList<SelectItem> alCausali = null;
	private ArrayList<SelectItem> alDestFunz = null;
	private ComboBoxRch cbxCausali = null;
	private ComboBoxRch cbxAnniFornitura = null;
	private ComboBoxRch cbxMesiFornitura = null;
	private ComboBoxRch cbxCategorie = null;
	private ComboBoxRch cbxDestinazioniFunzionali = null;
	private ComboBoxRch cbxStatoIstruttoria = null;
	private String cbxEsitoIstruttoria = null;
	private SuggestionBoxRch txtDicCognome = null;
	private HtmlInputText htmlDicCognome = null;
	private SuggestionBoxRch txtProCognome = null;
	private HtmlInputText htmlProCognome = null;
	private SuggestionBoxRch txtIndirizzo = null;
	private CalendarBoxRch txtDataVariazioneDal = null;
	private CalendarBoxRch txtDataVariazioneAl = null;
	private CalendarBoxRch txtDataRegistrazioneDal = null;
	private CalendarBoxRch txtDataRegistrazioneAl = null;
	private String cbxOperatore = "";
	private String cbxTipoOperazione = "";
	private String cbxCoerenza = "";
	private String lblValoreMedio = "";
	private String txtRapportoVCRV = "";
	private String txtScostamento = "";
	private ArrayList<SelectItem> alTipiOperazioni = null;
	private ArrayList<SelectItem> alCollaudati = null;
	private String collaudato = "";
	private String collaudoEsito = "";
	private String[] incoerenzaUiu = null;
	private ArrayList<SelectItem> alCollaudiEsiti = null;
	private int dicPrefixLenght = 0;
	private int proPrefixLenght = 0;
	private int indPrefixLenght = 0;
	private ComboBoxRch cbxOpeNumProtocollo = null;
	private ComboBoxRch cbxOpeDataVariazioneDal = null;
	private ComboBoxRch cbxOpeDataVariazioneAl = null;
	private ComboBoxRch cbxOpeDataRegistrazioneDal = null;
	private ComboBoxRch cbxOpeDataRegistrazioneAl = null;
	private ComboBoxRch cbxOpeDicCognome = null;
	private ComboBoxRch cbxOpeDicNome = null;
	private ComboBoxRch cbxOpeProCognome = null;
	private ComboBoxRch cbxOpeProNome = null;
	private ComboBoxRch cbxOpeIndirizzo = null;
	private HtmlInputText txtNumProtocollo = null;
	private HtmlInputText txtProNome = null;
	private HtmlInputText txtDicNome = null;
	private HtmlInputText txtImmFoglio = null;
	private HtmlInputText txtImmParticella = null;
	private HtmlInputText txtImmSubalterno = null;
	private HtmlInputText txtCivico = null;
	private HtmlOutputLabel outLabel = null;
	private ArrayList<BodBean> lstBod = null;
	private Integer numberOfRows = new Integer(0);
	private Boolean isAutorizzatoRicerca = false;
	
	public static final String emptyComboItem = "Tutti";
	
	private Boolean disableMesi = true;
		
	public RicercaFiltroBck() {
		
		super.initializer();
	}//-------------------------------------------------------------------------
	
	private void init(){
		/*
		 * Caricare i dati per la combo delle causali
		 */
		Info.htTipiOperazioni.put("C", "COSTITUITA");
		Info.htTipiOperazioni.put("S", "SOPPRESSA");
		Info.htTipiOperazioni.put("V", "VARIATA");
		
	}//-------------------------------------------------------------------------
	
	public String pulisci(){
		String esito = "ricercaFiltroBck.pulisci";
		/*
		 * Ripulisce i componenti del filtro
		 */
		this.txtNumProtocollo = new HtmlInputText();
		this.txtDataVariazioneDal = new CalendarBoxRch();
		this.txtDataVariazioneAl = new CalendarBoxRch();
		this.cbxCausali.setState("");
		this.cbxOperatore = "";
		this.txtDataRegistrazioneDal = new CalendarBoxRch();
		this.txtDataRegistrazioneAl = new CalendarBoxRch();
		this.txtDicCognome.setProperty2("");
		this.htmlDicCognome = new HtmlInputText();
		this.txtDicNome = new HtmlInputText();
		this.txtProCognome.setProperty2("");
		this.htmlProCognome = new HtmlInputText();
		this.txtProNome = new HtmlInputText();
		this.txtImmFoglio = new HtmlInputText();
		this.txtImmParticella = new HtmlInputText();
		this.txtImmSubalterno = new HtmlInputText();
		this.txtIndirizzo.setProperty2("");
		this.txtCivico = new HtmlInputText();
		this.cbxTipoOperazione = "";
		this.cbxDestinazioniFunzionali.setState("");
		this.cbxStatoIstruttoria.setState("");
		this.cbxEsitoIstruttoria = "";
		this.cbxAnniFornitura.setState("");
		this.cbxMesiFornitura.setState("");
		this.cbxCategorie.setState("");
		this.disableMesi=true;
		
		return esito;
	}//-------------------------------------------------------------------------
	
	public String ricerca(){
		String esito = "ricercaFiltroBck.ricerca";
		/*
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext extContext = context.getExternalContext();
		//ServletContext servletContext = (ServletContext)extContext.getContext();
		HttpServletRequest req = (HttpServletRequest)extContext.getRequest();
		HttpSession jsfSession = (HttpSession)req.getSession(false);

		jsfSession.removeAttribute("lstBod");
		*/
		String condizioni = "";
		SimpleDateFormat sdf = new SimpleDateFormat ("dd/MM/yyyy");
		/*
		 * Recupero i valori e imposto i filtri 
		 */
		ArrayList<FilterItem> aryFilters = new ArrayList<FilterItem>();
		String numProtocollo = (String)this.txtNumProtocollo.getValue();
		if (numProtocollo != null && !numProtocollo.trim().equalsIgnoreCase("")){
			String opeNumPro = this.cbxOpeNumProtocollo.getState();
			/*
			 * 1. uguale
			 * 2. contiene
			 */
			opeNumPro = (opeNumPro != null && opeNumPro.trim().equalsIgnoreCase("contiene"))?"LIKE":"=";
			FilterItem fi = new FilterItem();
			fi.setOperatore(opeNumPro);
			fi.setParametro(":numProtocollo");
			fi.setValore(numProtocollo.toUpperCase());
			aryFilters.add(fi);
			if (opeNumPro.equalsIgnoreCase("="))
				condizioni += "and D_GEN.protocollo_reg " + opeNumPro + " upper('" + numProtocollo.trim().toUpperCase() + "') ";
			else
				condizioni += "and D_GEN.protocollo_reg " + opeNumPro + " upper('%" + numProtocollo.trim().toUpperCase() + "%') ";
		}
		Date dataVariazioneDal = this.txtDataVariazioneDal.getSelectedDate();
		if (dataVariazioneDal != null){
			String opeDatVarDal = this.cbxOpeDataVariazioneDal.getState();
			/*
			 * 1. mag. uguale
			 * 2. min. uguale
			 */
			opeDatVarDal = (opeDatVarDal != null && opeDatVarDal.trim().equalsIgnoreCase("mag. uguale"))?">=":"<=";
			FilterItem fi = new FilterItem();
			fi.setOperatore(opeDatVarDal);
			fi.setParametro(":dataVariazioneDal");
			fi.setValore(sdf.format(dataVariazioneDal));
			aryFilters.add(fi);
			condizioni += "and D_GEN.data_variazione " + opeDatVarDal + " to_date('" + sdf.format(dataVariazioneDal) + "' ,'dd/mm/yyyy') "; 
		}
		Date dataVariazioneAl = this.txtDataVariazioneAl.getSelectedDate();
		if (dataVariazioneAl != null){
			String opeDatVarAl = this.cbxOpeDataVariazioneAl.getState();
			/*
			 * 1. min. uguale
			 * 2. mag. uguale
			 */
			opeDatVarAl = (opeDatVarAl != null && opeDatVarAl.trim().equalsIgnoreCase("mag. uguale"))?">=":"<=";
			FilterItem fi = new FilterItem();
			fi.setOperatore(opeDatVarAl);
			fi.setParametro(":dataVariazioneAl");
			fi.setValore(sdf.format(dataVariazioneAl));
			aryFilters.add(fi);
			condizioni += "and d_gen.data_variazione " + opeDatVarAl + " to_date('" + sdf.format(dataVariazioneAl) + "' ,'dd/mm/yyyy') ";
		}
		String causale = this.cbxCausali.getState();
		if (causale != null && !causale.trim().equalsIgnoreCase("") && !causale.trim().equalsIgnoreCase(emptyComboItem)){
			/*
			 * Decodifico la descrizione
			 */
			causale = Schiavo.fromDescrToCode(causale, Info.htCausali);
			FilterItem fi = new FilterItem();
			fi.setOperatore("=");
			fi.setParametro(":causale");
			fi.setValore(causale);
			aryFilters.add(fi);
			condizioni += "and D_GEN.causale_nota_vax = '" + causale + "' ";
		}
		String operatore = this.cbxOperatore;
		if (operatore != null && !operatore.trim().equalsIgnoreCase("") && !operatore.trim().equalsIgnoreCase(emptyComboItem)){
			FilterItem fi = new FilterItem();
			fi.setOperatore("=");
			fi.setParametro(":operatore");
			if (operatore.trim().equalsIgnoreCase("AdT"))
				fi.setValore("is null");
			if (operatore.trim().equalsIgnoreCase("Polo Catastale"))
				fi.setValore("is not null");
			aryFilters.add(fi);
			condizioni += "and OPER.campo14 " + fi.getValore() + " ";
		}
		Date dataRegistrazioneDal = this.txtDataRegistrazioneDal.getSelectedDate();
		if (dataRegistrazioneDal != null){
			String opeDatRegDal = this.cbxOpeDataRegistrazioneDal.getState();
			/*
			 * 1. mag. uguale
			 * 2. min. uguale
			 */
			opeDatRegDal = (opeDatRegDal != null && opeDatRegDal.trim().equalsIgnoreCase("mag. uguale"))?">=":"<=";
			FilterItem fi = new FilterItem();
			fi.setOperatore(opeDatRegDal);
			fi.setParametro(":dataRegistrazioneDal");
			fi.setValore(sdf.format(dataRegistrazioneDal));
			aryFilters.add(fi);
			condizioni += "and to_date(CEN.data_registrazione, 'yyyymmdd') " + opeDatRegDal + " to_date('" + sdf.format(dataRegistrazioneDal) + "', 'dd/mm/yyyy') ";

		}
		Date dataRegistrazioneAl = this.txtDataRegistrazioneAl.getSelectedDate();
		if (dataRegistrazioneAl != null){
			String opeDatRegAl = this.cbxOpeDataRegistrazioneAl.getState();
			/*
			 * 1. min. uguale
			 * 2. mag. uguale
			 */
			opeDatRegAl = (opeDatRegAl != null && opeDatRegAl.trim().equalsIgnoreCase("mag. uguale"))?">=":"<=";
			FilterItem fi = new FilterItem();
			fi.setOperatore(opeDatRegAl);
			fi.setParametro(":dataRegistrazioneAl");
			fi.setValore(sdf.format(dataRegistrazioneAl));
			aryFilters.add(fi);
			condizioni += "and to_date(CEN.data_registrazione, 'yyyymmdd') " + opeDatRegAl + " to_date('" + sdf.format(dataRegistrazioneAl) + "', 'dd/mm/yyyy') ";
		}
		String dicCognome = txtDicCognome.getProperty2(); 
		if (dicCognome == null || dicCognome.trim().equalsIgnoreCase(""))
			dicCognome = (String)htmlDicCognome.getValue();
		if (dicCognome != null && !dicCognome.trim().equalsIgnoreCase("")){
			String opeDicCognome = this.cbxOpeDicCognome.getState();
			/*
			 * 1. contiene
			 * 2. uguale
			 */
			opeDicCognome = (opeDicCognome != null && opeDicCognome.trim().equalsIgnoreCase("contiene"))?"LIKE":"=";
			FilterItem fi = new FilterItem();
			fi.setOperatore(opeDicCognome);
			fi.setParametro(":dicCognome");
			fi.setValore(dicCognome.trim());
			aryFilters.add(fi);
			if (opeDicCognome.equalsIgnoreCase("="))
				condizioni += "and upper(DICH.dic_cognome) " + opeDicCognome + " upper('" + dicCognome.trim() + "') ";
			else
				condizioni += "and upper(DICH.dic_cognome) " + opeDicCognome + " upper('%'|| '" + dicCognome.trim() + "' ||'%') ";
			
		}
		String dicNome = (String)this.txtDicNome.getValue();
		if (dicNome != null && !dicNome.trim().equalsIgnoreCase("")){
			String opeDicNome = this.cbxOpeDicNome.getState();
			/*
			 * 1. contiene
			 * 2. uguale
			 */
			opeDicNome = (opeDicNome != null && opeDicNome.trim().equalsIgnoreCase("contiene"))?"LIKE":"=";
			FilterItem fi = new FilterItem();
			fi.setOperatore(opeDicNome);
			fi.setParametro(":dicNome");
			fi.setValore(dicNome.trim());
			aryFilters.add(fi);
			if (opeDicNome.equalsIgnoreCase("="))
				condizioni += "and upper(DICH.dic_nome) " + opeDicNome + " upper('" + dicNome.trim() + "') ";
			else
				condizioni += "and upper(DICH.dic_nome) " + opeDicNome + " upper('%'|| '" + dicNome.trim() + "' ||'%') ";
			
		}
		String proCognome = this.txtProCognome.getProperty2();
		if (proCognome == null || proCognome.trim().equalsIgnoreCase(""))
			proCognome = (String)htmlProCognome.getValue();
		if (proCognome != null && !proCognome.trim().equalsIgnoreCase("")){
			String opeProCognome = this.cbxOpeProCognome.getState();
			/*
			 * 1. contiene
			 * 2. uguale
			 */
			opeProCognome = (opeProCognome != null && opeProCognome.trim().equalsIgnoreCase("contiene"))?"LIKE":"=";
			FilterItem fi = new FilterItem();
			fi.setOperatore(opeProCognome);
			fi.setParametro(":proCognome");
			fi.setValore(proCognome.trim());
			aryFilters.add(fi);
			if (opeProCognome.equalsIgnoreCase("="))
				condizioni += "and upper(DICH.tec_cognome) " + opeProCognome + " upper('" + proCognome.trim() + "') ";
			else
				condizioni += "and upper(DICH.tec_cognome) " + opeProCognome + " upper('%'|| '" + proCognome.trim() + "' ||'%') ";

		}
		String proNome = (String)this.txtProNome.getValue();
		if (proNome != null && !proNome.trim().equalsIgnoreCase("")){
			String opeProNome = this.cbxOpeProNome.getState();
			/*
			 * 1. contiene
			 * 2. uguale
			 */
			opeProNome = (opeProNome != null && opeProNome.trim().equalsIgnoreCase("contiene"))?"LIKE":"=";
			FilterItem fi = new FilterItem();
			fi.setOperatore(opeProNome);
			fi.setParametro(":proNome");
			fi.setValore(proNome.trim());
			aryFilters.add(fi);
			if (opeProNome.equalsIgnoreCase("="))
				condizioni += "and upper(DICH.tec_nome) " + opeProNome + " upper('" + proNome.trim() + "') ";
			else
				condizioni += "and upper(DICH.tec_nome) " + opeProNome + " upper('%'|| '" + proNome.trim() + "' ||'%') ";

		}
		String foglio = (String)this.txtImmFoglio.getValue();
		if (foglio != null && !foglio.trim().equalsIgnoreCase("")){
			FilterItem fi = new FilterItem();
			fi.setOperatore("=");
			fi.setParametro(":foglio");
			fi.setValore(foglio);
			aryFilters.add(fi);
			condizioni += "and U.foglio = lpad('" + foglio + "', 4, '0') ";
		}
		String particella = (String)this.txtImmParticella.getValue();
		if (particella != null && !particella.trim().equalsIgnoreCase("")){
			FilterItem fi = new FilterItem();
			fi.setOperatore("=");
			fi.setParametro(":particella");
			fi.setValore(particella);
			aryFilters.add(fi);
			condizioni += "and U.numero = lpad('" + particella + "', 5, '0') ";
		}
		String subalterno = (String)this.txtImmSubalterno.getValue();
		if (subalterno != null && !subalterno.trim().equalsIgnoreCase("")){
			FilterItem fi = new FilterItem();
			fi.setOperatore("=");
			fi.setParametro(":subalterno");
			fi.setValore(subalterno);
			aryFilters.add(fi);
			condizioni += "and U.subalterno = lpad('" + subalterno + "', 4, '0') ";
		}
		String indirizzo = this.txtIndirizzo.getProperty2();
		if (indirizzo != null && !indirizzo.trim().equalsIgnoreCase("")){
			String opeIndirizzo = this.cbxOpeIndirizzo.getState();
			/*
			 * 1. contiene
			 * 2. uguale
			 */
			opeIndirizzo = (opeIndirizzo != null && opeIndirizzo.trim().equalsIgnoreCase("contiene"))?"LIKE":"=";
			FilterItem fi = new FilterItem();
			fi.setOperatore(opeIndirizzo);
			fi.setParametro(":indirizzo");
			fi.setValore(indirizzo);
			aryFilters.add(fi);
			if (opeIndirizzo.equalsIgnoreCase("="))
				condizioni += "and upper(U.indir_toponimo) " + opeIndirizzo + " upper('" + indirizzo + "') ";
			else
				condizioni += "and upper(U.indir_toponimo) " + opeIndirizzo + " upper('%'|| '" + indirizzo + "' ||'%') ";

		}
		String civico = (String)this.txtCivico.getValue();
		if (civico != null && !civico.trim().equalsIgnoreCase("")){
			FilterItem fi = new FilterItem();
			fi.setOperatore("=");
			fi.setParametro(":civico");
			fi.setValore(civico);
			aryFilters.add(fi);
			condizioni += "and U.indir_nciv_uno like '%'|| '" + civico + "' ||'%' ";
		}
		String tipoOperazione = this.cbxTipoOperazione;
		if (tipoOperazione != null && !tipoOperazione.trim().equalsIgnoreCase("") && !tipoOperazione.trim().equalsIgnoreCase(emptyComboItem)){
			/*
			 * Decodifico dalla descrizione
			 */
			tipoOperazione = Schiavo.fromDescrToCode(tipoOperazione, Info.htTipiOperazioni);
			FilterItem fi = new FilterItem();
			fi.setOperatore("=");
			fi.setParametro(":tipoOperazione");
			fi.setValore(tipoOperazione);
			aryFilters.add(fi);
			condizioni += "and U.tipo_operazione = '" + tipoOperazione + "' ";
		}
		String destinazioneFunzionale = this.cbxDestinazioniFunzionali.getState();
		if (destinazioneFunzionale != null && !destinazioneFunzionale.trim().equalsIgnoreCase("") && !destinazioneFunzionale.trim().equalsIgnoreCase(emptyComboItem)){
			FilterItem fi = new FilterItem();
			fi.setOperatore("=");
			fi.setParametro(":destinazioneFunzionale");
			String[] ary = destinazioneFunzionale.split("-");
			fi.setValore(ary[0]);
			aryFilters.add(fi);
			/*
			 * è il codice della destinazione funzionale da passare alla query seguente
			 * non la descrizione
			 */
			condizioni += "and exists (select 1 from hw_partprg SPP where dest_funz = '" + ary[0] + "' and lpad(SPP.foglio, 4, '0') = U.foglio and lpad(SPP.particella, 5, '0') = U.numero) ";
		}
		String statoIstruttoria = this.cbxStatoIstruttoria.getState();
		if (statoIstruttoria != null && !statoIstruttoria.trim().equalsIgnoreCase("") && !statoIstruttoria.trim().equalsIgnoreCase(emptyComboItem)){
			FilterItem fi = new FilterItem();
			fi.setOperatore("=");
			fi.setParametro(":statoIstruttoria");
			fi.setValore(statoIstruttoria);
			aryFilters.add(fi);
			if (statoIstruttoria.trim().equalsIgnoreCase("NON ESAMINATA"))	
				//condizioni += " and not exists (select 1 from bod_istruttoria where fornitura = d_gen.fornitura and protocollo_reg=d_gen.protocollo_reg) ";
				condizioni += " and bi.id_ist is null ";
			else if (statoIstruttoria.trim().equalsIgnoreCase("PRESA IN CARICO"))	
				condizioni += " and bi.presa_in_carico = 1 and bi.chiusa = 0 ";
			else if (statoIstruttoria.trim().equalsIgnoreCase("CHIUSA"))	
				condizioni += " and bi.chiusa = 1 "; 
		}
		String esitoIstruttoria = this.cbxEsitoIstruttoria;
		if (esitoIstruttoria != null && !esitoIstruttoria.trim().equalsIgnoreCase("") && !esitoIstruttoria.trim().equalsIgnoreCase(emptyComboItem)){
			FilterItem fi = new FilterItem();
			fi.setOperatore("=");
			fi.setParametro(":esitoIstruttoria");
			fi.setValore(esitoIstruttoria);
			aryFilters.add(fi);
			if (esitoIstruttoria.trim().equalsIgnoreCase("L.662/1996"))	
				condizioni += " and bi.esito_662 = 1 ";
			else if (esitoIstruttoria.trim().equalsIgnoreCase("L.80/2006"))	
				condizioni += " and bi.esito_80 = 1 ";
			else if (esitoIstruttoria.trim().equalsIgnoreCase("L.311/2004"))	
				condizioni += " and bi.esito_311 = 1 "; 
			else if (esitoIstruttoria.trim().equalsIgnoreCase("Nessuna Anomalia"))	
				condizioni += " and bi.esito_NA = 1 ";
		}
		String annoFornitura = this.cbxAnniFornitura.getState();
		if (annoFornitura != null && !annoFornitura.trim().equalsIgnoreCase("") && !annoFornitura.trim().equalsIgnoreCase(emptyComboItem)){
			FilterItem fi = new FilterItem();
			fi.setOperatore("=");
			fi.setParametro(":annoFornitura");
			fi.setValore(annoFornitura);
			aryFilters.add(fi);
			condizioni += " and to_char(d_gen.fornitura, 'yyyy')= '" + annoFornitura + "' ";
		}
		String meseFornitura = this.cbxMesiFornitura.getState();
		if (meseFornitura != null && !meseFornitura.trim().equalsIgnoreCase("") && !meseFornitura.trim().equalsIgnoreCase(emptyComboItem)){
			FilterItem fi = new FilterItem();
			fi.setOperatore("=");
			fi.setParametro(":meseFornitura");
			fi.setValore(Schiavo.fromDescrToCode(meseFornitura, Info.htMesi) );	
			aryFilters.add(fi);
			condizioni += " and to_char(d_gen.fornitura, 'mm')= '" + Schiavo.fromDescrToCode(meseFornitura, Info.htMesi) + "' ";
		}
		
		String categoria = this.cbxCategorie.getState();
		if (categoria != null && !categoria.trim().equalsIgnoreCase("") && !categoria.trim().equalsIgnoreCase(emptyComboItem)){
			FilterItem fi = new FilterItem();
			fi.setOperatore("=");
			fi.setParametro(":categoria");
			fi.setValore(categoria);
			aryFilters.add(fi);
			condizioni += " and U.prop_categoria = '" + categoria + "' ";
		}
		if (collaudato != null && !collaudato.trim().equalsIgnoreCase("") && collaudato.trim().equalsIgnoreCase("SI")){
			FilterItem fi = new FilterItem();
			fi.setOperatore("=");
			fi.setParametro(":collaudo");
			fi.setValore(collaudato);
			aryFilters.add(fi);
			condizioni += " and be.esito is not null ";
		}else if (collaudato != null && !collaudato.trim().equalsIgnoreCase("") && collaudato.trim().equalsIgnoreCase("NO")){
			FilterItem fi = new FilterItem();
			fi.setOperatore("=");
			fi.setParametro(":collaudo");
			fi.setValore(collaudato);
			aryFilters.add(fi);
			condizioni += " and be.esito is null ";
		}
		if (collaudoEsito != null && !collaudoEsito.trim().equalsIgnoreCase("") && !collaudoEsito.trim().equalsIgnoreCase(emptyComboItem)){
			FilterItem fi = new FilterItem();
			fi.setOperatore("=");
			fi.setParametro(":collaudoEsito");
			fi.setValore(collaudoEsito);
			aryFilters.add(fi);
			condizioni += " and be.esito = '" + collaudoEsito + "' ";
		}
		
		int numeroFiltri = 0;
		if (aryFilters != null && aryFilters.size() > 0){
			numeroFiltri = aryFilters.size();
		}
		if (incoerenzaUiu != null && incoerenzaUiu.length>0)
			numeroFiltri++;
		if (txtRapportoVCRV != null && !txtRapportoVCRV.trim().equalsIgnoreCase("") 
				&& cbxCoerenza != null && !cbxCoerenza.trim().equalsIgnoreCase("") && !cbxCoerenza.trim().equalsIgnoreCase("Tutti")
				&& txtScostamento != null && !txtScostamento.trim().equalsIgnoreCase(""))
			numeroFiltri++;
		
//		String sql = "SELECT DISTINCT " +
//		"D_GEN.protocollo_reg as prot, " +
//		"to_char(D_GEN.fornitura,'mm/yyyy') as fornitura, " +
//		"D_GEN.data_variazione as datavariazione, " +
//		"D_GEN.uiu_in_soppressione as soppressione, " +
//		"D_GEN.uiu_in_variazione as variazione, " +
//		"D_GEN.uiu_in_costituzione as costituzione, " +
//		"DICH.dic_cognome || ' ' || DICH.dic_nome as dichiarante, " +
//		"DICH.tec_cognome || ' ' || DICH.tec_nome as tecnico, " +
//		"CAU.cau_des as causa " +
//		
//		"FROM " +
//		
//		"docfa_dati_generali D_GEN " +
//		"left join docfa_dichiaranti DICH on DICH.fornitura = D_GEN.fornitura and D_GEN.protocollo_reg = DICH.protocollo_reg " +
//		"left join docfa_uiu U on D_GEN.fornitura = U.fornitura and D_GEN.protocollo_reg = U.protocollo_reg " +
//		"left join docfa_dati_censuari CEN on D_GEN.fornitura = CEN.fornitura and D_GEN.protocollo_reg = CEN.protocollo_registrazione " +
//		"left join docfa_operatori OPER on to_char(D_GEN.fornitura,'yyyy') = OPER.anno and D_GEN.protocollo_reg = OPER.protocollo " +
//		"left join docfa_causali CAU on D_GEN.causale_nota_vax = CAU.cau_cod " +
//		"LEFT JOIN bod_istruttoria bi ON d_gen.fornitura = bi.fornitura AND d_gen.protocollo_reg = bi.protocollo " +
//		"WHERE 1=1 ";
		
		Properties prop = SqlHandler.loadProperties(propName);
		String sql = prop.getProperty("qryCerca");
		
		Hashtable htQry = new Hashtable();
		if (numeroFiltri > 0){
			//htQry.put("where", aryFilters);
			sql += " " + condizioni;
			
			//sql += " ORDER BY  D_GEN.fornitura DESC, D_GEN.protocollo_reg DESC, D_GEN.data_variazione DESC  ";
			sql += " ORDER BY  D_GEN.fornitura DESC, D_GEN.protocollo_reg DESC  ";

			htQry.put("queryString", sql);
			
			List listaObjs = bodLogicService.getList(htQry);
			/*
			 * XXX INIZIO FILTRI RUNTIME 
			 */
			List lstTmp = new LinkedList();
			if (incoerenzaUiu != null && incoerenzaUiu.length>0 && listaObjs != null && listaObjs.size()>0){
				
				for (Object iObjs : listaObjs){
					Object[] objs = (Object[])iObjs;
					/*
					 *  recupero l'oggetto 19 corrisponde a BOD_SEGNALAZIONI.ID_SEG
					 *  per accedere alla BOD_UIU.SEG_FK e verificare se le incoerenze
					 *  nelle colonne relative
					 */
					BigDecimal segId = (BigDecimal)objs[19];
					if (segId != null){
						String qry = prop.getProperty("qrySegnalazioniLista");
						String whereClause = "AND bu.seg_fk = '" + segId.longValue() + "' AND (";
						for (int z=0; z<incoerenzaUiu.length; z++){
							whereClause += " INC_" + incoerenzaUiu[z] + "= '1' ";
							if (z == incoerenzaUiu.length-1){
								
							}else{
								whereClause += " OR ";
							}
						}
						whereClause += ") ";
						Hashtable htSql = new Hashtable();
						htSql.put("queryString", qry + whereClause);
						List listaBodUiu = bodLogicService.getList(htSql);
						/*
						 * per ogni segnalazione è possibile avere piu di una uiu
						 * pertanto andiamo solo a verificare se la lista contiene
						 * qualche riga, ma non è necessario andare a verificarne 
						 * il contenuto 
						 */
						if (listaBodUiu != null && listaBodUiu.size()>0)
							lstTmp.add(iObjs);
					}
				}
				listaObjs = new LinkedList<Object>(lstTmp);
			}
			
			
			List lstTmp2 = new LinkedList();
			if (txtRapportoVCRV != null && !txtRapportoVCRV.trim().equalsIgnoreCase("") 
					&& cbxCoerenza != null && !cbxCoerenza.trim().equalsIgnoreCase("") && !cbxCoerenza.trim().equalsIgnoreCase("Tutti")
					&& txtScostamento != null && !txtScostamento.trim().equalsIgnoreCase("")
					&& listaObjs != null && listaObjs.size()>0){
				for (Object iObjs : listaObjs){
					Object[] objs = (Object[])iObjs;
					/*
					 * 0: prot
					 * 1: fornitura MM/YYYY
					 */
					/*
					 * Accedo alla docfa_report utilizzando protocollo e fornitura 
					 * per recuperare il campo RAPP_2 che andrà poi a confrontare
					 * con il valore medio immesso dall'utente
					 */
					String qry = prop.getProperty("qryDocfaReport");
					String whereClause = " AND PROT = '" + (String)objs[0] + "' AND TO_CHAR(FORN, 'mm/yyyy') = '" + (String)objs[1] + "' ";
					Hashtable htSql = new Hashtable();
					htSql.put("queryString", qry + whereClause);
					List listaDocfaRapporti = bodLogicService.getList(htSql);
					/*
					 * questa query restituisce piu righe ... una per ogni subalterno 
					 */
					BigDecimal rapporto = new BigDecimal(txtRapportoVCRV);
					BigDecimal scostamentoPerc = new BigDecimal(txtScostamento);
					BigDecimal scostamentoEuro = rapporto.multiply( scostamentoPerc.divide( new BigDecimal( 100 ) ) );
					BigDecimal limiteMin = rapporto.subtract(scostamentoEuro);
					BigDecimal limiteMax = rapporto.add(scostamentoEuro);
					if (listaDocfaRapporti != null && listaDocfaRapporti.size()>0){
						BigDecimal dr2 = null;
						boolean coerente = true;
						boolean incoerente = true;
						for (int k=0; k<listaDocfaRapporti.size(); k++){
							dr2 = (BigDecimal)listaDocfaRapporti.get(k);
							if (dr2 != null && dr2.compareTo(new BigDecimal(0)) == 1){
								if (cbxCoerenza.trim().equalsIgnoreCase("COERENTI")){
									/*
									 * l'esito del rapporto tra VAL.COMMERCIALE / RENDITA RIVALUTATA
									 * deve essere compreso tra limiteMin e limiteMax per
									 * essere definito COERENTE; se minore a limiteMin o maggiore di limiteMax
									 * viene classificato come INCOERENTE
									 */
									if (dr2.compareTo( limiteMin ) == -1 || dr2.compareTo( limiteMax ) == 1)
										coerente = false;
									
								}else if (cbxCoerenza.trim().equalsIgnoreCase("INCOERENTI")){
									/*
									 * l'esito del rapporto tra VAL.COMMERCIALE / RENDITA RIVALUTATA
									 * deve essere compreso tra limiteMin e limiteMax per
									 * essere definito COERENTE; se minore a limiteMin o maggiore di limiteMax
									 * viene classificato come INCOERENTE
									 */
									if (dr2.compareTo( limiteMin ) == 1 && dr2.compareTo( limiteMax ) == -1 )
										incoerente = false;
									
								}				
							}else{
								/*
								 * facciamo vedere le pratiche a rapporto nullo o
								 * inferiore a zero solo tra gli incoerenti
								 */
								coerente = false;
							}
						}
						if (coerente && cbxCoerenza.trim().equalsIgnoreCase("COERENTI"))
							lstTmp2.add(iObjs);							
						
						if (incoerente && cbxCoerenza.trim().equalsIgnoreCase("INCOERENTI"))
							lstTmp2.add(iObjs);							

					}
				}
				listaObjs = new LinkedList<Object>(lstTmp2);
			}
			
			/*
			 * XXX FINE FILTRI RUNTIME
			 */
			
			List lstObj = new LinkedList(listaObjs);
			
			this.lstBod = new ArrayList<BodBean>();
			if (lstObj != null && lstObj.size()>0){
				/*
				 * BodBean bb = null;
				for (int i=0; i<lstObj.size(); i++){
					bb = new BodBean();
					Object[] objs = (Object[])lstObj.get(i);
					bb.setProtocollo((String)objs[0]);
					bb.setFornitura((String)objs[1]);
					bb.setDataVariazione(objs[2] != null?sdf.format((Date)objs[2]):"");
					bb.setSoppressione((BigDecimal)objs[3]);
					bb.setVariazione((BigDecimal)objs[4]);
					bb.setCostituzione((BigDecimal)objs[5]);
					bb.setDichiarante((String)objs[6]);
					bb.setTecnico((String)objs[7]);
					bb.setCausale((String)objs[8]);
					
					this.lstBod.add(bb);
				}
				 */
				this.lstBod.addAll(Schiavo.setLstBodFromLstObject(lstObj));
			}
			//jsfSession.setAttribute("lstBod", lstBod);
		}else{
			esito = "failure";
			this.outLabel = new HtmlOutputLabel();
			this.outLabel.setValue("Inserire un filtro valido");
		}
		if (lstBod != null)
			numberOfRows = new Integer(lstBod.size());
		return esito;
	}//-------------------------------------------------------------------------

	
	public HtmlOutputLabel getOutLabel() {
		return outLabel;
	}

	public void setOutLabel(HtmlOutputLabel outLabel) {
		this.outLabel = outLabel;
	}

	public ArrayList<BodBean> getLstBod() {
		return lstBod;
	}

	public void setLstBod(ArrayList<BodBean> lstBod) {
		this.lstBod = lstBod;
	}

	public DatiGeneraliService getDatiGeneraliService() {
		return datiGeneraliService;
	}

	public void setDatiGeneraliService(DatiGeneraliService datiGeneraliService) {
		this.datiGeneraliService = datiGeneraliService;
	}

	public CategoriaService getCategoriaService() {
		return categoriaService;
	}

	public void setCategoriaService(CategoriaService categoriaService) {
		this.categoriaService = categoriaService;
	}

	public ComboBoxRch getCbxAnniFornitura() {
		return cbxAnniFornitura;
	}

	public void setCbxAnniFornitura(ComboBoxRch cbxAnnoFornitura) {
		this.cbxAnniFornitura = cbxAnnoFornitura;
	}

	public ComboBoxRch getCbxCategorie() {
		return cbxCategorie;
	}

	public void setCbxCategorie(ComboBoxRch cbxCategoria) {
		this.cbxCategorie = cbxCategoria;
	}

	public CalendarBoxRch getTxtDataVariazioneDal() {
		return txtDataVariazioneDal;
	}

	public void setTxtDataVariazioneDal(CalendarBoxRch txtDataVariazioneDal) {
		this.txtDataVariazioneDal = txtDataVariazioneDal;
	}

	public CalendarBoxRch getTxtDataVariazioneAl() {
		return txtDataVariazioneAl;
	}

	public void setTxtDataVariazioneAl(CalendarBoxRch txtDataVariazioneAl) {
		this.txtDataVariazioneAl = txtDataVariazioneAl;
	}

	public HtmlInputText getTxtCivico() {
		return txtCivico;
	}

	public void setTxtCivico(HtmlInputText txtCivico) {
		this.txtCivico = txtCivico;
	}

	public HtmlInputText getTxtImmFoglio() {
		return txtImmFoglio;
	}

	public void setTxtImmFoglio(HtmlInputText txtImmFoglio) {
		this.txtImmFoglio = txtImmFoglio;
	}

	public HtmlInputText getTxtImmParticella() {
		return txtImmParticella;
	}

	public void setTxtImmParticella(HtmlInputText txtImmParticella) {
		this.txtImmParticella = txtImmParticella;
	}

	public HtmlInputText getTxtImmSubalterno() {
		return txtImmSubalterno;
	}

	public void setTxtImmSubalterno(HtmlInputText txtImmSubalterno) {
		this.txtImmSubalterno = txtImmSubalterno;
	}

	public HtmlInputText getTxtDicNome() {
		return txtDicNome;
	}

	public void setTxtDicNome(HtmlInputText txtDicNome) {
		this.txtDicNome = txtDicNome;
	}

	public HtmlInputText getTxtNumProtocollo() {
		return txtNumProtocollo;
	}

	public void setTxtNumProtocollo(HtmlInputText txtNumProtocollo) {
		this.txtNumProtocollo = txtNumProtocollo;
	}

	public BodLogicService getBodLogicService() {
		return bodLogicService;
	}

	public void setBodLogicService(BodLogicService bodLogicService) {
		this.bodLogicService = bodLogicService;
	}

	public ComboBoxRch getCbxOpeIndirizzo() {
		return cbxOpeIndirizzo;
	}

	public void setCbxOpeIndirizzo(ComboBoxRch cbxOpeIndirizzo) {
		this.cbxOpeIndirizzo = cbxOpeIndirizzo;
	}

	public ComboBoxRch getCbxOpeProCognome() {
		return cbxOpeProCognome;
	}

	public void setCbxOpeProCognome(ComboBoxRch cbxOpeProCognome) {
		this.cbxOpeProCognome = cbxOpeProCognome;
	}

	public ComboBoxRch getCbxOpeProNome() {
		return cbxOpeProNome;
	}

	public void setCbxOpeProNome(ComboBoxRch cbxOpeProNome) {
		this.cbxOpeProNome = cbxOpeProNome;
	}

	public ComboBoxRch getCbxOpeDicNome() {
		return cbxOpeDicNome;
	}

	public void setCbxOpeDicNome(ComboBoxRch cbxOpeDicNome) {
		this.cbxOpeDicNome = cbxOpeDicNome;
	}

	public ComboBoxRch getCbxOpeDicCognome() {
		return cbxOpeDicCognome;
	}

	public void setCbxOpeDicCognome(ComboBoxRch cbxOpeDicCognome) {
		this.cbxOpeDicCognome = cbxOpeDicCognome;
	}

	public ComboBoxRch getCbxOpeDataRegistrazioneAl() {
		return cbxOpeDataRegistrazioneAl;
	}

	public void setCbxOpeDataRegistrazioneAl(ComboBoxRch cbxDataRegistrazioneAl) {
		this.cbxOpeDataRegistrazioneAl = cbxDataRegistrazioneAl;
	}

	public ComboBoxRch getCbxOpeDataRegistrazioneDal() {
		return cbxOpeDataRegistrazioneDal;
	}

	public void setCbxOpeDataRegistrazioneDal(ComboBoxRch cbxDataRegistrazioneDal) {
		this.cbxOpeDataRegistrazioneDal = cbxDataRegistrazioneDal;
	}

	public ComboBoxRch getCbxOpeNumProtocollo() {
		return cbxOpeNumProtocollo;
	}

	public void setCbxOpeNumProtocollo(ComboBoxRch cbxOpeNumProtocollo) {
		this.cbxOpeNumProtocollo = cbxOpeNumProtocollo;
	}

	public String getCbxEsitoIstruttoria() {
		return cbxEsitoIstruttoria;
	}

	public void setCbxEsitoIstruttoria(String cbxEsitoIstruttoria) {
		this.cbxEsitoIstruttoria = cbxEsitoIstruttoria;
	}

	public ComboBoxRch getCbxStatoIstruttoria() {
		return cbxStatoIstruttoria;
	}

	public void setCbxStatoIstruttoria(ComboBoxRch cbxStatoIstruttoria) {
		this.cbxStatoIstruttoria = cbxStatoIstruttoria;
	}

	public SuggestionBoxRch getTxtIndirizzo() {
		return txtIndirizzo;
	}

	public void setTxtIndirizzo(SuggestionBoxRch txtIndirizzo) {
		this.txtIndirizzo = txtIndirizzo;
	}

	public UiuService getUiuService() {
		return uiuService;
	}

	public void setUiuService(UiuService indirizzoService) {
		this.uiuService = indirizzoService;
	}

	public DestinazioneFunzionaleService getDestinazioneFunzionaleService() {
		return destinazioneFunzionaleService;
	}

	public void setDestinazioneFunzionaleService(
			DestinazioneFunzionaleService destinazioneFunzionaleService) {
		this.destinazioneFunzionaleService = destinazioneFunzionaleService;
	}

	public ComboBoxRch getCbxDestinazioniFunzionali() {
		return cbxDestinazioniFunzionali;
	}

	public void setCbxDestinazioniFunzionali(ComboBoxRch cbxDestinazioniFunzionali) {
		this.cbxDestinazioniFunzionali = cbxDestinazioniFunzionali;
	}

	public HtmlInputText getTxtProNome() {
		return txtProNome;
	}

	public void setTxtProNome(HtmlInputText txtProNome) {
		this.txtProNome = txtProNome;
	}

	public SuggestionBoxRch getTxtProCognome() {
		return txtProCognome;
	}

	public void setTxtProCognome(SuggestionBoxRch txtProCognome) {
		this.txtProCognome = txtProCognome;
	}

	public ArrayList<SelectItem> getAlTipiOperazioni() {
		return alTipiOperazioni;
	}

	public void setAlTipiOperazioni(ArrayList<SelectItem> alTipiOperazioni) {
		this.alTipiOperazioni = alTipiOperazioni;
	}

	public String getCbxOperatore() {
		return cbxOperatore;
	}

	public void setCbxOperatore(String cbxOperatore) {
		this.cbxOperatore = cbxOperatore;
	}

	public String getCbxTipoOperazione() {
		return cbxTipoOperazione;
	}

	public void setCbxTipoOperazione(String cbxTipoOperazione) {
		this.cbxTipoOperazione = cbxTipoOperazione;
	}

	public CalendarBoxRch getTxtDataRegistrazioneDal() {
		return txtDataRegistrazioneDal;
	}

	public void setTxtDataRegistrazioneDal(CalendarBoxRch txtDataRegistrazioneDal) {
		this.txtDataRegistrazioneDal = txtDataRegistrazioneDal;
	}

	public CalendarBoxRch getTxtDataRegistrazioneAl() {
		return txtDataRegistrazioneAl;
	}

	public void setTxtDataRegistrazioneAl(CalendarBoxRch txtDataRegistrazioneAl) {
		this.txtDataRegistrazioneAl = txtDataRegistrazioneAl;
	}

	public ComboBoxRch getCbxOpeDataVariazioneDal() {
		return cbxOpeDataVariazioneDal;
	}

	public void setCbxOpeDataVariazioneDal(ComboBoxRch cbxOpeDataVariazioneDal) {
		this.cbxOpeDataVariazioneDal = cbxOpeDataVariazioneDal;
	}

	public ComboBoxRch getCbxOpeDataVariazioneAl() {
		return cbxOpeDataVariazioneAl;
	}

	public void setCbxOpeDataVariazioneAl(ComboBoxRch cbxOpeDataVariazioneAl) {
		this.cbxOpeDataVariazioneAl = cbxOpeDataVariazioneAl;
	}

	public DichiaranteService getDichiaranteService() {
		return dichiaranteService;
	}

	public void setDichiaranteService(DichiaranteService dichiaranteService) {
		this.dichiaranteService = dichiaranteService;
	}

	public String preparaFiltro(){
		String esito = "ricercaFiltroBck.preparaFiltro";
		
		isAutorizzatoRicerca = GestionePermessi.autorizzato(utente, nomeApplicazione, "Ricerca Bod", "Accertato");
		if (isAutorizzatoRicerca){
			init();
			
			alCollaudati = new ArrayList<SelectItem>();
			SelectItem si0 = new SelectItem();
			si0.setDescription(emptyComboItem);
			si0.setLabel(emptyComboItem);
			si0.setValue(emptyComboItem);
			alCollaudati.add(si0);
			SelectItem si1 = new SelectItem();
			si1.setDescription("NO");
			si1.setLabel("NO");
			si1.setValue("NO");
			alCollaudati.add(si1);
			SelectItem si2 = new SelectItem();
			si2.setDescription("SI");
			si2.setLabel("SI");
			si2.setValue("SI");
			alCollaudati.add(si2);
			
			alCollaudiEsiti = new ArrayList<SelectItem>(Info.aylEsitiCollaudoOpz);
			
			Properties prop = SqlHandler.loadProperties(propName);
			String sql = prop.getProperty("qryDocfaRapporto");
			Hashtable htSql = new Hashtable();
			htSql.put("queryString", sql);
			List listaObjs = bodLogicService.getList(htSql);
			if (listaObjs != null && listaObjs.size()>0){
				txtRapportoVCRV = lblValoreMedio = (String)listaObjs.get(0);
			}
			/*
			 * imposto il valore di default della percentuale di scarto
			 */
			txtScostamento = "5.0";
			
			List<CausaleBean> lstCausali = causaleService.getList();
			if (lstCausali != null){
				logger.info("[numero causali caricate:] " + lstCausali.size());
				alCausali = new ArrayList<SelectItem>();
				Iterator<CausaleBean> itCau = lstCausali.iterator();
				SelectItem si = null;
				String suggestions = "";
				si = new SelectItem();
				si.setValue(emptyComboItem);
				si.setLabel(emptyComboItem);
				si.setDescription(emptyComboItem);
				alCausali.add(si);
				suggestions += "," + emptyComboItem;
				while(itCau.hasNext()){
					si = new SelectItem();
					CausaleBean cau = itCau.next();
					Info.htCausali.put(cau.getCau_cod(), Character.checkNullString(cau.getCau_des()));
					si.setValue(cau.getCau_cod());
					si.setLabel(cau.getCau_des());
					si.setDescription(cau.getCau_des());
					alCausali.add(si);
					suggestions += "," + cau.getCau_des();
				}
				cbxCausali = new ComboBoxRch();
				cbxCausali.setState("");
				cbxCausali.setSelectItems(alCausali);
				cbxCausali.setWidth("200");
				if (suggestions.length() > 1)
					cbxCausali.setSuggestions(suggestions.substring(1));
				else
					cbxCausali.setSuggestions("");
			}		
			/*
			 * Caricare i dati per la combo delle destinazioni funzionali
			 */
			List<DestinazioneFunzionaleBean> lstDestFunz = destinazioneFunzionaleService.getListaDestinazioniFunzionali();
			if (lstDestFunz != null){
				logger.info("[numero destinazioni funzionali caricate:] " + lstDestFunz.size());
				alDestFunz = new ArrayList<SelectItem>();
				Iterator<DestinazioneFunzionaleBean> itDF = lstDestFunz.iterator();
				SelectItem si = null;
				String suggestions = "";
				si = new SelectItem();
				si.setValue(emptyComboItem);
				si.setLabel(emptyComboItem);
				si.setDescription(emptyComboItem);
				alDestFunz.add(si);
				suggestions += "," + emptyComboItem;
				while(itDF.hasNext()){
					si = new SelectItem();
					DestinazioneFunzionaleBean df = itDF.next();
					si.setValue(df.getCodut());
					si.setLabel(df.getDescrizione());
					si.setDescription(df.getDescrizione());
					alDestFunz.add(si);
					if (df.getDescrizione() != null && !df.getDescrizione().trim().equalsIgnoreCase("") && df.getCodut().trim().equalsIgnoreCase(df.getDescrizione().trim()))
						suggestions += "," + df.getCodut();
					else if (df.getDescrizione() != null && !df.getDescrizione().trim().equalsIgnoreCase("") && !df.getCodut().trim().equalsIgnoreCase(df.getDescrizione().trim()))
						suggestions += "," + df.getCodut() + "-" + df.getDescrizione();
					else
						suggestions += "," + df.getCodut();
						
				}
				cbxDestinazioniFunzionali = new ComboBoxRch();
				cbxDestinazioniFunzionali.setState("");
				cbxDestinazioniFunzionali.setSelectItems(alDestFunz);
				cbxDestinazioniFunzionali.setWidth("200");
				if (suggestions.length() > 1)
					cbxDestinazioniFunzionali.setSuggestions(suggestions.substring(1));
				else
					cbxDestinazioniFunzionali.setSuggestions("");
			}
			/*
			 * Imposto gli oggetti calendar per le date
			 */
			txtDataVariazioneDal = new CalendarBoxRch();
			txtDataVariazioneAl = new CalendarBoxRch();
			txtDataRegistrazioneDal = new CalendarBoxRch();
			txtDataRegistrazioneAl = new CalendarBoxRch();
			txtDicCognome = new SuggestionBoxRch();
			txtProCognome = new SuggestionBoxRch();
			txtIndirizzo = new SuggestionBoxRch();

			cbxStatoIstruttoria = new ComboBoxRch();
			ArrayList<SelectItem> alStati = new ArrayList<SelectItem>();
			alStati.add(new SelectItem(emptyComboItem, emptyComboItem));
			alStati.add(new SelectItem("NON ESAMINATA", "NON ESAMINATA"));
			alStati.add(new SelectItem("PRESA IN CARICO", "PRESA IN CARICO"));
			alStati.add(new SelectItem("CHIUSA", "CHIUSA"));
			String suggestionsStati = "Tutti, NON ESAMINATA, PRESA IN CARICO, CHIUSA";
			cbxStatoIstruttoria.setState("");
			cbxStatoIstruttoria.setSelectItems(alStati);

			Hashtable htQry = new Hashtable();
			//htQry.put("queryString", "select distinct C.codice from CategoriaBean as C order by C.codice");
			htQry.put("queryName", "getListaCategorie");
			List<CategoriaBean> lstCategorie = categoriaService.getList(htQry);
			ArrayList<SelectItem> alCategorie = null;
			cbxCategorie = new ComboBoxRch();
			cbxCategorie.setState("");
			cbxCategorie.setWidth("200");
			if (lstCategorie != null){
				logger.info("[numero categorie caricate:] " + lstCategorie.size());
				alCategorie = new ArrayList<SelectItem>();
				Iterator itCat = lstCategorie.iterator();
				SelectItem si = null;
				String suggestions = "";
				si = new SelectItem();
				si.setValue(emptyComboItem);
				si.setLabel(emptyComboItem);
				si.setDescription(emptyComboItem);
				alCategorie.add(si);
				suggestions += "," + emptyComboItem;
				while(itCat.hasNext()){
					si = new SelectItem();
					String cat = (String)itCat.next();
					si.setValue(cat);
					si.setLabel(cat);
					si.setDescription(cat);
					alCategorie.add(si);
					suggestions += "," + cat;
				}
				cbxCategorie.setSelectItems(alCategorie);
				if (suggestions.length() > 1)
					cbxCategorie.setSuggestions(suggestions.substring(1));
				else
					cbxCategorie.setSuggestions("");
			}
			/*
			 * Caricare i dati per l'anno di fornitura
			 */
			htQry = new Hashtable();
			htQry.put("queryName", "getAnniFornitura");
			List<DatiGeneraliBean> lstDatiGenerali = datiGeneraliService.getList(htQry);
			ArrayList<SelectItem> alDatiGenerali = null;
			cbxAnniFornitura = new ComboBoxRch();
			cbxAnniFornitura.setWidth("200");
			cbxAnniFornitura.setState("");
			if (lstDatiGenerali != null){
				logger.info("[numero anni fornitura caricati:] " + lstDatiGenerali.size());
				alDatiGenerali = new ArrayList<SelectItem>();
				Iterator itDg = lstDatiGenerali.iterator();
				SelectItem si = null;
				String suggestions = "";
				si = new SelectItem();
				si.setValue(emptyComboItem);
				si.setLabel(emptyComboItem);
				si.setDescription(emptyComboItem);
				alDatiGenerali.add(si);
				suggestions += "," + emptyComboItem;
				while(itDg.hasNext()){
					si = new SelectItem();
					String dataFornitura = (String)itDg.next();
					si.setValue(dataFornitura);
					si.setLabel(dataFornitura);
					si.setDescription(dataFornitura);
					alDatiGenerali.add(si);
					suggestions += "," + dataFornitura;
				}
				cbxAnniFornitura.setSelectItems(alDatiGenerali);
				if (suggestions.length() > 1)
					cbxAnniFornitura.setSuggestions(suggestions.substring(1));
				else
					cbxAnniFornitura.setSuggestions("");
			}
			/*
			 * 
			 * Combobox Mesi
			 */
			ArrayList<SelectItem> alMesi = null;
			SelectItem si = null;
			String suggestions = "";
			/*si = new SelectItem();
			si.setValue(emptyComboItem);
			si.setLabel(emptyComboItem);
			si.setDescription(emptyComboItem);
			alMesi.add(si);*/
			
			suggestions += "," + emptyComboItem;
			cbxMesiFornitura = new ComboBoxRch();
			cbxMesiFornitura.setSuggestions(suggestions);
			cbxMesiFornitura.setState("");
			
			this.disableMesi=true;
			
			/*
			 * Combobox operatori
			 */
			cbxOpeNumProtocollo = new ComboBoxRch();
			cbxOpeNumProtocollo.setSuggestions("uguale, contiene");
			cbxOpeNumProtocollo.setState("uguale");
			
			cbxOpeDataVariazioneDal = new ComboBoxRch();
			cbxOpeDataVariazioneDal.setSuggestions("mag. uguale, min. uguale");
			cbxOpeDataVariazioneDal.setState("mag. uguale");

			cbxOpeDataVariazioneAl = new ComboBoxRch();
			cbxOpeDataVariazioneAl.setSuggestions("mag. uguale, min. uguale");
			cbxOpeDataVariazioneAl.setState("min. uguale");

			cbxOpeDataRegistrazioneDal = new ComboBoxRch();
			cbxOpeDataRegistrazioneDal.setSuggestions("mag. uguale, min. uguale");
			cbxOpeDataRegistrazioneDal.setState("mag. uguale");
			
			cbxOpeDataRegistrazioneAl = new ComboBoxRch();
			cbxOpeDataRegistrazioneAl.setSuggestions("mag. uguale, min. uguale");
			cbxOpeDataRegistrazioneAl.setState("min. uguale");
			
			cbxOpeDicCognome = new ComboBoxRch();
			cbxOpeDicCognome.setSuggestions("uguale, contiene");
			cbxOpeDicCognome.setState("contiene");

			cbxOpeDicNome = new ComboBoxRch();
			cbxOpeDicNome.setSuggestions("uguale, contiene");
			cbxOpeDicNome.setState("contiene");

			cbxOpeProCognome = new ComboBoxRch();
			cbxOpeProCognome.setSuggestions("uguale, contiene");
			cbxOpeProCognome.setState("contiene");

			cbxOpeProNome = new ComboBoxRch();
			cbxOpeProNome.setSuggestions("uguale, contiene");
			cbxOpeProNome.setState("contiene");

			cbxOpeIndirizzo = new ComboBoxRch();
			cbxOpeIndirizzo.setSuggestions("uguale, contiene");
			cbxOpeIndirizzo.setState("contiene");
			
			/*
			 * Log example
			 * catch (Exception e) {
	         *   logger.error("[Errore in SceltaComuneBean] " + e.getMessage());
	         *   FacesUtility.addMessage(ctx, "error.sceltaComune.listaComuni", FacesMessage.SEVERITY_ERROR);
	         * }
			 */
			this.outLabel = new HtmlOutputLabel();			
		}else{
			esito = "failure";
		}

		return esito;
	}//-------------------------------------------------------------------------
	
	
	public void getMesiFornituraByAnno(){
		
		Hashtable htQry = new Hashtable();
		
		String suggestionsMesi=emptyComboItem+ "," ;
		
		String anno= this.cbxAnniFornitura.getState();
		if (anno != null && !anno.equals("") && !anno.equals(this.emptyComboItem)){
		
		Properties prop = SqlHandler.loadProperties(propName);
		String sql = prop.getProperty("qryMesiFornituraByAnno");
		
		String condizioni=" '"+ anno +"' ";
		
			sql +=  condizioni;
			
			sql += "order by to_char(DG.fornitura, 'mm')  ";

			htQry.put("queryString", sql);
			
			List lstObj = bodLogicService.getList(htQry);
			
			if (lstObj != null && lstObj.size()>0){
				for (int i=0; i< lstObj.size(); i++){
				String codMese= (String)lstObj.get(i);
				String mese= Schiavo.fromCodeToDescr(codMese, Info.htMesi);
				
				suggestionsMesi= suggestionsMesi+ mese+ ",";
				}
		}
			cbxMesiFornitura.setSuggestions(suggestionsMesi);
			this.disableMesi=false;
		}else{
			this.disableMesi=true;
		}
	}

	public CausaleService getCausaleService() {
		return causaleService;
	}

	public void setCausaleService(CausaleService causaleService) {
		this.causaleService = causaleService;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		RicercaFiltroBck.logger = logger;
	}

	public ArrayList<SelectItem> getAlCausali() {
		return alCausali;
	}

	public void setAlCausali(ArrayList<SelectItem> alCausali) {
		this.alCausali = alCausali;
	}

	public String getSelCausale() {
		return selCausale;
	}

	public void setSelCausale(String selCausale) {
		this.selCausale = selCausale;
	}

	public ComboBoxRch getCbxCausali() {
		return cbxCausali;
	}

	public void setCbxCausali(ComboBoxRch cbxCausali) {
		this.cbxCausali = cbxCausali;
	}

	public SuggestionBoxRch getTxtDicCognome() {
		return txtDicCognome;
	}

	public void setTxtDicCognome(SuggestionBoxRch txtDicCognome) {
		this.txtDicCognome = txtDicCognome;
	}
	
	public void actionDic(){
		//logger.info("Dichiarante Selezionato: " + txtDicCognome.getProperty2());
		logger.info("Dichiarante Selezionato: " + txtDicCognome.getProperty2());
		//List<DichiaranteBean> lstDichiarantiCognomi = dichiaranteService.getListDicCognomi("");
		//if (lstDichiarantiCognomi != null){
			
		//}
	}//-------------------------------------------------------------------------
	
	public void actionPro(){
		//logger.info("Professionista Selezionato: " + txtProCognome.getProperty2());
		logger.info("Professionista Selezionato: " + txtProCognome.getProperty2());
		//List<DichiaranteBean> lstProfessionistiCognomi = dichiaranteService.getListProCognomi("");
		//if (lstProfessionistiCognomi != null){
			
		//}
	}//-------------------------------------------------------------------------

	public void actionInd(){
		//logger.info("Indirizzo Selezionato: " + txtIndirizzo.getProperty2());
		logger.info("Indirizzo Selezionato: " + txtIndirizzo.getProperty2());
		//List<DichiaranteBean> lstProfessionistiCognomi = dichiaranteService.getListProCognomi("");
		//if (lstProfessionistiCognomi != null){
			
		//}
	}//-------------------------------------------------------------------------

	public List<DichiaranteBean> autocompleteDic(Object suggest) {
		String esito = "ricercaFiltroBck.autocompleteDic";
		
		String pref = "";
		if (suggest != null)
			pref = ((String)suggest).toUpperCase();
		/*
		 * Il parametro della query è soggetto alla clausola LIKE, quindi si deve 
		 * concatenare il simbolo % alla stringa ricercata
		 */
       	List<DichiaranteBean> lstSuggestResult = new ArrayList<DichiaranteBean>();
		if (pref != null && !pref.equalsIgnoreCase("NULL")){
			logger.info("Pref: " + pref);
			//logger.info("Pref: " + pref);
			//if (pref.length() >= 3 && pref.length() > dicPrefixLenght){
			if (pref.length() >= 3){
		       	List<DichiaranteBean> lstDichiarantiCognomi = dichiaranteService.getListDicCognomi(pref + "%");
		        Iterator<DichiaranteBean> iterator = lstDichiarantiCognomi.iterator();
		        Hashtable<String, DichiaranteBean> htDistinct = new Hashtable<String, DichiaranteBean>();
		        while (iterator.hasNext()) {
		        	DichiaranteBean elem = ((DichiaranteBean) iterator.next());
		        	String elemento = elem.getDic_cognome().toLowerCase() + " " + elem.getDic_nome().toLowerCase();
		            if ((elemento != null && elemento.indexOf(pref.toLowerCase()) == 0) || "".equals(pref)){
		            	if (!htDistinct.containsKey(elemento)){
			            	lstSuggestResult.add(elem);
			            	htDistinct.put(elemento, elem);
		            	}
		            }
		        }
			}
			dicPrefixLenght = pref.length();
		}
        return lstSuggestResult;
    }//-------------------------------------------------------------------------
	
	public List<DichiaranteBean> autocompletePro(Object suggest) {
		String esito = "ricercaFiltroBck.autocompletePro";
		
		String pref = "";
		if (suggest != null)
			pref = ((String)suggest).toUpperCase();
		/*
		 * Il parametro della query è soggetto alla clausola LIKE, quindi si deve 
		 * concatenare il simbolo % alla stringa ricercata
		 */
		List<DichiaranteBean> lstSuggestResult = new ArrayList<DichiaranteBean>();
		logger.info("Pref: " + pref);
		//logger.info("Pref: " + pref);
		
       	if (pref != null && !pref.equalsIgnoreCase("NULL")){
			//logger.info("Pref: " + pref);
			logger.info("Pref: " + pref);
			//if (pref.length() >= 3 && pref.length() > proPrefixLenght){
			if (pref.length() >= 3){
		       	List<DichiaranteBean> lstProfessionistiCognomi = dichiaranteService.getListProCognomi(pref + "%");
		        Iterator<DichiaranteBean> iterator = lstProfessionistiCognomi.iterator();
		        Hashtable<String, DichiaranteBean> htDistinct = new Hashtable<String, DichiaranteBean>();
		        while (iterator.hasNext()) {
		        	DichiaranteBean elem = ((DichiaranteBean) iterator.next());
		        	String elemento = elem.getTec_cognome().toLowerCase() + " " + elem.getTec_nome().toLowerCase();
		            if ((elemento != null && elemento.indexOf(pref.toLowerCase()) == 0) || "".equals(pref)){
		            	if (!htDistinct.containsKey(elemento)){
			            	lstSuggestResult.add(elem);
			            	htDistinct.put(elemento, elem);
		            	}
		            }
		        }
			}
			proPrefixLenght = pref.length();			
       	}
        return lstSuggestResult;
    }//-------------------------------------------------------------------------
	
	public List<UiuBean> autocompleteInd(Object suggest) {
		String esito = "ricercaFiltroBck.autocompleteInd";
		
		String pref = "";
		if (suggest != null)
			pref = ((String)suggest).toUpperCase();
		/*
		 * Il parametro della query è soggetto alla clausola LIKE, quindi si deve 
		 * concatenare il simbolo % alla stringa ricercata
		 */
		List<UiuBean> lstSuggestResult = new ArrayList<UiuBean>();
		
		//logger.info("Pref: " + pref);
		logger.info("Pref: " + pref);
       	if (pref != null && !pref.equalsIgnoreCase("NULL")){
			//logger.info("Pref: " + pref);
			logger.info("Pref: " + pref);
			//if (pref.length() >= 7 && pref.length() > indPrefixLenght){
			if (pref.length() >= 7){
		       	List<UiuBean> lstIndirizzi = uiuService.getListaIndirizzi("%" + pref + "%");
		        Iterator iterator = lstIndirizzi.iterator();
		        while (iterator.hasNext()) {
		        	Object[] elem = (Object[])iterator.next();
		        	UiuBean ib = new UiuBean();
		        	//logger.info(elem[0] + " " + elem[1]);
		        	if (elem != null && elem[0] != null){
		        		ib.setIndir_toponimo(elem[0].toString());
		        		if (elem[1] != null)
		        			ib.setIndir_nciv_uno(elem[1].toString());
		        		else
		        			ib.setIndir_nciv_uno("");
		        		lstSuggestResult.add(ib);
		        	}
		        }
			}
			indPrefixLenght = pref.length();			
       	}
        return lstSuggestResult;
    }//-------------------------------------------------------------------------
	
	public void showDetail(){
		logger.info("FILTRO BCK");
	}

	public HtmlInputText getHtmlDicCognome() {
		return htmlDicCognome;
	}

	public void setHtmlDicCognome(HtmlInputText htmlDicCognome) {
		this.htmlDicCognome = htmlDicCognome;
	}

	public HtmlInputText getHtmlProCognome() {
		return htmlProCognome;
	}

	public void setHtmlProCognome(HtmlInputText htmlProCognome) {
		this.htmlProCognome = htmlProCognome;
	}

	public Integer getNumberOfRows() {
		return numberOfRows;
	}

	public void setNumberOfRows(Integer numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	public Boolean getIsAutorizzatoRicerca() {
		return isAutorizzatoRicerca;
	}

	public void setIsAutorizzatoRicerca(Boolean isAutorizzatoRicerca) {
		this.isAutorizzatoRicerca = isAutorizzatoRicerca;
	}

	public ComboBoxRch getCbxMesiFornitura() {
		return cbxMesiFornitura;
	}

	public void setCbxMesiFornitura(ComboBoxRch cbxMesiFornitura) {
		this.cbxMesiFornitura = cbxMesiFornitura;
	}

	public Boolean getDisableMesi() {
		return disableMesi;
	}

	public void setDisableMesi(Boolean disableMesi) {
		this.disableMesi = disableMesi;
	}

	public ArrayList<SelectItem> getAlDestFunz() {
		return alDestFunz;
	}

	public void setAlDestFunz(ArrayList<SelectItem> alDestFunz) {
		this.alDestFunz = alDestFunz;
	}

	public ArrayList<SelectItem> getAlCollaudati() {
		return alCollaudati;
	}

	public void setAlCollaudati(ArrayList<SelectItem> alCollaudati) {
		this.alCollaudati = alCollaudati;
	}

	public ArrayList<SelectItem> getAlCollaudiEsiti() {
		return alCollaudiEsiti;
	}

	public void setAlCollaudiEsiti(ArrayList<SelectItem> alCollaudiEsiti) {
		this.alCollaudiEsiti = alCollaudiEsiti;
	}

	public int getDicPrefixLenght() {
		return dicPrefixLenght;
	}

	public void setDicPrefixLenght(int dicPrefixLenght) {
		this.dicPrefixLenght = dicPrefixLenght;
	}

	public int getProPrefixLenght() {
		return proPrefixLenght;
	}

	public void setProPrefixLenght(int proPrefixLenght) {
		this.proPrefixLenght = proPrefixLenght;
	}

	public int getIndPrefixLenght() {
		return indPrefixLenght;
	}

	public void setIndPrefixLenght(int indPrefixLenght) {
		this.indPrefixLenght = indPrefixLenght;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCollaudato() {
		return collaudato;
	}

	public void setCollaudato(String collaudato) {
		this.collaudato = collaudato;
	}

	public String getCollaudoEsito() {
		return collaudoEsito;
	}

	public void setCollaudoEsito(String collaudoEsito) {
		this.collaudoEsito = collaudoEsito;
	}

	public String[] getIncoerenzaUiu() {
		return incoerenzaUiu;
	}

	public void setIncoerenzaUiu(String[] incoerenzaUiu) {
		this.incoerenzaUiu = incoerenzaUiu;
	}

	public static String getEmptycomboitem() {
		return emptyComboItem;
	}

	public String getCbxCoerenza() {
		return cbxCoerenza;
	}

	public void setCbxCoerenza(String cbxCoerenza) {
		this.cbxCoerenza = cbxCoerenza;
	}

	public String getLblValoreMedio() {
		return lblValoreMedio;
	}

	public void setLblValoreMedio(String lblValoreMedio) {
		this.lblValoreMedio = lblValoreMedio;
	}

	public String getTxtRapportoVCRV() {
		return txtRapportoVCRV;
	}

	public void setTxtRapportoVCRV(String txtRapportoVCRV) {
		this.txtRapportoVCRV = txtRapportoVCRV;
	}

	public String getTxtScostamento() {
		return txtScostamento;
	}

	public void setTxtScostamento(String txtScostamento) {
		this.txtScostamento = txtScostamento;
	}

	
}
