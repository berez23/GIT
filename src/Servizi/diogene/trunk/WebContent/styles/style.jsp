<%@ page language="java" %>

BODY
{
	background-color: #FFFFFF;
	font-family: Arial, Verdana, sans-serif;
}
TD
{
	background-color: #C0C0C0;
}
/*---------------------------------------------------------*/
/*					EDB Toolbar  						   */
/*---------------------------------------------------------*/
.tdTop
{
	background-color:#000099;
	color:White;
	font-size:16Px;
	height:30Px;
	font-family: Arial, Verdana, sans-serif;
	font-weight:bold;
	text-align:center;
}
.tdButton
{
	color: Navy;
	font-weight:bold;
	font-family:Arial, Verdana, sans-serif;
	height:25Px;
	width:100px;
}
.tdBottom
{
	background-color:#a2a2a2;
	height:30Px;
	font-family: Arial, Verdana, sans-serif;
	text-align:right;
}
.ActionButton
{
	font-family: Arial, Verdana, sans-serif;
	font-size:10Px;
}
/* -------------------------------------------------------*/
/*			Common Components for each control				  */
/* -------------------------------------------------------*/
		/*-----TABLE------*/
.editExtTable
{
border: Gray 1Px Solid;
}
.viewExtTable
{
border: none;
background-color:#F3F2F2;
}

.searchTable {
	border:1px #C0C0C0 solid 
	background-color:#F3F2F2;
}

		/*-----LABEL------*/
.TDmainLabel
{
	
	font-size: 8pt;
	background-color:#F3F2F2;
}
.TXTmainLabel
{
	font-size: 8pt;
	color: #000099;
	text-align: left;
	font-weight: Bold;
}

		/*-----OTHERS------*/

.imageBox
{

}
.defaultButton
{
	border: 1Px White Solid;
	background-color: #a2a2a2;
	color: White;
}
.extWindowList
{
	width:100%;
	background-color: #e8f0ff;
	background : #e8f0ff;
	;
}

		/*-----EXTERNAL WINDOW-----*/
		
.extWinTable
{
	border: 1Px Solid #000000;
}
.extWinTDTitle
{
	border: 1Px Solid #FFFFFF;
	text-align: center;
	height: 25pX;
	color: #FFFFFF;
	background-color:#4A75B5;
}
.extWinTXTTitle
{
	font-size:8pt;
	font-weight: bold;
	color: #FFFFFF;
	background-color:#4A75B5;
	
}
.extWinTDData
{
	border: 1pt white Solid;
	height: 22Px;
	background-color: #e8f0ff;
}
.extWinTXTData
{
	font-size: 7Pt;
}

/* -------------------------------------------------------*/
/*		   			Text Box Controls					  */
/* -------------------------------------------------------*/

		/*-----EDIT------*/
.TDeditTextBox  
{
/*border: #0000FF 1Px Solid;*/
}
.INPTextBox
{
	background-color: #e8f0ff;
	color: #000099;
	font-weight: Bold;
	text-align: left;
}

.INPTextBox_DIS /* if the control is disabled (Read Only) */
{
	background-color: #8f8f8f;
	color: #000099;
	font-weight: Bold;
	text-align: left;
}

		/*-----VIEW------*/		
.TDviewTextBox
{
/*	border: #FF0000 1Px Solid;*/
	background : #e8f0ff;
}

.TXTviewTextBox
{
	font-size:8pt;
	text-align: left;
	background : #e8f0ff;
	color: #000099;
	font-weight: Bold;
}
/* -------------------------------------------------------*/
/*		   			ComboBox Controls					  */
/* -------------------------------------------------------*/

		/*-----EDIT------*/
.TDeditComboBox
{
border: #FF0000 1Px Solid;
}
.INPComboBox
{
	background-color: #e8f0ff;
	color: #000099;
	font-weight: Bold;
}
		/*-----VIEW------*/

/* Like as viewTextBox*/
		
/* -------------------------------------------------------*/
/*		   			DBComboBox Controls					  */
/* -------------------------------------------------------*/

		/*-----EDIT------*/
.TDeditDBComboBox
{

}

.INPDBComboBox
{
	background-color: #e8f0ff;
	color: #000099;
	font-weight: Bold;
	font-size: 9pt;
}
		/*-----VIEW------*/

/* Like as viewTextBox*/
		
/* -------------------------------------------------------*/
/*		   			DBWindowList Controls				  */
/* -------------------------------------------------------*/

		/*-----EDIT------*/
		
