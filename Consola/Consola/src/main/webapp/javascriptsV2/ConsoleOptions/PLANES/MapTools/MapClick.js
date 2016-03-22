OpenLayers.Control.MapClick = OpenLayers.Class(OpenLayers.Control, {

    /**
    * Property:
    * defaultHandlerOptions
    * {Object}
    */
    defaultHandlerOptions: {
        'single': true,
        'double': false,
        'pixelTolerance': 0,
        'stopSingle': false,
        'stopDouble': false
    },

    /**
    * Constructor: OpenLayers.Control.Click
    * Create a new click handler
    *
    * Parameters:
    * options - {Object}
    */
    initialize: function (options) {
        this.handlerOptions = OpenLayers.Util.extend(
        {}, this.defaultHandlerOptions
            );
        OpenLayers.Control.prototype.initialize.apply(
            this, arguments
            );
        this.handler = new OpenLayers.Handler.Click(
            this, {
                'click': this.onClick,
                'dblclick': this.onDblclick
            }, this.handlerOptions
            );
    },

    /**
    * Method: onClick
    * default onClick method
    * to be redefined in the application
    */
    onClick: function (evt) {
    },

    /**
    * Method: onDbllick
    * default onDbllick method
    * to be redefined in the application
    */
    onDblclick: function (evt) {
    },

    CLASS_NAME: "OpenLayers.Control.MapClick"
});
