package it.webred.ct.service.tares.data.access.dao;

import it.webred.ct.service.tares.data.access.dto.SegnalazioniSearchCriteria;
import it.webred.ct.service.tares.data.model.SetarSegnala1;
import it.webred.ct.service.tares.data.model.SetarSegnala2;
import it.webred.ct.service.tares.data.model.SetarSegnala3;
import it.webred.ct.service.tares.data.model.SetarSegnalazione;

import java.util.List;

public interface SegnalazioniDAO {

	public List<SetarSegnala1> getSegnalazioni1(SegnalazioniSearchCriteria criteria);
	public SetarSegnala1 getSegnala1(SetarSegnala1 setarSegnala1);
	public SetarSegnala1 updSegnala1(SetarSegnala1 setarSegnala1);
	public SetarSegnala1 addSegnala1(SetarSegnala1 setarSegnala1);
	public int delSegnala1ById(SetarSegnala1 setarSegnala1);
	public int delSegnalazioni1(SegnalazioniSearchCriteria criteria);
	
	public List<SetarSegnala2> getSegnalazioni2(SegnalazioniSearchCriteria criteria);
	public SetarSegnala2 getSegnala2(SetarSegnala2 setarSegnala2);
	public SetarSegnala2 updSegnala2(SetarSegnala2 setarSegnala2);
	public SetarSegnala2 addSegnala2(SetarSegnala2 setarSegnala2);
	public int delSegnala2ById(SetarSegnala2 setarSegnala2);
	public int delSegnalazioni2(SegnalazioniSearchCriteria criteria);
	
	public List<SetarSegnala3> getSegnalazioni3(SegnalazioniSearchCriteria criteria);
	public SetarSegnala3 getSegnala3(SetarSegnala3 setarSegnala3);
	public SetarSegnala3 updSegnala3(SetarSegnala3 setarSegnala3);
	public SetarSegnala3 addSegnala3(SetarSegnala3 setarSegnala3);
	public int delSegnala3ById(SetarSegnala3 setarSegnala3);
	public int delSegnalazioni3(SegnalazioniSearchCriteria criteria);
	
	public SetarSegnalazione getSegnalazione(SetarSegnalazione setarSegnalazione);
	public List<SetarSegnalazione> getSegnalazioni(SegnalazioniSearchCriteria criteria);
	public SetarSegnalazione updSegnalazione(SetarSegnalazione setarSegnalazione);
	public SetarSegnalazione addSegnalazione(SetarSegnalazione setarSegnalazione);
	public int delSegnalazioneById(SetarSegnalazione setarSegnalazione);
	
	public List<Object> getUiu(SegnalazioniSearchCriteria criteria);
	public List<Object> getVani(SegnalazioniSearchCriteria criteria);
	public List<Object> getAmbienti(SegnalazioniSearchCriteria criteria);
	public Long getSegnalazioniMaxProgressivo(SegnalazioniSearchCriteria criteria);
	public Long getSegnala1MaxProgressivo(SegnalazioniSearchCriteria criteria);
	
	
}
