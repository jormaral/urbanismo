
function ajustarPnlCentral()
{	
	var altura;
	//Obtengo la altura actual de la ventana
    if(Browser.Engine.trident)
    {
        altura=window.availHeight;
    }
    else
    {
        altura=$(document.body).getSize().y;
    }
	
	$("pnlCentral").set('height',altura-$('pnlSuperior').getHeight()-$('pnlInferior').getHeight());
    $("pnlCentral").set('top',$('pnlSuperior').getHeight());

    /*
    $(idPanelCentral).morph({
        'height':altura-$('pnlSuperior').getHeight()-$('pnlInferior').getHeight(),
        'top':$('pnlSuperior').getHeight()
    });*/       
}

function changeLogoToLoginPosition()
{
    $('logo').setOpacity(0);
    $('logo').setStyle('background-position','center center');
    $('logo').setOpacity(1);
}

function changeLogoToConsolePosition()
{
    $('logo').setOpacity(0);
    $('logo').setStyle('background-position','right center');
    $('logo').setOpacity(1);
}
