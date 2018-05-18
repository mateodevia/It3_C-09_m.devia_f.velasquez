/*Los buenos clientes son de tres tipos: aquellos que hacen reservas en AlohAndes al menos una vez al mes, 
aquellos que siempre reservan alojamientos costosos (Entiéndase costoso, 
por ejemplo, como mayor a USD 150 por noche) y aquellos que siempre reservan suites*/


--IMPORTANTÍSIMO!!!!!! SE DEBERÍA TENER EN CUENTA SI LA RESERVA ESTÁ CANCELADA??????--



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
/*cantida de meses entre dos fechas*/
SELECT MOD( TRUNC( MONTHS_BETWEEN( '31/12/2018', '1/1/2018' ) ), 12 ) as MONTHS
FROM DUAL;

/*Idea: SELECT distinct de meses en reservas de un cliente. Sí # de valores distintos == número de meses, cliente es buen cliente*/
--SELECT TODOS LOS AÑOS DE RESERVAS ENTRE CURRENT DATE Y EL INICIO DE ALOHANDES????--
SELECT EXTRACT(MONTH FROM RESERVAS.FECHA_INICIO)  AS MES, EXTRACT (YEAR FROM RESERVAS.FECHA_INICIO) AS ANIO FROM 
CLIENTES INNER JOIN RESERVAS ON RESERVAS.ID_CLIENTE = CLIENTES.CARNET_UNIANDES
WHERE /*CLIENTE = IDCLIENTE PRRRO AND*/ RESERVAS.FECHA_INICIO <= CURRENT_DATE 
GROUP BY EXTRACT(MONTH FROM RESERVAS.FECHA_INICIO), EXTRACT (YEAR FROM RESERVAS.FECHA_INICIO);

/*NÚMERO DE MESES TOTALES EN RESERVAS, ESTO SE DEBERÍA HACER POR CADA UNO DE LOS CLIENTES*/
SELECT COUNT(*) FROM (SELECT EXTRACT(MONTH FROM RESERVAS.FECHA_INICIO)  AS MES, EXTRACT (YEAR FROM RESERVAS.FECHA_INICIO) AS ANIO FROM 
                        CLIENTES INNER JOIN RESERVAS ON RESERVAS.ID_CLIENTE = CLIENTES.CARNET_UNIANDES
                        WHERE /*CLIENTE = IDCLIENTE PRRRO AND*/ RESERVAS.FECHA_INICIO <= CURRENT_DATE 
                        GROUP BY EXTRACT(MONTH FROM RESERVAS.FECHA_INICIO), EXTRACT (YEAR FROM RESERVAS.FECHA_INICIO)
);