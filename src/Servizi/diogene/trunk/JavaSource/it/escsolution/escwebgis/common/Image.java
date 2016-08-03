 package it.escsolution.escwebgis.common;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Image
{
  /*-------------------------
   *   Get the Blob image
   *------------------------*/
  public static byte[] getImage(Connection conn, String iImage, String codEnte)
       throws Exception, SQLException
  {

    String req = "" ;
    Blob img ;
    byte[] imgData = null ;
    Statement stmt = conn.createStatement ();
    
    // Query
    req = "Select image From EWG_IMAGES i Where i.IMAGE_NAME = '" + iImage +"'  and codente='" + codEnte + "'";
    
    ResultSet rset  = stmt.executeQuery ( req ); 
    
    while (rset.next ())
    {    
      img = rset.getBlob(1);
      if (img!=null) 
    	  imgData = img.getBytes(1,(int)img.length());
    }    
    
    rset.close();
    stmt.close();
    
    return imgData ;

  }
  
}  
