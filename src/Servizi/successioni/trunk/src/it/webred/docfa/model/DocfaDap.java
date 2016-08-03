package it.webred.docfa.model;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DocfaDap extends DocfaDapBase {

	private DecimalFormat df = new DecimalFormat("0.00");
	public static SimpleDateFormat dateParser = new SimpleDateFormat("ddMMyyyy");
	public static SimpleDateFormat dateParserR = new SimpleDateFormat("yyyyMMdd");
	public DocfaDap() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DocfaDap(String iidFornitura, String iidProtocolloReg, String idSoggetto, String tipoSoggetto, String foglio, String particella, String subalterno, String tipoImmobile, BigDecimal dapImporto, Integer dapMesi, Boolean flagDapDiritto, Integer dapNumeroSoggetti, String dapData, String esitoData, Boolean flagSkipped) {
		super(iidFornitura, iidProtocolloReg, idSoggetto, tipoSoggetto, foglio, particella, subalterno, tipoImmobile, dapImporto, dapMesi, flagDapDiritto, dapNumeroSoggetti, dapData, esitoData, flagSkipped);
	}
	
	public String getDataIniziale(){
		return (getIidFornitura()!=null?getIidFornitura().substring(0, 6)+"01":"");
	}
	
	public String getDataFinale(){
		String dataF = "00000000";
		try{
			DateFormat ddff = DateFormat.getDateInstance();
			Date dataD = dateParserR.parse(getIidFornitura().substring(0, 6)+"01");
			//Date dataD = DateFormat.getDateInstance().parse("01"+getIidFornitura().substring(4, 6)+getIidFornitura().substring(0, 4));
			//Date dataD = ddff.parse(getIidFornitura().substring(0, 6)+"01");
			Calendar cal = Calendar.getInstance();
			cal.setTime(dataD);
			cal.roll(Calendar.MONTH, 1);
			cal.roll(Calendar.DAY_OF_MONTH, false);
			dataF = dateParserR.format(cal.getTime());
		}catch (Exception e){
			e.printStackTrace();
		}
		return (getIidFornitura()!=null?dataF:"00000000");
	}
	public String getIidProtocolloReg(){
		return (super.getIidProtocolloReg()!=null?super.getIidProtocolloReg():"");
	}
	public String getTipoRec(){
		return "6";
	}
	public String getIdSoggetto(){
		return (super.getIdSoggetto()!=null?super.getIdSoggetto():"");
	}
	
	public String getTipoImmobile(){
		return "F";
	}
	public String getDapImportoAsString(){
		return (super.getDapImporto()!=null?df.format( super.getDapImporto()):"0.00");
	}
	public String getDapMesiAsString(){
		return (super.getDapMesi()!=null?""+super.getDapMesi():"0");
	}
	public String getDapNumeroSoggettiAsString(){
		return (super.getDapNumeroSoggetti()!=null?""+(super.getDapNumeroSoggetti()+1):"1");
	}
	public String getDapDiritto(){
		return (super.getFlagDapDiritto()!=null?(super.getFlagDapDiritto()?"S":"N"):"N");
	}
	public String getDataDap(){
		return (super.getDapData()!=null?super.getDapData().substring(6, 10)+super.getDapData().substring(3, 5)+super.getDapData().substring(0, 2):"00000000");
	}
	public String getEsitoDapAsString(){
		return (super.getEsitoDap()!=null?super.getEsitoDap():"");
	}

}
