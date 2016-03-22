
var arrowImage = 'styles/images/select_arrow.gif';	
var arrowImageOver = 'styles/images/select_arrow_over.gif';
var arrowImageDown = 'styles/images/select_arrow_down.gif';

var selectBoxIds = 0;
var currentlyOpenedOptionBox = false;
var editableSelect_activeArrow = false;



function selectBox_switchImageUrl()
{
        if(this.src.indexOf(arrowImage)>=0){
                this.src = this.src.replace(arrowImage,arrowImageOver);	
        }else{
                this.src = this.src.replace(arrowImageOver,arrowImage);
        }


}

function selectBox_showOptions()
{
        if(editableSelect_activeArrow && editableSelect_activeArrow!=this){
                editableSelect_activeArrow.src = arrowImage;

        }
        editableSelect_activeArrow = this;

        var numId = this.id.replace(/[^\d]/g,'');
        var optionDiv = document.getElementById('selectBoxOptions' + numId);
        if(optionDiv.style.display=='block'){
                optionDiv.style.display='none';
                if(navigator.userAgent.indexOf('MSIE')>=0)document.getElementById('selectBoxIframe' + numId).style.display='none';
                this.src = arrowImageOver;	
        }else{			
                optionDiv.style.display='block';
                if(navigator.userAgent.indexOf('MSIE')>=0)document.getElementById('selectBoxIframe' + numId).style.display='block';
                this.src = arrowImageDown;	
                if(currentlyOpenedOptionBox && currentlyOpenedOptionBox!=optionDiv)currentlyOpenedOptionBox.style.display='none';	
                currentlyOpenedOptionBox= optionDiv;			
        }
}

function selectOptionValue()
{
        var parentNode = this.parentNode.parentNode;
        var textInput = parentNode.getElementsByTagName('INPUT')[0];
        textInput.value = this.title;	
        this.parentNode.style.display='none';	
        document.getElementById('arrowSelectBox' + parentNode.id.replace(/[^\d]/g,'')).src = arrowImageOver;

        if(navigator.userAgent.indexOf('MSIE')>=0)document.getElementById('selectBoxIframe' + parentNode.id.replace(/[^\d]/g,'')).style.display='none';

}
var activeOption;
function highlightSelectBoxOption()
{
        if(this.style.backgroundColor=='#316AC5'){
                this.style.backgroundColor='';
                this.style.color='';
        }else{
                this.style.backgroundColor='#316AC5';
                this.style.color='#FFF';			
        }	

        if(activeOption){
                activeOption.style.backgroundColor='';
                activeOption.style.color='';			
        }
        activeOption = this;

}

function createEditableSelect(dest,parent)
{

        dest.className='selectBoxInput';		
        var div = document.createElement('DIV');
        div.style.styleFloat = 'left';
        div.style.position = 'absolute';
        div.id = 'selectBox' + selectBoxIds;
        //var parent = dest.parentNode;
        //parent.insertBefore(div,dest);
        parent.appendChild(div);
        div.appendChild(dest);	
        div.className='selectBox';
        div.style.zIndex = 10000 - selectBoxIds;

        var img = document.createElement('IMG');
        img.src = arrowImage;
        img.className = 'selectBoxArrow';

        img.onmouseover = selectBox_switchImageUrl;
        img.onmouseout = selectBox_switchImageUrl;
        img.onclick = selectBox_showOptions;
        img.id = 'arrowSelectBox' + selectBoxIds;

        div.appendChild(img);

        var optionDiv = document.createElement('DIV');
        optionDiv.id = 'selectBoxOptions' + selectBoxIds;
        optionDiv.className='selectBoxOptionContainer';
        optionDiv.style.width = div.offsetWidth+2 + 'px';
        div.appendChild(optionDiv);

        if(navigator.userAgent.indexOf('MSIE')>=0){
                var iframe = document.createElement('<IFRAME src="about:blank" frameborder=0>');
                iframe.style.width = optionDiv.style.width;
                iframe.style.height = optionDiv.offsetHeight + 'px';
                iframe.style.display='none';
                iframe.id = 'selectBoxIframe' + selectBoxIds;
                div.appendChild(iframe);
        }

        if(dest.getAttribute('selectBoxOptions')){
                var options = dest.getAttribute('selectBoxOptions').split(';');
                var optionsTotalHeight = 0;
                var optionArray = new Array();
                for(var no=0;no<options.length;no++){
                    var anOption = document.createElement('DIV');
                    var array = options[no].split(",");
                    if (array.length == 2) {
                        anOption.innerHTML = array[0];
                        anOption.title = array[1];
                    }
                    else {
                        anOption.innerHTML = options[no];
                        anOption.title = options[no];
                    }
                    anOption.className='selectBoxAnOption';
                    anOption.onclick = selectOptionValue;
                    anOption.style.width = optionDiv.style.width.replace('px','')-2  + 'px'; 
                    anOption.onmouseover = highlightSelectBoxOption;
                    optionDiv.appendChild(anOption);	
                    optionsTotalHeight = optionsTotalHeight + anOption.offsetHeight;
                    optionArray.push(anOption);
                }
                if(optionsTotalHeight > optionDiv.offsetHeight){				
                        for(var no=0;no<optionArray.length;no++){
                                optionArray[no].style.width = optionDiv.style.width.replace('px','') - 18 + 'px'; 	
                        }	
                }		
                optionDiv.style.display='none';
                optionDiv.style.visibility='visible';
        }

        selectBoxIds = selectBoxIds + 1;
}	
