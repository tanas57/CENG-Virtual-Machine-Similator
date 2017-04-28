package Structures;

public class List {

	private Block root;
	private Sector head;
	private int blockSize;
	private int sectorSize;
	public List(int blockSize, int sectorSize)
	{
		this.blockSize = blockSize;
		this.sectorSize = sectorSize;
		root = null;
	}
	
	public void addBlock(Block block)
	{
		if(root != null){
			Block temp = root;
			while(temp != null){
				temp = root.next;
			}
			temp = block;
		}
		else{
			root = block;
		}
	}
	
}
