/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gap.main;

import gap.utils.GAPChromosome;
import gap.utils.GAPUtils;
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

    private static final int POPULATION_SIZE = 1000; //1000 tareas iniciales de entre las cuales elegir la siguiente
    private static final int CHROMOSOME_LENGTH = 10;
    private static final int GENERATION_LIMIT = 1; //Debe ser una, sino hay mutación y se pierde la escencia del ejercicio teórico
    
    public static void main(String[] args) throws Exception {
        
        System.out.println("TP IA: planificador de tareas basado en un algoritmo genético");

        //Fijo el cromosoma (va a ser de tipo array de 24 bits - ver los valores en el TP)
        Individual<BitwiseChromosome> sample = new Individual<>(new BitwiseChromosome(CHROMOSOME_LENGTH));
        Population<BitwiseChromosome> pop = new Population<>(sample, POPULATION_SIZE);
        
        Fitness<BitwiseChromosome> fit = new Fitness<BitwiseChromosome>(false) {

            @Override
            public void evaluate(Individual<BitwiseChromosome> individual) {
                BitwiseChromosome crom = individual.getChromosome();
                individual.setLegal(true);
                //Tengo que parsear el cromosoma y obtener los datos del proceso
                //para luego calificar según las condiciones del TP
                
                GAPChromosome gapCrom = GAPUtils.parseFromBitWiseChromosome(crom);

                int condiciones = 0;
                int penalizacion = 0;
                int restricciones = 0;

                //Verifico que el cromosoma sea correcto
                //Si el Tipo de Operación es (3) entonces sumar (30).
                if (gapCrom.getTipoOperacion() == 3) {
                    penalizacion = penalizacion + 30;
                    individual.setLegal(false);
                    return;
                }
                // Si Dependencia de tareas es (< 1 o > 5) entonces sumar (30).
                if (gapCrom.getDependencia() > 4) {
                    penalizacion = penalizacion + 30;
                    individual.setLegal(false);
                    return;
                }

                //Si la Eficiencia de recursos es distinta de (Baja, Media, o Alta) entonces sumar (30).
                if (gapCrom.getEficiencia() > 2) {
                    penalizacion = penalizacion + 30;
                    individual.setLegal(false);
                    return;
                }

                // Si Tiempo de uso de recursos es (< 1 o > 5) entonces sumar (30).
                if (gapCrom.getTiempoUso() > 4) {
                    penalizacion = penalizacion + 30;
                    individual.setLegal(false);
                    return;
                }                
                
                //Si la operación es Lógica 00 y la dependencia de tareas es >= 2 y <= 4) entonces (sumar (-10)) sino (sumar 5).
                //Si la operación es Lógica y la eficiencia de recursos es media o alta) entonces (sumar (-10)) sino (sumar 5)
                //Si la operación es Lógica y el tiempo de uso es < 4) entonces (sumar (-10)) sino (sumar 5).

                if (gapCrom.getTipoOperacion() == 0) {
                    if (gapCrom.getDependencia() == 2 || gapCrom.getDependencia() == 3 || gapCrom.getDependencia() == 4) {
                        condiciones = condiciones - 10;
                    } else {
                        condiciones = condiciones + 5;
                    }
                    if (gapCrom.getEficiencia() == 1 || gapCrom.getEficiencia() == 2) {
                        condiciones = condiciones - 10;
                    } else {
                        condiciones = condiciones + 5;
                    }
                    if (gapCrom.getTiempoUso() == 0 || gapCrom.getTiempoUso() == 1 || gapCrom.getTiempoUso() == 2) {
                        condiciones = condiciones - 10;
                    } else {
                        condiciones = condiciones + 5;
                    }
                }

                //Si la operación es Gráfica 10 y la eficiencia de recursos es baja) entonces (sumar (-10)) sino (sumar 5).
                //Si la operación es Gráfica y el tiempo de uso de recursos es >3) entonces (sumar (-10)) sino (sumar 5).
                //Si la operación es Gráfica y la dependencia de tareas es >= 3) entonces (sumar (-10)) sino (sumar 5).
                if (gapCrom.getTipoOperacion() == 2) {
                    if (gapCrom.getEficiencia() == 0) {
                        condiciones = condiciones - 10;
                    } else {
                        condiciones = condiciones + 5;
                    }
                    if (gapCrom.getTiempoUso() == 3 || gapCrom.getTiempoUso() == 4) {
                        condiciones = condiciones - 10;
                    } else {
                        condiciones = condiciones + 5;
                    }

                    if (gapCrom.getDependencia() == 2 || gapCrom.getDependencia() == 3 || gapCrom.getDependencia() == 4) {
                        condiciones = condiciones - 10;
                    } else {
                        condiciones = condiciones + 5;
                    }
                }

                //Si la operación es Aritmética  y la eficiencia de recursos es alta) entonces (sumar (-10)) sino (sumar 5).
                //Si la operación es Aritmética y la dependencia de tareas es <= 3 y tiempo de uso <= 3) entonces (sumar (-10)) sino (sumar 5).
                if (gapCrom.getTipoOperacion() == 1) {
                    if (gapCrom.getEficiencia() == 2) {
                        condiciones = condiciones - 10;
                    } else {
                        condiciones = condiciones + 5;
                    }
                    if ((gapCrom.getDependencia() == 2 || gapCrom.getDependencia() == 3 || gapCrom.getDependencia() == 4) && (gapCrom.getTiempoUso() == 0 || gapCrom.getTiempoUso() == 1 || gapCrom.getTiempoUso() == 2)) {
                        condiciones = condiciones - 10;
                    } else {
                        condiciones = condiciones + 5;
                    }
                }

                //Si (Eficiencia de recursos = Alta y tiempo de uso > 3) entonces (sumar 5).
                //Si (Eficiencia de recursos = Alta y dependencia de tareas > 3) entonces (sumar 5).
                if (gapCrom.getEficiencia() == 2) {
                    if (gapCrom.getTiempoUso() == 3 || gapCrom.getTiempoUso() == 4) {
                        restricciones = restricciones + 5;
                    }
                    if (gapCrom.getDependencia() == 3 || gapCrom.getDependencia() == 4) {
                        restricciones = restricciones + 5;
                    }
                }
                //Si (Eficiencia de recursos = 00 y tiempo de uso < 4) entonces (sumar 5).
                //Si (Eficiencia de recursos = Baja y dependencia de tareas < 3) entonces (sumar 5).
                if (gapCrom.getEficiencia() == 0) {
                    if (gapCrom.getTiempoUso() == 0 || gapCrom.getTiempoUso() == 1 || gapCrom.getTiempoUso() == 2) {
                        restricciones = restricciones + 5;
                    }
                    if (gapCrom.getDependencia() == 0 || gapCrom.getDependencia() == 1) {
                        restricciones = restricciones + 5;
                    }
                }

                individual.setScore(condiciones + restricciones + penalizacion);
            }
            
        };
        
        GeneticAlgorithm<BitwiseChromosome> ga = new GeneticAlgorithm<>(fit, pop, GENERATION_LIMIT);
        
        AbstractStage<BitwiseChromosome> selection = new TournamentSelector<>(3);
        AbstractStage<BitwiseChromosome> crossover = new OnePointCrossover<>(0.8);
        AbstractStage<BitwiseChromosome> mutation = new SimpleMutator<>(0.02);
        ga.addStage(selection);
        ga.addStage(crossover);
        ga.addStage(mutation);
        ga.setElitism(1);
        ga.evolve();
        
        Population.Statistics stats = ga.getCurrentPopulation().getStatistics();
        GeneticAlgorithm.Statistics algostats = ga.getStatistics();
        
        System.out.println("Objetivo: " + (fit.getBiggerIsBetter()[0] ? "Maximizar" : "Minimizar"));
        System.out.println();
        
        Group legals = stats.getGroup(Population.LEGALS);
        
        Individual solution = legals.get(0);
        GAPChromosome crom = GAPUtils.parseFromBitWiseChromosome((BitwiseChromosome)solution.getChromosome());
        
        System.out.println("Solution: ");
        System.out.println(crom.toString());
        System.out.format("Encontrada en %d ms.\n", algostats.getExecutionTime() );
        System.out.println();
        
        Utils.printStatistics(stats);
    }
    
}
