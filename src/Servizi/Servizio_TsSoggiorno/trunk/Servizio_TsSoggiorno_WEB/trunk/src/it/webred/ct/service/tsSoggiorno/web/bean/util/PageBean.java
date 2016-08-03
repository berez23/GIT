package it.webred.ct.service.tsSoggiorno.web.bean.util;

import it.webred.ct.service.tsSoggiorno.web.bean.TsSoggiornoBaseBean;
import it.webred.ct.service.tsSoggiorno.web.bean.anagrafica.SocietaBean;
import it.webred.ct.service.tsSoggiorno.web.bean.anagrafica.StrutturaBean;
import it.webred.ct.service.tsSoggiorno.web.bean.dichiarazione.DichiarazioneBean;
import it.webred.ct.service.tsSoggiorno.web.bean.report.RepRiepilogoBean;
import it.webred.ct.service.tsSoggiorno.web.bean.report.RepVersamentiBean;
import it.webred.ct.service.tsSoggiorno.web.bean.rimbsanz.RimbSanzBean;
import it.webred.ct.service.tsSoggiorno.web.bean.tariffa.TariffaBean;
import it.webred.ct.service.tsSoggiorno.web.dto.DichDTO;
import it.webred.ct.service.tsSoggiorno.web.dto.TreeItem;

import java.util.ArrayList;
import java.util.List;

import org.richfaces.model.TreeNode;
import org.richfaces.model.TreeNodeImpl;

public class PageBean extends TsSoggiornoBaseBean {

	private final static String codNewDich = "NEW_DICH";
	private final static String codListDich = "LIST_DICH";
	private final static String codNewSoc = "NEW_SOC";
	private final static String codListSoc = "LIST_SOC";
	private final static String codNewStr = "NEW_STR";
	private final static String codListStr = "LIST_STR";
	private final static String codPagStr = "PAG_STR";
	private final static String codPagDich = "PAG_DICH";
	private final static String codAnag = "ANAG";
	private final static String codNewTarif = "NEW_TARIF";
	private final static String codListTarif = "LIST_TARIF";
	private final static String codNewRimSan = "NEW_RIMSAN";
	private final static String codListRimSan = "LIST_RIMSAN";	
	private final static String codRiepReport = "RIEP_REPORT";
	private final static String codVersReport = "VERS_REPORT";	
	
	private String pageCenter = "/jsp/protected/content/riepilogo.xhtml";
	private String pageCenterDati = "/jsp/protected/empty.xhtml";

	private String menuValue;
	private String menuType;
	private boolean visDati;
	private boolean visListaPagam;
	private boolean visPagam;
	

	public String getPageCenter() {
		return pageCenter;
	}

	public void setPageCenter(String pageCenter) {
		this.pageCenter = pageCenter;
	}

	public String getPageCenterDati() {
		return pageCenterDati;
	}

	public void setPageCenterDati(String pageCenterDati) {
		this.pageCenterDati = pageCenterDati;
	}

	// MENU
	private TreeNode<TreeItem> dichMenuTree;
	private TreeNode<TreeItem> anaMenuTree;
	private TreeNode<TreeItem> pagMenuTree;
	private TreeNode<TreeItem> tarifMenuTree;
	private TreeNode<TreeItem> docMenuTree;
	private TreeNode<TreeItem> rimsanMenuTree;
	private TreeNode<TreeItem> reportMenuTree;

	public TreeNode<TreeItem> getDichMenuTree() {
		dichMenuTree = new TreeNodeImpl<TreeItem>();
		try {
			TreeNode<TreeItem> richNodeNuovo = new TreeNodeImpl<TreeItem>();
			richNodeNuovo.setData(new TreeItem(codNewDich,"Nuova dichiarazione",null,"/images/new.png"));
			dichMenuTree.addChild(new Integer(1), richNodeNuovo);
			TreeNode<TreeItem> richNodeLista = new TreeNodeImpl<TreeItem>();
			richNodeLista.setData(new TreeItem(codListDich,"Gestisci dichiarazioni","Visualizza, modifica, invia e dettaglio pagamenti","/images/list.png"));
			dichMenuTree.addChild(new Integer(2), richNodeLista);
		} catch (Throwable t) {
			getLogger().error("ERRORE in costruzione dich tree", t);
		}
		return dichMenuTree;
	}
	
	public void setDichMenuTree(TreeNode<TreeItem> dichMenuTree) {
		this.dichMenuTree = dichMenuTree;
	}

