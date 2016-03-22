/*
Script: mootree.js
	My Object Oriented Tree
	- Developed by Rasmus Schultz, <http://www.mindplay.dk>
	- Tested with MooTools release 1.2, under Firefox 2, Opera 9 and Internet Explorer 6 and 7.

License:
	MIT-style license.

Credits:
	Inspired by:
	- WebFX xTree, <http://webfx.eae.net/dhtml/xtree/>
	- Destroydrop dTree, <http://www.destroydrop.com/javascripts/tree/>

Changes:

	rev.12:
	- load() only worked once on the same node, fixed.
	- the script would sometimes try to get 'R' from the server, fixed.
	- the 'load' attribute is now supported in XML files (see example_5.html).

	rev.13:
	- enable() and disable() added - the adopt() and load() methods use these to improve performance by minimizing the number of visual updates.

	rev.14:
	- toggle() was using enable() and disable() which actually caused it to do extra work - fixed.

	rev.15:
	- adopt() now picks up 'href', 'target', 'title' and 'name' attributes of the a-tag, and stores them in the data object.
	- adopt() now picks up additional constructor arguments from embedded comments, e.g. icons, colors, etc.
	- documentation now generates properly with NaturalDocs, <http://www.naturaldocs.org/>

	rev.16:
	- onClick events added to MooTreeControl and MooTreeNode
	- nodes can now have id's - <MooTreeControl.get> method can be used to find a node with a given id

	rev.17:
	- changed icon rendering to use innerHTML, making the control faster (and code size slightly smaller).

	rev.18:
	- migrated to MooTools 1.2 (previous versions no longer supported)

*/

var MooTreeIcon = ['I','L','Lminus','Lplus','Rminus','Rplus','T','Tminus','Tplus','_closed','_doc','_open','minus','plus'];

/*
Class: MooTreeControl
	This class implements a tree control.

Properties:
	root - returns the root <MooTreeNode> object.
	selected - returns the currently selected <MooTreeNode> object, or null if nothing is currently selected.

Events:
	onExpand - called when a node is expanded or collapsed: function(node, state) - where node is the <MooTreeNode> object that fired the event, and state is a boolean meaning true:expanded or false:collapsed.
	onSelect - called when a node is selected or deselected: function(node, state) - where node is the <MooTreeNode> object that fired the event, and state is a boolean meaning true:selected or false:deselected.
	onClick - called when a node is clicked: function(node) - where node is the <MooTreeNode> object that fired the event.

Parameters:
	The constructor takes two object parameters: config and options.
	The first, config, contains global settings for the tree control - you can use the configuration options listed below.
	The second, options, should contain options for the <MooTreeNode> constructor - please refer to the options listed in the <MooTreeNode> documentation.

Config:
	div - a string representing the div Element inside which to build the tree control.
	mode - optional string, defaults to 'files' - specifies default icon behavior. In 'files' mode, empty nodes have a document icon - whereas, in 'folders' mode, all nodes are displayed as folders (a'la explorer).
	grid - boolean, defaults to false. If set to true, a grid is drawn to outline the structure of the tree.

	theme - string, optional, defaults to 'mootree.gif' - specifies the 'theme' GIF to use.

	loader - optional, an options object for the <MooTreeNode> constructor - defaults to {icon:'mootree_loader.gif', text:'Loading...', color:'a0a0a0'}

	onExpand - optional function (see Events above)
	onSelect - optional function (see Events above)

*/

