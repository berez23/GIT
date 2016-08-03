  SELECT distinct FP.codfisc AS codice_fiscale,sto_fami.fper_data_iniz_c,
	sto_famI.fper_data_fine_c
    FROM sit_d_persona fp, sit_d_persona p, mvw_suc_sto_fam_componenti sto_fami, mvw_suc_sto_fam_componenti sto_fam
   WHERE sto_fam.fper_cod_famiglia = sto_fami.fper_cod_famiglia
     AND ? BETWEEN NVL (sto_fami.fper_data_iniz_c,
                         TO_DATE ('18000101', 'YYYYMMDD')
                    )
            AND NVL (sto_fami.fper_data_fine_c,
                     TO_DATE ('22000101', 'YYYYMMDD')
                    )
 AND p.codfisc = ?
 AND p.dt_fine_val IS NULL
 AND LPAD (sto_fam.fper_cod_persona, 10, '0') = p.id_orig
 AND ? BETWEEN NVL (sto_fam.fper_data_iniz_c,
                     TO_DATE ('18000101', 'YYYYMMDD')
                    )
            AND NVL (sto_fam.fper_data_fine_c,
                     TO_DATE ('22000101', 'YYYYMMDD')
                    )                                
AND fp.dt_fine_val IS NULL
AND LPAD (sto_fami.fper_cod_persona, 10, '0') = fp.id_orig   




