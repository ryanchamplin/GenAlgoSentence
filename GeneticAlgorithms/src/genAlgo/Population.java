package genAlgo;
import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
import java.util.Random;


public class Population{
	
	DNA[] population;
	double muatationRate;
	ArrayList<DNA> matingPool;
	String targetPhrase;
	int generations;
	boolean isDone;
	int perfectScore;
	Random rand = new Random();
	String mySelection = "";
	
	Population(String target, double muationRate, int popSize, String selectionType){
		this.targetPhrase = target;
		this.muatationRate = muationRate;
		this.population = new DNA[popSize];
		
		for(int i = 0; i<population.length; i++){
			population[i] = new DNA(target.length()); 
		}
		calculateFitness();
		matingPool = new ArrayList<DNA>();
		isDone = false;
		generations = 0;
		this.mySelection = selectionType;
		perfectScore = 1;
	}
	
	public void calculateFitness(){
		for (int i = 0; i<population.length; i++){
			population[i].eveulateDNAFitness(targetPhrase);
		}
	}
	
	public void selection(){
		matingPool.clear();
		switch (mySelection){
		case "fp": 
			fitnessProp(population);
			break;
		case "sto":
			stochastic(population);
			break;
		case "tour":
			tournament(population);
			break;
		case "trun":
			truncation(population);
			break;
		default:
			System.out.println("Incorrect selection parameter. Try fp, sto, tour, or trun.");
			System.exit(1);
			break;
		}
	}
	
	private void truncation(DNA[] population) {
		DNA[] populationWorkSpace = new DNA[population.length];
		int midPoint =(int) population.length / 2;
		double[] fitnessArray = new double[population.length];
		
		for(int i = 0; i<population.length; i++){
			populationWorkSpace[i] = population[i];
			fitnessArray[i] = population[i].getFitness();
		}
		
		// TODO need to sort the workspace array by fitness
		//Arrays.sort(populationWorkSpace, getFitness());

		for(int i = 0; i<1; i++) {
			for(int j = 0; j<midPoint; j++){
				matingPool.add(population[i]);
			}
		}

	}
	

	/* tournament logic
	 * pick 2. 
	 * the better of the 2 get put into the mating pool.
	 * repeat until the mating pool is the size of the population.
	 */
	private void tournament(DNA[] population) {
		for (int i = 0; i<population.length; i++){
			DNA best = null;
			DNA current;
			
			for (int j = 0; j<1; j++){
				current = population[rand.nextInt(population.length)];
				if (best == null || current.getFitness() > best.getFitness()){
					best = current;
				}
			}
			matingPool.add(best);
		}
	}

	private void stochastic(DNA[] population) {
		// TODO Auto-generated method stub
	}

	/* logic behind Fitness Porportinate.
	 * 1. normalize your fitnesses between 0 and 1 in order to scale them.
	 * 2. find the number of times that you will add a DNA to the mating pool
	 * 	  The better the fitness the higher number of times it will get added into the pool
	 * 3. add the DNA to the mating pool x number of times.
	 */
	private void fitnessProp(DNA[] population) {
		// Define variables.
		double uniformDistrbVal = 0; // Will use this to normalize data
		double maxFitness = 0; // holds the highest fitness for the map function.
		
		// finds largest fitness, this is used to normalize the data with my map function.
		for (int i =0; i<population.length; i++){			
			if(population[i].getFitness() > maxFitness){
				maxFitness = population[i].getFitness();
			}
		}
		
		for (int i = 0; i<population.length; i++){
			uniformDistrbVal = map(population[i].getFitness(), 0, maxFitness, 0 ,1);
			int addToPool = (int) (uniformDistrbVal *100);
			for (int j = 0; j< addToPool; j++){
				matingPool.add(population[i]);
			}
		}
	}

	// maps a number from one range to another. doesn't check for 0 cause I'm lazy and won't need to.
	private double map(double value, double low1, double high1, double low2, double high2){
		double myDouble = 0;
		myDouble = low2 + (value - low1) * (high2 - low2) / (high1 - low1);
		return myDouble;
	}
	
	public void generateGeneration(){
		for (int i = 0; i<population.length; i++){
			int a = rand.nextInt(matingPool.size());
			int b = rand.nextInt(matingPool.size());
			DNA partnerA = matingPool.get(a);
			DNA partnerB = matingPool.get(b);
			
			DNA child = partnerA.crossover(partnerB);
			child.mutate(muatationRate);
			population[i] = child;
		}
		generations++;
	}
	
	public String getBest(){
		double currentBest = 0.0;
		int popIndex = 0;
		
		for (int i = 0; i< population.length; i++){
			if(population[i].getFitness() > perfectScore){
				popIndex = i;
				currentBest = population[i].getFitness();
			}
			
			if(currentBest == perfectScore){
				isDone = true;
			}
		}
		return population[popIndex].returnSentence();
	}
	
	
	public boolean isFinished(){
		return isDone;
	}
	
	public int getGenerationNumber(){
		return generations;
	}
	
	public double getAvgFitness(){
		double totalFit = 0;
		for (int i = 0; i<population.length; i++){
			totalFit += population[i].getFitness();
		}
		
		return totalFit/ population.length;
	}
	
	public String printSentences(){
		String everything = "";
		
		for (int i = 0; i < population.length; i++){
			everything += population[i].returnSentence() + "\n";
		}
		
		return everything;
	}
}