package it.webred.mui.http;

/**
 * <p>Title: MUI </p>
 * <p>Description: $Id: MuiHttpConstants.java,v 1.1 2007/05/16 07:45:40 dan Exp $</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: $Author: dan $</p>
 * <p> Date:</p>$Date: 2007/05/16 07:45:40 $
 * @author: Ing Francesco Ciacca
 * @version $Revision: 1.1 $
 */

public interface MuiHttpConstants {
  public static final String TEMPLATE = "/template.jsp";
  public static final String POPUP_TEMPLATE = "/templatePopup.jsp";
  public static final String POPUP_DISCRIMINATOR = "Popup";
  public static final String NOTEMPLATE_DISCRIMINATOR = "NoTemplate";
  public static final String FORMPOST_DISCRIMINATOR = "formPost";
  public static final String CONTROLLER_SES = "controller";
  public static final String CONTROLLER_PASS_REQ = "controlled";
  public static final String MAIN_URI_REQ = "main_page";
  public static final String CONNECTION_TIME_REQ = "connection_time";
  public static final String USER_REQ = "MUI_USER";
  public static final String MUI_APP_APPCTX = "MUI_APP";
  public static final String MUI_USERPROFILE_SES = "MUI_USERPROFILE";
  public static final String DBTIME_REQ = "DB_TIME";
  public static final int MUI_MAX_RESULTS = 200;
  public static final long MUI_DATE_DURATION = 60000;
  public static final long MUI_DBDATE_DURATION = 3600000;
  public static final String UPLOAD_LIST_VARNAME = "miDupFornituras";
  public static final String DAP_LIST_VARNAME = "miConsDaps";
  public static final String DOCFA_DAP_VARNAME = "docfaDaps";
  public static final String R0602_VARNAME = "R0602";
  public static final String BELFIORE_VARNAME = "BELFIORE";
  public static final long CACHE_DURATION = 2*1000*60*60*4;//2 perche divide per 2 *1000 secondi *60 minuti * 60 ore * 4 numero ore 
  public static final long CACHE_SLEEP_CYCLE = 15000;
}
/**
 * <p>History:</p>
 * $Log: MuiHttpConstants.java,v $
 * Revision 1.1  2007/05/16 07:45:40  dan
 * *** empty log message ***
 *
 * Revision 1.5  2007/06/12 15:56:40  dan
 * *** empty log message ***
 *
 * Revision 1.4  2007/05/24 16:49:58  ciacca
 * ripresa versione dan sovrascritta per sbaglio
 *
 * Revision 1.3  2007/05/24 15:26:09  ciacca
 * inserito supporto file XML
 *
 * Revision 1.1.1.1  2007/02/15 08:44:08  dan
 * no message
 *
 * Revision 1.11  2007/01/23 23:05:32  franci
 * fix bug pochi dap
 *
 * Revision 1.10  2007/01/16 09:49:49  franci
 * dap rilascio seconda offerta webred
 *
 * Revision 1.9  2006/11/19 22:15:38  franci
 * rilascio lunedi 20/11
 *
 * Revision 1.8  2006/10/23 12:14:36  franci
 *
 * inserito abbozzo DAP
 *
 * Revision 1.7  2006/09/12 23:57:28  franci
 * prima di rilascio
 *
 * Revision 1.6  2006/09/05 21:31:59  franci
 * anagrafe codici log + security tag
 *
 * Revision 1.5  2006/07/25 08:01:16  franci
 * *** empty log message ***
 *
 * Revision 1.4  2006/07/19 17:45:00  franci
 * prelast
 *
 * Revision 1.3  2006/06/11 15:49:38  franci
 * fix di buglist 10/06 meno salva comunicazione
 *
 * Revision 1.2  2006/05/25 07:51:46  franci
 * ricerche
 *
 * Revision 1.1.1.1  2006/05/04 09:45:45  franci
 * Importing from Scratch
 *
 */