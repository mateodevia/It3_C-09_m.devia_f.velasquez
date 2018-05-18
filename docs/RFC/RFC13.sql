/*Los buenos clientes son de tres tipos: aquellos que hacen reservas en AlohAndes al menos una vez al mes, 
aquellos que siempre reservan alojamientos costosos (Entiéndase costoso, 
por ejemplo, como mayor a USD 150 por noche) y aquellos que siempre reservan suites*/

/*-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X*/
/*--X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-*/
--IMPORTANTÍSIMO!!!!!! SE DEBERÍA TENER EN CUENTA SI LA RESERVA ESTÁ CANCELADA??????--
/*-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X*/
/*--X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-X-*/


/*SELECT TODOS LOS CLIENTES CUYAS RESERVAS SON TODAS MAYORAS A 150 USD*/
/*TODOS LOS ALOJAMIENTOS MAYORES A 150 USD SE REDONDEAN A 433,000 PESOS SEGÚN CONVERSIÓN DEL 17/05/18*/
SELECT CLIENTES.CARNET_UNIANDES 
FROM CLIENTES INNER JOIN RESERVAS ON RESERVAS.ID_CLIENTE = CLIENTES.CARNET_UNIANDES INNER JOIN OFERTAS_ALOJAMIENTOS ON OFERTAS_ALOJAMIENTOS.ID_AL = RESERVAS.ID_AL_OF
GROUP BY CLIENTES.CARNET_UNIANDES

MINUS

SELECT CLIENTES.CARNET_UNIANDES
FROM CLIENTES INNER JOIN RESERVAS ON RESERVAS.ID_CLIENTE = CLIENTES.CARNET_UNIANDES INNER JOIN OFERTAS_ALOJAMIENTOS ON OFERTAS_ALOJAMIENTOS.ID_AL = RESERVAS.ID_AL_OF
WHERE OFERTAS_ALOJAMIENTOS.PRECIO > 433000;

/*--------------------------------------------------------------------------------------------------------------------------------------*/
/*--------------------------------------------------------------------------------------------------------------------------------------*/
/*--------------------------------------------------------------------------------------------------------------------------------------*/
/*--------------------------------------------------------------------------------------------------------------------------------------*/

/*SELECT TODOS LOS CLIENTES CUYAS RESERVAS SON TODAS SUITES DE HOTELES*/
SELECT CLIENTES.CARNET_UNIANDES 
FROM CLIENTES INNER JOIN RESERVAS ON RESERVAS.ID_CLIENTE = CLIENTES.CARNET_UNIANDES INNER JOIN HABS_HOTELES ON HABS_HOTELES.ID_AL = RESERVAS.ID_AL_OF
GROUP BY CLIENTES.CARNET_UNIANDES


MINUS

SELECT CLIENTES.CARNET_UNIANDES
FROM CLIENTES INNER JOIN RESERVAS ON RESERVAS.ID_CLIENTE = CLIENTES.CARNET_UNIANDES INNER JOIN HABS_HOTELES ON HABS_HOTELES.ID_AL = RESERVAS.ID_AL_OF
WHERE TIPO = 'SEMISUITE' OR TIPO = 'ESTANDAR';

/*--------------------------------------------------------------------------------------------------------------------------------------*/
/*--------------------------------------------------------------------------------------------------------------------------------------*/
/*--------------------------------------------------------------------------------------------------------------------------------------*/
/*--------------------------------------------------------------------------------------------------------------------------------------*/

--OBTIENE LOS CARNET DE TODOS LOS CLIENTES QUE HAN RESERVADO POR LO MENOS UNA VEZ AL MES DESDE LA FECHA DE CREACION DEL CLIENTE--
SELECT MESES_RESERVADOS.CARNET_UNIANDES
FROM(  SELECT CLIENTES.CARNET_UNIANDES,TRUNC( MONTHS_BETWEEN( CURRENT_DATE,CLIENTES.FECHA_CREACION ) ) as MESES
        FROM CLIENTES
    ) MESES_CREACION
INNER JOIN
    (   SELECT CLIENTES.CARNET_UNIANDES, count(DISTINCT (EXTRACT(MONTH FROM RESERVAS.FECHA_INICIO) || '/' || EXTRACT (YEAR FROM RESERVAS.FECHA_INICIO))) AS MESES
        FROM CLIENTES INNER JOIN RESERVAS ON RESERVAS.ID_CLIENTE = CLIENTES.CARNET_UNIANDES
        WHERE RESERVAS.FECHA_INICIO <= CURRENT_DATE 
        GROUP BY CLIENTES.CARNET_UNIANDES
    ) MESES_RESERVADOS
