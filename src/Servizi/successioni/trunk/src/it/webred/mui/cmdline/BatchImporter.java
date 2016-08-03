package it.webred.mui.cmdline;

import it.webred.mui.input.MuiFornituraParser;
import it.webred.mui.input.MuiInvalidInputDataException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;

public class BatchImporter implements FilenameFilter {

	public static void main (String[] args) {
		BatchImporter bi = new BatchImporter();
		File f = new File(args[0]);
		String[] files = f.list(bi);
		bi.importFiles(files != null? files : args,files != null? args[0] : null);
	}
	private void importFiles(String[] args,String prefix) {
		for (int i = 0; i < args.length; i++) {
			try {
				if(prefix != null){
					args[i] = prefix + System.getProperty("file.separator") + args[i];
				}
				System.out.println("importing file = "+args[i]);
				File f = new File(args[i]);
				FileInputStream fin = new FileInputStream(f);
				MuiFornituraParser parser = new MuiFornituraParser();
				parser.setInput(fin);
				parser.parse();
				parser.parseLastLine(parser.getLastLine( f));
				parser.parseNotes();
				System.out.println("file = "+args[i]+" imported");
			} catch (MuiInvalidInputDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public boolean accept(File dir, String name){
		return (name.toLowerCase().endsWith(".txt"));
	}
}
