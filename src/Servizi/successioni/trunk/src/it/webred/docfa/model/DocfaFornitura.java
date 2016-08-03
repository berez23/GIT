package it.webred.docfa.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DocfaFornitura  implements java.io.Serializable {


	 private Long numeroDocfa;
     private Date dataFornitura;
     private Date dataInizio;


    // Constructors

    /** default constructor */
    public DocfaFornitura() {
    }
    
    /** full constructor */
    public DocfaFornitura(Long numeroDocfa, Date dataFornitura,Date dataInizio) {
        this.numeroDocfa = numeroDocfa;
        this.dataFornitura = dataFornitura;
        this.dataInizio = dataInizio;
    }
	private java.lang.String dateToString(java.util.Date d, String formato)
	{
		try
		{
			String dataOut = "";
			if ((d != null) && (d.toString().length() > 0))
			{
				dataOut = new java.text.SimpleDateFormat(formato).format(d);
			}
			return dataOut;
		}
		catch (Exception e)
		{
			return "";
		}
					
	}
	public Date getDataFornitura()
	{
		return dataFornitura;
	}
	public Long getDataFornituraComeIId()
	{
		return Long.valueOf(dateToString(dataFornitura,"yyyyMMdd")); 
	}
	public void setDataFornitura(Date dataFornitura)
	{
		this.dataFornitura = dataFornitura;
	}

	public Date getDataInizio()
	{
		return dataInizio;
	}

	public void setDataInizio(Date dataInizio)
	{
		this.dataInizio = dataInizio;
	}

	public Long getNumeroDocfa()
	{
		return numeroDocfa;
	}

	public void setNumeroDocfa(Long numeroDocfa)
	{
		this.numeroDocfa = numeroDocfa;
	}
    

}
