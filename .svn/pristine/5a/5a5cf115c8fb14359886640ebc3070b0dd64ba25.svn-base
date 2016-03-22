/**
 * MooScroll beta [for mootools 1.2]
 * @author Jason J. Jaeger | greengeckodesign.com
 * @version 0.59
 * @license MIT-style License
 *			Permission is hereby granted, free of charge, to any person obtaining a copy
 *			of this software and associated documentation files (the "Software"), to deal
 *			in the Software without restriction, including without limitation the rights
 *			to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *			copies of the Software, and to permit persons to whom the Software is
 *			furnished to do so, subject to the following conditions:
 *	
 *			The above copyright notice and this permission notice shall be included in
 *			all copies or substantial portions of the Software.
 *	
 *			THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *			IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *			FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *			AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *			LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *			OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *			THE SOFTWARE.
 *	
 *  @changeLog_________________________________________________________________________________
 *  
 *  Jan 3nd 2008
 *  JJJ - Incremented version to 0.59
 *  	- Finished adding smoothMooScroll.toAnchor and smoothMooScroll.toMooScrollArea options
 *  	  Thanks for Ivan Gascon and David Fink for the suggestions and contribution.
 *  
 *  Dec 28th 2008
 *  JJJ - Adding smoothMooScroll option
 *  
 *  Dec 27th 2008
 *  JJJ - Adding smoothMooScroll option (customized version of smoothscroll) and setSlider function
 *  
 *  Dec 24th 2008
 *  JJJ - Added refresh function to main MooScroll class (thanks to David's post on the blog for the suggestion)
 *  
 *  Dec 7th 2008
 *  JJJ - Incremented version to 0.58
 *  	- Implemented bug fixes (for ie6 and webkit) submitted by Mattia Placido Opizzi of moikano.it
 *  
 *  Nov 8th 2008
 *  JJJ	- Incremented version to 0.57.2
 *  	- Added line to unGrey method to bring full opacity back after a scollbar had been greyed out and then brought back
 *  
 *  Nov 8th 2008
 *  JJJ	- Incremented version to 0.57
 *  	- Fixed bug related to the disabledOpacity option (Thanks to Simon Terres at gmx.net) 
 *  
 *  Sept 21st 2008
 *  JJJ	- Incremented version to 0.56
 *  	- Added restrictedBrowsers option. Made Opera 9.25 or lower, Safari 2 or lower, and iPhone/iPod Touch the default restricted browsers
 *  	- Fixed small bug introduced with previous webkit fix which prevented webkit browsers from scrolling the document down when the MooScroll
 *  		area is all the way scrolled down and the end-user is still scrolling via mousewheel or keyboard.
 *  
 *  Sept 21st 2008
 *  JJJ - Incremented version to 0.55
 *  	- Fixed jitter bug for webkit browsers
 *  	- Made arrow keys and space key work in Firefox, Opera, Safari, and Chrome
 *  
 *  Sept 20th 2008
 *  JJJ - Incremented version to 0.54
 *  	- Disabled MooScroll for iphone/ipod touch until I can get an iphone to test on 
 *  	- Factored in border height when setting content area height
 *  
 *  Sept 6th 2008
 *  JJJ - Incremented version to 0.53
 *  	- Changed initialize function so that instead of wrapping the original element, an empty div (contentEl) adopts the children and is 
 *  		then injected into the the original element (parentEl). This way all the stlying of the original element is perfectly preserved.
 *  		An extra element then wraps the children of the contentDiv and the padding from the original element is transfered to 
 *  		this padding element (paddingEl).
 *  	- Added refresh function
 *  	- Added fullWindowMode option
 *  	- Added loadContent function
 *  
 *  August 31st 2008
 *  JJJ - Made the wrapper element absorb certain styles from the parentEl so that it works in layouts where the scrolled
 *  		element is positioned (thanks to Bob Ralian for suggesting and contributing to this feature!)
 *  		This allowed the styles for the parentEl (.scroll) to be separated out to the example.css file (from mooScroll.css)
 *  		in the example (where it should be). Also this makes appling MooScroll to a scrollable area in a pre-existing design easier.  
 *  	- Fixed bug which kept the scrollHandle from going all the way to the bottom sometimes when scrolling via the down button
 *  	- Moved some code from init function to dedicated functions (setHandleHeight and greyOut) in order to prepare to add refresh function
 *  	- Ran across some Mootools 1.2 bugs:
 *  		a.) In Firefox 2 and 3  element.getStyle('width') reports the actual element dimension in px even when the width is set in percent in the CSS
 *  		b.) In Firefox 3 element.getScrollSize().y is not including padding (this means that in FF3 you loose any bottom padding 
 *  			you may have set on the scollElement 
 *  	- Added unGrey function
 *  
 *  August 9th 2008
 *  JJJ - Incremented version to 0.52
 *  	- Disabled for Safari 2 to atleast keep it from crashig or looking messed up due to bugs that I don't currently have time to fix, Sorry Safari 2 users :(
 *  
 *  August 2nd 2008
 *  JJJ	- Incremented version to 0.51
 *  	- Made tweaks for IE6's poor CSS support
 *  	- Wrapped scroll controls in wrapper div so positioning can be easily tweaked via CSS
 *  	- Made scrollHandle position update when scroll area is scrolled via tabbing through links
 *  	- Made Scroll area scroll via arrow keys when that scroll area (or something in it) is in focus
 *  	- Made page scroll up if you are scrolling up through a scroll area via the mousewheel and you get to 
 *  	  the top of the scroll area but you keep scrolling up with the wheel (same with down).
 *  	- Made greyed out scroll controlls non-functional (css hover overs and all)
 *  	- Added opacity of greyed out scroll controls option (disabledOpacity)
 *  	- Added refresh option
 *  
 *  July 26th 2008
 *  JJJ - Incremented version to 0.50
 *  	- Improved class I had previously written to prepare it for public release:
 *  		* Updated for MooTools 1.2
 *  		* Made able to have multiple instances on a page
 *  
 *  
 *  
 *  TO DO:
 *  --------------------
 *  1. Add horizontal scrollbar ability
 *  2. Add Callback functions
 *  
 **/

