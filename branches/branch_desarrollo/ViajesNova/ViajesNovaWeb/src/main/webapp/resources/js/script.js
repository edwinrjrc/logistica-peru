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

function dataTableSelectOneRadio(radio) {
	var id = radio.name.substring(radio.name.lastIndexOf(':'));
	var el = radio.form.elements;
	for ( var i = 0; i < el.length; i++) {
		if (el[i].name.substring(el[i].name.lastIndexOf(':')) == id) {
			el[i].checked = false;
		}
	}
	radio.checked = true;
}