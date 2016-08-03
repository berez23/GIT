package it.webred.mui.output;

import it.webred.docfa.model.DocfaComunOggetto;
import it.webred.docfa.model.DocfaComunicazione;
import it.webred.mui.hibernate.HibernateUtil;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.hibernate.Query;
import org.hibernate.Session;

public class DocfaComunicazioneFornituraExporter {
	
	private SimpleDateFormat dateParser = new SimpleDateFormat("ddMMyyyy");

	private static String getVal(String in) {
		return (in != null ? in : "");
	}

	public void dumpFornitura(String fornitura, Writer writer)
			throws IOException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		Iterator comIterator = null;
		Session session = HibernateUtil.currentSession();
		Query query = session.createQuery("select c from it.webred.docfa.model.DocfaComunicazione as c where c.iidFornitura = :fornitura order by c.iidComunicazione asc");
		query.setParameter("fornitura", fornitura.substring(8, 10)+"/"+fornitura.substring(5, 7)+"/"+fornitura.substring(0, 4));
		comIterator = query.iterate();
		exportDap(writer, comIterator);
	}

	private void exportDap(Writer writer, Iterator comIterator)
			throws IOException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		while (comIterator.hasNext()) {
			DocfaComunicazione com = (DocfaComunicazione) comIterator.next();
			writer.write("C|");
			writeField(writer, ""+com.getIidComunicazione());
			writeField(writer, com.getDataComunicazione());
			writeField(writer, com.getCodfiscalePiva());
			writeField(writer, com.getNome());
			writeField(writer, com.getDenominazione());
			writeField(writer, com.getDataNascita());
			writeField(writer, com.getComuneNascita());
			writeField(writer, com.getProvinciaNascita());
			writeField(writer, com.getIndirizzo());
			writeField(writer, com.getComune());
			writeField(writer, com.getCap());
			writeField(writer, com.getProvincia());
			writeField(writer, com.getIndirizzoDomicilio());
			writeField(writer, com.getComuneDomicilio());
			writeField(writer, com.getCapDomicilio());
			writeField(writer, com.getProvinciaDomicilio());
			writeField(writer, com.getCodiceFiscalerl());
			writeField(writer, com.getNomerl());
			writeField(writer, com.getCognomerl());
			writeField(writer, com.getDataNascitarl());
			writeField(writer, com.getComuneNascitarl());
			writeField(writer, com.getProvinciaNascitarl());
			writeField(writer, com.getIndirizzorl());
			writeField(writer, com.getComunerl());
			writeField(writer, com.getCaprl());
			writeField(writer, com.getProvinciarl());
			writeField(writer, com.getIndirizzoDomiciliorl());
			writeField(writer, com.getComuneDomiciliorl());
			writeField(writer, com.getCapDomiciliorl());
			writeField(writer, com.getProvinciaDomiciliorl());
			writeField(writer, com.getFlagCuratoreFallimentare());
			writeField(writer, com.getFlagErede());
			writeField(writer, com.getFlagRappresentanteLegale());
			writeField(writer, com.getFlagTutore(), true);
			int oCount = 0;
			for (Iterator iter = com.getDocfaComunOggettos().iterator(); iter.hasNext();) {
				oCount++;
				DocfaComunOggetto ogg = (DocfaComunOggetto) iter.next();
				writer.write("O|");
				writeField(writer, com.getIidComunicazione());
				writeField(writer, oCount);
				writeField(writer, ogg.getIid());
				writeField(writer, ogg.getFoglio());
				writeField(writer, ogg.getParticella());
				writeField(writer, ogg.getSubalterno());
				writeField(writer, ogg.getIndirizzo());
//				writeField(writer, ogg.getNumeroCivico());
				writeField(writer, ogg.getCategoria());
				writeField(writer, ogg.getClasse());
				writeField(writer, ogg.getNumeroProtocollo());
				writeField(writer, ogg.getSezione());
				writeField(writer, ogg.getAnno());
				writeField(writer, ogg.getCodiceVariazione());
				writeField(writer, ogg.getDescVariazione());
				writeField(writer, ogg.getFoglioVar());
				writeField(writer, ogg.getParticellaVar());
				writeField(writer, ogg.getSubalternoVar());
				writeField(writer, ogg.getCategoriaVar());
				writeField(writer, ogg.getClasseVar());
				writeField(writer, ogg.getNumeroProtocolloVar());
				writeField(writer, ogg.getSezioneVar());
				writeField(writer, ogg.getAnnoVar());
				writeField(writer, ogg.getFlagAbitPrincipale());
				writeField(writer, ogg.getFlagAbitPrincipaleNm());
				writeField(writer, ogg.getFlagAgricolturaDiretta());
				writeField(writer, ogg.getFlagAreaFabbricabile());
				writeField(writer, ogg.getFlagCococo());
				writeField(writer, ogg.getFlagConiugePensionato());
				writeField(writer, ogg.getFlagCostiContabili());
				writeField(writer, ogg.getFlagDetrazioneDelib());
				writeField(writer, ogg.getFlagDetrazioneDelibNm());
				writeField(writer, ogg.getFlagDisoccupato());
				writeField(writer, ogg.getFlagFabbricato());
				writeField(writer, ogg.getFlagFabbricatoD());
				writeField(writer, ogg.getFlagImmobileEscluso());
				writeField(writer, ogg.getFlagInagibile());
				writeField(writer, ogg.getFlagInterinale());
				writeField(writer, ogg.getFlagInvalido());
				writeField(writer, ogg.getFlagMobilita());
				writeField(writer, ogg.getFlagPensionato());
				writeField(writer, ogg.getFlagPossessoDirittoAbit());
				writeField(writer, ogg.getFlagPossessoLeasing());
				writeField(writer, ogg.getFlagPossessoProprieta());
				writeField(writer, ogg.getFlagPossessoSuperficie());
				writeField(writer, ogg.getFlagPossessoUso());
				writeField(writer, ogg.getFlagPossessoUsufrutto());
				writeField(writer, ogg.getFlagRedditoDomenicale());
				writeField(writer, ogg.getFlagRenditaDefinitiva());
				writeField(writer, ogg.getFlagRenditaPresunta());
				writeField(writer, ogg.getFlagRiduzioneLocazione());
				writeField(writer, ogg.getFlagStorico());
				writeField(writer, ogg.getFlagTerrenoAgricolo());
				writeField(writer, ogg.getFlagValoreVenale());
				writeField(writer, ogg.getContitolariAbitPrincipale());
				writeField(writer, ogg.getPercentualePossesso());
				writeField(writer, ogg.getRedditoEuro());
				writeField(writer, ogg.getDecorrenza());
				writeField(writer, ogg.getMembriNucleoFamiliare(),true);
			}
		}
	}

	private void writeField(Writer writer, String field) throws IOException {
		writeField(writer, field, false);
	}
	private void writeField(Writer writer, Boolean field) throws IOException {
		writeField(writer, field, false);
	}
	private void writeField(Writer writer, Date field) throws IOException {
		writeField(writer, field, false);
	}
	private void writeField(Writer writer, Date field, boolean last) throws IOException {
		if(field == null){
			writeField(writer, (String)null, last);
		}
		else{
			writeField(writer,dateParser.format(field), last);
		}
	}
	private void writeField(Writer writer, Integer field) throws IOException {
		writeField(writer, field, false);
	}
	private void writeField(Writer writer, Integer field, boolean last) throws IOException {
		if(field == null){
			writeField(writer, (String)null, last);
		}
		else{
			writeField(writer,field.toString(), last);
		}
	}
	private void writeField(Writer writer, Long field) throws IOException {
		writeField(writer, field, false);
	}
	private void writeField(Writer writer, Long field, boolean last) throws IOException {
		if(field == null){
			writeField(writer, (String)null, last);
		}
		else{
			writeField(writer,field.toString(), last);
		}
	}
	private void writeField(Writer writer, BigDecimal field) throws IOException {
		writeField(writer, field, false);
	}
	private void writeField(Writer writer, BigDecimal field, boolean last) throws IOException {
		if(field == null){
			writeField(writer, (String)null, last);
		}
		else{
			writeField(writer,field.toString(), last);
		}
	}
	private void writeField(Writer writer, Boolean field, boolean last) throws IOException {
		if(field == null){
			writeField(writer, "0", last);
		}
		else{
			writeField(writer,field?"1":"0", last);
		}
	}

	private void writeField(Writer writer, String field, boolean last)
			throws IOException {
		if(field!=null)	writer.write(field);
		writer.write(last ? "\n" : "|");
	}
}
