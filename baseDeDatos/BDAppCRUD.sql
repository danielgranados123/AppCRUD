CREATE TABLE tbUsuarios(
UUID_usuario VARCHAR2(50),
correoElectronico VARCHAR2(50),
clave VARCHAR2(50)
)

create table tbTickets(
numTicket VARCHAR2(50),
titulo varchar2(50),
descripcion varchar2(200),
autor varchar2(20),
emailAutor varchar2(20),
fechaCreacion varchar2(50),
estado varchar2(10),
fechaFinalizacion varchar2(50)
);

select * from tbUsuarios

select * from tbTickets