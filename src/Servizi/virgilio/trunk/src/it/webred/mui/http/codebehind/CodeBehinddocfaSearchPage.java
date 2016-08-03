package it.webred.mui.http.codebehind;

import it.webred.docfa.model.Docfa;
import it.webred.docfa.model.DocfaFornitura;
import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.http.AbstractPage;
import it.webred.mui.http.MuiApplication;
import it.webred.mui.http.MuiBaseServlet;
import it.webred.mui.http.WhereClause;
import it.webred.mui.http.codebehind.pagination.HibernateWhereQueryDisplayTagPaginator;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.skillbill.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.displaytag.pagination.PaginatedList;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class  CodeBehinddocfaSearchPage extends AbstractPage {

	private static final Log log = LogFactory.getLog(CodeBehinddocfaSearchPage.class);

	@Override
	protected boolean controlImpl(HttpServletRequest req,
			HttpServletResponse resp, MuiBaseServlet servlet)
			throws ServletException, IOException {
		if (req.getMethod().equalsIgnoreCase("post") ) {
			//PaginatedList docfas = getPaginatedList(req);
			List docfas = getPaginatedList(req);
			req.setAttribute("docfas", docfas);
			req.setAttribute("requestedUri", "docfaList.jsp");
			req.getRequestDispatcher("docfaList.jsp").include(req, resp);
			return false;
		} else if (req.getQueryString() != null
				&& !req.getQueryString().trim().equals("")) {
			req.setAttribute("requestedUri", "docfaList.jsp");
			req.getRequestDispatcher("docfaList.jsp").include(req, resp);
			return false;
		}
		return true;
	}

	private List getPaginatedList(HttpServletRequest req) 
	{
		Connection conn = null;
		ArrayList docfas = new ArrayList();
		try
		{
			Context cont = new InitialContext();
			Context datasourceContext = (Context) cont.lookup("java:comp/env");
			DataSource theDataSource = (DataSource) datasourceContext.lookup("jdbc/mui");
			conn = theDataSource.getConnection();
			
			String SQL_INDIRIZZO_PART_CAT = "SELECT DISTINCT s.prefisso || ' ' || s.nome AS nome, s.numero as codice_via, " +
											 " c.civico " +
											 "FROM sitiuiu p, siticivi_uiu cu, siticivi c, sitidstr s, siticomu sc " +
											 "WHERE sc.codi_fisc_luna = ? " +
											 "AND p.foglio = ? " +
											 "AND p.particella = ? " +
											 "AND p.unimm = ? " +
											 "AND sc.cod_nazionale = p.cod_nazionale " +
											 "AND cu.pkid_uiu = p.pkid_uiu " +
											 "AND c.pkid_civi = cu.pkid_civi " +
											 "AND s.pkid_stra = c.pkid_stra";
			
			String query = "SELECT protocollo, data_variazione, causale, soppressione, variazione,  "
				 + "       costituzione, operazione, foglio, particella, subalterno,  "
				 + "       indirizzo_uiu, dichiarante, indirizzo_dichiarante, fornitura,  "
				 + "       data_protocollo, n  "
				 + "  FROM (SELECT protocollo,  "
				 + "               TO_CHAR (data_variazione, 'YYYY-MM-DD') data_variazione,  "
				 + "               causale, soppressione, variazione, costituzione, operazione,  "
				 + "               foglio, particella, subalterno, indirizzo_uiu, dichiarante,  "
				 + "               indirizzo_dichiarante,  "
				 + "               TO_CHAR (fornitura, 'YYYY-MM-DD') fornitura,  "
				 + "                  SUBSTR (data_registrazione, 7, 2)  "
				 + "               || '-'  "
				 + "               || SUBSTR (data_registrazione, 5, 2)  "
				 + "               || '-'  "
				 + "               || SUBSTR (data_registrazione, 1, 4) AS data_protocollo,  "
				 + "               ROWNUM AS n  "
				 + "          FROM (SELECT DISTINCT d_gen.protocollo_reg AS protocollo,  "
				 + "                                d_gen.data_variazione AS data_variazione,  "
				 + "                                cau.cau_des AS causale,  "
				 + "                                uiu_in_soppressione AS soppressione,  "
				 + "                                uiu_in_variazione AS variazione,  "
				 + "                                uiu_in_costituzione AS costituzione,  "
				 + "                                DECODE (u.tipo_operazione,  "
				 + "                                        'C', 'COSTITUITA',  "
				 + "                                        'V', 'VARIATA',  "
				 + "                                        'S', 'SOPPRESSA',  "
				 + "                                        u.tipo_operazione  "
				 + "                                       ) AS operazione,  "
				 + "                                u.foglio foglio, u.numero particella,  "
				 + "                                u.subalterno subalterno,  "
				 + "                                   TRIM (u.indir_toponimo)  "
				 + "                                || ' '  "
				 + "                                || u.indir_nciv_uno AS indirizzo_uiu,  "
				 + "                                   dic.dic_cognome  "
				 + "                                || ' '  "
				 + "                                || dic.dic_nome AS dichiarante,  "
				 + "                                   dic.dic_res_com  "
				 + "                                || ' '  "
				 + "                                || dic.dic_res_indir  "
				 + "                                || ' '  "
				 + "                                || dic.dic_res_numciv  "
				 + "                                                     AS indirizzo_dichiarante,  "
				 + "                                d_gen.fornitura fornitura,  "
				 + "                                metr.data_registrazione  "
				 + "                           FROM docfa_causali cau,  "
				 + "                                docfa_dati_generali d_gen,  "
				 + "                                docfa_uiu u,  "
				 + "                                docfa_dichiaranti dic,  "
				 + "                                docfa_dati_metrici metr,  "
				 + "                                docfa_dati_censuari cen  "
				 + "                          WHERE d_gen.causale_nota_vax = cau.cau_cod(+)  "
				 + "                            AND d_gen.protocollo_reg = u.protocollo_reg(+)  "
				 + "                            AND d_gen.fornitura = u.fornitura(+)  "
				 + "                            AND d_gen.protocollo_reg = dic.protocollo_reg(+)  "
				 + "                            AND d_gen.protocollo_reg = metr.protocollo_registrazione (+)  "
				 + "                            AND d_gen.fornitura = metr.fornitura (+) "
				 + "                            AND metr.protocollo_registrazione = cen.protocollo_registrazione (+)  "
				 + "                            AND metr.fornitura = cen.fornitura (+) "
				 + "                            AND metr.data_registrazione = cen.data_registrazione (+)  "
				 + "                            AND d_gen.fornitura = dic.fornitura(+)  "
				 + "                            AND 1 = 1  ";
		
			String orderby = "   ORDER BY data_registrazione DESC, operazione DESC))  ";
			String sqlWhere = "";
			
			// se proviene da lista upload altrimenti proviene da ricerca
			if (req.getParameter("flagSkipped")!= null && (!(req.getParameter("flagSkipped").trim()).equals(""))   )
			{
				//ignietto il dap come tabella della form
				query = query.replaceFirst("FROM docfa_causali cau,", "FROM docfa_dap dap, docfa_causali cau,");
				// le where di relazione
				query +=" and D_GEN.FORNITURA = to_date(dap.IID_FORNITURA,'yyyymmdd') "; 
				query +=" and D_GEN.PROTOCOLLO_REG = dap.IID_PROTOCOLLO_REG ";
				//fornitura c'è sempre
				String f = req.getParameter("fornitura");
				// la rimetto nella forma originale senza -
				f = f.replaceAll("-", "");
				query +=" and dap.iid_fornitura = '"+f+"'  ";
                // il flag c'è sempre 
				query +=" and dap.FLAG_SKIPPED = '"+req.getParameter("flagSkipped")+"'  ";
				//flagDapDiritto opzionale
				if (req.getParameter("flagDapDiritto")!= null && (!(req.getParameter("flagDapDiritto").trim()).equals(""))   )
					query +=" and dap.FLAG_DAP_DIRITTO = '"+req.getParameter("flagDapDiritto")+"'  ";
                    
			}
			else
			{
				//controllo se sono stati impostati dei criteri di filtro
				if (req.getParameter("protocollo")!= null && (!(req.getParameter("protocollo").trim()).equals(""))   )
					sqlWhere += " and D_GEN.PROTOCOLLO_REG = '"+ req.getParameter("protocollo")+"' ";
				if (req.getParameter("dt_variazione")!= null && (!(req.getParameter("dt_variazione").trim()).equals(""))   )
					sqlWhere += " and D_GEN.data_variazione = to_date('"+ req.getParameter("dt_variazione")+"','DD/MM/YYYY') ";
				if (req.getParameter("causale")!= null && (!(req.getParameter("causale").trim()).equals(""))   )
					sqlWhere += " and D_GEN.CAUSALE_NOTA_VAX = '"+ req.getParameter("causale")+"' ";
				//dichiaranti
				if (req.getParameter("cognome")!= null && (!(req.getParameter("cognome").trim()).equals(""))   )
				{
					if (req.getParameter("clause_cognome").equals("CONTAINS"))
						sqlWhere += " and dic.dic_cognome LIKE '%"+ req.getParameter("cognome")+"%' ";
					else	
						sqlWhere += " and dic.dic_cognome = '"+ req.getParameter("cognome")+"' ";
				}
				if (req.getParameter("nome")!= null && (!(req.getParameter("nome").trim()).equals(""))   )
				{
					if (req.getParameter("clause_nome").equals("CONTAINS"))
						sqlWhere += " and dic.dic_NOME LIKE '%"+ req.getParameter("nome")+"%' ";
					else	
						sqlWhere += " and dic.dic_NOME = '"+ req.getParameter("nome")+"' ";
				}
				//immobili
				if (req.getParameter("foglio")!= null && (!(req.getParameter("foglio").trim()).equals(""))   )
					sqlWhere += " and u.FOGLIO = '"+ req.getParameter("foglio")+"' ";
				if (req.getParameter("particella")!= null && (!(req.getParameter("particella").trim()).equals(""))   )
					sqlWhere += " and u.NUMERO = '"+ req.getParameter("particella")+"' ";
				if (req.getParameter("subalterno")!= null && (!(req.getParameter("subalterno").trim()).equals(""))   )
					sqlWhere += " and u.SUBALTERNO = '"+ req.getParameter("subalterno")+"' ";
				if (req.getParameter("indirizzo")!= null && (!(req.getParameter("indirizzo").trim()).equals(""))   )
				{
					if (req.getParameter("clause_indirizzo").equals("CONTAINS"))
						sqlWhere += " and u.indir_toponimo LIKE '%"+ req.getParameter("indirizzo")+"%' ";
					else	
						sqlWhere += " and u.indir_toponimo = '"+ req.getParameter("indirizzo")+"' ";
				}
				if (req.getParameter("operazione")!= null && (!(req.getParameter("operazione").trim()).equals(""))   )
					sqlWhere += " and u.TIPO_OPERAZIONE = '"+ req.getParameter("operazione")+"' ";
			}
			query += sqlWhere + orderby;
		
			PreparedStatement pstm = conn.prepareStatement(query);
			ResultSet rs = pstm.executeQuery();
			
			while (rs.next())
			{ 
				//campi della lista
				Docfa docfa = new Docfa();
				docfa.setProtocollo(tornaValoreRS(rs,"PROTOCOLLO"));
				docfa.setDataVariazione(tornaValoreRS(rs,"DATA_VARIAZIONE","YYYY-MM-DD"));
				docfa.setCausale(tornaValoreRS(rs,"CAUSALE"));
				docfa.setSoppressione(tornaValoreRS(rs,"SOPPRESSIONE"));
				docfa.setVariazione(tornaValoreRS(rs,"VARIAZIONE"));
				docfa.setCostituzione(tornaValoreRS(rs,"COSTITUZIONE"));
				//docfa.setCognome(tornaValoreRS(rs,"COGNOME"));
				//docfa.setNome(tornaValoreRS(rs,"NOME"));
				//docfa.setDenominazione(tornaValoreRS(rs,"DENOMINAZIONE"));
				//docfa.setCodiceFiscale(tornaValoreRS(rs,"CODICE_FISCALE"));
				//docfa.setPartitaIva(tornaValoreRS(rs,"PARTITA_IVA"));
				docfa.setOperazione(tornaValoreRS(rs,"OPERAZIONE"));
				docfa.setFoglio(tornaValoreRS(rs,"FOGLIO"));
				docfa.setParticella(tornaValoreRS(rs,"PARTICELLA"));
				docfa.setSubalterno(tornaValoreRS(rs,"SUBALTERNO"));
				docfa.setIndirizzoUiu(tornaValoreRS(rs,"INDIRIZZO_UIU"));
				docfa.setDichiarante(tornaValoreRS(rs,"DICHIARANTE"));
				docfa.setIndirizzoDichiarante(tornaValoreRS(rs,"INDIRIZZO_DICHIARANTE"));
				docfa.setFornitura(tornaValoreRS(rs,"FORNITURA","YYYY-MM-DD"));
				docfa.setDataProtocollo(tornaValoreRS(rs,"DATA_PROTOCOLLO"));
				
				//ricavo indirizzi uiu da catasto
				//solo per docfa con uiuFoglio numerici sennò esplode!!!!
				ArrayList indCat = new ArrayList(); 
				try
				{
					Integer.parseInt(docfa.getFoglio());
				
					if (!docfa.getOperazione().equals("COSTITUITA"))
					{	
						PreparedStatement pstcat= conn.prepareStatement(SQL_INDIRIZZO_PART_CAT);
						pstcat.setString(1, MuiApplication.belfiore);
						pstcat.setInt(2, Integer.parseInt(docfa.getFoglio()));
						pstcat.setString(3, docfa.getParticella());
						
						//controllo se il SUB è rappresentato da spazi vuoti (DOCFA_UIU) --> lo imposto a 0 (sitiuiu non a unimm a vuoto ma solo a 0!)
						//sennò esplode la query!!!
						if(docfa.getSubalterno().trim().equals(""))
							docfa.setSubalterno("0");
						
						pstcat.setInt(4, Integer.parseInt(docfa.getSubalterno()));
						
						ResultSet rscat = pstcat.executeQuery();
						
						while (rscat.next())
						{
							String appo = tornaValoreRS(rscat,"NOME")+" "+tornaValoreRS(rscat,"CIVICO");
							if (appo != null && !appo.trim().equals("")) 
								indCat.add(appo);
							
						}
						rscat.close();
						pstcat.close();
						
					}
					else
					{
						indCat.add(docfa.getIndirizzoUiu());
					}
				
				}catch (NumberFormatException e)
				{
					log.debug("valore foglio UIU non numerico!!!!");
					
				}
				docfa.setIndPart(indCat);
				
				docfas.add(docfa);
			
			}
			rs.close();
			pstm.close();	
		}catch(Exception e)
		{
			log.debug(e.getMessage());
		}
			
		return docfas;
	}

	private List<Docfa> searchDocfa(HttpServletRequest req) {
		List<Docfa> docfas = new ArrayList<Docfa>();
		
		String sqlSelect = "select distinct c ";
		String sqlFrom = "from it.webred.docfa.model.DocfaComunicazione as c ";
		String sqlWhere = "where 1=1 ";
		
		
		
		return docfas;

	}
	
	private static String tornaValoreRS(ResultSet rs, String colonna) throws Exception
	{
		return tornaValoreRS(rs, colonna, null);
	}

	private static String tornaValoreRS(ResultSet rs, String colonna, String tipo) throws Exception
	{
		try
		{
			String s = null;
			s = rs.getString(colonna);

			if (s == null && tipo != null)
				if (tipo.equals("VUOTO"))
					s = "";
			if (s == null)
				return s = "-";

			if (tipo != null)
				if (tipo.equals("NUM"))
				{
					s = new Integer(s).toString();
				}
				else if (tipo.equals("DOUBLE"))
				{
					s = new Double(s).toString();
				}
				else if(tipo.equals("EURO"))
				{
					NumberFormat nf = NumberFormat.getNumberInstance(Locale.ITALY);
					nf.setMinimumFractionDigits(2);
					nf.setMaximumFractionDigits(2);
					s = nf.format(new Double(s));
				}
				else if (tipo.equalsIgnoreCase("YYMMDD"))
				{
					s = s.substring(4) + "/" + s.substring(2, 4) + "/" + s.substring(0, 2);
				}
				else if (tipo.equalsIgnoreCase("YYYY/MM/DD"))
				{
					s = s.substring(8) + "/" + s.substring(5, 7) + "/" + s.substring(0, 4);
				}
				else if (tipo.equalsIgnoreCase("YYYY-MM-DD"))
				{
					s = s.substring(8,10) + "/" + s.substring(5, 7) + "/" + s.substring(0, 4);
				}
				else if (tipo.equalsIgnoreCase("DDMMYYYY"))
				{
					s = s.substring(0, 2) + "/" + s.substring(2, 4) + "/" + s.substring(4, 8);
				}
				else if (tipo.equalsIgnoreCase("YYYYMMDD"))
				{
					s = s.substring(6, 8) + "/" + s.substring(4, 6) + "/" + s.substring(0, 4);
				}			
				else if (tipo.equalsIgnoreCase("FLAG"))
				{
					if (s.equals("0"))
						s = "NO";
					else
						s = "SI";
				}
			return s;
		}
		catch (Exception e)
		{
			return "-";
		}
	}

	
}

