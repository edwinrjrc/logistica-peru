-- Function: negocio.fn_consultarservicioventa(integer, character varying, character varying)

CREATE OR REPLACE FUNCTION negocio.fn_consultarservicioventa(p_tipodocumento integer, p_numerodocumento character varying, p_nombres character varying)
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
       sercab.nrocuotas, sercab.tea, sercab.valorcuota, sercab.fechaprimercuota, sercab.fechaultcuota,
       sercab.usuariocreacion, sercab.fechacreacion, sercab.ipcreacion, sercab.usuariomodificacion, sercab.fechamodificacion, sercab.ipmodificacion
  from negocio."ServicioCabecera" sercab,
       negocio.vw_clientesnova cli1,
       negocio.vw_clientesnova cli2,
       soporte.destino des,
       soporte."Tablamaestra" maemp,
       soporte."Tablamaestra" maeep
 where sercab.idestadoregistro = 1
   and sercab.idcliente1       = cli1.id
   and sercab.idcliente2       = cli2.id
   and sercab.iddestino        = des.id
   and maemp.estado            = 'A'
   and maemp.idmaestro         = 13
   and maemp.id                = sercab.idformapago
   and maeep.estado            = 'A'
   and maeep.idmaestro         = 14
   and maeep.id                = sercab.idestadopago
   and cli1.idtipodocumento    = COALESCE(p_tipodocumento,cli1.idtipodocumento)
   and cli1.numerodocumento    = COALESCE(p_numerodocumento,cli1.numerodocumento)
   and CONCAT(replace(cli1.nombres,' ',''),trim(cli1.apellidopaterno),trim(cli1.apellidomaterno)) like '%'||COALESCE(p_nombres,CONCAT(replace(cli1.nombres,' ',''),trim(cli1.apellidopaterno),trim(cli1.apellidomaterno)))||'%';

return micursor;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
