SELECT CLIENTES.*, JUSTIFICACION 
FROM
    (SELECT CARNET_UNIANDES , '0' AS JUSTIFICACION
    FROM
    (   SELECT MESES_RESERVADOS.CARNET_UNIANDES
        FROM(   SELECT CLIENTES.CARNET_UNIANDES,TRUNC( MONTHS_BETWEEN( CURRENT_DATE,CLIENTES.FECHA_CREACION ) ) as MESES
                FROM CLIENTES
            ) MESES_CREACION
    INNER JOIN
    
    (   SELECT CLIENTES.CARNET_UNIANDES, COUNT(DISTINCT (EXTRACT(MONTH FROM RESERVAS.FECHA_INICIO) || '/' || EXTRACT (YEAR FROM RESERVAS.FECHA_INICIO))) AS MESES
        FROM CLIENTES INNER JOIN RESERVAS ON RESERVAS.ID_CLIENTE = CLIENTES.CARNET_UNIANDES
        WHERE RESERVAS.ESTADO = 'RESERVADA' AND RESERVAS.FECHA_INICIO <= CURRENT_DATE 
        GROUP BY CLIENTES.CARNET_UNIANDES
    ) MESES_RESERVADOS
    ON MESES_RESERVADOS.CARNET_UNIANDES = MESES_CREACION.CARNET_UNIANDES
    WHERE MESES_CREACION.MESES = MESES_RESERVADOS.MESES
    )
    
    UNION 
    
    (   
        SELECT CARNET_UNIANDES, '1' AS JUSTIFICACION
        FROM
            (   SELECT CLIENTES.CARNET_UNIANDES
                --Se hace este join, porque s�lo interesan clientes con al menos una reserva.
                FROM CLIENTES INNER JOIN RESERVAS ON CLIENTES.CARNET_UNIANDES = RESERVAS.ID_CLIENTE
                GROUP BY CLIENTES.CARNET_UNIANDES

                MINUS

                SELECT CLIENTES.CARNET_UNIANDES
                FROM CLIENTES INNER JOIN RESERVAS ON RESERVAS.ID_CLIENTE = CLIENTES.CARNET_UNIANDES INNER JOIN OFERTAS_ALOJAMIENTOS ON OFERTAS_ALOJAMIENTOS.ID_AL = RESERVAS.ID_AL_OF
                WHERE RESERVAS.ESTADO = 'RESERVADA' AND OFERTAS_ALOJAMIENTOS.PRECIO > 433000)
    )
    
    UNION
    
    (   SELECT CARNET_UNIANDES, '2' AS JUSTIFICACION 
        FROM
            (   SELECT CLIENTES.CARNET_UNIANDES 
                --Se hace este join, porque s�lo interesan clientes con al menos una reserva en habitaciones de hotel
                FROM CLIENTES INNER JOIN RESERVAS ON RESERVAS.ID_CLIENTE = CLIENTES.CARNET_UNIANDES INNER JOIN HABS_HOTELES ON HABS_HOTELES.ID_AL = RESERVAS.ID_AL_OF
                GROUP BY CLIENTES.CARNET_UNIANDES

                MINUS

                SELECT CLIENTES.CARNET_UNIANDES
                FROM CLIENTES INNER JOIN RESERVAS ON RESERVAS.ID_CLIENTE = CLIENTES.CARNET_UNIANDES INNER JOIN HABS_HOTELES ON HABS_HOTELES.ID_AL = RESERVAS.ID_AL_OF
                WHERE RESERVAS.ESTADO = 'RESERVADA' AND TIPO = 'SEMISUITE' OR TIPO = 'ESTANDAR'
            )  
    )
)MT
INNER JOIN CLIENTES
ON MT.CARNET_UNIANDES = CLIENTES.CARNET_UNIANDES; 