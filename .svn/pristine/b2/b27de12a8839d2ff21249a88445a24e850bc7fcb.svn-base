/*
    Document   : loader
    Created on : 25-nov-2009, 11:42:06
    Author     : jorge.bodas
    Description:
        Purpose of the stylesheet follows.
*/

//google.load("maps", "",{"other_params":"sensor=true"});
//google.load("search", "1");

var bug;
var visor;
window.addEvents({
   'load':function(){
       bug=new Notificacion(debug);
       createUser();
       runVisor();
   },
   'resize':function(){

   }
});


function runVisor()
{
    $(document.body).setStyles({
        'height':'101%',
        'width':'101%'
    });
    $(document.body).setStyles({
        'height':'100%',
        'width':'100%'
    });
    bug.log("Comienzo del visor");
    //squeeze.open('splashscreen.html',{handler: 'iframe'});
    //new SplashScreen(debug);
    visor=new Visor(numVisor,'map',jsIO.leerXML('XML/perfiles.xml'));
    visor.loadArbolCapas();
    //var win=new JobWindow('temp',false);
        //win.loadIframe('http://www.google.es');
}

function createUser()
{
    if(!Cookie.read('ID_USER'))
    {
        Cookie.write('ID_USER', $random(9999999, 9999999*1000),{duration:365});
    }
}