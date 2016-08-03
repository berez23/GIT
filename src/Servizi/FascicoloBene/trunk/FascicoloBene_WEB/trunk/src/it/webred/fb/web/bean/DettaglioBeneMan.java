package it.webred.fb.web.bean;

import it.webred.dto.utility.KeyValuePairBean;
import it.webred.ejb.utility.ClientUtility;
import it.webred.fb.data.model.DmBBene;
import it.webred.fb.data.model.DmDDoc;
import it.webred.fb.ejb.client.DettaglioBeneSessionBeanRemote;
import it.webred.fb.ejb.dto.BaseDTO;
import it.webred.fb.web.bean.util.UserBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.SelectableDataModel;
import org.primefaces.model.TreeNode;

@ManagedBean
@ViewScoped
public class DettaglioBeneMan extends FascicoloBeneBaseBean implements SelectableDataModel {
	private HashMap<String, String> nodes;
	
	private TreeBene treeBene;
	
	protected String datiPrincName = "datiPrinc";
	protected String localName = "local";
	protected String titoliName = "titoli";
	protected String docName = "doc";
	protected String logistName = "logist";
	protected String terreniName = "terreni";
	protected String cartografiaName = "cartografia";
	
	protected DatiPrincipaliMan datiPrincipaliMan;
	protected LocazioneMan locazioneMan;
	protected DocumentiMan documentiMan;
	protected TitoloMan titoloMan;
	protected TerreniMan terreniMan;
	protected MapMan mapMan;
	
	protected String tabName;
	protected String tabPanelId;
	protected DmBBene root;
	
	private List<BreadcrumbNode> breadCrumb = new ArrayList<BreadcrumbNode>();
	private DmBBene selectedBene;
	
	public void init(){
		if(root==null)
			inizializza();
		
		//RequestContext.getCurrentInstance().update("frmDatiPrincipali:dettaglioBene:datiPrincPanel");
		RequestContext.getCurrentInstance().update("dettaglioBene:frmdatiPrinc:datiPrincPanel");
    }

	public void inizializza(){
		root = (DmBBene) getSession().getAttribute("dettaglioBene");
		treeBene = new TreeBene();
		treeBene.beneSelected = root;
		
		initializeBreadcrumb();
		initializeTree(treeBene.beneSelected);
		initializeDatiPrincTab(treeBene.beneSelected);
	
	}
	
	public void onTabChange(TabChangeEvent tab) throws Exception {
		tabName = tab.getTab().getId();
		
		String name = "";
		
		if (tabName.equals(datiPrincName + "Tab")){
			initializeDatiPrincTab(treeBene.beneSelected);
			name = datiPrincName;
		}
		
		else if (tabName.equals(localName + "Tab")){
			initializeLocalTab(treeBene.beneSelected);
			name = localName;
		}
		
		else if (tabName.equals(titoliName + "Tab")){
			initializeTitoliTab(treeBene.beneSelected);
			name = titoliName;
		}
		
		else if(tabName.equals(docName + "Tab")){
			initializeDocTab(treeBene.beneSelected);
			name = docName;
		}
		
		else if(tabName.equals(logistName + "Tab")){
			initializeLogistTab(treeBene.beneSelected);
			name = logistName;
		}
		
		else if(tabName.equals(terreniName + "Tab")){
			initializeTerreniTab(treeBene.beneSelected);
			name = terreniName;
		}
		
		else if(tabName.equals(cartografiaName + "Tab")){
			initializeCartografiaTab(treeBene.beneSelected);
			name = cartografiaName;
		}
		
		String clientId = tab.getComponent().getClientId();
		
		tabPanelId = clientId + ":frm"+name+":" + name + "Panel";
		
		RequestContext.getCurrentInstance().update(tabPanelId);
	}

	public DmBBene getBene() {
		return bene;
	}

	public void setBene(DmBBene bene) {
		this.bene = bene;
	}
	
	/*
	private void initializeTree(){
		this.getTreeFromDatabase();
		
		treeBene.root = new DefaultTreeNode("root", null);
		treeBene.root.setExpanded(true);
		treeBene.root.setSelectable(false);
        
        TreeMap<String, TreeNode> treeMap = new TreeMap<String, TreeNode>();
        
        for (String subordinateNodeName : nodes.keySet()) {
            if (subordinateNodeName != null) {
                TreeNode treeNode = new DefaultTreeNode(subordinateNodeName, treeBene.root);
                treeNode.setExpanded(treeBene.nodeExpanded);
                treeMap.put(subordinateNodeName, treeNode);
                
                treeBene.expandedIcon = "ui-icon-folder-open";
                treeBene.collapsedIcon = "ui-icon-folder-collapsed";
                
                if(subordinateNodeName == treeBene.nomeSelected){
                	treeNode.setSelected(true);
                	treeBene.breadcrumb = treeBene.nomeSelected;
                }
            }
        }
       
        for (Map.Entry<String, String> entry : nodes.entrySet()) {
            String subordinateNodeName = entry.getKey();
            String superiorNodeName = entry.getValue();
            if (superiorNodeName != null) {
                setParent(treeMap.get(subordinateNodeName), treeMap.get(superiorNodeName));
            }
        } 
	}*/
	
