/*
    Document   : PagePrinter
    Author     : Alejandro.Centeno
*/


var PagePrinter=new Class({
    Implements: Events,
    feature:null,
    printProvider:null,
    layout:null,
    initialize:function(printConfig){
        this.feature = new OpenLayers.Feature.Vector(
            OpenLayers.Geometry.fromWKT("POLYGON((-1 -1,1 -1,1 1,-1 1,-1 -1))")
            );
        this.printProvider=printConfig;
        this.layout=printConfig.layouts[0];
    },
    setLayout:function(newLayout){
        //        if(this.printProvider.layout.get("rotation") === false) {
        //            this.setRotation(0, true);
        //        }
        this.setScale(this.scale,null,newLayout);
    },
    setScale: function(scale, units,layout) {
        if (!layout){
            layout=this.layout;
        }
        var bounds =null;
        if (typeof(scale)=="object"){
            bounds = this.calculatePageBounds(scale["value"], units,layout);
        }else{
            bounds = this.calculatePageBounds(scale, units,layout);
        }
         
        var geom = bounds.toGeometry();
        /* var rotation = this.rotation;
        if(rotation != 0) {
            geom.rotate(-rotation, geom.getCentroid());
        }*/
        this.updateFeature(geom, {
            scale: scale,
            layout: layout
        });
    },
    setCenter: function(center) {
        var geom = this.feature.geometry;
        var oldCenter = geom.getBounds().getCenterLonLat();
        var dx = center.lon - oldCenter.lon;
        var dy = center.lat - oldCenter.lat;
        geom.move(dx, dy);
        this.updateFeature(geom, {
            center: center
        });
    },
    setRotation: function(rotation, force) {
        if(force || this.layout["rotation"] === true) {
            var geom = this.feature.geometry;
            geom.rotate(this.layout["rotation"] - rotation, geom.getCentroid());
            this.updateFeature(geom, {
                rotation: rotation
            });
        }
    },
   /** api: method[fit]
     *  :param fitTo: :class:`GeoExt.MapPanel` or ``OpenLayers.Map`` or ``OpenLayers.Feature.Vector``
     *      The map or feature to fit the page to.
     *  :param options: ``Object`` Additional options to determine how to fit
     *
     *  Fits the page layout to a map or feature extent. If the map extent has
     *  not been centered yet, this will do nothing.
     * 
     *  Available options:
     *
     *  * mode - ``String`` How to calculate the print extent? If "closest",
     *    the closest matching print extent will be chosen. If "printer", the
     *    chosen print extent will be the closest one that can show the entire
     *    ``fitTo`` extent on the printer. If "screen", it will be the closest
     *    one that is entirely visible inside the ``fitTo`` extent. Default is
     *    "printer".
     * 
     */
    fit: function(fitTo, options) {
        options = options || {};
        var map = fitTo, extent;
        if(fitTo instanceof OpenLayers.Map) {
            map = map;
        } else if(fitTo instanceof OpenLayers.Feature.Vector) {
            map = fitTo.layer.map;
            extent = fitTo.geometry.getBounds();
        }
        if(!extent) {
            extent = map.getExtent();
            if(!extent) {
                return;
            }
        }
        this._updating = true;
        var center = extent.getCenterLonLat();
        this.setCenter(center);
        var units = map.getUnits();
        var scale = this.printProvider.scales[0];
        var closest = Number.POSITIVE_INFINITY;
        var mapWidth = extent.getWidth();
        var mapHeight = extent.getHeight();
        this.printProvider.scales.each(function(rec) {
            var bounds = this.calculatePageBounds(rec["value"], units);
            if (options.mode == "closest") {
                var diff = 
                    Math.abs(bounds.getWidth() - mapWidth) +
                    Math.abs(bounds.getHeight() - mapHeight);
                if (diff < closest) {
                    closest = diff;
                    scale = rec;
                }
            } else {
                /*var contains = options.mode == "screen" ?
                    !extent.containsBounds(bounds) :
                    bounds.containsBounds(extent);
                if (contains || (options.mode == "screen" && !contains)) {
                    scale = rec;
                }
                return contains;*/
                var contains = extent.containsBounds(bounds);
                if (contains) {
                    scale = rec;
                }
            }
        }, this);
        bug.log('Escala '+ scale["value"]);
        this.setScale(scale, units);
        delete this._updating;
        this.updateFeature(this.feature.geometry, {
            center: center,
            scale: scale
        });
        return scale;
    },
    calculatePageBounds: function(scale, units,layout) {
        if (!layout){
            layout=this.layout;
        }
        var s = scale;
        var f = this.feature;
        var geom = this.feature.geometry;
        var center = geom.getBounds().getCenterLonLat();
	
        var size = layout["map"];
        var units = units ||
        (f.layer && f.layer.map && f.layer.map.getUnits()) ||
        "dd";
        var unitsRatio = OpenLayers.INCHES_PER_UNIT[units];
        var w = size.width / 72 / unitsRatio * s / 2;
        var h = size.height / 72 / unitsRatio * s / 2;
	       
        return new OpenLayers.Bounds(center.lon - w, center.lat - h,
            center.lon + w, center.lat + h);
    },
    updateFeature: function(geometry, mods) {
        var f = this.feature;
        geometry.id = f.geometry.id;
        f.geometry = geometry;
	       
        if(!this._updating) {
            var modified = false;
            for(var property in mods) {
                if(mods[property] === this[property]) {
                    delete mods[property];
                } else {
                    this[property] = mods[property];
                    modified = true;
                }
            }
            Object.extend(this, mods);
            
            f.layer && f.layer.drawFeature(f);
            modified && this.fireEvent("change", [this, mods]);

        }
    }
            
});