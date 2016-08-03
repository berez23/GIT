SELECT DISTINCT  P2.CODFISC  as CODICE_FISCALE
	FROM SIT_D_PERS_FAM pf2, SIT_D_PERSONA p2,
	(
		SELECT  pf1.id_ext_d_famiglia
			FROM SIT_D_PERSONA p1, SIT_D_PERS_FAM PF1
			WHERE
			? BETWEEN  NVL(p1.dt_inizio_dato,TO_DATE('01/01/1000','DD/MM/YYYY')) AND NVL(p1.dt_fine_dato,sysdate)
			AND  p1.CODFISC = ?
			AND pf1.ID_EXT_D_PERSONA = p1.id_ext			
	) fam
	WHERE  pf2.id_ext_d_famiglia = fam.id_ext_d_famiglia
	AND pf2.ID_EXT_D_PERSONA = p2.ID_EXT
	AND ? BETWEEN  NVL(pf2.dt_inizio_dato,TO_DATE('01/01/1000','DD/MM/YYYY')) AND NVL(pf2.dt_fine_dato,sysdate)
	 
