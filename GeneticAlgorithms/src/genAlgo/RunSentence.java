package genAlgo;
import java.util.Scanner;


public class RunSentence {

	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		String target = "";
		String paramater = "";
		double mutationRate = 0.01;
		int maxPopulation = 150;
		
		System.out.println("Please enter your desired sentence.");
		target = scanner.nextLine();
		System.out.println("Please enter your desired selection parameter: fp, sto, tour, or trun.");
		paramater = scanner.nextLine();
		
		Population population = new Population(target, mutationRate, maxPopulation, paramater);
		scanner.close();
		
		population.selection();
		population.generateGeneration();
		population.calculateFitness();

	}

}
