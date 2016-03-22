/*
 * Copyright 2011 red.es
 * Autores: Arnaiz Consultores.
 *
 ** Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto
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
package es.mitc.redes.urbanismoenred.utils.consola;

import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author Alvaro
 */
public class OgridColumnModel {

    private String header;
    private String dataIndex;
    private String dataType;
    private int width;
    private boolean hidden;
    //Selection of referenced entities support
    private Boolean isSelection = false;
    private String refType = "";
    private String refIdCol = "";
    private String refNameCol = "";
    private Boolean editable = true;

    public OgridColumnModel(String header, String dataIndex, String dataType, int width) {
        this.header = header;
        this.dataIndex = dataIndex;
        this.dataType = dataType;
        this.width = width;
        this.hidden = false;
    }

    public OgridColumnModel(String header, String dataIndex, String dataType, boolean editable,int width) {
        this.header = header;
        this.dataIndex = dataIndex;
        this.dataType = dataType;
        this.editable = editable;
        this.width = width;
        
    }

    public OgridColumnModel(String header, String dataIndex, String dataType, int width, boolean hidden) {
        this.header = header;
        this.dataIndex = dataIndex;
        this.dataType = dataType;
        this.width = width;
        this.hidden = hidden;
    }

    public OgridColumnModel(String header, String dataIndex, String dataType, int width, boolean hidden, String refType, String refIdCol, String refNameCol) {
        this.header = header;
        this.dataIndex = dataIndex;
        this.dataType = dataType;
        this.width = width;
        this.hidden = hidden;

        this.isSelection = true;
        this.refType = refType;
        this.refIdCol = refIdCol;
        this.refNameCol = refNameCol;
    }

    /**
     * @return the header
     */
    public String getHeader() {
        return header;
    }

    /**
     * @param header the header to set
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * @return the dataIndex
     */
    public String getDataIndex() {
        return dataIndex;
    }

    /**
     * @param dataIndex the dataIndex to set
     */
    public void setDataIndex(String dataIndex) {
        this.dataIndex = dataIndex;
    }

    /**
     * @return the dataType
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * @param dataType the dataType to set
     */
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }

    public Map<String, Object> toJSON() {
        Map<String, Object> res = new HashMap<String, Object>();

        res.put("header", this.header);
        res.put("dataIndex", this.dataIndex);
        res.put("dataType", this.dataType);
        res.put("width", this.width);
        res.put("hidden", this.hidden);

        if (this.isSelection){
            res.put("isSelection", true);
            res.put("refType", this.refType);
            res.put("refIdCol", this.refIdCol);
            res.put("refNameCol", this.refNameCol);
        }
        if (!this.editable){
            res.put("editable", this.editable);
        }

        return res;
    }

    /**
     * @return the hidden
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * @param hidden the hidden to set
     */
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    /**
     * @return the isSelection
     */
    public Boolean getIsSelection() {
        return isSelection;
    }

    /**
     * @param isSelection the isSelection to set
     */
    public void setIsSelection(Boolean isSelection) {
        this.isSelection = isSelection;
    }

    /**
     * @return the refType
     */
    public String getRefType() {
        return refType;
    }

    /**
     * @param refType the refType to set
     */
    public void setRefType(String refType) {
        this.refType = refType;
    }

    /**
     * @return the refIdCol
     */
    public String getRefIdCol() {
        return refIdCol;
    }

    /**
     * @param refIdCol the refIdCol to set
     */
    public void setRefIdCol(String refIdCol) {
        this.refIdCol = refIdCol;
    }

    /**
     * @return the refNameCol
     */
    public String getRefNameCol() {
        return refNameCol;
    }

    /**
     * @param refNameCol the refNameCol to set
     */
    public void setRefNameCol(String refNameCol) {
        this.refNameCol = refNameCol;
    }
}
