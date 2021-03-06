
/*
Hola. La consulta de este documento no fue la utilizada en el programa.
Fue la primera versi�n de la consulta creada. Se guarda para referencia.

Para ver la consulta optimizada, por favor vaya a RFC13_OPTIMIZADO.sql que deber�a encontrarse en la misma carpeta que este archivo.
*/



create table semanas( numero integer);
insert into semanas (numero)values(1);
insert into semanas (numero)values(2);
insert into semanas (numero)values(3);
insert into semanas (numero)values(4);
insert into semanas (numero)values(5);
insert into semanas (numero)values(6);
insert into semanas (numero)values(7);
insert into semanas (numero)values(8);
insert into semanas (numero)values(9);
insert into semanas (numero)values(10);
insert into semanas (numero)values(11);
insert into semanas (numero)values(12);
insert into semanas (numero)values(13);
insert into semanas (numero)values(14);
insert into semanas (numero)values(15);
insert into semanas (numero)values(16);
insert into semanas (numero)values(17);
insert into semanas (numero)values(18);
insert into semanas (numero)values(19);
insert into semanas (numero)values(20);
insert into semanas (numero)values(21);
insert into semanas (numero)values(22);
insert into semanas (numero)values(23);
insert into semanas (numero)values(24);
insert into semanas (numero)values(25);
insert into semanas (numero)values(26);
insert into semanas (numero)values(27);
insert into semanas (numero)values(28);
insert into semanas (numero)values(29);
insert into semanas (numero)values(30);
insert into semanas (numero)values(31);
insert into semanas (numero)values(32);
insert into semanas (numero)values(33);
insert into semanas (numero)values(34);
insert into semanas (numero)values(35);
insert into semanas (numero)values(36);
insert into semanas (numero)values(37);
insert into semanas (numero)values(38);
insert into semanas (numero)values(39);
insert into semanas (numero)values(40);
insert into semanas (numero)values(41);
insert into semanas (numero)values(42);
insert into semanas (numero)values(43);
insert into semanas (numero)values(44);
insert into semanas (numero)values(45);
insert into semanas (numero)values(46);
insert into semanas (numero)values(47);
insert into semanas (numero)values(48);
insert into semanas (numero)values(49);
insert into semanas (numero)values(50);
insert into semanas (numero)values(51);
insert into semanas (numero)values(52);

-- para probar
update reservas set fecha_fin = '02/11/19' where fecha_inicio = '02/05/19' and id_al_of=10 and fecha_creacion_of ='01/01/14';
insert into RESERVAS (FECHA_INICIO, FECHA_FIN, TIPO_CONTRATO, NUM_PERSONAS, ID_AL_OF,FECHA_CREACION_OF,ID_CLIENTE, PRECIO_RESERVA, ESTADO, FECHA_CREACION) values ('03/05/16', '02/11/19', 'Considine', 1, 81, '01/01/14', 201630956, 9463533, 'RESERVADA', '03/03/16');


--en que semana esta la fecha 22/12/17 (la semana uno empieza el primero de enero)
select to_char(to_date('02/11/19'), 'ww') from dual;

--cuando empieza y cuando termina cada reserva del a�o 2019 en semanas(uno)
select FECHA_INICIO,
        fecha_fin,
        ID_AL_OF, 
        FECHA_CREACION_OF,         
        case when extract(year from fecha_inicio) = 2019 then to_char(to_date(fecha_inicio), 'ww') else '0' end as semana_inicio, 
        case when extract(year from fecha_fin) = 2019 then to_char(to_date(fecha_fin), 'ww') else '52' end as semana_fin 
from reservas
where extract(year from fecha_inicio) = 2019 or 
    extract(year from fecha_inicio) < 2019  and (extract(year from fecha_fin) > 2019 or extract(year from fecha_fin) = 2019) or 
    extract(year from fecha_fin) = 2019 or 
    extract(year from fecha_fin) > 2019  and (extract(year from fecha_inicio) < 2019 or extract(year from fecha_inicio) = 2019);

-- cuantas reservas hay en la semana 44 del 2019 (dos)
select count(*)
from (select FECHA_INICIO,
        fecha_fin,
        ID_AL_OF, 
        FECHA_CREACION_OF,         
        case when extract(year from fecha_inicio) = 2019 then to_char(to_date(fecha_inicio), 'ww') else '0' end as semana_inicio, 
        case when extract(year from fecha_fin) = 2019 then to_char(to_date(fecha_fin), 'ww') else '52' end as semana_fin 
    from reservas
    where extract(year from fecha_inicio) = 2019 or 
        extract(year from fecha_inicio) < 2019  and (extract(year from fecha_fin) > 2019 or extract(year from fecha_fin) = 2019) or 
        extract(year from fecha_fin) = 2019 or 
        extract(year from fecha_fin) > 2019  and (extract(year from fecha_inicio) < 2019 or extract(year from fecha_inicio) = 2019)
    )uno
