package Structures;

public class Block {

	private int number; // according to HDD , order next number
	private String filename; // for entered filename 
	
	private Sector root; // is sector of block
	private Block nextNumber; // for next Block
	private Block nextFilename; // for next Block
	
	public Block() // First, root and their next's are null
	{
		this.root = null;
		this.nextNumber = null;
		this.nextFilename = null;
	}
	
	// getter and setter methods..Starting
	public void setNumber(int number) { this.number = number; }
	public int getNumber() { return this.number; }
	
	public void setFileName(String filename){ this.filename = filename; }
	public String getFileName() { return this.filename; }
	
	public Block getNextFilename() { return this.nextFilename; }
	public Block getNextNumber() { return this.nextNumber; }
	
	public void setNextFilename(Block nextFilename) { this.nextFilename = nextFilename; }
	public void setNextNumber(Block nextNumber) { this.nextNumber = nextNumber; }
	
	public Sector getRight() { return this.root; }
	public void setRight(Sector root) { this.root = root;}
	// getter and setter methods..End
	public void updateSector(int place,String content)
	{
		if(root.getData() == content){
			root.setData(content);
		}
		else
		{
			Sector temp = root;
			for (int i = 1; i < place; i++) 
			{
				temp = temp.getNext();
			}
			temp.setData(content);
		}
	}
	// insert new sector , after place sector
	public void insertSectorToBlock(int place,Sector newsector)
	{
		if(root == null){
			root = newsector;
		}
		else
		{
			Sector temp = root;
			for (int i = 1; i < place; i++) {
				//System.out.println(temp.getData()+ " ");
				temp = temp.getNext();
			}

				newsector.setNext(temp.getNext());
				temp.setNext(newsector);
		}
	}
	// add new sector, in last
	public void addSectorToBlock(Sector newsector)
	{
		if(root == null){
			root = newsector;
		}
		else{
			Sector current = root;
			while( current.getNext() != null){
				current = current.getNext();
			}
			current.setNext(newsector);
		}
	}
	// remove the sectors , from first entered index to last entered index
	public void removeSectorsFromBlock(int first,int last)
	{
		if(root == null)
		{
			System.out.println("File is clear");
		}
		else
		{			
			if(first > size() || last > size())
			{
				System.out.println("Wrong .. !");
				
			}
			else
			{
				Sector prevFirst = find(first-1);
				Sector First = find(first);
				Sector Last = find(last);
				Sector afterLast = find(last+1);
				if(First == root && afterLast != null)
				{
					root = Last.getNext();
				}
				else if(First != root && afterLast == null)
				{
					prevFirst.setNext(null);
				}
				else if(First == root && Last == root)
				{
					root = root.getNext();
				}
				else
				{
					prevFirst.setNext(afterLast);
				}
			}
			
		}
	}
	// find to sector for removeMethod, first entered index and last entered index
	public Sector find(int order)
	{
		Sector temp = root;
		for (int i = 1; i < order; i++) 
		{
			temp = temp.getNext();
		}
		return temp;
	}
	// size of sector
	public byte size()
	{
		byte count = 0;
		Sector temp = root;
		while(temp != null)
		{
			count++;
			temp = temp.getNext();
		}
		return count;
	}
}
