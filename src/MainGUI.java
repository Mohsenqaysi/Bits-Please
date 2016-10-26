import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.demo.charts.ExampleChart;

import studentProjectAssiger.CandidateSolution;
import studentProjectAssiger.GA;
import studentProjectAssiger.PreferenceTable;
import studentProjectAssiger.SA;
import studentProjectAssiger.StudentEntry;

public class MainGUI {
	// Global variables
	private JFrame frmSoftware;
	private JLabel lblFileFormat;
	private String fileName = "";
	private JLabel lblFilePathIs;
	private JTextPane textPane;
	private JRadioButton rdbtnGa;
	private JRadioButton rdbtnSimulating;
	private ButtonGroup bGroup = new ButtonGroup();
	private JTextArea textAreaOutPut, textArea;
	private GA ga;
	private SA sa;
	private PreferenceTable file;
	private JLabel lblIGit = new JLabel("");
	private  Object[][] data = new Object[51][3];
	private JScrollPane scrollPane_2;
	private CandidateSolution BestSol = null;
	private JTable table_1;
	private  ExampleChart<PieChart> exampleChart;
	private  PieChart chart;
	private JPanel panelPieGraph;
	private String[] columNames = {"Student Name", "Project Preference", "Project Name"};  
	Thread GAThread = new Thread() {
		public void run() {
			getGA();
		}
	};
	
	Thread SAThread = new Thread() {
		public void run() {
			getSA();
		}
	};
	
	Thread ProgessBar = new Thread() {
		public void run() {
			setProgessBarWorking();
		}
	};
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGUI window = new MainGUI();
					window.frmSoftware.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// Set up the frame
		frmSoftware = new JFrame();
		frmSoftware.setLocation(14, -22);
		frmSoftware.setTitle("Student Project Assigner");
		frmSoftware.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		frmSoftware.getContentPane().setBounds(new Rectangle(0, 0, 23, 0));
		frmSoftware.setSize(978, 710);// set frame size
		frmSoftware.setLocationRelativeTo(null); // center the frame
		frmSoftware.setResizable(false); // Disable maximizing the window size
		frmSoftware.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSoftware.getContentPane().setLayout(null);
			/*
		 * +++++++++++_ All labels on the GUI _+++++++++++++++
		 */
		lableFormat("Please choose a file in .tsv format",Color.BLACK);
		JLabel lbCopyRight = new JLabel("© Bits Please Team 2016");
		lbCopyRight.setHorizontalAlignment(SwingConstants.CENTER);
		lbCopyRight.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lbCopyRight.setBounds(393, 659, 173, 23);
		frmSoftware.getContentPane().add(lbCopyRight);

		// lblFilePathIs label is hidden
		// it'll show when user choose a file
		lblFilePathIs = new JLabel("File Path:");
		lblFilePathIs.setBounds(6, 33, 75, 16);
		lblFilePathIs.setHorizontalAlignment(SwingConstants.LEFT);
		lblFilePathIs.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		frmSoftware.getContentPane().add(lblFilePathIs);

		textPane = new JTextPane();
		textPane.setBounds(55, 31, 228, 18);
//		fileName = "./src/Data/Projectallocationdata.tsv";
		textPane.setText(fileName);
		frmSoftware.getContentPane().add(textPane);

