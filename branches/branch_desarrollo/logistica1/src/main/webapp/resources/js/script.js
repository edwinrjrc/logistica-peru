function clicBoton(idBoton){
	document.getElementById(idBoton).click();
}

function mostrarModal(){
	alert('hola');
	var mostrarModal = document.getElementById('idformadmparametro:idShowModal').value;
	var tipoModal = document.getElementById('idformadmparametro:idTipoModal').value;
	alert("mostrar::"+mostrarModal);
	alert("tipo::"+tipoModal);
	
	if (tipoModal == "1" && eval(mostrarModal)){
		alert("entro 1::");
		document.getElementById('idFormParametro:idBotonModalCorrecto').click();
	}
	else if (tipoModal == "2" && eval(mostrarModal)){
		document.getElementById('idFormParametro:idBotonModalError').click();
	}
}