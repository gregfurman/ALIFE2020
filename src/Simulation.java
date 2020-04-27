import org.encog.neural.neat.NEATNetwork;

import java.io.IOException;
import java.util.Map;


public class Simulation {

    volatile String filename;

    volatile Population population;
    volatile int index;
    volatile double Fitness = 0, Consumed = 0;
    volatile boolean stationary;
    int[] proportions;
    double radius;
    boolean twoPlayer, similar;
    int fitness;
    Map<Character,Integer> map;
    int dimx, dimy, resources;




    volatile int NEATGeneration;
    public NEATNetwork network;


    public Simulation(){

    }

    public Simulation(int index,int[] proportions, double radius, boolean twoPlayer, boolean similar, int dimx,int dimy, int resources){

        this.proportions = proportions;
        this.radius = radius;
        this.twoPlayer = twoPlayer;
        this.similar = similar;
        this.index = index;
        this.dimx = dimx;
        this.dimy = dimy;
        this.resources = resources;


    }


    public Simulation(int index,int[] proportions, double radius, boolean twoPlayer, boolean similar, int dimx,int dimy, int resources,int fitness ){

        this.proportions = proportions;
        this.radius = radius;
        this.twoPlayer = twoPlayer;
        this.similar = similar;
        this.index = index;
        this.dimx = dimx;
        this.dimy = dimy;
        this.resources = resources;
        this.fitness = fitness;

    }

    public Simulation( int generation,int index,int[] proportions, double radius, boolean twoPlayer, boolean similar, int dimx,int dimy, int resources,int fitness,boolean stationary,NEATNetwork network){
        this.NEATGeneration = generation;
        this.proportions = proportions;
        this.radius = radius;
        this.twoPlayer = twoPlayer;
        this.similar = similar;
        this.index = index;
        this.dimx = dimx;
        this.dimy = dimy;
        this.resources = resources;
        this.fitness = fitness;
        this.network = network;
        this.stationary = stationary;
    }


    synchronized void simulatePort(){

        population = new Population(proportions[0], proportions[1], proportions[2], resources, dimx, dimy, twoPlayer, similar, radius,false, fitness,network);


        double Consumed = 0;
        double Fitness = 0;

            for (int j = 0; j < 1000; j++) {


                do{
                    population.move();
                }while (population.checkAdjacency());


                Fitness += population.totalFitness();
                Consumed += population.totalConsumed();


//                try {
//                    StatsRecorder statsRecorder = new StatsRecorder(index);
//                    statsRecorder.collectData(NEATGeneration,j,population,languageProportions());
//                } catch (IOException e){
//                    System.out.println("failed to create statsrecorder object");
//
//                }

                population.evaluateFitness();
                population.loadGrid(dimx, dimy);


                if (j == 499){
                population.migrate();
            }


            }


                setFitness(Fitness/1000);
                setConsumed(Consumed/1000);




    }


    synchronized void simulate(StatsRecorder statsRecorder){


        population = new Population(proportions[0], proportions[1], proportions[2], resources, dimx, dimy, twoPlayer, similar, radius,stationary,fitness,network,statsRecorder);


        double Consumed = 0;
        double Fitness = 0;


        for (int j = 0; j < 1000; j++) {

            population.setGenerations(NEATGeneration,j);

            do{
                population.move();
            }while (population.checkAdjacency());


            Fitness += population.totalFitness();
            Consumed += population.totalConsumed();

            statsRecorder.collectData(NEATGeneration,j,population,languageProportions());


            if (!stationary)
                if (j == 499)
                    population.migrate();


            population.evaluateFitness();
            population.loadGrid(dimx, dimy);


        }

        setFitness(Fitness/(1000*population.population.size()));
        setConsumed(Consumed/1000);

        statsRecorder.collectData(NEATGeneration,getFitness(),getConsumed());

    }

    synchronized void setConsumed(double Consumed){
        this.Consumed = Consumed;

    }

    synchronized void setFitness(double Fitness){
        this.Fitness = Fitness;
    }

    synchronized double getFitness(){

        return population.normalise(0,15,Fitness);


    }

    synchronized double getConsumed(){
        return population.normalise(0,50,Consumed);
    }

    synchronized void setFilename(String filename){
        this.filename = filename;
    }

    double[] languageProportions(){


        int sumA = 0,sumB=0,sumC=0;

        map = population.evaluateSimilarities();

        for (Map.Entry entry: map.entrySet()){

            if (population.languageA.contains((char)entry.getKey())){
                sumA+=(int)entry.getValue();

            } else if (population.languageB.contains((char)entry.getKey()))
                sumB += (int)entry.getValue();
            else
                sumC+=(int)entry.getValue();
        }




        double total = sumA + sumB + sumC;

        double[] values ={sumA/total,sumB/total,sumC/total};
        return values;

    }

}