where (uno.semana_inicio < 44 or uno.semana_inicio = 44) and (uno.semana_fin > 44 or uno.semana_fin = 44);

-- cuantas reservas hay en la semana 44 del 2019 en el alojamiento 10 (tres)
select count(*)
from (select FECHA_INICIO,
        fecha_fin,
        ID_AL_OF, 
        FECHA_CREACION_OF,         
        case when extract(year from fecha_inicio) = 2019 then to_char(to_date(fecha_inicio), 'ww') else '0' end as semana_inicio, 
        case when extract(year from fecha_fin) = 2019 then to_char(to_date(fecha_fin), 'ww') else '52' end as semana_fin 
    from reservas
    where extract(year from fecha_inicio) = 2019 or 
        extract(year from fecha_inicio) < 2019  and (extract(year from fecha_fin) > 2019 or extract(year from fecha_fin) = 2019) or 
        extract(year from fecha_fin) = 2019 or 
        extract(year from fecha_fin) > 2019  and (extract(year from fecha_inicio) < 2019 or extract(year from fecha_inicio) = 2019)
    )uno
where (uno.semana_inicio < 44 or uno.semana_inicio = 44) and (uno.semana_fin > 44 or uno.semana_fin = 44) and UNO.ID_AL_OF = 10;

--cual es el  alojamiento que mas reservas tiene en la semana 49 del a�o 2019, no hay reservas en esta semana (cuatro) 
select concat(alojamiento, concat(', numero de reservas: ',reservas))aloj_reserv
from(select concat('id: ',concat(id, concat(', capacidad: ', concat(capacidad,concat(', compartida: ', concat(compartida, concat(', tipo: ', concat(tipo, concat(', ubicacion: ', ubicacion))))))))) alojamiento, (select count(*)
        from (select FECHA_INICIO,
                    fecha_fin,
                    ID_AL_OF, 
                    FECHA_CREACION_OF,         
                    case when extract(year from fecha_inicio) = 2019 then to_char(to_date(fecha_inicio), 'ww') else '0' end as semana_inicio, 
                    case when extract(year from fecha_fin) = 2019 then to_char(to_date(fecha_fin), 'ww') else '52' end as semana_fin 
                from reservas
                where extract(year from fecha_inicio) = 2019 or 
                    extract(year from fecha_inicio) < 2019  and (extract(year from fecha_fin) > 2019 or extract(year from fecha_fin) = 2019) or 
                    extract(year from fecha_fin) = 2019 or 
                    extract(year from fecha_fin) > 2019  and (extract(year from fecha_inicio) < 2019 or extract(year from fecha_inicio) = 2019)
            )uno
        where (uno.semana_inicio < 44 or uno.semana_inicio = 44) and (uno.semana_fin > 44 or uno.semana_fin = 44) and UNO.ID_AL_OF = id) reservas
from alojamientos
order by reservas  desc)
where rownum = 1;

-- cuantas reservas hay en cada semana del 2019 en el alojamiento 10 (cinco) NO SIRVE PA NADA
select numero, (select count(*) as numero_reservas
                from (select FECHA_INICIO,
                            fecha_fin,
                            ID_AL_OF, 
                            FECHA_CREACION_OF,         
                            case when extract(year from fecha_inicio) = 2019 then to_char(to_date(fecha_inicio), 'ww') else '0' end as semana_inicio, 
                            case when extract(year from fecha_fin) = 2019 then to_char(to_date(fecha_fin), 'ww') else '52' end as semana_fin 
                        from reservas
                        where
                            extract(year from fecha_inicio) <= 2019  and (extract(year from fecha_fin) >= 2019) or  
                            extract(year from fecha_fin) >= 2019  and (extract(year from fecha_inicio) <= 2019)
                    )uno
                where (uno.semana_inicio < numero or uno.semana_inicio = numero) and (uno.semana_fin > numero or uno.semana_fin = numero) and UNO.ID_AL_OF = 10
                ) as numero_reservas
from semanas;