ON MESES_RESERVADOS.CARNET_UNIANDES = MESES_CREACION.CARNET_UNIANDES
WHERE MESES_CREACION.MESES = MESES_RESERVADOS.MESES;

/*------------------------------------------------------------------------------------------------------------*/
/*------------------------------------------------------------------------------------------------------------*/
/*------------------------------------------------------------------------------------------------------------*/
/*------------------------------------------------------------------------------------------------------------*/
/*------------------------------------------------------------------------------------------------------------*/
-- JUSTIFICACION: 
--                0 -> EL CLIENTE RESERVA UNA VEZ AL MES
--                1 -> EL CLIENTE RESERVA ALOJAMIENTOS COSTOSOS
--                2 -> EL CLIENTE SIEMPRE HA RESERVADO SUITES
SELECT * FROM (
    SELECT CLIENTES.*, (CASE  WHEN CARNET_UNIANDES IN ( SELECT MESES_RESERVADOS.CARNET_UNIANDES
                                                        FROM(   SELECT CLIENTES.CARNET_UNIANDES,TRUNC( MONTHS_BETWEEN( CURRENT_DATE,CLIENTES.FECHA_CREACION ) ) as MESES
                                                                FROM CLIENTES
                                                            ) MESES_CREACION
                                                        INNER JOIN
                                                            (   SELECT CLIENTES.CARNET_UNIANDES, COUNT(DISTINCT (EXTRACT(MONTH FROM RESERVAS.FECHA_INICIO) || '/' || EXTRACT (YEAR FROM RESERVAS.FECHA_INICIO))) AS MESES
                                                                FROM CLIENTES INNER JOIN RESERVAS ON RESERVAS.ID_CLIENTE = CLIENTES.CARNET_UNIANDES
                                                                WHERE RESERVAS.FECHA_INICIO <= CURRENT_DATE 
                                                                GROUP BY CLIENTES.CARNET_UNIANDES
                                                            ) MESES_RESERVADOS
                                                        ON MESES_RESERVADOS.CARNET_UNIANDES = MESES_CREACION.CARNET_UNIANDES
                                                        WHERE MESES_CREACION.MESES = MESES_RESERVADOS.MESES
                                                      ) THEN 0
                
                              WHEN CARNET_UNIANDES IN (    SELECT CLIENTES.CARNET_UNIANDES 
                                                           FROM CLIENTES INNER JOIN RESERVAS ON RESERVAS.ID_CLIENTE = CLIENTES.CARNET_UNIANDES INNER JOIN OFERTAS_ALOJAMIENTOS ON OFERTAS_ALOJAMIENTOS.ID_AL = RESERVAS.ID_AL_OF
                                                           GROUP BY CLIENTES.CARNET_UNIANDES

                                                           MINUS

                                                           SELECT CLIENTES.CARNET_UNIANDES
                                                           FROM CLIENTES INNER JOIN RESERVAS ON RESERVAS.ID_CLIENTE = CLIENTES.CARNET_UNIANDES INNER JOIN OFERTAS_ALOJAMIENTOS ON OFERTAS_ALOJAMIENTOS.ID_AL = RESERVAS.ID_AL_OF
                                                           WHERE OFERTAS_ALOJAMIENTOS.PRECIO > 433000
                
                                                      ) THEN 1
                                                      
                              WHEN CARNET_UNIANDES IN (   SELECT CLIENTES.CARNET_UNIANDES 
                                                          FROM CLIENTES INNER JOIN RESERVAS ON RESERVAS.ID_CLIENTE = CLIENTES.CARNET_UNIANDES INNER JOIN HABS_HOTELES ON HABS_HOTELES.ID_AL = RESERVAS.ID_AL_OF
                                                          GROUP BY CLIENTES.CARNET_UNIANDES

                                                          MINUS

                                                          SELECT CLIENTES.CARNET_UNIANDES
                                                          FROM CLIENTES INNER JOIN RESERVAS ON RESERVAS.ID_CLIENTE = CLIENTES.CARNET_UNIANDES INNER JOIN HABS_HOTELES ON HABS_HOTELES.ID_AL = RESERVAS.ID_AL_OF
                                                          WHERE TIPO = 'SEMISUITE' OR TIPO = 'ESTANDAR'
                                                      ) THEN 2
                        END) AS JUSTIFICACION
    FROM CLIENTES)
WHERE JUSTIFICACION IS NOT NULL;


