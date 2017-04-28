import javax.swing.text.html.HTMLDocument.HTMLReader.CharacterAction;

import Hardware.*;
import enigma.core.Enigma;
public class main {
	public static enigma.console.Console console = Enigma.getConsole("CENG Virtual Machine", 90, 22, 15, 6);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		
		HDD hdd = new HDD(30);
		RAM ram = new RAM(hdd);
		CPU cpu = new CPU();
		
		/*hdd.writeFile("by tayo", "merhaba ben tayyip bea");

		hdd.writeFile("programme", "our class programm are here asdsadad123123123");
		
		hdd.updateFile("by tayo", "content");
		
		hdd.deleteContent("by tayo", 2, 2); // by tayo dosyasının 2.blokunu silecek
		*/
		hdd.printBlock();
	}

}