-- cual es el alojamiento mas ocupado en cada semana del 2017 (final)
select numero as semana, (select concat(alojamiento, concat(', numero de reservas: ',reservas))aloj_reserv
from(select concat('id: ',concat(id, concat(', capacidad: ', concat(capacidad,concat(', compartida: ', concat(compartida, concat(', tipo: ', concat(tipo, concat(', ubicacion: ', ubicacion))))))))) alojamiento, (select count(*)
        from (select FECHA_INICIO,
                    fecha_fin,
                    ID_AL_OF, 
                    FECHA_CREACION_OF,         
                    case when extract(year from fecha_inicio) = 2017 then to_char(to_date(fecha_inicio), 'ww') else '0' end as semana_inicio, 
                    case when extract(year from fecha_fin) = 2017 then to_char(to_date(fecha_fin), 'ww') else '52' end as semana_fin 
                from reservas
                where extract(year from fecha_inicio) = 2017 or 
                    extract(year from fecha_inicio) < 2017  and (extract(year from fecha_fin) > 2017 or extract(year from fecha_fin) = 2017) or 
                    extract(year from fecha_fin) = 2017 or 
                    extract(year from fecha_fin) > 2017  and (extract(year from fecha_inicio) < 2017 or extract(year from fecha_inicio) = 2017)
            )uno
        where (uno.semana_inicio < numero or uno.semana_inicio = numero) and (uno.semana_fin > numero or uno.semana_fin = numero) and UNO.ID_AL_OF = id) reservas
from alojamientos
order by reservas  desc)
where rownum = 1) alojamiento_mayor__ocupacion
from semanas;

-- cual es el alojamiento menos ocupado en cada semana del 2019 (final)
select numero as semana, (select concat(alojamiento, concat(', numero de reservas: ',reservas))aloj_reserv
from(select concat('id: ',concat(id, concat(', capacidad: ', concat(capacidad,concat(', compartida: ', concat(compartida, concat(', tipo: ', concat(tipo, concat(', ubicacion: ', ubicacion))))))))) alojamiento, (select count(*)
        from (select FECHA_INICIO,
                    fecha_fin,
                    ID_AL_OF, 
                    FECHA_CREACION_OF,         
                    case when extract(year from fecha_inicio) = 2019 then to_char(to_date(fecha_inicio), 'ww') else '0' end as semana_inicio, 
                    case when extract(year from fecha_fin) = 2019 then to_char(to_date(fecha_fin), 'ww') else '52' end as semana_fin 
                from reservas
                where extract(year from fecha_inicio) = 2019 or 
                    extract(year from fecha_inicio) < 2019  and (extract(year from fecha_fin) > 2019 or extract(year from fecha_fin) = 2019) or 
                    extract(year from fecha_fin) = 2019 or 
                    extract(year from fecha_fin) > 2019  and (extract(year from fecha_inicio) < 2019 or extract(year from fecha_inicio) = 2019)
            )uno
        where (uno.semana_inicio < numero or uno.semana_inicio = numero) and (uno.semana_fin > numero or uno.semana_fin = numero) and UNO.ID_AL_OF = id) reservas
from alojamientos
order by reservas  asc)
where rownum = 1) alojamiento_mayor__ocupacion
from semanas;

--cuales son las reservas de la semana 44(seis)
select *
from (select FECHA_INICIO,
        fecha_fin,
        ID_AL_OF, 
        FECHA_CREACION_OF,         
        case when extract(year from fecha_inicio) = 2019 then to_char(to_date(fecha_inicio), 'ww') else '0' end as semana_inicio, 
        case when extract(year from fecha_fin) = 2019 then to_char(to_date(fecha_fin), 'ww') else '52' end as semana_fin 
    from reservas
    where extract(year from fecha_inicio) = 2019 or 
        extract(year from fecha_inicio) < 2019  and (extract(year from fecha_fin) > 2019 or extract(year from fecha_fin) = 2019) or 
        extract(year from fecha_fin) = 2019 or 
        extract(year from fecha_fin) > 2019  and (extract(year from fecha_inicio) < 2019 or extract(year from fecha_inicio) = 2019)
    )uno
where (uno.semana_inicio < 44 or uno.semana_inicio = 44) and (uno.semana_fin > 44 or uno.semana_fin = 44);

