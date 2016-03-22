/*
* Copyright 2011 red.es
* Autores: IDOM Consulting
*
* Licencia con arreglo a la EUPL, Versi�n 1.1 o -en cuanto
* sean aprobadas por la Comision Europea- versiones
* posteriores de la EUPL (la <<Licencia>>);
* Solo podra usarse esta obra si se respeta la Licencia.
* Puede obtenerse una copia de la Licencia en:
*
* http://ec.europa.eu/idabc/eupl5
*
* Salvo cuando lo exija la legislacion aplicable o se acuerde.
* por escrito, el programa distribuido con arreglo a la
* Licencia se distribuye <<TAL CUAL>>,
* SIN GARANTIAS NI CONDICIONES DE NINGUN TIPO, ni expresas
* ni implicitas.
* Vease la Licencia en el idioma concreto que rige
* los permisos y limitaciones que establece la Licencia.
*/ 

package com.mitc.redes.editorfip.servicios.genericos;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.icesoft.faces.component.tree.IceUserObject;
import com.icesoft.faces.context.effects.Effect;
import com.icesoft.faces.context.effects.Highlight;
import com.mitc.redes.editorfip.entidades.comunes.ParIdentificadorTexto;

public abstract class GestionArbolGenerico implements GestionArbolGenericoIF {

	protected DefaultTreeModel model = null;
	protected ArbolGenericoObject selectedUserObject;
	protected com.icesoft.faces.component.tree.Tree treeComponent;
	
	
	
	protected final Log logger = LogFactory.getLog(this.getClass());

    // effect that shows a value binding chance on there server
    protected Effect valueChangeEffect;
    
    // Constructor
    
    public GestionArbolGenerico() {
    	valueChangeEffect = new Highlight("#fda505");
        valueChangeEffect.setFired(true);
	}
    
    // Metodos abstractos a implementar
    /* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.GestionArbolGenericoIF#menuEvent(javax.faces.event.ActionEvent)
	 */
    public abstract void menuEvent(ActionEvent event);

	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.GestionArbolGenericoIF#nodeSelected(javax.faces.event.ActionEvent)
	 */
	public abstract void nodeSelected(ActionEvent event);
	
	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.GestionArbolGenericoIF#cargarArbolInicial()
	 */
	public abstract void cargarArbolInicial();
	
	
    // Del BaseBean
    /* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.GestionArbolGenericoIF#effectChangeListener(javax.faces.event.ValueChangeEvent)
	 */
    public void effectChangeListener(ValueChangeEvent event){
        valueChangeEffect.setFired(false);
    }

    
	protected void init() {

    }

    /* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.GestionArbolGenericoIF#getValueChangeEffect()
	 */
    public Effect getValueChangeEffect() {
        return valueChangeEffect;
    }

    /* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.GestionArbolGenericoIF#setValueChangeEffect(com.icesoft.faces.context.effects.Effect)
	 */
    public void setValueChangeEffect(Effect valueChangeEffect) {
        this.valueChangeEffect = valueChangeEffect;
    }
    
    
	// Para los Arboles

	

	

	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.GestionArbolGenericoIF#getTreeComponent()
	 */
	public com.icesoft.faces.component.tree.Tree getTreeComponent() {
		return treeComponent;
	}

	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.GestionArbolGenericoIF#setTreeComponent(com.icesoft.faces.component.tree.Tree)
	 */
	public void setTreeComponent(
			com.icesoft.faces.component.tree.Tree treeComponent) {
		this.treeComponent = treeComponent;
	}

	

	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.GestionArbolGenericoIF#getSelectedUserObject()
	 */
	public ArbolGenericoObject getSelectedUserObject() {
		return selectedUserObject;
	}

	/* (non-Javadoc)
	 * @see com.mitc.redes.editorfip.servicios.GestionArbolGenericoIF#getSelectedTreePath()
	 */
	public ArrayList getSelectedTreePath() {

		ArrayList treePath = new ArrayList();

		if (selectedUserObject != null) {
			Object[] objectPath = selectedUserObject.getWrapper()
					.getUserObjectPath();

			Object anObjectPath;
			for (int i = 0, max = objectPath.length - 1; i < max; i++) {
				anObjectPath = objectPath[i];
				IceUserObject userObject = (IceUserObject) anObjectPath;
				treePath.add(userObject.getText());
			}
		}

		return treePath;
	}

	

	protected DefaultMutableTreeNode addNode(DefaultMutableTreeNode parent,String title, ParIdentificadorTexto datosIdTextoArbolGenerico) 
	{
		DefaultMutableTreeNode node = new DefaultMutableTreeNode();
		ArbolGenericoObject userObject = new ArbolGenericoObject(node);
		node.setUserObject(userObject);
		userObject.setDatosIdTextoArbolGenerico(datosIdTextoArbolGenerico);
		
		// non-employee node or branch
		if (title != null) {
			userObject.setText(title);
			userObject.setLeaf(false);
			userObject.setExpanded(true);
			node.setAllowsChildren(true);
		}

		else {

			if (!datosIdTextoArbolGenerico.isHoja()) {
				userObject.setText(datosIdTextoArbolGenerico.getTexto());
				userObject.setLeaf(false);
				userObject.setExpanded(false);
				node.setAllowsChildren(true);
			} else {
				// Es una hoja
				userObject.setText(datosIdTextoArbolGenerico.getTexto());
				userObject.setLeaf(true);
				node.setAllowsChildren(false);

			}

		}
		// finally add the node to the parent.
		if (parent != null) {
			parent.add(node);
		}

		return node;
	}
	
	

    protected DefaultMutableTreeNode findTreeNode(String nodeId) {
        DefaultMutableTreeNode rootNode =
                (DefaultMutableTreeNode) model.getRoot();
        DefaultMutableTreeNode node;
        ArbolGenericoObject tmp;
        Enumeration nodes = rootNode.depthFirstEnumeration();
        while (nodes.hasMoreElements()) {
            node = (DefaultMutableTreeNode) nodes.nextElement();
            
            tmp = (ArbolGenericoObject) node.getUserObject();
            //logger.debug(tmp.getDatosIdTextoArbolGenerico().getTexto());
            if (nodeId.equals(String.valueOf(tmp.getDatosIdTextoArbolGenerico().getIdBaseDatos()))) {
                return node;
            }
        }
        return null;
    }
    
}
