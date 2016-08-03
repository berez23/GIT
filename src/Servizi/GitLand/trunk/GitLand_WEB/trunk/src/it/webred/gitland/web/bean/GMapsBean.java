package it.webred.gitland.web.bean;

import it.webred.cet.permission.CeTUser;
import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.CatastoBaseDTO;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@RequestScoped
public class GMapsBean extends GitLandBaseBean{

	private String x;
	private String y;
	
	private String[] selCoordinate;  //latitudine, longitudine
	
	@PostConstruct
	public void init() {
		 FacesContext context = FacesContext.getCurrentInstance();
		 Map<String, String> paramMap = context.getExternalContext().getRequestParameterMap();
		 
		 String lat = paramMap.get("latitudine");
		 String lon = paramMap.get("longitudine");
		 if(lat!=null && lon!=null){
			 selCoordinate= new String[2];
			 selCoordinate[0]=lat;
			 selCoordinate[1]=lon;
		 }
		
		if(selCoordinate==null){
			x = paramMap.get("x");
			y = paramMap.get("y");
		
			selCoordinate = this.getLatitudineLongitudineFromXY(x, y);
		}
		
	}
	
	public String[] getLatitudineLongitudineFromXY(String x, String y){
		CatastoService cs = this.getCatastoService();
		CatastoBaseDTO cb = new CatastoBaseDTO();
		//fillUserData(cb);
		CeTUser user = getUser();
		CeTBaseObject ro = new CeTBaseObject();
		if(user != null){
			ro.setEnteId(user.getCurrentEnte());
			ro.setUserId(user.getUsername());
			ro.setSessionId(user.getSessionId());
		}
		String[] in = new String[2];
		in[0]=x;
		in[1]=y;
		cb.setObj1(in);
		return cs.getLatitudineLongitudineFromXY(cb);
	}
	
	public String getX() {
		return x;
	}
	public void setX(String x) {
		this.x = x;
	}
	public String getY() {
		return y;
	}
	public void setY(String y) {
		this.y = y;
	}
	public String getLatitudine() {
		return selCoordinate[0];
	}
	public String getLongitudine() {
		return selCoordinate[1];
	}
	public String[] getSelCoordinate() {
		return selCoordinate;
	}
	public void setSelCoordinate(String[] coordinate) {
		this.selCoordinate = coordinate;
	}
	
	
	
}
