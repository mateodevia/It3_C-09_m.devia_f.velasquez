SELECT CLIENTES.*
FROM(
    --todos los clientes en alohandes
    SELECT carnet_uniandes
    FROM clientes

    MINUS

    --todos los cliente que hicieron reservas en esa oferta en el rango de fechas
    SELECT id_cliente
    FROM RESERVAS
    WHERE ID_AL_OF = 46711 AND '1/1/02' <= FECHA_INICIO AND FECHA_FIN <= '1/1/40' 
)T INNER JOIN CLIENTES ON T.CARNET_UNIANDES = CLIENTES.CARNET_UNIANDES;