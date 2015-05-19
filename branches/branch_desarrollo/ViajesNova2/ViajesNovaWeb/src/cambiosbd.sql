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
       sercab.fechacompra, sercab.montototal, sercab.montocomisiontotal, sercab.montototaligv, sercab.montototalfee,
       sercab.idformapago, maemp.nombre as nommediopago, maemp.descripcion as descmediopago,
       sercab.idestadopago, maeep.nombre as nomestpago, maeep.descripcion as descestpago,
       sercab.nrocuotas, sercab.tea, sercab.valorcuota, sercab.fechaprimercuota, sercab.fechaultcuota, sercab.montocomisiontotal,
       sercab.idestadoservicio, 
       (select count(1) from negocio."PagosServicio" where idservicio=sercab.id) tienepagos,
       usu.id as idusuario,
       usu.nombres as nombresvendedor, usu.apepaterno, usu.apematerno,
       sercab.usuariocreacion, sercab.fechacreacion, sercab.ipcreacion, 
       sercab.usuariomodificacion, sercab.fechamodificacion, sercab.ipmodificacion, sercab.generocomprobantes, sercab.guardorelacioncomprobantes, sercab.observaciones
  from negocio."ServicioCabecera" sercab 
 inner join negocio.vw_clientesnova cli1 on sercab.idcliente1 = cli1.id
 inner join soporte."Tablamaestra" maemp on maemp.estado = 'A' and maemp.idmaestro = 13 and maemp.id = sercab.idformapago
 inner join soporte."Tablamaestra" maeep on maeep.estado = 'A' and maeep.idmaestro = 14 and maeep.id = sercab.idestadopago
 inner join seguridad.usuario usu on usu.id = sercab.idvendedor
  left join negocio.vw_clientesnova cli2 on sercab.idcliente2 = cli2.id
 where sercab.idestadoregistro = 1
   and (select count(1) from negocio."ServicioDetalle" det where det.idservicio = sercab.id) > 0
   and sercab.id               = p_idservicio;

return micursor;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

