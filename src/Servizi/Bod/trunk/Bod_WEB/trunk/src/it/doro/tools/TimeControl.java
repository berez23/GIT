package it.doro.tools;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeControl {

	public TimeControl() {
	}
	
	public static  String formatDate(Date curDate){
		  String cur = null;
		  try{    
		   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		   if (curDate != null)
		    cur = sdf.format(curDate);   
		  }
		  catch(Exception ex){
			  ex.printStackTrace();  
		  }   
		  return cur;
	}
	
	public Timestamp lessDays(Timestamp current, int gg){
		Timestamp t = null;
		long day = 86399000; //1gg in millisecondi
		long newDay = current.getTime() - (gg*day);

		t = new Timestamp(newDay);
		
		return t;
	}
	
	public Timestamp addDays(Timestamp current, int gg){
		Timestamp t = null;
		long day = 86399000; //1gg in millisecondi
		long newDay = current.getTime() + (gg*day);

		t = new Timestamp(newDay);
		return t;
	}
	
	public Timestamp addMonths(Timestamp current, int mm){
		Timestamp t = null;
		Calendar cal = Calendar.getInstance();
		
		cal.setTimeInMillis(current.getTime());
		/*
		 * Ricordati di aggiungere una unità al mese ottenuto con l'oggetto calendar
		 * perché GEN = 0 e DIC = 11
		 */
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hh = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		
		int mese = cal.get(Calendar.MONTH) + 1;
		int anno = cal.get(Calendar.YEAR);
		
		long year = anno + mm / 12; //il risultato della divisione
		long month = mese + mm % 12; //il resto della divisione
	
		if (month > 12){
			year++;
			month = month - 12;
		}
		
		String strData = Character.fillUpZeroInFront(String.valueOf(year), 4) + "-" + Character.fillUpZeroInFront(String.valueOf(month), 2) + "-" + Character.fillUpZeroInFront(String.valueOf(day), 2) + " " + Character.fillUpZeroInFront(String.valueOf(hh), 2) + ":" + Character.fillUpZeroInFront(String.valueOf(min), 2) + ":00.0";
		t = Timestamp.valueOf(strData.trim());
		
		return t;
	}//-------------------------------------------------------------------------
	
	public Timestamp addYears(Timestamp current, int aa){
		Timestamp t = null;
		Calendar cal = Calendar.getInstance();
		
		cal.setTimeInMillis(current.getTime());
		/*
		 * Ricordati di aggiungere una unità al mese ottenuto con l'oggetto calendar
		 * perché GEN = 0 e DIC = 11
		 */
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);

		int hh = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);

		year = year + aa;		
		
		String strData = Character.fillUpZeroInFront(String.valueOf(year), 4) + "-" + Character.fillUpZeroInFront(String.valueOf(month), 2) + "-" + Character.fillUpZeroInFront(String.valueOf(day), 2) + " " + Character.fillUpZeroInFront(String.valueOf(hh), 2) + ":" + Character.fillUpZeroInFront(String.valueOf(min), 2) + ":00.0";
		t = Timestamp.valueOf(strData.trim());
		
		return t;
	}
	
	public Timestamp addHours(Timestamp current, int hh){
		Timestamp t = null;
		Calendar cal = Calendar.getInstance();
		
		cal.setTimeInMillis(current.getTime());
		/*
		 * Ricordati di aggiungere una unità al mese ottenuto con l'oggetto calendar
		 * perché GEN = 0 e DIC = 11
		 */
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);

		int ora = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);

		ora = ora + hh;
		if (ora > 23){
			ora = ora - 24;				
		}
		
		String strData = Character.fillUpZeroInFront(String.valueOf(year), 4) + "-" + Character.fillUpZeroInFront(String.valueOf(month), 2) + "-" + Character.fillUpZeroInFront(String.valueOf(day), 2) + " " + Character.fillUpZeroInFront(String.valueOf(ora), 2) + ":" + Character.fillUpZeroInFront(String.valueOf(min), 2) + ":00.0";
		t = Timestamp.valueOf(strData.trim());
		
		return t;		
	}//-------------------------------------------------------------------------
	
	public Timestamp addMinutes(Timestamp current, int min){
		Timestamp t = null;
		Calendar cal = Calendar.getInstance();
		
		cal.setTimeInMillis(current.getTime());
		/*
		 * Ricordati di aggiungere una unità al mese ottenuto con l'oggetto calendar
		 * perché GEN = 0 e DIC = 11
		 */
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);

		int hh = cal.get(Calendar.HOUR_OF_DAY);
		int mm = cal.get(Calendar.MINUTE);

		long hour = hh + min / 60; //il risultato della divisione
		long minute = mm + min % 60; //il resto della divisione
	
		if (minute > 60){
			hour++;
			minute = minute - 60;
		}
		//String strData = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":00.0";
		String strData = Character.fillUpZeroInFront(String.valueOf(year), 4) + "-" + Character.fillUpZeroInFront(String.valueOf(month), 2) + "-" + Character.fillUpZeroInFront(String.valueOf(day), 2) + " " + Character.fillUpZeroInFront(String.valueOf(hour), 2) + ":" + Character.fillUpZeroInFront(String.valueOf(minute), 2) + ":00.0";		
		t = Timestamp.valueOf(strData.trim());
		
		return t;				
	}//-------------------------------------------------------------------------
	
	public String getDay(Timestamp current){
		String giorno = "";
		if (current != null){
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(current.getTime());
			
			int gg = cal.get(Calendar.DAY_OF_MONTH);
			if (gg < 10)
				giorno = "0" + String.valueOf(gg);
			else
				giorno = String.valueOf(gg);
		}
		
		return giorno;
	}//-------------------------------------------------------------------------
	
	public static String getMonth(Timestamp current){
		String mese = "";
		if (current != null){
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(current.getTime());
			/*
			 * Ricordati di aggiungere una unità al mese ottenuto con l'oggetto calendar
			 * perché GEN = 0 e DIC = 11
			 */		
			int mm = cal.get(Calendar.MONTH)+1;
			
			if (mm < 10)
				mese = "0" + String.valueOf(mm);
			else
				mese = String.valueOf(mm);
		}
		
		return mese;
	}//-------------------------------------------------------------------------

	public static String getYear(Timestamp current){
		String anno = "";
		if (current != null){
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(current.getTime());
			
			int aa = cal.get(Calendar.YEAR);
			
			anno = String.valueOf(aa);
		}
		
		return anno;
	}//-------------------------------------------------------------------------

	public String getHour(Timestamp current){
		String ora = "";
		if (current != null){
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(current.getTime());
			
			int hh = cal.get(Calendar.HOUR_OF_DAY);
			if (hh < 10)
				ora = "0" + String.valueOf(hh);
			else
				ora = String.valueOf(hh);
		}
		
		return ora;
	}//-------------------------------------------------------------------------
	
	public String getMinute(Timestamp current){
		String minuti = "";
		if (current != null){
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(current.getTime());
			
			int mm = cal.get(Calendar.MINUTE);

			if (mm < 10)
				minuti = "0" + String.valueOf(mm);
			else
				minuti = String.valueOf(mm);
		}
		
		return minuti;
	}//-------------------------------------------------------------------------

	public Timestamp setDay(Timestamp current, String gg){
		Timestamp t = null;
		
		if (current != null){
			String mese = this.getMonth(current);
			String anno = this.getYear(current);
			String ora = this.getHour(current);
			String min = this.getMinute(current);				
			
			t = Timestamp.valueOf(anno + "-" + mese + "-" + gg + " " + ora + ":" + min + ":00.0");			
		}
		
		return t;	
	}//-------------------------------------------------------------------------

	public Timestamp setMonth(Timestamp current, String mm){
		Timestamp t = null;

		if (current != null){
			String giorno = this.getDay(current);
			String anno = this.getYear(current);
			String ora = this.getHour(current);
			String min = this.getMinute(current);				
			
			t = Timestamp.valueOf(anno + "-" + mm + "-" + giorno + " " + ora + ":" + min + ":00.0");
			
		}
		
		return t;	
	}//-------------------------------------------------------------------------

	public Timestamp setYear(Timestamp current, String aa){
		Timestamp t = null;

		if (current != null){
			String giorno = this.getDay(current);
			String mese = this.getMonth(current);
			String ora = this.getHour(current);
			String min = this.getMinute(current);				
			
			t = Timestamp.valueOf(aa + "-" + mese + "-" + giorno + " " + ora + ":" + min + ":00.0");
			
		}
		
		return t;	
	}//-------------------------------------------------------------------------

	public Timestamp setHour(Timestamp current, String hh){
		Timestamp t = null;
		
		if (current != null){

			String giorno = this.getDay(current);
			String mese = this.getMonth(current);
			String anno = this.getYear(current);
			String min = this.getMinute(current);
			
			t = Timestamp.valueOf(anno + "-" + mese + "-" + giorno + " " + hh + ":" + min + ":00.000000000");
			
		}
		
		return t;	
	}//-------------------------------------------------------------------------

	public Timestamp setMinute(Timestamp current, String min){
		Timestamp t = null;
		if (current != null){

			String giorno = this.getDay(current);
			String mese = this.getMonth(current);
			String anno = this.getYear(current);
			String ora = this.getHour(current);				

			t = Timestamp.valueOf(anno + "-" + mese + "-" + giorno + " " + ora + ":" + min + ":00.000000000");
		}
		
		return t;	
	}//-------------------------------------------------------------------------
	
	public Timestamp createDate(String aa, String mm, String gg, String hh, String min){
		Timestamp t = null;
		
		if (aa != null && !aa.trim().equals("")){
			if (mm != null && !mm.trim().equals("")){
				if (gg != null && !gg.trim().equals("")){
					if (hh != null && !hh.trim().equals("")){
						if (min != null && !min.trim().equals("")){
							t = Timestamp.valueOf(aa.trim() + "-" + mm.trim() + "-" + gg.trim() + " " + hh.trim() + ":" + min.trim() + ":00.000000000");
						}
					}
				}
			}
		}
		
		return t;
	}//-------------------------------------------------------------------------
	
	public int getDayOfWeek(Timestamp t){
		int dayOfWeek = -1;

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(t.getTime());
		
		dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		/*
		 * 1 : domenica
		 * 2 : lunedi
		 * 3 : martedi
		 * 4 : mercoledi
		 * 5 : giovedi
		 * 6 : venerdi
		 * 7 : sabato
		 */ 
		return dayOfWeek;
	}
	
	public String getDayOfWeekName(int key){
		String s = "";
		
		switch (key) {
			case 1 :
				s = "Dom";
				break;
			case 2 :
				s = "Lun";
				break;
			case 3 :
				s = "Mar";
				break;
			case 4 :
				s = "Mer";
				break;
			case 5 :
				s = "Gio";
				break;
			case 6 :
				s = "Ven";
				break;
			case 7 :
				s = "Sab";
				break;

		}
		
		return s.trim();
	}
	
	public String getMonthName(int key){
		String s = "";
		
		switch (key) {
			case 1 :
				s = "Gen";
				break;
			case 2 :
				s = "Feb";
				break;
			case 3 :
				s = "Mar";
				break;
			case 4 :
				s = "Apr";
				break;
			case 5 :
				s = "Mag";
				break;
			case 6 :
				s = "Giu";
				break;
			case 7 :
				s = "Lug";
				break;
			case 8 :
				s = "Ago";
				break;
			case 9 :
				s = "Set";
				break;
			case 10 :
				s = "Ott";
				break;
			case 11 :
				s = "Nov";
				break;
			case 12 :
				s = "Dic";
				break;
		}
		return s.trim();
	}

	public String parsItalyDate(Timestamp curDate){
		String cur = null;
		try{				
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.applyPattern("dd/MM/yyyy");
			if (curDate != null)
				cur = sdf.format(curDate);			
		}
		catch(Exception ex){
			ex.printStackTrace();		
		}
		return cur;
	}//-------------------------------------------------------------------------
	
	public String shortFormat(Timestamp t){
		String s = "";
		if (t != null){
			String gg = this.getDay(t);
			String mm = this.getMonth(t);
			String aa = this.getYear(t);
			s = gg + "-" + mm + "-" + aa;			
		}
		
		return s.trim();
	}//-------------------------------------------------------------------------
	
	public String longFormat(Timestamp t){
		String s = "";
		if (t != null){
			String gg = this.getDay(t);
			String mm = this.getMonth(t);
			String aa = this.getYear(t);
			String hh = this.getHour(t);
			String min = this.getMinute(t);
			s = gg + "-" + mm + "-" + aa + " " + hh + ":" + min;			
		}
		
		return s.trim();
	}//-------------------------------------------------------------------------
	
	public Timestamp getStartDay(Timestamp t){		
			
		if (t != null){
			t = this.setHour(t, "00");
			t = this.setMinute(t, "00");			
		}
	
		return t;		
			
	}//-------------------------------------------------------------------------

	public Timestamp getEndDay(Timestamp t){			
		if (t != null){
			t = this.setHour(t, "23");
			t = this.setMinute(t, "59");	
		}
		
		return t;
									
	}//-------------------------------------------------------------------------	
	
	public Timestamp getLastOfMonth(Timestamp t){
		
		Timestamp tmp = this.addMonths(t, 1);
		String aa = String.valueOf(this.getYear(tmp));
		String mm = String.valueOf(this.getMonth(tmp));
		String gg = String.valueOf(this.getYear(tmp));
		
		tmp = this.createDate(aa.trim(), mm.trim(), "01", "23", "59");

		Timestamp endMonth = this.lessDays(tmp, 1); 		

		return endMonth;
	}//-------------------------------------------------------------------------
	
	public Timestamp getFirstOfMonth(Timestamp t){
		
		String aa = String.valueOf(this.getYear(t));
		String mm = String.valueOf(this.getMonth(t));
		String gg = String.valueOf(this.getYear(t));

		Timestamp startMonth = this.createDate(aa.trim(), mm.trim(), "01", "00", "00");; 		

		return startMonth;
	}//-------------------------------------------------------------------------

	public Timestamp fromFormatStringToTimestampMorning(String s){
		Timestamp t = null;
		/*
		 * Questo metodo funziona nel caso di data formattata cosi: 
		 * gg-mm-aaaa
		 * gg/mm/aaaa
		 */
		if (s != null  && !s.trim().equalsIgnoreCase("__-__-____") && s.length() == 10){
			String gg = s.substring(0, 2);
			String mm = s.substring(3, 5);
			String aa = s.substring(6, 10);
			t = this.createDate(aa, mm, gg, "00", "00");
		}
			
		return t;
	}//-------------------------------------------------------------------------
	
	public static Date fromFormatStringToDate(String giorno, String orario){
		Date d = null;
		/*
		 * Questo metodo funziona nel caso di data formattata cosi: 
		 * gg-mm-aaaa
		 * gg/mm/aaaa
		 */
		if (giorno != null  && !giorno.trim().equalsIgnoreCase("") && giorno.length() == 10){
			String gg = giorno.substring(0, 2);
			String mm = giorno.substring(3, 5);
			String aa = giorno.substring(6, 10);
			String hh = "00";
			String min = "00";
			if (orario != null && !orario.trim().equalsIgnoreCase("") && orario.length() == 5){
				hh = orario.substring(0, 2);
				min = orario.substring(3, 5);
			}
			
			if (aa != null && !aa.trim().equals("")){
				if (mm != null && !mm.trim().equals("")){
					if (gg != null && !gg.trim().equals("")){
						if (hh != null && !hh.trim().equals("")){
							if (min != null && !min.trim().equals("")){
								Timestamp t = Timestamp.valueOf(aa.trim() + "-" + mm.trim() + "-" + gg.trim() + " " + hh.trim() + ":" + min.trim() + ":00.000000000");
								d = new Date(t.getTime());
							}
						}
					}
				}
			}
				
			
		}
			
		return d;
	}//-------------------------------------------------------------------------
	
	public Timestamp fromFormatStringToTimestamp(String s){
		Timestamp t = null;
		/*
		 * Questo metodo funziona nel caso di data formattata cosi: 
		 * gg-mm-aaaa
		 * gg/mm/aaaa
		 */
		if (s != null  && !s.trim().equalsIgnoreCase("__-__-____") && !s.trim().equalsIgnoreCase("") && s.length() == 10){
			String gg = s.substring(0, 2);
			String mm = s.substring(3, 5);
			String aa = s.substring(6, 10);
			t = this.createDate(aa, mm, gg, "13", "00");
		}
			
		return t;
	}//-------------------------------------------------------------------------

	public Timestamp fromFormatStringToTimestampEvening(String s){
		Timestamp t = null;
		/*
		 * Questo metodo funziona nel caso di data formattata cosi: 
		 * gg-mm-aaaa
		 * gg/mm/aaaa
		 */
		if (s != null  && s.length() == 10){
			String gg = s.substring(0, 2);
			String mm = s.substring(3, 5);
			String aa = s.substring(6, 10);
			t = this.createDate(aa, mm, gg, "23", "59");
		}
			
		return t;
	}//-------------------------------------------------------------------------

	public Timestamp parseStringToTimestamp(String s){
		Timestamp t = null;
		if (s != null && !s.trim().equalsIgnoreCase("")){
			t = Timestamp.valueOf(s.trim());
		}
		return t;
	}//-------------------------------------------------------------------------

	public static  String parsItalyDate(Date curDate){
		String cur = null;
		try{				
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.applyPattern("dd/MM/yyyy");
			if (curDate != null)
				cur = sdf.format(curDate);			
		}
		catch(Exception ex){
			ex.printStackTrace();		
		}			
		return cur;
	}//-------------------------------------------------------------------------

	
	
}//CLASSE-----------------------------------------------------------------------
