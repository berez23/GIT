package it.webred.ct.service.carContrib.data.access.cc;

import java.util.ArrayList;
import java.util.List;

import it.webred.ct.service.carContrib.data.access.cc.dao.CarContribDAO;
import it.webred.ct.service.carContrib.data.access.cc.dto.FiltroRichiesteDTO;
import it.webred.ct.service.carContrib.data.access.cc.dto.FiltroRichiesteSearchCriteria;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

@Stateless
public class FiltroRichiesteServiceBean implements  FiltroRichiesteService {
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private CarContribDAO carContribDAO;

	public List<FiltroRichiesteDTO> filtroRichieste(FiltroRichiesteSearchCriteria filtro,int start, int numberRecord)
	{
		List<FiltroRichiesteDTO> lista = new ArrayList<FiltroRichiesteDTO>();
		try
		{
			lista = carContribDAO.filtroRichiste(filtro,start, numberRecord);
		}
		catch (Exception ex)
		{
			
		}
		return lista;
	}
	
	public Long getRecordCount(FiltroRichiesteSearchCriteria filtro)
	{
		try
		{
			return carContribDAO.getRecordCount(filtro);
		}
		catch (Exception ex)
		{
			return new Long(0);
		}
	}
	
}

