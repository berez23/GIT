package it.webred.mui.input.test;

import it.webred.mui.input.MuiFornituraParser;
import it.webred.mui.input.MuiInvalidInputDataException;
import it.webred.mui.input.RowField;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import junit.framework.TestCase;

public class TestMuiFornituraParser extends TestCase {

	/*
	 * Test method for 'it.webred.mui.input.MuiFornituraParser.parse()'
	 */
	public void testParse() {
		try{
			FileInputStream fin = new FileInputStream("./doc/input/Forniture_Formato_txt/F205_20050101_O.TXT");
			MuiFornituraParser parser = new MuiFornituraParser();
			parser.setInput(fin);
			parser.parse();
		}
		catch (FileNotFoundException fnfe){
			fnfe.printStackTrace();
			fail();
		}
		catch (IOException ioe){
			ioe.printStackTrace();
			fail();
		}
		catch (MuiInvalidInputDataException miide){
			miide.printStackTrace();
		}
		catch (Throwable t){
			t.printStackTrace();
			fail();
		}
	}

	/*
	 * Test method for 'it.webred.mui.input.MuiFornituraParser.getNextField(String)'
	 */
	public void testGetNextField() {
		String val="1|2|T|1||2005|10122004|03012005|0||11012005|145011|0||||SALVINI GIULIANO|SLVGLN34L26F205H|F205|0||";
		RowField rf= MuiFornituraParser.getNextField(val);
		assertEquals(rf.field,"1");
		assertEquals(rf.remaining,"2|T|1||2005|10122004|03012005|0||11012005|145011|0||||SALVINI GIULIANO|SLVGLN34L26F205H|F205|0||");
		rf= MuiFornituraParser.getNextField(rf.remaining);
		assertEquals(rf.field,"2");
		assertEquals(rf.remaining,"T|1||2005|10122004|03012005|0||11012005|145011|0||||SALVINI GIULIANO|SLVGLN34L26F205H|F205|0||");
	}

}
