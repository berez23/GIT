SELECT DISTINCT 'VIA PIPPO' as nome, 
				'100', 
				'101'
           FROM dual
          WHERE 1 = 1
          OR ( ? IS NULL AND ? IS NULL AND ? IS NULL ) 
UNION          
SELECT DISTINCT 'CORSO PAPERINO', 
				'300', 
				'301'
           FROM dual
UNION          
SELECT DISTINCT 'LARGO TOPOLINO', 
				'400', 
				'401'
           FROM dual
UNION          
SELECT DISTINCT 'PIAZZA PLUTO', 
				'200', 
				'201'
           FROM dual
           