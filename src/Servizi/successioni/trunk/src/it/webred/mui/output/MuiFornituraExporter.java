package it.webred.mui.output;

import it.webred.mui.consolidation.ComunicazioneConverter;
import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.http.MuiApplication;
import it.webred.mui.input.parsers.MuiFornituraRecordAbstractParser;
import it.webred.mui.model.MiDupFabbricatiIdentifica;
import it.webred.mui.model.MiDupFabbricatiInfo;
import it.webred.mui.model.MiDupFornitura;
import it.webred.mui.model.MiDupIndirizziSog;
import it.webred.mui.model.MiDupNotaTras;
import it.webred.mui.model.MiDupSoggetti;
import it.webred.mui.model.MiDupTerreniInfo;
import it.webred.mui.model.MiDupTitolarita;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Query;
import org.hibernate.Session;

public class MuiFornituraExporter {

	private static String getVal(String in){
		return (in != null ? in: "");
	}
	public void dumpFornitura(MiDupFornitura fornitura, Writer writer)
			throws IOException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		writer.write("1|"+MuiApplication.belfiore+"|"+getVal(fornitura.getDataIniziale())+"|"+getVal(fornitura.getDataFinale())+"|"+getVal(fornitura.getDataEstrCons())+"|"+getVal(fornitura.getDataEstrCata())+"\n");
		Iterator noteIterator =null;
//		 noteIterator = fornitura.getMiDupNotaTrases().iterator();
		Session session = HibernateUtil.currentSession();
		Query query = session.createQuery("select c from it.webred.mui.model.MiDupNotaTras as c where c.miDupFornitura = :fornitura order by c.iid asc");
		query.setParameter("fornitura",fornitura);
		noteIterator =query.iterate();
		exportNotes(writer, noteIterator);

		writer.write("9|");
		exportRow(writer, fornitura.getMiDupFornituraInfo());
	}

	private void exportNotes(Writer writer, Iterator noteIterator) throws IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		while (noteIterator.hasNext()) {
			MiDupNotaTras nota = (MiDupNotaTras)noteIterator.next();
			exportRow(writer, nota,2);
			if (nota.getMiDupSoggettis() != null) {
				Iterator<MiDupSoggetti> soggettiIterator = nota
						.getMiDupSoggettis().iterator();
				while (soggettiIterator.hasNext()) {
					MiDupSoggetti soggetto = soggettiIterator.next();
					exportRow(writer, soggetto,3);
				}
			}
			if (nota.getMiDupTitolaritas() != null) {
				Iterator<MiDupTitolarita> titolaritaIterator = ComunicazioneConverter
						.groupGraffati(nota.getMiDupTitolaritas()).iterator();
				while (titolaritaIterator.hasNext()) {
					MiDupTitolarita titolarita = titolaritaIterator.next();
					exportRow(writer, titolarita,4);
				}
			}
			if (nota.getMiDupIndirizziSogs() != null) {
				Iterator<MiDupIndirizziSog> indirizziIterator = nota
						.getMiDupIndirizziSogs().iterator();
				while (indirizziIterator.hasNext()) {
					MiDupIndirizziSog indirizzo = indirizziIterator.next();
					exportRow(writer, indirizzo,5);
				}
			}
			if (nota.getMiDupFabbricatiInfos() != null) {
				Iterator<MiDupFabbricatiInfo> fabbricatiIterator = nota
						.getMiDupFabbricatiInfos().iterator();
				while (fabbricatiIterator.hasNext()) {
					MiDupFabbricatiInfo fabbricato = fabbricatiIterator.next();
					exportRow(writer, fabbricato,6);
				}
			}
			if (nota.getMiDupFabbricatiIdentificas() != null) {
				Iterator<MiDupFabbricatiIdentifica> identificaIterator = nota
						.getMiDupFabbricatiIdentificas().iterator();
				while (identificaIterator.hasNext()) {
					MiDupFabbricatiIdentifica identifica = identificaIterator
							.next();
					exportRow(writer, identifica,7);
				}
			}
			if (nota.getMiDupTerreniInfos() != null) {
				Iterator<MiDupTerreniInfo> terreniIterator = nota
						.getMiDupTerreniInfos().iterator();
				while (terreniIterator.hasNext()) {
					MiDupTerreniInfo terreno = terreniIterator.next();
					exportRow(writer, terreno,9);
				}
			}
		}
	}

	private void exportRow(Writer writer, Object pojo, int tipoRecord) throws IOException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		writer.write(BeanUtils.getProperty(pojo, "idNota")+"|"+tipoRecord+"|");
		exportRow(writer,pojo);
	}

	private void exportRow(Writer writer, Object pojo) throws IOException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		String[] fields;
		fields = MuiFornituraRecordAbstractParser
				.getFieldNames(pojo.getClass());
		for (int i = 0; i < fields.length; i++) {
			if(!"idNota".equalsIgnoreCase(fields[i])){
				writer.write(getValue(BeanUtils.getProperty(pojo, fields[i]))
					+ getDelimiter(i, fields.length));
			}
		}
	}

	private static char getDelimiter(int index, int max) {
		if (index < max - 1) {
			return '|';
		} else {
			return '\n';
		}
	}

	private static String getValue(String val) {
		if (val == null) {
			return "";
		} else {
			return val;
		}
	}
}
