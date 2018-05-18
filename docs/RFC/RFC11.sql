SELECT CLIENTES.*
FROM(
    --todos los clientes en alohandes
    SELECT carnet_uniandes
    FROM clientes

    MINUS

    --todos los cliente que hicieron reservas en esa oferta en el rango de fechas
    SELECT id_cliente
    FROM RESERVAS
    WHERE ID_AL_OF = 10 AND FECHA_CREACION_OF = '01/01/14' AND '01/01/16' <= FECHA_INICIO 
    AND FECHA_FIN <= '01/01/19' 
)T INNER JOIN CLIENTES ON T.CARNET_UNIANDES = CLIENTES.CARNET_UNIANDES;