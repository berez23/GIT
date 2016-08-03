package it.bod.business.service.impl;

import java.math.BigDecimal;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.bod.application.beans.DocfaClasseBean;
import it.bod.application.beans.DocfaValoriBean;
import it.bod.application.common.FilterItem;
import it.bod.application.common.MasterClass;
import it.bod.application.common.MasterItem;
import it.bod.business.service.BodLogicService;
import it.bod.persistence.dao.BodLogicDao;
import it.doro.tools.Character;
import it.persistance.common.SqlHandler;

@Service
@Transactional
public class BodLogicServiceImpl extends MasterClass implements BodLogicService {

	private static final long serialVersionUID = 1938947097408270205L;
	
	@Autowired
	private BodLogicDao dao = null;

	public BodLogicServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	public BodLogicDao getDao() {
		return dao;
	}

	public void setDao(BodLogicDao dao) {
		this.dao = dao;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public List getList(Hashtable htQry) {
		List lst = dao.getList(htQry);
		return lst;
	}
	public List getListCaronte(Hashtable htQry) {
		List lst = dao.getListCaronte(htQry);
		return lst;
	}
	

	public List getList(Hashtable htQry, Class cls) {
		List lst = dao.getList(htQry, cls);
		return lst;
	}

	public List getListCaronte(Hashtable htQry, Class cls) {
		List lst = dao.getListCaronte(htQry, cls);
		return lst;
	}
	public MasterItem getItemById(Long id, Class cls){
		MasterItem mi = (MasterItem)dao.getItemById(id, cls);
		return mi;
	}

	public Long addItem(MasterItem item, Class cls) {
		Long id = (Long)dao.addItem(item, cls);
		return id;
	}
	
	public Long addAndFlushItem(MasterItem item, Class cls) {
		Long id = (Long)dao.addAndFlushItem(item, cls);
		return id;
	}

	public MasterItem updItem(MasterItem mi, Class cls) {
		MasterItem item = (MasterItem)dao.updItem(mi, cls);
		return item;
	}

	public void delItem(MasterItem sheet, Class cls) {
		dao.delItem(sheet, cls);		
	}
	
	public List<DocfaClasseBean> getListaClassiComp(String zc, String categoriaEdilizia, String tipologiaIntervento, Hashtable hashClassiMax, String flgAscensore, Double consistenza, Double tariffa){
		
		List<DocfaClasseBean> listaClassi= new ArrayList<DocfaClasseBean>();
		
		Properties prop = SqlHandler.loadProperties(propName);
		String sql = prop.getProperty("qryDocfaClassiCompatibili");
		sql = sql + " where ";
		if (zc != null && !zc.equals("") ){
		sql = sql + " zona= '"+ zc + "' and ";
		}
		//sql += sql + " and tariffa_euro< 1.1*"+ tariffa;
		//sql += sql + " and tariffa_euro> 0.9*"+ tariffa;
	if (categoriaEdilizia.equals("1"))
			sql =sql + "  categoria IN ('A01','A02','A07','A08')";
		else if (categoriaEdilizia.equals("2"))
			sql = sql + "  categoria IN ('A03','A04')";
		
		Hashtable htQry = new Hashtable();
		htQry.put("queryString", sql);
		
		
		List lstObj = this.getList(htQry);
		
		
			if (lstObj != null && lstObj.size()>0){
				DocfaClasseBean dcb = null;
				
				for (int i=0; i<lstObj.size(); i++){
					
							Object[] objs = (Object[])lstObj.get(i);
					
					String categoria=(String)objs[1];
					String classe=(String)objs[2];
					BigDecimal tariffaEuro= (BigDecimal)objs[4];
					
									
				
					//se nuova costruzione e abitazioni economiche, allora scarto categoria A4
					if (tipologiaIntervento!= null && tipologiaIntervento.equals("1") &&  categoriaEdilizia != null && categoriaEdilizia.equals("2")){
						if (categoria != null && categoria.equals("A04"))
							continue;
					}
					
					//se ristrutturazione e abitazioni economiche e flgAscensore =S , allora scarto categoria A4
					if (tipologiaIntervento!= null && tipologiaIntervento.equals("2") &&  categoriaEdilizia != null && categoriaEdilizia.equals("2") && flgAscensore != null && flgAscensore.equals("SI")){
						if (categoria != null && categoria.equals("A04"))
							continue;
					}
					
					
					//se ristrutturazione scartare le categorie per cui  la  classe è minore di quella massima per la categoria
					if (tipologiaIntervento!= null && tipologiaIntervento.equals("2")){
						/*if (hashClassiMax.get(categoria)== null ){
							continue;
						}*/
						 if (hashClassiMax!= null && hashClassiMax.get(categoria)!= null && !hashClassiMax.get(categoria).equals("")){
							if (  Integer.valueOf(classe).intValue() < Integer.valueOf((String)hashClassiMax.get(categoria)).intValue())
								continue;
						}
					}
				
					
					double tariffaEuroD= 0;
					double tariffaCalc=0;
					if (tariffaEuro!= null){
						tariffaEuroD= tariffaEuro.doubleValue();
					}
					if (tariffa!= null)
							tariffaCalc= tariffa.doubleValue();
					
					//se sono nel caso di nuova costruzione verifico  che la tariffa sia compresa tra il 90% e il 110% della tariffa per vano calcolata
					if (tipologiaIntervento!= null && tipologiaIntervento.equals("1")){
						
						if (tariffaEuroD> (tariffaCalc * 0.9) && tariffaEuroD< (tariffaCalc * 1.1)  ){
							
							dcb = new DocfaClasseBean();
							
							dcb.setZona(((String)objs[0]));
							dcb.setCategoria(categoria);
							dcb.setClasse((classe));
							
							dcb.setTariffaEuro(tariffaEuro);
							if (tariffaEuro!= null){
								double tariffaPerVanoD= tariffaEuro.doubleValue();
								
								if (consistenza!= null){
									double consistenzaD= consistenza.doubleValue();
								
									double renditaComplessivaD= tariffaPerVanoD*consistenzaD;
									 
									//BigDecimal renditaComplessiva= new BigDecimal(renditaComplessivaD);
									Double renditaComplessiva = new Double(renditaComplessivaD);
									
									//String renditaComplessivaS=nf.format(renditaComplessivaD);
									
									dcb.setRenditaComplessiva(renditaComplessiva);
								}
							}
							
							listaClassi.add(dcb);	
						}
					}
					//se sono nel caso di ristrutturazione verifico  che la tariffa sia compresa tra il 90% e il 110% della tariffa per vano calcolata ; oppure, se la classe è = classeMax per categoria, allora la tariffa può essere anche superiore al 110%
					else if (tipologiaIntervento!= null && tipologiaIntervento.equals("2")){
						
						//if (hashClassiMax.get(categoria) != null ){
						if ((tariffaEuroD> (tariffaCalc * 0.9) && tariffaEuroD< (tariffaCalc * 1.1)  ) || (tariffaEuroD> (tariffaCalc * 1.1)   &&  hashClassiMax!= null && hashClassiMax.get(categoria)!= null && (Integer.valueOf(classe).intValue() == Integer.valueOf((String)hashClassiMax.get(categoria)).intValue()) )){
													
								dcb = new DocfaClasseBean();
								
								dcb.setZona(((String)objs[0]));
								dcb.setCategoria(categoria);
								dcb.setClasse((classe));
								
								dcb.setTariffaEuro(tariffaEuro);
								if (tariffaEuro!= null){
									double tariffaPerVanoD= tariffaEuro.doubleValue();
									
									if (consistenza!= null){
										double consistenzaD= consistenza.doubleValue();
									
										double renditaComplessivaD= tariffaPerVanoD*consistenzaD;
										//BigDecimal renditaComplessiva= new BigDecimal(renditaComplessivaD);
										Double renditaComplessiva = new Double(renditaComplessivaD);
										
										//String renditaComplessivaS=nf.format(renditaComplessivaD);
										
										dcb.setRenditaComplessiva(renditaComplessiva);
									}
								}
								
								listaClassi.add(dcb);	
							//}
						}
					}
						
			
			
		}
		}
		return listaClassi;
		
	}
	
public List<DocfaClasseBean> getListaClassiComp(String zc, String categoriaEdilizia, Double tariffa){
		
		List<DocfaClasseBean> listaClassi= new ArrayList<DocfaClasseBean>();
		
	
			
		
		
		Properties prop = SqlHandler.loadProperties(propName);
		String sql = prop.getProperty("qryDocfaClassiCompatibili");
		sql = sql + " where ";
		if (zc != null && !zc.equals("") ){
		sql = sql + " zona= '"+ zc + "' and ";
		}
		//sql += sql + " and tariffa_euro< 1.1*"+ tariffa;
		//sql += sql + " and tariffa_euro> 0.9*"+ tariffa;
		if (categoriaEdilizia.equals("1"))
			sql =sql + "  categoria IN ('A01','A08')";
		else if (categoriaEdilizia.equals("2"))
			sql = sql + "  categoria IN ('A02','A07')";
		else if (categoriaEdilizia.equals("3"))
			sql = sql + "  categoria IN ('A03')";
		else if (categoriaEdilizia.equals("4"))
			sql = sql + "  categoria IN ('A04','A05')";
		 
		
		Hashtable htQry = new Hashtable();
		htQry.put("queryString", sql);
		
		
		List lstObj = this.getList(htQry);
		
		
			if (lstObj != null && lstObj.size()>0){
				DocfaClasseBean dcb = null;
				
				for (int i=0; i<lstObj.size(); i++){
					
							Object[] objs = (Object[])lstObj.get(i);
					
					String categoria=(String)objs[1];
					String classe=(String)objs[2];
					BigDecimal tariffaEuro= (BigDecimal)objs[4];
					
					
				
					
					double tariffaEuroD= 0;
					double tariffaCalc=0;
					if (tariffaEuro!= null){
						tariffaEuroD= tariffaEuro.doubleValue();
					}
					if (tariffa!= null)
							tariffaCalc= tariffa.doubleValue();
					
					//verifico  che la tariffa sia compresa tra il 90% e il 110% della tariffa per vano calcolata
					
						if (tariffaEuroD> (tariffaCalc * 0.9) && tariffaEuroD< (tariffaCalc * 1.1)  ){
							
							dcb = new DocfaClasseBean();
							
							dcb.setZona(((String)objs[0]));
							dcb.setCategoria(categoria);
							dcb.setClasse((classe));
							
							dcb.setTariffaEuro(tariffaEuro);
							
							
							listaClassi.add(dcb);	
						}
					
					
			
			
		}
		}
		return listaClassi;
	}


	public BigDecimal getValoreCommercialeMq(String microzona, String tipologiaEdilizia, String stato){
		
		BigDecimal valoreCommerciale= null;
		
		Hashtable htQry = new Hashtable();
		
		ArrayList<FilterItem> aryFilters = new ArrayList<FilterItem>();
		
		FilterItem fi = null;
		
		if (microzona!= null && !microzona.equals("")){
			
			 fi = new FilterItem();
			fi.setOperatore("=");
			fi.setParametro("microzona");
			fi.setAttributo("microzona");
			fi.setValore(microzona);
			aryFilters.add(fi);
		}
			
			fi = new FilterItem();
			fi.setOperatore("=");
			fi.setParametro("tipologiaEdilizia");
			fi.setAttributo("tipologiaEdilizia");
			fi.setValore(tipologiaEdilizia);
			aryFilters.add(fi);
			
			fi = new FilterItem();
			fi.setOperatore("=");
			fi.setParametro("stato");
			fi.setAttributo("stato");
			fi.setValore(stato);
			aryFilters.add(fi);
			
			htQry.put("where", aryFilters);
			

			List lstObj = this.getList(htQry, DocfaValoriBean.class );
			
			if (lstObj!= null && lstObj.size()>0){
				
				DocfaValoriBean docfaValoriBean= (DocfaValoriBean)lstObj.get(0);
				valoreCommerciale=  docfaValoriBean.getValMed();
				}
			
			return valoreCommerciale;
		
	}
	
	public Double getDocfaRapporto(){
		
		Double rapportoD= new Double(0);
		
		Properties prop = SqlHandler.loadProperties(propName);
		String sql = prop.getProperty("qryDocfaRapporto");
		
		Hashtable htQry = new Hashtable();
		htQry.put("queryString", sql);
		
		List<Object> lstRapporti = this.getList(htQry);
		String rapporto = "1";
		if (lstRapporti != null && lstRapporti.size()>0){
			rapporto = Character.checkNullString((String)lstRapporti.get(0));
		}
		
		rapportoD= new Double(rapporto);
		
		return rapportoD;
	}
	
	public  List<SelectItem> getListaZC(String foglio){
		List<SelectItem> listaZC=new ArrayList<SelectItem>();
		
		
		Hashtable htQry = new Hashtable();
		//htQry.put("queryName", "DocfaFogliMicrozonaBean");
		ArrayList<FilterItem> aryFilters = new ArrayList<FilterItem>();
		
		/*
		FilterItem fi = new FilterItem();
		fi.setOperatore("=");
		fi.setParametro("foglio");
		fi.setAttributo("foglio");
		fi.setValore(foglio);
		aryFilters.add(fi);
		
		htQry.put("where", aryFilters);
		*/
		
		
		Properties prop = SqlHandler.loadProperties(propName);
		String sql = prop.getProperty("qryDocfaFogliMicrozona");
		
		if ( foglio != null && !foglio.equals("") ){	
			sql= sql + " where foglio='"+ foglio+"'";
		}
		
		
		htQry.put("queryString", sql);
		
		
		List lstObj = this.getList(htQry);
					
		if (lstObj!= null && lstObj.size()>0){
			
			for (int i=0; i<lstObj.size(); i++){
				
				Object[] objs = (Object[])lstObj.get(i);
				if (objs[0]!= null){
				String zc= (String)objs[0];
				SelectItem si = new SelectItem();
				si.setValue(zc);
				si.setLabel(zc);
				listaZC.add(si);
				}
			}
			
		}
		
	return listaZC;	
	
	}
	
public String getMicrozona(String foglio, String zc){
		

		
		String microzona= "";
		
		
		/*Hashtable htQry = new Hashtable();
		
		ArrayList<FilterItem> aryFilters = new ArrayList<FilterItem>();
		
		if (this.cbxFoglio != null && this.cbxFoglio.getState() != null && !this.cbxFoglio.getState().equals("") ){
			
			FilterItem fi = new FilterItem();
			fi.setOperatore("=");
			fi.setParametro("foglio");
			fi.setAttributo("foglio");
			fi.setValore(foglio);
			aryFilters.add(fi);
		}
		
		if (this.cbxZc != null && this.cbxZc.getState() != null && !this.cbxZc.getState().equals("") ){
			FilterItem fi = new FilterItem();
			fi = new FilterItem();
			fi.setOperatore("=");
			fi.setParametro("zc");
			fi.setAttributo("zc");
			fi.setValore(zc);
			aryFilters.add(fi);
		}	
			htQry.put("where", aryFilters);
			
			*/
		Properties prop = SqlHandler.loadProperties(propName);
		String sql = prop.getProperty("qryDocfaFogliMicrozona");
		
		String sqlCond = "";
		
		if (foglio != null &&  !foglio.equals("") ){	
			sqlCond= sqlCond + " foglio='"+ foglio+"'";
		}
		else{
			sqlCond= sqlCond + " foglio is null ";
		}
		
		
		if (zc != null &&  !zc.equals("")  ){
			sqlCond= sqlCond + " and zc='"+ zc+"'";
		}
			
		sql = sqlCond.length()>0 ? sql + " where " + sqlCond : sql;

		Hashtable htQry = new Hashtable();
		htQry.put("queryString", sql);
		
		
		List lstObj = this.getList(htQry);
					
		if (lstObj!= null && lstObj.size()>0){
			
			for (int i=0; i<lstObj.size(); i++){
				
				Object[] objs = (Object[])lstObj.get(i);
			
									
				String oldMicrozona=(String)objs[2];
				String newMicrozona=(String)objs[3];
							
				microzona= oldMicrozona + " - "+ newMicrozona;
				
				}
				
			}
			return microzona;		
		

		
	}

}
