package it.bod.application.backing;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;


import it.bod.application.beans.BodBean;
import it.bod.application.beans.UiuBean;
import it.bod.application.common.FilterItem;
import it.bod.application.common.MasterClass;
import it.bod.application.common.Schiavo;
import it.bod.business.service.UiuService;
import it.doro.tools.TimeControl;
import it.persistance.common.SqlHandler;
import it.doro.tools.Character;

public class RicercaListaBck  extends MasterClass{

	private static final long serialVersionUID = 1703418699864929627L;
	private static Logger logger = Logger.getLogger("it.bod.application.backing.RicercaListaBck");
	
	private ArrayList<BodBean> lstBod = null;
	private String sql = "";
	private BodBean selBodBean = null;
	private Integer currentIndex = null;
	private List<UiuBean> lstUiu = null;
	private List<Object> lstObj = null;
	
	private UiuService uiuService = null;
	
	public RicercaListaBck(){
		super.initializer();
	}//-------------------------------------------------------------------------
	
	public RicercaListaBck(List lstObj){
		this.lstBod = new ArrayList<BodBean>();
		if (lstObj != null && lstObj.size()>0){
			this.lstBod.addAll(Schiavo.setLstBodFromLstObject(lstObj));			
		}
	}//-------------------------------------------------------------------------
	
	public List<UiuBean> getLstUiu() {
		return lstUiu;
	}

	public void setLstUiu(List<UiuBean> lstUiu) {
		this.lstUiu = lstUiu;
	}

	public UiuService getUiuService() {
		return uiuService;
	}

	public void setUiuService(UiuService uiuService) {
		this.uiuService = uiuService;
	}

	public Integer getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(Integer currentRowIndex) {
		this.currentIndex = currentRowIndex;
	}

	public BodBean getSelBodBean() {
		return selBodBean;
	}

	public void setSelBodBean(BodBean selBodBean) {
		this.selBodBean = selBodBean;
	}

	public ArrayList<BodBean> getLstBod() {
		return lstBod;
	}

	public void setLstBod(ArrayList<BodBean> listaBod) {
		this.lstBod = listaBod;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
	
/*
	public void setLstBodFromLstObject(List lstObj) {
		SimpleDateFormat sdf = new SimpleDateFormat ("dd/MM/yyyy");
		this.lstBod = new ArrayList<BodBean>();
		if (lstObj != null && lstObj.size()>0){
			BodBean bb = null;
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
		}
 
	}//-------------------------------------------------------------------------
*/

	public void showDetail(){
		/*
		 * Anche se non servirà per fare la query perché è uno degli oggetti mappati
		 * in hibernate, scrivo la query per chiarezza
		 */
//		String sql = "SELECT " +
//				"u.foglio as fg, u.numero as part, u.subalterno as sub, u.PROP_CATEGORIA as cat, " +
//				"decode (u.tipo_operazione, 'C', 'COSTITUITA', 'V', 'VARIATA', 'S','SOPPRESSA', u.tipo_operazione) as operazione, " +
//				"trim(u.indir_toponimo) || ' ' || u.indir_nciv_uno as indirizzo " +
//				"FROM " +
//				"docfa_uiu u ";
		
		Properties prop = SqlHandler.loadProperties(propName);
		sql = prop.getProperty("qryRicercaLista");
		
		TimeControl tc = new TimeControl();
		Hashtable htQry = new Hashtable();
		ArrayList<FilterItem> aryFilters = new ArrayList<FilterItem>();
		String condizioni = "";
		SimpleDateFormat sdf = new SimpleDateFormat ("dd/MM/yyyy");

		if (selBodBean != null && selBodBean.getFornitura() != null){
			String[] mmAaaa = selBodBean.getFornitura().split("/");
			Timestamp dataFornituraDal = tc.fromFormatStringToTimestampMorning("01/" + mmAaaa[0] + "/" + mmAaaa[1]);
			FilterItem fi = new FilterItem();
			fi.setAttributo("fornitura");
			fi.setOperatore("_DAL_");
			fi.setParametro("fornitura_DAL_");
			fi.setValore(dataFornituraDal);
			aryFilters.add(fi);
			condizioni += "and fornitura = to_date('" + selBodBean.getFornitura() + "', 'MM/yyyy') ";//questa la prendi dal campo fornitura
		}

		if (selBodBean != null && selBodBean.getFornitura() != null){
			String[] mmAaaa = selBodBean.getFornitura().split("/");
			Timestamp dataFornituraAl = tc.fromFormatStringToTimestampEvening("01/" + mmAaaa[0] + "/" + mmAaaa[1]);
			dataFornituraAl = tc.getLastOfMonth(dataFornituraAl);
			FilterItem fi = new FilterItem();
			fi.setAttributo("fornitura");
			fi.setOperatore("_AL_");
			fi.setParametro("fornitura_AL_");
			fi.setValore(dataFornituraAl);
			aryFilters.add(fi);
			//condizioni += "and fornitura = to_date('" + fi.getValore() + "', 'dd/mm/yyyy') ";//questa la prendi dal campo fornitura
		}

		if (selBodBean != null && selBodBean.getProtocollo() != null){
			FilterItem fi = new FilterItem();
			fi.setAttributo("protocollo_reg");
			fi.setOperatore("=");
			fi.setParametro("protocollo_reg");
			fi.setValore(selBodBean.getProtocollo());
			aryFilters.add(fi);
			condizioni += "and protocollo_reg = '" + selBodBean.getProtocollo() + "' ";  //questo lo prendi dal campo protocollo
		}

		if (aryFilters != null && aryFilters.size() > 0){
			sql += " where 1=1 " + condizioni;
			sql += " ORDER BY foglio, numero, subalterno";
			
			htQry.put("queryString", sql);
			htQry.put("where", aryFilters);
			htQry.put("orderBy", "foglio, UIUBEAN.particella, UIUBEAN.subalterno");
			
			//lstUiu = uiuService.getList(htQry, UiuBean.class);
			lstObj = uiuService.getList(htQry);
			
			for (int index=0; index<lstObj.size(); index++){
        		Object[] uiu = (Object[])lstObj.get(index);
			String via= (String)uiu[6];
    		String civico= (String)uiu[7];
    		String viaNew="";
    		String civicoNew="";
    		
    		viaNew=Character.pulisciVia(via);
    		
    		if (civico != null){
        		try{
        			Integer civicoInt= Integer.valueOf(civico);
        			civicoNew= String.valueOf(civicoInt);
        		}catch(Exception e){
        			civicoNew= civico;
        		}
    		}
    		
    		uiu[6]=viaNew;
    		uiu[7]=civicoNew;
			}
		}
			
		if (lstUiu != null)
			logger.info("Numero Uiu: " + lstUiu.size());
			
		
	}//-------------------------------------------------------------------------
/*
	public void selectionChanged() {
		logger.info(selBodBean.getCausale());
    }//-------------------------------------------------------------------------

	
	public void selectionChanged(ActionEvent event) {
        UIComponent comp = event.getComponent();
        Object obj = comp.getParent();
        if (obj instanceof HtmlDataTable) {
                HtmlDataTable table = (HtmlDataTable) obj;
                Object rowData = table.getRowData();
                if (rowData instanceof BodBean) {
                	BodBean selObj = (BodBean) rowData;
                	this.setSelBodBean(selObj);                             
                }                       
        }
        logger.info(selBodBean.getCausale());
    }//-------------------------------------------------------------------------

*/

	public List<Object> getLstObj() {
		return lstObj;
	}

	public void setLstObj(List<Object> lstObj) {
		this.lstObj = lstObj;
	}	

}
