var ZoomBar = new Class({
    initialize: function (map) {
        var container = new Element('div', {
            'class': 'zoomBar'
        }).injectInside($(map.div));
        var containerPan = new Element('div', {
            'id': 'container_Pan'
        }).injectInside(container);
        var divSlider = new Element('div', {
            'id': 'zoomBar_slider'
        }).injectInside(container);
        new Element('div', {
            'id': 'knob'
        }).injectInside(divSlider);
        new Element('div',
        {
            'id': 'zoomBar_up',
            'events': {
                'click': function () {
                    this.moveByPx(0, 50);
                } .bind(map)
            }
        }).injectInside(containerPan);

        new Element('div',
        {
            'id': 'zoomBar_down',
            'events': {
                'click': function () {
                    this.moveByPx(0, -50);
                } .bind(map)
            }
        }).injectInside(containerPan);

        new Element('div',
        {
            'id': 'zoomBar_left',
            'events': {
                'click': function () {
                    this.moveByPx(-50, 0);
                } .bind(map)
            }
        }).injectInside(containerPan);

        new Element('div',
        {
            'id': 'zoomBar_right',
            'events': {
                'click': function () {
                    this.moveByPx(50, 0);
                } .bind(map)
            }
        }).injectInside(containerPan);

        new Element('div',
        {
            'id': 'zoomBar_center',
            'events': {
                'click': function () {
                    this.zoomToMaxExtent();
                } .bind(map)
            }
        }).injectInside(containerPan);

        new Element('div',
        {
            'id': 'zoomBar_in',
            'events': {
                'click': function () {
                    this.zoomTo(map.getZoom() + 1);
                } .bind(map)
            }
        }).injectInside(container);
        new Element('div',
        {
            'id': 'zoomBar_out',
            'events': {
                'click': function () {
                    this.zoomTo(map.getZoom() - 1);
                } .bind(map)
            }
        }).injectInside(container);

        var slider = new Slider(divSlider, 'knob', {
            range: [map.getZoomForExtent(map.getMaxExtent()),map.getNumZoomLevels()],
            initialStep: map.getZoom(),//map.getNumZoomLevels() - map.getZoom(),
            wheel: true,
            mode: 'vertical',
            onChange: function (value) {
                if (value) this.zoomTo(this.getNumZoomLevels()-value+this.getZoomForExtent(map.getMaxExtent()));
            } .bind(map)
        });
        slider.set (map.getNumZoomLevels()-map.getZoom());
//        slider.addEvent('change',function (value) {
//                if (value) this.zoomTo(this.getNumZoomLevels()-value+this.getZoomForExtent(map.getMaxExtent()));
//            } .bind(map)
//        );
        map.events.register('zoomend', map, function (evt) {
            this.slider.set (this.map.getNumZoomLevels()+this.map.getZoomForExtent(this.map.getMaxExtent())-this.map.getZoom());
        } .bind({
            'slider': slider, 
            'map': map
        })
        );
    }
}
);