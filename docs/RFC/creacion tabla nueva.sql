--Para poblar la tabla NUM_RESERV_ALOJ_SEM

create table NUM_RESERV_ALOJ_SEM(ID_ALOJ NUMBER(19),
                                    ID_OP NUMBER(19),
                                    SEMANA INTEGER,
                                    ANIO NUMBER (19),
                                    NUM_RESERV INTEGER);

select * from NUM_RESERV_ALOJ_SEM where id_aloj = 74157 order by id_aloj,anio, semana;

select * from alojamientos where id = 348;
                            
ALTER TABLE NUM_RESERV_ALOJ_SEM 
ADD CONSTRAINT PK_NUM_RESERV_ALOJ_SEM PRIMARY KEY(ID_ALOJ, SEMANA, ANIO);

ALTER TABLE NUM_RESERV_ALOJ_SEM 
ADD CONSTRAINT FK_NUM_RESERV_ALOJ_SEM FOREIGN KEY(ID_ALOJ) REFERENCES ALOJAMIENTOS(ID);

ALTER TABLE NUM_RESERV_ALOJ_SEM 
ADD CONSTRAINT FK_NUM_RESERV_ALOJ_SEM_OP FOREIGN KEY(ID_OP) REFERENCES OPERADORES(ID);

--que cuando comienza y termina cada reserva del 2019
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
    
--demana de inicio y fin de cada reserva con su id_al
select id_al_of, (select to_char(to_date(reservas.fecha_Inicio), 'ww') from dual) as semana_inicio, (extract(year from fecha_inicio)) as anio_inicio, (select to_char(to_date(reservas.fecha_fin), 'ww') from dual) as semana_fin, (extract(year from fecha_fin)) as anio_fin from reservas order by id_al_of;

select * from reservas;

