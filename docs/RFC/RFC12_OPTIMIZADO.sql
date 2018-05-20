-- cuantas reservas tiene el alojamiento 10 en la semana 44 del 2019 (uno)
select id_aloj from NUM_RESERV_ALOJ_SEM where semana = 44 and anio = 2019 and id_aloj = 10;

-- cual es el id_aloj del alojamiento con mas reservas en la semana 44 del 2019 (dos)
select id_aloj, num_reserv
from(select id_aloj, num_reserv from NUM_RESERV_ALOJ_SEM where semana = 44 and anio = 2019 order by num_reserv desc) uno
where rownum = 1;

-- cual es el alojamiento con mas reservas en la semana 44 del 2019 (tres)
select ('id: '||id_aloj||' capacidad: '||capacidad||' compartida: '||compartida||' tipo: '||tipo||' ubicacion: '||ubicacion||' operador: '||id_op||' numero de reservas: '||num_reserv) as alojamiento
from (select id_aloj, num_reserv
        from(select id_aloj, num_reserv from NUM_RESERV_ALOJ_SEM where semana = 44 and anio = 2019 order by num_reserv desc) uno
        where rownum = 1
    ) dos
    inner join
    alojamientos
    on dos.id_aloj = alojamientos.id;
    
    
--cual es el alojamiento con mayor ocupacion por cada semana del año 2019(cuatro)   
select numero as semana, 
        (select ('id: '||id_aloj||' capacidad: '||capacidad||' compartida: '||compartida||' tipo: '||tipo||' ubicacion: '||ubicacion||' operador: '||id_op||' numero de reservas: '||num_reserv) as alojamiento
        from (select id_aloj, num_reserv
                from(select id_aloj, num_reserv 
                        from NUM_RESERV_ALOJ_SEM 
                        where semana = numero and anio = 2017 
                        order by num_reserv desc
                    ) uno
                where rownum = 1
            ) dos
        inner join
        alojamientos
        on dos.id_aloj = alojamientos.id) as alojamiento_mayor_ocupacion
from semanas;

-- es el alojamiento con mayor y menor ocupacion por cada semana del año 2019 (final)
select numero as semana, 
        (select ('id: '||id_aloj||' capacidad: '||capacidad||' compartida: '||compartida||' tipo: '||tipo||' ubicacion: '||ubicacion||' operador: '||id_op||' numero de reservas: '||num_reserv) as alojamiento
        from (select id_aloj, num_reserv
                from(select id_aloj, num_reserv 
                        from NUM_RESERV_ALOJ_SEM 
                        where semana = numero and anio = 2019 
                        order by num_reserv desc
                    ) uno
                where rownum = 1
            ) dos
        inner join
        alojamientos
        on dos.id_aloj = alojamientos.id
        ) as alojamiento_mayor_ocupacion,
        (select ('id: '||id_aloj||' capacidad: '||capacidad||' compartida: '||compartida||' tipo: '||tipo||' ubicacion: '||ubicacion||' operador: '||id_op||' numero de reservas: '||num_reserv) as alojamiento
        from (select id_aloj, num_reserv
                from(select id_aloj, num_reserv 
                        from NUM_RESERV_ALOJ_SEM 
                        where semana = numero and anio = 2019 
                        order by num_reserv asc
                    ) uno
                where rownum = 1
            ) dos
        inner join
        alojamientos
        on dos.id_aloj = alojamientos.id
        ) as alojamiento_menor_ocupacion
from semanas;

--cuantas reservas tiene el operador 21664 en la semana 1 del 2019 (cinco)
select id_op, sum(num_reserv) num_reservas from NUM_RESERV_ALOJ_SEM where semana = 1 and anio = 2019 and id_op = 21664 group by ID_OP;

--cuantas reservas tienen los peradores en la semana 1 del 2019 (seis)
select id_op, sum(num_reserv) num_reservas from NUM_RESERV_ALOJ_SEM where semana = 1 and anio = 2019 group by ID_OP order by num_reservas desc;

--cual es el id del operador con mas reservas en la semana 1 del 2019 (siete)
select *
from (select id_op, sum(num_reserv) num_reservas from NUM_RESERV_ALOJ_SEM where semana = 1 and anio = 2019 group by ID_OP order by num_reservas desc) seis
where rownum = 1;

--cual es el operador con mas reservas en la semana 1 del año 2019
select ('id: '||id_op||' nombre: '||nombre||' tipo: '||tipo||' numero de reservas: '||num_reservas) as alojamiento
from (select *
        from (select id_op, sum(num_reserv) num_reservas from NUM_RESERV_ALOJ_SEM where semana = 1 and anio = 2019 group by ID_OP order by num_reservas desc) seis
        where rownum = 1
    )siete
    inner join
    operadores
    on siete.id_op = operadores.id;
    
--cual es el operador con mas reservas por cada semana del año 2019
select numero as semana, (select ('id: '||id_op||' nombre: '||nombre||' tipo: '||tipo||' numero de reservas: '||num_reservas) as alojamiento
                            from (select *
                                    from (select id_op, sum(num_reserv) num_reservas from NUM_RESERV_ALOJ_SEM where semana = numero and anio = 2019 group by ID_OP order by num_reservas desc) seis
                                    where rownum = 1
                                )siete
                                inner join
                                operadores
                                on siete.id_op = operadores.id
                        )as operador_mas_solicitado
from semanas;

--cual es el operador con menos reservas por cada semana del año 2019
select numero as semana, (select ('id: '||id_op||' nombre: '||nombre||' tipo: '||tipo||' numero de reservas: '||num_reservas) as alojamiento
                            from (select *
                                    from (select id_op, sum(num_reserv) num_reservas from NUM_RESERV_ALOJ_SEM where semana = numero and anio = 2019 group by ID_OP order by num_reservas asc) seis
                                    where rownum = 1
                                )siete
                                inner join
                                operadores
                                on siete.id_op = operadores.id
                        )as operador_menos_solicitado
from semanas;

--cual es el operador con mas/menos reservas por cada semana del año 2019 (final)
select numero as semana, (select ('id: '||id_op||' nombre: '||nombre||' tipo: '||tipo||' numero de reservas: '||num_reservas) as alojamiento
                            from (select *
                                    from (select id_op, sum(num_reserv) num_reservas from NUM_RESERV_ALOJ_SEM where semana = numero and anio = 2019 group by ID_OP order by num_reservas desc) seis
                                    where rownum = 1
                                )siete
                                inner join
                                operadores
                                on siete.id_op = operadores.id
                        )as operador_mas_solicitado,

                        (select ('id: '||id_op||' nombre: '||nombre||' tipo: '||tipo||' numero de reservas: '||num_reservas) as alojamiento
                            from (select *
                                    from (select id_op, sum(num_reserv) num_reservas from NUM_RESERV_ALOJ_SEM where semana = numero and anio = 2019 group by ID_OP order by num_reservas asc) seis
                                    where rownum = 1
                                )siete
                                inner join
                                operadores
                                on siete.id_op = operadores.id
                        )as operador_menos_solicitado
from semanas;