	public TreeNode<TreeItem> getAnaMenuTree() {
		anaMenuTree = new TreeNodeImpl<TreeItem>();
		try {
			TreeNode<TreeItem> richNodeNuovo = new TreeNodeImpl<TreeItem>();
			richNodeNuovo.setData(new TreeItem(codNewSoc,"Nuova società",null,"/images/new.png"));
			anaMenuTree.addChild(new Integer(1), richNodeNuovo);
			TreeNode<TreeItem> richNodeLista = new TreeNodeImpl<TreeItem>();
			richNodeLista.setData(new TreeItem(codListSoc,"Gestisci società","Visualizza e modifica","/images/list.png"));
			anaMenuTree.addChild(new Integer(2), richNodeLista);
			richNodeNuovo = new TreeNodeImpl<TreeItem>();
			richNodeNuovo.setData(new TreeItem(codNewStr,"Nuova struttura",null,"/images/new.png"));
			anaMenuTree.addChild(new Integer(3), richNodeNuovo);
			richNodeLista = new TreeNodeImpl<TreeItem>();
			richNodeLista.setData(new TreeItem(codListStr,"Gestisci strutture","Visualizza, modifica e dettaglio pagamenti","/images/list.png"));
			anaMenuTree.addChild(new Integer(4), richNodeLista);
			TreeNode<TreeItem> richNode = new TreeNodeImpl<TreeItem>();
			richNode.setData(new TreeItem(codAnag,"Dati personali",null,"/images/person.png"));
			anaMenuTree.addChild(new Integer(5), richNode);
		} catch (Throwable t) {
			getLogger().error("ERRORE in costruzione ana tree", t);
		}
		return anaMenuTree;
	}

	public void setAnaMenuTree(TreeNode<TreeItem> anaMenuTree) {
		this.anaMenuTree = anaMenuTree;
	}

	public TreeNode<TreeItem> getPagMenuTree() {
		pagMenuTree = new TreeNodeImpl<TreeItem>();
		try {
			TreeNode<TreeItem> richNodeNuovo = new TreeNodeImpl<TreeItem>();
			richNodeNuovo.setData(new TreeItem(codPagDich,"Versamenti per dichiarazione",null,"/images/pay.png"));
			pagMenuTree.addChild(new Integer(1), richNodeNuovo);
			TreeNode<TreeItem> richNodeLista = new TreeNodeImpl<TreeItem>();
			richNodeLista.setData(new TreeItem(codPagStr,"Versamenti per società",null,"/images/pay.png"));
			pagMenuTree.addChild(new Integer(2), richNodeLista);
		} catch (Throwable t) {
			getLogger().error("ERRORE in costruzione pag tree", t);
		}
		return pagMenuTree;
	}

	public void setPagMenuTree(TreeNode<TreeItem> pagMenuTree) {
		this.pagMenuTree = pagMenuTree;
	}
	
	public TreeNode<TreeItem> getTarifMenuTree() {
		tarifMenuTree = new TreeNodeImpl<TreeItem>();
		try {
			TreeNode<TreeItem> richNodeNuovo = new TreeNodeImpl<TreeItem>();
			richNodeNuovo.setData(new TreeItem(codNewTarif,"Nuova tariffa",null,"/images/new.png"));
			tarifMenuTree.addChild(new Integer(1), richNodeNuovo);
			TreeNode<TreeItem> richNodeList = new TreeNodeImpl<TreeItem>();
			richNodeList.setData(new TreeItem(codListTarif,"Gestione tariffe",null,"/images/list.png"));
			tarifMenuTree.addChild(new Integer(2), richNodeList);
		} catch (Throwable t) {
			getLogger().error("ERRORE in costruzione tariffe tree", t);
		}
		return tarifMenuTree;
	}

	public void setTarifMenuTree(TreeNode<TreeItem> tarifMenuTree) {
		this.tarifMenuTree = tarifMenuTree;
	}

	public void setDocMenuTree(TreeNode<TreeItem> docMenuTree) {
		this.docMenuTree = docMenuTree;
	}

	public TreeNode<TreeItem> getRimsanMenuTree() {
		rimsanMenuTree = new TreeNodeImpl<TreeItem>();
		try {
			TreeNode<TreeItem> richNodeNuovo = new TreeNodeImpl<TreeItem>();
			richNodeNuovo.setData(new TreeItem(codNewRimSan,"Nuovo rimborso/sanzione",null,"/images/new.png"));
			rimsanMenuTree.addChild(new Integer(1), richNodeNuovo);
			TreeNode<TreeItem> richNodeLista = new TreeNodeImpl<TreeItem>();
			richNodeLista.setData(new TreeItem(codListRimSan,"Gestione rimborsi/sanzioni",null,"/images/list.png"));
			rimsanMenuTree.addChild(new Integer(2), richNodeLista);
		} catch (Throwable t) {
			getLogger().error("ERRORE in costruzione rimborsi sanzioni tree", t);
		}
		return rimsanMenuTree;
	}

	public void setRimsanMenuTree(TreeNode<TreeItem> rimsanMenuTree) {
		this.rimsanMenuTree = rimsanMenuTree;
	}
	
