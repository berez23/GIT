SELECT distinct a.pk_id_succa, a.ufficio, a.anno, a.volume, a.numero, a.sottonumero, a.comune, a.progressivo, a.tipo_dichiarazione, a.data_apertura, a.cf_defunto, a.cognome_defunto, a.nome_defunto, a.sesso a_sesso,
 a.citta_nascita a_citta_nascita, a.prov_nascita a_prov_nascita, a.data_nascita a_data_nascita, a.citta_residenza a_citta_residenza, a.prov_residenza a_prov_residenza, a.indirizzo_residenza a_indirizzo_residenza, a.fornitura,
 b.pk_id_succb, b.comune, b.progressivo b_progressivo, b.progressivo_erede b_progressivo_erede, b.cf_erede, b.denominazione, b.sesso b_sesso, b.citta_nascita b_citta_nascita, b.prov_nascita b_prov_nascita, b.data_nascita b_data_nascita, b.citta_residenza b_citta_residenza, b.prov_residenza b_prov_residenza, b.indirizzo_residenza b_indirizzo_residenza,
 c.comune c_comune, c.progressivo c_progressivo, c.progressivo_immobile c_progressivo_immobile, TO_NUMBER(c.numeratore_quota_def) numeratore_quota_def, TO_NUMBER(c.denominatore_quota_def) denominatore_quota_def, c.diritto diritto, c.catasto, c.tipo_dati, c.foglio, c.particella1,
 c.particella2, c.subalterno1, c.subalterno2, c.denuncia1, c.anno_denuncia, c.natura, c.superficie_ettari, TO_NUMBER(c.superficie_mq) superficie_mq, TO_NUMBER(c.vani) vani, c.indirizzo_immobile,
 d.progressivo d_progressivo, d.progressivo_immobile d_progressivo_immobile, d.progressivo_erede d_progressivo_erede, TO_NUMBER(d.numeratore_quota) d_numeratore_quota, TO_NUMBER(d.denominatore_quota) d_denominatore_quota, d.agevolazione_1_casa d_agevolazione_1_casa, d.tipo_record d_tipo_record
 FROM mi_successioni_a a, mi_successioni_b b, mi_successioni_c c, mi_successioni_d d
 WHERE a.ufficio = b.ufficio
 AND a.anno = b.anno
 AND a.volume = b.volume
 AND a.numero = b.numero
 AND a.sottonumero = b.sottonumero
 AND a.fornitura = b.fornitura
 AND a.ufficio = c.ufficio
 AND a.anno = c.anno
 AND a.volume = c.volume
 AND a.numero = c.numero
 AND a.sottonumero = c.sottonumero
 AND a.fornitura = c.fornitura
 AND B.PROGRESSIVO_EREDE = D.PROGRESSIVO_EREDE
 AND c.PROGRESSIVO_IMMOBILE = d.PROGRESSIVO_IMMOBILE
 AND d.ufficio = a.ufficio
 AND d.anno = a.anno
 AND d.volume = a.volume
 AND d.numero = a.numero
 AND d.sottonumero = a.sottonumero
 AND d.comune = a.comune
 AND d.fornitura = a.fornitura
 AND a.pk_id_succa = (select max(pk_id_succa)
 FROM mi_successioni_a aa, mi_successioni_b bb, mi_successioni_c cc, mi_successioni_d dd
 WHERE aa.ufficio = bb.ufficio
 AND aa.anno = bb.anno
 AND aa.volume = bb.volume
 AND aa.numero = bb.numero
 AND aa.sottonumero = bb.sottonumero
 AND aa.fornitura = bb.fornitura
 AND aa.ufficio = cc.ufficio
 AND aa.anno = cc.anno
 AND aa.volume = cc.volume
 AND aa.numero = cc.numero
 AND aa.sottonumero = cc.sottonumero
 AND aa.fornitura = cc.fornitura
 AND bb.PROGRESSIVO_EREDE = dd.PROGRESSIVO_EREDE
 AND cc.PROGRESSIVO_IMMOBILE = dd.PROGRESSIVO_IMMOBILE
 AND dd.ufficio = aa.ufficio
 AND dd.anno = aa.anno
 AND dd.volume = aa.volume
 AND dd.numero = aa.numero
 AND dd.sottonumero = aa.sottonumero
 AND dd.comune = aa.comune
 AND dd.fornitura = aa.fornitura
 AND a.cf_defunto = aa.cf_defunto
 AND a.tipo_dichiarazione = aa.tipo_dichiarazione
 AND a.data_apertura = aa.data_apertura
 AND b.cf_erede = bb.cf_erede
 AND NVL(c.foglio, '-') = NVL(cc.foglio, '-')
 AND NVL(c.particella1, '-') = NVL(cc.particella1, '-')
 AND NVL(c.particella2, '-') = NVL(cc.particella2, '-')
 AND NVL(c.subalterno1, 0) = NVL(cc.subalterno1, 0)
 AND NVL(c.subalterno2, '-') = NVL(cc.subalterno2, '-')
 AND TO_NUMBER(c.numeratore_quota_def) = TO_NUMBER(cc.numeratore_quota_def)
 AND TO_NUMBER(c.denominatore_quota_def) = TO_NUMBER(cc.denominatore_quota_def)
 AND TO_NUMBER(d.numeratore_quota) = TO_NUMBER(dd.numeratore_quota)
 AND TO_NUMBER(d.denominatore_quota) = TO_NUMBER(dd.denominatore_quota)
 AND c.diritto = cc.diritto)
 AND a.data_apertura >= ?
 AND a.data_apertura <= ?
 ORDER BY a.pk_id_succa, b.pk_id_succb, a.ufficio, a.anno, a.volume, a.numero, a.sottonumero, a.comune, c.progressivo, a.fornitura, d.progressivo, d.progressivo_immobile, d.progressivo_erede