package it.webred.fb.ejb.client;

import java.util.List;

import javax.ejb.Remote;

import it.webred.fb.ejb.dto.BaseDTO;
import it.webred.fb.data.model.DmBBene;
import it.webred.fb.data.model.SsTest;


@Remote
public interface TestSessionBeanRemote {

	public List<SsTest> getTestByDescrizione(BaseDTO dto);
	
	public void saveTest(BaseDTO dto);

	
	
}