		/*
		 * +++++++++++++_ All buttons on the GUI _+++++++++++++++++
		 */
		JButton btnGenerateSolution = new JButton("Generate Solution");
		btnGenerateSolution.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		btnGenerateSolution.addActionListener(new ActionListener() {
			// Action performed when button clicked
			public void actionPerformed(ActionEvent e) {
				System.out.println("SAThread: "+ SAThread.isAlive());

				if (!fileName.endsWith(".txt") && !fileName.endsWith(".tsv") || fileName.isEmpty()) {
					// if the user did not pick a file
					JOptionPane.showMessageDialog(null, "There is no file ? Please choose one...", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else if(!GAThread.isAlive() && !SAThread.isAlive()){
					
					Emptylable();
					frmSoftware.update(frmSoftware.getGraphics());
					lableFormat("We will take care of the proccess Please Wait... :) ", Color.BLACK);

					if (rdbtnGa.isSelected()) {
						GAThread = new Thread() {
							public void run() {
								getGA();
								Sleep(1000);
								lblIGit.setIcon(new ImageIcon(MainGUI.class.getResource("")));

							}
						};
						setProgessBarWorking();
						GAThread.start();
					} else

					if (rdbtnSimulating.isSelected()) {
						SAThread = new Thread() {
							public void run() {
								getSA();
								Sleep(1000);
								lblIGit.setIcon(new ImageIcon(MainGUI.class.getResource("")));
							}
						};
						setProgessBarWorking();
						SAThread.start();
						System.out.println("SAThread: "+ SAThread.isAlive());
					}
					Emptylable();
					frmSoftware.update(frmSoftware.getGraphics());
					
				}
			}
		});

		btnGenerateSolution.setBounds(9, 439, 125, 37);
		frmSoftware.getContentPane().add(btnGenerateSolution);
		
		JButton btnClearPath = new JButton("Clear");
		btnClearPath.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		
		btnClearPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textPane.setText(null);
				lblFilePathIs.setText(null);
				fileName = ""; // empty the previous path
				Emptylable();
//				frmSoftware.update(frmSoftware.getGraphics());
				lableFormat("Please choose a file in .tsv format",Color.BLACK);
				textAreaOutPut.setText(null);
				textArea.setText(null);
				lblIGit.setIcon(new ImageIcon(MainGUI.class.getResource("")));

				for(int i = 0; i < 51; i++)
				{
					for(int j = 0; j < 3; j++)
						data[i][j] = "";
					
				}

				frmSoftware.getContentPane().add(lblIGit);
 				frmSoftware.update(frmSoftware.getGraphics());

			}
		});
		btnClearPath.setBounds(136, 440, 81, 37);
		frmSoftware.getContentPane().add(btnClearPath);
		JButton btnFilePath = new JButton(".....");
		btnFilePath.setToolTipText("choose a file in .tsv format");
		btnFilePath.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		btnFilePath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chooseFile();
			}
		});

		btnFilePath.setIcon(new ImageIcon(MainGUI.class.getResource("/Icons/Open-Folder.png")));
		btnFilePath.setBounds(289, 31, 70, 19);
		frmSoftware.getContentPane().add(btnFilePath);
		/*
		 * +++++++++++_ All JRadioButton on the GUI _+++++++++++++++
		 */
		rdbtnGa = new JRadioButton("Genetic Algorithm", true);
		rdbtnGa.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		rdbtnGa.setBounds(9, 302, 147, 23);
		frmSoftware.getContentPane().add(rdbtnGa);

		rdbtnSimulating = new JRadioButton("Simulating Annealing");
		rdbtnSimulating.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		rdbtnSimulating.setBounds(9, 326, 170, 23);
		frmSoftware.getContentPane().add(rdbtnSimulating);

		// Radio Buttons
		bGroup.add(rdbtnGa);
		bGroup.add(rdbtnSimulating);
		
		JButton btnSave = new JButton("Save");
		btnSave.setToolTipText("Save the file of the soultions to your computer");
		btnSave.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				if (rdbtnGa.isSelected()) {
					SaveFile(ga.getSolution());
				} else

				if (rdbtnSimulating.isSelected()) {
					SaveFile(sa.getSolution(50));
				}
			}
		});
		btnSave.setIcon(new ImageIcon(MainGUI.class.getResource("/Icons/Save-icon.png")));
		btnSave.setBounds(607, 642, 108, 37);
		frmSoftware.getContentPane().add(btnSave);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(9, 354, 164, 73);
		frmSoftware.getContentPane().add(scrollPane);
		
		textAreaOutPut = new JTextArea();
		scrollPane.setViewportView(textAreaOutPut);
		textAreaOutPut.setEditable(false);
		textAreaOutPut.setLineWrap(true);
		
		JLabel lblSatisfaction = new JLabel("<html>"
				+"<u>"+"Satisfaction"+"</u>"+"</html>");
		lblSatisfaction.setToolTipText("<html>" + "<br/>" + "How the satisfaction score is calculated." + "<BR>"
				+ "<br/>" + "A perfect score for a single student is 100" + "<br/>"
				+ "providing they got their 1st preference." + "<br/>" + "For each student in the solution we get the"
				+ "<br/>" + "ranking of their assigned preference, times" + "<br/>"
				+ "it by 10 and subtract that number from " + "<br/>" + "a perfect score which is 100." + "<BR>"
				+ "<br/>" + "Meaning 1st=100, 2nd=90, 3rd=80 etc.." + "<BR>" + "<br/>"
				+ "For each student we then add all these" + "<br/>" + "scores together and that will be the" + "<br/>"
				+ "satisfaction" + "</html>");
		lblSatisfaction.setIcon(new ImageIcon(MainGUI.class.getResource("/Icons/info.png")));
		lblSatisfaction.setFont(new Font("Times New Roman", Font.BOLD, 13));
		lblSatisfaction.setBounds(176, 373, 93, 22);
		frmSoftware.getContentPane().add(lblSatisfaction);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(377, 11, 11, 639);
		frmSoftware.getContentPane().add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(393, 328, 579, 12);
		frmSoftware.getContentPane().add(separator_1);

		/*
		 * +++++++++++++_ Table _+++++++++++++++++
		 */
				JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(393, 342, 579, 297);
		frmSoftware.getContentPane().add(scrollPane_1);
	
		table_1 = new JTable(data, columNames);
		table_1.setColumnSelectionAllowed(true);
		table_1.setCellSelectionEnabled(true);
		table_1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table_1.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		table_1.setPreferredScrollableViewportSize(new Dimension(10, 10));
		scrollPane_1.setViewportView(table_1);
		if (!table_1.isVisible())
			scrollPane_1.setVisible(false);
		/*
		 * +++++++++++++_ Table _+++++++++++++++++
		 */
		if (!table_1.isVisible())
			scrollPane_2.setVisible(false);
		scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(6, 82, 354, 179);
		frmSoftware.getContentPane().add(scrollPane_2);
		
