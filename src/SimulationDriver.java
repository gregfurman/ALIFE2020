import org.encog.neural.neat.NEATNetwork;

import java.io.*;

public class SimulationDriver{


    Simulation[] simulations;
    Simulation simulation;
    int experimentNumber;
    StatsRecorder statsRecorder;
    volatile int NEATGeneration;

    boolean[] moving = {true,false};
    boolean[][] gameTypes = {{false, false}};
    int[][] sizeArr = {{50,50,50},{20,65,65},{110,20,20}};
    int[] fitness = {5,15};
    double[] radii = {0.25};


    synchronized void load(NEATNetwork network){

        int count = 1;

        for (boolean stationary : moving)
            for (int[] sizes : sizeArr) {

                for (boolean[] gameType : gameTypes) {

                    for (double radius : radii) {

                        for (int fitnessValue : fitness) {

                            if (count == experimentNumber) {
                                simulation = new Simulation(NEATGeneration, count, sizes, radius, gameType[0], gameType[1], 40, 20, 20, fitnessValue, stationary, network);
                                return;
                            }
                            count++;

                    }
                }

            }
        }





    }


    public void load() {

        boolean[][] gameTypes = {{true, true}, {true, false}, {false, false}};
        int[][] sizeArr = {{50,50,50},{100,25,25},{25,100,25}, {25,25,100}};
        double[] radii = {0.25,0.5, 0.75, 1};


        simulations = new Simulation[gameTypes.length*sizeArr.length*radii.length];

        int count = 0;


        for (int[] sizes : sizeArr) {

            for (boolean[] gameType : gameTypes) {

                for (double radius : radii) {

                    simulations[count] = new Simulation(count,sizes, radius, gameType[0], gameType[1],40,20,25);
                    count++;

                }

            }
        }

        for (int thread = 0; thread < simulations.length; thread++) {

            try {
                BufferedWriter writerProportions = new BufferedWriter(new FileWriter("agentsvsproportions_" + simulations[thread].index + ".txt", true));
                BufferedWriter writerFitness = new BufferedWriter(new FileWriter("agentsvfitness_" + simulations[thread].index + ".txt", true));
                BufferedWriter writerMap = new BufferedWriter(new FileWriter("letterMap" + simulations[thread].index + ".txt", true));
                BufferedWriter writerIndex = new BufferedWriter(new FileWriter("index.txt", true));


                writerIndex.write("index,Aprop,Bprop,Cprop,twoPlayer,similar,radius\n");
                writerProportions.write("Generation,Asize,Bsize,Csize,Aprop,Bprop,Cprop\n");
                writerFitness.write("Generation,Fitness,SimilarityThreshold");
                writerMap.write("Generation,a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z," +
                        "&, ', (, ), *, +, comma, -, ., /, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, :, ;, <, =, >, ?," +
                        "A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z\n");

                writerProportions.close();
                writerFitness.close();
                writerMap.close();
                writerIndex.close();

            } catch (IOException e) {

            }

        }




    }

    synchronized void loadPort(){

        boolean[][] gameTypes = {{false, false}};
        int[][] sizeArr = {{50,50,50},{20,65,65}};
        int[] fitness = {5,15};
        double[] radii = {0.25};

//        boolean[][] gameTypes = {{false, false}};
//        int[][] sizeArr = {{20,65,65}};
//        int[] fitness = {5};
//        double[] radii = {0.25};



        simulations = new Simulation[gameTypes.length*sizeArr.length*radii.length*fitness.length];

        int count = 0;


        for (int[] sizes : sizeArr) {

            for (boolean[] gameType : gameTypes) {

                for (double radius : radii) {

                    for (int fitnessValue : fitness) {
                        simulations[count] = new Simulation(count, sizes, radius, gameType[0], gameType[1], 40, 20, 30,fitnessValue);
                        count++;
                    }
                }

            }
        }

        //filename = filename + "/";
//        for (int thread = 0; thread < simulations.length; thread++) {
//
//            try {
//                BufferedWriter writerProportions = new BufferedWriter(new FileWriter(filename+"agentsvsproportions_" + simulations[thread].index + ".txt", true));
//                BufferedWriter writerFitness = new BufferedWriter(new FileWriter(filename+"agentsvfitness_" + simulations[thread].index + ".txt", true));
//                BufferedWriter writerMap = new BufferedWriter(new FileWriter(filename+"letterMap" + simulations[thread].index + ".txt", true));
//
//                if (!new File(filename+"index.txt").exists()) {
//                    BufferedWriter writerIndex = new BufferedWriter(new FileWriter(filename+"index.txt", true));
//
//
//                    writerIndex.write("index,Aprop,Bprop,Cprop,twoPlayer,similar,radius,fitness\n");
//                    writerIndex.close();
//                }
//                writerProportions.write("Generation,Asize,Bsize,Csize,Aprop,Bprop,Cprop\n");
//                writerFitness.write("Generation,Fitness,SimilarityThreshold\n");
//                writerMap.write("Generation,a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z," +
//                        "&, ', (, ), *, +, comma, -, ., /, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, :, ;, <, =, >, ?," +
//                        "A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z\n");
//
//                writerProportions.close();
//                writerFitness.close();
//                writerMap.close();
//
//
//            } catch (IOException e) {
//
//            }
//
//        }



    }

    public SimulationDriver(NEATNetwork network, int generation, int experimentNumber){

        this.experimentNumber = experimentNumber;
        NEATGeneration = generation;
        load(network);
        //
        this.statsRecorder = new StatsRecorder(experimentNumber);



    }

    public SimulationDriver(boolean port){

        if (port)
           loadPort();
        else
            load();

    }


    public synchronized Simulation[] getSimulations(){
        return simulations;
    }


    public void run(){

        simulation.simulate(statsRecorder);
        statsRecorder.writeData();
    }

    public void runMultiple() {


        for (int thread = 0; thread < simulations.length; thread++) {

            simulations[thread].simulatePort();

        }

    }



}






