import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import javax.swing.JOptionPane;


public class IDs{
	ArrayList<String> idx = new ArrayList<String>(); 
	ArrayList<String> idy = new ArrayList<String>(); 
	ArrayList<String> id_genelist1 = new ArrayList<String>(); // id of the genes. Note: idx.size can be unequal to this size. it Means there might be input genes not found.
	ArrayList<String> id_genelist1Sym = new ArrayList<String>(); // id_genelist1Sym.size = id_genelist1.size 
	ArrayList<String> id_genelist2 = new ArrayList<String>();
	ArrayList<String> id_genelist2Sym = new ArrayList<String>(); 
	ArrayList<String> id_genelist1Entrez = new ArrayList<String>();
	ArrayList<String> id_genelist2Entrez = new ArrayList<String>();
	ArrayList<String> idGO_genelist1 = new ArrayList<String>(); // GO for each id(gene). NOTE: it is possible a gene has no GO, so maybe idGO_genelist1(2).size != id_genelist1(2)Sym
	ArrayList<String> idGO_genelist2 = new ArrayList<String>();
	ArrayList<String> GOshared = new ArrayList<String>();
	ArrayList<String> idGOshared = new ArrayList<String>();
	ArrayList<String> Refinedid = new ArrayList<String>(); // id of the Refined GOs
	ArrayList<String> Refined = new ArrayList<String>(); // Refined GOs of the Refinedid
	ArrayList<Integer> Refined_BackgroundGene = new ArrayList<Integer>(); // Number of total genes inside a GO term. .size() = Refined.size()
	ArrayList<String> Refined_Score = new ArrayList<String>();
	ArrayList<String> Refinedsym = new ArrayList<String>();  
	ArrayList<String> RefinedEntrez = new ArrayList<String>();
	ArrayList<String> Refined_GOterm = new ArrayList<String>(); 
	ArrayList<String> Refined_GOcat = new ArrayList<String>(); 
	static List<String> CheckedOrNotList;

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
	//This function puts gene_list1/2 from TextAreas into idx/y.
	public void getIDs_genelist(){
		String genes_textArea_genelist1[] =  GOTrapper.textArea_genelist1.getText().split("\n");
		String genes_textArea_genelist2[] =  GOTrapper.textArea_genelist2.getText().split("\n");
		for(String line: genes_textArea_genelist1){
			if(idx.contains(line.trim())){
				JOptionPane.showMessageDialog(null, "Warning: You have a duplicated gene in Gene List 1 "
						+ "so automatically it will be removed : \n"+line.trim());
			}
			if(!line.trim().isEmpty()){
				if(!idx.contains(line.trim())){
					idx.add(line.trim());
				}
				
			}
		}
		for(String line: genes_textArea_genelist2){
			if(!line.trim().isEmpty()){
				if(idx.contains(line.trim())){
					JOptionPane.showMessageDialog(null, "Warning: You have a gene exists in both lists "
							+ "so automatically it will be removed from Gene List 2 : \n"+line.trim());
				}
				if(!idy.contains(line.trim()) && !idx.contains(line.trim())){
					idy.add(line.trim());
				}
				
			}
		}
	}
	// get the IDs of the gene symbols.
	public void getSymID(ArrayList<String> a, ArrayList<String> b, ArrayList<String> Entrez_a,ArrayList<String> Entrez_b, boolean radioButtonGeneSym){
		for (String x:idx){
			if(radioButtonGeneSym==true){
				for (String y:b){
					if (x.equals(y)){
						id_genelist1Sym.add(y);
						id_genelist1.add(a.get(b.indexOf(y)));
						id_genelist1Entrez.add(Entrez_b.get(b.indexOf(y)));
						
					}
				}
			}else{
				for (String y:Entrez_b){
					if (x.equals(y)){
						id_genelist1Sym.add(b.get(Entrez_b.indexOf(y)));
						id_genelist1.add(Entrez_a.get(Entrez_b.indexOf(y)));
						id_genelist1Entrez.add(y);
						
					}
				}
			}
			
		}
		for (String x:idy){
			if(radioButtonGeneSym==true){
				for (String y:b){
					if (x.equals(y)){
						id_genelist2Sym.add(y);
						id_genelist2.add(a.get(b.indexOf(y)));
						id_genelist2Entrez.add(Entrez_b.get(b.indexOf(y)));
						
					}
				}
			}else{
				for (String y:Entrez_b){
					if (x.equals(y)){
						id_genelist2Sym.add(b.get(Entrez_b.indexOf(y)));
						id_genelist2.add(Entrez_a.get(Entrez_b.indexOf(y)));
						id_genelist2Entrez.add(y);
						
					}
				}
			}
		}
}
	// get the GOs of the gene IDs
	public void getSymGO(ArrayList<String> a, ArrayList<String> b, ArrayList<String> idEvidenceCode){
		CheckedOrNotList = Arrays.asList(GOTrapper.CheckedOrNot); 
		ArrayList<String> temp = new ArrayList<String>();
		for (String x:id_genelist1){
			temp.clear();
			int d = 0;
			for (String y:a){
				if (x.equals(y) && !temp.contains(b.get(d)) && CheckedOrNotList.contains(idEvidenceCode.get(d))){
					temp.add(b.get(d));
				}
				d++;
			}
			// if a gene has no any GO, it(an empty set) will not be added into idGO_genelist. 
			if(!temp.isEmpty()){
			idGO_genelist1.add(temp.toString());
			}
		}
		for (String x:id_genelist2){
			temp.clear();
			int d = 0;
			for (String y:a){
				if (x.equals(y) && !temp.contains(b.get(d)) && CheckedOrNotList.contains(idEvidenceCode.get(d))){
					temp.add(b.get(d));
				}
				d++;
			}
			// if a gene has no any GO, it(an empty set) will not be added into idGO_genelist. 
			if(!temp.isEmpty()){
			idGO_genelist2.add(temp.toString());
			}
		}
		temp.clear();
	}
	// get shared GO terms
	public void getSharedGO(){
		int threshold = GOTrapper.threshold; // e.g. If 5 genes needed to be shared in a GOterm, the threshold is 4
		if(GOTrapper.rdbtnWithin.isSelected()==true){
			// Warning if insufficient genes provided
			if(GOTrapper.textArea_genelist1.getText().split("\n").length < 2){
				JOptionPane.showMessageDialog(null, "First list shouldn't be less than two genes");
			}
			//For loops for comparing within genelists
			for (String x:idGO_genelist1){ // example: [GO:XXX, GO:XXX, GO:XXX]
				int dx=idGO_genelist1.indexOf(x);
				java.util.List<String> items = Arrays.asList(x.replace("[", "").replace("]", "").split(","));
				for (String y:items){
					int j = 0; // number of genes share GO term with x gene
					for (String xx:idGO_genelist1){
						int dy=idGO_genelist1.indexOf(xx);
						/*
						 * this IF for optimization. First logic to avoid comparing same index(gene) to each other. 
						 * Second logic dx>=dy to avoid comparing two genes that already compared
						*/
						if(!Integer.toString(dx).equals(Integer.toString(dy)) && dx >= dy){
							java.util.List<String> items2 = Arrays.asList(xx.replace("[", "").replace("]", "").split(","));
								if (!GOshared.contains(y.trim()) && items2.contains(y) ){ 
									j++;
								}
						}
					}
					if(j>=threshold){
						GOshared.add(y.trim());
					}
				}
			}
		}else{
			// Warning if insufficient genes provided
			if(GOTrapper.textArea_genelist1.getText().isEmpty() || GOTrapper.textArea_genelist2.getText().isEmpty()){
				JOptionPane.showMessageDialog(null, "Both gene lists shouldn't be empty");
			}
		//For loops for comparing between genelists. 
		for (String x:idGO_genelist1){ // example: [GO:XXX, GO:XXX, GO:XXX]
			java.util.List<String> items = Arrays.asList(x.replace("[", "").replace("]", "").split(","));
			for (String y:items){
				int j = 0;
				for (String xx:idGO_genelist2){
						java.util.List<String> items2 = Arrays.asList(xx.replace("[", "").replace("]", "").split(","));
							if (!GOshared.contains(y.trim()) && items2.contains(y) ){ 
								j++;
							}
				}
				if(j>=threshold){
					GOshared.add(y.trim());
				}
			}
			
		}
		}
		//System.out.println("GOshared: "+GOshared); // to see all the shared GO terms before refinement
	}
	