//		scrollPane_1.setVisible(false);
		
		JButton btnBestSoultion = new JButton("Load Best Solution");
		btnBestSoultion.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		btnBestSoultion.setIcon(new ImageIcon(MainGUI.class.getResource("/Icons/re.png")));
		btnBestSoultion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(BestSol != null){
				
				try {
					textAreaOutPut.setText(null);
					textAreaOutPut.getDocument().insertString(0, "Best Solution:\nBest Energy: " + BestSol.getEnergy() +"\nSatisfaction rate: "+BestSol.getSatisfaction()+ "%\n", null);
					   int j = 0;
					   for(StudentEntry student : file.getAllStudentEntries())
					   {
					    data[j][0] = student.getStudentName();
					    data[j][1] = student.getRanking(BestSol.getAssignmentFor(student.getStudentName()).getAssignedProject())+1;
					    data[j][2] = BestSol.getAssignmentFor(student.getStudentName()).getAssignedProject();
					      j++;
					   }
					textAreaOutPut.update(textAreaOutPut.getGraphics());
					table_1.update(table_1.getGraphics());	
					SetPieData(BestSol);
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
				
			}else{
					try {
						textAreaOutPut.setText(null);
						textAreaOutPut.getDocument().insertString(0, "No solution so far..." + "\n", null);
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
					textAreaOutPut.update(textAreaOutPut.getGraphics());

				}
			}

		});
		btnBestSoultion.setBounds(727, 642, 147, 36);
		frmSoftware.getContentPane().add(btnBestSoultion);
		
		textArea = new JTextArea ();
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		
	
	}
	
	/*
	 * +++++++++++++_ Methods _+++++++++++++++++
	 */

	private void lableFormat(String s, Color c) {
		lblFileFormat = new JLabel(s);
		lblFileFormat.setHorizontalAlignment(SwingConstants.CENTER);
		lblFileFormat.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblFileFormat.setForeground(c);
		lblFileFormat.setBounds(9, 4, 299, 23);
		frmSoftware.getContentPane().add(lblFileFormat);
	}
	
	private void Emptylable() {
		lblFileFormat.setText(" ");
		 Sleep(100);
	}
	
	// Load the file form the System
	private void chooseFile() {
		final JFileChooser chooseFile = new JFileChooser();
		chooseFile.setAcceptAllFileFilterUsed(false);
		chooseFile.setFileFilter(new FileNameExtensionFilter("Files .tsv", "TSV","tsv"));
		int ok = chooseFile.showOpenDialog(chooseFile);
		if (ok == JFileChooser.APPROVE_OPTION) {
			fileName = chooseFile.getSelectedFile().toString();
			Emptylable();
			textAreaOutPut.setText(null);
			frmSoftware.update(frmSoftware.getGraphics());
			lableFormat("File has been crrectlly loaded :)",Color.BLACK);
			frmSoftware.update(frmSoftware.getGraphics());
			textPane.setText(fileName);
	

		file = new PreferenceTable(fileName);
		try {
			textArea.getDocument().insertString(0,
					"Number of Students: "+file.getNumberOfStudents()+"\n"
					+"\nNumber of Projects: "+file.getNumberOfProjects()+"\n"
					+"\nNumber of pre-assigned projects: "+file.getNumberOfPreassigned()+"\n"
					+"\nMost popular project:\n"+file.mostPopularProject()

					,null);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}
		scrollPane_2.setViewportView(textArea);

	}
	
	private void SaveFile(CandidateSolution sol) {
		final JFileChooser chooseFile = new JFileChooser();
		chooseFile.setAcceptAllFileFilterUsed(false);
		chooseFile.setFileFilter(new FileNameExtensionFilter("Files .tsv","TSV","tsv"));
		int ok = chooseFile.showSaveDialog(chooseFile);
		if (ok == JFileChooser.APPROVE_OPTION) {
			fileName = chooseFile.getSelectedFile().toString();
			Emptylable();
			textAreaOutPut.setText(null);
			frmSoftware.update(frmSoftware.getGraphics());
			lableFormat("File is Saved!",Color.BLACK);
			frmSoftware.update(frmSoftware.getGraphics());
			lblFilePathIs.setText("File path is: ");
			textPane.setText(fileName);
			
			// output the file to the OS
			String str = "Bits Please Team 2016\nStudent Name\tProject Preference\tProject Name\n";

			for (StudentEntry student : file.getAllStudentEntries()) {

				str += student.getStudentName() + "\t"
						+ (student.getRanking(sol.getAssignmentFor(student.getStudentName()).getAssignedProject()) + 1)
						+ "\t" + sol.getAssignmentFor(student.getStudentName()).getAssignedProject() + "\n";
			}
			Writer writer = null;
			try {
				writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName + ".tsv"), "utf-8"));
				writer.write(str);
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				try {
					writer.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		} 
	}

	// sleep for n millis before changing the label
	private void Sleep(long n) {
		try {
			Thread.sleep(n);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void getGA() {
		try {
			 file = new PreferenceTable(fileName);
			 file.fillPreferenceOfAll(10);
			ga = new GA(file);
			int gaEnrgy =  ga.getSolution().getEnergy();
			CandidateSolution sol = ga.getSolution();
			if(BestSol == null || sol.getEnergy() < BestSol.getEnergy()){
			   BestSol = new CandidateSolution(sol);
			}
			
			textAreaOutPut.setText(null);
			textAreaOutPut.getDocument().insertString(0, "Genetic Algorithm:\nBest Energy: " + gaEnrgy +"\nSatisfaction rate: "+sol.getSatisfaction()+ "%\n", null);
	
			   int j = 0;
			   for(StudentEntry student : file.getAllStudentEntries())
			   {
			    data[j][0] = student.getStudentName();
			    data[j][1] = student.getRanking(sol.getAssignmentFor(student.getStudentName()).getAssignedProject())+1;
			    data[j][2] = sol.getAssignmentFor(student.getStudentName()).getAssignedProject();
			      j++;
			   }
			textAreaOutPut.update(textAreaOutPut.getGraphics());
			table_1.update(table_1.getGraphics());	
			SetPieData(sol);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void getSA() {
		file = new PreferenceTable(fileName);
		file.fillPreferenceOfAll(10);
		sa = new SA(file);
		CandidateSolution sol = sa.getSolution(200);
		int saEnergy = sol.getEnergy();
		if(BestSol == null || sol.getEnergy() < BestSol.getEnergy()){
			   BestSol = new CandidateSolution(sol);
			}
		try {
			textAreaOutPut.setText(null);
		textAreaOutPut.getDocument().insertString(0,"Simulating Annealing:\nBest Energy: " + saEnergy +"\nSatisfaction rate: "+sol.getSatisfaction()+ "%\n", null);

		   int j = 0;
		   for(StudentEntry student : file.getAllStudentEntries())
		   {
		    data[j][0] = student.getStudentName();
		    data[j][1] = student.getRanking(sol.getAssignmentFor(student.getStudentName()).getAssignedProject())+1;
		    data[j][2] = sol.getAssignmentFor(student.getStudentName()).getAssignedProject();
		      j++;
		   }
			textAreaOutPut.update(textAreaOutPut.getGraphics());
			table_1.update(table_1.getGraphics());	
			SetPieData(sol);

		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	private void setProgessBarWorking(){
		
		lblIGit.setBounds(9, 450, 366, 150);
		lblIGit.setIcon(new ImageIcon(MainGUI.class.getResource("/Icons/loading.gif")));
		lblIGit.setHorizontalAlignment(SwingConstants.LEFT);
		frmSoftware.getContentPane().add(lblIGit);
		frmSoftware.setVisible(true);
		
	}
	
	/*
	 * +++++++++++++_ PieChart Frame _+++++++++++++++++
	 * We use the XChart library to help create the PieChart
	 */
	
	private void SetPieData(CandidateSolution sol){
	 exampleChart = new PieChart02(sol.getHistogramData());
	chart = exampleChart.getChart();
	panelPieGraph = new XChartPanel<PieChart>(chart);
//	panelPieGraph.setToolTipText(
//			"<html>" + "The graph show the satisfaction rate of"
//			+ "<BR>"
//			+ "<br/>" + "the student over all fro the project that"
//			+ "been assigned to them from their 10 prefrences the picked"
//			+ "<BR>"
//			+ "<br/>" + "</html>");
	panelPieGraph.setBounds(393, -10, 579, 337);
	
	frmSoftware.getContentPane().add(panelPieGraph);
	panelPieGraph.update(panelPieGraph.getGraphics());
	frmSoftware.getContentPane().remove(panelPieGraph);

	
	/*
	 * +++++++++++++_ PieChart Frame _+++++++++++++++++
	 */		
	}
}