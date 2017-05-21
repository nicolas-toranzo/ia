/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gap.main;

import jenes.population.Fitness;
import jenes.GeneticAlgorithm;
import jenes.chromosome.BitwiseChromosome;
import jenes.population.Individual;
import jenes.population.Population;
import jenes.population.Population.Statistics.Group;
import jenes.stage.AbstractStage;
import jenes.stage.operator.common.OnePointCrossover;
import jenes.stage.operator.common.SimpleMutator;
import jenes.stage.operator.common.TournamentSelector;
import jenes.tutorials.utils.Utils;

/**
 *
 * @author nikot
 */
public class GeneticAlgorithmPlan {

    private static int POPULATION_SIZE = 100;
    private static int CHROMOSOME_LENGTH = 24;
    private static int GENERATION_LIMIT = 1000;
    
    public static void main(String[] args) throws Exception {
        
        System.out.println("TP IA: planificador de tareas basado en un algoritmo genético");

        //Fijo el cromosoma (va a ser de tipo array de 24 bits - ver los valores en el TP)
        Individual<BitwiseChromosome> sample = new Individual<BitwiseChromosome>(new BitwiseChromosome(CHROMOSOME_LENGTH));
        Population<BitwiseChromosome> pop = new Population<BitwiseChromosome>(sample, POPULATION_SIZE);
        
        Fitness<BitwiseChromosome> fit = new Fitness<BitwiseChromosome>(false) {

            @Override
            public void evaluate(Individual<BitwiseChromosome> individual) {
                BitwiseChromosome chrom = individual.getChromosome();
                int count = 0;
                int length=chrom.length();
                
                
                individual.setScore(count);
            }
            
        };
        
        GeneticAlgorithm<BitwiseChromosome> ga = new GeneticAlgorithm<BitwiseChromosome>(fit, pop, GENERATION_LIMIT);
        
        AbstractStage<BitwiseChromosome> selection = new TournamentSelector<BitwiseChromosome>(3);
        AbstractStage<BitwiseChromosome> crossover = new OnePointCrossover<BitwiseChromosome>(0.8);
        AbstractStage<BitwiseChromosome> mutation = new SimpleMutator<BitwiseChromosome>(0.02);
        ga.addStage(selection);
        ga.addStage(crossover);
        ga.addStage(mutation);
        ga.setElitism(1);
        ga.evolve();
        
        Population.Statistics stats = ga.getCurrentPopulation().getStatistics();
        GeneticAlgorithm.Statistics algostats = ga.getStatistics();
        
        System.out.println("Objective: " + (fit.getBiggerIsBetter()[0] ? "Max! (All true)" : "Min! (None true)"));
        System.out.println();
        
        Group legals = stats.getGroup(Population.LEGALS);
        
        Individual solution = legals.get(0);
                
        System.out.println("Solution: ");
        System.out.println( solution );
        System.out.format("found in %d ms.\n", algostats.getExecutionTime() );
        System.out.println();
        
        Utils.printStatistics(stats);
    }
    
}
