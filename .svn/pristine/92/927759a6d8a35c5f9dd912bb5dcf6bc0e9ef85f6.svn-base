var formVal = new Class({
    Implements: [Events,Options],

    initialize: function(form_id){
        this.errors = new Array();
        this.idform = form_id;
    },
    
    // API
    addError: function(element, errorlit){
        if (element) {
            element.addClass('fvFieldError');
            var errMsg = new Element('div',{
                'class': 'fvErrorMsg',
                'html': errorlit
            });
            insertAfter(errMsg,element);
        }
        this.errors.push(errorlit);
    },

    // API
    isValid: function(){
        return this.errors.length == 0;
    },
    
    
    clearErrors: function(){
        //clear error messages
        $$('form#'+this.idform+' div.fvErrorMsg').each(function(node){
            node.dispose();
        });
        
        //revert error style to form elements
        $$('form#'+this.idform+' input.fvFieldError').each(function(node){
            node.removeClass('fvFieldError');
        });
        $$('form#'+this.idform+' select.fvFieldError').each(function(node){
            node.removeClass('fvFieldError');
        });
        $$('form#'+this.idform+' textarea.fvFieldError').each(function(node){
            node.removeClass('fvFieldError');
        });

        this.errors = new Array();
    },
    clearErrorsFromDiv: function(){
        //clear error messages
        $$('#'+this.idform+' div.fvErrorMsg').each(function(node){
            node.dispose();
        });
        //revert error style to form elements
        $$('#'+this.idform+' input.fvFieldError').each(function(node){
            node.removeClass('fvFieldError');
        });
        $$('#'+this.idform+' select.fvFieldError').each(function(node){
            node.removeClass('fvFieldError');
        });
        $$('#'+this.idform+' textarea.fvFieldError').each(function(node){
            node.removeClass('fvFieldError');
        });

        this.errors = new Array();
    }
});

function insertAfter(node, referenceNode) {
    var parent = referenceNode.parentNode;
    parent.insertBefore(node, referenceNode.nextSibling);
}