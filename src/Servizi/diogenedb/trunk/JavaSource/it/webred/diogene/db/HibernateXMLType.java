package it.webred.diogene.db;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import oracle.sql.OPAQUE;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;


public class HibernateXMLType implements UserType, Serializable {
   
   private static final Class returnedClass = byte[].class; // *** String.class;
   private static final int[] SQL_TYPES = new int[] { oracle.xdb.XMLType._SQL_TYPECODE };
   
   /* (non-Javadoc)
    * @see net.sf.hibernate.UserType#sqlTypes()
    */
   public int[] sqlTypes() {
      return SQL_TYPES;
   }

   /* (non-Javadoc)
    * @see net.sf.hibernate.UserType#returnedClass()
    */
   public Class returnedClass() {
      return returnedClass;
   }

   /* (non-Javadoc)
    * @see net.sf.hibernate.UserType#equals(java.lang.Object, java.lang.Object)
    */
   public boolean equals(Object x, Object y) throws HibernateException {
      
	   return (x == y) 
	      || (x != null 
	        && y != null 
	        && java.util.Arrays.equals((byte[]) x, (byte[]) y)); 
   }

   /* (non-Javadoc)
    * @see net.sf.hibernate.UserType#nullSafeGet(java.sql.ResultSet, java.lang.String[], java.lang.Object)
    */
   public Object nullSafeGet(ResultSet rs, String[] names, Object arg2)
      throws HibernateException, SQLException {
/*
	   try {
         OracleResultSet ors = (OracleResultSet) rs;
         OPAQUE op = ors.getOPAQUE(names[0]);               
         oracle.xdb.XMLType xt= oracle.xdb.XMLType.createXML(op);
      
         if (xt!= null)
         return new String(xt.getStringVal());
         else
        	 return null;
	   } catch (Exception e ) {
		  System.out.println("????????????????????????????");
		  throw new HibernateException(e);
	   }
	   */
       OracleResultSet ors = (OracleResultSet) rs;
       OPAQUE op = ors.getOPAQUE(names[0]);               
       oracle.xdb.XMLType xt= oracle.xdb.XMLType.createXML(op);
    
       if (xt!= null)
       return xt.getStringVal().getBytes();
       else
      	 return null;

   }

   /* (non-Javadoc)
    * @see net.sf.hibernate.UserType#nullSafeSet(java.sql.PreparedStatement, java.lang.Object, int)
    */
   public void nullSafeSet(PreparedStatement st, Object value, int index)
      throws HibernateException, SQLException {
/*      
	   try {
	   OraclePreparedStatement ost = (OraclePreparedStatement) st;
      
      ost.setOPAQUE(index, oracle.xdb.XMLType.createXML(st.getConnection(), (String) value));
	   } catch (Exception e ) {
			  System.out.println("????????????????????????????");
			  throw new HibernateException(e);
		   }
 */
	   
      try {

   	   OraclePreparedStatement ost = (OraclePreparedStatement) st;
       
       ost.setOPAQUE(index, oracle.xdb.XMLType.createXML(st.getConnection(), new ByteArrayInputStream((byte[]) value)));
    	  
       } catch ( Exception e ) {

           e.printStackTrace ();

           throw new HibernateException(e);

       } 	   
   }

   /* (non-Javadoc)
    * @see net.sf.hibernate.UserType#deepCopy(java.lang.Object)
    */
   public Object deepCopy(Object value) throws HibernateException {
      
      if (value == null) return null;
      

      // return oracle.xdb.XMLType.createXML((OPAQUE) value);
	return value;
            
   }

   /* (non-Javadoc)
    * @see net.sf.hibernate.UserType#isMutable()
    */
   public boolean isMutable() {
      return false;
   }

public int hashCode(Object arg0) throws HibernateException {
	// TODO Auto-generated method stub
	return arg0.hashCode();
}

public Serializable disassemble(Object arg0) throws HibernateException {
	// TODO Auto-generated method stub
	return (Serializable) arg0;
}

public Object assemble(Serializable arg0, Object arg1) throws HibernateException {
	// TODO Auto-generated method stub
	return arg0;
}

public Object replace(Object arg0, Object arg1, Object arg2) throws HibernateException {
	// TODO Auto-generated method stub
	return arg0;
}

} 