--cuales son los operadores que tienen reservas en la semana 44(siete)
select alojamientos.id_op
from alojamientos
    inner join
    (select *
    from (select FECHA_INICIO,
                fecha_fin,
                ID_AL_OF, 
                FECHA_CREACION_OF,         
                case when extract(year from fecha_inicio) = 2019 then to_char(to_date(fecha_inicio), 'ww') else '0' end as semana_inicio, 
                case when extract(year from fecha_fin) = 2019 then to_char(to_date(fecha_fin), 'ww') else '52' end as semana_fin 
        from reservas
        where extract(year from fecha_inicio) = 2019 or 
            extract(year from fecha_inicio) < 2019  and (extract(year from fecha_fin) > 2019 or extract(year from fecha_fin) = 2019) or 
            extract(year from fecha_fin) = 2019 or 
            extract(year from fecha_fin) > 2019  and (extract(year from fecha_inicio) < 2019 or extract(year from fecha_inicio) = 2019)
        )uno
    where (uno.semana_inicio < 44 or uno.semana_inicio = 44) and (uno.semana_fin > 44 or uno.semana_fin = 44)
    
    ) seis
    on alojamientos.id = seis.id_al_of;


--cuantas reservas tienen los operadores que tienen reservas en la semana 44(ocho)
select alojamientos.id_op, count(*) as num_reservas
from alojamientos
    inner join
    (select *
    from (select FECHA_INICIO,
                fecha_fin,
                ID_AL_OF, 
                FECHA_CREACION_OF,         
                case when extract(year from fecha_inicio) = 2019 then to_char(to_date(fecha_inicio), 'ww') else '0' end as semana_inicio, 
                case when extract(year from fecha_fin) = 2019 then to_char(to_date(fecha_fin), 'ww') else '52' end as semana_fin 
        from reservas
        where extract(year from fecha_inicio) = 2019 or 
            extract(year from fecha_inicio) < 2019  and (extract(year from fecha_fin) > 2019 or extract(year from fecha_fin) = 2019) or 
            extract(year from fecha_fin) = 2019 or 
            extract(year from fecha_fin) > 2019  and (extract(year from fecha_inicio) < 2019 or extract(year from fecha_inicio) = 2019)
        )uno
    where (uno.semana_inicio < 44 or uno.semana_inicio = 44) and (uno.semana_fin > 44 or uno.semana_fin = 44)
    
    ) siete
    on alojamientos.id = siete.id_al_of
group by(id_op);


--cuantas reservas tienen los operadores de la semana 44(nueve)
select 'id:'||operadores.id||', nombre: '||operadores.nombre||', tipo: '||operadores.tipo||', numero de reservas: ' as operadores, case when ocho.num_reservas is not null then ocho.num_reservas else 0 end as num_reservas
from operadores
    left outer join
    (select alojamientos.id_op, count(*) as num_reservas
    from alojamientos
    inner join
    (select *
    from (select FECHA_INICIO,
                fecha_fin,
                ID_AL_OF, 
                FECHA_CREACION_OF,         
                case when extract(year from fecha_inicio) = 2019 then to_char(to_date(fecha_inicio), 'ww') else '0' end as semana_inicio, 
                case when extract(year from fecha_fin) = 2019 then to_char(to_date(fecha_fin), 'ww') else '52' end as semana_fin 
        from reservas
        where extract(year from fecha_inicio) = 2019 or 
            extract(year from fecha_inicio) < 2019  and (extract(year from fecha_fin) > 2019 or extract(year from fecha_fin) = 2019) or 
            extract(year from fecha_fin) = 2019 or 
            extract(year from fecha_fin) > 2019  and (extract(year from fecha_inicio) < 2019 or extract(year from fecha_inicio) = 2019)
        )uno
    where (uno.semana_inicio < 44 or uno.semana_inicio = 44) and (uno.semana_fin > 44 or uno.semana_fin = 44)
    
    ) siete
    on alojamientos.id = siete.id_al_of
    group by(id_op)
    ) ocho
    on
    operadores.id = ocho.id_op;