/* Like as editTextBox*/

		/*-----VIEW------*/

/* Like as viewTextBox*/

/* -------------------------------------------------------*/
/*				   			Image						  */
/* -------------------------------------------------------*/

		/*-----EDIT------*/
		
/* Like as editTextBox*/

		/*-----VIEW------*/

.TDviewImage
{
	border: 1Px Solid #000099;
	text-align:center;
}

/* -------------------------------------------------------*/
/*				   			Image						  */
/* -------------------------------------------------------*/

		/*-----EDIT------*/
		
.TDeditCheckbox
{
 background-color: #FF0000;

}
.INPCheckBox
{
}

		/*-----VIEW------*/

/* Like as editTextBox*/


/* -------------------------------------------------------*/
/*			   			Document					 	  */
/* -------------------------------------------------------*/

		/*-----EDIT------*/
		
.TDeditCheckbox
{
 background-color: #FF0000;

}
.INPCheckBox
{
}


		/*-----VIEW------*/
.TXTViewDocument
{
	cursor: pointer;
	color: #000000;
	font-weight: Bold;
	font-size:10PX;
	text-align: right;
}
.TDIMGDocument
{
	border: #FF0000 Solid 1Px;
}
.IMGDocument
{
		cursor: pointer;
}

/* -------------------------------------------------------*/
/*			   			Automatic					  	  */
/* -------------------------------------------------------*/

		/*-----EDIT------*/
		
/* Like as editTextBox*/

		/*-----VIEW------*/

/* Like as editTextBox*/

/* -------------------------------------------------------*/
/*			   			External Table 				  	  */
/* -------------------------------------------------------*/
.linkextTable
{
	font-size: 12Px;
	text-decoration: underline;
	cursor: pointer;
	color: #000099;
}

/* -------------------------------------------------------*/
/*			   			External Table 				  	  */
/* -------------------------------------------------------*/
.textLabel
{
	font-size: 12Px;
	color: #595959;
	font-weight:bold;
}

/* -------------------------------------------------------*/
/*			   			List View	 				  	  */
/* -------------------------------------------------------*/
.tdHeader
{
	background-color:#A1A1A1;
	border-bottom:1Px Solid #000099;
}
.txtHeader
{
	font-weight:Bold;
	font-size: 12Px;
	color: #000099;
}
.tdRow
{
	cursor: pointer;
	background-color:#E2E2E2;
	border-bottom:1Px Solid #000000;
	border-right:1Px Solid #000000;
}
.txtRow
{
	font-size: 10Px;
	color: #000099;
}
/* -------------------------------------------------------*/
/*			  		Waiting View	 				  	  */
/* -------------------------------------------------------*/
  .cursorWait  { 
  	cursor: wait;
  	margin: 0;
  	padding: 0;
  }
  .cursorReady  { 
  	cursor: auto; 
  }
  #wait {
  	position: absolute;
  	top: 0;
  	left: 0;
  	visibility: hidden;
  	width: 100%;
  	height: 100%;
  	margin: 0;
  	padding: 0;
  	background-color: white;
  	background-repeat: no-repeat;
  	background-position: 50% 50%;
  	background-image: url("<%= it.escsolution.escwebgis.common.EscServlet.URL_PATH %>/images/loading-380x228.gif");
  	z-index: 1000; 
  }

td.iFrameLink
{
	background-color: white; 
	vertical-align: top; 
	text-align: right; 
	width: 10%;
	height: 50%;
}
  
iframe.iFrameLink
{
	height: 250px;
	overflow: visible;
}

/* Stili relativi al frame iFrameLink */

.iFrameLink {
	background-color: #C0C0C0;
}

div.iFrameLink
{
	margin: 0;
	padding: 0;
	background-color: #C0C0C0;
	border: 2px dashed black;
}
h3.iFrameLink
{
	margin: 0;
	padding: 4px 4px 2px 4px;
	font-weight: bold;
	font-size: 10pt;
	font-family: Tahoma, Arial, helvetica, sans-serif;
	font-style: italic;
	color: black;
}
ul.iFrameLink
{
	margin: 0;
	padding: 2px 4px 4px 4px;
	list-style-type: circle;
	list-style: inside;
}
ul.iFrameLink li
{
	font-family: Tahoma, Arial, helvetica, sans-serif;
	font-size: 10pt;
}
ul.iFrameLink li a
{
	font-family: Tahoma, Arial, helvetica, sans-serif;
	font-size: 10pt;
	color: #009;
	text-decoration: none;
}
