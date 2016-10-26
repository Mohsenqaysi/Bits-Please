package studentProjectAssiger;
import java.io.BufferedReader;
import java.util.Hashtable;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Random;

public class PreferenceTable {
	
	private Hashtable<String,StudentEntry> hashtable_entries = new Hashtable<String,StudentEntry>(); // initialize variables
	private Vector<StudentEntry> entries_list;
	public static final Random RND = new Random();
	private int preAssigned = 0;

	public PreferenceTable(String path)
	{
		createStudentEntries(loadContentFromFile(path)); // read the file on creation and create entries out of the input
	}
	
	private Vector<Vector<String>> loadContentFromFile(String path)
	{
		Vector<Vector<String>> outer_vector = new Vector<Vector<String>>();
		FileInputStream stream;
		try {
			stream = new FileInputStream(path);
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			
			String line;
			do
			{
				line = reader.readLine();
				if (line != null)
				{
					Vector<String> inner_vector = new Vector<String>();
					StringTokenizer tokens = new StringTokenizer(line,"\t"); // break string into tokens on tab
					while(tokens.hasMoreTokens())
					{
						inner_vector.add((tokens.nextToken()).intern()); // adding tokens to "sub vectors", making sure only single instance of a string is contained in memory
					}
					outer_vector.add(inner_vector);
				}
			}while(line != null);
			
			stream.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return outer_vector;
	}
	
	private void createStudentEntries(Vector<Vector<String>> data)
	{
		Vector<StudentEntry> entries = new Vector<StudentEntry>();
		hashtable_entries.clear();
		
		for(int i = 1; i < data.size(); i++) // Omit the first row
		{
				String name = data.get(i).remove(0);
				boolean pre;
				String temp = data.get(i).remove(0);
				if ( temp == "Yes" || temp == "yes" || temp == "YES")
					pre = true;
				else
					pre = false;
				
				StudentEntry student = new StudentEntry(name,pre,data.get(i)); // add the rest of the vector as projects
				hashtable_entries.put(name, student);
				entries.add(student);
		}
		
		this.entries_list =  entries;
	}
	
	public Vector<StudentEntry> getAllStudentEntries() { return entries_list; }
	
	public StudentEntry getEntryFor(String name) { return hashtable_entries.get(name); }
	
	public StudentEntry getRandomStudent() { return entries_list.get(RND.nextInt(entries_list.size())); }
	
	public String getRandomPreference()
	{
		StudentEntry student = getRandomStudent(); // make sure that the project isn't supposed be unique (preassigned) and that there is one
		while(student.hasPreassignedProject() || student.getOrderedPreferences().size() == 0)
		{
			student = getRandomStudent();
		}
		return student.getRandomPreference();
	}
	
	public void fillPreferenceOfAll(int max_pref)
	{
		for(StudentEntry student : entries_list)
		{
			if(!student.hasPreassignedProject()) // only consider those who arn't preassigned
			while(student.getOrderedPreferences().size() < max_pref)
			{
				student.addProject(getRandomPreference());
			}
		}
	}
	
	// Returns number of pre-assigned 
	public int getNumberOfPreassigned(){
		timesProjectsChosen();
		return preAssigned;
	}
	
	// Get number of students
	public int getNumberOfStudents(){
		return this.getAllStudentEntries().size();
	}
	
	// Gets number of times project chosen
	// returns HashMap <ProjectName, TimesChosen>
	private  HashMap<String, Integer> timesProjectsChosen() {
		HashMap<String, Integer> ht = new HashMap<String, Integer>();
		for(StudentEntry student : this.getAllStudentEntries()){
			for(String pref : student.getOrderedPreferences()){
				if(student.hasPreassignedProject()){preAssigned++;continue;}
				if(ht.containsKey(pref)){
					ht.put(pref, ht.get(pref)+1);
				}else{
					ht.put(pref, 1);
				}
			}
		}
		return ht;
	}
	
	// Returns in string format the most popular project
	public String mostPopularProject(){
		HashMap<String, Integer> projects = timesProjectsChosen();
		List<String> keys = new ArrayList<String>();
		keys.addAll(projects.keySet());
		Collections.sort(keys, new Comparator<String>() {
		    public int compare(String o1, String o2) {
		        Integer i1 = projects.get(o1);
		        Integer i2 = projects.get(o2);
		        return (i1 > i2 ? -1 : (i1 == i2 ? 0 : 1));
		    }
		});
		//System.out.println(keys);
		String mostPopular = keys.get(0) + "\n";
		for(int i=1; i > 0; i--){
			if(projects.get(keys.get(0)) ==  projects.get(keys.get(i))){
				mostPopular = mostPopular + "\n" + keys.get(i-1) + "\n";
			}
		}
		return mostPopular;
	}
	
	// Returns in string format the most popular project
	public String leastPopularProject(){
		HashMap<String, Integer> projects = timesProjectsChosen();
		List<String> keys = new ArrayList<String>();
		keys.addAll(projects.keySet());
		Collections.sort(keys, new Comparator<String>() {
		    public int compare(String o1, String o2) {
		        Integer i1 = projects.get(o1);
		        Integer i2 = projects.get(o2);
		        return (i1 > i2 ? -1 : (i1 == i2 ? 0 : 1));
		    }
		});
		//System.out.println(keys);
		String leastPopular = keys.get(keys.size()-1) + "\n";
		for(int i=keys.size()-1; i > 0; i--){
			if(projects.get(keys.get(keys.size()-1)) ==  projects.get(keys.get(i-1))){
				leastPopular = leastPopular + "\n" + keys.get(i-1) + "\n";
			}
		}
		return leastPopular;
	}
	
	// Returns number of pre-assigned 
		public int getNumberOfProjects(){
			Vector<String> allProjects = new Vector<String>();
			for(StudentEntry student : entries_list){
				if(student.hasPreassignedProject()){continue;}
				for(String project : student.getOrderedPreferences()){
					if(!allProjects.contains(project)){
						allProjects.add(project);
					}
				}
			}
			return allProjects.size();
		}
	

	public void print() { for(StudentEntry student : entries_list) { System.out.println(student); } } // change to toString()

}
