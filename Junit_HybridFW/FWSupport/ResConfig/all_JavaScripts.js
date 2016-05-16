
var mouseX;
var mouseY;
var layer;
var show = false;
ie4 = (document.all)? true:false

function showLayer(name,theImage) {
	layer = document.getElementById(name);
	layer.innerHTML = '<img src=' +theImage+'>';
	layer.style.visibility = "visible";
	layer.style.top = mouseY + "px";
	layer.style.left = mouseX + "px";
	positionLayer()
	show = true;
}


function hideLayer(name) {
	layer = document.getElementById(name);
	layer.style.visibility = "hidden";
	show = false;
}



function positionLayer() {
	layer.style.top = mouseY + "px";
	layer.style.left = mouseX + "px";
}

//Get Mouse X and Y
function init() {
	document.onmousemove=mousemove;
}

function mousemove(e) {
	if (ie4) { mouseX=event.x; mouseY=event.y}
	else  { mouseX=e.pageX; mouseY=e.pageY}	
	if (show) { positionLayer() }
}

function openWin(img){
	
	window.open(img,"FWImgVeiwer","menubar=0,resizable=1,width=600,height=800")
}