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

    private static int POPULATION_SIZE = 100; //100 tareas iniciales de entre las cuales elegir la siguiente
    private static int CHROMOSOME_LENGTH = 10;
    private static int GENERATION_LIMIT = 1; //Debe ser una, sino hay mutación y se pierde la escencia del ejercicio teórico
    
    public static void main(String[] args) throws Exception {
        
        System.out.println("TP IA: planificador de tareas basado en un algoritmo genético");

        //Fijo el cromosoma (va a ser de tipo array de 24 bits - ver los valores en el TP)
        Individual<BitwiseChromosome> sample = new Individual<BitwiseChromosome>(new BitwiseChromosome(CHROMOSOME_LENGTH));
        Population<BitwiseChromosome> pop = new Population<BitwiseChromosome>(sample, POPULATION_SIZE);
        
        Fitness<BitwiseChromosome> fit = new Fitness<BitwiseChromosome>(false) {

            @Override
            public void evaluate(Individual<BitwiseChromosome> individual) {
                BitwiseChromosome crom = individual.getChromosome();
                int score = 0;
                
                //Tengo que parsear el cromosoma y obtener los datos del proceso
                //para luego calificar según las condiciones del TP
                
                
                individual.setScore(score);
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
        System.out.println( solution.getChromosome() );
        System.out.format("found in %d ms.\n", algostats.getExecutionTime() );
        System.out.println();
        
        Utils.printStatistics(stats);
    }
    
}
