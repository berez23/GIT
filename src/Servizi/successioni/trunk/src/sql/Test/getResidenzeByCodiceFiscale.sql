select * from
(
SELECT DISTINCT 
				'190' as cod_via, 
				'191'  as numero_civ,
				'10/01/1970'as DATA_INIZIO,
				'10/01/1971'as DATA_FINE
           FROM dual
UNION          
SELECT DISTINCT 
				'120' as cod_via, 
				'121'  as numero_civ,
				'11/01/1971'as DATA_INIZIO,
				'20/05/2006'as DATA_FINE
           FROM dual
UNION          
SELECT DISTINCT 
				'100' as cod_via, 
				'101'  as numero_civ,
				'11/01/1971'as DATA_INIZIO,
				'20/05/2006'as DATA_FINE
           FROM dual
UNION          
SELECT DISTINCT 
				'130' as cod_via, 
				'131'  as numero_civ,
				'11/01/1971'as DATA_INIZIO,
				'20/05/2006'as DATA_FINE
           FROM dual
UNION          
SELECT DISTINCT  
				'300' as cod_via, 
				'301'  as numero_civ,
				'21/05/2006'as DATA_INIZIO,
				'10/01/2010'as DATA_FINE
           FROM dual
)
WHERE ? < 'D'
UNION
SELECT DISTINCT 
				'792' as cod_via, 
				'793'  as numero_civ,
				'10/01/1970'as DATA_INIZIO,
				'10/01/2010'as DATA_FINE
FROM dual
WHERE ? > 'R'
		   