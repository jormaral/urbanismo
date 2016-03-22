var userAdministrationSelectedUser; //id del usuario actualmente seleccionado
var userAdministrationGrid; //Grid de usuarios de gestion de usuarios
var formUserAdministration; //Formuario de usuarios de gestion de usuarios
var idUserAdministrationSelected = -1; //indica cual es el id del usuario actualmente seleccionado

function initUserAdministration(){
	//$('LeftSideColumn').addEvent('OnResize',resizeUserAdministration);	
	//$('LeftSideColumn').ResizeEvent = resizeUserAdministration;
	
    $('gridUsuarios').addClass('panelWaiting');
    warnNoUserSelected();

    // Setup user grid
    var cmu = [
    {
        header: "Usuario",
        dataIndex: 'nombreUsuario',
        dataType:'string',
        width: parseInt($('LeftSideColumn').getStyle('width')) - 40
    }];

    //tamano original
    //width: 201,
    //height: 450
    userAdministrationGrid = new omniGrid('gridUsuarios', {
        columnModel: cmu,
        url: 'GestionConsola?wsName=GET_LISTA_USUARIOS',
        pagination:false,
        serverSort:false,
        showHeader: true,
        alternaterows: true,
        sortHeader:true,
        resizeColumns:false,
        multipleSelection:false,
        width: '100%',//parseInt($('LeftSideColumn').getStyle('width')) - 40,
        height: parseInt($('LeftSideColumn').getStyle('height')) // - 60
    });
    
    userAdministrationGrid.addEvent('click', onSelectUsuario);
    $('gridUsuarios').removeClass('panelWaiting');

    //Botones anadir y eliminar
    // No permitir borrar usuarios hasta que no se haya seleccionado un usuario en el grid    
    $('AdminUserbtnAnadir').addEvent('click', onNuevoUsuarioClick);    
    $('AdminUserbtnEliminar').addEvent('click', onBorrarUsuarioClick);
    enableControl('AdminUserbtnAnadir');
    disableControl('AdminUserbtnEliminar');
    
    /* Botones de guardar y cancelar
     *  - Como arrancamos la pantalla de usuarios sin seleccionar ningun usuario
     *    tenemos que deshabilitar los botones de guardar y cancelar
     */
    $('AdminUserbtnGuardar').addEvent('click', onGuardarClick);
    $('AdminUserbtnCancelar').addEvent('click', onCancelarClick);
    disableControl('AdminUserbtnGuardar');
    disableControl('AdminUserbtnCancelar');   
    
    $('LeftSideColumn').fade(1);
}

/*******************************************************************************
 * User management
 ******************************************************************************/

function onSelectUsuario(evt){		
    var idUsuario = evt.target.getDataByRow(evt.row).idUsuario;
    userAdministrationSelectedUser = idUsuario;
    
    if (idUsuario)
    {
    	if (idUsuario != idUserAdministrationSelected){
    		idUserAdministrationSelected = idUsuario;
        	
        	removeWarnNoUserSelected();
        	//selectUserTab(); //selecciono el tab de usuario
        	
        	formUserAdministration = new FormularioUsuario();
        	formUserAdministration.display(idUsuario);
            // como tenemos un usuario seleccionado habilitamos el boton de borrado
            // de usuarios
            enableControl('AdminUserbtnEliminar');
    	}    	    	
    }
    else 
    {
        //como no tenemos ningun usuario seleccionado, deshabilitamos
        // el boton de borrado de usuarios
        disableControl('AdminUserbtnEliminar');

        warnNoUserSelected();        
    }
}

function onGuardarClick(){
	
	if ($('AdminUserbtnGuardar').disabled == false){
		var result = formUserAdministration.saveUserData();

	    if(result){
	    	
	        refreshUserList();
	        warnNoUserSelected();
	        
	        disableControl('AdminUserbtnGuardar');
	        disableControl('AdminUserbtnCancelar');   
	        
	        idUserAdministrationSelected = '';
	    }
	}	    
}

