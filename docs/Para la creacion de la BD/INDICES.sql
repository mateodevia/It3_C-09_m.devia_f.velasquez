--Indices en las FKs
create index aloj_serv_id_aloj
on aloj_serv(id_aloj);

create index aloj_serv_id_serv
on aloj_serv(id_serv);

create index aloj_id_op
on alojamientos(id_op);

create index ofertas_id_al
on ofertas_alojamientos(id_al);
--NO
create index reserv_col_serv_id_reserv
on reserv_col_serv(id_reserv_col);
--NO
create index reserv_col_serv_id_serv
on reserv_col_serv(id_serv);

create index reserv_id_al_fech_creacion
on reservas(id_al_of, fecha_creacion);

create index reserv_id_cliente
on reservas(id_cliente);
--NO
create index reserv_col_id_cliente
on reservas_colectivas(id_cliente);