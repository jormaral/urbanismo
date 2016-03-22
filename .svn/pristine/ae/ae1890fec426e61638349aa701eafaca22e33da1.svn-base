var mooList = new Class({
    Implements: [Events,Options],

    getOptions: function(){
        return {
            alternaterows: true,
            selectable:true,
            url:null,
            rowbuttons: []
        };
    },

    initialize: function(container, options){
        this.setOptions(this.getOptions(), options);
        this.container = $(container);

        if (!this.container)
            return;

        this.draw();

        this.reset();

        this.loadData();
    },

    // API
    reset: function(){

        this.renderData();

        this.refreshDelayID = null;
        this.dragging = false;
        this.selected = new Array();

        if (this.options.accordion)
            this.elements = this.ulBody.getElements('li:nth-child(2n+1)'); // all li el. except accordian sections
        else
            this.elements = this.ulBody.getElements('li');

        this.filtered = false;
        this.lastsection = null;

        if (this.options.alternaterows)	this.altRow();

        //        this.elements.each(function(el,i){
        //            el.addEvent('click', this.onRowClick.bind(this));
        //
        //        }, this);
    },

    toggle: function(el){
        if ( el.getStyle('display') == 'block' )
        {
            el.setStyle('display', 'none');
        }else{
            el.setStyle('display', 'block');
        }
    },

    // API
    getSection: function(row){
        return this.ulBody.getElement('.section-'+row);
    },

    getLiParent: function (target){
        // ! ako se koristi labelFunction onda neki html elem. moze hvatati event, detektiraj pravi li
        target = $(target);

        while ( target && !target.hasClass('td') ){
            target = target.getParent();
        }

        if (target)
            return target.getParent();
    },


    onLoadData: function (data)
    {
        if ( this.container.getElement('.gBlock') )
            this.container.getElement('.gBlock').dispose();


        var pReload = this.container.getElement('div.pDiv .pReload');
        if (pReload)
            pReload.removeClass('loading');

        this.setData(data);

        // API
        this.fireEvent("loaddata", {
            target:this
        });
    },

    // API
    loadData: function (url)
    {
        if (!this.options.url)
            return;

        var data = {};

		

        // ************* white overflow & loader ************
        if ( this.container.getElement('.gBlock') )
            this.container.getElement('.gBlock').dispose();

        var gBlock = new Element('div', {
            style:'top: 0px; left: 0px;   background: white none repeat scroll 0% 0%;  -moz-background-clip: -moz-initial; -moz-background-origin: -moz-initial; -moz-background-inline-policy: -moz-initial; position: absolute; z-index: 999; opacity: 0.5; filter: alpha(opacity=50'
        } ) ;
        var bDiv = this.container.getElement('.bDiv');

        var top = 1;
        top += this.container.getElement('.tDiv') ? this.container.getElement('.tDiv').getSize().y : 0;
        top += this.container.getElement('.hDiv') ? this.container.getElement('.hDiv').getSize().y : 0;

        gBlock.setStyles({
            width:this.options.width,
            height: (bDiv ? bDiv.getSize().y:0),
            top:top
        });
        gBlock.addClass('gBlock');

        this.container.appendChild(gBlock);

        var pReload = this.container.getElement('div.pDiv .pReload');
        if (pReload)
            pReload.addClass('loading');
        // **************************************************

        var url = (url != null) ? url : this.options.url;
        var request = new Request.JSON({
            url:url,
            data:data
        });

        request.addEvent("complete", this.onLoadData.bind(this) ) ;

        request.send();
    },

    // API
    refresh: function(){
        this.loadData();
    },

    // API
    setData: function(data, cm){

        if (this.options.pagination){
            this.options.data = data.data;

            this.options.page =  data.page*1;
            this.options.total =  data.total;
            this.options.maxpage = Math.ceil(this.options.total/this.options.perPage);

            this.container.getElement('div.pDiv input').value = data.page;
            var to = (data.page*this.options.perPage) > data.total ? data.total : (data.page*this.options.perPage);
            this.container.getElement('div.pDiv .pPageStat').set('html', ((data.page-1)*this.options.perPage+1)+'..'+to+' / '+data.total);
            this.container.getElement('div.pDiv .pcontrol span').set('html', this.options.maxpage);
        }else
            this.options.data = data.data;

        this.reset();
    },

    // API
    getData: function(){
        return this.options.data;
    },

    // API
    getDataByRow: function(row){
        if (row >=0 && row<this.options.data.length)
            return this.options.data[row];
    },

    // API
    setDataByRow: function(row, data){
        if (row >=0 && row<this.options.data.length)
        {
            this.options.data[row] = data;

            this.reset();
        }
    },

    // API
    addRow: function(data, row){
        if (row >=0)
        {
            // ako podataci nisu inic. napravi novi array
            if (!this.options.data)
                this.options.data = [];

            this.options.data.splice(row, 0, data);

            this.reset();
        }
    },

    // API
    deleteRow: function(row){
        if (row >=0 && row<this.options.data.length)
        {
            this.options.data.splice(row, 1);
            this.reset();
        }
    },

    isHidden: function(i){
        return this.elements[i].hasClass( this.options.filterHideCls );
    },

    showLoader: function(){
        if (this.loader)
            return;

        this.loader = new Element('div');

        this.loader.addClass('elementloader');
        this.loader.inject(this.container);
    },

    hideLoader: function(){
        if (!this.loader)
            return;

        this.loader.dispose();
        this.loader = null;

    },

    // API
    selectAll: function(){

        this.elements.each(function(el, i){
            this.selected.push(el.retrieve('row'));
            el.addClass('selected');
        }, this);
    },

    // API
    unselectAll: function(){
        this.elements.each(function(el, i){
            el.removeClass('selected');
        }, this);

        this.selected = [];
    },

    // API
    getSelectedIndices: function(){
        return this.selected;
    },

    // API
    setSelectedIndices: function(arr){
        this.selected = arr;

        for (var i = 0; i < arr.length; i++)
        {
            var li = this.elements[arr[i]];
            //el.addClass('selected');
            // simulate user click
            this.onRowClick({
                target:li.getFirst(),
                control:false
            });
        }

    },

    

    // API
    removeAll: function(){
        if (this.ulBody)
            this.ulBody.empty();

        this.selected = new Array();

        //this.options.data = null;
    },




    renderData: function(){
        this.ulBody.empty();

        if (this.options.data)
        {
            var rowCount = this.options.data.length;

            for (var r=0; r<rowCount; r++)
            {
                var dataitem = this.options.data[r];
                var li = new Element('li');
                li.appendChild(new Element('div',{
                    'html': dataitem.text,
                    'class': 'rtext'
                }));

                var rowButtons = new Element('div',{
                    'class':'rbtnpanel'
                });

                for(var b=0, btnCount=this.options.rowbuttons.length; b<btnCount; b++){
                    var bDiv = new Element('div',{
                        'alt': this.options.rowbuttons[b].name,
                        'class': this.options.rowbuttons[b].bclass
                    });
                    rowButtons.appendChild(bDiv);
                    bDiv.addEvent('click', this.options.rowbuttons[b].onclick.bind(this, {
                        'id': dataitem.id,
                        'text':dataitem.text,
                        'data':dataitem.data
                    }));
                }

                li.appendChild(rowButtons);
                li.appendChild(new Element('div', {
                    'style': 'clear:both;'
                }));


                li.store('row', r);
                li.store('id', dataitem.id);
                if (dataitem.data != undefined){
                    li.store('data', dataitem.data);
                }
                this.ulBody.appendChild(li);

                if (this.options.tooltip)
                {
                    this.options.tooltip.attach( tr );
                }
            }
        }
    },

    // ************************************************************************
    // ************************* Main draw function ***************************
    // ************************************************************************
    draw: function(){
        this.removeAll(); // reset variables and only empty ulBody
        this.container.empty(); // empty all

        // ************************************************************************
        // ************************* Common ***************************************
        // ************************************************************************
        var width = this.options.width - (Browser.Engine.trident ? 2 : 2 ); //border 1px on each side


        // ************************************************************************
        // ************************* Container ************************************
        // ************************************************************************
        if (this.options.width)
            this.container.setStyle('width', width);
        this.container.addClass('moolist');

        // ************************************************************************
        // ************************* Header ***************************************
        // ************************************************************************
        if (this.options.title){
            var hDiv = new Element('div', {
                'html': this.options.title,
                'class': 'colheader'
            });
            hDiv.setStyle('width', width);
            this.container.appendChild(hDiv);
        }

		
        // ************************************************************************
        // ************************* Body *****************************************
        // ************************************************************************

        var bDiv = new Element('div');
        bDiv.addClass('bDiv');

        if (this.options.width)
            bDiv.setStyle('width', width);

        this.container.appendChild(bDiv);

		
        this.ulBody = new Element('ul');
        this.ulBody.setStyle('width', width); // da se ne vidi visak, ul je overflow hidden
        bDiv.appendChild(this.ulBody);
    },

	

    altRow: function(){
        this.elements.each(function(el,i){

            if(i % 2){
                el.removeClass('erow');
            }else{
                el.addClass('erow');
            }
        });
    },

    removeRowById: function(rowId){
        var newData = new Array();
        for(var i=0, len=this.options.data.length; i<len; i++){
            if (this.options.data[i].id != rowId){
                newData.push(this.options.data[i]);
            }
        }
        this.setData({data:newData});
    }

});


/*************************************************************/