package it.webred.mui.model;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class MiDupSoggetti extends MiDupSoggettiBase {

	private Set<Residenza> residenzas = null;
	public void addResidenza(Residenza resid){
		if(residenzas == null){
			residenzas = new TreeSet<Residenza>();
		}
		residenzas.add(resid);
	}
	
	public void addResidenzas(ArrayList<Residenza> residenzas) {
		
		Iterator<Residenza> theIterator = residenzas.iterator();
		while (theIterator.hasNext()) {
			Residenza res = theIterator.next();
			addResidenza(res);
		}
		
	}
	
	public Residenza[] getResidenzas(){
		Residenza[] res = null;
		if(residenzas == null){
			res = new Residenza[0];
		}
		else{
			res = new Residenza[residenzas.size()];
			Iterator<Residenza> theIterator = residenzas.iterator();
			int i = 0;
			while (theIterator.hasNext()) {
				res[i] = theIterator.next();
				i++;
			}
		}
		return res;
	}
	public boolean isGiuridico(){
		return ("G".equalsIgnoreCase(getTipoSoggetto()) );
	}
}
