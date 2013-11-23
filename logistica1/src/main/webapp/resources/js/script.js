function clicBoton(idBoton){
	document.getElementById(idBoton).click();
}

function mostrarModal(idform){
	var mostrarModal = document.getElementById('idFormHidden:idShowModal').value;
	var tipoModal = document.getElementById('idFormHidden:idTipoModal').value;
	
	if (tipoModal == "1" && eval(mostrarModal)){
		document.getElementById(idform+':idBotonModalCorrecto').click();
	}
	else if (tipoModal == "2" && eval(mostrarModal)){
		document.getElementById(idform+':idBotonModalError').click();
	}
}