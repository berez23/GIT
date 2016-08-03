/*
 * Creato il 26-mag-2004
 *
 * Per modificare il modello associato a questo file generato, aprire
 * Finestra&gt;Preferenze&gt;Java&gt;Generazione codice&gt;Codice e commenti
 */
package it.webred.diogene.querybuilder;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.locale.converters.BigDecimalLocaleConverter;


/**
 * @author bartoccioni
 *
 * Per modificare il modello associato al commento di questo tipo generato, aprire
 * Finestra&gt;Preferenze&gt;Java&gt;Generazione codice&gt;Codice e commenti
 */
public class ObjectComparator implements Comparator {
	public int compare(Object x, Object y) {
		for (int i = 0; i < campi.length; i++) {
			//Recupero le proprietà
			Comparable piy;
			Comparable pix;
			Class cly;
			Class clx;
			try {
				pix = (Comparable) PropertyUtils.getProperty(x, campi[i]);
				clx = PropertyUtils.getPropertyType(x, campi[i]);
			} catch (Exception e) {
				pix = "";
				clx=String.class;
			}

			//Recupero la proprietà
			try {
				piy = (Comparable) PropertyUtils.getProperty(y, campi[i]);
				cly = PropertyUtils.getPropertyType(y, campi[i]);
			} catch (Exception e) {
				piy = "";
				cly=String.class;
			}
			if(pix==null)pix="";
			if(piy==null)piy="";
			//Comparo gli oggetti, utilizzando il loro metodo compareTo
			int iResult = 0;
			
			// Controllo se il tipo da comparare è diverso da quello predefinito
			if(!pix.equals(piy)){
				SimpleDateFormat sdf = null;
				if(tipi[i]==Timestamp.class && clx==String.class ){
					try {
						sdf = new SimpleDateFormat("dd/MM/yyyy HH.mm.ss");
						Date dtx = sdf.parse(pix.toString());
						Date dty = sdf.parse(piy.toString());
						iResult=dtx.compareTo(dty);
					} catch (Exception e) {
						// se uno dei due è uguale a "" e l'atro no allora è minore
						if(pix.equals("") && !piy.equals(""))iResult=-1;
						if(!pix.equals("") && piy.equals(""))iResult=1;
					}
				}else if(tipi[i]==Date.class && clx==String.class){
					try {
						sdf = new SimpleDateFormat("dd/MM/yyyy");
						Date dtx = sdf.parse(pix.toString());
						Date dty = sdf.parse(piy.toString());
						iResult=dtx.compareTo(dty);
					} catch (Exception e) {
						// se uno dei due è uguale a "" e l'atro no allora è minore
						if(pix.equals("") && !piy.equals(""))iResult=-1;
						if(!pix.equals("") && piy.equals(""))iResult=1;
					}
				}else if(tipi[i]==BigDecimal.class && clx==String.class){
					try {
						DecimalFormat decFormat= (DecimalFormat)DecimalFormat.getInstance(Locale.ITALY);
						BigDecimalLocaleConverter cv= new BigDecimalLocaleConverter(Locale.ITALY,decFormat.toPattern());
						BigDecimal bdx= new BigDecimal(cv.convert(pix.toString()).toString());
						BigDecimal bdy= new BigDecimal(cv.convert(piy.toString()).toString());
						iResult=bdx.compareTo(bdy);
					} catch (Exception e) {
						// se uno dei due è uguale a "" e l'atro no allora è minore
						if(pix.equals("") && !piy.equals(""))iResult=-1;
						if(!pix.equals("") && piy.equals(""))iResult=1;
					}


				}else{
					// Chiamo il compareTo generico
					if(pix.equals("") && !piy.equals("")){
						iResult=-1;
					}else if(!pix.equals("") && piy.equals("")){
						iResult=1;
					}else{
						iResult = pix.compareTo(piy);
					}
				}
			}

			if (iResult != 0) {
				//Esco se non sono uguali
				if (discendenti[i]) {
					//Inverto ordine
					return -iResult;
				} else {
					return iResult;
				}
			}
		}
		//Gli oggetti hanno lo stesso ordine
		return 0;
	}
	/**
	 *  costruttori
	 */
	public ObjectComparator() {
		super();
	}

	/** Costruisce un oggetto Comparator con la capacità di comparare oggetti anche diversi 
	 * in base al nome delle proprietà seguendo la notazione JavaBean. Inoltre la comparazione 
	 * può essere effettuata anche su più campi e impostando l'ordine asc o desc per ogni campo.
	 * 
	 * 
	*	@param campi lista dei nomi delle proprietà degli oggetti da comparare in ordine di importanza.
	*/
		public ObjectComparator(String[] campi) {
		this(campi, new boolean[campi.length] );
	}