	// Getting id of GO:XXX  e.g. 544 --> GO:0000724
	public void idSharedGO(ArrayList<String> a, ArrayList<String> b){
		for (String x:GOshared){
			if (b.contains(x)){
				idGOshared.add(a.get(b.indexOf(x)));
			}
		}
	}
	
	// remove the parents of idGOshared -- removing parents of shared GO terms (a:child, b:parent)
	public void ParentRemove(ArrayList<String> a, ArrayList<String> b){
		ArrayList<String> blacklist = new ArrayList<String>(); // stored parents which are in idGOshared
		for (String x:idGOshared){
			int d = 0; // tracking index of child-parent (index of xx)
			for (String xx:a){
				if (x.equals(xx)){
					String goparent = b.get(d); // b.get(d) returns the parent of index d
					if (idGOshared.contains(goparent) && !blacklist.contains(goparent)){
						blacklist.add(goparent);
					}
				}
				d++;
			}
		}
		for (String x:idGOshared){
			if(!blacklist.contains(x)){
				Refinedid.add(x);
			}
		}
		blacklist.clear();
		//System.out.println("Refinedid: "+Refinedid); 
	}
	
	// get back GO terms for the IDs
	public void RefinedGO(ArrayList<String> a, ArrayList<String> b){
		for (String x:Refinedid){
			if (a.contains(x)){
				Refined.add(b.get(a.indexOf(x)));
			}
		}
		//System.out.println("Refined: "+Refined); // printing all GO ids after refinement 
	}
	
