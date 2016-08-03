   create table MUI.MI_CONS_DAP (
        IID number(19,0) not null,
        IID_FORNITURA number(19,0) not null,
        IID_TITOLARITA number(19,0) not null,
        ID_NOTA varchar2(20 char),
        IID_NOTA number(19,0),
        FLAG_SKIPPED char(1 char),
        FLAG_ABITATIVO char(1 char),
        FLAG_ABIT_PRINC char(1 char),
        DATA_INIZIALE_DATE timestamp,
        DATA_FINALE_DATE timestamp,
        ID_SOGGETTO_NOTA varchar2(20 char),
        IID_SOGGETTO number(19,0),
        ID_SOGGETTO_CATASTALE varchar2(20 char),
        TIPO_SOGGETTO varchar2(1 char),
        ID_IMMOBILE varchar2(20 char),
        TIPOLOGIA_IMMOBILE varchar2(1 char),
        DAP_IMPORTO number(15,2),
        DAP_MESI number(10,0),
        FLAG_DAP_DIRITTO char(1 char),
        DAP_NUMERO_SOGGETTI number(10,0),
        DAP_DATA timestamp,
        DAP_ESITO number(10,0),
        CODICE_FISCALE varchar2(16 char),
        FOGLIO number(10,0),
        NUMERO varchar2(5 char),
        SUBALTERNO number(10,0),
        primary key (IID)
    );

 
    create index dap_sgt on MUI.MI_CONS_DAP (IID_SOGGETTO);

    create index dap_nota on MUI.MI_CONS_DAP (IID_NOTA);

    alter table MUI.MI_CONS_DAP 
        add constraint FK83F314A820061530 
        foreign key (IID_TITOLARITA) 
        references MUI.MI_DUP_TITOLARITA;

    alter table MUI.MI_CONS_DAP 
        add constraint FK83F314A8B681F4A 
        foreign key (IID_NOTA) 
        references MUI.MI_DUP_NOTA_TRAS;

    alter table MUI.MI_CONS_DAP 
        add constraint FK83F314A8E99ACF8A 
        foreign key (IID_FORNITURA) 
        references MUI.MI_DUP_FORNITURA;

    alter table MUI.MI_CONS_DAP 
        add constraint FK83F314A8FA773CE4 
        foreign key (IID_SOGGETTO) 
        references MUI.MI_DUP_SOGGETTI;