	/** Costruisce un oggetto Comparator con la capacità di comparare oggetti anche diversi 
	 * in base al nome delle proprietà seguendo la notazione JavaBean. Inoltre la comparazione 
	 * può essere effettuata anche su più campi e impostando l'ordine asc o desc per ogni campo.
	 * 
	 * 
	*	@param campi lista dei nomi delle proprietà degli oggetti da comparare in ordine di importanza.
	*	@param discendenti lista di boolean degli ordinamenti se = true ordine Discendente (dal più grande al più piccolo)
	*/
		public ObjectComparator(String[] campi, boolean[] discendenti) {
		this(campi, discendenti , new Class[campi.length]);
	}

	/** Costruisce un oggetto Comparator con la capacità di comparare oggetti anche diversi 
	 * in base al nome delle proprietà seguendo la notazione JavaBean. Inoltre la comparazione 
	 * può essere effettuata anche su più campi e impostando l'ordine asc o desc per ogni campo.
	 * 
	 * 
	*	@param campi lista dei nomi delle proprietà degli oggetti da comparare in ordine di importanza.
	*	@param tipi lista di class utilizzata per la conversione dei campi stringa (esempio "01/01/2001" puo essere tradotto in Date)
	*/
	public ObjectComparator(String[] campi, Class[] tipi ) {
		this(campi, new boolean[campi.length],tipi );
	}

	/** Costruisce un oggetto Comparator con la capacità di comparare oggetti anche diversi 
	 * in base al nome delle proprietà seguendo la notazione JavaBean. Inoltre la comparazione 
	 * può essere effettuata anche su più campi e impostando l'ordine asc o desc per ogni campo.
	 * 
	 * 
	*	@param campi lista dei nomi delle proprietà degli oggetti da comparare in ordine di importanza.
	*	@param discendenti lista di boolean degli ordinamenti se = true ordine Discendente (dal più grande al più piccolo)
	*	@param tipi lista di class utilizzata per la conversione dei campi stringa (esempio "01/01/2001" puo essere tradotto in Date)
	*/
	public ObjectComparator(String[] campi, boolean[] discendenti, Class[] tipi ) {
		this.campi = campi;
		this.discendenti = discendenti;
		this.tipi=tipi;
		for (int i = 0; i < tipi.length; i++) {
			if(tipi[i]==null)tipi[i]= String.class;
		}
	}

	/** Costruisce un oggetto Comparator con la capacità di comparare oggetti anche diversi 
	 * in base al nome delle proprietà seguendo la notazione JavaBean. Inoltre la comparazione 
	 * può essere effettuata anche su più campi e impostando l'ordine asc o desc per ogni campo.
	 * 
	 * 
	*	@param campo il nome della proprietà degli oggetti da comparare.
	*/
	public ObjectComparator(String campo) {
		this(campo,false);
	}
	
	/** Costruisce un oggetto Comparator con la capacità di comparare oggetti anche diversi 
	 * in base al nome delle proprietà seguendo la notazione JavaBean. Inoltre la comparazione 
	 * può essere effettuata anche su più campi e impostando l'ordine asc o desc per ogni campo.
	 * 
	 * 
	*	@param campo il nome della proprietà degli oggetti da comparare.
	*	@param discendente boolean ordinamento se = true ordine Discendente (dal più grande al più piccolo)
	*/
	public ObjectComparator(String campo, Boolean ascendente) {
		this(campo,ascendente,String.class);
	}
	
	/** Costruisce un oggetto Comparator con la capacità di comparare oggetti anche diversi 
	 * in base al nome delle proprietà seguendo la notazione JavaBean. Inoltre la comparazione 
	 * può essere effettuata anche su più campi e impostando l'ordine asc o desc per ogni campo.
	 * 
	 * 
	*	@param campo il nome della proprietà degli oggetti da comparare.
	*	@param tipo il class utilizzato per la conversione dei campi stringa (esempio "01/01/2001" puo essere tradotto in Date)
	*/
	public ObjectComparator(String campo, Class tipo) {
		this(campo,false, tipo);
	}
	
	/** Costruisce un oggetto Comparator con la capacità di comparare oggetti anche diversi 
	 * in base al nome delle proprietà seguendo la notazione JavaBean. Inoltre la comparazione 
	 * può essere effettuata anche su più campi e impostando l'ordine asc o desc per ogni campo.
	 * 
	 * 
	*	@param campo il nome della proprietà degli oggetti da comparare.
	*	@param discendente boolean ordinamento se = true ordine Discendente (dal più grande al più piccolo)
	*	@param tipo il class utilizzato per la conversione dei campi stringa (esempio "01/01/2001" puo essere tradotto in Date)
	*/
	public ObjectComparator(String campo, Boolean ascendente, Class tipo) {
		this(new String[]{campo},new boolean[]{ascendente},new Class[]{tipo});
	}
	protected String[] campi;
	protected boolean[] discendenti;
	protected Class[] tipi;
}
