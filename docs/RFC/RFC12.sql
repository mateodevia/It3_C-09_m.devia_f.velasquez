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

--cuando empieza y cuando termina cada reserva del año 2019 en semanas(uno)
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

--cual es el  alojamiento que mas reservas tiene en la semana 49 del año 2019, no hay reservas en esta semana (cuatro) 
select *
from(select id, (select count(*)
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
        where (uno.semana_inicio < 49 or uno.semana_inicio = 49) and (uno.semana_fin > 49 or uno.semana_fin = 49) and UNO.ID_AL_OF = id) reservas
from alojamientos
order by reservas  desc)
where rownum = 1;

-- cuantas reservas hay en cada semana del 2019 en el alojamiento 10 (cinco)
select numero, (select count(*) as numero_reservas
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
                where (uno.semana_inicio < numero or uno.semana_inicio = numero) and (uno.semana_fin > numero or uno.semana_fin = numero) and UNO.ID_AL_OF = 10
                ) as numero_reservas
from semanas;


