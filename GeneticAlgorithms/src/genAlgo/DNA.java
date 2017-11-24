package genAlgo;

import java.util.Random;


public class DNA {
	
	Random rand = new Random();
	final String ALPHABET = "1234567890-=!@#$%^&*()_+ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz[]{};':\"\\,./<>?";
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
			if(rand.nextInt(1) < mutationRate){
				population[i] = (char) ALPHABET.charAt(rand.nextInt(ALPHABET.length()));
			}
		}
	}
	
	public double getFitness(){
		return fitness;
	}
}
