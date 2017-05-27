package Structures;

import java.util.Scanner;

public class List { // multi-list structure

	private Scanner scan; // this attribute is waiting after the process
	private Block root;   // starting the list structure
	private int blockSize;// size of block in HDD
	private int sectorSize;// size of sector in block in HDD
	
	public List(int blockSize, int sectorSize) // block and sector size , you can change these in HDD 
	{
		this.scan = new Scanner(System.in); // I used scanner for waiting.
		this.blockSize = blockSize; // Here is only for the flexibility.
		this.sectorSize = sectorSize; // Here is only for the flexibility.
		root = null; // first, Root is a block and row of list structure.
	}

	public void makeEmpty()
	{
		root = null;
	}
	// sorting according to vdisk number;
	public void sortNumber()
	{
		Block current = root;
		// insertion sort
		while(current != null)
		{
			Block current2 = root;
			while(current2 != null)
			{
				if(current.getNumber() < current2.getNumber() ) { swap(current, current2); }
				current2 = current2.getNextFilename();
			}
			current = current.getNextFilename();
		}
		display();
	}
	// sorting according to name
	public void sortName()
	{
		Block current = root;
		// insertion sort
		while(current != null)
		{
			Block current2 = root;
			while(current2 != null)
			{
				if(current2.getFileName().compareTo(current.getFileName()) > 0) { swap(current, current2); }
				current2 = current2.getNextFilename();
			}
			current = current.getNextFilename();
		}
		display();
	}
	// I used swap for sorting , Short changing Blocks according to number or name
	public void swap(Block first, Block second)
	{
		// swap current and current2
		Sector tempData = first.getRight();
		String tempFile = first.getFileName();
		int tempIndex = first.getNumber();

		first.setRight(second.getRight());
		first.setFileName(second.getFileName());
		first.setNumber(second.getNumber());
		
		second.setRight(tempData);
		second.setFileName(tempFile);
		second.setNumber(tempIndex);
	}
	// update content in the block 
	public void update(String filename,int place,String content)
	{
			Block current = root;
			while(current != null)
			{
				if(current.getFileName().equals("@" + filename))
				{
					current.updateSector(place, content);
					break;
				}
				current = current.getNextFilename();
			}
	}
	// delete it according to filename
	public void deleteBlock(String filename)
	{
		Block tempBlock = root;
		Block previous = null;
		if(tempBlock.getFileName().equals("@" + filename))
		{
			root = root.getNextFilename();
		}
		else
		{
			while(tempBlock != null)
			{
				if(tempBlock.getFileName().equals("@" + filename))
				{
					previous.setNextFilename(tempBlock.getNextFilename());
					previous.setNextNumber(tempBlock.getNextNumber());
					tempBlock = previous;
					break;
				}
				previous = tempBlock;
				tempBlock = tempBlock.getNextFilename();
			}
		}
	}
	// delete sector in the filename , From first entered index to last entered index
	public void deleteSector(String filename,int first,int last)
	{
		Block tempBlock = root;
		while(tempBlock != null)
		{
			if(tempBlock.getFileName().equals("@" + filename))
			{
				tempBlock.removeSectorsFromBlock(first, last);
				break;
			}
			tempBlock = tempBlock.getNextFilename();
		}
	}
	// create a new file 
	public void create(String filename,int index)
	{
		if(filename.length() < sectorSize) // this control depends on the HDD.
		{
			Block newfile = new Block();
			newfile.setFileName("@" + filename);
			if(root == null)
			{
				root = newfile;
				newfile.setNumber(index);
			}
			else
			{
				Block temp = root;
				while( temp.getNextFilename() != null)
				{
					temp = temp.getNextFilename();
				}
				temp.setNextFilename(newfile);
				newfile.setNumber(index);
			}
		}
		else
		{
			System.out.println("The Filename can not be longer than 9 characters.");
		}
	}
	// add content to filename , in 10 chars pieces.
	public void append(String filename, String content)
	{
		Block current = root;
		int size = content.length() / sectorSize;
		if(content.length() % sectorSize != 0) { size += 1; }
		String[] addingContent = new String[size];
		int k = 0;
		for (int i = 0; i < size; i++) {
			if(k+sectorSize > content.length())
			{
				addingContent[i] = content.substring(k,content.length());
				break;
			}
			addingContent[i] = content.substring(k,k+sectorSize);
			k += sectorSize;
		}
		while(current != null)
		{
			if(current.getFileName().equals("@" + filename))
			{
				for (int i = 0; i < size; i++) 
				{
					Sector newsector = new Sector(addingContent[i]);
					current.addSectorToBlock(newsector);
				}
				break;
			}
			current = current.getNextFilename();
		}
	}
	// insert content to filename , after place sector
	public void insert(String filename,int place,String content)
	{
		Block current = root;
		if(current != null)
		{
			int size = content.length() / sectorSize;
			if(content.length() % sectorSize != 0) { size += 1; }
			String[] addingContent = new String[size];
			int k = 0;
			for (int i = 0; i < size; i++) 
			{
				if(k+sectorSize > content.length())
				{
					addingContent[i] = content.substring(k,content.length());
					break;
				}
				addingContent[i] = content.substring(k,k+sectorSize);
				k += sectorSize;
			}
			while(current != null)
			{
				if(current.getFileName().equals("@" + filename))
				{
					Sector[] newsector = new Sector[size];
					for (int i = 0; i < newsector.length; i++) {
						newsector[i] = new Sector(addingContent[i]);
					}
					for (int i = 0; i < size; i++) {
						current.insertSectorToBlock(place,newsector[i]);
						place++;
					}
					break;
				}
				current = current.getNextFilename();
			}
		}
		
	}
	// print the filename and its sectors
	public void print(String filename)
	{
		Block current = root;
		boolean control = false;
		while(current != null)
		{
			if(current.getFileName().equals("@" + filename))
			{
				control = true;
				Sector curSec = current.getRight();
				System.out.print(current.getFileName() + "->");
				while(curSec != null){
					System.out.print(curSec.getData()+"->");
					curSec = curSec.getNext();
				}
				break;
			}
			current = current.getNextFilename();
		}
		if(control) scan.nextLine();
		else System.out.println("The file does not exist");
	}
	// display the list structure
	public void display()
	{
		Block cur = root;
		while(cur != null)
		{
			System.out.print(cur.getFileName() + "|"+ cur.getNumber() + " -> ");
			Sector curSec = cur.getRight();
			while(curSec != null)
			{
				System.out.print(curSec.getData() + " ->" );
				curSec = curSec.getNext();
			}
			System.out.println();
			System.out.println("|");
			System.out.println("v");
			
			cur = cur.getNextFilename();
		}
		if(root != null) scan.nextLine();
		else System.out.println("Any file has not found, please add a file");
	}
	// filename count
	public int getBlocksize() { return this.blockSize; }
}
