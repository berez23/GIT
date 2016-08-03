package it.webred.fb.web.bean;

import it.webred.fb.data.model.DmBBene;

import org.primefaces.model.TreeNode;

public class TreeBene {
	protected TreeNode root;
	protected TreeNode selectedNode;
	protected boolean nodeExpanded = true;
	protected String breadcrumb ;
	protected DmBBene beneSelected;
	protected String nomeSelected;
	protected String nomeComplesso;
	protected String expandedIcon;
	protected String collapsedIcon;
	protected Long idComplesso;
	protected boolean beneSelectedHasFigli;

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public TreeNode getRoot() {
		return root;
	}

	public String getBreadcrumb() {
		return breadcrumb;
	}

	public DmBBene getBeneSelected() {
		return beneSelected;
	}

	public String getNomeSelected() {
		return nomeSelected;
	}

	public String getNomeComplesso() {
		return nomeComplesso;
	}

	public Long getIdComplesso() {
		return idComplesso;
	}

	public String getExpandedIcon() {
		return expandedIcon;
	}

	public String getCollapsedIcon() {
		return collapsedIcon;
	}

	public boolean isBeneSelectedHasFigli() {
		return beneSelectedHasFigli;
	}
}

