package it.webred.fb.web.bean;

import it.webred.fb.data.model.DmBBene;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class TabBaseMan extends FascicoloBeneBaseBean{
	private String overlayId;
	private String activeIndex = null;
	
    public void initializeData(){
		if(bene != null)
			loadDataPage(bene);
    }
    
    public void loadDataPage(DmBBene bene){
    	this.bene = bene;
    	activeIndex = null;
    }
    
	public List<DmBBene> getGroupParent(){
    	List<DmBBene> tempList = new LinkedList<DmBBene>();
    	
    	if(bene.getDmBBenes() != null || !bene.getDmBBenes().isEmpty())
    		tempList = bene.getDmBBenes();
    	
    	return tempList;
    }

	public List<DmBBene> getGroupChildren(DmBBene parent) {

		if (parent != null)
			return parent.getDmBBenes() != null	|| !parent.getDmBBenes().isEmpty() ? parent.getDmBBenes() : new ArrayList<DmBBene>();

		return new ArrayList<DmBBene>();
	}
    
	/**
	 * Converts a Map to a List filled with its entries. This is needed since 
	 * very few if any JSF iteration components are able to iterate over a map.
	 */
	public static <T, S> List<Map.Entry<T, S>> mapToList(Map<T, S> map) {

	    if (map == null) {
	        return null;
	    }

	    List<Map.Entry<T, S>> list = new ArrayList<Map.Entry<T, S>>();
	    list.addAll(map.entrySet());

	    return list;
	}
	
	public void OpenAllTabAccordion(){
		StringBuilder out = new StringBuilder();
		
		if(activeIndex == null || activeIndex.equals("")){
		   int parentSize = getGroupParent().size();
		   
		   for(int i = 0; i <= parentSize; i++){
		        out.append(i);
		        
		        if(i-1 != parentSize)
		           out.append(",");
		    }
		   
		   activeIndex = out.toString();
		}
		else
			activeIndex = null;
	}
	
	public String getActiveIndex() {
		return activeIndex;
	}
	
	public void setActiveIndex(String activeIndex) {
		this.activeIndex = activeIndex;
	}

	public String getOverlayId() {
		return overlayId;
	}
	
}
