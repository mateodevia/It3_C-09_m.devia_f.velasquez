SELECT *
FROM CLIENTES INNER JOIN RESERVAS ON RESERVAS.ID_CLIENTE =  CLIENTES.CARNET_UNIANDES 
WHERE RESERVAS.ESTADO = 'RESERVADA' AND RESERVAS.ID_AL_OF = 46711 AND '1/1/02' <= RESERVAS.FECHA_INICIO AND RESERVAS.FECHA_FIN <= '1/1/40';