function onCancelarClick(){
	if ($('AdminUserbtnCancelar').disabled == false){
		idUserAdministrationSelected = -1;
		
		userAdministrationGrid.refresh();
		warnNoUserSelected();
		
		disableControl('AdminUserbtnGuardar');
	    disableControl('AdminUserbtnCancelar');
	}	
}

function onNuevoUsuarioClick(button, grid){
	if ($('AdminUserbtnAnadir').disabled == false){
		userAdministrationGrid.refresh();
		
		removeWarnNoUserSelected();
		//selectUserTab(); //selecciono el tab de usuario
		
		formUserAdministration = new FormularioUsuario();
		formUserAdministration.display();   
	}
	 
}

function onBorrarUsuarioClick(button, grid){
	if ($('AdminUserbtnEliminar').disabled == false){
		if (userAdministrationGrid.getSelectedIndices() && userAdministrationGrid.getSelectedIndices().length > 0){
	        this.idUsuario = userAdministrationGrid.getDataByRow(userAdministrationGrid.getSelectedIndices()[0]).idUsuario;
	        this.nombreUsuario = userAdministrationGrid.getDataByRow(userAdministrationGrid.getSelectedIndices()[0]).nombreUsuario;
	        var msg = '';
	        msg = '&iquest;Desea eliminar el usuario '+ this.nombreUsuario+'?';
	        this.confirmmsg = new PopupMsg(msg, {
	            type: 'yesno'
	        });
	        this.confirmmsg.addEvent('close', onBorrarUsuarioConfirmClose.bind(this));
	    }
	}    
}

function onBorrarUsuarioConfirmClose(){
    if (this.confirmmsg.getResult() === 'yes') {
        var result = runService({
            'wsName': 'BORRAR_USUARIO',
            'idUsuario': this.idUsuario
        });
        if(result==="DIARY_DEPENDENCY"){
            var msg = '';
            msg = 'No se puede Suspender el usuario por dependencia de datos\n &iquest;Desea borrar todos los roles del usuario '+ this.nombreUsuario+'?';
            this.confirmmsg = new PopupMsg(msg, {
                type: 'yesno'
            });
            this.confirmmsg.addEvent('close', manageDiaryDependendy.bind(this));
        }else{
            msg = '';
            if (result==='OK') {
                msg = 'Se ha eliminado el usuario';
            } else {
                msg = 'Se ha producido un error al eliminar el usuario';
                if (result){
                    msg += ': '+ result;
                }
            }       
            new PopupMsg(msg);
            
            refreshUserList();
            warnNoUserSelected();
        }
        
    }
}

function manageDiaryDependendy(){
    if (this.confirmmsg.getResult() === 'yes'){
        var userData = {
            'isNew': !Boolean($('idUsuario').value),
            'id': $('idUsuario').value,
            'nombre': $('txtNombre').value,
            'password': $('txtPass').value,
            'correo': $('txtEmail').value,
            'DNI': $('txtDNI').value,
            'passwordConfirm': $('txtPassRepeat').value  //lo enviamos al servidor para que se vuelva a comprobar
        };
        var rl = [];
        userData.roles = rl;
        var result = runService({
            'wsName': 'GUARDAR_USUARIO',
            'userData': JSON.encode(userData)
        });
        var msg = "";
        if (result === "OK") {
            msg ="Datos modificados correctamente";
        } else {
            msg = "Error al modificar los datos";
            if (result !== "null" && result !== ""){
                msg += ' '+ result;
            }

        }
        new PopupMsg(msg);
        refreshUserList();
    }
}

function warnNoUserSelected(){
	$('mainColumn').setOpacity(0);
}

function removeWarnNoUserSelected(){
	$('mainColumn').fade(1);
}

function selectUserTab(){
	$('panelDatosUsuario').setStyle('display', 'block'); //setOpacity(100);
	$('panelDatosRolUsuario').setStyle('display', 'none'); //setOpacity(0);
	
	$('tabDatos').set('class','selected');
	$('tabRoles').set('class','');
}

function refreshUserList(){
	userAdministrationGrid.loadData();
}

