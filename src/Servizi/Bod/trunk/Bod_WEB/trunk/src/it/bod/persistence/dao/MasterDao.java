package it.bod.persistence.dao;

import it.bod.application.common.MasterItem;


import java.util.Hashtable;
import java.util.List;

public interface MasterDao {
	
	public Long addItem(MasterItem item, Class cls);
	public Long addAndFlushItem(MasterItem item, Class cls);
	public List getList(Hashtable htQry, Class cls);
	public List getListCaronte(Hashtable htQry, Class cls);
	public MasterItem getItemById(Long id, Class cls);
	public void delItem(MasterItem sheet, Class cls);
	public MasterItem updItem(MasterItem mi, Class cls);
	public List getList(String strQry);
	public List getListCaronte(String strQry);
	public List getList(Hashtable htQry);
	public List getListCaronte(Hashtable htQry);


}
