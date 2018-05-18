--todos los clientes en alohandes
select carnet_uniandes
from clientes

minus

--todos los cliente que hicieron reservas en esa oferta en el rango de fechas
select id_cliente
from RESERVAS
where ID_AL_OF = 10 and FECHA_CREACION_OF = '01/01/14' and ('01/01/16' < FECHA_INICIO or '01/01/16' = FECHA_INICIO) and (FECHA_FIN < '01/01/19' or FECHA_FIN = '01/01/19');