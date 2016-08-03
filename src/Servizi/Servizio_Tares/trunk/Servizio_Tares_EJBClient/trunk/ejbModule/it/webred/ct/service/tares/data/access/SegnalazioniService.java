package it.webred.ct.service.tares.data.access;

import it.webred.ct.service.tares.data.access.dto.DataInDTO;
import it.webred.ct.service.tares.data.model.SetarSegnala1;
import it.webred.ct.service.tares.data.model.SetarSegnala2;
import it.webred.ct.service.tares.data.model.SetarSegnala3;
import it.webred.ct.service.tares.data.model.SetarSegnalazione;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface SegnalazioniService {
	
	public List<SetarSegnala1> getSegnalazioni1(DataInDTO dataIn);
	public SetarSegnala1 getSegnala1(DataInDTO dataIn);
	public SetarSegnala1 updSegnala1(DataInDTO dataIn);
	public SetarSegnala1 addSegnala1(DataInDTO dataIn);
	public int delSegnala1ById(DataInDTO dataIn);
	public int delSegnalazioni1(DataInDTO dataIn);

	public List<SetarSegnala2> getSegnalazioni2(DataInDTO dataIn);
	public SetarSegnala2 getSegnala2(DataInDTO dataIn);
	public SetarSegnala2 updSegnala2(DataInDTO dataIn);
	public SetarSegnala2 addSegnala2(DataInDTO dataIn);
	public int delSegnala2ById(DataInDTO dataIn);
	public int delSegnalazioni2(DataInDTO dataIn);

	public List<SetarSegnala3> getSegnalazioni3(DataInDTO dataIn);
	public SetarSegnala3 getSegnala3(DataInDTO dataIn);
	public SetarSegnala3 updSegnala3(DataInDTO dataIn);
	public SetarSegnala3 addSegnala3(DataInDTO dataIn);
	public int delSegnala3ById(DataInDTO dataIn);
	public int delSegnalazioni3(DataInDTO dataIn);

	public List<Object> getUiu(DataInDTO dataIn);
	public List<Object> getVani(DataInDTO dataIn);
	public List<Object> getAmbienti(DataInDTO dataIn);
	public Long getSegnalazioniMaxProgressivo(DataInDTO dataIn);
	public Long getSegnala1MaxProgressivo(DataInDTO dataIn);
	
	public SetarSegnalazione updSegnalazione(DataInDTO dataIn);
	public SetarSegnalazione getSegnalazione(DataInDTO dataIn);
	public List<SetarSegnalazione> getSegnalazioni(DataInDTO dataIn);
	public SetarSegnalazione addSegnalazione(DataInDTO dataIn);
	public int delSegnalazioneById(DataInDTO dataIn);

}
