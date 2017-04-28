package Structures;

public class Block {

	//private Sector[] sectors;
	public Block next;
	private Sector root;
	private int size;
	public Block()
	{
		//sectors = new Sector[10];
		size = 0;
		root = null;
		next = null;
	}
	
	public void add(Sector sector)
	{
		
	}
}
