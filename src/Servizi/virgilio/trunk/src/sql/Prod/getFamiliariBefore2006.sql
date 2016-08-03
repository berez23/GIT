SELECT fp.CODFISC as CODICE_FISCALE,sto_fam.fper_data_iniz_c,sto_fam.fper_data_fine_c
  FROM MVW_DUP_STO_FAM_COMPONENTI sto_fam, sit_d_persona fp
WHERE fp.id_orig = sto_fam.fper_cod_persona
    and  fp.DT_FINE_VAL is null
    AND ?
       BETWEEN  nvl(sto_fam.fper_data_iniz_c,to_date('18000101','YYYYMMDD'))
        AND nvl(sto_fam.fper_data_fine_c,to_date('22000101','YYYYMMDD'))
   AND EXISTS  
(
SELECT  1
    FROM sit_d_persona p,
         MVW_DUP_STO_FAM_COMPONENTI sto_fami
   WHERE   
   sto_fam.fper_cod_famiglia = sto_fami.fper_cod_famiglia
   AND p.CODFISC = ?
   AND sto_fami.fper_cod_persona = p.id_orig
   and p.DT_FINE_VAL is null
   AND sto_fami.ident NOT IN ('69191','80113','80122','142194') 
    AND ?
      BETWEEN  nvl(sto_fami.fper_data_iniz_c,to_date('18000101','YYYYMMDD'))
   AND nvl(sto_fami.fper_data_fine_c,to_date('22000101','YYYYMMDD'))
)



