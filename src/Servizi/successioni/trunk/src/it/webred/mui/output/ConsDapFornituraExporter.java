package it.webred.mui.output;

import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.model.MiConsDap;
import it.webred.mui.model.MiDupFornitura;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

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
				.createQuery("select c from it.webred.mui.model.MiConsDap as c where c.miDupFornitura = :fornitura and c.flagSkipped='N' order by c.iid asc");
		query.setParameter("fornitura", fornitura);
		dapIterator = query.iterate();
		exportDap(writer, dapIterator);
	}

	private void exportDap(Writer writer, Iterator dapIterator)
			throws IOException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		while (dapIterator.hasNext()) {
			MiConsDap dap = (MiConsDap) dapIterator.next();
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
