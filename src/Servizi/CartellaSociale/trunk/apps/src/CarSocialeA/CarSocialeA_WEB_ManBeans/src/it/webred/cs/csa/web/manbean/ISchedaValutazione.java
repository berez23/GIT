package it.webred.cs.csa.web.manbean;

import it.webred.cs.data.model.CsDValutazione;

/**
 * @author Alessandro Feriani
 *
 */
public interface ISchedaValutazione {

	public void init( CsDValutazione parent, CsDValutazione barthel );
	public void save();
	public void restore();
	public String getVersionLowerCase();
}
