package it.webred.amprofiler.ejb.client;

import it.webred.amprofiler.ejb.AmProfilerSessionFacade;
import it.webred.amprofiler.model.AmKeyValue;
import it.webred.amprofiler.model.AmSection;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Main {
	public static void main(String[] args) throws NamingException {
		Hashtable<String, String> hash = new Hashtable<String, String>();
		hash.put("java.naming.factory.initial",
				"org.jnp.interfaces.NamingContextFactory");
		hash.put("java.naming.factory.url.pkgs",
				"org.jboss.naming:org.jnp.interfaces");
		hash.put("java.naming.provider.url", "localhost");
		InitialContext ctx = new InitialContext(hash);

		AmProfilerSessionFacade facade = (AmProfilerSessionFacade) ctx
				.lookup("AmProfiler/AmProfilerSessionFacadeBean/remote");

		System.out.println("Sezioni:");
		for (AmSection item : facade.findAmSections("PortaleServizi",null)) {
			System.out.println(item.getId() + "     " + item.getName());
		}
		//facade.deleteAmSection(1);
		AmSection section = facade.findAmSectionById(3);
		boolean isNew =(section==null);
	
		if(isNew)section = new AmSection();
		section.setName("Common1");

		section.setAmKeyValues( new  HashSet<AmKeyValue>()) ;
		section.getAmKeyValues().add(new AmKeyValue(section,"PrimaChiave","PrimoValore"));
		section.getAmKeyValues().add(new AmKeyValue(section,"SecondaChiave","SecondoValore"));
		section.getAmKeyValues().add(new AmKeyValue(section,"TerzaChiave","TerzoValore"));
		
		if (isNew)
			facade.createAmSection(section, "PortaleServizi",null);
		else
			facade.updateAmSection(section);
		for (AmSection item : facade.findAmSections("PortaleServizi",null)) {
			AmSection amSection = facade.findAmSectionById(item.getId());
			System.out.println(amSection.getId() + "     " + amSection.getName());
			for (AmKeyValue keyValue : amSection.getAmKeyValues()) {
				System.out.println(keyValue.getKeyConf()+" - "+keyValue.getValueConf());
			}
		}

	}
}