	private void initializeTree(DmBBene beneSelected){
		this.getTreeFromDatabase(beneSelected);
		
		treeBene.root = new DefaultTreeNode("root", null);
		treeBene.root.setExpanded(true);
		treeBene.root.setSelectable(false);
        
        TreeMap<String, TreeNode> treeMap = new TreeMap<String, TreeNode>();
        
        for (String subordinateNodeName : nodes.keySet()) {
            if (subordinateNodeName != null) {
                TreeNode treeNode = new DefaultTreeNode(subordinateNodeName, treeBene.root);
                treeNode.setExpanded(treeBene.nodeExpanded);
                treeMap.put(subordinateNodeName, treeNode);
                
                treeBene.expandedIcon = "ui-icon-folder-open";
                treeBene.collapsedIcon = "ui-icon-folder-collapsed";
                
                if(subordinateNodeName == treeBene.nomeSelected){
                	treeNode.setSelected(true);
                	treeBene.breadcrumb = treeBene.nomeSelected;
                }
            }
        }
       
        for (Map.Entry<String, String> entry : nodes.entrySet()) {
            String subordinateNodeName = entry.getKey();
            String superiorNodeName = entry.getValue();
            if (superiorNodeName != null) {
                setParent(treeMap.get(subordinateNodeName), treeMap.get(superiorNodeName));
            }
        } 
	}
	
	private void initializeBreadcrumb(){
		//Initilize root
		breadCrumb = new ArrayList<BreadcrumbNode>();
		List<KeyValuePairBean<Boolean, DmBBene>> rootNode = new ArrayList<KeyValuePairBean<Boolean,DmBBene>>() ;
		rootNode.add(new KeyValuePairBean<Boolean, DmBBene>(true, root));
		
		breadCrumb.add(new BreadcrumbNode(rootNode));
		
		if(root.getDmBBenes().size()>0){
			//Initialize SubRoot
			List<KeyValuePairBean<Boolean, DmBBene>> subRootNode = new ArrayList<KeyValuePairBean<Boolean,DmBBene>>() ;
			for (DmBBene it : root.getDmBBenes()) {
				subRootNode.add(new KeyValuePairBean<Boolean, DmBBene>(false, it));
			}
			
			breadCrumb.add(new BreadcrumbNode(subRootNode));
		}
	}
	
	private void addBreadcrumb(DmBBene bs){
		//Initilize root
		List<KeyValuePairBean<Boolean, DmBBene>> beneNode = new ArrayList<KeyValuePairBean<Boolean,DmBBene>>() ;
		beneNode.add(new KeyValuePairBean<Boolean, DmBBene>(true, bs));
		
		breadCrumb.remove(breadCrumb.size()-1);
		breadCrumb.add(new BreadcrumbNode(beneNode));
		
		if(bs.getDmBBenes().size()>0){
			//Initialize SubRoot
			List<KeyValuePairBean<Boolean, DmBBene>> subRootNode = new ArrayList<KeyValuePairBean<Boolean,DmBBene>>() ;
			for (DmBBene it : bs.getDmBBenes()) {
				subRootNode.add(new KeyValuePairBean<Boolean, DmBBene>(false, it));
			}
			
			breadCrumb.add(new BreadcrumbNode(subRootNode));
		}
	}
	
	public String getDescrizioneBene(Long id){
		String descrizione="";
		try {
				
				DettaglioBeneSessionBeanRemote beneService = this.getDettaglioBeneService();
	   			BaseDTO dtoBene = new BaseDTO();
	   			dtoBene.setObj(id);
	   			
	   	        this.fillUserData(dtoBene);
	   		
	   	        descrizione = beneService.getDescrizioneBene(dtoBene);
		} catch (Exception e) {
			addError("general", "dettaglio.error");
			logger.error(e.getMessage(), e);
		}
		return descrizione;
	}
	
/*	private void getTreeFromDatabase(){
		nodes = new HashMap<String, String>();
		
		treeBene.idComplesso = root.getId();
		treeBene.nomeComplesso = root.getChiave();
		treeBene.nomeSelected = root.getChiave();
		treeBene.beneSelectedHasFigli = root.getDmBBenes() != null ? root.getDmBBenes().size() > 0 ? true : false : false;
		
		nodes.put(root.getChiave(), null);

		if (treeBene.beneSelectedHasFigli) {
			builderTree(root);
		}
	}*/
	
