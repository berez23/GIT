package it.webred.diogene.visualizzatore.runtime.paging;

import java.sql.ResultSet;
import java.util.Iterator;

import org.hibernate.type.Type;


public interface DataBaseRowConverter {

    

    public Object toObject(ResultSet row) throws Exception;
    
}
