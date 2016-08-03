/**
 * 
 */
package it.webred.cs.csa.web.manbean;

import it.webred.classfactory.VersionClassComparator;
import it.webred.cs.jsf.manbean.superc.CsUiCompBaseBean;

import java.io.Serializable;

/**
 * @author alessandro.feriani
 *
 */
public abstract class SchedaValutazioneBaseBean extends CsUiCompBaseBean implements Serializable, ISchedaValutazione {

	private static final long serialVersionUID = 1L;

	public SchedaValutazioneBaseBean() {
	}
	
	@Override
	public String getVersionLowerCase() {
		return VersionClassComparator.getVersionLowerCase( this.getClass() );
	}
}
