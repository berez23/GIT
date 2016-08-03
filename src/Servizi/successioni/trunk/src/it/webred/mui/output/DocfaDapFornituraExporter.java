package it.webred.mui.output;

import it.webred.mui.hibernate.HibernateUtil;
import it.webred.docfa.model.DocfaDap;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import org.hibernate.Query;
import org.hibernate.Session;

public class DocfaDapFornituraExporter {

	public void dumpFornitura(String fornitura, Writer writer)
			throws IOException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		Iterator dapIterator = null;
		// dapIterator = fornitura.getMiDupNotaTrases().iterator();
		Session session = HibernateUtil.currentSession();
		Query query = session
				.createQuery("select c from it.webred.docfa.model.DocfaDap as c where c.iidFornitura = :fornitura and c.flagSkipped='N' order by c.iid asc");
		query.setParameter("fornitura", fornitura);
		dapIterator = query.iterate();
		exportDap(writer, dapIterator);
	}

	private void exportDap(Writer writer, Iterator dapIterator)
			throws IOException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		while (dapIterator.hasNext()) {
			DocfaDap dap = (DocfaDap) dapIterator.next();
			writeField(writer, dap.getDataIniziale());
			writeField(writer, dap.getDataFinale());
			writeField(writer, dap.getIidProtocolloReg());
			writeField(writer, dap.getIdSoggetto());
			writeField(writer, dap.getTipoSoggetto());
			writeField(writer, dap.getFoglio());
			writeField(writer, dap.getParticella());
			writeField(writer, dap.getSubalterno());
			writeField(writer, dap.getTipoImmobile());
			writeField(writer, dap.getDapImportoAsString());
			writeField(writer, dap.getDapMesiAsString());
			writeField(writer, dap.getDapDiritto());
			writeField(writer, dap.getDapNumeroSoggettiAsString());
			writeField(writer, dap.getDataDap());
			writeField(writer, dap.getEsitoDap(), true);
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
