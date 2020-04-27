import org.encog.ml.CalculateScore;
import org.encog.ml.MLMethod;
import org.encog.neural.neat.NEATNetwork;

import java.io.*;

public class ScoreCalculate implements CalculateScore {


    public NEATNetwork network;
    volatile int NEATgeneration, experimentNumber;

    public ScoreCalculate(int experimentNumber){
        this.experimentNumber = experimentNumber;
    }


    @Override
    public double calculateScore(MLMethod mlMethod) {

        network = (NEATNetwork)mlMethod;

        SimulationDriver driver = new SimulationDriver(network,NEATgeneration,experimentNumber);

        driver.run();

        double score = driver.simulation.getFitness();
        double score2 = driver.simulation.getConsumed();

        return score - score2;


    }


    public double calculateScore2(Simulation[] simulations){

        double score = 0;
        for (Simulation simulation : simulations){
            score += simulation.getConsumed();
        }

        return score/simulations.length;


    }

    public synchronized void setGeneration(int generation){
        this.NEATgeneration = generation;

    }

    @Override
    public boolean shouldMinimize() {
        return false;
    }

    @Override
    public boolean requireSingleThreaded() {
        return false;
    }



}
