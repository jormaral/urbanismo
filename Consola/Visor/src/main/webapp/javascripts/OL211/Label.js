/* Copyright (c) 2006-2007 MetaCarta, Inc., published under a modified BSD license.
 * See http://svn.openlayers.org/trunk/openlayers/repository-license.txt 
 * for the full text of the license. */


/**
 * @requires OpenLayers/Marker.js
 * @requires OpenLayers/Events.js
 * @requires OpenLayers/Icon.js
 * 
 * Class: OpenLayers.Marker.Label
 *
 * Example:
 * (code)
 * var markers = new OpenLayers.Layer.Markers( "Markers" );
 * map.addLayer(markers);
 *
 * var size = new OpenLayers.Size(10,17);
 * var offset = new OpenLayers.Pixel(-(size.w/2), -size.h);
 * var icon = new OpenLayers.Icon('http://boston.openguides.org/markers/AQUA.png',size,offset);
 * markers.addMarker(new OpenLayers.Marker.Label(new OpenLayers.LonLat(0,0),
 *                                                   icon, "hallo, world"));
 * markers.addMarker(new OpenLayers.Marker.Label(new OpenLayers.LonLat(0,0),
 *                                                  icon.clone(),"mouseover"));
 *
 * (end)
 *
 * Note that if you pass an icon into the Marker constructor, it will take
 * that icon and use it. This means that you should not share icons between
 * markers -- you use them once, but you should clone() for any additional
 * markers using that same icon.
 */
OpenLayers.Marker.Label = OpenLayers.Class(OpenLayers.Marker, {

    /** 
     * Property: labelDiv
     * {DOMElement}
     */
    labelDiv: null,

    /** 
     * Property: label
     * {String}
     */
    label: null,

    /** 
     * Property: label
     * {Boolean}
     */
    mouseOver: false,

    /** 
     * Property: labelClass
     * {String}
     */
    labelClass: "olMarkerLabel",

    /** 
     * Property: events 
     * {<OpenLayers.Events>} the event handler.
     */
    events: null,

    /** 
     * Property: div
     * {DOMElement}
     */
    div: null,

    /** 
     * Property: onlyOnMouseOver
     * {Boolean}
     */
    onlyOnMouseOver: false,

    /** 
     * Property: mouseover
     * {Boolean}
     */
    mouseover: false,

    /** 
     * Property: labelOffset
     * {String}
     */
    labelOffset: "10px",

    /** 
     * Constructor: OpenLayers.Marker.Label
     * Parameters:
     * icon - {<OpenLayers.Icon>}  the icon for this marker
     * lonlat - {<OpenLayers.LonLat>} the position of this marker
     * label - {String} the position of this marker
     * options - {Object}
     */
    initialize: function(lonlat, icon, label, options) {

        var newArguments = [];
        OpenLayers.Util.extend(this, options);
        newArguments.push(lonlat, icon, label);
        OpenLayers.Marker.prototype.initialize.apply(this, newArguments);


        this.label = label ;
        this.labelDiv = OpenLayers.Util.createDiv(this.icon.id + "_Text", null, null);
        this.labelDiv.className = this.labelClass;
        this.labelDiv.innerHTML = label;
        this.labelDiv.style.marginTop = this.labelOffset;
        this.labelDiv.style.marginLeft = this.labelOffset;
    },
    
    /**
     * APIMethod: destroy
     * Destroy the marker. You must first remove the marker from any 
     * layer which it has been added to, or you will get buggy behavior.
     * (This can not be done within the marker since the marker does not
     * know which layer it is attached to.)
     */
    destroy: function() {
        this.label = null;
        this.labelDiv = null;
        OpenLayers.Marker.prototype.destroy.apply(this, arguments);
    },

   
    /** 
    * Method: draw
    * Calls draw on the icon, and returns that output.
    * 
    * Parameters:
    * px - {<OpenLayers.Pixel>}
    * 
    * Returns:
    * {DOMElement} A new DOM Image with this marker's icon set at the 
    * location passed-in
    */
    draw: function(px) {
        this.div = OpenLayers.Marker.prototype.draw.apply(this, arguments);
        this.div.appendChild(this.labelDiv, this.div.firstChild);
        
        if (this.mouseOver === true) {
            this.setLabelVisibility(false);
            this.events.register("mouseover", this, this.onmouseover);
            this.events.register("mouseout", this, this.onmouseout);
        }
        else {
            this.setLabelVisibility(true);
        }
        return this.div;
    }, 

    /** 
     * Method: onmouseover
     * When mouse comes up within the popup, after going down 
     * in it, reset the flag, and then (once again) do not 
     * propagate the event, but do so safely so that user can 
     * select text inside
     * 
     * Parameters:
     * evt - {Event} 
     */
    onmouseover: function (evt) {
    
        if (!this.mouseover) {
            this.setLabelVisibility(true);
            this.mouseover = true;
        }

        if (this.map.getSize().w - this.map.getPixelFromLonLat(this.lonlat).x<50) {
            this.labelDiv.style.marginLeft = (-10-this.icon.size.w)+"px";
        }
        if (this.map.getSize().h - this.map.getPixelFromLonLat(this.lonlat).y<50) {
            this.labelDiv.style.marginTop = (-10-this.icon.size.h)+"px";
        }
        OpenLayers.Event.stop(evt, true);
    },

    /** 
     * Method: onmouseout
     * When mouse goes out of the popup set the flag to false so that
     *   if they let go and then drag back in, we won't be confused.
     * 
     * Parameters:
     * evt - {Event} 
     */
    onmouseout: function (evt) {
        this.mouseover = false;
        this.setLabelVisibility(false);
        this.labelDiv.style.marginLeft = this.labelOffset;
        this.labelDiv.style.marginTop = this.labelOffset;
        OpenLayers.Event.stop(evt, true);
    },

    /** 
     * Method: setLabel
     * Set new label
     * 
     * Parameters:
     * label - {String} 
     */
    setLabel: function (label) {
        this.label= label  ;
        this.labelDiv.innerHTML =  label ;
    },

    /** 
     * Method: setLabelVisibility
     * Toggle label visibility
     * 
     * Parameters:
     * visibility - {Boolean} 
     */
    setLabelVisibility: function (visibility) {
        if (visibility) {
            this.labelDiv.style.display = "block";
        }
        else {
            this.labelDiv.style.display = "none";
        }
    },
    
    /** 
     * Method: getLabelVisibility
     * Get label visibility
     * 
     * Returns:
     *   visibility - {Boolean} 
     */
    getLabelVisibility: function () {
        if (this.labelDiv.style == "none") {
            return false;
        }
        else {
            return true;
        }
    },
    
    CLASS_NAME: "OpenLayers.Marker.Label"
});

