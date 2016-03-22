/*******************************************************************************
* Popup Mensaje
******************************************************************************/

var modalWindow;

var PopupMsg = new Class({
    Implements: [Events, Options],
    //Variables privadas
    text: null, // texto que se mostrara en el popup
    popup: null, // objeto que gestiona el popup
    dialogresult:'',
    options: {
        type: 'ok', // allowed values: ok, yesno
        closable: true
    },


    // API - Constructor
    initialize: function(text, options){
        this.setOptions(this.options, options);
        this.text = text;


        var textDiv = new Element('div', {
            'class': 'popupmsg_text',
            'html': this.text
        });

        var btnDiv = this.getButtons();

        var mainDiv = new Element('div',{
            'class': 'popupmsg_wrapper'
        });
        mainDiv = mainDiv.grab(textDiv);
        
        if (btnDiv){
            mainDiv.grab(btnDiv);
        }


        this.removeEvents('onShow');
        this.removeEvents('onOpen');
        
        modalWindow = this;
        
        this.popup = function(){
            new MUI.Modal({
                id: 'modalWindow',
                title: '',         
                contentURL: 'Pages/Modal.html',
                type: 'modal',
                loadMethod: 'xhr',
                evalScripts: false,
                closable: false,
                width: 400,
                height: 200,
                padding: {
                    top: 43, 
                    right: 12, 
                    bottom: 10, 
                    left: 12
                },
                scrollbars: false,
                require: {
                    onload: function(){
                        mainDiv.inject('mainDiv');
    					
                        if (modalWindow.options.type === 'ok' || modalWindow.options.type === 'error'){
    			            
                            $$('.popupmsg_okbtn').addEvent('click', function(e) {
    			            	 
                                $('modalWindow').retrieve('instance').close();
                                modalWindow.dialogresult = 'ok';
                                modalWindow.fireEvent('close');			            
                            });
                        }

                        if (modalWindow.options.type === 'yesno'){
    			        	
                            $$('.popupmsg_yesbtn')[0].addEvent('click', function(e) {
    				        	
                                $('modalWindow').retrieve('instance').close();
                                modalWindow.dialogresult = 'yes';
                                modalWindow.fireEvent('close');		            
                            });
    			            
                            $$('.popupmsg_nobtn')[0].addEvent('click',function(e) {
    				        	
                                $('modalWindow').retrieve('instance').close();
                                modalWindow.dialogresult = 'no';
                                modalWindow.fireEvent('close');		            
                            });
    			            
                        }
                    }
                }
            });
        };
        this.popup();
    },

    //API
    getResult: function(){
        return this.dialogresult;
    },

    /********************
     * Private
     ********************/
    getButtons: function(){
        if (this.options.type === 'ok' || this.options.type === 'error'){
            var btnDiv = new Element('div', {
                'class': 'popupmsg_okbtn button',
                'html': 'Aceptar'
            });
            return btnDiv;
        }

        if (this.options.type === 'yesno'){
            var btnYesDiv = new Element('div', {
                'class':  'popupmsg_yesbtn  button',
                'html': 'S&iacute;'
            });

            var btnNoDiv = new Element('div', {
                'class': 'popupmsg_nobtn  button',
                'html': 'No'
            });

            var btnPanelDiv = new Element('div', {
                'class': 'popupmsg_panelbtn'
            }).grab(btnYesDiv).grab(btnNoDiv);
            return btnPanelDiv;
        }
        return '';
    }

});