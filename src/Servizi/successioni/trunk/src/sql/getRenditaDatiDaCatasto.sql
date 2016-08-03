SELECT DISTINCT s.prefisso || ' ' || s.nome AS NOME, s.numero AS CODICE_VIA, c.CIVICO, p.RENDITA, p.CATEGORIA, p.CLASSE, p.DATA_INIZIO_VAL, p.DATA_FINE_VAL
           FROM sitiuiu p, siticivi_uiu cu, siticivi c, sitidstr s
          WHERE p.foglio = ?
            AND p.particella = ?
            AND p.unimm = ?
            --AND p.data_fine_val = TO_DATE ('99991231', 'yyyymmdd')
            AND p.pkid_uiu = cu.pkid_uiu (+)
            AND cu.pkid_civi = c.pkid_civi (+)
            AND c.pkid_stra = s.pkid_stra (+)
            ORDER BY p.DATA_INIZIO_VAL

  
  
  /*SELECT DISTINCT s.prefisso || ' ' || s.nome AS NOME, s.numero AS CODICE_VIA, c.CIVICO, p.RENDITA, p.CATEGORIA, p.CLASSE, p.DATA_INIZIO_VAL, p.DATA_FINE_VAL
           FROM siti.sitiuiu p, siti.siticivi_uiu cu, siti.siticivi c, siti.sitidstr s
          WHERE p.foglio = ?
            AND p.particella = ?
            AND p.unimm = ?
            AND cu.pkid_uiu = p.pkid_uiu
            AND c.pkid_civi = cu.pkid_civi
            AND s.pkid_stra = c.pkid_stra
            ORDER BY p.data_inizio_val*/
            