select a.cuaa as CODICE_FISCALE,c.perc_poss as PERC_POSS
  from cons_csui_tab c, cons_azie_tab a, siticomu sc
 where    	
    c.pk_cuaa = a.pk_cuaa
   and sc.cod_nazionale = c.cod_nazionale 
   and sc.codi_fisc_luna = ?
   and c.foglio = ?           	
   and c.particella = ?   	
   and c.sub = ' '
   and c.unimm = ?  
AND   
?    
 BETWEEN NVL(c.data_inizio, to_date('01/01/1000','DD/MM/YYYY'))  
 AND NVL(c.data_fine, to_date('31/12/2100','DD/MM/YYYY'))
  
  