var MooTreeControl_Capas = new Class({

    initialize: function(config, options,visor) {
        this.visor=visor;
        //alert('comienzoArbol');
        options.control = this;
        //alert(1)// make sure our new MooTreeNode knows who it's owner control is
        options.div = config.div;             // tells the root node which div to insert itself into
        //alert(2)
        this.progressive = options.progressive;
        //alert(3)
        this.root = new MooTreeNode_Capas(options); // create the root node of this tree control
        //alert("root: "+this.root);
        this.index = new Object();            // used by the get() method

        this.enabled = true;                  // enable visual updates of the control

        this.theme = config.theme || 'javascriptsV2/ConsoleOptions/previewVisor/arbolCapas/mootree.gif';

        this.loader = config.loader || {
            icon:'javascriptsV2/ConsoleOptions/previewVisor/arbolCapas/mootree_loader.gif',
            text:'Loading...',
            color:'#a0a0a0'
        };

        this.selected = null;                 // set the currently selected node to nothing
        this.mode = config.mode;              // mode can be "folders" or "files", and affects the default icons
        this.grid = config.grid;              // grid can be turned on (true) or off (false)

        this.onExpand = config.onExpand || new Function(); // called when any node in the tree is expanded/collapsed
        this.onSelect = config.onSelect || new Function(); // called when any node in the tree is selected/deselected
        this.onClick = config.onClick || new Function(); // called when any node in the tree is clicked

        this.root.update(true);
    //alert('finArbol');

    },

    /*
	Property: insert
		Creates a new node under the root node of this tree.

	Parameters:
		options - an object containing the same options available to the <MooTreeNode> constructor.

	Returns:
		A new <MooTreeNode> instance.
	*/

    insert: function(options) {
        options.control = this;
        return this.root.insert(options);
    },

    /*
	Property: select
		Sets the currently selected node.
		This is called by <MooTreeNode> when a node is selected (e.g. by clicking it's title with the mouse).

	Parameters:
		node - the <MooTreeNode> object to select.
	*/

    select: function(node) {
        this.onClick(node);
        node.onClick(); // fire click events
        if (this.selected === node) return; // already selected
        if (this.selected) {
            // deselect previously selected node:
            this.selected.select(false);
            this.onSelect(this.selected, false);
        }
        // select new node:
        this.selected = node;
        node.select(true);
        this.onSelect(node, true);
    },

    /*
	Property: expand
		Expands the entire tree, recursively.
	*/

    expand: function() {
        this.root.toggle(true, true);
    },

    /*
	Property: collapse
		Collapses the entire tree, recursively.
	*/

    collapse: function() {
        this.root.toggle(true, false);
    },

    /*
	Property: get
		Retrieves the node with the given id - or null, if no node with the given id exists.

	Parameters:
		id - a string, the id of the node you wish to retrieve.

	Note:
		Node id can be assigned via the <MooTreeNode> constructor, e.g. using the <MooTreeNode.insert> method.
	*/

    get: function(id) {
        return this.index[id] || null;
    },

    /*
	Property: adopt
		Adopts a structure of nested ul/li/a elements as tree nodes, then removes the original elements.

	Parameters:
		id - a string representing the ul element to be adopted, or an element reference.
		parentNode - optional, a <MooTreeNode> object under which to import the specified ul element. Defaults to the root node of the parent control.

	Note:
		The ul/li structure must be properly nested, and each li-element must contain one a-element, e.g.:

		><ul id="mytree">
		>  <li><a href="test.html">Item One</a></li>
		>  <li><a href="test.html">Item Two</a>
		>    <ul>
		>      <li><a href="test.html">Item Two Point One</a></li>
		>      <li><a href="test.html">Item Two Point Two</a></li>
		>    </ul>
		>  </li>
		>  <li><a href="test.html"><!-- icon:_doc; color:#ff0000 -->Item Three</a></li>
		></ul>

		The "href", "target", "title" and "name" attributes of the a-tags are picked up and stored in the
		data property of the node.

		CSS-style comments inside a-tags are parsed, and treated as arguments for <MooTreeNode> constructor,
		e.g. "icon", "openicon", "color", etc.
	*/

    adopt: function(id, parentNode) {
        if (parentNode === undefined) parentNode = this.root;
        this.disable();
        this._adopt(id, parentNode);
        parentNode.update(true);
        $(id).destroy();
        this.enable();
    },

    _adopt: function(id, parentNode) {
        /* adopts a structure of ul/li elements into this tree */
        e = $(id);
        var i=0, c = e.getChildren();
        for (i=0; i<c.length; i++) {
            if (c[i].nodeName == 'LI') {
                var con={
                    text:''
                }, comment='', node=null, subul=null;
                var n=0, z=0, se=null, s = c[i].getChildren();
                for (n=0; n<s.length; n++) {
                    switch (s[n].nodeName) {
                        case 'A':
                            for (z=0; z<s[n].childNodes.length; z++) {
                                se = s[n].childNodes[z];
                                switch (se.nodeName) {
                                    case '#text':
                                        con.text += se.nodeValue;
                                        break;
                                    case '#comment':
                                        comment += se.nodeValue;
                                        break;
                                }
                            }
                            con.data = s[n].getProperties('href','target','title','name','idNode','tipo');
                            break;

                        case 'UL':
                            subul = s[n];
                            break;
                    }
                }
                if (con.label != '') {
                    con.data.url = con.data['href']; // (for backwards compatibility)
                    if (comment != '') {
                        var bits = comment.split(';');
                        for (z=0; z<bits.length; z++) {
                            var pcs = bits[z].trim().split(':');
                            if (pcs.length == 2) con[pcs[0].trim()] = pcs[1].trim();
                        }
                    }
                    node = parentNode.insert(con);
                    if (subul) this._adopt(subul, node);
                }
            }
        }
    },

    /*
	Property: disable
		Call this to temporarily disable visual updates -- if you need to insert/remove many nodes
		at a time, many visual updates would normally occur. By temporarily disabling the control,
		these visual updates will be skipped.

		When you're done making changes, call <MooTreeControl.enable> to turn on visual updates
		again, and automatically repaint all nodes that were changed.
	*/

    disable: function() {
        this.enabled = false;
    },

    /*
	Property: enable
		Enables visual updates again after a call to <MooTreeControl.disable>
	*/

    enable: function() {
        this.enabled = true;
        this.root.update(true, true);
    }

});

