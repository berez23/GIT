select c.foglio, c.particella, c.unimm, c.allegato
  from cons_csui_tab c, cons_azie_tab a
 where a.cuaa = ?
   and c.pk_cuaa = a.pk_cuaa
   and c.sub = ' '
AND ?
       BETWEEN NVL(c.data_inizio, TO_DATE ('01011000', 'DDMMYYYY'))
       AND NVL(c.data_fine, TO_DATE ('31129999', 'DDMMYYYY'))
order by c.foglio, c.particella,c.unimm asc       