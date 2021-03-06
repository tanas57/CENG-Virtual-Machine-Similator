package Hardware;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
/*
 * Created by Tayyip Muslu on 05.05.17
*/
public class HDD{

	private Block [] blocks; // data
	private int blockNum;	 // number of blocks
	private int blockSize;	 // blocks data size
	private int currentBlock;// which block is available to write data
	private String storeData;
	// A HDD contains blocks, and sectors
	public HDD(int blockNum, int blockSize)
	{
		this.blockNum = blockNum;
		this.blockSize = blockSize;
		this.blocks = new Block[blockNum];
		this.currentBlock = 0;
		this.storeData = "vdisk.txt";
		makeEmptyBlocks(blocks);
	}
	// get number of blocks
	public int getBlocks() { return this.blockNum; }
	// get number of block size
	public int getBlockSize() { return this.blockSize; }
	// get vdisk data
	public Block[] Blocks() { return this.blocks; }
	// print all data
	public void printDisk()
	{
		for (int i = 1; i <= this.blockNum; i++) {
			
			System.out.print((i < 10) ? "0" + i : i);
			
			System.out.print(blocks[i - 1].DataToString());
			
			int nextBlock = blocks[i - 1].getNext();
			System.out.print((nextBlock < blockSize) ? " " + nextBlock : nextBlock);
			System.out.print("\t");
		}
	}
	// create a new file in vdisk
	public int create(String filename)
	{
		char[] file = filename.toCharArray();
		int currentblock = 0;
		boolean control = false;
		for (int i = 0; i < blocks.length; i++) {
			if(blocks[i].DataToString().contains("@"+filename))
			{
				control = true;
				break;
			}
		}
		if(!control && !filename.equals(null))
		{
			updateAvailableBlock();
			if(currentBlock < blockNum)
			{
				blocks[currentBlock].write('@');
				for (int i = 0; i < file.length; i++) {
					blocks[currentBlock].write(file[i]);
				}
				blocks[currentBlock].setNext((byte)0);
				currentblock = currentBlock;
				updateAvailableBlock();
				return currentblock;
			}
		}
		return -1;
	}
	// write data to related file
	public boolean append(String filename, String content)
	{
		char [] data = content.toCharArray();
		boolean control = false;
		
		
		for (int i = 0; i < blocks.length; i++) {
			if(blocks[i].DataToString().contains("@"+filename))
			{
				control = true;
				if(blocks[i].getNext() == 0) blocks[i].setNext((byte) (currentBlock + 1));
				else{
					// nextlerinin s�f�r olan�na gidip s�f�r� current yap�caz
					int index = blocks[i].getNext() - 1;
					while(true)
					{
						if(blocks[index].getNext() == 0)
						{
							blocks[index].setNext((byte)(currentBlock + 1));
							break;
						}
						else{
							index = blocks[index].getNext() - 1;
						}
					}
				}
				break;
			}
		}
		
		if(control){
			for (int i = 0; i < data.length; i++) {
				if(i % blockSize == 0 && i != 0) { blocks[currentBlock].setNext((byte)(currentBlock + 2)); updateAvailableBlock(); }
				blocks[currentBlock].write(data[i]);
			}
			
			updateAvailableBlock(); // bo� blok a ge�

			return true;
		}
		else return false;
	}
	// insert data to related file's block
	public boolean insert(String filename, int block, String content)
	{
		
		boolean control = false;
		
		String next = ""; int tempStart = 0;
		int makezero = 0;
		for (int i = 0; i < blocks.length; i++) {
			if(blocks[i].DataToString().contains("@"+filename))
			{
				control = true;
				tempStart = blocks[i].getNext();
				if(tempStart == 0) return false;
				int temp2 = tempStart, count = 0;
				while(true)
				{
					tempStart--;
					next += tempStart + " ";
					makezero = tempStart;
					tempStart = blocks[tempStart].getNext();
					if(tempStart == 0) break; 
					count++;
				}
				tempStart = temp2; // reset temp start index
				if(count + 1 < block) return false;
				break;
			}
		}
		
		if(control)
		{
			String[] nexts = next.split(" ");
			byte tempNext = 0; int tempBlock = 0; // en son append edilenin nexti bu olacak
			for (int i = 0; i <= block; i++) {
				if((i + 1) == block)
				{
					tempNext = blocks[Integer.parseInt(nexts[i])].getNext();
					tempBlock = Integer.parseInt(nexts[i]);
					break;
				}
			}
			
			// yeni veri eklendi verinin en sonunu se�ilen bloktan sonras�na ba�l�y�caz
			updateAvailableBlock();
			byte nextblock =(byte)(currentBlock + 1);
			
			this.append(filename, content);
			
			blocks[makezero].setNext((byte)0);
			blocks[tempBlock].setNext(nextblock);
			blocks[currentBlock-1].setNext((byte)tempNext);
			updateAvailableBlock();
			return true;
		}
		
		return false;
	}
	// update data
	public boolean update(String filename, int block, String content)
	{
			boolean control = false;
			String next = ""; int tempStart = 0;
			for (int i = 0; i < blocks.length; i++) 
			{
				if(blocks[i].DataToString().contains("@"+filename))
				{
					control = true;
					tempStart = blocks[i].getNext();
					if(tempStart == 0) return false;
					int temp2 = tempStart, count = 0;
					while(true)
					{
						tempStart--;
						next += tempStart + " ";
						tempStart = blocks[tempStart].getNext();
						if(tempStart == 0) break; 
						count++;
					}
					tempStart = temp2; // reset temp start index
					if(count + 1 < block) return false;
					break;
				}
			}
			String[] nexts = next.split(" ");
			if(control) 
			{
				blocks[Integer.parseInt(nexts[block-1])].setData(content);
				return true;
			}
			return false;
	}
	// delete a file from vdisk
	public boolean delete(String filename)
	{
		boolean control = false;
		
		int startIndex = -1, firstIndex = -1;
		for (int i = 0; i < blocks.length; i++) {
			if(blocks[i].DataToString().contains("@"+filename))
			{
				control = true;
				startIndex = blocks[i].getNext();
				firstIndex = i;
				
				System.out.println("bulundu index : " + i);
				break;
			}
		}
		
		if(control){
			System.out.println(firstIndex + " başı silindi");
			blocks[firstIndex].makeEmpty(); // delete @filename
			int tempStart = startIndex;
			if(tempStart == 0 ) return false;
			while(true) // delete @filename data
			{
				tempStart--;
				tempStart = blocks[tempStart].getNext();
				if(tempStart == 0) break;
			}
		}
		else return false;
		updateAvailableBlock();
		return true;
	}
	// delete file's data between start and end blocks
	public boolean delete(String filename, int startBlock, int endBlock)
	{
		boolean control = false;
		
		// startindex : @file's next
		// firstindex : @file's index
		byte startIndex = 0, firstIndex = 0;
		// search @filename
		for (byte i = 0; i < blocks.length; i++) {
			if(blocks[i].DataToString().contains("@"+filename))
			{
				control = true;
				startIndex = blocks[i].getNext();
				firstIndex = i;
				break;
			}
		}

		if(control && (startBlock >= 1 && startBlock <= 30) && (endBlock >= 1 && endBlock <= 30)){
			
			int startblocknext = 0;
			// block control
			String block = String.valueOf(firstIndex) + " "; 
			byte tempStart = startIndex, count = 0;
			if(tempStart == 0 ) return false;
			while(true)
			{
				tempStart--;
				block += tempStart + " ";
				tempStart = blocks[tempStart].getNext();
				if(tempStart == 0) break;
				count++;
			}

			if(count + 1 < endBlock) return false;
			
			String[] blockCont = block.split(" ");
			
			for (int i = 0; i < blockCont.length; i++) {
				if((i + 1) == startBlock)
				{
					startblocknext = Integer.parseInt(blockCont[i]);
					break;
				}
			}
			
			if(blockCont.length >= endBlock)
			{
				byte nextindex = 0;
				for (int i = startBlock; i <= endBlock; i++) {
					nextindex = blocks[Integer.parseInt(blockCont[i])].getNext();
					blocks[Integer.parseInt(blockCont[i])].makeEmpty();
				}
				blocks[startblocknext].setNext(nextindex);

				updateAvailableBlock();
				return true;
			}
			
		}

		return false;
	}
	// combine file and file's data
	public boolean defrag()
	{
		Block[] tempVdisk = new Block[this.blockNum];
		for (int i = 0; i < blocks.length; i++) {
			tempVdisk[i] = blocks[i];
		}
		
		makeEmptyBlocks(blocks);
		updateAvailableBlock();
		for (int i = 0; i < tempVdisk.length; i++) {

			String filename = "", content = "";
			if(tempVdisk[i].getData()[0] == '@')
			{
				for (int j = 1; j < tempVdisk[i].getData().length; j++) {
					filename += tempVdisk[i].getData()[j];
				}
				this.create(filename);
				int next = (tempVdisk[i].getNext() - 1);
				
				while(next > -1)
				{
					for (int j = 0; j < tempVdisk[next].getData().length; j++) {
								content += tempVdisk[next].getData()[j];
					}
					next = tempVdisk[next].getNext() - 1;
				}
				this.append(filename, content);
			}
		}
		
		return true;
	}
	// save as a file to primary disk
	public boolean store()
	{
		try{
		    PrintWriter writer = new PrintWriter(storeData, "UTF-8");
		    for (int i = 0; i < blocks.length; i++) {
		    	String data = "";
				for (int j = 0; j < blocks[i].getData().length; j++) {
					data += blocks[i].getData()[j];
				}
				//data += " " + ( ( blocks[i].getNext() < 9 ) ? "0" + blocks[i].getNext() : blocks[i].getNext());
				data += blocks[i].getNext();
				writer.println(data);
		    }
		    writer.close();
		} catch (Exception e) {
		   // do something
		}
		
		return true;
	}
	// get data from disk to vdisk
	public boolean restore() throws IOException
	{
		String fileName = storeData;
	    // This will reference one line at a time
	    String line = null;
	    try 
	    {
	           FileReader fileReader = new FileReader(fileName);

	           BufferedReader bufferedReader =  new BufferedReader(fileReader);
	           int i = 0;
	           for (int j = 0; j < blocks.length; j++) {
	        	   blocks[j].makeEmpty();
	           }
	           while((line = bufferedReader.readLine()) != null) 
	           {
	        	   char[] data = line.toCharArray();
	        	   String next = "";
	        	   for (int j = 0; j < data.length; j++) {
	        		   if(j < blockSize) blocks[i].write(data[j]);
	        		   if(j >= blockSize) next += data[j];
	        	   }
	        	   blocks[i].setNext((byte)Integer.parseInt(next));
	        	   i++;
	           }
	           bufferedReader.close();
	           updateAvailableBlock();
	      }
	      catch(FileNotFoundException ex) { }
		return true;
	}
	// update currentblock which is writable block.
	private void updateAvailableBlock()
	{
		String emptyData = "";
		for (int i = 0; i < blockSize; i++) emptyData += '.';
		
		for (int i = 0; i < blocks.length; i++) {
			if(blocks[i].DataToString().equals(emptyData) && blocks[i].getNext() == 0)
			{
				currentBlock = i;
				break;
			}
		}
	}
	// delete all vdisk data
	public void makeEmptyBlocks(Block[] block)
	{
		for (int i = 0; i < block.length; i++) {
			block[i] = new Block();
		}
	}
	// this class is just used in vdisk
	public class Block
	{
		private char [] sectors;
		private byte nextSector; // 
		private byte nextBlock;
		public Block()
		{
			this.nextSector = 0;
			sectors = new char[blockSize];
			makeEmpty();
		}
		// write data to vdisk block
		private void write(char letter)
		{
			if(nextSector < blockSize)
			{
				sectors[nextSector] = letter;
				nextSector++;
			}
		}
		// get next block number
		public byte getNext() { return this.nextBlock; }
		// set next block number
		public void setNext(byte num) { this.nextBlock = num; }
		// reset block data
		private void makeEmpty()
		{
			for (int i = 0; i < sectors.length; i++) {
				sectors[i] = '.';
			}
			this.nextBlock = 0;
			this.nextSector = 0;
		}
		// get related block data
		public char[] getData() { return sectors; }
		public void setData(String data)
		{
			char[] newdata = data.toCharArray();
			if( sectors.length < newdata.length ) 
			{
				System.out.print("long content");
			}
			else
			{
				for (int i = 0; i < sectors.length; i++) 
				{
					if(i < newdata.length)
					{
						this.sectors[i] = newdata[i];
					}
					else
					{
						this.sectors[i] = '.';
					}
				}
			}
		}
		// get String data which combines char array
		public String DataToString() {
			String data = "";
			for (int i = 0; i < sectors.length; i++) {
				data += sectors[i];
			}
			return data;
		}
	}
}