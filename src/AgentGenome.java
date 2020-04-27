import org.encog.neural.neat.NEATNetwork;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class AgentGenome extends Cell implements Comparable<AgentGenome> {


    ArrayList<Character> genome = new ArrayList<>();

    double similarityThreshold;
    int fitness = 5;
    int resourcesConsumed = 0;
    int talkingThreshold;
    char type;
    String types = "";
    NEATController controller;


    int NEATGeneration, agentGeneration;

    String candidateTerm;

    AgentGenome parent1;
    AgentGenome parent2;

    private static char[] language_types = {'A','B','C'};
    private Random random = new Random();


    public AgentGenome(double similarityThreshold, char languageType){
        loadLanguage(languageType);
        this.similarityThreshold = similarityThreshold;
        type = languageType;


    }

    public AgentGenome(double similarityThreshold, ArrayList<Character> genome, AgentGenome parent1, AgentGenome parent2){


        this.similarityThreshold  = (Math.round((similarityThreshold) * 10) / 10.0);


        if (parent1.types.length() + parent2.types.length() == 0 )
            types += String.valueOf(parent1.type) + String.valueOf(parent2.type);
        else if (parent1.types.length() > 0)
            types += parent1.types + String.valueOf(parent2.type);
        else if (parent2.types.length() > 0)
            types += String.valueOf(parent1.type) + parent2.types;
        else
            types += parent1.types + parent2.types;

        this.genome = new ArrayList<>(genome);



    }

    public AgentGenome(){

        similarityThreshold = Math.round(Math.random() * 10) / 10.0;
        //similarityThreshold = 0.1;
        type = language_types[random.nextInt(3)];
        loadLanguage(type);


    }

    public AgentGenome(AgentGenome agent){
        genome = new ArrayList<>(agent.genome);
        similarityThreshold = agent.similarityThreshold;
        fitness = agent.fitness;
        type = agent.type;

        NEATGeneration = agent.NEATGeneration;
        agentGeneration = agent.agentGeneration;

        parent1 = agent.parent1;
        parent2 = agent.parent2;
        resourcesConsumed = agent.resourcesConsumed;
        controller = agent.controller;
        candidateTerm = agent.candidateTerm;

    }

    public AgentGenome(char type){

        similarityThreshold = Math.round(Math.random() * 10) / 10.0;
        this.type = type;
        loadLanguage(type);

    }

    public AgentGenome(char type, int fitness){

        similarityThreshold = Math.round(Math.random() * 10) / 10.0;
        this.type = type;
        loadLanguage(type);
        this.fitness = fitness;

    }


    public void setNetwork(NEATNetwork network){
        controller = new NEATController(network);
    }


    public void setFitness(int fitness){
        this.fitness = fitness;
    }
    private void loadLanguage(char type) {
        char ch;
        int index = 0;
        int size  = 26;

        switch (type) {

            case 'A':
                for (ch = 'a'; ch <= 'z'; ch++) {
                    if (index == size)
                        break;
                    genome.add(ch);
                    index++;
                }
                break;
            case 'B':
                for (index = 38; index < (64); index++) {
                    genome.add((char) index);
                }
                break;

            case 'C':
                for (ch = 'A'; ch <= 'Z'; ch++) {
                    if (index == size)
                        break;
                    genome.add(ch);
                    index++;
                }

        }

    }


    void setGenerations(int NEATGeneration, int agentGeneration){
        this.agentGeneration = agentGeneration;
        this.NEATGeneration = NEATGeneration;

    }

    public String generateWord(int wordSize){

        String word = "";
        for (int i = 0;i<wordSize;i++){
            word += genome.get(random.nextInt(genome.size()));
        }

        return word;
    }

    public boolean checkWord(String word){

        return (wordRatio(word) >= similarityThreshold);
    }

    public double wordRatio(String word){

        char[] letters = word.toCharArray();

        int length = letters.length;
        int count = 0;

        for (char letter :letters){
            if (genome.contains(letter))
                count++;
        }


        return (double)count/length ;
    }

    public void addMorphemes(String word){

        char[] letters = word.toCharArray();

        for (char letter :letters){
            if (!genome.contains(letter))
                genome.add(letter);

        }

    }

    public AgentGenome[] crossover(AgentGenome other){


        ArrayList<Character> parent2Genome = new ArrayList<>(other.genome);
        ArrayList<Character> parent1Genome = new ArrayList<>(genome);


        int parent1GenomeSize = genome.size();
        int parent2GenomeSize = other.genome.size();
        int points;


        PriorityQueue<Integer> indices = new PriorityQueue<>();

        Collections.shuffle(parent1Genome);
        Collections.shuffle(parent2Genome);

        ArrayList<Character> child1 = new ArrayList<>(parent1Genome);
        ArrayList<Character> child2 =  new ArrayList<>(parent2Genome);


        int length = Math.max(parent1GenomeSize,parent2GenomeSize);

            points = random.nextInt(length-1)+1;

            for(int i = 0 ; i < points; i++){
                indices.add(random.nextInt(length));
            }


            if (points%2 ==1) {
                indices.add(length - 1);
            }
            Set<Integer> set = new HashSet<>(indices);
            indices.clear();
            indices.addAll(set);


        while (!indices.isEmpty()) {

                int start = indices.poll();
                int end;

                if (indices.isEmpty()){
                    break;
                }
                else
                    end = indices.poll();

                if (end+1 > child1.size() && end+1 > child2.size())
                    break;
                    else
                replace(child1,child2,start,end);

                }


        double max = Math.max(other.similarityThreshold,similarityThreshold);
        double min = Math.min(other.similarityThreshold,similarityThreshold);

        AgentGenome[] children = new AgentGenome[2];


        if (min - max != 0) {
            children[0] = new AgentGenome(ThreadLocalRandom.current().nextDouble(min, max), child1, this,other);
            children[1] = new AgentGenome(ThreadLocalRandom.current().nextDouble(min, max), child2,this,other);
        }
        else {
            children[0] = new AgentGenome(min,child1, this,other);
            children[1] = new AgentGenome(min, child2, this, other);
        }

        children[0].mutate();
        children[1].mutate();

        children[0].parent1 = new AgentGenome(this);
        children[0].parent2 = new AgentGenome(other);

        children[1].parent1 = new AgentGenome(this);
        children[1].parent2 = new AgentGenome(other);

        return children;
    }

    private void replace(ArrayList<Character> list1,ArrayList<Character> list2,int start, int end){


        end += 1;

        if (start > list1.size()){

            ArrayList<Character> section2 = new ArrayList<>(list2.subList(start,end));

            list2.subList(start,end).clear();
            list1.addAll(section2);

        }else if (start > list2.size()){

            ArrayList<Character> section1 = new ArrayList<>(list1.subList(start,end));

            list1.subList(start,list1.size()).clear();
            list2.addAll(section1);

        } else if (end > list1.size()){

            ArrayList<Character> section1 = new ArrayList<>(list1.subList(start,list1.size()));
            ArrayList<Character> section2 = new ArrayList<>(list2.subList(start,end));

            list1.subList(start,list1.size()).clear();
            list2.subList(start,end).clear();

            list1.addAll(section2);
            list2.addAll(section1);

        } else if (end > list2.size()){

            ArrayList<Character> section1 = new ArrayList<>(list1.subList(start,end));
            ArrayList<Character> section2 = new ArrayList<>(list2.subList(start,list2.size()));

            list1.subList(start,end).clear();
            list2.subList(start,list2.size()).clear();

            list2.addAll(section1);
            list1.addAll(section2);


        } else {
            ArrayList<Character> section1 = new ArrayList<>(list1.subList(start, end));
            ArrayList<Character> section2 = new ArrayList<>(list2.subList(start, end));

            list1.subList(start, end).clear();
            list2.subList(start, end).clear();

            list1.addAll(start, section2);
            list2.addAll(start, section1);
        }

    }

    public double commonElements(AgentGenome other){

        ArrayList<Character> common = new ArrayList<>(genome);

        common.retainAll(other.genome);

        return (double) common.size()/(genome.size());

    }


    public double commonElements(char type){

        ArrayList<Character> common = new ArrayList<>(genome);

        common.retainAll(new AgentGenome(type).genome);

        return (double) common.size()/(genome.size());

    }

    public double commonElements(){

        ArrayList<Character> word = new ArrayList<>();

        for (char element : candidateTerm.toCharArray()){
            word.add(element);
        }

        word.retainAll(genome);


        return (double) word.size()/(candidateTerm.length());

    }

    public void setCandidateTerm(String candidateTerm) {
        this.candidateTerm = candidateTerm;
    }

    public String getInfo(){

        String str;
        if (types.length() > 0) {
            str = "Types: " + types + "\nSimilarity Threshold: " + similarityThreshold + "\nGenome" + genome.toString();

        } else
            str = "Type: " + type + "\nSimilarity Threshold: " + similarityThreshold + "\nGenome" + genome.toString();


        return str;

    }

    public void mutate(){

        if (random.nextFloat() < 0.05) {
            AgentGenome temp = new AgentGenome();
            genome.set(random.nextInt(genome.size()), temp.genome.get(random.nextInt(temp.genome.size())));
        }

        //This adds a gaussian constant to the similarity threshold.
        if (random.nextFloat() < 0.05){
            similarityThreshold += random.nextGaussian()*0.5;
            if (similarityThreshold < 0)
                similarityThreshold =0;
            else if(similarityThreshold >= 1)
                similarityThreshold = 1;
        }
    }

    public int compareTo(AgentGenome other){

        return Integer.compare(fitness,other.fitness);

    }

    public boolean equals(AgentGenome other){

        return other.type == type && other.fitness == fitness && other.similarityThreshold == similarityThreshold && other.genome.equals(genome);

    }

    public String mutateWord(String word){

        char[] wordArray = word.toCharArray();
        String candidateWord;

        int randomint = random.nextInt(wordArray.length);

        calculateTalkingThreshold();

        while (wordRatio(String.valueOf(wordArray)) < similarityThreshold){


            if (fitness <= talkingThreshold){
                return String.valueOf(wordArray);
            }

            if (random.nextInt(10) > 0) {

                if (!genome.contains(wordArray[randomint])) {
                    wordArray[randomint] = genome.get(random.nextInt(genome.size()));
                    consume();

                } else
                    randomint = random.nextInt(wordArray.length);
            }else {

                if (!genome.contains(wordArray[randomint])) {
                    candidateWord = String.valueOf(wordArray);
                    candidateWord += genome.get(random.nextInt(genome.size()));
                    wordArray = candidateWord.toCharArray();
                    consume();
                } else
                    randomint = random.nextInt(wordArray.length);
            }



        }


        return String.valueOf(wordArray);
    }


    public void win(Resource payout){
        fitness += payout.reward;

    }

    public void win(int payout){
        fitness += payout;

    }


    public void consume(){

        resourcesConsumed++;
        fitness--;

    }




    public String toString(){
        return "Resources Consumed: " + resourcesConsumed + "\nFitness: " + fitness + "\n";
    }

    public void displayGenerations(){


        if (this.parent1 != null)
            displayGenerations(this);
    }

    void displayGenerations(AgentGenome agent){

        if(agent != null){
            if(agent.parent1 != null && agent.parent2 != null){
                System.out.println();
                System.out.print("Child:    " + agent.genome+ "\n");
                System.out.print("Parent 1: " + agent.parent1.genome + "\n");
                System.out.print("Parent 2: " + agent.parent2.genome + "\n");
                displayGenerations(agent.parent1);
                displayGenerations(agent.parent2);
            }
            else if(agent.parent1 == null && agent.parent2 == null){
                return;
            }
            else if(agent.parent2 == null){
                System.out.print(agent.parent1.genome + " ");
            }
            else{
                System.out.print(agent.parent2.genome + " ");
            }
        }
    }

    public void calculateTalkingThreshold(){

        talkingThreshold = (int) Math.round(fitness*0.2);

    }

    public boolean checkTerm(AgentGenome agent){

        if (candidateTerm == null || agent.candidateTerm == null) return false;

        return agent.candidateTerm.equals(candidateTerm);

    }


    public String getCandidateTerm(){
        if (candidateTerm == null)
            return "";

        return candidateTerm;

    }

    public AgentGenome choose(ArrayList<AgentGenome> agents){

        int index;

        agents = new ArrayList<>(agents);
        agents.remove(this);


        if (candidateTerm != null) {

            Iterator<AgentGenome> iterator = agents.iterator();
            while (iterator.hasNext()) {
                AgentGenome agent = iterator.next();

                if (agent.candidateTerm != null)
                    if (agent.candidateTerm.equals(candidateTerm)){
                        iterator.remove();
                    }

            }


        }



        if (controller != null) {

            double[] inputs = new double[8];
            Arrays.fill(inputs, 0);




            if (agents.size() == 0)
                return null;

            int count = 0;
            for (AgentGenome agent : agents) {
                inputs[count] = commonElements(agent);
                count ++;
            }
            index = controller.control(agents, inputs);

        } else{

            index = random.nextInt(agents.size());


        }

        return agents.get(index);

    }



}
