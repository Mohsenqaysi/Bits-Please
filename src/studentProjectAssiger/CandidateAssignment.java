package studentProjectAssiger;

public class CandidateAssignment {
	
	private final int ENERGY_RANKING_POWER = 2;
	private StudentEntry student;
	private String project;
	private String previous_project;
	
	CandidateAssignment(StudentEntry student)
	{
		this.student = student;
		randomizeAssignment();
		previous_project = project; // undo after first random won't return to null
	}
	
	public CandidateAssignment(CandidateAssignment Cand) {
		student = Cand.getStudentEntry();
		project = Cand.getAssignedProject();
		previous_project = project; // at the initial state they are the same.
	}
	
	public void randomizeAssignment()
	{
		previous_project = project;
		project = student.getRandomPreference();
	}
	
	public void undoAssignment() { project = previous_project; }
	
	public StudentEntry getStudentEntry() { return student;	}
	
	public String getAssignedProject() { return project; }
	
	public int getEnergy() { return (int) Math.pow(student.getRanking(project)+1,ENERGY_RANKING_POWER); }

}
