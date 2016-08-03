SELECT fp.CODFISC as CODICE_FISCALE,sto_fam.fper_data_iniz_c,sto_fam.fper_data_fine_c
  FROM MVW_SUC_STO_FAM_COMPONENTI sto_fam, sit_d_persona fp
WHERE fp.id_orig = lpad(sto_fam.fper_cod_persona,10,'0')
    and  fp.DT_FINE_VAL is null
    AND ?
       BETWEEN  nvl(sto_fam.fper_data_iniz_c,to_date('18000101','YYYYMMDD'))
        AND nvl(sto_fam.fper_data_fine_c,to_date('22000101','YYYYMMDD'))
   AND EXISTS  
(
SELECT  1
     FROM sit_d_persona p,
         MVW_SUC_STO_FAM_COMPONENTI sto_fami
   WHERE   
   to_char(sto_fam.fper_cod_famiglia) = sto_fami.fper_cod_famiglia
   AND p.CODFISC = ?
   and lpad(sto_fam.fper_cod_persona,10,'0') = p.id_orig
   and p.DT_FINE_VAL is null
    AND ?
      BETWEEN  nvl(sto_fami.fper_data_iniz_c,to_date('18000101','YYYYMMDD'))
   AND nvl(sto_fami.fper_data_fine_c,to_date('22000101','YYYYMMDD'))
)