	private void getTreeFromDatabase(DmBBene beneSelected){
		nodes = new HashMap<String, String>();
		
		treeBene.idComplesso = beneSelected.getId();
		treeBene.nomeComplesso = beneSelected.getChiave();
		treeBene.nomeSelected = beneSelected.getChiave();
		treeBene.beneSelectedHasFigli = beneSelected.getDmBBenes() != null ? beneSelected.getDmBBenes().size() > 0 ? true : false : false;
		
		nodes.put(beneSelected.getChiave(), null);

		if (treeBene.beneSelectedHasFigli) {
			builderTree(beneSelected);
		}
	}
	
	private void builderTree(DmBBene bene){
		for (DmBBene it : bene.getDmBBenes()) {
			nodes.put(it.getChiave(), it.getChiavePadre());
			//logger.debug(it.getChiave() + "   " + it.getChiavePadre());
			
			//ALBERO CON PADRE E FIGLI (a due livelli)
			//if (it.getDmBBenes().size() > 0) builderTree(it);
		
		}
	}

	private void setParent(TreeNode node, TreeNode parent) {
        if (parent != null) {
            node.getParent().getChildren().remove(node);
            parent.getChildren().add(node);
        }
    }

	public void onNodeSelect(NodeSelectEvent event) {
		String nomeBene = event.getTreeNode().getData().toString();
		DmBBene bene = getBeneByChiave(nomeBene);
		addBreadcrumb(bene);
		gestisciSelezioneNodo(bene);
	}
	
	public void onBreadcrumbSelected(DmBBene bs){
		removeBreadcrumb(bs);
		gestisciSelezioneNodo(bs);
	}
		
	public void onRowBeneSelect(SelectEvent event) {
		String nomeBene = ((DmBBene) event.getObject()).getChiave();
		DmBBene bene = getBeneByChiave(nomeBene);
		addBreadcrumb(bene);
		gestisciSelezioneNodo(bene);
    }
	
	private void gestisciSelezioneNodo(DmBBene b){
		ArrayList<String> temp = new ArrayList<String>();
		
		treeBene.breadcrumb = "";
		treeBene.nomeSelected = b.getChiave();
		treeBene.beneSelected = getBeneByChiave(treeBene.nomeSelected);
		treeBene.beneSelectedHasFigli = treeBene.beneSelected.getDmBBenes() != null ? treeBene.beneSelected.getDmBBenes().size() > 0 ? true : false : false;
		
		temp.add(treeBene.nomeSelected);
		
		initializeTree(treeBene.beneSelected); //Aggiorno l'albero
		
		if(tabName == null){
			initializeDatiPrincTab(treeBene.beneSelected);
			//RequestContext.getCurrentInstance().update("frmDatiPrincipali:dettaglioBene:datiPrincPanel");
			RequestContext.getCurrentInstance().update("dettaglioBene:frmdatiPrinc:datiPrincPanel");
		}
		else{
			if (tabName.equals(datiPrincName + "Tab"))
				initializeDatiPrincTab(treeBene.beneSelected);
			
			else if (tabName.equals(localName + "Tab"))
				initializeLocalTab(treeBene.beneSelected);
			
			else if (tabName.equals(terreniName + "Tab"))
				initializeTerreniTab(treeBene.beneSelected);
			
			else if(tabName.equals(docName + "Tab"))
				initializeDocTab(treeBene.beneSelected);
			
			else if(tabName.equals(titoliName + "Tab"))
				initializeTitoliTab(treeBene.beneSelected);
			
			else if(tabName.equals(cartografiaName + "Tab"))
				initializeCartografiaTab(treeBene.beneSelected);

			RequestContext.getCurrentInstance().update(tabPanelId);
		}
	}
	
	@SuppressWarnings("static-access")
	public DmBBene getBeneByChiave(String chiave){
		 DettaglioBeneSessionBeanRemote beneService;
		 
		try {
			beneService = (DettaglioBeneSessionBeanRemote) 
						ClientUtility.getEjbInterface("FascicoloBene", "FascicoloBene_EJB", "DettaglioBeneSessionBean");
			
			BaseDTO dtoBene = new BaseDTO();
			dtoBene.setObj(chiave);
			
	        this.fillUserData(dtoBene);
			
	        return beneService.getBeneByChiave(dtoBene);
		} catch (Exception e) {
			addError("general", "dettaglio.error");
			logger.error(e.getMessage(), e);
		}
		
		return null;	        
	}
	
	public List<DmBBene> getElemsBreadfromNode(BreadcrumbNode node){
		List<DmBBene> temp = new ArrayList<DmBBene>();
		if(node!=null){
			for (KeyValuePairBean<Boolean, DmBBene> it : node.getNode())
				temp.add(it.getValue());
		}
		return temp;
	}
	