/*
Class: MooTreeNode
	This class implements the functionality of a single node in a <MooTreeControl>.

Note:
	You should not manually create objects of this class -- rather, you should use
	<MooTreeControl.insert> to create nodes in the root of the tree, and then use
	the similar function <MooTreeNode.insert> to create subnodes.

	Both insert methods have a similar syntax, and both return the newly created
	<MooTreeNode> object.

Parameters:
	options - an object. See options below.

Options:
	text - this is the displayed text of the node, and as such as is the only required parameter.
	id - string, optional - if specified, must be a unique node identifier. Nodes with id can be retrieved using the <MooTreeControl.get> method.
	color - string, optional - if specified, must be a six-digit hexadecimal RGB color code.

	open - boolean value, defaults to false. Use true if you want the node open from the start.

	icon - use this to customize the icon of the node. The following predefined values may be used: '_open', '_closed' and '_doc'. Alternatively, specify the URL of a GIF or PNG image to use - this should be exactly 18x18 pixels in size. If you have a strip of images, you can specify an image number (e.g. 'my_icons.gif#4' for icon number 4).
	openicon - use this to customize the icon of the node when it's open.

	data - an object containing whatever data you wish to associate with this node (such as an url and/or an id, etc.)

Events:
	onExpand - called when the node is expanded or collapsed: function(state) - where state is a boolean meaning true:expanded or false:collapsed.
	onSelect - called when the node is selected or deselected: function(state) - where state is a boolean meaning true:selected or false:deselected.
	onClick - called when the node is clicked (no arguments).
*/

