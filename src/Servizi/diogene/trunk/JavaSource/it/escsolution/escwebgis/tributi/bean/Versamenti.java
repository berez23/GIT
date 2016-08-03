/*
 * Created on 13-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.tributi.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;
/**
 * @author administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Versamenti extends EscObject implements Serializable{
	
	private String codent;
	private String pk_id_versamenti;
	private String ninc;
	private String sogd;
	private String anno;
	private String ogge;
	private String data_vers;
	private String imp_totale;
	private String imp_terreni_agri;
	private String imp_aree_fabbr;
	private String imp_prima_casa;
	private String imp_fabbr;
	private String imp_detraz;
	private String codfisc;
	private String num_immobili;
	private String tipo_paga;
	private String tipo_denuncia;
	private String ufficio_posta;
	private String num_bollettino;
	private String data_validita;
	private String dati_ori;
	private String tms_agg;
	private String provenienza;
	private String tipo_tributo;
	private String flg_trf;
	
	
	public Versamenti()
	{
		this.codent = "";
		this.pk_id_versamenti = "";
		this.ninc = "";
		this.sogd = "";
		this.anno = "";
		this.ogge = "";
		this.data_vers = "";
		this.imp_totale = "";
		this.imp_terreni_agri = "";
		this.imp_aree_fabbr = "";
		this.imp_prima_casa = "";
		this.imp_fabbr = "";
		this.imp_detraz = "";
		this.codfisc = "";
		this.num_immobili = "";
		this.tipo_paga = "";
		this.tipo_denuncia = "";
		this.ufficio_posta = "";
		this.num_bollettino = "";
		this.data_validita = "";
		this.dati_ori = "";
		this.tms_agg = "";
		this.provenienza = "";
		this.tipo_tributo = "";
		this.flg_trf = "";
	}


	public String getAnno()
	{
		return anno;
	}

	public void setAnno(String anno)
	{
		this.anno = anno;
	}

	public String getCodent()
	{
		return codent;
	}

	public void setCodent(String codent)
	{
		this.codent = codent;
	}

	public String getCodfisc()
	{
		return codfisc;
	}

	public void setCodfisc(String codfisc)
	{
		this.codfisc = codfisc;
	}

	public String getData_validita()
	{
		return data_validita;
	}

	public void setData_validita(String data_validita)
	{
		this.data_validita = data_validita;
	}

	public String getData_vers()
	{
		return data_vers;
	}

	public void setData_vers(String data_vers)
	{
		this.data_vers = data_vers;
	}

	public String getDati_ori()
	{
		return dati_ori;
	}

	public void setDati_ori(String dati_ori)
	{
		this.dati_ori = dati_ori;
	}

	public String getFlg_trf()
	{
		return flg_trf;
	}

	public void setFlg_trf(String flg_trf)
	{
		this.flg_trf = flg_trf;
	}

	public String getImp_aree_fabbr()
	{
		return imp_aree_fabbr;
	}

	public void setImp_aree_fabbr(String imp_aree_fabbr)
	{
		this.imp_aree_fabbr = imp_aree_fabbr;
	}

	public String getImp_detraz()
	{
		return imp_detraz;
	}

	public void setImp_detraz(String imp_detraz)
	{
		this.imp_detraz = imp_detraz;
	}

	public String getImp_fabbr()
	{
		return imp_fabbr;
	}

	public void setImp_fabbr(String imp_fabbr)
	{
		this.imp_fabbr = imp_fabbr;
	}

	public String getImp_prima_casa()
	{
		return imp_prima_casa;
	}

	public void setImp_prima_casa(String imp_prima_casa)
	{
		this.imp_prima_casa = imp_prima_casa;
	}

	public String getImp_terreni_agri()
	{
		return imp_terreni_agri;
	}

	public void setImp_terreni_agri(String imp_terreni_agri)
	{
		this.imp_terreni_agri = imp_terreni_agri;
	}

	public String getImp_totale()
	{
		return imp_totale;
	}

	public void setImp_totale(String imp_totale)
	{
		this.imp_totale = imp_totale;
	}

	public String getNinc()
	{
		return ninc;
	}

	public void setNinc(String ninc)
	{
		this.ninc = ninc;
	}

	public String getNum_bollettino()
	{
		return num_bollettino;
	}

	public void setNum_bollettino(String num_bollettino)
	{
		this.num_bollettino = num_bollettino;
	}

	public String getNum_immobili()
	{
		return num_immobili;
	}

	public void setNum_immobili(String num_immobili)
	{
		this.num_immobili = num_immobili;
	}

	public String getOgge()
	{
		return ogge;
	}

	public void setOgge(String ogge)
	{
		this.ogge = ogge;
	}

	public String getPk_id_versamenti()
	{
		return pk_id_versamenti;
	}

	public void setPk_id_versamenti(String pk_id_versamenti)
	{
		this.pk_id_versamenti = pk_id_versamenti;
	}

	public String getProvenienza()
	{
		return provenienza;
	}

	public void setProvenienza(String provenienza)
	{
		this.provenienza = provenienza;
	}

	public String getSogd()
	{
		return sogd;
	}

	public void setSogd(String sogd)
	{
		this.sogd = sogd;
	}

	public String getTipo_denuncia()
	{
		return tipo_denuncia;
	}

	public void setTipo_denuncia(String tipo_denuncia)
	{
		this.tipo_denuncia = tipo_denuncia;
	}

	public String getTipo_paga()
	{
		return tipo_paga;
	}

	public void setTipo_paga(String tipo_paga)
	{
		this.tipo_paga = tipo_paga;
	}

	public String getTipo_tributo()
	{
		return tipo_tributo;
	}

	public void setTipo_tributo(String tipo_tributo)
	{
		this.tipo_tributo = tipo_tributo;
	}

	public String getTms_agg()
	{
		return tms_agg;
	}

	public void setTms_agg(String tms_agg)
	{
		this.tms_agg = tms_agg;
	}

	public String getUfficio_posta()
	{
		return ufficio_posta;
	}

	public void setUfficio_posta(String ufficio_posta)
	{
		this.ufficio_posta = ufficio_posta;
	}

	
	
	public String getChiave()
	{
	 	return pk_id_versamenti;			
	}
	
	
}
