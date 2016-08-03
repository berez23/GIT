select vt.sedime ||' '||vt.indirizzo   from sit_civico_unico c, sit_via_totale vt
where id_civico = ?
and c.fk_via = vt.fk_via
and vt.fk_ente_sorgente = ?
and vt.prog_es = ?