var MooScroll = new Class({
	Implements: Options,
	options: {
        selector: '.scroll',
		increment:30,
		upBtnClass:'upBtn',
		downBtnClass:'downBtn',
		scrollBarClass:'scrollBar',
		scrollHandleClass:'scrollHandle',
		scrollHandleBGClass:'scrollHandleBG',	
		scrollHandleTopClass:'scrollHandleTop',
		scrollHandleMiddleClass:'scrollHandleMiddle',			
		scrollHandleBottomClass:'scrollHandleBottom',
		scrollControlsYClass: 'scrollControlsY',
		handleOpacity:1,
		handleActiveOpacity:0.85,
		disabledOpacity:0.5,
		fullWindowMode:false,
		smoothMooScroll:{
			toAnchor:true,
			toMooScrollArea:true
		},
		restrictedBrowsers:[Browser.Engine.presto925,Browser.Platform.ipod,Browser.Engine.webkit419]//Opera 9.25 or lower, Safari 2 or lower, iPhone/iPod Touch
    },
	
	initialize: function(options){
		//don't run in restricted browsers
		if(this.options.restrictedBrowsers.contains(true)){return;}
		
		this.setOptions(options);		
		this.mooScrollAreas = [];
		this.windowFxScroll = new Fx.Scroll(document.window,{wait: false});

		$(document.body).getElements(this.options.selector).each(function(item,index){
			var scrollArea = new MooScrollArea(this.options, item,this.windowFxScroll);
			this.mooScrollAreas.include(scrollArea);
			if(this.options.smoothMooScroll.toAnchor || this.options.smoothMooScroll.toMooScrollArea){				
				this.smoothMooScroll = new SmoothMooScroll({toAnchor:this.options.smoothMooScroll.toAnchor,toMooScrollArea:this.options.smoothMooScroll.toMooScrollArea},scrollArea.contentEl,this.windowFxScroll);
			}
		}.bind(this));
	},
	
	loadContent:function(content){
		this.mooScrollAreas.each(function(item,index){
			item.loadContent(content);
		});
	},
	
	refresh:function(){
		this.mooScrollAreas.each(function(item,index){
			item.refresh();
		});
	},
	
	setSlider:function(v){
		this.mooScrollAreas.each(function(item,index){
			item.setSlider(v);			
		});
	}
});

