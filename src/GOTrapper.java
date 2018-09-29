/*
 * GOTrapper
 */
import java.awt.EventQueue;
import java.awt.Insets;
import java.io.FileNotFoundException;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JRadioButton;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class GOTrapper {
	static IDs IDsgenes;
	static GObpmf IDsGObpmf;
	static GOid IDsGOid;
	static GObpmf IDsGOidSym;
	static GObpmf bpmfGOvsNumgene;
	static GOdetail GO_detail;
	private boolean btnGO_pressed = false;
	public boolean radioButtonWithin = true;
	public static int threshold = 1; // 1 means a GO term shared by 2 genes. 
	public boolean CanExport = false; // to check if Export button should work
	private JFrame frame;
	private SwingWorker<Void,String> worker;
	public static JTextArea textArea_genelist1;
	public static JTextArea textArea_genelist2;
	public static JRadioButton rdbtnWithin;
	public static JRadioButton rdbtnBetween;
	public static JSpinner spinner;
	private JPanel panel;
	private JTable table;
	private JScrollPane scrollPane;
	public static DefaultTableModel model;
	private JCheckBox checkBoxEXP;
	private JCheckBox checkBoxIBA;
	static String[] CheckedOrNot;
	private JCheckBox checkBoxIC;
	private JCheckBox checkBoxIDA;
	private JCheckBox checkBoxIEA;
	private JCheckBox checkBoxIGI;
	private JCheckBox checkBoxIMP;
	private JCheckBox checkBoxIPI;
	private JCheckBox checkBoxISS;
	private JCheckBox checkBoxNAS;
	private JCheckBox checkBoxND;
	private JCheckBox checkBoxTAS;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws FileNotFoundException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GOTrapper window = new GOTrapper();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws FileNotFoundException 
	 */
	public GOTrapper() throws FileNotFoundException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws FileNotFoundException 
	 */
	@SuppressWarnings("serial")
	private void initialize() throws FileNotFoundException {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 585, 616);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		// Start of the GO button
		JButton btnGo = new JButton("GO");
		btnGo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (!btnGO_pressed) {
		            btnGO_pressed = true; 
				// Start Thread 
		            worker = new SwingWorker<Void, String>(){
		    			@Override
		    			protected Void doInBackground() throws Exception {
		    				UpdateVariables();
		    				ClearTable(); 
		    				runGO();
		    				return null;
		    			}
		    			@Override
		    			protected void done() {
		    				btnGO_pressed = false;
		    				
		    			}
		    		}; 
		    		worker.execute();
		    		CanExport = true;
				// END Thread
				}else{
					JOptionPane.showMessageDialog(null, "Wait until your first task's finished. ");
				}
			}
		});
		btnGo.setBounds(469, 50, 75, 48);
		frame.getContentPane().add(btnGo);
		// END of the GO button
		
		//Start Spinner = threshold
		SpinnerNumberModel spi = new SpinnerNumberModel(2, 2, 30000, 1); 
		spinner = new JSpinner(spi);
		spinner.setToolTipText("Number of genes in a shared GO term");
		spinner.setBounds(357, 76, 65, 28);
		frame.getContentPane().add(spinner);
		//END Spinner
		
		//Start Panel & textArea_genelist1/2 & ScrollBar
		panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, UIManager.getColor("RadioButton.shadow")));
		panel.setBackground(UIManager.getColor("ProgressBar.background"));
		panel.setBounds(20, 50, 242, 223);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		textArea_genelist1 = new JTextArea();
		textArea_genelist1.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		textArea_genelist1.setForeground(UIManager.getColor("Button.highlight"));
		textArea_genelist1.setBackground(UIManager.getColor("CheckBoxMenuItem.selectionBackground"));
		textArea_genelist1.setToolTipText("type your first gene list here");
		textArea_genelist1.setTabSize(0);
		textArea_genelist1.setLineWrap(true);
		textArea_genelist1.setMargin(new Insets(5, 4, 1, 2));
		
		textArea_genelist2 = new JTextArea();
		textArea_genelist2.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		textArea_genelist2.setBackground(UIManager.getColor("RadioButton.select"));
		textArea_genelist2.setTabSize(0);
		textArea_genelist2.setLineWrap(true);
		textArea_genelist2.setToolTipText("type your second gene list here");
		textArea_genelist2.setMargin(new Insets(5, 4, 1, 2));
		
		JScrollPane Scroll_textArea_genelist1 = new JScrollPane();
		Scroll_textArea_genelist1.setBounds(6, 6, 114, 211);
		panel.add(Scroll_textArea_genelist1);
		Scroll_textArea_genelist1.setViewportView(textArea_genelist1);
		
		JScrollPane Scroll_textArea_genelist2 = new JScrollPane();
		Scroll_textArea_genelist2.setBounds(122, 6, 114, 211);
		panel.add(Scroll_textArea_genelist2);
		Scroll_textArea_genelist2.setViewportView(textArea_genelist2);
		//END Panel & textArea_genelist1/2 & ScrollBar
		
		//Start Radio Buttons
		rdbtnWithin = new JRadioButton("Within");
		rdbtnWithin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(btnGO_pressed==false){
					rdbtnWithin.setSelected(true);
					radioButtonWithin = true;
				}else{ 
					if(radioButtonWithin==false){
						rdbtnBetween.setSelected(true);
					}
				}
			}
		});
		rdbtnWithin.setSelected(true);
		rdbtnWithin.setBounds(274, 50, 75, 23);
		frame.getContentPane().add(rdbtnWithin);
		
		rdbtnBetween = new JRadioButton("Between");
		rdbtnBetween.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(btnGO_pressed==false){
					rdbtnBetween.setSelected(true);
					radioButtonWithin = false;
				}else{ // when the program is running means GO button is pressed
					if(radioButtonWithin==true){
						rdbtnWithin.setSelected(true);
					}
				}
			}
		});
		rdbtnBetween.setSelected(false);
		rdbtnBetween.setBounds(347, 50, 84, 23);
		frame.getContentPane().add(rdbtnBetween);
		
		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnWithin);
		group.add(rdbtnBetween);
		//END Radio Buttons
		
		//Start JTable
		scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 291, 487, 177);
		frame.getContentPane().add(scrollPane);
		model = new DefaultTableModel(new Object[0][], new String[] {
                 }) {
			// Explain (1)
		    @Override
		    public Class<?> getColumnClass(int column) {
		        if (column == 0) {
		            return Integer.class;
		        }else if(column==3){
		        	return Integer.class;
		        }
		        return super.getColumnClass(column);
		    }
		}; 
		table = new JTable(model);
		table.setDefaultEditor(Object.class, null);
		table.setRowSelectionAllowed(false);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollPane.setViewportView(table);
		model.addColumn("#"); 
		model.addColumn("GO ID"); 
		model.addColumn("GO Category");
		model.addColumn("# Background Genes");
		model.addColumn("Genes"); 
		model.addColumn("Score"); 
		model.addColumn("GO Term"); 
		table.getColumnModel().getColumn(0).setPreferredWidth(40); // controlling column width
		table.getColumnModel().getColumn(1).setPreferredWidth(90); 
		table.getColumnModel().getColumn(2).setPreferredWidth(80);
		table.getColumnModel().getColumn(3).setPreferredWidth(125);
		table.getColumnModel().getColumn(4).setPreferredWidth(135);
		table.getColumnModel().getColumn(6).setPreferredWidth(190); 
				// Start Sorting
				// Explain (2)
		table.setAutoCreateRowSorter(true);
				// End Sorting
      //END JTable
		
		frame.getContentPane().setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{textArea_genelist2, textArea_genelist1, spinner, btnGo}));
		
		// Export button
		final JButton btnExport = new JButton("Export");
		btnExport.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(CanExport == true){
					JFileChooser f = new JFileChooser();
			        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
			        f.setDialogTitle("Export To Folder");
			        if(f.showOpenDialog(btnExport)==JFileChooser.APPROVE_OPTION){
			        	try {
							IDsgenes.ExportToFile(f.getSelectedFile().getAbsoluteFile());
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
			        }
				}else{
					JOptionPane.showMessageDialog(null, "No Data To Export");
				}
			}
		});
		btnExport.setBounds(30, 487, 117, 29);
		frame.getContentPane().add(btnExport);	
		// End Export button
		
		// Start Statistics button
		JButton btnStatistics = new JButton("Statistics");
		btnStatistics.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(CanExport == true){
					IDsgenes.statistics();
					}else{
						JOptionPane.showMessageDialog(null, "Statistics is not available");
					}
			}
		});
		btnStatistics.setBounds(145, 487, 117, 29);
		frame.getContentPane().add(btnStatistics);
		// End Statistics button
		
		// Start Info button
		JButton btnInfo = new JButton("Info");
		btnInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Quick tips:"+"\n- Within : to compare a list of genes with each other"+
						"\n- Between : to compare two lists of genes with each other."+
						"\n- Threshold : the minimum number of genes to be found in each returned shared GO term.\n"
						+ "   The bigger the 'Threshold', the less specific the returned shared GO terms are."+
						"\n- Background genes : total number of genes annotated to a GO term."+
						"\n- Score : the lower the score, the more specific the term is.","Tips", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		btnInfo.setBounds(260, 487, 117, 29);
		frame.getContentPane().add(btnInfo);
		// End Info Button
		
		// Evidence Code Panel
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Evidence Code", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(274, 109, 233, 164);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		checkBoxEXP = new JCheckBox("EXP");
		checkBoxEXP.setToolTipText("Inferred from Experiment");
		checkBoxEXP.setSelected(true);
		panel_1.add(checkBoxEXP, "2, 2");
		
		checkBoxIGI = new JCheckBox("IGI");
		checkBoxIGI.setToolTipText("Inferred from Genetic Interaction");
		checkBoxIGI.setSelected(true);
		panel_1.add(checkBoxIGI, "4, 2");
		
		checkBoxND = new JCheckBox("ND");
		checkBoxND.setToolTipText("No biological Data available");
		checkBoxND.setSelected(true);
		panel_1.add(checkBoxND, "6, 2");
		
		checkBoxIBA = new JCheckBox("IBA");
		checkBoxIBA.setToolTipText("Inferred from Biological aspect of Ancestor");
		checkBoxIBA.setSelected(true);
		panel_1.add(checkBoxIBA, "2, 4");
		
		checkBoxIMP = new JCheckBox("IMP");
		checkBoxIMP.setToolTipText("Inferred from Mutant Phenotype");
		checkBoxIMP.setSelected(true);
		panel_1.add(checkBoxIMP, "4, 4");
		
		checkBoxTAS = new JCheckBox("TAS");
		checkBoxTAS.setToolTipText("Traceable Author Statement");
		checkBoxTAS.setSelected(true);
		panel_1.add(checkBoxTAS, "6, 4");
		
		checkBoxIC = new JCheckBox("IC");
		checkBoxIC.setToolTipText("Inferred by Curator");
		checkBoxIC.setSelected(true);
		panel_1.add(checkBoxIC, "2, 6");
		
		checkBoxIPI = new JCheckBox("IPI");
		checkBoxIPI.setToolTipText("Inferred from Physical Interaction");
		checkBoxIPI.setSelected(true);
		panel_1.add(checkBoxIPI, "4, 6");
		
		checkBoxIDA = new JCheckBox("IDA");
		checkBoxIDA.setToolTipText("Inferred from Direct Assay");
		checkBoxIDA.setSelected(true);
		panel_1.add(checkBoxIDA, "2, 8");
		
		checkBoxISS = new JCheckBox("ISS");
		checkBoxISS.setToolTipText("Inferred from Sequence or structural Similarity");
		checkBoxISS.setSelected(true);
		panel_1.add(checkBoxISS, "4, 8");
		
		checkBoxIEA = new JCheckBox("IEA");
		checkBoxIEA.setToolTipText("Inferred from Electronic Annotation");
		checkBoxIEA.setSelected(true);
		panel_1.add(checkBoxIEA, "2, 10");
		
		checkBoxNAS = new JCheckBox("NAS");
		checkBoxNAS.setToolTipText("Non-traceable Author Statement");
		checkBoxNAS.setSelected(true);
		panel_1.add(checkBoxNAS, "4, 10");
		
		JLabel lblGeneList = new JLabel("Gene List 1");
		lblGeneList.setBounds(44, 27, 75, 16);
		frame.getContentPane().add(lblGeneList);
		
		JLabel lblGeneList_1 = new JLabel("Gene List 2");
		lblGeneList_1.setBounds(161, 27, 75, 16);
		frame.getContentPane().add(lblGeneList_1);
		
		JLabel lblThreshold = new JLabel("Threshold:");
		lblThreshold.setBounds(284, 82, 84, 16);
		frame.getContentPane().add(lblThreshold);
	}
	
	public void runGO() throws FileNotFoundException{
		CallIDs("","A");
		CallIDs("GOidvsGeneSym.tsv","G");
		CallIDs("GObpALLvsid.tsv","E");
		CallIDs("GOmfALLvsid.tsv","F");
		
		IDsgenes.getSymID(IDsGOidSym.idx,IDsGOidSym.idy);
		IDsgenes.getSymGO(IDsGOid.idx,IDsGOid.idy, IDsGOid.idEvidenceCode);
		IDsgenes.getSharedGO();
		
		CallIDs("goterm.tsv","B");
		IDsgenes.idSharedGO(GO_detail.id,GO_detail.GO_id);
		CallIDs("gobp.tsv","C"); //child-parent
		CallIDs("gomf.tsv","D"); //child-parent
		IDsgenes.ParentRemove(IDsGObpmf.idx,IDsGObpmf.idy);
		
		IDsgenes.RefinedGO(GO_detail.id,GO_detail.GO_id);
		
		CallIDs("bpGOvsNumgene.tsv","H"); // GO-NumberOfGenes
		CallIDs("mfGOvsNumgene.tsv","I"); // GO-NumberOfGenes
		IDsgenes.Scoring(bpmfGOvsNumgene.idx, bpmfGOvsNumgene.idy);
		IDsgenes.RefinedSym(IDsGOid.idx, IDsGOid.idy, IDsGOidSym.idx, IDsGOidSym.idy,GO_detail.GO_id,GO_detail.GO_term,GO_detail.GO_cat);
	}
	
	public void CallIDs(String x, String y) throws FileNotFoundException{
		if(y=="A"){
			IDsgenes = new IDs();
			IDsgenes.getIDs_genelist();
		}else if(y=="B"){
			GO_detail = new GOdetail();	
			GO_detail.getIDs(x);
		}else if(y=="C"){ //child-parent
			IDsGObpmf = new GObpmf();	
			IDsGObpmf.getIDs(x);	
		}else if(y=="D"){ //child-parent
			IDsGObpmf.getIDs(x);	
		}else if(y=="E"){
			IDsGOid = new GOid(); //id of gene sym vs GOs	
			IDsGOid.getIDs2(x);
		}else if(y=="F"){
			IDsGOid.getIDs2(x);
		}else if(y=="H"){
			bpmfGOvsNumgene = new GObpmf();
			bpmfGOvsNumgene.getIDs(x);
		}else if(y=="I"){
			bpmfGOvsNumgene.getIDs(x);
		}else{ // "G"
			IDsGOidSym = new GObpmf();	
			IDsGOidSym.getIDs(x);
		}
	}
	
	//Update general variables when GO btn pressed
	public void UpdateVariables(){
		threshold = ((Integer) spinner.getValue())-1;
		UpdateCheckBox();
	}
	
	// function to clear the table before the next run
	public void ClearTable(){
		int rows = model.getRowCount(); 
		for(int i = rows - 1; i >=0; i--)
		{
		   model.removeRow(i); 
		}
	}
	public void UpdateCheckBox(){
		CheckedOrNot = new String[12];
        if(checkBoxEXP.isSelected()){
        	CheckedOrNot[0] = "EXP";
        }if(checkBoxIBA.isSelected()){
        	CheckedOrNot[1] = "IBA";
        }if(checkBoxIC.isSelected()){
        	CheckedOrNot[2] = "IC";
        }if(checkBoxIDA.isSelected()){
        	CheckedOrNot[3] = "IDA";
        }if(checkBoxIEA.isSelected()){
        	CheckedOrNot[4] = "IEA";
        }if(checkBoxIGI.isSelected()){
        	CheckedOrNot[5] = "IGI";
        }if(checkBoxIMP.isSelected()){
        	CheckedOrNot[6] = "IMP";
        }if(checkBoxIPI.isSelected()){
        	CheckedOrNot[7] = "IPI";
        }if(checkBoxISS.isSelected()){
        	CheckedOrNot[8] = "ISS";
        }if(checkBoxNAS.isSelected()){
        	CheckedOrNot[9] = "NAS";
        }if(checkBoxND.isSelected()){
        	CheckedOrNot[10] = "ND";
        }if(checkBoxTAS.isSelected()){
        	CheckedOrNot[11] = "TAS";
        }
	}
}


