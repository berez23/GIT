package it.webred.diogene.db;

import it.webred.diogene.db.model.DcRel;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class TestDBModel
{

	public static void main(java.lang.String[] args)
	{
		/*
		 try {
		 
		 DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());

		 Connection conn = 
		 DriverManager.getConnection("jdbc:oracle:thin:@rapanui:1521:rapanui","diogenedbdev","diogenedbdev");

		 PreparedStatement stmt = 
		 (PreparedStatement) conn.prepareStatement(
		 "select condition from dc_rel");

		 ResultSet rs = stmt.executeQuery();
		 
		 rs.next();
		 /**    		
		 while(rset.next())
		 {
		 //    		 the first argument is a CLOB
		 oracle.sql.CLOB clb = rset.getCLOB(1);

		 //    		 the second argument is a string..
		 String poString = orset.getString(2);

		 //    		 now use the CLOB inside the program
		 }
		 
		 
		 OPAQUE op = (OPAQUE) rs.getObject("condition"); 
		 
		 oracle.xdb.XMLType xt = oracle.xdb.XMLType.createXML(op);

		 DOMReader reader = new DOMReader ();

		 org.dom4j.Document document = reader.read ( xt.getDOM () );

		 System.out.println(document.toString());
		 
		 XMLType xml = oracle.xdb.XMLType.createXML ( conn, ( (Element) document.getRootElement() ).asXML () );
		 
		 
		 //          return document.getRootElement();
		 
		 } catch (Exception e) {
		 e.printStackTrace();
		 }
		 */

		System.out.println("qui");
		Configuration c = new Configuration().configure("hibernate.cfg.xml");

		SessionFactory sf = c.buildSessionFactory();

		try
		{
			// This step will read hibernate.cfg.xml and prepare hibernate for use

			System.out.println("quo");

			Session session = sf.openSession();

			Query q = session.createQuery("from DC_REL in class DcRel where DC_REL.id=:id");

			DcRel dcrel = new DcRel();
			dcrel.setId(new Long(1));
			//dcrel.setCondition(new HibernateXMLType());

			System.out.println(q.getQueryString());

			q.setProperties(dcrel);
			
			List dcrels = q.list();
			System.out.println("qua");

			Iterator it = dcrels.iterator();
			while (it.hasNext())
			{
				DcRel element = (DcRel) it.next();
				System.out.println(new String(element.getId().toString()));
			}

		}
		catch (Exception e)
		{
			//     System.out.println(e);
			e.printStackTrace();
		}
		finally
		{
			sf.close();

		}

	}

}
