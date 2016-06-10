/**
 * 
 */
package rox.model;

/**
 * @author Ross
 *
 */
public class GridCoordinate
{
	int x = 0;
	int y = 0;
	
	public GridCoordinate(int x, int y)
	{
		this.x=x;
		this.y=y;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	public GridCoordinate add(GridCoordinate coord1, GridCoordinate coord2)
	{
		return new GridCoordinate(coord1.getX()+coord2.getX(), coord1.getY()+coord2.getY());
	}
	
	@Override
	public String toString()
	{
		return "Grid Location [" + x + "," + y + "] @" + hashCode();
	}
}