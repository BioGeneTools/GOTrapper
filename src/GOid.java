import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;


public class GOid {
	ArrayList<String> idx = new ArrayList<String>();
	ArrayList<String> idy = new ArrayList<String>(); 
	ArrayList<String> idEvidenceCode = new ArrayList<String>(); 
	
		public void getIDs2(String fileName) throws FileNotFoundException{
			Scanner file = new Scanner(new FileReader(fileName));
			while (file.hasNext()){
				String f = file.nextLine();
				String[] a = f.split("\t");
				String b = a[0].trim().replaceAll("\"", "");
				String c = a[1].trim().replaceAll("\"", "");
				String EvidenceCode = a[2].trim().replaceAll("\"", "");
				idx.add(b);
				idy.add(c);
				idEvidenceCode.add(EvidenceCode);
				
			}
			file.close();
		}

}
