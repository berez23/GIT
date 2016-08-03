SELECT DISTINCT ind.progressivo, t.descr, ind.ind AS ind, ind.civ1 AS CIV1
           FROM load_cat_uiu_id ID, siticomu c, load_cat_uiu_ind ind, cat_d_toponimi t
          WHERE c.codi_fisc_luna = ?
            AND ID.codi_fisc_luna = c.codi_fisc_luna
            AND ID.sez = c.sezione_censuaria
            AND ID.foglio = ?
            AND ID.mappale = ?
            AND ID.sub = ?
            AND ind.codi_fisc_luna = c.codi_fisc_luna
            AND ind.sezione = ID.sezione
            AND ind.id_imm = ID.id_imm
            AND ind.progressivo = ID.progressivo
            AND t.pk_id = ind.toponimo
       ORDER BY 1 DESC, 2, 3
