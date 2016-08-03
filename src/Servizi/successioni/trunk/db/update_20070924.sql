CREATE INDEX INDICE_SUCC_D ON MILANO.MI_SUCCESSIONI_D
(UFFICIO, ANNO, VOLUME, NUMERO, SOTTONUMERO, 
COMUNE, PROGRESSIVO)
LOGGING
TABLESPACE USERS
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            MINEXTENTS       1
            MAXEXTENTS       2147483645
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOPARALLEL;



CREATE SYNONYM mi_successioni_a FOR milano.mi_successioni_a;
CREATE SYNONYM mi_successioni_b FOR milano.mi_successioni_b;
CREATE SYNONYM mi_successioni_c FOR milano.mi_successioni_c;
CREATE SYNONYM mi_successioni_d FOR milano.mi_successioni_d;


INSERT INTO MI_DUP_USER_ROLE ( IID, LOGIN, ROLENAME ) VALUES ( 
9, 'sadmin', 'succ-adm'); 
INSERT INTO MI_DUP_USER_ROLE ( IID, LOGIN, ROLENAME ) VALUES ( 
10, 'sadmin', 'succ-usr'); 
INSERT INTO MI_DUP_USER_ROLE ( IID, LOGIN, ROLENAME ) VALUES ( 
11, 'sadmin', 'succ-supusr'); 
COMMIT;
