
function js_soloEntero(event){
	var code = event.keyCode;
	
	var valida = (parseInt(code) >= 48 && parseInt(code) <= 57);
	
	return valida;
}