var MooScrollArea = new Class({
	Implements: Options,		

	initialize: function(options, parentEl, windowFxScroll){
		this.windowFxScroll = windowFxScroll;
		this.setOptions(options);
		this.parentEl = parentEl.setProperty('rel', 'MooScrollArea');
		this.viewPort = {x:$(window).getSize().x,y:$(window).getSize().y};
		this.parentElPadding = this.parentEl.getStyles('padding-top','padding-right','padding-bottom','padding-left');
		this.paddingHeight = parseFloat(this.parentEl.getStyle('padding-top'))+parseFloat(this.parentEl.getStyle('padding-bottom'));
		this.paddingWidth = parseFloat(this.parentEl.getStyle('padding-left'))+parseFloat(this.parentEl.getStyle('padding-right'));
		
		this.contentEl = new Element('div',{'class':'contentEl'}).adopt(this.parentEl.getChildren()).inject(this.parentEl,'top');
		this.parentEl.setStyle('overflow', 'hidden').setStyles({
			'padding':0, 
			width:parseFloat(this.parentEl.getStyle('width')) + this.paddingWidth,	
			height:parseFloat(this.parentEl.getStyle('height')) +  this.paddingHeight	
		});
		
		this.borderHeight = parseFloat(this.parentEl.getStyle('border-top-width'))+parseFloat(this.parentEl.getStyle('border-bottom-width'));
		this.contentEl.setStyles({'height':this.parentEl.getSize().y-this.borderHeight, overflow:'hidden','padding':0});
		this.paddingEl = new Element('div',{'class':'paddingEl'}).adopt(this.contentEl.getChildren()).inject(this.contentEl,'top').setStyles(this.parentElPadding);
		
		if(this.options.fullWindowMode){
			//turn off overflow for html element here so non-javascript users can still scroll
			$(document).getElement('html').setStyle('overflow','hidden');
			this.parentEl.setStyles({ 'height':'100%', 'width':'100%', 'position':'absolute' });
			this.contentEl.setStyles({ 'height':'100%', 'width':'100%', 'position':'absolute'});		
		}
		
		//Add Control Elements
		this.scrollControlsYWrapper = new Element('div', {	'class': this.options.scrollControlsYClass	}).inject(this.parentEl,'bottom');
		this.upBtn = new Element('div', {	'class': this.options.upBtnClass	}).inject(this.scrollControlsYWrapper,'bottom');
		this.downBtn = new Element('div', {	'class': this.options.downBtnClass	}).inject(this.scrollControlsYWrapper,'bottom');
		this.scrollBar = new Element('div', {	'class': this.options.scrollBarClass	}).inject(this.scrollControlsYWrapper,'bottom');
		this.scrollHandle = new Element('div', {	'class': this.options.scrollHandleClass	}).inject(this.scrollBar,'inside');
		this.scrollHandleTop = new Element('div', {	'class': this.options.scrollHandleTopClass }).inject(this.scrollHandle,'inside');
		this.scrollHandleBG = new Element('div', {	'class': this.options.scrollHandleBGClass }).inject(this.scrollHandle,'inside');
		this.scrollHandleMiddle = new Element('div', {	'class': this.options.scrollHandleMiddleClass }).inject(this.scrollHandle,'inside');
		this.scrollHandleBottom = new Element('div', {	'class': this.options.scrollHandleBottomClass }).inject(this.scrollHandle,'inside');
		this.coverUp = new Element('div').inject(this.scrollControlsYWrapper,'bottom');
		
		this.fixIE6CSSbugs();
		
		this.overHang = this.paddingEl.getSize().y - this.parentEl.getSize().y   ;
		
		this.setHandleHeight();
		
		if(this.overHang <=0){this.greyOut();return;}

		this.initSlider();		
		
		this.parentEl.addEvents({
			'mousewheel': function(e){
				e = new Event(e).stop();							
				// Mousewheel UP 
				if (e.wheel > 0) { this.scrollUp(true); }				
				// Mousewheel DOWN
				else if (e.wheel < 0) { this.scrollDown(true); }			
			}.bind(this),
			'keydown': function(e){	
				if (e.key === 'up') { 
					e = new Event(e).stop();
					this.scrollUp(true); 					
				} 						
				else if (e.key === 'down' || e.key === 'space') { 
					e = new Event(e).stop();
					this.scrollDown(true);
				}			
			}.bind(this),			
			
			'click':function(e){				
				this.hasFocus = true;				
				this.hasFocusTimeout = (function(){
					$clear(this.hasFocusTimeout);
					this.hasFocus = true;
				}.bind(this)).delay(50);				
			}.bind(this)		
			
		});
		
		this.contentEl.addEvents({
			'scroll': function(e){
				this.slider.set(this.contentEl.getScroll().y);			
			}.bind(this)
		})
		
		this.scrollHandle.addEvents({
				'mousedown': function(e){					
					this.scrollHandle.addClass(this.options.scrollHandleClass +'-Active').setStyle('opacity',this.options.handleActiveOpacity);
				}.bind(this)
		});
		
		document.addEvents({
			'mouseup': function(e){					
				this.scrollHandle.removeClass(this.options.scrollHandleClass +'-Active').setStyle('opacity',this.options.handleOpacity);
				this.upBtn.removeClass(this.options.upBtnClass +'-Active');
				this.downBtn.removeClass(this.options.downBtnClass +'-Active');
			}.bind(this),
			
			'keydown':function(e){				
				if( (this.hasFocus ||this.options.fullWindowMode) && (e.key === 'down' || e.key === 'space' ||e.key === 'up') ){	this.parentEl.fireEvent('keydown',e);		}
			}.bind(this),
			
			'click':function(e){				
				this.hasFocus = false;									
			}.bind(this)			
		});
		
		window.addEvent('resize', function() {			

		$clear(this.refreshTimeout);
			if (this.options.fullWindowMode) {
				this.refreshTimeout = (function(){
					$clear(this.refreshTimeout);
					if (this.viewPort.x != $(window).getSize().x || this.viewPort.y != $(window).getSize().y) {
						this.refresh();
						this.viewPort.x = $(window).getSize().x;
						this.viewPort.y = $(window).getSize().y;
					}
				}.bind(this)).delay(250);
			}	
		}.bind(this));
		
		this.upBtn.addEvents({
				'mousedown': function(e){					
					$clear(this.upInterval);
					$clear(this.downInterval);
					this.upInterval = this.scrollUp.periodical(10,this);
					this.upBtn.addClass(this.options.upBtnClass +'-Active');					
				}.bind(this),
				
				'mouseup': function(e){
					$clear(this.upInterval);
					$clear(this.downInterval);					
				}.bind(this),
				
				'mouseout': function(e){
					$clear(this.upInterval);
					$clear(this.downInterval);
				}.bind(this)
		});
			
		this.downBtn.addEvents({
				'mousedown': function(e){
					$clear(this.upInterval);
					$clear(this.downInterval);
					this.downInterval = this.scrollDown.periodical(10,this);
					this.downBtn.addClass(this.options.downBtnClass +'-Active');
				}.bind(this),
				
				'mouseup': function(e){
					$clear(this.upInterval);
					$clear(this.downInterval);
				}.bind(this),
				
				'mouseout': function(e){
					$clear(this.upInterval);
					$clear(this.downInterval);
				}.bind(this)
		});
		
		
    },
	
	initSlider:function(){
		this.slider = new Slider(this.scrollBar, this.scrollHandle, {	
			range:[0, Math.round(this.overHang )],	
			mode: 'vertical',	
			onChange: function(step,e){
				this.contentEl.scrollTo(0, step);				
				this.webKitKludge(step);				 
			}.bind(this)
		}).set(0);
	},
	
	webKitKludge:function(step){
		if (!Browser.Engine.webkit) {return;}
		//if scrollHandle is withing 1% of the bottom, kick it down that last little bit since webkit browsers seem to
		//have trouble getting it that last little bit sometimes (varies with amount of content.. probably due to rounding)
		if(this.step > step){ 
			this.step = step;	
			return; 
		}			
		$clear(this.sliderTimeout);
		this.sliderTimeout = (function(){ 
			$clear(this.sliderTimeout);						
			var onePercent = (1*this.paddingEl.getSize().y)/100;			
			if((onePercent + step) >= this.overHang ){						
				if(this.paddingElTopMargin == null){this.paddingElTopMargin = parseFloat(this.paddingEl.getStyle('margin-top'));}	
				this.paddingEl.setStyle('margin-top', this.paddingElTopMargin -onePercent);	
				if(!this.scrollHandleTopMargin){this.scrollHandleTopMargin = parseFloat(this.scrollHandle.getStyle('margin-top'));}
				this.scrollHandle.setStyle('margin-top',this.scrollHandleTopMargin+2);				
				this.contentEl.scrollTo(0, this.overHang );					
				this.step = this.overHang ;	
	
			}else{
				this.paddingEl.setStyle('margin-top', this.paddingElTopMargin );
				this.scrollHandle.setStyle('margin-top',this.scrollHandleTopMargin);
				this.contentEl.scrollTo(0, step);
				this.step = step;
			}	
		}.bind(this)).delay(10);
		
	},

	scrollUp:function(scrollPageWhenDone){		
		var target = this.contentEl.getScroll().y - 30;// this.options.increment;
		this.slider.set(target);
		if(this.contentEl.getScroll().y <= 0 && scrollPageWhenDone){
			document.window.scrollTo(0 ,document.window.getScroll().y - this.options.increment );
		}
	},
	
	scrollDown:function(scrollPageWhenDone){		
		var target = this.contentEl.getScroll().y + this.options.increment;
		this.slider.set(target);
		var onePercent = (1*this.paddingEl.getSize().y)/100;
		var atBottom = 	(this.paddingEl.getSize().y - this.parentEl.getSize().y)<= (this.contentEl.getScroll().y + onePercent);	
		if(atBottom && scrollPageWhenDone){
			document.window.scrollTo(0 ,document.window.getScroll().y + this.options.increment );
		}
	},
	
	fixIE6CSSbugs:function(){
		//fix some CSS bugs for IE6
		if(Browser.Engine.trident4){			
			this.parentEl.setStyle('height',this.parentEl.getStyle('height'));
			this.contentEl.setStyle('height',this.parentEl.getStyle('height'));			
			var top = this.scrollBar.getStyle('top').toInt();
			var bottom = this.scrollBar.getStyle('bottom').toInt();
			var parentHeight = this.parentEl.getSize().y - this.borderHeight;
			this.scrollControlsYWrapper.setStyles({'height':parentHeight});
			this.scrollBar.setStyles({'height':parentHeight-top-bottom });
		}
	},
	
	setHandleHeight:function(){		
		var handleHeightPercent = (100 - ((this.overHang*100)/this.paddingEl.getSize().y));		
		this.handleHeight = ((handleHeightPercent*this.parentEl.getSize().y)/100) - (this.scrollHandleTop.getSize().y + this.scrollHandleBottom.getSize().y );
		if((this.handleHeight + this.scrollHandleTop.getSize().y + this.scrollHandleBottom.getSize().y ) >= this.scrollBar.getSize().y){
			this.handleHeight-=( this.scrollHandleTop.getSize().y + this.scrollHandleBottom.getSize().y )*2;
		}
		if(this.scrollHandle.getStyle('min-height') && this.handleHeight < parseFloat(this.scrollHandle.getStyle('min-height'))){
			this.handleHeight = parseFloat(this.scrollHandle.getStyle('min-height')) + this.scrollHandleBottom.getSize().y + this.scrollHandleTop.getSize().y;
		}	
		this.scrollHandle.setStyles({'height':this.handleHeight});
	},
	
	greyOut:function(){
		this.scrollHandle.setStyles({'display':'none'});
		this.upBtn.setStyles({'opacity':this.options.disabledOpacity});
		this.scrollControlsYWrapper.setStyles({opacity:this.options.disabledOpacity});
		this.downBtn.setStyles({'opacity':this.options.disabledOpacity});
		this.scrollBar.setStyles({'opacity':this.options.disabledOpacity});				
		this.coverUp.setStyles({'display':'block','position':'absolute','background':'white','opacity':0.01,'right':'0','top':'0','width':'100%','height':this.scrollControlsYWrapper.getSize().y});
	},
	
	unGrey:function(){
		this.scrollHandle.setStyles({'display':'block','height':'auto'});
		this.scrollControlsYWrapper.setStyles({opacity:1});
		this.upBtn.setStyles({'opacity':1});
		this.downBtn.setStyles({'opacity':1});
		this.scrollBar.setStyles({'opacity':1});		
		this.coverUp.setStyles({'display':'none','width':0,	'height':0	});
		this.setHandleHeight();
	},
	
	loadContent:function(content){
		this.slider.set(0);
		this.paddingEl.empty().set('html',content);	
		this.refresh();
	},
	
	refresh:function(){
		var scrollPercent = Math.round(((100* this.step)/this.overHang));
		if(this.options.fullWindowMode){
			var windowSize = $(window).getSize();
			this.parentEl.setStyles({ width:'100%',height:'100%'});
		}
		this.fixIE6CSSbugs();		
		this.overHang = this.paddingEl.getSize().y - this.parentEl.getSize().y   ;
		this.setHandleHeight();
		if(this.overHang <= 0){
			this.greyOut();
			return;
		}else{
			this.unGrey();
		}
		this.scrollHandle.removeEvents();		
		var newStep = Math.round((scrollPercent*this.overHang)/100);
		this.initSlider();
		this.slider.set(newStep);
		
		//another IE6 kludge
		if (Browser.Engine.trident4) {this.scrollHandleBG.setStyle('height','0').setStyle('height','100%');}
		
		if(this.options.smoothMooScroll.toAnchor || this.options.smoothMooScroll.toMooScrollArea){				
			this.smoothMooScroll = new SmoothMooScroll({toAnchor:this.options.smoothMooScroll.toAnchor,toMooScrollArea:this.options.smoothMooScroll.toMooScrollArea},this.contentEl,this.windowFxScroll);
		}
	},
	
	setSlider:function(v){
		if(v =='top'){
			this.slider.set(0);
		}else if(v=='bottom'){
			this.slider.set('100%');
		}else{
			this.slider.set(v);
		}			
	}
 
 
});


