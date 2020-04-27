import java.io.*;
import java.util.Map;

public class StatsRecorder {


    volatile int index;
    String scores, fitness, proportions, words;
    final static String DIRECTORY_PREFIX = "./data_";

    public StatsRecorder(int index) {

        this.index = index;

        //fitness = "NEATGeneration,agentGeneration,averageFitness,averageResourcesConsumed,averageThreshold\n";

        scores = fitness = proportions = words = "";

//        scoreWriter = new BufferedWriter(new FileWriter("scores_" + index + ".csv", true));
//        fitnessWriter = new BufferedWriter(new FileWriter("agentsvfitness_" + index + ".txt", true));
//        proportionsWriter = new BufferedWriter(new FileWriter("agentsvsproportions_" + index + ".txt", true));
//        wordWriter = new BufferedWriter(new FileWriter("wordlist_" + index + ".txt", true));
//
//
//
//        if (new File("scores_" + index + ".csv").exists()) {
//                scoreWriter = new BufferedWriter(new FileWriter("scores_" + index + ".csv", true));
//            } else {
//                scoreWriter = new BufferedWriter(new FileWriter("scores_" + index + ".csv", true));
//                scoreWriter.write("NEATGeneration,fitness,consumed,score\n");
//            }
//
//            if (new File("wordlist_" + index + ".txt").exists()) {
//                wordWriter = new BufferedWriter(new FileWriter("wordlist_" + index + ".txt", true));
//            } else {
//                wordWriter = new BufferedWriter(new FileWriter("wordlist_" + index + ".txt", true));
//                wordWriter.write("NEATGeneration,agentGeneration,word\n");
//            }
//
//            if (new File("agentsvfitness_" + index + ".txt").exists()) {
//
//                fitnessWriter = new BufferedWriter(new FileWriter("agentsvfitness_" + index + ".txt", true));
//
//            } else {
//
//                fitnessWriter = new BufferedWriter(new FileWriter("agentsvfitness_" + index + ".txt", true));
//                fitnessWriter.write("NEATGeneration,agentGeneration,averageFitness,averageResourcesConsumed,averageThreshold\n");
//
//            }
//
//
//            if (new File("agentsvsproportions_" + index + ".txt").exists()) {
//
//                proportionsWriter = new BufferedWriter(new FileWriter("agentsvsproportions_" + index + ".txt", true));
//
//            } else {
//
//                proportionsWriter = new BufferedWriter(new FileWriter("agentsvsproportions_" + index + ".txt", true));
//                proportionsWriter.write("NEATGeneration,Generation,Asize,Bsize,Csize,A,B,C\n");
//            }


    }


    void collectData(int NEATGeneration, int agentGeneration, Population population, double[] values) {


        fitness += NEATGeneration + "," + (agentGeneration + 1) + "," + population.averageFitness() + "," + population.averageResourcesConsumed() + "," + population.averageThreshold() + "\n";
        proportions += NEATGeneration + "," + (agentGeneration + 1) + "," + population.A + ","
                + population.B + ","
                + population.C + "," + values[0] + "," + values[1] + "," + values[2] + "\n";

//        try {
//                BufferedWriter fitnessWriter;
//
//                if (new File("exp_"+index+"/agentsvfitness.txt").exists()) {
//
//                    fitnessWriter = new BufferedWriter(new FileWriter("exp_"+index+"/agentsvfitness.txt", true));
//
//                } else {
//
//                    fitnessWriter = new BufferedWriter(new FileWriter("exp_"+index+"/agentsvfitness.txt", true));
//                    fitnessWriter.write("NEATGeneration,agentGeneration,averageFitness,averageResourcesConsumed,averageThreshold\n");
//
//                }
//
//
//                fitnessWriter.write(NEATGeneration + "," + (agentGeneration + 1) + "," + population.averageFitness() + "," + population.averageResourcesConsumed() + "," + population.averageThreshold() + "\n");
//                fitnessWriter.close();
//            } catch (IOException e) {
//                System.out.println("writing to fitness failed");
//                System.out.println(NEATGeneration + "," + (agentGeneration + 1) + "," + population.averageFitness() + "," + population.averageResourcesConsumed() + "," + population.averageThreshold());
//            }
//
//
//
//            try {
//                BufferedWriter proportionsWriter;
//
//                if (new File("exp_"+index+"/agentsvsproportions.txt").exists()) {
//
//                    proportionsWriter = new BufferedWriter(new FileWriter("exp_"+index+"/agentsvsproportions.txt", true));
//
//                } else {
//
//                    proportionsWriter = new BufferedWriter(new FileWriter("exp_"+index+"/agentsvsproportions.txt", true));
//                    proportionsWriter.write("NEATGeneration,Generation,Asize,Bsize,Csize,A,B,C\n");
//                }
//
//                proportionsWriter.write(NEATGeneration + "," + (agentGeneration + 1) + "," + population.A + ","
//                        + population.B + ","
//                        + population.C + "," + values[0] + "," + values[1] + "," + values[2] + "\n");
//                proportionsWriter.close();
//            } catch (IOException e) {
//                System.out.println("writing to proportions failed");
//                System.out.println(NEATGeneration + "," + (agentGeneration + 1) + "," + population.A + ","
//                        + population.B + ","
//                        + population.C + "," + values[0] + "," + values[1] + "," + values[2]);
//
//            }


    }


