package genAlgo;

//import java.util.Comparator;
import java.util.Random;


public class DNA{ // may need impliment Comparable<DNA>
	
	Random rand = new Random();
	final String ALPHABET = " 1234567890-=!@#$%^&*()_+ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz[]{};':\"\\,./<>?";
	//final String ALPHABET = " abcdefghijklmnopqrstuvwxyz";
	private double fitness;
	
	char[] population;
	
	DNA(int popSize){
		population = new char[popSize];
		for(int i = 0; i < population.length; i++){		
			population[i] = (char) ALPHABET.charAt(rand.nextInt(ALPHABET.length()));
		}
	}
	
	public String returnSentence(){
		return new String(population);
	}
	
	public void eveulateDNAFitness(String inSentence){
		double score = 0;
		for(int i=0; i < population.length; i++){
			if (population[i] == inSentence.charAt(i)){
				score++;
			}
		}
		
		fitness = score / inSentence.length();
	}
	
	public DNA crossover(DNA secondParent){
		DNA child = new DNA(population.length);
		
		int splitPoint = rand.nextInt(population.length);
		
		for (int i = 0; i<population.length; i++){
			if(i > splitPoint){
				child.population[i] = population[i];
			}
			else {
				child.population[i] = secondParent.population[i];
			}
		}
		return child;
	}
	
	public void mutate(double mutationRate){
		for (int i = 0; i<population.length; i++){
			if(rand.nextDouble() < mutationRate){
				population[i] = (char) ALPHABET.charAt(rand.nextInt(ALPHABET.length()));
			}
		}
	}
	
	public double getFitness(){
		return fitness;
	}
	
/*	public static Comparator<DNA> DNAComparaator = new Comparator<DNA>(){
		public int compare(DNA dna1, DNA dna2){
			double fitness1 = dna1.getFitness();
			double fitness2 = dna2.getFitness();
			
			return fitness2.compareTo(fitness1);
		}
	};*/

/*	@Override
	public int compareTo(DNA other) {
		if (this.getFitness() == other.getFitness() ){
			return 0;
		}
		if (this.getFitness() < other.getFitness()){
			return -1;
		}
		else{
			return 1;
		}	
	}
	*/
	

}