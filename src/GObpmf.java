import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;


public class GObpmf {
	ArrayList<String> idx = new ArrayList<String>(); 
	ArrayList<String> idy = new ArrayList<String>(); 
	
	public void getIDs(String fileName) throws FileNotFoundException{
		Scanner file = new Scanner(new FileReader(fileName));
		while (file.hasNext()){
			String f = file.nextLine();
			String[] a = f.split("\t");
			String b = a[0].trim().replaceAll("\"", "");
			String c = a[1].trim().replaceAll("\"", "");
			idx.add(b);
			idy.add(c);
			
		}
		file.close();
	}

}
