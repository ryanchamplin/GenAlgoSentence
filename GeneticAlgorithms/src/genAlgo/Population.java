package genAlgo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;


public class Population{
	
	DNA[] population;
	double muatationRate;
	ArrayList<DNA> matingPool;
	String targetPhrase;
	int generations;
	boolean isDone;
	final int PERFECTSCORE = 1;
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
		//perfectScore = 1;
	}
	public void printMatingPool(){
		for(int i = 0; i<matingPool.size(); i++){
			System.out.println(matingPool.get(i).returnSentence());
		}
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
	/* Truncation logic.
	 * Sort the DNA array by average fitness. 
	 * Drop the worst 50%, and only breed with the top 50%.
	 */
	private void truncation(DNA[] inPopulation) {
		DNA[] populationWorkSpace = new DNA[inPopulation.length];
		int midPoint =(int) inPopulation.length / 2;
		double[] fitnessArray = new double[inPopulation.length];
		
		for(int i = 0; i<inPopulation.length; i++){
			populationWorkSpace[i] = inPopulation[i];
			fitnessArray[i] = inPopulation[i].getFitness();
		}
		
		Arrays.sort(populationWorkSpace);
		for(int i = 0; i<midPoint; i++){
			for(int j=0; j<10; j++){
				matingPool.add(populationWorkSpace[i]);
			}
		}
	}
	

	/* tournament logic
	 * pick 2. 
	 * the better of the 2 get put into the mating pool.
	 * repeat until the mating pool is the size of the population.
	 */
	private void tournament(DNA[] inPopulation) {
		for (int i = 0; i<inPopulation.length; i++){
			DNA best = null;
			DNA current;
			
			for (int j = 0; j<2; j++){
				current = inPopulation[rand.nextInt(inPopulation.length)];
				if (best == null || current.getFitness() > best.getFitness()){
					best = current;
				}
			}
			matingPool.add(best);
		}
	}
	
	
	/* stochastic logic
	 * Instead of a single selection pointer employed in roulette wheel, SUS uses N equally spaced pointers. 
	 */
	private void stochastic(DNA[] inPopulation) {
		// TODO Auto-generated method stub
		double maxFit = findMaxFitness(inPopulation);
		int numToKeep = (int) (inPopulation.length *.1);
		double distance = (maxFit / numToKeep);
		double startingPlace = 0 + (distance - 0) * rand.nextDouble();
		double[] pointers = new double[numToKeep];
		
		for(int i = 0; i < numToKeep; i++){
			pointers[i] = startingPlace + (i*distance);
		}
		
		for(double point : pointers){
			int counter = 0;
			double fitnessSum = 0;
			for(int i = 0; i < pointers.length; i++){
				fitnessSum += population[counter].getFitness();
				if(fitnessSum < point){
					counter++;
				}else{
					matingPool.add(inPopulation[counter]);
				}
			}
		}
	}
	

	/* logic behind Fitness Porportinate.
	 * 1. normalize your fitnesses between 0 and 1 in order to scale them.
	 * 2. find the number of times that you will add a DNA to the mating pool
	 * 	  The better the fitness the higher number of times it will get added into the pool
	 * 3. add the DNA to the mating pool x number of times.
	 */
	private void fitnessProp(DNA[] inPopulation) {
		
		// Define variables.
		double uniformDistrbVal = 0; // Will use this to normalize data
		double maxFitness = 0; // holds the highest fitness for the map function.
		
		// finds largest fitness, this is used to normalize the data with my map function.
		for (int i =0; i<inPopulation.length; i++){			
			if(inPopulation[i].getFitness() > maxFitness){
				maxFitness = inPopulation[i].getFitness();
			}
		}
		
		for (int i = 0; i<inPopulation.length; i++){
			uniformDistrbVal = map(inPopulation[i].getFitness(), 0, maxFitness, 0 ,1);
			int addToPool = (int) (uniformDistrbVal *100);
			for (int j = 0; j< addToPool; j++){
				matingPool.add(inPopulation[i]);
			}
		}
	}

	// maps a number from one range to another. doesn't check for div by 0 cause I'm lazy and won't need to.
	private double map(double value, double low1, double high1, double low2, double high2){
		double myDouble = 0;
		myDouble = ((value-low1)/(high1-low1) *(high2 - low2) + low2);
		return myDouble;
	}
	
	private double findMaxFitness(DNA[] inPopulation){
		double totalFitness = 0;
		for (int i =0; i<inPopulation.length; i++){			
			totalFitness += inPopulation[i].getFitness();
		}
		return totalFitness;
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
		int bestPopIndex = 0;
		
		for (int i = 0; i< population.length; i++){
			if(population[i].getFitness() > currentBest){
					currentBest = population[i].getFitness();
					bestPopIndex = i;				
			}
		}
		if(currentBest == PERFECTSCORE){
			isDone = true;
		}
		return population[bestPopIndex].returnSentence();
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
	
	private double getTotalFitness(){
		double totalFit = 0;
		for (int i = 0; i<population.length; i++){
			totalFit += population[i].getFitness();
		}
		return totalFit;
	}
	
	public String printSentences(){
		String everything = "";
		
		for (int i = 0; i < population.length; i++){
			everything += population[i].returnSentence() + "\n";
		}	
		return everything;
	}
}