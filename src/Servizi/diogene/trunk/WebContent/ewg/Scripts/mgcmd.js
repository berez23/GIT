var refreshTimeout;
var pointType;
var objCounter = 0;
function GetMap()
{
	return MGMap;
}

function ViewBuffer()
{
	if (GetMap().getSelection().getMapObjectsEx(null).size() == 0)
		alert('You must select an object before using this function');
	else
		var result = GetMap().viewBufferDlg();
}

function SelectWithIn() 
{
	GetMap().getSelectionMode()
	GetMap().setSelectionMode("Intersection")
	GetMap().selectWithinDlg ()
}

function ClearSelection(){
	GetMap().getSelection().clear();
	GetMap().removeMapLayer("Buffer");
}

function Print()
{
	GetMap().pageSetupDlg()
	GetMap().printDlg()
}

function modInsert(blnOk)
{
	SelBk.style.display = "";
	InsBk.style.display = "none";
	
	if(blnOk)
	{
		AddLocation(txtBookmark.value,lstBookmarks)	
	}
	
	txtBookmark.value = "";
}

function modAdd(blnAdd)
{
	if(blnAdd)
	{
		SelBk.style.display = "none";
		InsBk.style.display = "";
		txtBookmark.focus();
	}else{
		RemoveLocation(lstBookmarks);
	}
}

function AddLocation(locationName, oSelect)
{
	if(locationName && locationName != "")
	{
		var oOption = document.createElement("OPTION");
		oSelect.options.add(oOption);
		oOption.innerText = locationName;
		oOption.value = GetMap().getLat() + "|" + GetMap().getLon() + "|" + GetMap().getScale();
	}
}

function RemoveLocation(oSelect)
{
	oSelect.options.remove(oSelect.selectedIndex);
}

function GoToSelection(oSelect)
{
	if (oSelect.options.length == 0) return false;
	var zoomArgs = oSelect.options[oSelect.selectedIndex].value;
	zoomArgs = zoomArgs.split("|");
	GetMap().zoomScale(parseFloat(zoomArgs[0]),parseFloat(zoomArgs[1]), parseInt(zoomArgs[2]));
}

function FillLayerList(oSelect)
{
	var allLayers = GetMap().getMapLayers();
	for(i=0;i<allLayers.Count;i++)
	{
		if(allLayers.item(i).getName()!="")
		{
			var oOption = document.createElement("OPTION");
			oSelect.options.add(oOption);
			oOption.value = allLayers.item(i).getName();
			oOption.innerText =  oOption.value;
		}
	}
}

function SetTrackingOn()
{
	btnTrackOn.className="buttonDown";
	btnTrackOff.className="button";
	StartTracking();
}
function StartTracking()
{
	var interval = parseFloat(txtInterval.value);
	if(!isNaN(interval))
	{
		interval*= 1000;
		RefreshSelectedLayers(lstAllLayers);
		refreshTimeout = window.setTimeout("StartTracking();",interval);
	}
	
}

function StopTracking()
{
	btnTrackOn.className="button";
	btnTrackOff.className="buttonDown";
	window.clearTimeout(refreshTimeout);
}

function RefreshSelectedLayers(oSelect)
{
	for(i=0;i<oSelect.options.length;i++)
	{
		if(!oSelect.options[i].selected)
		{
			var mapLayer = GetMap().getMapLayer(oSelect.options[i].value);
			mapLayer.setRebuild(false);
		}
		else
		{
			var mapLayer = GetMap().getMapLayer(oSelect.options[i].value);
			mapLayer.setRebuild(true);
		}
	}
		GetMap().refresh();
}

function CreateRedline(oSelect)
{
	var rlLayer = GetMap().getMapLayer('Redline');
	if(!rlLayer)
	{
		rlLayer = GetMap().createLayer('redline','Redline');
		rlLayer.setShowInLegend(false);
	}
	
	switch (oSelect.selectedIndex)
	{
	case 0: GetMap().digitizePolygon();
			break;
	case 1: GetMap().digitizeCircle("FT");
			break;
	case 2:	GetMap().digitizePolyline();
			break;
	case 3: pointType = 0 ; GetMap().digitizePoint();
			break;
	case 4: pointType = 1 ; GetMap().digitizePoint();
			break;
	}
}

function DeleteRedline()
{
	var rlLayer = GetMap().getMapLayer('Redline');
	if(rlLayer)
	{
		var selobjs = GetMap().getSelection().getMapObjectsEx(rlLayer);
		if (selobjs) rlLayer.removeObjects(selobjs);
	}
}

function ClearRedline()
{
	GetMap().removeMapLayer('Redline');
}

function SaveMap(mapPath)
{
	if(!GetMap().saveMWF(mapPath,""))
		alert('Unable to save MWF File.');
	else
		alert('MWF File saved to \"' + mapPath + '\".'); 
}

function ShowStyleDlg()
{
	
	window.showModalDialog("Forms/redlining.htm",GetMap().getRedlineSetup(),
							"dialogHeight: 308px; dialogWidth: 405px ; center: yes; scroll: no; help: no; status:no; resizable: no;");
}

// BASIC COMMANDS //

