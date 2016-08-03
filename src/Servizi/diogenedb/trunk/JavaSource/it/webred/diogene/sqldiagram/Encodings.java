package it.webred.diogene.sqldiagram;

import static java.util.Collections.synchronizedMap;

import java.util.HashMap;
import java.util.Map;

public class Encodings
{
	private static final Map<String,String> fromCharUTF8Entities = synchronizedMap(new HashMap<String,String>());
	private static final Map<String,String> fromUTF8EntitiesToChar = synchronizedMap(new HashMap<String,String>());
	static {
		fromCharUTF8Entities.put("\r", "&#x000D;");
		fromUTF8EntitiesToChar.put("&#x000D;", "\r");
		fromCharUTF8Entities.put("\n", "&#x000A;");
		fromUTF8EntitiesToChar.put("&#x000A", "\n");
		fromCharUTF8Entities.put("\u0020", "&#x0020;");
		fromUTF8EntitiesToChar.put("&#x0020;", "\u0020");
	}
	/*
	public static String mapFromCharToUTF8Entities(String s)
	{
		if (s != null)
			for (String key : fromCharUTF8Entities.keySet())
			{
				String regexp = "\\Q" + key + "\\E";
				s = s.replaceAll(regexp, fromCharUTF8Entities.get(key));
			}
		return s;
	}
	public static String mapFromUTF8EntitiesToChar(String s)
	{
		if (s != null)
			for (String key : fromUTF8EntitiesToChar.keySet())
			{
				String regexp = "\\Q" + key + "\\E";
				s = s.replaceAll(regexp, fromUTF8EntitiesToChar.get(key));
			}
		return s;
	}
	*/
}
