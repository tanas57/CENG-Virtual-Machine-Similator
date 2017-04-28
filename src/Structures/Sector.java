package Structures;

public class Sector {

	private Sector next;
	private char data;
	
	public Sector(char data)
	{
		next = null;
	}
	
	public char getData()
	{
		return data;
	}
	
	public void setData(char data)
	{
		this.data = data;
	}
}