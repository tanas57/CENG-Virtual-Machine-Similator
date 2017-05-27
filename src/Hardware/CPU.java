package Hardware;
import enigma.core.Enigma;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import enigma.console.TextAttributes;
import java.awt.Color;

public class CPU  {
	String[] loadCommand;
	private static char[] alphabet;	 // Supported character set
	private static String[] commands;// This keeps command set
	private int keypr;   			 // key pressed?
	private int rkey;    			 // key   (for press/release)
	private KeyListener klis; 
	private enigma.console.Console console;
	private Scanner scan;
	
	public CPU() throws Exception
	{
		alphabet = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
		commands = new String[] { "Load","Print","Printram (Name)","Printram (Number)","Create","Update","Append","Insert","Delete","Printdisk","Defrag","Store","Restore","Exit" };
		console  = Enigma.getConsole("CENG Virtual Machine", 90, 22, 14, 4);
		// ------ Standard code for mouse and keyboard ------ Do not change
		scan = new Scanner(System.in);
		
	      klis=new KeyListener() 
	      {
	         public void keyTyped(KeyEvent e) {}
	         public void keyPressed(KeyEvent e) {
	            if(keypr==0) 
	            {
	               keypr=1;	
	               rkey=e.getKeyCode();
	            }
	         }
	         public void keyReleased(KeyEvent e) {}
	      };
	      console.getTextWindow().addKeyListener(klis);
	    // ----------------------------------------------------
	    instruction();
	    // you can chance disk size block, blocksize(30,10)
		HDD hdd = new HDD(30, 10);
		RAM ram = new RAM(hdd);
		System.out.println("Presy any key to continue");
		
		TextAttributes reverse = new TextAttributes(Color.blue,Color.white);
        TextAttributes attrs = new TextAttributes(Color.white,Color.black);
        String selectedCommand = null;
        
        int prev  = 3;
		int next  = 3;
		int i = 0 , index = 0;
		String filename = "";
		String content = "";
        
        while(true)
        {
        	klis=new KeyListener() 
  	      	{
  	         public void keyTyped(KeyEvent e) {}
  	         public void keyPressed(KeyEvent e) {
  	            if(keypr==0) 
  	            {
  	               keypr=1;	
  	               rkey=e.getKeyCode();
  	            }
  	         }
  	         public void keyReleased(KeyEvent e) {}
  	      	};
        	if(keypr==1)  // if keyboard button pressed
        	{
        		clear();
        		keypr = 0;
                writeMenu();
        		break;
        	}
        }
        
        console.setTextAttributes(reverse);
    	console.getTextWindow().setCursorPosition(3, next);
    	console.getOutputStream().print(commands[next-3]);
        boolean writeagain = false;
		while(selectedCommand != "Exit")
		{
			if(writeagain)
			{
				clear();
				writeMenu();
				writeagain = false;
			}
			if(keypr==1)  // if keyboard button pressed
	         {  
				console.setTextAttributes(attrs);
		        if(rkey==KeyEvent.VK_UP)
		        {
		        	prev = next;
		        	next--;
		        }
		        if(rkey==KeyEvent.VK_DOWN)
		        {
		        	prev = next;
		        	next++;
		        }
		        char rckey=(char)rkey;
                if(next<3)
                {
                	next = commands.length+prev-1;
                }
                if(next-3>commands.length-1)
                {
                	next = 3;
                }
                //       up            down
                if( rckey=='&' || rckey=='(')
                {
                	console.setTextAttributes(reverse);
		        	console.getTextWindow().setCursorPosition(3, next);
		        	console.getOutputStream().print(commands[next-3]);
		        	
		        	console.setTextAttributes(attrs);
		        	console.getTextWindow().setCursorPosition(3, prev);
		        	console.getOutputStream().print(commands[prev-3]);
                }
                if(rkey==KeyEvent.VK_ENTER)
                {
                	selectedCommand = commands[next-3];
            		clear();
            		switch(selectedCommand)
            		{
            		
            		case "Print":
            		{
            			console.getOutputStream().print("Enter Filename = ");
            			filename = scan.nextLine();
            			ram.print(filename);
            		}break;
            		case "Printram (Name)":
            		{
            			ram.printramOrderName();
            		}break;
            		case "Printram (Number)":
            		{
            			ram.printramOrderNumber();
            		}break;
            		case "Printdisk":
            		{
        				hdd.printDisk();
        				scan.nextLine();
            		}break;
            		
            		case "Create":
            		{
            			console.getOutputStream().print("Filename = ");
            			filename = scan.nextLine();
            			if(!checkTheEntered(filename))
            			{
            				System.out.println("Wrong Character");
            			}
            			else
            			{
	            			index = hdd.create(filename);
	            			if(index != -1) ram.create(filename,++index);
	            			else
	            			{
	            				console.getOutputStream().print("Wrong Command");
	            				scan.nextLine();
	            			}
            			}
            		}break;
            		
            		case "Append":
            		{
            			console.getOutputStream().print("Filename = ");
            			filename = scan.nextLine();
            			console.getOutputStream().print("Content = ");
            			content = scan.nextLine();
            			if(!checkTheEntered(filename) || !(checkTheEntered(content)))
            			{
            				System.out.println("Wrong Character");
            			}
            			else
            			{
            				if(hdd.append(filename, content)) ram.append(filename, content);
            				else System.out.println("An error is occured, please try again");
            			}
            		}break;
            		case "Delete":
            		{
            			console.getOutputStream().print("File or Content ? / F - C =");
            			String select = scan.nextLine();
            			if(select.equals("C") || select.equals("c"))
            			{
	            			console.getOutputStream().print("Enter filename = ");
	            			filename = scan.nextLine();
	            			console.getOutputStream().print("Enter first int = ");
	            			byte startBlock = 0;
	            			try
	            			{
	            				startBlock = scan.nextByte();
	            			}
	            			catch (Exception e) {
	            				System.out.println("Wrong input");
							}
	            			console.getOutputStream().print("Enter second int = ");
	            			byte endBlock = 0;
	            			try
	            			{
	            				endBlock = scan.nextByte();
	            			}
	            			catch (Exception e) {
	            				System.out.println("Wrong input");
							}
	            			scan.nextLine();
	            			if(hdd.delete(filename, startBlock, endBlock)) ram.delete(filename,startBlock,endBlock);
	            			else System.out.println("An error is occured, please try again");
            			}
            			if(select.equals("F") || select.equals("f"))
            			{
            				console.getOutputStream().print("Enter filename = ");
	            			filename = scan.nextLine();
	            			if(hdd.delete(filename))ram.delete(filename);
            			}
            		}break;
            		case "Insert":
            		{
            			console.getOutputStream().print("Enter filename = ");
            			filename = scan.nextLine();
            			console.getOutputStream().print("Enter inserting order =");
            			byte block = 0;
            			try
            			{
            				block = scan.nextByte();
            			}
            			catch (Exception e) {
            				System.out.println("Wrong input");
						}
            			scan.nextLine();
            			console.getOutputStream().print("Enter the content =");
            			content = scan.nextLine();
            			if(hdd.insert(filename, block, content)) ram.insert(filename, block, content);
            			else System.out.println("An error is occured, please try again");
            		}break;
            		case "Update":
            		{
            			console.getOutputStream().print("Enter filename = ");
            			filename = scan.nextLine();
            			console.getOutputStream().print("Enter updating order =");
            			byte block = 0;
            			try
            			{
            				block = scan.nextByte();
            			}
            			catch (Exception e) {
            				System.out.println("Wrong input");
						}
            			scan.nextLine();
            			console.getOutputStream().print("Enter the content =");
            			content = scan.nextLine();
            			if(hdd.update(filename, block, content))ram.update(filename, block, content);
            			else
            			{
            				System.out.println("Wrong input");
            			}
            		}break;
            		case "Defrag":
            		{
        				hdd.defrag();
        				System.out.println("File(s) has been combined in vdisk");
        				scan.nextLine();
            		}break;
            		case "Store":
            		{
        				hdd.store();
        				System.out.println("File(s) has been stored to disk");
        				scan.nextLine();
            		}break;
            		case "Restore":
            		{
        				if(hdd.restore()){ ram.restore(); }
        				else System.out.println("An error is occured, please try again");
        				System.out.println("File(s) has been restored");
        				scan.nextLine();
            		}break;
            		case "Load":
            		{
            			hdd.makeEmptyBlocks(hdd.Blocks());
            			ram.makeEmptyList();
            			console.getOutputStream().print("file.txt = ");
            			String loadfile = scan.nextLine();
            			read(loadfile);
            			for (i = 0; i < loadCommand.length; i++) 
            			{
            				clear();
            				//System.out.println(loadCommand[i] + " = " + "OK");
            				String[] parseCommand = null;
            				try
            				{
            					parseCommand = loadCommand[i].split(" ");
            				}
            				catch (Exception e) {
								// TODO: handle exception
            					System.out.println("A problem has been dedected, try again");
							}
            				
            				int a = 0, commandLenght = 0;
            				while(a < parseCommand.length)
            				{
            					commandLenght++;
            					if(parseCommand[a].contains("\"") ) break;
            					a++;
            				}
            				switch (commandLenght)
            				{
	            				case 1: 
	            				{
	            					switch(parseCommand[0])
	            					{
	            					case "Printram(Name)":
	            						ram.printramOrderName();
	            						break;
	            					case "Printram(Number)":
	            						ram.printramOrderNumber();
	            						break;
	            					case "Printdisk":
	            						hdd.printDisk();
	            						break;
	            					case "Defrag":
	            						hdd.defrag();
	            						System.out.println("Defrag : OK");
	            						break;
	            					case "Store":
	            						hdd.store();
	            						System.out.println("Store : OK");
	            						break;
	            					case "Restore":
	            						if(hdd.restore()){ ram.restore(); }
	            						System.out.println("Restore : OK");
	            						break;
	            					}
	            				}break;
	            				case 2:
	            				{
		            					switch(parseCommand[0])
		            					{
			            					case "Print":
			            					{
			            						ram.print(parseCommand[1]);
			            					}break;
			            					case "Create":
			            					{
			            						index = hdd.create(parseCommand[1]);
			            						if(index != -1) ram.create(parseCommand[1],++index);
			            						System.out.println("create : OK" + " - " + parseCommand[1]);
			            					}break;
			            					case "Delete":
			            					{
			            						if(hdd.delete(parseCommand[1]))ram.delete(parseCommand[1]);
			            						System.out.println("Delete : OK" + " - " + parseCommand[1]);
			            					}break;
		            					}
	            				}break;
	            				case 3:
	            				{
	            					clear();
	            					String[] split1 = null;
	            					String[] split2 = null;
	            					try
	            					{
	            						split1 = loadCommand[i].split(" \"");
		            					split2 = split1[1].split("\"");
	            					}
	            					catch (Exception e) {
	            						System.out.println("A problem has been dedected, try again");
									}
	            					if(hdd.append(parseCommand[1], split2[0]))ram.append(parseCommand[1], split2[0]);
	            					System.out.println("Append : OK" + " " + parseCommand[1] + " " + split2[0]);
	            				}break;
	            				case 4:
	            				{
	            					switch(parseCommand[0])
	            					{
		            					case "Insert":
		            					{
		            						String[] split1 = null;
			            					String[] split2 = null;
			            					try
			            					{
			            						split1 = loadCommand[i].split(" \"");
				            					split2 = split1[1].split("\"");
			            					}
			            					catch (Exception e) {
			            						System.out.println("A problem has been dedected, try again");
											}
			            					if(hdd.insert(parseCommand[1], Integer.parseInt(parseCommand[2]) , split2[0])) ram.insert(parseCommand[1], Integer.parseInt(parseCommand[2]) , split2[0]);
			            					System.out.println("Insert : OK" + " " + parseCommand[1] + " " + parseCommand[2] + " " + split2[0]);
		            					}break;
		            					case "Delete":	
		            					{
		            						if(hdd.delete(parseCommand[1], Integer.parseInt(parseCommand[2]), Integer.parseInt(parseCommand[3])))
		            						ram.delete(parseCommand[1], Integer.parseInt(parseCommand[2]), Integer.parseInt(parseCommand[3]));	
		            						System.out.println("Delete : OK" + " " + parseCommand[1] + " " + parseCommand[2] + " " + parseCommand[3]);
		            					}break;
	            					}
	            				}break;
            				}
            				scan.nextLine();
						}
            			scan.nextLine();
            		}break;
            		}
            		Thread.sleep(500);
            		writeagain = true;
                }
                
				keypr=0;    // last action 
	         }
			Thread.sleep(100);
		}
		System.exit(-1);
		
	}
	