function checkUserNameExists(username){
    var result = runService({
        'wsName': 'EXISTE_NOMBRE_USUARIO',
        'username': username
    });
    return result == 'S';
}

function getStoredUserData(idUser){
	var result = runServiceJson({'wsName':'GET_DATOS_USUARIO_JSON','idUsuario': idUser});	 
    return result;
}



    


/*******************************************************************************
* Formulario de datos del usuario
******************************************************************************/
var FormularioUsuario = new Class({
    Implements: [Events],

    /*
 * Variables privadas
 */
    // identificador del usuario cargado en el formulario.
    // Si se trata de un usuario nuevo el esta variable contendra null
    userId: null,
    rolelist: null, // mooList que contiene la lista de roles
    popuproles: null, //Squeezebox que se usa como dialogo de seleccion de rol


    // API
    display: function(idUser)
    {
    	this.userId = idUser;
    	
		//Setup user data
		if (idUser){
			result = getStoredUserData(this.userId);	    
			if (result && result.nombre)
			{
	    		$('txtNombre').set('value',result.nombre);
	    		$('txtEmail').set('value',result.correo);
	    		$('txtDNI').set('value',result.dni);
	    		//Por si tuviera datos lo limpio
	    		$('txtPass').set('value','');
	    		$('txtPassRepeat').set('value','');
			} 
		}
		else{ //limpio los datos
			$('txtNombre').set('value','');
    		$('txtEmail').set('value','');
    		$('txtDNI').set('value','');
    		$('txtPass').set('value','');
    		$('txtPassRepeat').set('value','');
		}
		
	    // Setup rolelist
		this.rolelist = new mooList('rolesSeleccionados', {
	        title: 'Roles del usuario',
	        alternaterows: true,
	        width: '50%', //parseInt($('mainColumn').getStyle('width')) - 80,
	        height: 400,
	        rowbuttons: [
	        {
	            'name': 'Quitar rol',
	            'bclass': 'removeRoleButton',
	            'onclick': this.onRemoveRoleClick.bind(this)
	        }]
	    });	    

        // Solo cargamos los roles si el formulario pertenece a un usuario
        // existente en la base de datos (no para un usuario nuevo)
        var roles = [];
        if(this.userId)
        {      	
            var roljs = runServiceJson({
                'wsName': 'GET_LISTA_ROLES_USUARIO',
                'idUsuario': idUser,
                'idioma': 'es'
            });
            if(roljs){
            	// sort roles so they appear grouped an can be more easily read
                roljs.sort(function(a,b){
                    if (a.nombreRol>b.nombreRol){
                        return 1;
                    } else if (a.nombreRol<b.nombreRol){
                        return -1;
                    } else {
                        return 0;
                    }
                });               
                            
                for(var i=0, len=roljs.length; i<len; i++){
                    var roltext= this.getRolListText(roljs[i].nombreRol, roljs[i].nombreAmbito, roljs[i].nombreTipoAmbito);

                    roles.push({
                        'id': roljs[i].idRol+roljs[i].idAmbito,
                        'text': roltext,
                        'data': {
                            'idRol': roljs[i].idRol,
                            'nombreRol': roljs[i].nombreRol,
                            'idAmbito': roljs[i].idAmbito,
                            'nombreAmbito': roljs[i].nombreAmbito
                        }
                    });
                }

                this.rolelist.setData({data:roles});
            }
            else {
            	debugConsole('GET_LISTA_ROLES_USUARIO error');
            	this.rolelist.setData({data:roles}); //Si es un usuario nuevo
            }
        } //Fin carga roles
        else {
        	//MochaUI.notification('Nuevo usuario');
        	this.rolelist.setData({data:roles}); //Si es un usuario nuevo
        }
        
        $('btnAddRol').removeEvents();
        $('btnAddRol').addEvent('click',this.onAddRolClick.bind(this));

        // Si se esta creando un usuario nuevo hay que habilitar el campo
        // de texto del nombre del usuario
        if(this.userId){
            $('txtNombre').set('disabled',true);
        } else {
            $('txtNombre').erase('disabled');
        }
        
        enableControl('AdminUserbtnGuardar');
        enableControl('AdminUserbtnCancelar');
    },

    /* *****************************************************************************
     *      Gestion de Roles
     * *****************************************************************************
     */

    onRemoveRoleClick: function(liData){
        this.rolelist.removeRowById(liData.id);
    },

    onAddRolClick: function(evt){

        // Si ya se ha seleccionado un rol exclusivo no permitir anadir otro rol
        var mydata = this.rolelist.getData();
        var hayRolExclusivo = false;
        if (mydata){
            for (var i=0,len=mydata.length;i<len; i++) {
                if (this.roleIsExclusive(mydata[i].data.nombreRol)){
                    hayRolExclusivo = true;
                    break;
                }
            }
        }
        
      //parar el evento para que no haga submit del form
        new Event(evt).stop();   
        
        if (hayRolExclusivo){
            var msg = "El usuario actual tiene asignado el rol administrador.\n Un usuario con este rol no puede tener otros roles. \nElimine el rol administrador antes de a&ntilde;adir otros roles";
            alert(msg);            
        }                     
        else{
        	// Call popu window SELECT ROLE
            this.popuproles = new RoleSelectionPopup();
            this.popuproles.display();
            this.popuproles.addEvent('addrole',this.onAddRoleToUser.bind(this));       
        }        
    },

    onAddRoleToUser: function(roledata){
        var mydata = this.rolelist.getData();
        if (!mydata)
            mydata = [];
        this.selectedRole = roledata;
        this.popuproles = null;

        var roltext = "";
        //comprobar roles exclusivos 
        if(this.roleIsExclusive(roledata.nombreRol)){
            var msg = '';
            msg = 'El rol '+ roledata.nombreRol +' es exclusivo.\n Un usuario con este rol no puede tener otros roles. \nSe borrarán los otros roles del usuario.\n ¿Desea continuar?';
            if (1 == confirm(msg)){
                if($('selectrole_idAmbito').value !=""){
                    alert('El rol '+ roledata.nombreRol+' no permite introducir ningún ámbito');
                    return;
                }
               
                mydata = [];

                roltext= this.getRolListText(roledata.nombreRol, roledata.nombreAmbito, roledata.nombreTipoAmbito);
                mydata.push({
                    'id': roledata.idRol+roledata.idAmbito,
                    'text': roltext,
                    'data': {
                        'idRol': roledata.idRol,
                        'nombreRol': roledata.nombreRol,
                        'idAmbito': roledata.idAmbito,
                        'nombreAmbito': roledata.nombreAmbito
                    }
                });

                this.rolelist.setData({
                    data: mydata
                });
            }
            return;
        }

        //Si ya esta en la lista no lo anado
        for(var i = 0; i < mydata.length; i++) {
            if(mydata[i] && mydata[i].id === roledata.idRol+roledata.idAmbito) {
            	alert('La lista de roles ya contiene un rol de nombre '+ roledata.nombreRol+' y ambito '+roledata.nombreAmbito);
                return;
            }
        }        
        
        roltext= this.getRolListText(roledata.nombreRol, roledata.nombreAmbito, roledata.nombreTipoAmbito);
        mydata.push({
            'id': roledata.idRol+roledata.idAmbito,
            'text': roltext,
            'data': {
                'idRol': roledata.idRol,
                'nombreRol': roledata.nombreRol,
                'idAmbito': roledata.idAmbito,
                'nombreAmbito': roledata.nombreAmbito
            }
        });
        this.rolelist.setData({
            data: mydata
        });
    },
    
  //Utility functions
    getRolListText: function(nombreRol, nombreAmbito, nombreTipoAmbito){
        var roltext= '<b>'+nombreRol+'</b> ';
        if (nombreAmbito){
        	if (nombreTipoAmbito){
        		roltext += 'sobre <b>'+nombreAmbito+'-'+nombreTipoAmbito+'</b>';
        	} else {
        		roltext += 'sobre <b>'+nombreAmbito+'</b>';
        	}            
        }
        return roltext;
    },

    roleIsExclusive: function(roleName){
        return roleName.toLowerCase() === 'administrador';
    },

    /* *****************************************************************************
     *      CRUD de usuarios
     * *****************************************************************************
     */
    
    // API
    saveUserData: function (){
    	    	
        if (!this.validateFormDatosUsuario()){
            var errlist = this.frm_val.errors;
            var html = "";

            for (var i=0, len=errlist.length; i<len; i++){
                html += '<li>'+errlist[i]+'</li> \n';
            }
            
            html = '<p>Hay errores en los datos de usuario</p> <ul class=frmusr_popuperrlist>'+html+'</ul>';

            new PopupMsg(html, {type:'ok'});
            
            return null;
        }

        var userData = this.buildUserData();
       
        var result = runServiceJson({'wsName': 'GUARDAR_USUARIO','userData': JSON.encode(userData)});

        var msg = "";
        
        if (result){
        	if (!result.error) {
                msg ="Datos guardados correctamente";
            } else {
                msg = 'Error al guardar los datos '+ result.error;
                result = null;
            }
        } else {
        	msg = 'Error al guardar los datos';
        	result = null;
        }               
                
        new PopupMsg(msg);
        return result;
    },

    validateFormDatosUsuario: function (){
    	
    	if (!this.frm_val) {
    		this.frm_val = new formVal('panelDatosUsuario');
    	}        
    	
        var userData = this.buildUserData();
        if (!userData){
            return false;
        }
        
        this.frm_val.clearErrors();
        $$('div.fvErrorMsg').each(function(node){
            node.dispose();
        });
        
        //revert error style to form elements
        $$('input.fvFieldError').each(function(node){        	
            node.removeClass('fvFieldError');
        });
        
        $$('div.fvFieldError').each(function(node){
            node.removeClass('fvFieldError');
        });

        /*
         * Reglas de validacion:
         *  - Si es nuevo usuario (campo idUsuario vacio), el nombre de usuario no debe existir
         *  - Si es usuario existente (campo idUsuario relleno), el nombre de usuario no debe haber cambiado
         *  - La contrasena debe tener un tamano minimo de 4 caracteres
         *      (solo si es usuario nuevo o se esta cambiando la contraseÃ±a de un usuario existente)
         *  - La contrasena y la contrasena repetida deben ser iguales
         *      (olo si es usuario nuevo o se esta cambiando la contraseÃ±a de un usuario existente)
         *  - El dni no puede ser vacio y debe ser un dni valido
         *  - El correo electronico no puede ser vacio
         */

        // Si es nuevo usuario (campo idUsuario vacio), el campo nombre de usuario debe estar relleno
        if (userData.isNew && !$('txtNombre').value){
            this.frm_val.addError($('txtNombre'), "Debe indicar un nombre para el usuario");
        }
        // Si es nuevo usuario (campo idUsuario vacio), el nombre de usuario no debe existir
        if (userData.isNew && 'S' === checkUserNameExists(userData.nombre)){
            this.frm_val.addError($('txtNombre'),'Ya existe un usuario con este nombre');
        }
        
        // Si es usuario existente (campo idUsuario relleno), el nombre de usuario no debe haber cambiado
        if (!userData.isNew && userData.nombre !== getStoredUserData(userData.id).nombre){
            this.frm_val.addError($('txtNombre'), 'No se permite cambiar el nombre de un usuario existente');
        }

        // La contrasena debe tener un tamano minimo de 4 caracteres y la contrasena y su confirmacion
        // deben ser iguales
        //      - Si es un usuario nuevo
        if (userData.isNew && !userData.password){
            this.frm_val.addError($('txtPass'), 'La contrase&ntilde;a no puede esta vac&iacute;a');
        } else if (userData.isNew && userData.password.length < 4){
            this.frm_val.addError($('txtPass'), 'La contrase&ntilde;a debe tener 4 caracteres como m&aacute;nimo');
        } else if (userData.isNew && userData.password !== userData.passwordConfirm) {
            this.frm_val.addError($('txtPassRepeat'), 'Las contrase&ntilde;as no son iguales');
        }
        //      - Si se esta cambiando la contrasena de un usuario existente
        if (!userData.isNew && userData.password) {
            if (userData.password.length < 4) {
                this.frm_val.addError($('txtPass'), 'La contrase&ntilde;a debe tener 4 caracteres como m&aacute;nimo');
            } else if (userData.password !== userData.passwordConfirm) {
                this.frm_val.addError($('txtPassRepeat'), 'Las contras&ntilde;as no son iguales');
            }
        }
        
        //El correo electronio NO puede ser vacio y debe ser valido
        if (userData.correo){
            if (isEmail(userData.correo) == false){
                this.frm_val.addError($('txtEmail'), 'El correo electr&oacute;nico no es v&aacute;lido');
            }
        } else {
        	this.frm_val.addError($('txtEmail'), 'El correo electr&oacute;nico es un dato obligatorio');
        }
        
        //El dni NO puede ser vacio y debe ser valido
        if (userData.DNI){
            if (isNIF(userData.DNI) == false){
                this.frm_val.addError($('txtDNI'), 'El DNI no es v&aacute;lido');
            }
        } else {
        	this.frm_val.addError($('txtDNI'), 'El DNI es un dato obligatorio');
        }
        
        //El usuario debe tener al menos un rol asignado
        var roleData = this.rolelist.getData();
        if (!roleData || roleData.length == 0){
        	this.frm_val.addError($('rolesSeleccionados'), 'El usuario debe al menos debe tener un rol');        	
        }

        return this.frm_val.isValid();
    },


    buildUserData: function (){
    	
    	var userData = null;
    	
    	if (!Boolean(this.userId)){
    		//nuevo usuario. Ha de indicar el usuario y password
    		userData = {
    	            'isNew': !Boolean(this.userId),
    	            'id': this.userId,
    	            'nombre': $('txtNombre').value,    	            
    	            'correo': $('txtEmail').value,
    	            'DNI': $('txtDNI').value,
    	            'password': $('txtPass').value,
    	            'passwordConfirm': $('txtPassRepeat').value  //lo enviamos al servidor para que se vuelva a comprobar
    	        };    		    		
    	} else {
    		//modificacion. No tiene porque indicar usuario y password
    		if ($('txtPass').value.length > 0){
        		userData = {
        	            'isNew': !Boolean(this.userId),
        	            'id': this.userId,
        	            'nombre': $('txtNombre').value,    	            
        	            'correo': $('txtEmail').value,
        	            'DNI': $('txtDNI').value,
        	            'password': $('txtPass').value,
        	            'passwordConfirm': $('txtPassRepeat').value  //lo enviamos al servidor para que se vuelva a comprobar
        	        };
        	} else {
        		userData = {
        	            'isNew': !Boolean(this.userId),
        	            'id': this.userId,
        	            'nombre': $('txtNombre').value,
        	            'correo': $('txtEmail').value,
        	            'DNI': $('txtDNI').value,
        	        };
        	}
    	}    	    	    	 	        

        // get assigned roles
        var roleData = this.rolelist.getData();
        if (!roleData){
            roleData = [];
        }
        var rl = [];
        for (var i=0, len=roleData.length; i<len; i++){
            rl.push(roleData[i].data);
        }
        userData.roles = rl;

        return userData;
    }    
});

