package studentProjectAssiger;
import java.util.Vector;

/**
 * 
 * @author Tomasz Sewerynski - 13487278
 *
 */
public class StudentEntry {

	private Vector<String> projects = new Vector<String>();
	private boolean preassigned = false;
	private int project_number;
	private String name;
	
	StudentEntry(String name, boolean preassigned, Vector<String> projects)
	{
		this.name = name;
		if (preassigned == true)
			preassignProject(projects.get(0));
		else
		for(String project : projects)
			addProject(project); // make sure no duplicate projects are present
		project_number = projects.size();
	}
	
	public String getStudentName() { return name; }
	
	public Vector<String> getOrderedPreferences() { return projects; }
	
	public void preassignProject(String project_name)
	{
		preassigned = true;
		addProject(project_name);

	}
	
	public boolean hasPreassignedProject() { return preassigned; }
	
	public int getNumberOfStatedPreferances() { return project_number; }
	
	public void addProject(String project_name)
	{
		if( !hasPreference(project_name) )
			projects.add(project_name.intern());
	}
	
	public boolean hasPreference(String preferance) { return projects.contains(preferance.intern()); }
	
	public String getRandomPreference() { return projects.get(PreferenceTable.RND.nextInt(projects.size())); }
	
	public String toString() { return name+" - "+preassigned+" - "+projects.size() +" - "+projects; }
	
	public int getRanking(String project) { return projects.indexOf(project); }
}