	// Control vdisk and RAM data which must be "a-z,A-Z,0-9", and special characters must be "., @"
	private boolean checkTheEntered(String command)
	{
		boolean control = false;
		for (int i = 0; i < command.length(); i++) {
			control = false;
			for (int j = 0; j < alphabet.length; j++) {
				if(command.charAt(i) == alphabet[j])
				{
					control = true;
				}
			}
			if(control == false)
			{
				break;
			}
		}
		return control;
	}
	// Read file on primary disk
	private void read(String loadfile) throws Exception // read file operation
	{
		 // The name of the file to open.
       String fileName = loadfile;
       // This will reference one line at a time
       String line = null;
       int i = 0;
       try 
       {
           FileReader fileReader = new FileReader(fileName);
           BufferedReader bufferedReader =  new BufferedReader(fileReader);
           i = 0;
           while((line = bufferedReader.readLine()) != null) 
           {
        	   i++;
           }
           
           loadCommand = new String[i];
           bufferedReader.close();         
           
           FileReader fileReader2 = new FileReader(fileName);
           BufferedReader bufferedReader2 =  new BufferedReader(fileReader2);
           i = 0;
           while((line = bufferedReader2.readLine()) != null) 
           {
        	   loadCommand[i] = line;
        	   i++;
           }
           bufferedReader2.close();     
       }
       catch(FileNotFoundException ex) {
    	   loadCommand = new String[i];
       	console.getOutputStream().println(
               "Unable to open file '" + 
               fileName + "'");                
       }
       catch(IOException ex) {
    	   loadCommand = new String[i];
       	console.getOutputStream().println(
               "Error reading file '" 
               + fileName + "'");                  
           // Or we could just do this: 
           // ex.printStackTrace();
       }
	}
	// How to use this program
	private void instruction()
	{
		System.out.println("load file.txt   // load and run an executable batch file from harddisk");
		System.out.println("print filename  // print the content of the file on the screen");
		System.out.println("create filename // create a file");
		System.out.println("append filename \"hello world\"//append data to the end of the file(as a block/blocks)");
		System.out.println("insert filename 3\"hello world\"//insert data into the file from the 3rd block (as a block/blocs)");
		System.out.println("delete filename // delete file");
		System.out.println("delete filename 5 7 // delete blocks from 5 to 7");
		System.out.println("printdisk // print all vdisk on the screen");
		System.out.println("defrag    // defragment vdisk");
		System.out.println("store     // save vdisk from harddisk as a file(vdisk.txt)");
		System.out.println("restore   // restore vdisk from harddisk,clear RAM structures,and create appropriate");
		System.out.println("new memory structures in the RAM");
	}
	// clear console
	private void clear()
	{
		for (int i = 0; i < console.getTextWindow().getRows()-1; i++) {
			for (int j = 0; j < console.getTextWindow().getColumns(); j++) {
				console.getTextWindow().setCursorPosition(j, i);
				console.getOutputStream().print(" ");
			}
		}
		console.getTextWindow().setCursorPosition(0, 0);
	}
	// Show main menu
	private void writeMenu()
	{
		for (int j = 3; j < commands.length+3; j++) 
        {
        	console.getTextWindow().setCursorPosition(3, j);
        	console.getOutputStream().print(commands[j-3]);
		}
	}
}
