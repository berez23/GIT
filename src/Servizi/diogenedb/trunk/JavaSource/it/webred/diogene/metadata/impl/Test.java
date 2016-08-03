package it.webred.diogene.metadata.impl;

import it.webred.diogene.metadata.*;
import it.webred.diogene.metadata.beans.*;
import static it.webred.utils.MetadataConstants.*;
import java.sql.*;
import java.util.*;

public class Test
{
	public static void main(String[] args) throws Exception {
		
		//TEST STAMPA DATATYPES
		System.out.println("DATA TYPES:");
		List<String> al = getDataTypesFromDBName("MySql");
		for (String str : al) {
			System.out.println("\n" + str);
			System.out.println("JAVA TYPES:");
			List<String> al1 = getJavaTypeNamesFromDataType(str, "MySql");
			for (String str1 : al1) {
				System.out.println(str1);
				System.out.println("DATA TYPES PER QUESTO JAVA TYPE:");
				List<String> al2 = getDataTypeNamesFromJavaType(str1, "MySql");
				for (String str2 : al2) {
					System.out.println(str2);
				}
			}		
		}
		
		//TEST TRANSLATE
		List<String> list = new ArrayList<String>();
		list.add("BLOB");
		list.add("BLOB");
		list.add("BLOB");
		al = translateDataTypes(list, "MySql", "Oracle");
		System.out.println("\nJAVA TYPES CORRISPONDENTI:");
		for (String str1 : al) {
			System.out.println(str1);
		}
		
		
		//STAMPO LE DESCRIZIONI DB
		System.out.println("\nDB PRESENTI:");
		for (String str : getDBDescriptions()) {
			System.out.println(str);
		}
		MetadataManager mdm = new MetadataManager("Oracle");
		
		MetadataFactory mf = mdm.getMetadataFactory();
		
		Connection conn = mf.getConnection("jdbc:oracle:thin:@rapanui:1521:rapanui", "diogenedbdev", "diogenedbdev", true);
		List<Schema> listS = mf.getSchemas(conn);
		//STAMPO GLI SCHEMI
		System.out.println("\nNOMI SCHEMI:");
		for (Schema s : listS) {
			System.out.println(s.getTableSchema());
		}
		String nomeSchema = "DIOGENEDBDEV";
		List<Table> listT = mf.getTables(conn, null, nomeSchema, null, null);
		// STAMPO LE TABELLE
		System.out.println("\nNOMI TABELLE DI " + nomeSchema + ":");
		for (Table t : listT) {
			System.out.println(t.getName());
		}
		String nomeTabella = "DC_COLUMN";
		List<Column> listC = mf.getColumns(conn, null, nomeSchema, nomeTabella, null);
		// STAMPO LE COLONNE
		System.out.println("\nNOMI COLONNE DI " + nomeTabella + ":");
		for (Column c : listC) {
			System.out.println(c.getName());
			System.out.println(c.getColType());
		}
		mf.closeConnection(conn);
	}
}