	// Scoring function. NOTE: location of that 2 is --> ...(float)2/...
	public void Scoring(ArrayList<String> a, ArrayList<String> b){
		for (String x:Refined){
			if(a.contains(x)){
				//int j = Integer.parseInt(b.get(a.indexOf(x)));
				//usage: Math.log(n)/Math.log(base) ; base =log 2 or 10... 
				Refined_Score.add(String.format("%.4f",(-1)*Math.log((float)2/Integer.parseInt(b.get(a.indexOf(x))))/Math.log(2)));
				Refined_BackgroundGene.add(Integer.parseInt(b.get(a.indexOf(x))));
			}
		}
	}
	// get gene Sym for refined GOs.
	public void RefinedSym(ArrayList<String> a, ArrayList<String> b, ArrayList<String> aa, ArrayList<String> bb, ArrayList<String> GO_id, ArrayList<String> GO_term, ArrayList<String> GO_cat){
		if(GOTrapper.rdbtnWithin.isSelected()==true){
			for (String x:Refined){ 
				ArrayList<String> Sym = new ArrayList<String>();
				ArrayList<String> Entrez = new ArrayList<String>();
				int d=0; // track index of y
				for (String y:b){
					if(x.equals(y) && id_genelist1.contains(a.get(d)) && !Sym.contains(bb.get(aa.indexOf(a.get(d))))){
						Sym.add(bb.get(aa.indexOf(a.get(d))));
						Entrez.add(id_genelist1Entrez.get(id_genelist1Sym.indexOf(bb.get(aa.indexOf(a.get(d))))));
					}
					d++;
				}
				Refinedsym.add(Sym.toString());
				RefinedEntrez.add(Entrez.toString());
				Refined_GOterm.add(GO_term.get(GO_id.indexOf(x)));
				Refined_GOcat.add(GO_cat.get(GO_id.indexOf(x)));
			}
			for (int i=0; i<=Refined.size(); i++){
				GOTrapper.model.addRow(new Object[]{i+1, Refined.get(i), Refined_GOcat.get(i), Refined_BackgroundGene.get(i), Refinedsym.get(i), RefinedEntrez.get(i), Refined_Score.get(i), Refined_GOterm.get(i)});
			}
		}else{
			for (String x:Refined){ 
				ArrayList<String> Sym = new ArrayList<String>();
				ArrayList<String> Entrez = new ArrayList<String>();
				int d=0; // track index of y
				for (String y:b){
					if(x.equals(y) && (id_genelist1.contains(a.get(d)) || id_genelist2.contains(a.get(d))) && !Sym.contains(bb.get(aa.indexOf(a.get(d))))){
						Sym.add(bb.get(aa.indexOf(a.get(d))));
						if(id_genelist1Sym.contains(bb.get(aa.indexOf(a.get(d))))){
							Entrez.add(id_genelist1Entrez.get(id_genelist1Sym.indexOf(bb.get(aa.indexOf(a.get(d))))));
						}else{
							Entrez.add(id_genelist2Entrez.get(id_genelist2Sym.indexOf(bb.get(aa.indexOf(a.get(d))))));
						}
					}
					d++;
				}
				Refinedsym.add(Sym.toString());
				RefinedEntrez.add(Entrez.toString());
				Refined_GOterm.add(GO_term.get(GO_id.indexOf(x)));
				Refined_GOcat.add(GO_cat.get(GO_id.indexOf(x)));
			}
			for (int i=0; i<=Refined.size(); i++){
				GOTrapper.model.addRow(new Object[]{i+1, Refined.get(i), Refined_GOcat.get(i), Refined_BackgroundGene.get(i), Refinedsym.get(i).replace("[", "").replace("]", ""), RefinedEntrez.get(i).replace("[", "").replace("]", ""), Refined_Score.get(i), Refined_GOterm.get(i)});
			}
		}
	}
	
	
	public void ExportToFile(File FilePath) throws FileNotFoundException{
		PrintWriter pw = new PrintWriter(new File(FilePath+File.separator+"GOtoolResult.tsv"));
        StringBuilder sb = new StringBuilder();
        sb.append("id");
        sb.append("	");
    	sb.append("GO_id");
    	sb.append("	");
    	sb.append("GO_category");
    	sb.append("	");
    	sb.append("#_Background_Genes");
    	sb.append("	");
    	sb.append("Genes");
    	sb.append("	");
    	sb.append("Score");
    	sb.append("	");
    	sb.append("GO_term");
    	sb.append('\n');
        for (int i = 0; i < Refined.size(); i++){
        	sb.append(i+1);
        	sb.append("	");
        	sb.append(Refined.get(i));
        	sb.append("	");
        	sb.append(Refined_GOcat.get(i));
        	sb.append("	");
        	sb.append(Refined_BackgroundGene.get(i));
        	sb.append("	");
        	sb.append(Refinedsym.get(i).replace("[", "").replace("]", ""));
        	sb.append("	");
        	sb.append(Refined_Score.get(i));
        	sb.append("	");
        	sb.append(Refined_GOterm.get(i));
        	sb.append('\n');
        }
        pw.write(sb.toString());
        pw.close();
        JOptionPane.showMessageDialog(null, "Export is done!");
	}
	
