function clicBoton(idBoton){
	document.getElementById(idBoton).click();
}

function mostrarModal(idform){
	var mostrarModal = document.getElementById('idFormHidden:idShowModal').value;
	var tipoModal = document.getElementById('idFormHidden:idTipoModal').value;
	
	alert('MOSTRAR ::'+mostrarModal);
	alert('tipo ::'+tipoModal);
	
	if (tipoModal == "1" && eval(mostrarModal)){
		document.getElementById(idform+':idBotonModalCorrecto').click();
	}
	else if (tipoModal == "2" && eval(mostrarModal)){
		document.getElementById(idform+':idBotonModalError').click();
	}
}

function cerrarModalBoton(idform,idBoton){
	try{
		var direccionAgregada = document.getElementById(idform+':idHdDirAgr').value;
		if (eval(direccionAgregada)){
			document.getElementById(idform+':'+idBoton).click();
		}
	}
	catch (e){
		alert("error ::"+e.message);
	}
}

function cerrarModalBoton(idform,idBoton,idHid){
	try{
		var direccionAgregada = document.getElementById(idform+':'+idHid).value;
		if (eval(direccionAgregada)){
			document.getElementById(idform+':'+idBoton).click();
		}
	}
	catch (e){
		alert("error ::"+e.message);
	}
}