var SmoothMooScroll = new Class({
	Extends: Fx.Scroll,
	initialize: function(options, context, windowFxScroll){
		this.setOptions(options);
		this.windowFxScroll = windowFxScroll;
		this.context = context;
		context = context || document;
		this.context = context;
		var doc = context.getDocument(), win = context.getWindow();		
		this.parent(context, options);
		
		this.links = (this.options.links) ? $$(this.options.links) : $$(doc.links);
		var location = win.location.href.match(/^[^#]*/)[0] + '#';
		this.links.each(function(link){
			if (link.href.indexOf(location) != 0) {	return;	}
			var anchor = link.href.substr(location.length);
			if (anchor && $(anchor) && $(anchor).getParents().contains($(this.context))) {
				this.useLink(link,anchor, true);
			}else if(anchor && $(anchor) && !this.inMooScrollArea($(anchor))){
				this.useLink(link,anchor, false);
			}
		}, this);
		if (!Browser.Engine.webkit419) this.addEvent('complete', function(){
			win.location.hash = this.anchor;
		}, true);
	},
	
	inMooScrollArea:function(el){
		return el.getParents().filter(function(item, index){return item.match('[rel=MooScrollArea]');}).length > 0;
	},
	
	putAnchorInAddressBar:function(anchor){
		window.location.href = "#" + anchor;      
	},

	useLink: function(link, anchor, inThisMooScrollArea){		
		link.removeEvents('click');
		link.addEvent('click', function(event){			
			if(!anchor || !$(anchor)){return;}			
			this.anchor = anchor;
			if (inThisMooScrollArea) {
				if(this.options.toMooScrollArea && this.options.toAnchor){
					this.windowFxScroll.toElement(this.context.getParent()).chain(function(item, index){				
						this.toElement(anchor).chain(function(){	this.putAnchorInAddressBar(anchor);	}.bind(this));				
					}.bind(this));
				}else if(this.options.toMooScrollArea){
					this.windowFxScroll.toElement(this.context.getParent()).chain(function(){	this.putAnchorInAddressBar(anchor);	}.bind(this));
				}else if(this.options.toAnchor){
					this.toElement(anchor).chain(function(){	this.putAnchorInAddressBar(anchor);	}.bind(this));	
				}				
			}else{
				this.windowFxScroll.toElement(anchor).chain(function(){	this.putAnchorInAddressBar(anchor);	}.bind(this));     
			}
			event.stop();		
		}.bind(this));
	}

});