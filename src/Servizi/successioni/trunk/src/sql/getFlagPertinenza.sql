select gr.* from siti.SITIUIU_GRAF gr, siticomu sc
 where sc.CODI_FISC_LUNA = ?
   and sc.COD_NAZIONALE = gr.COD_NAZIONALE 
   and gr.FOGLIO_G = ?
   and gr.PARTICELLA_G = ?
   and gr.UNIMM_G = ?
