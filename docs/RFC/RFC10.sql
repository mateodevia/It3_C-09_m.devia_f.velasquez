SELECT *
FROM CLIENTES INNER JOIN RESERVAS ON RESERVAS.ID_CLIENTE =  CLIENTES.CARNET_UNIANDES 
WHERE RESERVAS.ID_AL_OF = 10 AND '10/10/12' <= RESERVAS.FECHA_INICIO AND RESERVAS.FECHA_FIN <= '10/10/22' ;
