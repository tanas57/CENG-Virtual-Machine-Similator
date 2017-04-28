package Hardware;

public class HDD {

	private Block [] blocks;
	private int blockNum;
	private int blockSize;
	private int currentBlock;
	/*
	 * ilk 10 index veri tutacak
	 * son index, sonraki blok id sini tutacak
	 */
	public HDD(int blockNum)
	{
		this.blockSize = 10;
		this.blockNum = blockNum;
		blocks = new Block[blockNum];
		currentBlock = 0;
		makeEmptyBlocks();
	}
	
	private void makeEmptyBlocks()
	{
		for (int i = 0; i < this.blockNum; i++) {
			blocks[i] = new Block();
		}
	}
	
	public void printBlock()
	{
		for (int i = 1; i <= this.blockNum; i++) {
			
			System.out.print((i < 10) ? "0" + i : i);
			
			for (int j = 0; j < blockSize; j++) {
				System.out.print(blocks[i - 1].getData()[j]);
			}
			
			int nextBlock = blocks[i - 1].getNext();
			System.out.print((nextBlock < 10) ? " " + nextBlock : nextBlock);
			System.out.print("\t");
		}
	}
	
	// sıfırdan veri yazarken
	public boolean writeFile(String filename, String content)
	{
		char [] data = content.toCharArray();
		char [] file = filename.toCharArray();
		
		blocks[currentBlock].write('@');
		
		for (int i = 0; i < file.length; i++) {
			if(i % ( blockSize - 1 ) == 0 && i != 0) { blocks[currentBlock].setNext(currentBlock + 2); currentBlock++; }
			blocks[currentBlock].write(file[i]);
		}
		
		blocks[currentBlock].setNext(currentBlock + 2);
		currentBlock++;
		
		for (int i = 0; i < data.length; i++) {
			if(i % blockSize == 0 && i != 0) { blocks[currentBlock].setNext(currentBlock + 2); currentBlock++; }
			blocks[currentBlock].write(data[i]);
		}
		
		currentBlock++; // boş blok a geç
		return true;
	}
	// güncelleme işlemi yaparken7
	public boolean updateFile(String filename, String content)
	{
		
		return true;
	}
	
	public boolean deleteContent(String filename, int startBlock, int endBlock)
	{
		
		return true;
	}
	
	public class Block
	{
		private char [] sectors;
		private int nextSector;
		private int nextBlock;
		private Block()
		{
			this.nextSector = 0;
			sectors = new char[blockSize];
			makeEmpty();
		}
		
		private void write(char letter)
		{
			sectors[nextSector] = letter;
			nextSector++;
		}
		
		private int getNext() { return this.nextBlock; }
		
		private void setNext(int num) { this.nextBlock = num; }
		
		private void makeEmpty()
		{
			for (int i = 0; i < sectors.length; i++) {
				sectors[i] = '.';
			}
			this.nextBlock = 0;
		}
		
		private char[] getData() { return sectors; }
	}
}
