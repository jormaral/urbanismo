OpenLayers.Util.extend(OpenLayers.Format.GML.v3.prototype.readers.gml, 
                                    {
                                        "Envelope":function(node, container) {
                                                var obj = {points: new Array()};
                                                this.readChildNodes(node, obj);
                                                if(!container.components) {
                                                    container.components = [];
                                                }
                                                var min = obj.points[0];
                                                var max = obj.points[1];
                                                container.components.push(
                                                    new OpenLayers.Bounds(min.x, min.y, max.x, max.y)
                                                );
                                            },
                                        "pos": function(node, obj) {
                                                var str = this.getChildValue(node).replace(
                                                    this.regExes.trimSpace, ""
                                                );
                                                var coords = str.split(this.regExes.splitSpace);
                                                var point;
                                                if(this.xy) {
                                                    point = new OpenLayers.Geometry.Point(
                                                        coords[0], coords[1], coords[2]
                                                    );
                                                } else {
                                                    point = new OpenLayers.Geometry.Point(
                                                        coords[1], coords[0], coords[2]
                                                    );
                                                }
                                                if (obj.points && obj.points.length > 0) { 
                                                    obj.points = obj.points.concat(point); 
 		                } else { 
                                                    obj.points = [point]; 
 		                } 
                                            },
                                        "lowerCorner": function(node, container) { 
		                var obj = {}; 
		                this.readers.gml.pos.apply(this, [node, obj]); 
 		                container.points = container.points.concat(obj.points[0]); 
		            }, 
                                        "upperCorner": function(node, container) { 
		                var obj = {}; 
		                this.readers.gml.pos.apply(this, [node, obj]); 
 		                container.points = container.points.concat(obj.points[0]); 
		            }
                                    })