
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;


public class GOdetail {
	ArrayList<String> id = new ArrayList<String>(); // id : first column
	ArrayList<String> GO_id = new ArrayList<String>(); // GO id : GO:xxx
	ArrayList<String> GO_term = new ArrayList<String>(); // GO term
	ArrayList<String> GO_cat = new ArrayList<String>(); // GO category : bp,mf

	public void getIDs(String fileName) throws FileNotFoundException{
		Scanner file = new Scanner(new FileReader(fileName));
		while (file.hasNext()){
			String f = file.nextLine();
			String[] a = f.split("\t");  
			String b = a[0].trim().replaceAll("\"", "");
			String c = a[1].trim().replaceAll("\"", "");
			String d = a[2].trim().replaceAll("\"", "");
			String e = a[3].trim().replaceAll("\"", "");
			id.add(b);
			GO_id.add(c);
			GO_term.add(d);
			GO_cat.add(e);
		}
		file.close();
	}
	
}
