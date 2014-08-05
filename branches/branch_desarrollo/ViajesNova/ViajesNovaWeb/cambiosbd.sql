-- Function: negocio.fn_consultarservicioventa(integer)

-- DROP FUNCTION negocio.fn_consultarservicioventa(integer);

CREATE OR REPLACE FUNCTION negocio.fn_consultarservicioventa(p_idservicio integer)
  RETURNS refcursor AS
$BODY$
declare micursor refcursor;

begin

open micursor for
select sercab.id, sercab.idcliente1, cli1.nombres as nombres1, cli1.apellidopaterno as apellidopaterno1, cli1.apellidomaterno as apellidomaterno1, 
       sercab.idcliente2, cli2.nombres as nombres2, cli2.apellidopaterno as apellidopaterno2, cli2.apellidomaterno as apellidomaterno2, 
       sercab.fechaservicio, sercab.montototal, sercab.cantidadservicios, 
       sercab.iddestino, des.descripcion as descdestino, sercab.descripciondestino,
       sercab.idformapago, maemp.nombre as nommediopago, maemp.descripcion as descmediopago,
       sercab.idestadopago, maeep.nombre as nomestpago, maeep.descripcion as descestpago,
       sercab.nrocuotas, sercab.tea, sercab.valorcuota, sercab.fechaprimercuota, sercab.fechaultcuota, sercab.montocomisiontotal,
       usu.nombres as nombresvendedor, usu.apepaterno, usu.apematerno,
       sercab.usuariocreacion, sercab.fechacreacion, sercab.ipcreacion, 
       sercab.usuariomodificacion, sercab.fechamodificacion, sercab.ipmodificacion
  from negocio."ServicioCabecera" sercab 
 inner join negocio.vw_clientesnova cli1 on sercab.idcliente1 = cli1.id
 inner join soporte.destino des on sercab.iddestino = des.id
 inner join soporte."Tablamaestra" maemp on maemp.estado = 'A' and maemp.idmaestro = 13 and maemp.id = sercab.idformapago
 inner join soporte."Tablamaestra" maeep on maeep.estado = 'A' and maeep.idmaestro = 14 and maeep.id = sercab.idestadopago
 inner join seguridad.usuario usu on usu.id = sercab.idvendedor
  left join negocio.vw_clientesnova cli2 on sercab.idcliente2 = cli2.id
 where sercab.idestadoregistro = 1
   and sercab.id               = p_idservicio;

return micursor;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
  
  -- Function: negocio.fn_consultarservicioventadetalle(integer)

-- DROP FUNCTION negocio.fn_consultarservicioventadetalle(integer);

CREATE OR REPLACE FUNCTION negocio.fn_consultarcronogramaservicio(p_idservicio integer)
  RETURNS refcursor AS
$BODY$
declare micursor refcursor;

begin
open micursor for
SELECT nrocuota, idservicio, fechavencimiento, capital, interes, totalcuota, 
       idestadocuota, usuariocreacion, fechacreacion, ipcreacion, usuariomodificacion, 
       fechamodificacion, ipmodificacion, idestadoregistro
  FROM negocio."CronogramaPago"
 WHERE idservicio = p_idservicio;

return micursor;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

  
  -- Table: negocio."ServicioCabecera"

-- DROP TABLE negocio."ServicioCabecera";

CREATE TABLE negocio."ServicioCabecera"
(
  id bigint NOT NULL,
  idcliente1 integer NOT NULL,
  idcliente2 integer,
  fechaservicio date NOT NULL,
  cantidadservicios integer,
  iddestino integer,
  descripciondestino character varying(15),
  idformapago integer,
  idestadopago integer,
  nrocuotas integer,
  tea numeric,
  valorcuota numeric,
  fechaprimercuota date,
  fechaultcuota date,
  montocomisiontotal numeric NOT NULL,
  montototal numeric NOT NULL,
  montototalfee numeric NOT NULL,
  idvendedor integer NOT NULL,
  usuariocreacion character varying(20) NOT NULL,
  fechacreacion timestamp with time zone NOT NULL,
  ipcreacion character(15) NOT NULL,
  usuariomodificacion character varying(15) NOT NULL,
  fechamodificacion timestamp with time zone NOT NULL,
  ipmodificacion character(15) NOT NULL,
  idestadoregistro integer NOT NULL DEFAULT 1,
  CONSTRAINT pk_serviciocabecera PRIMARY KEY (id),
  CONSTRAINT fk_cliente1 FOREIGN KEY (idcliente1)
      REFERENCES negocio."Persona" (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_cliente2 FOREIGN KEY (idcliente2)
      REFERENCES negocio."Persona" (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE negocio."ServicioCabecera"
  OWNER TO postgres;

  
  -- Table: negocio."ServicioDetalle"

-- DROP TABLE negocio."ServicioDetalle";

CREATE TABLE negocio."ServicioDetalle"
(
  id bigint NOT NULL,
  idtiposervicio integer NOT NULL,
  descripcionservicio character varying(150) NOT NULL,
  idservicio integer NOT NULL,
  iddestino integer,
  descripciondestino character varying(15),
  dias integer,
  noches integer,
  fechaida date,
  fecharegreso date,
  cantidad integer,
  preciobase numeric NOT NULL,
  porcencomision numeric,
  montocomision numeric,
  montototal numeric,
  usuariocreacion character varying(20) NOT NULL,
  fechacreacion timestamp with time zone NOT NULL,
  ipcreacion character(15) NOT NULL,
  usuariomodificacion character varying(15) NOT NULL,
  fechamodificacion timestamp with time zone NOT NULL,
  ipmodificacion character(15) NOT NULL,
  idestadoregistro integer NOT NULL DEFAULT 1,
  CONSTRAINT pk_serviciodetalle PRIMARY KEY (id),
  CONSTRAINT fk_serviciocabecera FOREIGN KEY (idservicio)
      REFERENCES negocio."ServicioCabecera" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE negocio."ServicioDetalle"
  OWNER TO postgres;

