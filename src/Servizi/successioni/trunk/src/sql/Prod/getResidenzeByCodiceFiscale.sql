   SELECT  '1' NSEL, null DATA_EMI, null DATA_IMM,   TO_CHAR(p.id_orig) AS matricola,
      p.codfisc AS codice_fisc,
      p.cognome,
      p.nome,
      p.SESSO,
      sto_ind.ind_cod_via AS cod_via,
      via.VIASEDIME ||' '||via.DESCRIZIONE descr_via,
      -- sto_ind.ind_num_civ ||' '||sto_ind.ind_esp_civ AS num_civ,
      sto_ind.ind_num_civ numero_CIV,
      sto_ind.ind_esp_civ AS ESP_civ,
      DECODE(NVL(STO_IND.IND_DATA_INIZ,'-'),'-','','00000000','', 
      SUBSTR(STO_IND.IND_DATA_INIZ, 7, 2)||'/'||SUBSTR(STO_IND.IND_DATA_INIZ, 
      5, 2)||'/'||SUBSTR(STO_IND.IND_DATA_INIZ, 1, 4)) AS DATA_INIZIO,
      DECODE(NVL(STO_IND.IND_KEY_DATA_FINE,'-'),'-','','99999999','', 
      SUBSTR(STO_IND.IND_KEY_DATA_FINE, 7, 
      2)||'/'||SUBSTR(STO_IND.IND_KEY_DATA_FINE, 5, 
      2)||'/'||SUBSTR(STO_IND.IND_KEY_DATA_FINE, 1, 4)) AS DATA_FINE
   FROM sit_d_persona p,
      MVW_SUC_STO_FAM_INDIRIZZI sto_ind,
      sit_d_via via
   WHERE 1 = 1
      AND LPAD(p.id_orig,20,'0') = LPAD(sto_ind.matricola,20,'0')
      AND P.CODFISC = ?
      and p.dt_fine_val is null
      AND sto_ind.ind_cod_via = via.id_orig
      AND ( SUBSTR (IND_DATA_INIZ,1,1) = '1' OR SUBSTR (IND_DATA_INIZ,1,1) = '2' ) 
   -- ORDER BY COGNOME,NOME,TO_NUMBER(STO_IND.IND_DATA_INIZ) ASC
UNION
   SELECT distinct  '2' NSEL, p.DATA_EMI, p.data_imm, 
      p.id_ORIG matricola,
      p.codfisc codice_fisc,
      p.cognome,
      p.nome,
      p.sesso,
      SUBSTR(C.ID_ORIG,0,instr(C.ID_ORIG, '#')-1) cod_via,
      via.VIASEDIME ||' '||via.DESCRIZIONE descr_via,
      cxml.numero AS NUMERO_CIV,
      cxml.barrato ESP_CIV,
      TO_CHAR (pc.dt_inizio_DATO,'DD/MM/YYYY') data_inizio_res,
      TO_CHAR (pc.dt_fine_DATO,'DD/MM/YYYY') data_fine_res
   FROM sit_d_persona_civico pc,
      sit_d_civico c,
      sit_d_persona p,
      sit_d_pers_fam f,
      sit_d_via via,
      (SELECT ID_ext,
         EXTRACT
         (civico_composto,
         '/civicocomposto/att[@tipo="numero"]/@valore'
         ).getstringval() numero,
         EXTRACT
         (civico_composto,
         '/civicocomposto/att[@tipo="barrato"]/@valore'
         ).getstringval() barrato,
         EXTRACT
         (civico_composto,
         '/civicocomposto/att[@tipo="subbarrato"]/@valore'
            ).getstringval() subbarrato
      FROM sit_d_civico) cxml
   WHERE pc.ID_EXT_d_civico = c.ID_EXT
   AND p.ID_ext = pc.id_ext_d_persona (+)
   AND c.id_ext = cxml.ID_ext (+)
   AND p.ID_ext = f.id_ext_D_PERSONA (+)
   AND SUBSTR(C.ID_ORIG,0,instr(C.ID_ORIG, '#')-1) = via.id_orig
   AND P.CODFISC = ?
   and (p.DATA_EMI is null or data_emi < data_imm OR P.DATA_EMI =
   (
   SELECT MIN(DATA_EMI) FROM SIT_D_PERSONA PP WHERE PP.ID_EXT = P.ID_EXT
   AND PP.DATA_EMI > ?
    and not exists 
    (
        select 1 from sit_d_persona ppp 
        where ppp.id_ext = pp.id_ext
        and ppp.data_imm is not null
        and ppp.data_imm between ? and pp.data_emi 
    )
   )
   )
   and (p.DATA_IMM is null or data_emi > data_imm OR P.DATA_IMM =
   (
   SELECT MAX(DATA_IMM) FROM SIT_D_PERSONA PP WHERE PP.ID_EXT = P.ID_EXT
   AND PP.DATA_IMM <= ?
   and not exists 
    (
        select 1 from sit_d_persona ppp 
        where ppp.id_ext = pp.id_ext
        and ppp.data_emi is not null
        and ppp.data_emi between pp.data_imm and ?
    )
   )
   )
