package rox.gui.event;

public interface MapEventGenerator 
{
	public void addMapEventListener(MapEventListener listener);
	public void removeMapEventListener(MapEventListener listener);
}
