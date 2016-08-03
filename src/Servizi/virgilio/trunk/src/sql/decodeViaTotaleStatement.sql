select vt.sedime ||' '||vt.indirizzo   from diogene_c349.sit_civico_unico c, diogene_c349.sit_via_totale vt
where id_civico = ?
and c.fk_via = vt.fk_via
and vt.fk_ente_sorgente = ?
and vt.prog_es = ?