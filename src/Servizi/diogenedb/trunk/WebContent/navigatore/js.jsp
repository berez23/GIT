<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!--
function visualizzaSoggettoInMappa(s1,s2)
{
	alert("Qui apre mappa con "+s1+" "+s2);
} 


function zoomInMappaParticelle(codente,foglio,particella){
	var wnd ;
	wnd = parent.window.opener;
	var x,y,w,h;
	try
	{
		if (wnd == null)
		{
			popup = window.open("../viewer/applet/index.jsp?cn="+codente+"&fgl="+foglio+"&par="+particella,"map","width="+(screen.availWidth-10)+",height="+(screen.availHeight-27)+",scrollbars=no,resizable=no,left=0,top=0");								
			popup.focus();						
		}
		else{
			var wnd2 = wnd.parent;
			if(wnd2.zoomAppletParticelle)
				wnd2.zoomAppletParticelle(codente,particella,foglio);
			else
				wnd2.parent.zoomAppletParticelle(codente,particella,foglio);
		}
	}
	catch(e)
	{
		alert("Errore in zoomInMappaParticelle: "+e.message);
	}
		

}

/* menuDropdown.js - implements a dropdown menu based on a HTML list
 * Author: Dave Lindquist (http://www.gazingus.org)
 * Modified by: Nicolas Lesbats (nicolas lesbats at laposte net)
 * Version: 0.1b (2004-03-11)
 */

var maxWidth = 50;
/* maximum width of the submenus (in 'em' units) */

var borderBox  = false;
var horizontal = new Array();
var menuTop    = new Array();
var menuHeight = new Array();
var menuLeft   = new Array();
var menuWidth  = new Array();

window.onload = function() { loadMenu(); }


function toggleSelect(x,y,w,h,tip){
    var appVer = navigator.appVersion.toLowerCase();
    var iePos = appVer.indexOf('msie');
    if (iePos !=-1) 
    {
        var is_minor = parseFloat(appVer.substring(iePos+5,appVer.indexOf(';',iePos)));
        var is_major = parseInt(is_minor);
    }
    if (navigator.appName.substring(0,9) == "Microsoft")
    { // Check if IE version is 6 or older
        if (is_major <= 6) 
        {
            var selx,sely,selw,selh,i
            var sel=document.getElementsByTagName("SELECT")
            for(i=0;i<sel.length;i++)
            {
                selx=0; sely=0; var selp;
                if(sel[i].offsetParent)
                {
                    selp=sel[i];
                    while(selp.offsetParent)
                    {
                        selp=selp.offsetParent;
                        selx+=selp.offsetLeft;
                        sely+=selp.offsetTop;
                    }
                }
                selx+=sel[i].offsetLeft;
                sely+=sel[i].offsetTop;
                selw=sel[i].offsetWidth;
                selh=sel[i].offsetHeight;
                if(selx+selw>x && selx<x+w && sely+selh>y && sely<y+h)
                    if(sel[i].style.visibility!="hidden" && tip.style.visibility!="hidden") sel[i].style.visibility="hidden";
                else sel[i].style.visibility="visible";
            }
        }
    }
} 

function loadMenu() {
  if (!document.getElementById) return;
  var i = 0, j, root, submenus, node, li, link, division;
  while (true) {
    root = document.getElementById("menuList" + (i + 1));
    if (root == null)
      break;
    submenus = root.getElementsByTagName("ul");
    division = root.parentNode;

    if (document.createElement) {
      /* Win/IE5-6 trick: makes the whole width of the submenus clickable
       */
      for (j = 0; j < submenus.length; j++) {
        node = submenus.item(j);
        if (node.className == "menu" && node.getElementsByTagName("ul").length == 0) {
          li = document.createElement("li");
          node.appendChild(li);
          li.style.position = "absolute";
          li.style.visibility = "hidden";
        }
      }
      /* checks whether the 'width' property applies to the border box or
       * the content box of an element
       */
      if (i == 0) {
        li.style.display = "block";
        li.style.padding = "0";
        li.style.width   = "2px";
        li.style.border  = "1px solid";
        if (li.offsetWidth == 2)
          borderBox = true;
      }
    }

    initializeMenu(root, division, i);

    for (j = 0; j < submenus.length; j++) {
      node = submenus.item(j);
      if (node.className == "menu") {
        link = node.previousSibling;
        while (link != null) {
          if (link.className == "actuator") {
            initializeSubmenu(node, link, root, division);
            node.set();
            break;
          }
          link = link.previousSibling;
        }
      }
    }
    i++;
  }
}

