/*
* Copyright 2011 red.es
* Autores: Arnaiz Consultores.
*
* Licencia con arreglo a la EUPL, Versi�n 1.1 o -en cuanto
* sean aprobadas por la Comision Europea- versiones
* posteriores de la EUPL (la <<Licencia>>);
* Solo podra usarse esta obra si se respeta la Licencia.
* Puede obtenerse una copia de la Licencia en:
*
* http://ec.europa.eu/idabc/eupl5
*
* Salvo cuando lo exija la legislacion aplicable o se acuerde.
* por escrito, el programa distribuido con arreglo a la
* Licencia se distribuye <<TAL CUAL>>,
* SIN GARANTIAS NI CONDICIONES DE NINGUN TIPO, ni expresas
* ni implicitas.
* Vease la Licencia en el idioma concreto que rige
* los permisos y limitaciones que establece la Licencia.
*/

var ArbolAmbCoords = new Class({
    Implemtns:[Events, Options],

    // Private vars
    plantree: null, // mootree del árbol de ámbitos
    options: {
        x: 0,
        y: 0,
        rutaServ: '',
        divtree: 'divtree',
        titulo: 'LISTADO DE AMBITOS',
        onSelectNode: null,
        idioma: 'es',
        srs: 4326
    },

    /*----------------------------
     * API Methods
     *---------------------------*/
    initialize: function(options){
        this.options.x = options.x,
        this.options.y = options.y,
        this.options.srs = options.srs,
        this.options.rutaServ = options.rutaServ,
        this.options.divtree = options.divtree,
        this.options.titulo = options.titulo
        this.options.onSelectNode = options.onSelectNode,
        this.options.idioma = options.idioma
    },

    display: function(){
        this.plantree = new MooTreeControl({
            div: this.options.divtree,
            mode: 'folders',
            grid: true,
            theme: this.options.rutaServ+'javascripts/mooTree2/mootree.gif',
            loader: {
                icon: this.options.rutaServ+'javascripts/mooTree2/mootree_loader.gif',
                text: 'Cargando...',
                color: '#a0a0a0'
            },
            onSelect: this.options.onSelectNode.bind(this)
        },{
            text: this.options.titulo,
            open: true,
            progressive: true,
            progressive_chunk: 1
        });
        this.plantree.root.load(this.options.rutaServ+'RestMethod?wsName=GET_NODOS_AMBITOS_DE_COOR_VIS&x='+escape(this.options.x)+'&y='+escape(this.options.y)+'&srs='+escape(this.options.srs)+'&rutaServ='+escape(this.options.rutaServ)+'&idioma='+escape(this.options.idioma));
    }
});