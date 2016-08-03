package it.webred.mui.http;

import java.text.SimpleDateFormat;

public class FieldConverter {
	private static SimpleDateFormat _formatter = new SimpleDateFormat("dd/MM/yyyy");

	private static SimpleDateFormat _parser = new SimpleDateFormat("ddMMyyyy");


	private ThreadLocal _tl = new ThreadLocal();

	// private String data;

	public String getData() {
//		Logger.log().error("FieldConverter.getData ", _tl.get());
		return (String) _tl.get();
	}

	public void setData(String data) {
		if (data != null) {
			try {
				_tl.set(_formatter.format(_parser.parse(data)));
			} catch (Exception e) {
				_tl.set(null);
			}
		}
		else{
			_tl.set(null);
		}
//		Logger.log().error("FieldConverter.setData ", _tl.get());
	}
	public String toString(){
		return String.valueOf(_tl.get());
	}

}
