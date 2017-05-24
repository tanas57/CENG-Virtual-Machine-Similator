package Structures;

public class Sector {

	private String data;	// it is sector's data in block in list
	private Sector next;    // it is next Sector

	public Sector(String data) // Create a sector with data.
	{
		next = null;
		this.data = data;
	}
	
	// getter and setter methods
	public String getData()
	{
		return data;
	}
	public void setData(String data)
	{
		this.data = data;
	}
	public void setNext(Sector next) { this.next = next; }
	public Sector getNext() { return this.next; }
}