package it.webred.mui.model;

import java.util.Date;

import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.input.MuiFornituraParser;
import net.skillbill.logging.Logger;

import org.hibernate.Hibernate;
import org.hibernate.Session;

public class MiDupFornitura extends MiDupFornituraBase {

	private int anno = -1 ;
	private int numeroAnno = -1;
	
	public boolean isFullyLoaded(){
		if(getFileTotNotParsed() == null||getFileTotNotParsed().trim().length()==0){
			return true;
		}
		else{
			String[] loaded = getFileLoadedNotParsed().split(",");
			return loaded.length == Integer.valueOf(getFileTotNotParsed());
		}
	}
	
	public void deleteFully(Session s){
		Class table[] = new Class[]{MiConsDap.class,MiConsIntegrationLog.class,MiConsOggetto.class,MiConsComunicazione.class,MiDupImportLog.class,MiDupTitolarita.class,MiDupFabbricatiIdentifica.class,MiDupFabbricatiInfo.class,MiDupTerreniInfo.class,MiDupIndirizziSog.class, MiDupSoggetti.class,MiDupNotaTras.class,MiDupFornituraInfo.class};
		for (int i = 0; i < table.length; i++) {
			deleteReferencedTable(s, table[i]);
		}
		s.delete(this);
	}

	protected void deleteReferencedTable(Session s, Class table) {
		String hqlDelete = "delete "+table.getName()+" c where c.miDupFornitura = :miDupFornitura";
		Logger.log().info(this.getClass().getName(),"deleting from "+table.getName()+" where iidFornitura="+this.getIid());
		int deletedEntities = s.createQuery( hqlDelete ).setParameter( "miDupFornitura", this).executeUpdate();
		Logger.log().info(this.getClass().getName(),"deleted "+deletedEntities+" row(s) from "+table.getName()+" where iidFornitura="+this.getIid());
		
	}

	public int getAnno() {
		if(anno == -1){
			anno  = Integer.valueOf( MuiFornituraParser.yearParser.format(getDataInizialeDate()));
		}	
		return anno;
	}

	public int getNumeroAnno() {
		return numeroAnno;
	}

	public void setNumeroAnno(int numeroAnno) {
		this.numeroAnno = numeroAnno;
	}
	
	public boolean isIntegrata() {
		boolean integrata = false;
		try {
			Session session = HibernateUtil.currentSession();
			String sql = "select count(distinct(iid_fornitura)) as conta from suc_cons_comunicazione where cognome is not null and iid_fornitura = "
				+ getIid();
			int conta = ((Integer)session.createSQLQuery(sql).addScalar("conta", Hibernate.INTEGER).uniqueResult()).intValue();
			integrata = (conta > 0);
		} catch (Throwable thr) {}
		return integrata;
	}
	
	public boolean isIntegrataFabbr() {
		boolean integrataFabbr = false;
		try {
			Session session = HibernateUtil.currentSession();
			String sql = "select count(distinct(iid_fornitura)) as conta from suc_dup_fabbricati_info where flag_pertinenza is not null and iid_fornitura = "
				+ getIid();
			int conta = ((Integer)session.createSQLQuery(sql).addScalar("conta", Hibernate.INTEGER).uniqueResult()).intValue();
			integrataFabbr = (conta > 0);
		} catch (Throwable thr) {}
		return integrataFabbr;		
	}
	
}
