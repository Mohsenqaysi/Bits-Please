package studentProjectAssiger;

import java.util.List;
import java.util.Random;
import java.util.Vector;


public class GA {
	
	private final int MAX_RUNS = 50;
	private final Random RND = new Random();
	private final int MUTATION_NUM_MULTIPLE = 5; // used for multiple mutation cycles
	private final int MIN_RUN_TIME = 2; // minutes
	private final int MINUTE_IN_MILLS = 1000*60;
	
	private List<CandidateSolution> population = new Vector<CandidateSolution>();
	private PreferenceTable preference_table;
	private CandidateSolution best_solution;
	private CandidateSolution solution;
	private int max_population = 5000;
	private int mutation_chance = 100;
	private int max_reproduction = 500;
	private int random_size = 0;
	
	public GA(PreferenceTable preference_table)
	{
		if (max_reproduction > max_population)
			max_reproduction = max_population;
		this.preference_table = preference_table;
		
		
		long time_start = System.currentTimeMillis();
		long time_end = System.currentTimeMillis();
		while((time_end - time_start)/MINUTE_IN_MILLS < MIN_RUN_TIME)
		{
			genereateInitialPopulation();
			if (solution != null)
				population.add(new CandidateSolution(solution));
			
			random_size = Math.min(population.size(),max_reproduction);
			evolve();
			
			if(solution == null)
				solution = new CandidateSolution(getSolution());
			else
				if(best_solution.getEnergy() < solution.getEnergy())
					solution = new CandidateSolution(getSolution());
			
			time_end = System.currentTimeMillis();
		}
		
		population.add(solution);
		sortNChop();
	}
	
	private void genereateInitialPopulation()
	{
		population.clear();
		for(int i = 0; i < max_population*2; i++)
			population.add(new CandidateSolution(preference_table));
		sortNChop();
		best_solution = new CandidateSolution(getSolution());
	}
	
	private void evolve()
	{
		int bestenergy = 0;
		int solutionenergy = 1;
		int last = 0;
		int itereation = 0;
		
		do
		{
			// generate offspring
			for(int i = 0; i < max_reproduction*MUTATION_NUM_MULTIPLE; i++)
				population.add(getOffspring(population.get(RND.nextInt(random_size)),population.get(RND.nextInt(random_size))));

			sortNChop();
			bestenergy = best_solution.getEnergy();
			solutionenergy = getSolution().getEnergy();
			setBestSolution(solutionenergy,bestenergy);
			
//			// determine mutations
			if(mutation_chance >= 0)
				for(CandidateSolution sol : population)
					if(RND.nextInt(mutation_chance) == 0)
						mutateSolution(sol);
			
			setBestSolution(solutionenergy,bestenergy);
			solutionenergy = getSolution().getEnergy();
			if (solutionenergy != last)
			{
				last = solutionenergy;
				itereation = 0;
			}
			else
				itereation++;
			
		}
		while(itereation != MAX_RUNS);
		
		population.add(best_solution);
		sortNChop();
	}
	
	private CandidateSolution getOffspring(CandidateSolution parent1, CandidateSolution parent2)
	{
		CandidateSolution child = new CandidateSolution(parent1);
		for(StudentEntry student : preference_table.getAllStudentEntries())
		{
			if (RND.nextBoolean())
				child.setAssignmentFor(student.getStudentName(), new CandidateAssignment(parent2.getAssignmentFor(student.getStudentName())));
		}
		return child;
	}
	
	private void sortNChop()
	{
		population.sort(null);
		while (population.size() > max_population)
			population.remove(max_population);
	}
	
	private void mutateSolution(CandidateSolution sol)
	{
		sol.getRandomAssignment().randomizeAssignment();
	}
	
	public CandidateSolution getSolution()
	{
		return population.get(0);
	}
	
	private void setBestSolution(int solutionenergy, int bestenergy)
	{
		if(solutionenergy >= bestenergy)
		{
			population.add(new CandidateSolution(best_solution));
		}
		else
		{
			best_solution = new CandidateSolution(getSolution());
		}
	}
	
}