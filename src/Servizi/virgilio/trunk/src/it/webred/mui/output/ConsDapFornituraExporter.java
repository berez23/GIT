package it.webred.mui.output;

import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.model.MiConsDap;
import it.webred.mui.model.MiDupFornitura;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.hibernate.Query;
import org.hibernate.Session;

public class ConsDapFornituraExporter {

	private static String getVal(String in) {
		return (in != null ? in : "");
	}

	public void dumpFornitura(MiDupFornitura fornitura, Writer writer)
			throws IOException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		Iterator dapIterator = null;
		// dapIterator = fornitura.getMiDupNotaTrases().iterator();
		Session session = HibernateUtil.currentSession();
		Query query = session
				.createQuery("select  distinct c.idNota,c.dataInizialeDate,c.dataFinaleDate,c.idSoggettoNota,c.idSoggettoCatastale," +
						"c.tipoSoggetto,c.idImmobile,c.tipologiaImmobile,c.dapImporto,c.dapMesi,c.flagDapDiritto,c.dapNumeroSoggetti," +
						"c.dapData,c.flagRegoleDapNoDataResidenza," +
						"c.flagRegoleDapDataResOltre90Giorni," +
						"c.flagRegoleDapPrecentualePossessoTotaleErrata," +
						"c.flagRegoleDapSoggettoPossessorePiuImmobili," +
						"c.flagRegoleDapSoggettoPossessorePiuImmobiliStessoIndirizzo," +
						"c.flagRegoleDapNoDataComposizioneFamiliare " +
						"from it.webred.mui.model.MiConsDap as c " +
						"where c.miDupFornitura = :fornitura " +
						"and c.flagSkipped='N' ");
		query.setParameter("fornitura", fornitura);
		dapIterator = query.iterate();
		//List<MiConsDap> list =  query.list();
		exportDap(writer, dapIterator);
		//exportDap(writer, list);
	}

	private void exportDap(Writer writer, Iterator dapIterator)
	//private void exportDap(Writer writer, List<MiConsDap> dapIterator)
			throws IOException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		//ListIterator<MiConsDap> iter =  dapIterator.listIterator();
		while (dapIterator.hasNext()) { 
			Object[] dap = (Object[]) dapIterator.next();
/*			MiConsDap dap = (MiConsDap) iter.next();
			writeField(writer, dap.getDataIniziale());
			writeField(writer, dap.getDataFinale());
			writeField(writer, dap.getIdNota());
			writeField(writer, dap.getTipoRec());
			writeField(writer, dap.getIdSoggettoNota());
			writeField(writer, dap.getIdSoggettoCatastale());
			writeField(writer, dap.getTipoSoggetto());
			writeField(writer, dap.getIdImmobile());
			writeField(writer, dap.getTipologiaImmobile());
			writeField(writer, dap.getDapImportoAsString());
			writeField(writer, dap.getDapMesiAsString());
			writeField(writer, dap.getDapDiritto());
			writeField(writer, dap.getDapNumeroSoggettiAsString());
			writeField(writer, dap.getDataDap());
			writeField(writer, dap.getDapEsitoAsString(), true);
*/		
			MiConsDap d = new MiConsDap();
			d.setIdNota((String)dap[0]);
			d.setDataInizialeDate((Timestamp)dap[1]);
			d.setDataFinaleDate((Timestamp)dap[2]);
			d.setIdSoggettoNota((String)dap[3]);
			d.setIdSoggettoCatastale((String)dap[4]);
			d.setTipoSoggetto((String)dap[5]);
			d.setIdImmobile((String)dap[6]);
			d.setTipologiaImmobile((String)dap[7]);
			d.setDapImporto((BigDecimal)dap[8]);
			d.setDapMesi((Integer)dap[9]);
			d.setFlagDapDiritto((Boolean)dap[10]);
			d.setDapNumeroSoggetti((Integer)dap[11]);
			d.setDapData((Timestamp)dap[12]);
			d.setFlagRegoleDapNoDataResidenza((Boolean)dap[13]);
			d.setFlagRegoleDapDataResOltre90Giorni((Boolean)dap[14]);
			d.setFlagRegoleDapPrecentualePossessoTotaleErrata((Boolean)dap[15]);
			d.setFlagRegoleDapSoggettoPossessorePiuImmobili((Boolean)dap[16]);
			d.setFlagRegoleDapSoggettoPossessorePiuImmobiliStessoIndirizzo((Boolean)dap[17]);
			d.setFlagRegoleDapNoDataComposizioneFamiliare((Boolean)dap[18]);
			
		
			
			//writeField(writer, (String)dap[1]);
			writeField(writer, d.getDataIniziale());
			writeField(writer, d.getDataFinale());
			writeField(writer, d.getIdNota());
			writeField(writer, d.getTipoRec());
			writeField(writer, d.getIdSoggettoNota());
			writeField(writer, d.getIdSoggettoCatastale());
			writeField(writer, d.getTipoSoggetto());
			writeField(writer, d.getIdImmobile());
			writeField(writer, d.getTipologiaImmobile());
			writeField(writer, d.getDapImportoAsString());
			writeField(writer, d.getDapMesiAsString());
			writeField(writer, d.getDapDiritto());
			writeField(writer, d.getDapNumeroSoggettiAsString());
			writeField(writer, d.getDataDap());
			writeField(writer, d.getDapEsitoAsString(), true);
		}
	}

	private void writeField(Writer writer, String field) throws IOException {
		writeField(writer, field, false);
	}

	private void writeField(Writer writer, String field, boolean last)
			throws IOException {
		writer.write(field);
		writer.write(last ? "\n" : "|");
	}
}
