package it.webred.ct.service.tares.data.access;

import it.webred.ct.service.tares.data.access.dao.SegnalazioniDAO;
import it.webred.ct.service.tares.data.access.dto.DataInDTO;
import it.webred.ct.service.tares.data.model.SetarSegnala1;
import it.webred.ct.service.tares.data.model.SetarSegnala2;
import it.webred.ct.service.tares.data.model.SetarSegnala3;
import it.webred.ct.service.tares.data.model.SetarSegnalazione;

import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

@Stateless
public class SegnalazioniServiceBean extends TaresServiceBaseBean implements SegnalazioniService{

	private static final long serialVersionUID = -6445524790522970054L;
	
	@Autowired
	private SegnalazioniDAO segnalazioniDAO;
	
	public SegnalazioniServiceBean() {

	}//-------------------------------------------------------------------------
	
	public List<SetarSegnala1> getSegnalazioni1(DataInDTO dataIn) {
		return segnalazioniDAO.getSegnalazioni1(dataIn.getCriteriaSegnalazioni());
	}//-------------------------------------------------------------------------

	public SetarSegnala1 getSegnala1(DataInDTO dataIn){
		return segnalazioniDAO.getSegnala1((SetarSegnala1)dataIn.getObj());
	}//-------------------------------------------------------------------------
	
	public SetarSegnala1 updSegnala1(DataInDTO dataIn){
		return segnalazioniDAO.updSegnala1((SetarSegnala1)dataIn.getObj());
	}//-------------------------------------------------------------------------
	
	public SetarSegnala1 addSegnala1(DataInDTO dataIn){
		return segnalazioniDAO.addSegnala1((SetarSegnala1)dataIn.getObj());
	}//-------------------------------------------------------------------------
	
	public int delSegnala1ById(DataInDTO dataIn){
		return segnalazioniDAO.delSegnala1ById((SetarSegnala1)dataIn.getObj());
	}//-------------------------------------------------------------------------
	
	public int delSegnalazioni1(DataInDTO dataIn){
		return segnalazioniDAO.delSegnalazioni1(dataIn.getCriteriaSegnalazioni());
	}//-------------------------------------------------------------------------

	public List<SetarSegnala2> getSegnalazioni2(DataInDTO dataIn) {
		return segnalazioniDAO.getSegnalazioni2(dataIn.getCriteriaSegnalazioni());
	}//-------------------------------------------------------------------------

	public SetarSegnala2 getSegnala2(DataInDTO dataIn){
		return segnalazioniDAO.getSegnala2((SetarSegnala2)dataIn.getObj());
	}//-------------------------------------------------------------------------
	
	public SetarSegnala2 updSegnala2(DataInDTO dataIn){
		return segnalazioniDAO.updSegnala2((SetarSegnala2)dataIn.getObj());
	}//-------------------------------------------------------------------------
	
	public SetarSegnala2 addSegnala2(DataInDTO dataIn){
		return segnalazioniDAO.addSegnala2((SetarSegnala2)dataIn.getObj());
	}//-------------------------------------------------------------------------
	
	public int delSegnala2ById(DataInDTO dataIn){
		return segnalazioniDAO.delSegnala2ById((SetarSegnala2)dataIn.getObj());
	}//-------------------------------------------------------------------------
	
	public int delSegnalazioni2(DataInDTO dataIn){
		return segnalazioniDAO.delSegnalazioni2(dataIn.getCriteriaSegnalazioni());
	}//-------------------------------------------------------------------------

	public List<SetarSegnala3> getSegnalazioni3(DataInDTO dataIn) {
		return segnalazioniDAO.getSegnalazioni3(dataIn.getCriteriaSegnalazioni());
	}//-------------------------------------------------------------------------
	
	public SetarSegnala3 updSegnala3(DataInDTO dataIn){
		return segnalazioniDAO.updSegnala3((SetarSegnala3)dataIn.getObj());
	}//-------------------------------------------------------------------------
	
	public SetarSegnala3 getSegnala3(DataInDTO dataIn){
		return segnalazioniDAO.getSegnala3((SetarSegnala3)dataIn.getObj());
	}//-------------------------------------------------------------------------
	
	public SetarSegnala3 addSegnala3(DataInDTO dataIn){
		return segnalazioniDAO.addSegnala3((SetarSegnala3)dataIn.getObj());
	}//-------------------------------------------------------------------------
	
	public int delSegnala3ById(DataInDTO dataIn){
		return segnalazioniDAO.delSegnala3ById((SetarSegnala3)dataIn.getObj());
	}//-------------------------------------------------------------------------
	
	public int delSegnalazioni3(DataInDTO dataIn){
		return segnalazioniDAO.delSegnalazioni3(dataIn.getCriteriaSegnalazioni());
	}//-------------------------------------------------------------------------
	
	public List<Object> getUiu(DataInDTO dataIn){
		return segnalazioniDAO.getUiu(dataIn.getCriteriaSegnalazioni());
	}//-------------------------------------------------------------------------
	
	public List<Object> getVani(DataInDTO dataIn){
		return segnalazioniDAO.getVani(dataIn.getCriteriaSegnalazioni());
	}//-------------------------------------------------------------------------
	
	public List<Object> getAmbienti(DataInDTO dataIn){
		return segnalazioniDAO.getAmbienti(dataIn.getCriteriaSegnalazioni());
	}//-------------------------------------------------------------------------
	
	public Long getSegnalazioniMaxProgressivo(DataInDTO dataIn){
		return segnalazioniDAO.getSegnalazioniMaxProgressivo(dataIn.getCriteriaSegnalazioni());
	}//-------------------------------------------------------------------------
	
	public Long getSegnala1MaxProgressivo(DataInDTO dataIn){
		return segnalazioniDAO.getSegnala1MaxProgressivo(dataIn.getCriteriaSegnalazioni());
	}//-------------------------------------------------------------------------
	
	public SetarSegnalazione updSegnalazione(DataInDTO dataIn){
		return segnalazioniDAO.updSegnalazione((SetarSegnalazione)dataIn.getObj());
	}//-------------------------------------------------------------------------
	
	public SetarSegnalazione getSegnalazione(DataInDTO dataIn){
		return segnalazioniDAO.getSegnalazione((SetarSegnalazione)dataIn.getObj());
	}//-------------------------------------------------------------------------
	
	public List<SetarSegnalazione> getSegnalazioni(DataInDTO dataIn) {
		return segnalazioniDAO.getSegnalazioni(dataIn.getCriteriaSegnalazioni());
	}//-------------------------------------------------------------------------
	
	public SetarSegnalazione addSegnalazione(DataInDTO dataIn){
		return segnalazioniDAO.addSegnalazione((SetarSegnalazione)dataIn.getObj());
	}//-------------------------------------------------------------------------
	
	public int delSegnalazioneById(DataInDTO dataIn){
		return segnalazioniDAO.delSegnalazioneById((SetarSegnalazione)dataIn.getObj());
	}//-------------------------------------------------------------------------

}
