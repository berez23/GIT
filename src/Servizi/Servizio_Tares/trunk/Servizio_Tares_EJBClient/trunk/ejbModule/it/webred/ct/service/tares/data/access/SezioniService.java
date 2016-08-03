package it.webred.ct.service.tares.data.access;
import it.webred.ct.service.tares.data.access.dto.DataInDTO;

//import it.webred.ct.service.tares.data.model.CataSezioni;

import java.util.ArrayList;

import javax.ejb.Remote;

@Remote
public interface SezioniService {
	
//	public List<CataSezioni> getSezioniByName(DataInDTO dataIn);
	public Object[] getUpdateDate(DataInDTO dataIn);
	public ArrayList<Object> execElab(DataInDTO dataIn);
	public ArrayList<Object> execElab2(DataInDTO dataIn);
	public ArrayList<Object> execStat(DataInDTO dataIn);
	public ArrayList<Object> execDia(DataInDTO dataIn);
	public ArrayList<Object> execTrasf(DataInDTO dataIn);
	
}