/*******************************************************************************
* Popup de seleccion de roles
******************************************************************************/

var RoleSelectionPopup = new Class({
    Implements: [Events],
    
    // Private vars
    form_validator: null,
    popupbox: null,


    // API
    display: function(){
    	new MUI.Modal({
            id: 'roleSelection',
            title: 'Selección de rol y ambito de aplicación',         
            contentURL: 'Pages/Administracion/selectRole.jsp',
            type: 'modal',
            loadMethod: 'xhr',
            evalScripts: false,
            width: 450,
            height: 280,
            padding: { top: 43, right: 12, bottom: 10, left: 12 },
            scrollbars: false,
            require: {
				onload: function(){
					
					$('selectrole_btnAddRol').addEvent('click', function(e) {
				    	
				        //Validacon del formulario
						formUserAdministration.popuproles.form_validator.clearErrors();
						
						 var idRole = undefined;
						 var nameRole = undefined;
						 var idAmbito = undefined;
						 var inpAmb = undefined;

				        // Se debe seleccionar un rol
				        var slcObj = $('selectrole_idRol');
				        if (slcObj.selectedIndex < 0) {
				        	formUserAdministration.popuproles.form_validator.addError(slcObj,'Debe seleccionar un rol');
				        }
				        else {
				        	var selectedIndex = slcObj.selectedIndex;
				            nameRole = slcObj.options[selectedIndex].text;	
				        	idRole = slcObj.options[selectedIndex].value;
				            
				            // El ambito seleccionado debe existir para los roles que lo necesitan
				            inpAmb = $('selectrole_idAmbito');
				            if (formUserAdministration.popuproles.roleNeedsAmbito(idRole)) {
				                if (inpAmb.value.length==0){
				                	formUserAdministration.popuproles.form_validator.addError(inpAmb, 'Debe introducir un &Aacute;mbito');
				                }
				                idAmbito = runService({
				                    'wsName': "GET_AMBITO_ID",
				                    'nombre': formUserAdministration.popuproles.stripTipoAmbito(inpAmb.value),
				                    'idioma': idioma,
				                    'tipoambito': formUserAdministration.popuproles.stripNombreAmbito(inpAmb.value)
				                });
				                if (!idAmbito || isNumeric(idAmbito) == false || idAmbito == -1){
				                	formUserAdministration.popuproles.form_validator.addError(inpAmb, '&Aacute;mbito no encontrado');
				                } else {				                					                	
				                	formUserAdministration.popuproles.form_validator.clearErrors();
				                }
				            }
				        }

				        if (formUserAdministration.popuproles.form_validator.isValid() && idRole){				            
				            formUserAdministration.popuproles.fireEvent('addrole',{
				                'idRol':idRole,
				                'nombreRol':nameRole,
				                'idAmbito':idAmbito,
				                'nombreAmbito': formUserAdministration.popuproles.stripTipoAmbito(inpAmb.value),
				                'nombreTipoAmbito': formUserAdministration.popuproles.stripNombreAmbito(inpAmb.value)
				            });
				            $('roleSelection').retrieve('instance').close();
				        }
					});
										
			        $('selectrole_idRol').addEvent('change', function(e) {
			        	
			            var slc = $('selectrole_idRol');
			            if (slc.selectedIndex >= 0){			            			            	
			            	if (formUserAdministration.popuproles.roleNeedsAmbito(slc.options[slc.selectedIndex].value)){			            		
				                enableControl('selectrole_idAmbito');
				                $$('label[for="selectrole_idAmbito"]').each(function(n){
				                    n.setOpacity(1);
				                });
				            } else {
				                disableControl('selectrole_idAmbito');
				                $$('label[for="selectrole_idAmbito"]').each(function(n){
				                    n.setOpacity(0.4);
				                });
				            }
			            } 			            
					});
			        
			        formUserAdministration.popuproles.form_validator = new formVal("selectrole_form");

			        // init autosuggest on "selectrole_idAmbito" input
			        new Autocompleter.Request.JSON('selectrole_idAmbito', 'GestionConsola?wsName=GET_LISTA_AMBITOS_POR_NOMBRE&idioma=es', {
			            'postVar': 'nombre',
			            'overflow': true
			        });									
				}	
			}
        });
    },

    
    init: function(){},

    roleNeedsAmbito: function(idRole){
        var result = runService({
            'wsName': 'GET_ROL_NEEDS_AMBITO',
            'rol': idRole
        });

        return result == "S";
    },

    // private methods   
    stripTipoAmbito: function(txt){
        return txt.split(" - ")[0];
    },

    stripNombreAmbito: function(txt){
        return txt.split(" - ")[1];
    }
});

