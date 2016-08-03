package it.webred.ct.service.tares.data.access.dao;

import java.util.ArrayList;
import java.util.List;

public interface SezioniDAO {

//	public List getSezioniByName(String nomeEnte);
	public Object[] getUpdateDate(String nomeEnte);
	public ArrayList<Object> execElab(String codiceEnte);
	public ArrayList<Object> execElab2(String codiceEnte);
	public ArrayList<Object> execStat(String codiceEnte);
	public ArrayList<Object> execDia(String codiceEnte);
	public ArrayList<Object> execTrasf(String codiceEnte);
	
	
}