var MooTreeNode_Capas = new Class({

    initialize: function(options) {
        var layer = options.control.visor.map.getLayer(options.text);
        if (layer){
            options.data.visibility=layer.getVisibility().toString();
            options.data.opacity=layer.opacity;
        }
        this.clase='mooTree_checkOFF';
        if(options.data && options.data.typeButton && options.data.typeButton=='delete')
        {
            this.clase='mooTree_deleteOFF';
        }
        if(options.data && options.data.visibility=='true')
        {
            this.clase='mooTree_checkON';
            if(options.data.type!='GOOGLE' && options.data.type!='VECTOR' && options.data.type!='MARKERS')
            {
                var estado=options.control.visor.generarNuevaCapaSegunNodeArbol(options.text);
                layer.setVisibility(true);
                options.control.visor.actualizarZIndexMarkers();
            }
        }
        var checkbox=new Element('div',{
            'id':'chk_'+options.text,
            'class':this.clase
        });

        
       
        //        if(visor.map.getLayer(jsIO.cargarTextoSegunIdioma(options.text)))
        //        {
        //            checkbox.setProperty('class',this.getClase());
        //        }
        //alert('¡nuevo mootreenode 1');
        this.checkbox = checkbox;       // the text displayed by this node
        this.text = options.text;       // the text displayed by this node
        this.id = options.id || null;   // the node's unique id
        this.nodes = new Array();       // subnodes nested beneath this node (MooTreeNode objects)
        this.parent = null;             // this node's parent node (another MooTreeNode object)
        this.last = true;               // a flag telling whether this node is the last (bottom) node of it's parent
        this.control = options.control; // owner control of this node's tree
        this.selected = false;          // a flag telling whether this node is the currently selected node in it's tree


        this.color = options.color || null; // text color of this node

        this.data = options.data || {}; // optional object containing whatever data you wish to associate with the node (typically an url or an id)
        //alert('¡nuevo mootreenode 2');
        this.onExpand = options.onExpand || new Function(); // called when the individual node is expanded/collapsed
        this.onSelect = options.onSelect || new Function(); // called when the individual node is selected/deselected
        this.onClick = options.onClick || new Function(); // called when the individual node is clicked

        this.open = options.open ? true : false; // flag: node open or closed?

        this.progressive = options.progressive || false;  //progressive load of child nodes

        this.icon = options.icon;
        this.openicon = options.openicon || this.icon;

        // add the node to the control's node index:
        if (this.id) this.control.index[this.id] = this;

        this.id=options.text;
        //alert('¡nuevo mootreenode 3');
        // create the necessary divs:
        //this.id='node_'+ options.text;
        
        this.div = {
            main: new Element('div').addClass('mooTree_node_Capas'),
            indent: new Element('div'),
            gadget: new Element('div'),
            icon: new Element('div'),
            checkbox: checkbox,
            text: new Element('div').addClass('mooTree_text_Capas'),
            sub: new Element('div')
        }
        


        // put the other divs under the main div:
        this.div.main.adopt(this.div.indent);
        this.div.main.adopt(this.div.gadget);
        this.div.main.adopt(this.div.icon);
        
        if(this.data.type!=undefined && this.data.type!='folder')
        {
            this.div.main.adopt(this.div.checkbox);
        }
        this.div.main.adopt(this.div.text);
        //alert('¡nuevo mootreenode 4');
        // put the main and sub divs in the specified parent div:
        $(options.div).adopt(this.div.main);
        $(options.div).adopt(this.div.sub);

        // attach event handler to gadget:
        this.div.gadget._node = this;
        this.div.gadget.onclick = this.div.gadget.ondblclick = function() {
            this._node.toggle();
        }
        //alert('¡nuevo mootreenode 5');
        // attach event handler to icon/text:
        this.div.icon._node = this.div.text._node = this.div.checkbox._node = this;
        //        if (this.data.exportPdf!=undefined && this.data.exportPdf=='true'){
        //            this.div.icon.onclick = this.div.icon.ondblclick = function() {
        //               var aLayerPdf= visor.map.getLayer(this._node.text);
        //               if (aLayerPdf){
        //                   var urlPdf=aLayerPdf.getFullRequestString({BBOX:visor.map.getExtent().transform(new OpenLayers.Projection(visor.projection),new OpenLayers.Projection(visor.displayProjection)).toBBOX(),FORMAT:"application/pdf",WIDTH: visor.map.size.w,HEIGHT: visor.map.size.h})
        //                   window.open(urlPdf,"_blank");
        //               }
        //            }

        //        }else{
        //            this.div.icon.onclick = this.div.icon.ondblclick = this.div.text.onclick = this.div.text.ondblclick = this.div.checkbox.onclick = this.div.checkbox.ondblclick = function() {
        //               // this._node.control.select(this._node);
        //            }
        //        }
        
        //DESACTIVO LA LEYENDA DE MOMENTO
        if(options && options.data && options.data.type=='WMS' && false)
        {
            this.tipo=options.data.type;
            this.div.icon.setStyle('cursor','pointer')
            this.div.icon.addEvent('click',function(){

                //var JV = new JobWindow("LEYENDA_"+Math.floor(Math.random()*1011)*(-1),false,300,250,320,320,true,false);
                var JV = document.getElementById("jwContenedor_LEYENDA_"+this.options.data.layers);
                if (JV==null)
                {
                    JV=new JobWindow("LEYENDA_"+this.options.data.layers,false,300,250,320,320,true,false);
                    JV.titulo.set('html','LEYENDA '+this.options.text);
                
                
                    var div=new Element('div',{
                        'id':'x_'+this.options.text
                    });
                    if(this.options.data.typeButton=='delete')
                    {
                        jsIO.getServiceAsync({
                            'wsName':'GET_LEYENDA',
                            'idHoja':this.options.data.idHoja
                        },
                        function(url){
                            var lnk=new Element('a',{
                                'href':url,
                                'target':'blank'
                            }).injectInside(div);

                            new Element('img',{
                                'id':'imageLegend_'+this.text,
                                'src':url,
                                'title':this.text,
                                'style':'width:100%;height:100%;border-style:none',
                                'alt':''
                            }).injectInside(lnk);
                        }.bind({
                            div:div,
                            text:this.options.text
                            }),
                        function(error){
                            this.div.set('html','<br><br><center>'+jsIO.cargarTextoSegunIdioma("NO HAY LEYENDA PARA ESTA CAPA")+'</center>')
                            this.div.injectInside(this.JV.zonaActiva);
                        }.bind({
                            div:div,
                            JV:JV
                        }))
                        div.injectInside(JV.zonaActiva);
                    }
                    else
                    {
                        var url=this.options.data.url;
                        if(url)
                        {
                            if(!url.contains('?'))
                            {
                                url=url+'?';
                            }
                            var capa=this.visor.getLayerLegendDeCapaSiLoTiene(this.options.text);

                            new Element('img',{
                                'id':'imageLegend_'+this.options.text,
                                'src':url.replace("gwc/service/","")+"request=GetLegendGraphic&LAYER="+capa+"&FORMAT=image/png",
                                'title':this.options.text
                            }).injectInside(div);
                            div.injectInside(JV.zonaActiva);
                        }
                        else
                        {
                            div.set('html','<br><br><center>'+jsIO.cargarTextoSegunIdioma("NO HAY LEYENDA PARA ESTA CAPA")+'</center>')
                            div.injectInside(JV.zonaActiva);
                        }
                    }
                }

            }.bind({
                options:options
            }));
        }

        
        if(this.div.checkbox)
        {
            this.div.checkbox.onclick =this.div.checkbox.ondblclick = function(checkbox) {
                if(this._node.div.checkbox.getProperty('class')=='mooTree_deleteOFF')
                {
                    var layer=this._node.control.visor.map.getLayer(this._node.text)
                    if(layer!=null && layer!=undefined)
                    {
                        this.visor.map.removeLayer(layer);
                    }
                    this._node.div.main.destroy();
                    if(this._node.control.visor.layerVector){
                        //visor.layerVector.setZIndex((layerCreada.getZIndex()).toInt()+5);
                        this._node.control.visor.layerVector.display(false);
                        this._node.control.visor.layerVector.display(true);
                    }
                }
                else
                {
                    //this._node.control.select(this._node);
                    var estado=this._node.control.visor.generarNuevaCapaSegunNodeArbol(this._node.text);
                    this._node.control.visor.actualizarZIndexMarkers();
                    if(estado)
                    {
                        this._node.div.checkbox.setProperty('class','mooTree_checkON');
                        var layerCreada=this._node.control.visor.map.getLayer(this._node.text)
                        layerCreada.setOpacity(($('knobElement_'+this._node.text).getStyle('left').toInt())/100);
                        

                        if(this._node.control.visor.layerVector){
                            //visor.layerVector.setZIndex((layerCreada.getZIndex()).toInt()+5);
                            this._node.control.visor.layerVector.display(false);
                            this._node.control.visor.layerVector.display(true);
                        }
                    }
                    else
                    {
                        if(this._node.text=='G-BASE')
                        {
                            if(this._node.div.checkbox.getProperty('class')=='mooTree_checkOFF')
                            {
                                this._node.div.checkbox.setProperty('class','mooTree_checkON');
                            }
                            else
                            {
                                this._node.div.checkbox.setProperty('class','mooTree_checkOFF');
                            }
                            
                            this._node.div.icon.getFirst().setStyle ('background-position', '-163px 0px');
                            this._node.div.icon.getFirst().setStyle ('cursor', null);
                        }
                        else
                        {
                            this._node.div.checkbox.setProperty('class','mooTree_checkOFF');
                            //this._node.div.icon.getFirst().setStyle ('background-position', '-173px 0px');
                            this._node.div.icon.getFirst().setStyle ('cursor', null);
                        }
                        
                    }
                }
            }
        }

    //alert('¡nuevo mootreenode 6');

    },

    /*
	Property: insert
		Creates a new node, nested inside this one.

	Parameters:
		options - an object containing the same options available to the <MooTreeNode> constructor.

	Returns:
		A new <MooTreeNode> instance.
	*/
    getClaseON:function(options){

        if(options.data && options.data.typeButton && options.data.typeButton=='delete')
        {
            this.clase='mooTree_deleteON';
        }
        else
        {
            this.clase='mooTree_checkON';
        }
    },
    getClaseOFF:function(options){

        if(options.data && options.data.typeButton && options.data.typeButton=='delete')
        {
            this.clase='mooTree_deleteOFF';
        }
        else
        {
            this.clase='mooTree_checkOFF';
        }
    },
    insert: function(options) {

        // set the parent div and create the node:
        options.div = this.div.sub;
        options.control = this.control;
        var node = new MooTreeNode_Capas(options);


        // set the new node's parent:
        node.parent = this;

        // mark this node's last node as no longer being the last, then add the new last node:
        var n = this.nodes;
        if (n.length) n[n.length-1].last = false;
        n.push(node);

        
        // repaint the new node:
        node.update();

        // repaint the new node's parent (this node):
        if (n.length == 1) this.update();

        // recursively repaint the new node's previous sibling node:
        if (n.length > 1) n[n.length-2].update(true);

        
        
        return node;

    },

    /*
	Property: remove
		Removes this node, and all of it's child nodes. If you want to remove all the childnodes without removing the node itself, use <MooTreeNode.clear>
	*/

    remove: function() {
        var p = this.parent;
        this._remove();
        p.update(true);
    },

    _remove: function() {

        // recursively remove this node's subnodes:
        var n = this.nodes;
        while (n.length) n[n.length-1]._remove();

        // remove the node id from the control's index:
        delete this.control.index[this.id];

        // remove this node's divs:
        this.div.main.destroy();
        this.div.sub.destroy();

        if (this.parent) {

            // remove this node from the parent's collection of nodes:
            var p = this.parent.nodes;
            p.erase(this);

            // in case we removed the parent's last node, flag it's current last node as being the last:
            if (p.length) p[p.length-1].last = true;

        }

    },

    /*
	Property: clear
		Removes all child nodes under this node, without removing the node itself.
		To remove all nodes including this one, use <MooTreeNode.remove>
	*/

    clear: function() {
        this.control.disable();
        while (this.nodes.length) this.nodes[this.nodes.length-1].remove();
        this.control.enable();
    },

    /*
	Property: update
		Update the tree node's visual appearance.

	Parameters:
		recursive - boolean, defaults to false. If true, recursively updates all nodes beneath this one.
		invalidated - boolean, defaults to false. If true, updates only nodes that have been invalidated while the control has been disabled.
	*/

    update: function(recursive, invalidated) {

        var draw = true;

        if (!this.control.enabled) {
            // control is currently disabled, so we don't do any visual updates
            this.invalidated = true;
            draw = false;
        }

        if (invalidated) {
            if (!this.invalidated) {
                draw = false; // this one is still valid, don't draw
            } else {
                this.invalidated = false; // we're drawing this item now
            }
        }

        if (draw) {

            var x;

            // make selected, or not:
            this.div.main.className = 'mooTree_node_Capas' + (this.selected ? ' mooTree_selected_Capas' : '');

            // update indentations:
            var p = this, i = '';
            while (p.parent) {
                p = p.parent;
                i = this.getImg(p.last || !this.control.grid ? '' : 'I') + i;
            }
            this.div.indent.innerHTML = i;
            
            var slider=new Element('div',{
                'id':'slider_'+this.text,
                'class':'zonaSlider',
                'html':"<div id='sliderCapa_lbl' class='sliderCapa_lbl'>"+this.text+"</div>"
            //"<div id='sliderElement_"+this.text+"' class='sliderCapa'><div class='knobCapa'></div></div>"

            });
                    


            // update the text:
            x = this.div.text;
            x.empty();
            //x.adopt(this.text);
            x.adopt(slider);
            if(this.data.type && this.data.type!='folder')
            {
                var sliderCapa=new Element('div',{
                    'id':'sliderElement_'+this.text,
                    'class':'sliderCapa'
                }).injectInside(slider);
                var knobCapa=new Element('div',{
                    'id':'knobElement_'+this.text,
                    'class':'knobCapa'
                }).injectInside(sliderCapa);
                var el=$('sliderElement_'+this.text);
                if (el!=null){
                    var s=new Slider(el,el.getElement('div[class=knobCapa]'), {
                        steps: 100,	// There are 35 steps
                        range: [1,100],	// Minimum value is 8
                        mode: 'horizontal',
                        snap:true,
                        wheel:true,
                        invert:false,
                        onChange: function(value){
                            var layer=this.control.visor.map.getLayer(jsIO.cargarTextoSegunIdioma(this.text));
                            if(layer)
                            {
                                layer.setOpacity(value/100);
                            }
                        }.bind(this)
                    });
                    var layerOp=this.control.visor.map.getLayer(jsIO.cargarTextoSegunIdioma(this.text));
                    if (layerOp && layerOp.opacity){
                        s.set(layerOp.opacity*100);
                    }else{
                        s.set(100);
                    }
                }
            }
                
            

            if (this.color) x.style.color = this.color;

            // update the icon:
            this.div.icon.innerHTML = this.getImg( this.nodes.length ? ( this.open ? (this.openicon || this.icon || '_open') : (this.icon || '_closed') ) : ( this.icon || (this.control.mode == 'folders' ? '_closed' : '_doc') ) );

            // update the plus/minus gadget:
            this.div.gadget.innerHTML = this.getImg( ( this.control.grid ? ( this.control.root == this ? (this.nodes.length ? 'R' : '') : (this.last?'L':'T') ) : '') + (this.nodes.length ? (this.open?'minus':'plus') : '') );

            // show/hide subnodes:
            this.div.sub.style.display = this.open ? 'block' : 'none';

        }
        
        if(this.data && this.data.esPlano)
        {
            this.div.icon.getFirst().setStyle ('background-position', 'center center');
            this.div.icon.getFirst().setStyle ('background-image', 'url(styles/images/closebox.png)');
            this.div.icon.getFirst().setStyle ('cursor', 'pointer');
            this.div.icon.set('title','Eliminar plano')
            this.div.icon.onclick = this.div.icon.ondblclick = function() { 
                var layer=this.node.control.visor.map.getLayer(this.node.text)
                if(layer!=null && layer!=undefined)
                {
                    this.node.control.visor.map.removeLayer(layer);
                    this.node.remove();
                }
            }.bind({node:this})
        }

        // if recursively updating, update all child nodes:
        if (recursive) this.nodes.forEach( function(node) {
            node.update(true, invalidated);
        });


    },

    /*
	Property: getImg
		Creates a new image, in the form of HTML for a DIV element with appropriate style.
		You should not need to manually call this method. (though if for some reason you want to, you can)

	Parameters:
		name - the name of new image to create, defined by <MooTreeIcon> or located in an external file.

	Returns:
		The HTML for a new div Element.
	*/

    getImg: function(name) {

        var html = '<div class="mooTree_img"';

        //
        //            var i = MooTreeIcon.indexOf(name);
        //                if (i == -1) {
        //                    // custom (external) icon:
        //                    var x = name.split('#');
        //                    img = x[0];
        //                    i = (x.length == 2 ? parseInt(x[1])-1 : 0);
        //                }
        //
        //            if(i==1)
        //            {
        //                html += ' style="background-position:-' + (i*15) + 'px 0px;"';
        //            }
        //            else
        //            {
        //                html += ' style="background-position:-' + ((i*17)+3) + 'px 0px;"';
        //            }
        //        }
        //        else
        //        {

        if (name != '') {
            var img = this.control.theme;
            var i = MooTreeIcon.indexOf(name);
            if (i == -1) {
                // custom (external) icon:
                var x = name.split('#');
                img = x[0];
                i = (x.length == 2 ? parseInt(x[1])-1 : 0);
            }
            if(this.tipo && this.tipo=='WMS')
            {
                html = '<div alt="' + i + '" class="mooTree_imgLeyenda" title="' + jsIO.cargarTextoSegunIdioma("txt_leyenda") + ' ' + this.text + '" ';
                html += ' style="background-position:-' + (i*18) + 'px 0px;"';
            }else{
                html += ' alt="' + i + '" style="background-image:url(' + img + '); background-position:-' + (i*18) + 'px 0px;"';
            }
                
                
        }
        //}
        html += "></div>";

        return html;

    },

    /*
	Property: toggle
		By default (with no arguments) this function toggles the node between expanded/collapsed.
		Can also be used to recursively expand/collapse all or part of the tree.

	Parameters:
		recursive - boolean, defaults to false. With recursive set to true, all child nodes are recursively toggle to this node's new state.
		state - boolean. If undefined, the node's state is toggled. If true or false, the node can be explicitly opened or closed.
	*/

    toggle: function(recursive, state) {

        this.open = (state === undefined ? !this.open : state);
        this.update();

        this.onExpand(this.open);
        this.control.onExpand(this, this.open);

        if (recursive) this.nodes.forEach( function(node) {
            node.toggle(true, this.open);
        }, this);

    },

    /*
	Property: select
		Called by <MooTreeControl> when the selection changes.
		You should not manually call this method - to set the selection, use the <MooTreeControl.select> method.
	*/

    select: function(state) {
        this.selected = state;
        this.update();
        this.onSelect(state);
    },

    /*
	Property: load
		Asynchronously load an XML structure into a node of this tree.

	Parameters:
		url - string, required, specifies the URL from which to load the XML document.
		vars - query string, optional.
	*/

    load: function(url, vars) {
        //alert(url)
        //alert(vars)
        if (this.loading) return; // if this node is already loading, return
        this.loading = true;      // flag this node as loading

        this.toggle(false, true); // expand the node to make the loader visible

        this.clear();

        this.insert(this.control.loader);
        var f = function() {
            new Request({
                method: 'GET',
                url: url,
                onSuccess: this._loaded.bind(this),
                onFailure: this._load_err.bind(this)
            //}).send(url, vars || '');
            }).send();
        }.bind(this).delay(20);

    //window.setTimeout(f.bind(this), 20); // allowing a small delay for the browser to draw the loader-icon.

    },

    _loaded: function(text, xml) {
        // called on success - import nodes from the root element:
        this.control.disable();
        this.clear();
        this._import(xml.documentElement);
        this.control.enable();
        this.loading = false;
    },

    _import: function(e) {
        if (this.progressive){
            this._import_progressive(this, e, 0);
            return;
        }
        // import childnodes from an xml element:
        var n = e.childNodes;
        for (var i=0; i<n.length; i++) if (n[i].tagName == 'node') {
            //if(visor.baseLayerGoogle==true || (visor.baseLayerGoogle==false && n[i].getAttribute('type')=='folder' && n[i].getAttribute('text')!='GOOGLE') ||(visor.baseLayerGoogle==false && n[i].getAttribute('type')!='GOOGLE'))
            if(this._checkBaseLayerGoogle(n[i]))
            {
                var opt = {
                    data:{}
                };
                var a = n[i].attributes;
                for (var t=0; t<a.length; t++) {
                    switch (a[t].name) {
                        case 'text':
                        case 'id':
                        case 'icon':
                        case 'openicon':
                        case 'color':
                        case 'open':
                            opt[a[t].name] = a[t].value;
                            break;
                        default:
                            opt.data[a[t].name] = a[t].value;
                    }
                }
                var node = this.insert(opt);
                if (node.data.load) {
                    node.open = false; // can't have a dynamically loading node that's already open!
                    node.insert(this.control.loader);
                    node.onExpand = function(state) {
                        this.load(this.data.load);
                        this.onExpand = new Function();
                    }
                }
                //if (node.data.visibility) this.clase='mooTree_checkON';
                // recursively import subnodes of this node:
                if (n[i].childNodes.length) node._import(n[i]);
            }
        }
    },
    _checkBaseLayerGoogle:function(nodo){
        if(this.control.visor.baseLayerGoogle==true){
            return true;
        }
        if(this.control.visor.baseLayerGoogle==false && nodo.getAttribute('type')=='folder' && nodo.getAttribute('text')=='GOOGLE'){
            return false;
        }
        if(this.control.visor.baseLayerGoogle==false && nodo.getAttribute('type')=='GOOGLE'){
            return false;
        }
        return true;
    },
    _import_progressive: function(inst, e,index){
        // import childnodes from an xml element:

        var n = e.getChildren();
        if (!index){
            index = 0;
        }
        var chunklength = 5;
        for (var i=index, len = n.length; i<len; i++) if (n[i].tagName == 'node') {
            var opt = {
                data:{}
            };
            var a = n[i].attributes;
            for (var t=0, tlen = a.length; t<a.length; t++) {
                switch (a[t].name) {
                    case 'text':
                    case 'id':
                    case 'icon':
                    case 'openicon':
                    case 'color':
                    case 'open':
                        opt[a[t].nodeName] = a[t].value;
                        break;
                    default:
                        opt.data[a[t].nodeName] = a[t].value;
                }
            }
            var node = this.insert(opt);
            node.progressive = this.progressive;
            if (node.data.load) {
                node.open = false; // can't have a dynamically loading node that's already open!
                node.insert(this.control.loader);
                node.onExpand = function(state) {
                    this.load(this.data.load);
                    this.onExpand = new Function();
                }
            }

            // recursively import subnodes of this node:
            if (n[i].childNodes.length) node._import(n[i]);

            // do not freeze the browser! Executing loop in [chunklength] blocks
            // http://stackoverflow.com/questions/714942/how-to-stop-intense-javascript-loop-from-freezing-the-browser
            if (i > 0 && i+1 < len && i % chunklength == 0 ){
                setTimeout(function(){
                    inst._import_progressive(inst, e, i+1);
                },5);
                break;
            }

        }
    },

    _load_err: function(req) {
        window.alert('Error loading: ' + this.text);
    }

});