	public TreeNode<TreeItem> getReportMenuTree() {
		reportMenuTree = new TreeNodeImpl<TreeItem>();
		try {
			TreeNode<TreeItem> richNodeNuovo = new TreeNodeImpl<TreeItem>();
			richNodeNuovo.setData(new TreeItem(codRiepReport,"Report riepilogativo",null,"/images/report.png"));
			reportMenuTree.addChild(new Integer(1), richNodeNuovo);
			TreeNode<TreeItem> richNodeLista = new TreeNodeImpl<TreeItem>();
			richNodeLista.setData(new TreeItem(codVersReport,"Report versamenti",null,"/images/report.png"));
			reportMenuTree.addChild(new Integer(2), richNodeLista);
		} catch (Throwable t) {
			getLogger().error("ERRORE in costruzione report tree", t);
		}
		return reportMenuTree;
	}

	public void setReportMenuTree(TreeNode<TreeItem> reportMenuTree) {
		this.reportMenuTree = reportMenuTree;
	}

	public void doMenuAction() {
		if ("dichiarazioni".equals(menuType)) {
			DichiarazioneBean bean = (DichiarazioneBean) getBeanReference("dichiarazioneBean");
			if (codNewDich.equals(menuValue)) {
				bean.doLoadNewDichiarazione();
				bean.setIdSocieta(null);
				bean.setIdStruttura(null);
				pageCenter = "/jsp/protected/content/dich.xhtml";
			} else {
				pageCenter = "/jsp/protected/content/dichLista.xhtml";
			}
		}
		if ("anagrafica".equals(menuType)) 
		{
			SocietaBean socBean = (SocietaBean) getBeanReference("societaBean");
			StrutturaBean strBean = (StrutturaBean) getBeanReference("strutturaBean");
			if (codNewSoc.equals(menuValue)) {
				pageCenter = "/jsp/protected/content/soc.xhtml";
				socBean.doNewSocieta();
			} else if (codListSoc.equals(menuValue)) {
				socBean.doLoadListaSocieta();
				pageCenter = "/jsp/protected/content/socLista.xhtml";
			} else if (codNewStr.equals(menuValue)) {
				strBean.doNewStruttura();
				pageCenter = "/jsp/protected/content/str.xhtml";
			}else if (codListStr.equals(menuValue)) {
				strBean.doLoadListaStrutture();
				pageCenter = "/jsp/protected/content/strLista.xhtml";
			}else
				pageCenter = "/jsp/protected/content/anagrafica.xhtml";
		}
		if ("pagamenti".equals(menuType)) {
			if (codPagDich.equals(menuValue)) {
				pageCenter = "/jsp/protected/content/dichLista.xhtml";
			} else {
				pageCenter = "/jsp/protected/content/socLista.xhtml";
			}
		}
		if ("tariffe".equals(menuType)) {
			TariffaBean tarBean = (TariffaBean) getBeanReference("tariffaBean");
			if (codNewTarif.equals(menuValue)) {
				tarBean.doNewTariffa();
				pageCenter = "/jsp/protected/content/tariffa.xhtml";
			} else {
				tarBean.doLoadTariffe();
				pageCenter = "/jsp/protected/content/tariffaLista.xhtml";
			}
		}
		if ("rimborsisanzioni".equals(menuType)) {
			RimbSanzBean rsBean = (RimbSanzBean) getBeanReference("rimbsanzBean");
			rsBean.setRimbsanz("R");
			if (codNewRimSan.equals(menuValue)) {
				rsBean.doNewRimbSanz();
				pageCenter = "/jsp/protected/content/rimbsanz.xhtml";
			} else {
				rsBean.doLoadRimbSanz();
				pageCenter = "/jsp/protected/content/rimbsanzLista.xhtml";
			}
		}
		if ("report".equals(menuType)) {
			if (codRiepReport.equals(menuValue)) {
				RepRiepilogoBean reportBean = (RepRiepilogoBean) getBeanReference("repRiepilogoBean");
				reportBean.loadPeriodi();
				pageCenter = "/jsp/protected/content/report/riepilogativo.xhtml";
			} else {
				RepVersamentiBean reportBean = (RepVersamentiBean) getBeanReference("repVersamentiBean");
				reportBean.loadPeriodi();
				pageCenter = "/jsp/protected/content/report/versamenti.xhtml";
			}
		}
	}
	
	public String getMenuValue() {
		return menuValue;
	}

	public void setMenuValue(String menuValue) {
		this.menuValue = menuValue;
	}

	public String getMenuType() {
		return menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	public boolean isVisDati() {
		return visDati;
	}

	public void setVisDati(boolean visDati) {
		this.visDati = visDati;
	}

	public boolean isVisListaPagam() {
		return visListaPagam;
	}

	public void setVisListaPagam(boolean visListaPagam) {
		this.visListaPagam = visListaPagam;
	}

	public boolean isVisPagam() {
		return visPagam;
	}

	public void setVisPagam(boolean visPagam) {
		this.visPagam = visPagam;
	}

}
