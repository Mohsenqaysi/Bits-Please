package studentProjectAssiger;

import java.util.Random;

public class SA 
{
	private PreferenceTable preferenceTable;
	private CandidateSolution currentSolution;
	private int temperature;
	private Random random;

	public SA(PreferenceTable preferenceTable)
	{
		this.preferenceTable = preferenceTable;
		this.currentSolution = new CandidateSolution(this.preferenceTable);
		//this.temperature = this.currentSolution.getEnergy(); ////////////////////
		this.temperature = 5000; ////////////////////

		this.random = new Random();
	}

	private double BoltzmannProbability(int delta)
	// The argument delta must be a negative integer.
	{
		return Math.exp(- (double) delta / this.temperature);
	}

	private boolean isAccepted(int oldEnergy, int newEnergy)
	{
		int delta = newEnergy - oldEnergy;
		if ( delta < 0 )
		{
			return true;
		}
		else
		{
			double probability = this.BoltzmannProbability(delta);
			if ( this.random.nextDouble() <= probability )
			{
				return true;
			}
			else
			{
				return false;
			}
		}

	}

	private void runOneTime()
	{
		int oldEnergy = this.currentSolution.getEnergy();
		CandidateAssignment randomAssignment = this.currentSolution.getRandomAssignment();
		String studentName = randomAssignment.getStudentEntry().getStudentName();
		randomAssignment.randomizeAssignment();
		this.currentSolution.setAssignmentFor(studentName, randomAssignment); // Update it.

		if ( !this.isAccepted(oldEnergy, this.currentSolution.getEnergy()) )
		{
			randomAssignment.undoAssignment();
			this.currentSolution.setAssignmentFor(studentName, randomAssignment);
		}
	}

	public void multipleRuns(int times)
	{
		for ( ; this.temperature > 1; this.temperature = this.temperature - 1 )
		{
			for ( int i = 0; i < 50; i++ )
			{
				this.runOneTime();
			}
		}
		int counter = 1;
		int lastEnergy = this.currentSolution.getEnergy();
		while ( counter < 1000 )
		{
			this.runOneTime();
			if ( lastEnergy >= this.currentSolution.getEnergy() )
			{
				counter++;
			}
			else
			{
				counter = 0;
			}
		}
	}

	public CandidateSolution getSolution(int times)
	{
		System.out.println(this.currentSolution.getEnergy());
		this.multipleRuns(times);
		System.out.println(this.currentSolution.getEnergy());
		return this.currentSolution;
	}
}