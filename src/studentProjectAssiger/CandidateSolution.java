package studentProjectAssiger;
import java.util.Hashtable;
import java.util.Vector;

public class CandidateSolution implements Comparable {
	
	private Hashtable<String, CandidateAssignment> hashtable_assignments = new Hashtable<String, CandidateAssignment>(); // for quick retrieval
	private Vector<CandidateAssignment> assignment_list = new Vector<CandidateAssignment>();
	private final int MULTI_PERSON_PROJECT_PENALTY = 5000; // penalty for multiple students receiving the same project
	private int duplicates = 0; // might come in handy later on
	private PreferenceTable studentPerfs;
	CandidateSolution(PreferenceTable pref_table)
	{
		studentPerfs = pref_table;
		for(StudentEntry student : pref_table.getAllStudentEntries())
		{
			CandidateAssignment assignment = new CandidateAssignment(student);
			assignment_list.add(assignment);
			hashtable_assignments.put(student.getStudentName(), assignment);
		}
	}
	
	public CandidateSolution(CandidateSolution sol) {
		studentPerfs = sol.studentPerfs;
//		assignment_list = new HashMap<>();
//		hashtable_assignments
		
		for (CandidateAssignment ass :sol.assignment_list) {
			CandidateAssignment new_ass = new CandidateAssignment(ass);
			hashtable_assignments.put(ass.getStudentEntry().getStudentName(), new_ass);
			assignment_list.add(new_ass);
		}
	}
	
	public CandidateAssignment getRandomAssignment() { return assignment_list.elementAt(PreferenceTable.RND.nextInt(assignment_list.size())); }
	
	public CandidateAssignment getAssignmentFor(String name) { return hashtable_assignments.get(name); }

	// Returns a 10 integer array of student preferences
	// Index 0 = num of 1st prefs
	// index 1 = num of 2nd prefs
	// etc..
	public int[] getHistogramData(){
		int[] data = new int[10];
		for(StudentEntry student : studentPerfs.getAllStudentEntries()){
			data[student.getRanking(getAssignmentFor(student.getStudentName()).getAssignedProject())] += 1;
		}
		return data;
	}
	
	// Get the satisfaction score
	public int getSatisfaction(){
		int satisfactionScore=0;
		int perfectScore=100;
		for(CandidateAssignment cand : assignment_list){
			StudentEntry student = cand.getStudentEntry();
			int pref = student.getRanking(cand.getAssignedProject());
			for(int i=0; i<10; i++){ 
				if(pref==i){
					satisfactionScore += perfectScore - pref*10;
				} 
			}
		}
		return satisfactionScore/studentPerfs.getNumberOfStudents();
	}
	
	public void print() // change to toString()
	{
		for(CandidateAssignment assignment : assignment_list)
			System.out.println(assignment.getStudentEntry().getStudentName()+" got => "+assignment.getAssignedProject());
		System.out.println("Energy cost: "+getEnergy());
		System.out.println("Fitness value: "+getFitness());
		System.out.println("Duplicates no.: "+duplicates);
	}
	
	public int getEnergy()
	{
		Hashtable<String,Integer> temp_table = new Hashtable<String,Integer>();
		int energy = 0;
		int duplicates = 0;
		for(CandidateAssignment assignment : assignment_list) 
		{ 
			energy += assignment.getEnergy();
			if(temp_table.containsKey(assignment.getAssignedProject()))
			{
				energy += MULTI_PERSON_PROJECT_PENALTY;
				// remember how many people got the same project, currently unused and unnecessary
//				temp_table.replace(assignment.getAssignedProject(), temp_table.get(assignment.getAssignedProject())+1);
				duplicates++;
			}
			else
				temp_table.put(assignment.getAssignedProject(),(Integer)1); // record an instance of this project
		}
		
		this.duplicates = duplicates; // record the duplicates
		// note current print method forced the energy and duplicates to be synchronised, that might not always be the case
		// Another way is to calculate energy
		return energy;
	}
	
	public int getFitness() { return -getEnergy(); }

	@Override
	public int compareTo(Object other) {
		return getEnergy() - ((CandidateSolution) other).getEnergy();
	}
	
	public void setAssignmentFor(String name, CandidateAssignment assignment)
	{
//		System.out.println(name+"  "+assignment_list.indexOf(assignment));
		assignment_list.set(assignment_list.indexOf(getAssignmentFor(name)) , (assignment));
		hashtable_assignments.put(name, assignment);
	}
	
	int getDuplicates() { return duplicates; }

}
