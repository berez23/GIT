package it.webred.ct.service.carContrib.data.access.common;

import it.webred.ct.service.carContrib.data.access.common.utility.StringUtility;
import it.webred.ct.service.carContrib.data.access.ici.dto.DatiIciDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//1.
		ArrayList<DatiIciDTO> lista = new ArrayList<DatiIciDTO>();
		DatiIciDTO ogg = new DatiIciDTO();
		ogg.setAnnoRifConfr("2000");
		lista.add(ogg);
		ogg = new DatiIciDTO();
		ogg.setAnnoRifConfr("1999");
		lista.add(ogg);
		ogg = new DatiIciDTO();
		ogg.setAnnoRifConfr("2001");
		lista.add(ogg);
		ogg = new DatiIciDTO();
		ogg.setAnnoRifConfr("2007");
		lista.add(ogg);
		DatiIciDTO[] arr = new  DatiIciDTO[4]; 
		DatiIciDTO[] arr1 = lista.toArray(arr);
		System.out.println("-- PRIMA DEL SORT --" ) ; 
		for (int i = 0; i < arr1.length; i++) {
			System.out.println("ele " + i + " valore: " + arr1[i].getAnnoRifConfr()); 
		}
		System.out.println("-- DOPO IL SORT --" ) ; 
		Arrays.sort(arr1,ogg);
		for (int i = 0; i < arr1.length; i++) {
			System.out.println("ele " + i + " valore: " + arr1[i].getAnnoRifConfr()); 
		}
		List<DatiIciDTO> listaOrdinata = Arrays.asList(arr1);
		System.out.println("-- LISTA --" ) ; 
		for (DatiIciDTO ele:  listaOrdinata  ) {
			System.out.println(" valore: " + ele.getAnnoRifConfr()); 
		}
		//2.
		lista = new ArrayList<DatiIciDTO>();
		ogg = new DatiIciDTO();
		ogg.setAnnoDenuncia("2000");
		lista.add(ogg);
		ogg = new DatiIciDTO();
		ogg.setAnnoDenuncia("1999");
		lista.add(ogg);
		ogg = new DatiIciDTO();
		ogg.setAnnoDenuncia("2001");
		lista.add(ogg);
		ogg = new DatiIciDTO();
		ogg.setAnnoDenuncia("2007");
		lista.add(ogg);
		arr = new  DatiIciDTO[4]; 
		arr1 = lista.toArray(arr);
		System.out.println("-- 2. PRIMA DEL SORT --" ) ; 
		for (int i = 0; i < arr1.length; i++) {
			System.out.println("ele " + i + " valore: " + arr1[i].getAnnoDenuncia()); 
		}
		System.out.println("-- 2. DOPO IL SORT --" ) ; 
		Arrays.sort(arr1,ogg);
		for (int i = 0; i < arr1.length; i++) {
			System.out.println("ele " + i + " valore: " + arr1[i].getAnnoDenuncia() ); 
		}
		listaOrdinata = Arrays.asList(arr1);
		System.out.println("-- 2. LISTA --" ) ; 
		for (DatiIciDTO ele:  listaOrdinata  ) {
			System.out.println(" valore: " + ele.getAnnoDenuncia()); 
		}
		//con il metodo
		listaOrdinata = riordina(lista, false);
		System.out.println("-- LISTA ORDINATA TRAMITE IL METODO--" ) ; 
		for (DatiIciDTO ele:  listaOrdinata  ) {
			System.out.println(" valore: " + ele.getAnnoDenuncia()); 
		}
		/*
		String strNum= "54.367";
		Double num = Double.parseDouble(strNum);
		String numFormatted = StringUtility.DFEURO.format(num);
		System.out.println("formattata " + numFormatted); 
		
		//
		String str1 = "0012340";
		str1 = "00012";
		String str3 = removeLeadingZero(str1);
		System.out.println("str3: " + str3); 
		
		//
		Date data = new Date();
		String dataStr="";
		String fmt ="dd/MM/yyyy";
		SimpleDateFormat df = new SimpleDateFormat(fmt);
		dataStr = df.format(data);
		System.out.println(" data: " + dataStr);
		*/
	}
	
	
	public static String removeLeadingZero(String str) {
		if (str==null || str.length() == 0)
			return str; 
		
		String retVal=new String(str);
		int i=0;
		System.out.println("lunghezza: " + str.length() ); 
		while (i<str.length()) {
			System.out.println("i: " + i);
			System.out.println("str: " + str );
			System.out.println("retVal: " + retVal ); 
			if(str.charAt(i)=='0' && str.length() >i+1 ) {
				retVal=str.substring(i+1);
				System.out.println("retVal dopo: " + retVal ); 
			}else
				break;
			i++;
		}
		return retVal;
	}
	
	private static List<DatiIciDTO>  riordina(ArrayList<DatiIciDTO> lista, boolean ordinamentoCrescente) {
		if (lista ==null || lista.size() == 0)
			return lista;
		List<DatiIciDTO> listaOrdinata = new ArrayList<DatiIciDTO> ();
		DatiIciDTO[] arr = new  DatiIciDTO[lista.size()]; 
		DatiIciDTO[] arr1 = lista.toArray(arr);
		Arrays.sort(arr1,lista.get(0));
		listaOrdinata = Arrays.asList(arr1);
		if (!ordinamentoCrescente)	{
			listaOrdinata = new ArrayList<DatiIciDTO>();
			System.out.println("arr1.length: " + arr1.length ); 
			for(int i=arr1.length - 1; i >= 0; i-- ) {
				System.out.println("SCORRIMENTO ARR1 " ); 
				DatiIciDTO ele = arr1[i];
				listaOrdinata.add(ele);
			}
		}
		return listaOrdinata;	
	}
	
}
