package Hardware;

import Hardware.HDD.Block;
import Structures.*;

public class RAM {

	private HDD vhdd; 	 // Vdisk some data is using in RAM
	private List memory; // Multi-list structure
	
	public RAM(HDD hdd) // Add virtual drive on hard drive.
	{
		this.vhdd = hdd;
		memory = new List(hdd.getBlocks(), hdd.getBlockSize()); // 30 blocks, 10 sectors a block contains 10 sectors
	}
	// clear RAM
	
	private void clearRAM()
	{
		memory = new List(vhdd.getBlocks(), vhdd.getBlockSize());
	}
	// display data according to name sorting
	public void printramOrderName()
	{
		memory.sortName();
	}
	// display data according to number sorting
	public void printramOrderNumber()
	{
		memory.sortNumber();
	}
	// display RAM unsorted
	public void printram()
	{
		memory.display();
	}
	// create a file
	public void create(String filename,int index)
	{
		memory.create(filename,index);
	}
	// write data to file
	public void append(String filename, String content)
	{
		memory.append(filename, content);
	}
	// print file's data
	public void print(String filename)
	{
		memory.print(filename);
	}
	// delete file in RAM
	public void delete(String filename)
	{
		memory.deleteBlock(filename);
	}
	// delete data between first and second blocks in related filename
	public void delete(String filename,int first,int second)
	{
		memory.deleteSector(filename, first, second);
	}
	// insert a data in RAM
	public void insert(String filename,int place,String content)
	{
		memory.insert(filename, place, content);
	}
	// get files from real disk drive to RAM
	public void makeEmptyList()
	{
		memory.makeEmpty();
	}
	
	public void restore()
	{
		
		// first clear ram
		clearRAM();
		// get hdd to temproray
		Block[] tempDisk = new Block[vhdd.getBlocks()];
		
		for (int i = 0; i < tempDisk.length; i++) {
			tempDisk[i] = vhdd.Blocks()[i];
		}
		// hdd defrag
		vhdd.defrag();
		String filename = "", content = "";
		// transfer files and contents from hdd to RAM
		for (int i = 0; i < vhdd.getBlocks(); i++) {
			if(vhdd.Blocks()[i].getData()[0] == '@')
			{
				filename = "";
				for (int j = 1; j < vhdd.Blocks()[i].getData().length; j++) {
					if(vhdd.Blocks()[i].getData()[j] != '.')
						filename += vhdd.Blocks()[i].getData()[j];
				}
				this.create(filename, i + 1);
			}
			else if(vhdd.Blocks()[i].getData()[0] != '.')
			{
				content = "";
				for (int j = 0; j < vhdd.Blocks()[i].getData().length; j++) 
				{
					if(vhdd.Blocks()[i].getData()[j] != '.')
						content += vhdd.Blocks()[i].getData()[j];
				}
				this.append(filename, content);
			}
		}
		// hdd clear
		vhdd.makeEmptyBlocks();
		// transfer temproray hdd to real HDD
		for (int i = 0; i < vhdd.getBlocks(); i++) {
			vhdd.Blocks()[i] = tempDisk[i];
		}
	}
}