	public boolean renderBreadNode(BreadcrumbNode node){
		if(node!=null){
			for (KeyValuePairBean<Boolean, DmBBene> it : node.getNode()) {
				if(it.getKey() == true)
					return true;
			}
		}
		
		return false;
	}
	
	private void removeBreadcrumb(DmBBene bs){
		int i=breadCrumb.size()-1;
		boolean trovato = false;
		while(i>=0 && !trovato){
			String chiaveCorrente = breadCrumb.get(i).getNode().get(0).getValue().getChiave();
			if(chiaveCorrente.equals(bs.getChiave())){
				BreadcrumbNode bc = breadCrumb.get(i);
				DmBBene b = bc.getNode().get(0).getValue();
				trovato=true;
				this.addBreadcrumb(b);
			}else
				breadCrumb.remove(i);

			i--;
		}
	}
	

//	public void createBreadcrumb(){
//		if(breadCrumb.size() == 1)
//			
//	}
//	public void createBreadcrumb(TreeNode node, ArrayList<String> temp){
//		
//		if(node.getParent().getRowKey() != "root"){
//			temp.add(node.getParent().getData().toString()); 
//			createBreadcrumb(node.getParent(), temp);
//			}
//		else{
//			Collections.reverse(temp);
//			int i = temp.size() - 1;
//			
//			for (String item : temp) {
//				if(i != 0)
//					treeBene.breadcrumb += item + " > ";
//				else
//					treeBene.breadcrumb += item;
//				i--;
//			}
//			return;	
//		}	
//	}
	
	private void initializeCartografiaTab(DmBBene bene) {
		mapMan = (MapMan) getBeanReference("mapMan");
		mapMan.initialize(bene);
	}

	private void initializeTerreniTab(DmBBene bene) {
		terreniMan = (TerreniMan)getBeanReference("terreniMan");
		terreniMan.initialize(bene);
	}

	private void initializeLogistTab(DmBBene bene) {
		// TODO Auto-generated method stub
		
	}

	private void initializeDocTab(DmBBene bene) {
		documentiMan = (DocumentiMan) getBeanReference("documentiMan");
		documentiMan.initialize(bene);
	}

	private void initializeTitoliTab(DmBBene bene) {
		titoloMan = (TitoloMan) getBeanReference("titoloMan");
		titoloMan.initialize(bene);
	}

	private void initializeLocalTab(DmBBene bene) {
		locazioneMan = (LocazioneMan) getBeanReference("locazioneMan");
		locazioneMan.initialize(bene);
	}

	private void initializeDatiPrincTab(DmBBene bene) {
		datiPrincipaliMan = (DatiPrincipaliMan) getBeanReference("datiPrincipaliMan");
		datiPrincipaliMan.initialize(bene);
	}
	
	public TreeBene getTreeBene() {
		return treeBene;
	}

	public DatiPrincipaliMan getDatiPrincipaliMan() {
		return datiPrincipaliMan;
	}
	
	public LocazioneMan getLocazioneMan() {
		return locazioneMan;
	}
	
	public DocumentiMan getDocumentiMan() {
		return documentiMan;
	}

	public TitoloMan getTitoloMan() {
		return titoloMan;
	}

	public void setTitoloMan(TitoloMan titoloMan) {
		this.titoloMan = titoloMan;
	}

	public String getDatiPrincName() {
		return datiPrincName;
	}

	public HashMap<String, String> getNodes() {
		return nodes;
	}

	public String getLocalName() {
		return localName;
	}

	public String getTitoliName() {
		return titoliName;
	}

	public String getDocName() {
		return docName;
	}

	public String getLogistName() {
		return logistName;
	}

	public String getTerreniName() {
		return terreniName;
	}

	public String getCartografiaName() {
		return cartografiaName;
	}

	public String getTabName() {
		return tabName;
	}

	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	public List<BreadcrumbNode> getBreadCrumb() {
		return breadCrumb;
	}

	public DmBBene getSelectedBene() {
		return selectedBene;
	}

	public void setSelectedBene(DmBBene selectedBene) {
		this.selectedBene = selectedBene;
	}

	public BreadcrumbNode getLastBcNode(){
		if(this.breadCrumb!=null && this.breadCrumb.size()>0)
			return this.breadCrumb.get(this.breadCrumb.size()-1);
		else
			return null;
	}
	
	@Override
	public Object getRowData(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getRowKey(Object arg0) {
		return ((DmBBene)arg0).getChiave();
	}

	public void setCartografiaName(String cartografiaName) {
		this.cartografiaName = cartografiaName;
	}

	public MapMan getMapMan() {
		return mapMan;
	}

	public TerreniMan getTerreniMan() {
		return terreniMan;
	}

	public void setTerreniMan(TerreniMan terreniMan) {
		this.terreniMan = terreniMan;
	}
	
}