function initializeMenu(root, div, index) {

  horizontal[index] = menuIsHorizontal(root);
  menuTop[index]    = div.offsetTop;
  menuHeight[index] = div.offsetHeight;
  menuLeft[index]   = div.offsetLeft;
  menuWidth[index]  = div.offsetWidth;

  div.horizontal = function() {
    return horizontal[index];
  }

  div.checkMove = function() {
    if (this.hasMoved()) this.resetMenu();
  }

  div.hasMoved = function() {
    if (menuTop[index]    == this.offsetTop    &&
        menuHeight[index] == this.offsetHeight &&
        menuLeft[index]   == this.offsetLeft   &&
        menuWidth[index]  == this.offsetWidth)
      return false;
    return true;
  }

  div.resetMenu = function() {
    horizontal[index] = menuIsHorizontal(root);
    menuTop[index]    = this.offsetTop;
    menuHeight[index] = this.offsetHeight;
    menuLeft[index]   = this.offsetLeft;
    menuWidth[index]  = this.offsetWidth;

    var submenus = root.getElementsByTagName("ul");
    for (var j = 0; j < submenus.length; j++) {
      var node = submenus.item(j);
      if (node.className == "menu") {
        node.style.right = "";
        node.style.left  = "";
        if (!window.opera)
          node.style.width = "";
        node.set();
      }
    }
  }
}

function menuIsHorizontal(root) {
  var first = firstElement(root, "LI");
  if (first != null) {
    var second = first.nextSibling;
    while (second != null) {
      if (second.tagName == "LI") {
        first  = firstElement(first,  "A");
        second = firstElement(second, "A");
        if (first != null && second != null)
          if (first.offsetLeft == second.offsetLeft)
            return false;
        return true;
      }
      second = second.nextSibling;
    }
  }
  return true;
}

function initializeSubmenu(menu, actuator, root, div) {

  var parent = menu.parentNode;

  parent.onmouseover = function() {
    div.checkMove();
    menu.style.visibility = "visible";
    //toggleSelect(menu.offsetLeft,menu.offsetTop,menu.offsetWidth,menu.offsetHeight,menu);
  }

  actuator.onfocus = function() {
    div.checkMove();
    menu.style.visibility = "visible";
    //toggleSelect(menu.offsetLeft,menu.offsetTop,menu.offsetWidth,menu.offsetHeight,menu);
  }

  parent.onmouseout = function() {
    menu.style.visibility = "";
    //toggleSelect(menu.offsetLeft,menu.offsetTop,menu.offsetWidth,menu.offsetHeight,menu);
  }

  var tags = menu.getElementsByTagName("a");
  var link = tags.item(tags.length - 1);
  if (!link.onblur)
    link.onblur = function() {
      var node = link.parentNode.parentNode;
      while (node != menu) {
        node.style.visibility = "";
        node = node.parentNode.parentNode;
      }
      menu.style.visibility = "";
    }

  if (parent.parentNode == root) {
    menu.set = function() {
      setLocation1(this, actuator, root, div);
    }
  } else {
    menu.set = function() {
      setLocation2(this, actuator, div);
    }
  }
}

function setLocation1(menu, actuator, root, div) {
  var first = firstElement(menu, "LI");
  if (first != null)
    if (first.offsetParent == menu)
      setWidth(menu);
  if (div.horizontal()) {
    if (actuator.offsetParent == menu.offsetParent) {
      menu.style.left = actuator.offsetLeft + "px";
      menu.style.top  = actuator.offsetTop  + actuator.offsetHeight + "px";
    } else {
      /* happens in Win/IE5-6 when some ancestors are 'static' and have their
       * 'width' or 'height' different than 'auto' */
      var parent = actuator.offsetParent;
      var top  = 0;
      var left = 0;
      while (parent != menu.offsetParent && parent != null) {
        top  = top  + parent.offsetTop;
        left = left + parent.offsetLeft;
        parent = parent.offsetParent;
      }
      menu.style.left = left + actuator.offsetLeft + "px";
      menu.style.top  = top  + actuator.offsetTop  + actuator.offsetHeight + "px";
    }
  } else {
    menu.style.top = actuator.offsetTop + "px";
    menu.style.left = (div.offsetWidth + actuator.offsetWidth) / 2 + "px";
  }
}

function setLocation2(menu, actuator, div) {
  if (menu.offsetParent != document.body)
    setWidth(menu);
  menu.style.top = actuator.offsetTop + "px";
  menu.style.left = actuator.offsetWidth + "px";
}

function setWidth(menu) {
  menu.style.right = - maxWidth + "em";
  var width  = 0;
  var height = 0;
  var items = menu.getElementsByTagName("a");
  for (var i = 0; i < items.length; i++) {
    var link = items.item(i);
    if (link.parentNode.parentNode == menu) {
      height = height + link.offsetHeight;
      if (link.offsetWidth > width)
        width = link.offsetWidth;
    }
  }
  if (borderBox)
    width = width + (menu.offsetHeight - height);
  menu.style.width = width + "px";
}

function firstElement(node, name) {
  var first = node.firstChild;
  while (first != null) {
    if (first.tagName == name)
      return first;
    first = first.nextSibling;
  }
  return null;
}
-->
