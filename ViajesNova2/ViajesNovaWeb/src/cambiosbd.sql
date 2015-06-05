-- Function: negocio.fn_consultarcomprobantesserviciodetalle(integer)

-- DROP FUNCTION negocio.fn_consultarcomprobantesserviciodetalle(integer);

CREATE OR REPLACE FUNCTION negocio.fn_consultarcompserviciodethijo(p_idservicio integer, p_iddetserv integer)
  RETURNS refcursor AS
$BODY$
declare micursor refcursor;

begin
open micursor for
SELECT serdet.id as idSerdetalle, serdet.idtiposervicio, 
       tipser.id, tipser.nombre as nomtipservicio, tipser.desccorta as descservicio, tipser.requierefee, 
       tipser.pagaimpto, tipser.cargacomision, tipser.esimpuesto, tipser.esfee,
       serdet.descripcionservicio, serdet.fechaida, serdet.fecharegreso, serdet.cantidad, 
       serdet.preciobase, serdet.porcencomision, serdet.montocomision, serdet.montototal, serdet.idempresaproveedor, pro.nombres, pro.apellidopaterno, 
       pro.apellidomaterno, tipser.visible,
       (select cg.tienedetraccion
          from negocio."DetalleComprobanteGenerado" dc,
               negocio."ComprobanteGenerado" cg
         where dc.idserviciodetalle = serdet.id
           and dc.idcomprobante = cg.id) as tieneDetraccion,
       (select cg.tieneretencion
          from negocio."DetalleComprobanteGenerado" dc,
               negocio."ComprobanteGenerado" cg
         where dc.idserviciodetalle = serdet.id
           and dc.idcomprobante = cg.id) as tieneRetencion,
       (select cg.id
          from negocio."DetalleComprobanteGenerado" dc,
               negocio."ComprobanteGenerado" cg
         where dc.idserviciodetalle = serdet.id
           and dc.idcomprobante = cg.id) as idComprobante,
       (select cg.idtipocomprobante
          from negocio."DetalleComprobanteGenerado" dc,
               negocio."ComprobanteGenerado" cg
         where dc.idserviciodetalle = serdet.id
           and dc.idcomprobante = cg.id) as tipoComprobante,
       (select tm.nombre
          from negocio."DetalleComprobanteGenerado" dc,
               negocio."ComprobanteGenerado" cg,
               soporte."Tablamaestra" tm
         where dc.idserviciodetalle = serdet.id
           and dc.idcomprobante     = cg.id
           and tm.id                = cg.idtipocomprobante
           and tm.idmaestro         = 17) as tipoComprobanteNombre,
       (select cg.numerocomprobante
          from negocio."DetalleComprobanteGenerado" dc,
               negocio."ComprobanteGenerado" cg
         where dc.idserviciodetalle = serdet.id
           and dc.idcomprobante = cg.id) as numeroComprobante
       
  FROM negocio."ServicioDetalle" serdet
 INNER JOIN negocio."MaestroServicios" tipser ON tipser.idestadoregistro = 1 AND tipser.id = serdet.idtiposervicio
  LEFT JOIN negocio.vw_proveedoresnova pro ON pro.id = serdet.idempresaproveedor
 WHERE serdet.idestadoregistro = 1
   AND serdet.idservicio       = p_idservicio
   AND serdet.idservdetdepende = p_iddetserv;

return micursor;

end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
  
  
  
SELECT serdet.id as idSerdetalle, serdet.idtiposervicio, 
       tipser.id, tipser.nombre as nomtipservicio, tipser.desccorta as descservicio, tipser.requierefee, 
       tipser.pagaimpto, tipser.cargacomision, tipser.esimpuesto, tipser.esfee,
       serdet.descripcionservicio, serdet.fechaida, serdet.fecharegreso, serdet.cantidad, 
       serdet.preciobase, serdet.porcencomision, serdet.montocomision, serdet.montototal, serdet.idempresaproveedor, pro.nombres, pro.apellidopaterno, 
       pro.apellidomaterno, tipser.visible,
       (select cg.tienedetraccion
          from negocio."DetalleComprobanteGenerado" dc,
               negocio."ComprobanteGenerado" cg
         where dc.idserviciodetalle = serdet.id
           and dc.idcomprobante = cg.id) as tieneDetraccion,
       (select cg.tieneretencion
          from negocio."DetalleComprobanteGenerado" dc,
               negocio."ComprobanteGenerado" cg
         where dc.idserviciodetalle = serdet.id
           and dc.idcomprobante = cg.id) as tieneRetencion,
       (select cg.id
          from negocio."DetalleComprobanteGenerado" dc,
               negocio."ComprobanteGenerado" cg
         where dc.idserviciodetalle = serdet.id
           and dc.idcomprobante = cg.id) as idComprobante,
       (select cg.idtipocomprobante
          from negocio."DetalleComprobanteGenerado" dc,
               negocio."ComprobanteGenerado" cg
         where dc.idserviciodetalle = serdet.id
           and dc.idcomprobante = cg.id) as tipoComprobante,
       (select tm.nombre
          from negocio."DetalleComprobanteGenerado" dc,
               negocio."ComprobanteGenerado" cg,
               soporte."Tablamaestra" tm
         where dc.idserviciodetalle = serdet.id
           and dc.idcomprobante     = cg.id
           and tm.id                = cg.idtipocomprobante
           and tm.idmaestro         = 17) as tipoComprobanteNombre,
       (select cg.numerocomprobante
          from negocio."DetalleComprobanteGenerado" dc,
               negocio."ComprobanteGenerado" cg
         where dc.idserviciodetalle = serdet.id
           and dc.idcomprobante = cg.id) as numeroComprobante
       
  FROM negocio."ServicioDetalle" serdet
 INNER JOIN negocio."MaestroServicios" tipser ON tipser.idestadoregistro = 1 AND tipser.id = serdet.idtiposervicio
  LEFT JOIN negocio.vw_proveedoresnova pro ON pro.id = serdet.idempresaproveedor
 WHERE serdet.idestadoregistro = 1
   AND serdet.idservicio       = 163
   AND serdet.idservdetdepende = p_iddetserv;
   
select * from negocio."ServicioDetalle" where idservicio = 163 and idservdetdepende = 285;