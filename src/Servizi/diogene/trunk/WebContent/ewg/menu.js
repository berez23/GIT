var contractall_default= true; //Should all submenus be contracted by default? (true or false)

var menu, titles, submenus, arrows,nr,autocloseother;
autocloseother= false;
nr=30;




/////DD added expandall() and contractall() functions/////

function slash_expandall(){
if (typeof menu!="undefined"){
	for(i=0; i<Math.max(titles.length, submenus.length); i++){
		titles[i].className="title";
		arrows[i].src = "expanded.gif";
		submenus[i].style.display="";
		submenus[i].style.height = submenus[i].scrollHeight+"px";
	}
}
}

function slash_contractall(){
if (typeof menu!="undefined"){
	for(i=0; i<Math.max(titles.length, submenus.length); i++){
		titles[i].className="titlehidden";
		arrows[i].src = "collapsed.gif";
		submenus[i].style.display="none";
		submenus[i].style.height = 0;
	}
}
}


/////End DD added functions///////////////////////////////


function init(){
    menu = getElementsByClassName("sdmenu", "div", document)[0];
    if(!menu)
    	return;
    titles = getElementsByClassName("title", "span", menu);
    submenus = getElementsByClassName("submenu", "div", menu);
    arrows = getElementsByClassName("arrow", "img", menu);
    for(i=0; i<Math.max(titles.length, submenus.length); i++) {
        titles[i].onclick = gomenu;
        arrows[i].onclick = gomenu;
        submenus[i].style.height = submenus[i].scrollHeight+"px";
    }
 if (contractall_default) //DD added code
				slash_contractall() //DD added code
}



function gomenu(e) {
    if (!e)
        var e = window.event;
    var ce = (e.target) ? e.target : e.srcElement;
    var sm;
    for(var i in titles) {
        if(titles[i] == ce || arrows[i] == ce)
            sm = i;
    }
    if(parseInt(submenus[sm].style.height) > 2) {
        hidemenu(sm);
    } else if(parseInt(submenus[sm].style.height) < 2) {
        titles[sm].className = "title";
        showmenuWrapper(sm);
    }
}

function hidemenu(sm) {
    var nrLocale =nr;
    if(parseInt(submenus[sm].style.height) < nrLocale)
    	nrLocale = parseInt(submenus[sm].style.height);
    submenus[sm].style.height = (parseInt(submenus[sm].style.height)-nrLocale)+"px";
    var to = setTimeout("hidemenu("+sm+")", 30);
    if(parseInt(submenus[sm].style.height) <= 0) {
        clearTimeout(to);
        submenus[sm].style.display = "none";
        submenus[sm].style.height = "0px";
        arrows[sm].src = "collapsed.gif";
        titles[sm].className = "titlehidden";
    }
    

    
}
function showmenuWrapper(sm)
{
	if(autocloseother)   
	for(i=0; i<Math.max(titles.length, submenus.length); i++) 
	{
		if(i != sm)
			hidemenu(i);
	}
	showmenu(sm);
}

function showmenu(sm) {	    
    submenus[sm].style.display = "";
    submenus[sm].style.height = (parseInt(submenus[sm].style.height)+nr)+"px";
    var to = setTimeout("showmenu("+sm+")", 30);
    if(parseInt(submenus[sm].style.height) >= parseInt(submenus[sm].scrollHeight)) {
        clearTimeout(to);
        submenus[sm].style.height = submenus[sm].scrollHeight+"px";
        arrows[sm].src = "expanded.gif";
    }
  
}



function getElementsByClassName(strClassName, strTagName, oElm){
    var arrElements = (strTagName == "*" && document.all)? document.all : oElm.getElementsByTagName(strTagName);
    var arrReturnElements = new Array();
    strClassName = strClassName.replace(/\-/g, "\\-");
    var oRegExp = new RegExp("(^|\\s)" + strClassName + "(\\s|$)");
    var oElement;
    for(var i=0; i<arrElements.length; i++){
        oElement = arrElements[i];      
        if(oRegExp.test(oElement.className)){
            arrReturnElements.push(oElement);
        }   
    }
    return (arrReturnElements)
}





window.onload = init;

