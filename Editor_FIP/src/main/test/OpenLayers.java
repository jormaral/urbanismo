package test;

public interface OpenLayers {

	public Integer getZoomLevel(); 

	public void setZoomLevel(Integer zoomLevel); 

	public Double getLon();

	public void setLon(Double lon);

	public Double getLat(); 

	public void setLat(Double lat);

	public boolean isAddNasaLayer(); 

	public void setAddNasaLayer(boolean addNasaLayer); 

	public boolean isAddMetaCartaLayer(); 

	public void setAddMetaCartaLayer(boolean addMetaCartaLayer); 

	public boolean isShowPanZoomControl(); 
	
	public void setShowPanZoomControl(boolean showPanZoomControl); 

	
	

}

