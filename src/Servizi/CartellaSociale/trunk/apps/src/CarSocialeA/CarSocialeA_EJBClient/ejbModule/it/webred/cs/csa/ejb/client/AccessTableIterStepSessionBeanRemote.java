package it.webred.cs.csa.ejb.client;

import it.webred.cs.csa.ejb.dto.IterDTO;
import it.webred.cs.data.model.CsCfgItStato;
import it.webred.cs.data.model.CsItStep;
import it.webred.cs.data.model.CsCfgItTransizione;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface AccessTableIterStepSessionBeanRemote {

	public List<CsItStep> getIterStepsByCaso(IterDTO dto) throws Exception;

	public CsItStep getLastIterStepByCaso(IterDTO dto) throws Exception;

	public boolean newIter(IterDTO dto) throws Exception;

	public boolean addIterStep(IterDTO dto) throws Exception;

	public CsCfgItStato findStatoById(IterDTO dto) throws Exception;

	public List<CsCfgItTransizione> getTransizionesByStatoRuolo (IterDTO dto) throws Exception;
}
