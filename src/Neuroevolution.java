import org.encog.ml.ea.train.basic.TrainEA;
import org.encog.neural.neat.NEATPopulation;
import org.encog.neural.neat.NEATUtil;

public class Neuroevolution implements Runnable {

    final static private int POPULATION_SIZE = 150;
    int experimentIndex;
    int iteration;

    public Neuroevolution(int iteration,int experimentIndex){

        this.experimentIndex = experimentIndex;
        this.iteration = iteration;

    }

    public Neuroevolution(int experimentIndex){

        this.experimentIndex = experimentIndex;

    }

    @Override
    public void run(){

//        long startTime = System.nanoTime();

        ScoreCalculate scoreCalculator = new ScoreCalculate(experimentIndex);
        NEATPopulation population =  new NEATPopulation(8,8,POPULATION_SIZE);
        population.setInitialConnectionDensity(1.0);
        population.reset();

        TrainEA evolution = NEATUtil.constructNEATTrainer(population,scoreCalculator);

        System.out.println("Experiment: " +experimentIndex+ " of iteration " + iteration+"\nStarting Evolution with "+ POPULATION_SIZE + " networks\n***************************\n");


        for (int i = evolution.getIteration(); i < 100; i++){

//
            System.out.println("Running generation " + i + " of iteration " + iteration);
            scoreCalculator.setGeneration(i);
            evolution.iteration();

//
//
//            System.out.println("Best Score: " + evolution.getBestGenome().getScore());

        }

//        System.out.println("Time: " + (System.nanoTime()- startTime)/1000000/1000);


//        System.out.println("finished thread " + iteration);



        evolution.finishTraining();

//        NEATNetwork bestPerformingNetwork = (NEATNetwork) evolution.getCODEC().decode(evolution.getBestGenome());
//
//        try {
//            SerializeObject.save(new File("bestnetworks_" + experimentIndex + ".txt"), bestPerformingNetwork);
//        }catch (IOException e){
//            System.out.println("Could not save network");
//        }
//
//        EncogUtility.saveEGB( "bestnetworks_"+experimentIndex+".txt" , data);
//
//
//        EncogDirectoryPersistence.saveObject(new File("bestnetworks_"+experimentIndex+".txt"), bestPerformingNetwork);

    }




}