--cual es el operador con mas reservas en la semana 44
select operadores
from (
    select 'id:'||operadores.id||', nombre: '||operadores.nombre||', tipo: '||operadores.tipo||', numero de reservas: '||case when ocho.num_reservas is not null then ocho.num_reservas else 0 end as operadores, case when ocho.num_reservas is not null then ocho.num_reservas else 0 end as num_reservas
from operadores
    left outer join
    (select alojamientos.id_op, count(*) as num_reservas
    from alojamientos
    inner join
    (select *
    from (select FECHA_INICIO,
                fecha_fin,
                ID_AL_OF, 
                FECHA_CREACION_OF,         
                case when extract(year from fecha_inicio) = 2019 then to_char(to_date(fecha_inicio), 'ww') else '0' end as semana_inicio, 
                case when extract(year from fecha_fin) = 2019 then to_char(to_date(fecha_fin), 'ww') else '52' end as semana_fin 
        from reservas
        where extract(year from fecha_inicio) = 2019 or 
            extract(year from fecha_inicio) < 2019  and (extract(year from fecha_fin) > 2019 or extract(year from fecha_fin) = 2019) or 
            extract(year from fecha_fin) = 2019 or 
            extract(year from fecha_fin) > 2019  and (extract(year from fecha_inicio) < 2019 or extract(year from fecha_inicio) = 2019)
        )uno
    where (uno.semana_inicio < 44 or uno.semana_inicio = 44) and (uno.semana_fin > 44 or uno.semana_fin = 44)
    
    ) siete
    on alojamientos.id = siete.id_al_of
    group by(id_op)
    ) ocho
    on
    operadores.id = ocho.id_op
    order by num_reservas desc)
where rownum = 1;

-- cual es el operador con mas reservas por cada semana
select numero as semana, (select operadores
from (
    select 'id:'||operadores.id||', nombre: '||operadores.nombre||', tipo: '||operadores.tipo||', numero de reservas: '||case when ocho.num_reservas is not null then ocho.num_reservas else 0 end as operadores, case when ocho.num_reservas is not null then ocho.num_reservas else 0 end as num_reservas
    from operadores
        left outer join
        (select alojamientos.id_op, count(*) as num_reservas
        from alojamientos
            inner join
            (select *
            from (select FECHA_INICIO,
                        fecha_fin,
                        ID_AL_OF, 
                        FECHA_CREACION_OF,         
                        case when extract(year from fecha_inicio) = 2019 then to_char(to_date(fecha_inicio), 'ww') else '0' end as semana_inicio, 
                        case when extract(year from fecha_fin) = 2019 then to_char(to_date(fecha_fin), 'ww') else '52' end as semana_fin 
                from reservas
                where extract(year from fecha_inicio) = 2019 or 
                    extract(year from fecha_inicio) < 2019  and (extract(year from fecha_fin) > 2019 or extract(year from fecha_fin) = 2019) or 
                    extract(year from fecha_fin) = 2019 or 
                    extract(year from fecha_fin) > 2019  and (extract(year from fecha_inicio) < 2019 or extract(year from fecha_inicio) = 2019)
                )uno
            where (uno.semana_inicio < numero or uno.semana_inicio = numero) and (uno.semana_fin > numero or uno.semana_fin = numero) 
            ) siete
            on alojamientos.id = siete.id_al_of
            group by(id_op)
    ) ocho
    on
    operadores.id = ocho.id_op
    order by num_reservas desc)
    where rownum = 1) as operadores 
from semanas;

-- cual es el operador con menos reservas por cada semana
select numero as semana, (select operadores
from (
    select 'id:'||operadores.id||', nombre: '||operadores.nombre||', tipo: '||operadores.tipo||', numero de reservas: '||case when ocho.num_reservas is not null then ocho.num_reservas else 0 end as operadores, case when ocho.num_reservas is not null then ocho.num_reservas else 0 end as num_reservas
    from operadores
        left outer join
        (select alojamientos.id_op, count(*) as num_reservas
        from alojamientos
            inner join
            (select *
            from (select FECHA_INICIO,
                        fecha_fin,
                        ID_AL_OF, 
                        FECHA_CREACION_OF,         
                        case when extract(year from fecha_inicio) = 2019 then to_char(to_date(fecha_inicio), 'ww') else '0' end as semana_inicio, 
                        case when extract(year from fecha_fin) = 2019 then to_char(to_date(fecha_fin), 'ww') else '52' end as semana_fin 
                from reservas
                where extract(year from fecha_inicio) = 2019 or 
                    extract(year from fecha_inicio) < 2019  and (extract(year from fecha_fin) > 2019 or extract(year from fecha_fin) = 2019) or 
                    extract(year from fecha_fin) = 2019 or 
                    extract(year from fecha_fin) > 2019  and (extract(year from fecha_inicio) < 2019 or extract(year from fecha_inicio) = 2019)
                )uno
            where (uno.semana_inicio < numero or uno.semana_inicio = numero) and (uno.semana_fin > numero or uno.semana_fin = numero) 
            ) siete
            on alojamientos.id = siete.id_al_of
            group by(id_op)
    ) ocho
    on
    operadores.id = ocho.id_op
    order by num_reservas asc)
    where rownum = 1) as operadores 
from semanas;