	public void statistics(){
		if(GOTrapper.rdbtnWithin.isSelected()==true){
			JOptionPane.showMessageDialog(null, "# of Total Genes: "+(idx.size())+"\n# of Genes not exist: "+(idx.size()-id_genelist1.size())+"\n# of Genes without GO Term: "+
					(id_genelist1Sym.size()-idGO_genelist1.size())+"\n# of Processed Genes: "+idGO_genelist1.size()+"\n# of Shared GO Terms: "+idGOshared.size()+
					"\n# of Refined Shared GO Terms: "+Refinedid.size(),"Statistics Report", JOptionPane.INFORMATION_MESSAGE);
		}else{
			JOptionPane.showMessageDialog(null, "# of Total Genes: "+(idx.size()+idy.size())+"\n# of Genes not exist: "+((idx.size()+idy.size())-(id_genelist1.size()+id_genelist2.size()))+"\n# of Genes without GO Term: "+
					((id_genelist1Sym.size()+id_genelist2Sym.size())-(idGO_genelist1.size()+idGO_genelist2.size()))+"\n# of Processed Genes: "+
							(idGO_genelist1.size()+idGO_genelist2.size())+"\n# of Shared GO Terms: "+idGOshared.size()+
					"\n# of Refined Shared GO Terms: "+Refinedid.size(),"Statistics Report", JOptionPane.INFORMATION_MESSAGE);
		}
		
	
	}
		
	}

