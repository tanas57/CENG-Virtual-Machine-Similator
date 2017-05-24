
import Hardware.*;
import enigma.core.Enigma;
public class main {
	public static enigma.console.Console console = Enigma.getConsole("CENG Virtual Machine", 90, 22, 14, 4);

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		HDD hdd = new HDD(30, 10);
		RAM ram = new RAM(hdd);
		CPU cpu = new CPU();
	}

}
