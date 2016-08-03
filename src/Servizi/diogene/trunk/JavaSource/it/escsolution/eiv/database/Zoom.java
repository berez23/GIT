/*
 * Created on 14-apr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.eiv.database;
import java.util.Vector;
/**
 * @author silviat
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Zoom {
	public String nome;
	public String colDesc;
	public String pkZoom;
	public int numeroParametri;
	public String pk_classe;
	public String tablename;
	public String keyColumn;
	public String whereClause;
	public Vector keyColumnVec;
	public Vector descColumVec ;
	public Vector parametri;
	public String descrizione;
	public Vector XCENTROID;
	public Vector YCENTROID;
	public Vector FWIDTH;
	public Vector FHEIGHT;
	public Vector listParam;
	
	 public void CreaListParam(){
	 	listParam=new Vector();
	 }
	public void CreaVector(){
		parametri=new Vector(); 
		 }
	public void AddVector(String param){
		parametri.addElement(param);
		 }
	public int getNumpar(){
		return numeroParametri;
	}
	public void setNumpar(int num){
			numeroParametri=num;
		}
	 public String getKeyColumn(){
	 	return keyColumn;
	 }
	 public String getTableName(){
	 	return tablename;
	 }
	 public String getClasse(){
	 	 return pk_classe;
	 }
	 public String getNome(){
	 	return nome;
	 }
	public String getColDesc(){
		   return colDesc;
		}
	public String getDescrizione(){
		return descrizione;
	}
	public String getPkZoom(){
		   return pkZoom;
		}
	public int getNumeroParametri(){
		   return numeroParametri;
		}
	public void setNome(String name){
		nome=name;
	}
	public void setColDesc(String Colonna){
		colDesc=Colonna;
		}
	public void setPkZoom(String id){
		pkZoom=id;
	}
	public void setNumeroParametri(int num){
			numeroParametri=num;
			}
	public void setClasse(String classe){
		pk_classe=classe;
	}
	public void setTableName(String nometab){
		tablename=nometab;
	}
	public void setKeyColumn(String chiave){
		keyColumn=chiave;
	}

}
