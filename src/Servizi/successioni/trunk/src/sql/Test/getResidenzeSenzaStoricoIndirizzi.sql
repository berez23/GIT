SELECT 
		DECODE(SUBSTR(cod.codicefiscale,0,1),'D','A','E','A','F','A','G','A','H','I','I','I','L','I','M','I','N','D','O','D','P','D','Q','D','A') as POSIANA,
		DECODE(SUBSTR(cod.codicefiscale,0,1),'D',to_date(NULL),'E',to_date(NULL),'F',vecchia_data,'G',prossima_data,'H',to_date(NULL),'I',to_date(NULL),'L',vecchia_data,'M',prossima_data,'N',to_date(NULL),'O',to_date(NULL),'P',vecchia_data,'Q',prossima_data,vecchia_data) as IMMDATA,
		DECODE(SUBSTR(cod.codicefiscale,0,1),'D',to_date(NULL),'E',futura_data,'F',prossima_data,'G',futura_data,'H',to_date(NULL),'I',futura_data,'L',prossima_data,'M',futura_data,'N',to_date(NULL),'O',futura_data,'P',prossima_data,'Q',futura_data,futura_data) as EMIDATA,
		DECODE(SUBSTR(cod.codicefiscale,0,1),'D',to_date(NULL),'E',futura_data,'F',prossima_data,'G',futura_data,'H',to_date(NULL),'I',futura_data,'L',prossima_data,'M',futura_data,'N',to_date(NULL),'O',futura_data,'P',prossima_data,'Q',futura_data,futura_data) as MORDATA,
		DECODE(SUBSTR(cod.codicefiscale,0,1),'P','100','1101') as COD_VIA, 
		DECODE(SUBSTR(cod.codicefiscale,0,1),'P','101','1102') as NUMERO_CIV
	FROM 	(select ? as codicefiscale,to_date('1/05/1996','dd/MM/yyyy') as vecchia_data,to_date('11/05/2004','dd/MM/yyyy') as recente_data,to_date('21/05/2006','dd/MM/yyyy') as prossima_data,to_date('20/05/2020','dd/MM/yyyy') as futura_data from dual) cod
