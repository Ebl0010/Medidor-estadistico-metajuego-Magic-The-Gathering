drop table if exists torneos;
drop table if exists barajas_usuarios;
drop table if exists cruces;
drop table if exists roles_usuarios;
drop table if exists barajas;
drop table if exists usuarios;
drop table if exists roles;

create table usuarios (
    nombre_usuario	varchar(20) Primary key,
    clave	varchar(16) NOT NULL,
    correo	varchar(50),
    rondas_ganadas	int,
    rondas_empatadas	int,
    rondas_perdidas	int,
    partidas_ganadas	int,
    partidas_perdidas	int,
    porcentaje_rondas	decimal(4,2),
    porcentaje_partidas	decimal(4,2)
    );
    
    create table barajas (
    nombre_baraja	varchar(20) Primary key,
    tier	int(11) not null,
    ganadas_main	int,
    ganadas_side	int,
    perdidas_main	int,
    perdidas_side	int,
    porcentaje_main	decimal(4,2),
    porcentaje_side	decimal(4,2),
    porcentaje_total	decimal(4,2)
    );

create table roles (
    idRol int primary key,
    descripcion varchar(40)
    );

create table roles_usuarios (
    nombre_usuario	varchar(20),
    idRol	int, 
    estado	int,
    foreign key (nombre_usuario) references usuarios(nombre_usuario) on delete cascade on update cascade,
    primary key(nombre_usuario, idRol)
    );

create table barajas_usuarios (
    nombre_usuario	varchar(20),
    nombre_baraja	varchar(20),
    rondas_ganadas	int,
    rondas_perdidas	int,
    rondas_empatadas	int,
    partidas_ganadas_main	int,
    partidas_perdidas_main	int,
    partidas_ganadas_side	int,
    partidas_perdidas_side	int,
    porcentaje_rondas_ganadas	decimal(4,2),
    porcentaje_partidas_ganadas_main	decimal(4,2),
    porcentaje_partidas_ganadas_side	decimal(4,2),
    porcentaje_partidas_ganadas_total	decimal(4,2),
    foreign key (nombre_usuario) references usuarios(nombre_usuario) on delete cascade on update cascade,
    foreign key (nombre_baraja) references barajas(nombre_baraja) on delete cascade on update cascade,
    primary key (nombre_usuario, nombre_baraja)
    );

create table cruces (
    nombre_baraja_1	varchar(20),
    nombre_baraja_2	varchar(20),
    ganadas_main_1	int,
    perdidas_main_1	int,
    ganadas_side_1	int,
    perdidas_side_1	int,
    porcentaje_main_1	decimal(4,2),
    porcentaje_side_1	decimal(4,2),
    porcentaje_total_1	decimal(4,2),
    foreign key (nombre_baraja_1) references barajas(nombre_baraja) on delete cascade on update cascade,
    foreign key (nombre_baraja_2) references barajas(nombre_baraja) on delete cascade on update cascade,
    primary key (nombre_baraja_1, nombre_baraja_2)
    );

create table torneos (
    nombre_usuario	varchar(20),
    nombre_baraja	varchar(20),
    resultado	varchar(9),
    puntos	int,
    repeticiones	int,
    foreign key (nombre_usuario) references usuarios(nombre_usuario) on delete cascade on update cascade,
    foreign key (nombre_baraja) references barajas(nombre_baraja) on delete cascade on update cascade,
    primary key (nombre_usuario, nombre_baraja)
    );

insert into roles values (0, 'administrador');
insert into roles values (1, 'estandar');





    
