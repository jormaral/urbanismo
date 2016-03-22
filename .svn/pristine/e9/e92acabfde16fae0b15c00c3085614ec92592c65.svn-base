
function ajustarPaneles_CerrarLogin()
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

    //Modifico el color de fondo del panel central
    $(idPanelCentral).morph({
        'background-color':'#eeeeee'
    });
    
    //Vuelvo al panel superior a la anchura requerida (60%)
    $('pnlSuperior').morph({
        'height':((altura*60)/100).toInt()
    });
    
  //Vuelvo al panel superior a la anchura requerida (40%)
    $('botonera').fade(1);
    $('pnlInferior').morph({
        'height':((altura*40)/100).toInt()
    });
    
    //Vuelvo a colocar el logo donde corresponde
    $('logo').morph({
        'top':0,
        'right':0
    });
    
    loginPanelsState = "CLOSED";
}

function ajustarPaneles_AbrirLogin()
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
    
    var duracion=200; //duracion de la transicion
    
    //Defino el tiempo y el tipo de transicion
    $(idPanelCentral).set('morph',{
        duration:duracion,
        transition:Fx.Transitions.Elastic.easeInOut
    });
    $('pnlSuperior').set('morph',{
        duration:duracion,
        transition:Fx.Transitions.Elastic.easeInOut
    });
    $('pnlInferior').set('morph',{
        duration:duracion,
        transition:Fx.Transitions.Elastic.easeInOut
    });

    //Modifico el color de fondo del panel central
    $(idPanelCentral).morph({
        'background-color':'#dddddd'
    });
    
    //Modifico el ancho del panel superior (originalmente 126)
    $('pnlSuperior').morph({
        'height':85
    });
    
    //Modifico el ancho del panel inferior (originalmente 65)
    $('botonera').setOpacity(0);
    $('pnlInferior').morph({
        'height':0
    });
    
    //Coloco el logo arriba a la izquierda
    $('logo').morph({
        'top':0,
        'right':0
    });
    
    //En duracion*3 ajusto el panel central
    setTimeout("ajustarPnlCentral()",duracion*3);
    
    loginPanelsState = "OPENED";
}

function ajustarPnlCentral()
{	
	var altura=$(document.body).getSize().y;

    $(idPanelCentral).morph({
        'height':altura-$('pnlSuperior').getHeight()-$('pnlInferior').getHeight(),
        'top':$('pnlSuperior').getHeight()
    });
}

function changeLogoToLoginPosition()
{
    $('logo').setOpacity(0);
    //$('logo').setStyle('background-image','url(styles/images/logo.png)');
    $('logo').setStyle('background-position','left center');
    $('logo').setStyle('height',60);
    $('logo').setStyle('width','100%');
    $('logo').fade(1);
    //$('logo').setOpacity(1);
}

function changeLogoToConsolePosition()
{
    $('logo').setOpacity(0);
    //$('logo').setStyle('background-image','url(styles/images/logo1024.png)');
    $('logo').setStyle('background-position','right center');
    $('logo').setStyle('height',60);
    $('logo').fade(1);
    $('logo').setStyle('width',569);
    //$('logo').setOpacity(1);
}

function checkPositionBotones()
{
    $('botoneraSuperior').setOpacity(0);
    setTimeout(function(){
        var topBotonera=$('pnlSuperior').getHeight().toInt()-$('cabTituloSeccion').getHeight().toInt()-$('botoneraSuperior').getHeight().toInt()-3;
        $('botoneraSuperior').setStyle('top',topBotonera);
        $('botoneraSuperior').fade(1);
    },300);
}
