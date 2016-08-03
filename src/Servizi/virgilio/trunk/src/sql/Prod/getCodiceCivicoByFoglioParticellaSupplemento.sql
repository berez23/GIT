SELECT DISTINCT s.prefisso || ' ' || s.nome AS nome, s.numero as codice_via, c.civico, c.PKID_CIVI
           FROM sitiuiu p, siticivi_uiu cu, siticivi c, sitidstr s, siticomu sc
          WHERE sc.codi_fisc_luna = ?
            AND p.foglio = ?
            AND p.particella = ?
            AND p.unimm = ?
            --dt-inizio_data
--            AND p.data_fine_val = TO_DATE ('99991231', 'yyyymmdd')
			AND sc.cod_nazionale = p.cod_nazionale       
			AND cu.pkid_uiu = p.pkid_uiu
            AND c.pkid_civi = cu.pkid_civi
            AND s.pkid_stra = c.pkid_stra
