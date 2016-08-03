package it.webred.fb.web.bean;

import it.webred.dto.utility.KeyValuePairBean;
import it.webred.fb.data.model.DmBBene;

import java.util.ArrayList;
import java.util.List;

public class BreadcrumbNode {
	private List<KeyValuePairBean<Boolean, DmBBene>> node = new ArrayList<KeyValuePairBean<Boolean,DmBBene>>() ;

	public BreadcrumbNode(List<KeyValuePairBean<Boolean, DmBBene>> node) {
		this.node = node;
	}

	public List<KeyValuePairBean<Boolean, DmBBene>> getNode() {
		return node;
	}

	public void setNode(List<KeyValuePairBean<Boolean, DmBBene>> node) {
		this.node = node;
	}
}