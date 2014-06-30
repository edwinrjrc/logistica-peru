CREATE TABLE negocio."ServicioCabecera"
(
  id bigint NOT NULL,
  idcliente integer NOT NULL,
  fechaservicio date NOT NULL,
  montototal double precision NOT NULL,
  cantidadservicios integer,
  iddestino integer,
  descripciondestino character varying(15),
  usuariocreacion character varying(20) NOT NULL,
  fechacreacion timestamp with time zone NOT NULL,
  ipcreacion character(15) NOT NULL,
  usuariomodificacion character varying(15) NOT NULL,
  fechamodificacion timestamp with time zone NOT NULL,
  ipmodificacion character(15) NOT NULL,
  idestadoregistro integer NOT NULL DEFAULT 1,
  CONSTRAINT pk_serviciocabecera PRIMARY KEY (id)
);


CREATE TABLE negocio."ServicioDetalle"
(
  id bigint NOT NULL,
  idtiposervicio integer NOT NULL,
  idservicio integer NOT NULL,
  fechaservicio date NOT NULL,
  montototal double precision NOT NULL,
  cantidadservicios integer,
  iddestino integer,
  descripciondestino character varying(15),
  dias integer,
  noches integer,
  fechaida date,
  fecharegreso date,
  cantidad integer,
  preciounitario double precision NOT NULL,
  usuariocreacion character varying(20) NOT NULL,
  fechacreacion timestamp with time zone NOT NULL,
  ipcreacion character(15) NOT NULL,
  usuariomodificacion character varying(15) NOT NULL,
  fechamodificacion timestamp with time zone NOT NULL,
  ipmodificacion character(15) NOT NULL,
  idestadoregistro integer NOT NULL DEFAULT 1,
  CONSTRAINT pk_serviciodetalle PRIMARY KEY (id)
);

CREATE SEQUENCE negocio.seq_serviciocabecera
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE SEQUENCE negocio.seq_serviciodetalle
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
  