    void collectData(int NEATGeneration, double score1, double score2) {

//            BufferedWriter scoreWriter;

        scores += NEATGeneration + "," + score1 + "," + score2 + "," + (score1 - score2) + "\n";

//                if (new File("exp_"+index+"/scores.csv").exists()) {
//                scoreWriter = new BufferedWriter(new FileWriter("exp_"+index+"/scores.csv", true));
//            } else {
//                scoreWriter = new BufferedWriter(new FileWriter("exp_"+index+"/scores.csv", true));
//                scoreWriter.write("NEATGeneration,fitness,consumed,score\n");
//            }
//        scoreWriter.write( NEATGeneration +","+score1 + ","+ score2 + "," + (0.5*score1-0.5*score2) +"\n");
//            scoreWriter.close();

    }

    void collectData(AgentGenome agent) {


        words += agent.NEATGeneration + "," + agent.agentGeneration + "," + agent.candidateTerm + "\n";

//        BufferedWriter wordWriter;
//
//        if (new File("exp_"+index+"/wordlist.txt").exists()) {
//                wordWriter = new BufferedWriter(new FileWriter("exp_"+index+"/wordlist.txt", true));
//            } else {
//                wordWriter = new BufferedWriter(new FileWriter("exp_"+index+"/wordlist.txt", true));
//                wordWriter.write("NEATGeneration,agentGeneration,word\n");
//            }
//
//
//        wordWriter.write(agent.NEATGeneration + "," + agent.agentGeneration + "," + agent.candidateTerm + "\n");
//
//        wordWriter.close();
    }


    void writeData() {


        if (words.length() + fitness.length() + proportions.length() + scores.length() == 0)
            System.out.println("Length of files are 0");
        else {


            BufferedWriter wordWriter, proportionsWriter, scoreWriter, fitnessWriter;


//            try {
//
//
//                if (new File(DIRECTORY_PREFIX + index + "/wordlist.txt").exists()) {
//                    wordWriter = new BufferedWriter(new FileWriter(DIRECTORY_PREFIX + index + "/wordlist.txt", true));
//                } else {
//                    wordWriter = new BufferedWriter(new FileWriter(DIRECTORY_PREFIX + index + "/wordlist.txt", true));
//                    wordWriter.write("NEATGeneration,agentGeneration,word\n");
//                }
//
//
//                wordWriter.write(words);
//                wordWriter.close();
//            } catch (IOException e) {
//
//                System.out.println("cannot write to word list");
//            }



            try {

                if (new File(DIRECTORY_PREFIX + index + "/agentsvsproportions.txt").exists()) {

                    proportionsWriter = new BufferedWriter(new FileWriter(DIRECTORY_PREFIX + index + "/agentsvsproportions.txt", true));

                } else {

                    proportionsWriter = new BufferedWriter(new FileWriter(DIRECTORY_PREFIX + index + "/agentsvsproportions.txt", true));
                    proportionsWriter.write("NEATGeneration,Generation,Asize,Bsize,Csize,A,B,C\n");
                }

                proportionsWriter.write(proportions);
                proportionsWriter.close();

            } catch (IOException e) {
                System.out.println("Cannot write to proportioons");
            }


            try {

                String filename = DIRECTORY_PREFIX + index + "/scores.csv";
                if (new File(filename).exists()) {
                    scoreWriter = new BufferedWriter(new FileWriter(filename, true));
                } else {
                    scoreWriter = new BufferedWriter(new FileWriter(filename, true));
                    scoreWriter.write("NEATGeneration,fitness,consumed,score\n");
                }

                scoreWriter.write(scores);
                scoreWriter.close();

            } catch (IOException e) {
                System.out.println("cannot write to scores");
            }

            try {


                File file = new File(DIRECTORY_PREFIX + index, "agentsvfitness.txt");

                if (file.exists()) {

                    fitnessWriter = new BufferedWriter(new FileWriter(file, true));

                } else {
                    fitnessWriter = new BufferedWriter(new FileWriter(file));
                    fitnessWriter.write("NEATGeneration,agentGeneration,averageFitness,averageResourcesConsumed,averageThreshold\n");

                }

                fitnessWriter.write(fitness);
                fitnessWriter.close();

            } catch (IOException e) {
                System.out.println("Unable write to fitness.");
            }


        }


    }


}
