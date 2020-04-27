import org.encog.neural.neat.NEATNetwork;

import java.io.IOException;
import java.util.*;

public class Population {

    class sortBySimilarity implements Comparator<AgentGenome> {

        char type;

        sortBySimilarity(char type) {
            this.type = type;
        }


        public int compare(AgentGenome a, AgentGenome b) {
            if (a.commonElements(type) > b.commonElements(type))
                return 1;
            else if (a.commonElements(type) < b.commonElements(type))
                return -1;

            return 0;
        }


    }


    ArrayList<AgentGenome> population;
    int size;
    int resources, consumed;
    ArrayList<Character> languageA, languageB, languageC;
    int A, B, C, fitnessA;
    boolean twoPlayer, playWithMostSimilar;
    final int resourcePayout = 20;

    int gamesplayed;

    int wordLength;
    int radius;

    StatsRecorder statsRecorder;

    Cell[][] grid;

    NEATNetwork network;

    public Population(int size) {
        population = new ArrayList<>();
        this.size = size;


        languageA = new ArrayList<>(new AgentGenome('A').genome);
        languageB = new ArrayList<>(new AgentGenome('B').genome);
        languageC = new ArrayList<>(new AgentGenome('C').genome);

        //System.out.println(languageA + "\n" + languageB + "\n" + languageC);


        for (int i = 0; i < size; i++) {

            population.add(new AgentGenome());

        }


    }


    public Population(int A, int B, int C, int resources, int dimx, int dimy, boolean twoPlayer, boolean similar, double radius, boolean stationary, int fitnessA, NEATNetwork network) {


        this.statsRecorder = statsRecorder;

        this.playWithMostSimilar = similar;
        this.resources = resources;
        population = new ArrayList<>();
        this.size = A + B;
        this.twoPlayer = twoPlayer;

        this.radius = (int) Math.round(Math.sqrt(dimx * dimy * radius));


        this.A = A;
        this.B = B;
        this.C = C;
        this.fitnessA = fitnessA;

        this.network = network;


        for (int i = 0; i < A; i++) {

            population.add(new AgentGenome('A', fitnessA));

        }

        for (int i = 0; i < B; i++) {

            population.add(new AgentGenome('B'));

        }


        if (stationary)
            for (int i = 0; i < C; i++) {

                population.add(new AgentGenome('C'));

            }

        setNetworks();


        languageA = new ArrayList<>(population.get(A - 1).genome);
        languageB = new ArrayList<>(population.get(A).genome);
        languageC = new ArrayList<>(new AgentGenome('C').genome);

        //agentSimilarities(A, B, C);


        loadGrid(dimx, dimy);


    }

    public Population(int A, int B, int C, int resources, int dimx, int dimy, boolean twoPlayer, boolean similar, double radius, boolean stationary, int fitnessA, NEATNetwork network, StatsRecorder statsRecorder) {


        this.statsRecorder = statsRecorder;

        this.playWithMostSimilar = similar;
        this.resources = resources;
        population = new ArrayList<>();

        if (stationary)
            this.size = A + B + C;
        else
            this.size = A + B;


        this.twoPlayer = twoPlayer;

        this.radius = (int) Math.round(Math.sqrt(dimx * dimy * radius));


        this.A = A;
        this.B = B;
        this.C = C;
        this.fitnessA = fitnessA;

        this.network = network;


        for (int i = 0; i < A; i++) {

            population.add(new AgentGenome('A', fitnessA));

        }

        for (int i = 0; i < B; i++) {

            population.add(new AgentGenome('B'));

        }


        if (stationary)
            for (int i = 0; i < C; i++) {

                population.add(new AgentGenome('C'));

            }

        setNetworks();


        languageA = new ArrayList<>(population.get(A - 1).genome);
        languageB = new ArrayList<>(population.get(A).genome);
        languageC = new ArrayList<>(new AgentGenome('C').genome);

        //agentSimilarities(A, B, C);


        loadGrid(dimx, dimy);


    }

    public Population(int A, int B, int C, int resources, int dimx, int dimy, boolean twoPlayer, boolean similar, double radius, int fitnessA) {

        this.playWithMostSimilar = similar;
        this.resources = resources;
        population = new ArrayList<>();
        this.size = A + B;
        this.twoPlayer = twoPlayer;

        this.radius = (int) Math.round(Math.sqrt(dimx * dimy * radius));


        this.A = A;
        this.B = B;
        this.C = C;
        this.fitnessA = fitnessA;

        for (int i = 0; i < A; i++) {

            population.add(new AgentGenome('A', fitnessA));

        }

        for (int i = 0; i < B; i++) {

            population.add(new AgentGenome('B'));

        }


        languageA = new ArrayList<>(population.get(A - 1).genome);
        languageB = new ArrayList<>(population.get(A).genome);
        languageC = new ArrayList<>(new AgentGenome('C').genome);

        agentSimilarities(A, B, C);


        loadGrid(dimx, dimy);


    }


    public Population(ArrayList<AgentGenome> agents) {

        this.population = agents;

    }

    public Population(int A, int B, int C, int resources, int dimx, int dimy, boolean twoPlayer, boolean similar, double radius) {

        this.playWithMostSimilar = similar;
        this.resources = resources;
        population = new ArrayList<>();
        this.size = A + B + C;
        this.twoPlayer = twoPlayer;

        this.radius = (int) Math.round(Math.sqrt(dimx * dimy * radius));


        this.A = A;
        this.B = B;
        this.C = C;

        for (int i = 0; i < A; i++) {

            population.add(new AgentGenome('A'));

        }

        for (int i = 0; i < B; i++) {

            population.add(new AgentGenome('B'));

        }

        for (int i = 0; i < C; i++) {

            population.add(new AgentGenome('C'));

        }


        languageA = new ArrayList<>(population.get(A - 1).genome);
        languageB = new ArrayList<>(population.get(A).genome);
        languageC = new ArrayList<>(new AgentGenome('C').genome);

        //      agentSimilarities(A,B,C);

        //System.out.println(languageA + "\n" + languageB + "\n" + languageC);


        loadGrid(dimx, dimy);

    }

    public void loadGrid(int x, int y, boolean swap) {

        gamesplayed = 0;
        wordLength = 0;
        ArrayList<AgentGenome> pop1, pop2;

        Random random = new Random();
        grid = new Cell[y][x];

        for (int i = 0; i < y; i++) {
            Arrays.fill(grid[i], new Cell());
        }
        Stack<Cell> items = new Stack<>();


        pop1 = getAgents('A');

        if (swap)
            pop2 = getAgents('C');
        else
            pop2 = getAgents('B');


        //System.out.println("Population size: " + pop1.size() + " " + pop2.size() + " " + population.size());
        population = pop1;
        population.addAll(pop2);

        items.addAll(pop1);
        items.addAll(pop2);
        Collections.shuffle(items);

        for (AgentGenome agent : population) {
            agent.fitness = 5;
        }

        for (int i = 0; i < resources; i++)
            items.add(new Resource(twoPlayer, random.nextInt(resourcePayout) + 10));


        do {
            int xRand = random.nextInt(x);
            int yRand = random.nextInt(y);

            if (grid[yRand][xRand].getClass() == Cell.class)
                grid[yRand][xRand] = items.pop();


        } while (!items.isEmpty());

    }

    public void loadGrid(int x, int y) {

        gamesplayed = 0;
        wordLength = 0;

        Random random = new Random();
        grid = new Cell[y][x];


        for (int i = 0; i < y; i++) {
            Arrays.fill(grid[i], new Cell());
        }


        Stack<Cell> items = new Stack<>();
        Collections.shuffle(population);
        items.addAll(population);

        for (AgentGenome agent : population) {

            //Sets network
            if (network != null)
            agent.setNetwork(network);
            agent.fitness = 5;

            if (agent.types.length() == 0) {
                if (agent.type == 'A' || agent.commonElements('A') > 0.5) { //if agent language is more than 50%
                    agent.fitness = fitnessA;
                }
            }


        }


        loadResources();


        do {
            int xRand = random.nextInt(x);
            int yRand = random.nextInt(y);

            if (grid[yRand][xRand].getClass() == Cell.class)
                grid[yRand][xRand] = items.pop();


        } while (!items.isEmpty());

        //System.out.println("Population size: " + population.size());


    }


    void setGenerations(int NEATGeneration, int agentGeneration){
        for (AgentGenome agent : population){
            agent.setGenerations(NEATGeneration,agentGeneration);
        }


    }

    void reloadResources(){

        for (int y = 0; y < grid.length; y++)
            for (int x = 0; x < grid[0].length; x++)
                if (grid[y][x].getClass() == Resource.class)
                    grid[y][x] = new Cell();

        loadResources();

    }

    void loadResources(){

        int x = grid[0].length;
        int y = grid.length;

        Random random = new Random();
        Stack<Cell> items = new Stack<>();


        consumed = 0;

        for (int i = 0; i < resources; i++)
            items.add(new Resource(twoPlayer, random.nextInt(resourcePayout) + 10));


        do {
            int xRand = random.nextInt(x);
            int yRand = random.nextInt(y);

            if (grid[yRand][xRand].getClass() == Cell.class)
                grid[yRand][xRand] = items.pop();


        } while (!items.isEmpty());


    }



    void loadResources(int x, int y) {

        Random random = new Random();
        Stack<Cell> items = new Stack<>();


        consumed = 0;

        for (int i = 0; i < resources; i++)
            items.add(new Resource(twoPlayer, random.nextInt(resourcePayout) + 10));


        do {
            int xRand = random.nextInt(x);
            int yRand = random.nextInt(y);

            if (grid[yRand][xRand].getClass() == Cell.class)
                grid[yRand][xRand] = items.pop();


        } while (!items.isEmpty());


    }

    void displayGrid() {

        for (int y = 0; y < grid.length; y++) {
            System.out.println();
            for (int x = 0; x < grid[0].length; x++)
                System.out.print(grid[y][x]);
        }
        System.out.println();
    }



    public void move() {




        Random random = new Random();
        int[] movements = new int[]{-1, 0, 1};
        int x, y;

        for (int i = 0; i < grid.length; i++)
            for (int j = 0; j < grid[0].length; j++) {


                if (grid[i][j].getClass() == AgentGenome.class) {

                    do {
                        x = movements[random.nextInt(3)];
                        y = movements[random.nextInt(3)];

                        int z = x + j;
                        int v = y + i;
                        if (z < grid[0].length - 1 && z > -1 && v < grid.length - 1 && v > -1) {
                            break;
                        }


                    } while (true);

                    if (grid[i + y][j + x].getClass() == Cell.class) {
                        AgentGenome agent = (AgentGenome) grid[i][j];
                        if (agent.fitness >= 0) {
                            grid[i + y][j + x] = agent;
                            grid[i][j] = new Cell();
                        }
                    }

                }
            }


    }

    public boolean checkAdjacency() {


        if (consumed == resources)
            return true;

        for (int i = 0; i < grid.length; i++)
            for (int j = 0; j < grid[0].length; j++) {

                if (grid[i][j].getClass() == Resource.class) {

                    Resource talkingResource = (Resource) grid[i][j];

                    ArrayList<AgentGenome> agents = new ArrayList<>();

                    int h = radius;

                    outerloop:
                    for (int x = i - h; x < i + h; x++)

                        for (int y = j - h; y < j + h; y++) {

                            if (x > -1 && y > -1 && x < grid[0].length && y < grid.length)
                                if (grid[y][x].getClass() == AgentGenome.class) {
                                    AgentGenome agent = (AgentGenome) grid[y][x];
                                    //if (agent.fitness >= 5)
                                        if (agents.size() < 8)
                                            agents.add(agent);
                                        else {

                                            break outerloop;
                                        }
                                    //else
                                    //  System.out.println("Unable to play. Fitness = " + agent.fitness);
                                }


                        }

                    if (agents.size() > 1) {


                        AgentGenome agent1, agent2;


                        if (twoPlayer) {

                            agent1 = agents.get(0);
                            agents.remove(0);


                            if (playWithMostSimilar)
                                agent2 = mostSimilar(agent1, agents);
                            else {
                                agent2 = agents.get(0);
                                Collections.shuffle(agents);
                            }
                            playGame(agent1, agent2, talkingResource);
                            grid[i][j] = new Cell();

                        } else {

                            //System.out.println("played with " + agents.size() + " agents");

                            boolean pass = true;


                            //The selection of agents should take place here.
                            if (pass) {

                                playGame(agents, talkingResource);


                            } else {

                                agent1 = agents.get(0);
                                agents.remove(0);


                                for (AgentGenome agent : agents) {

                                    playGame(agent1, agent, talkingResource);

                                }

                            }

                            grid[i][j] = new Cell();
                        }

                    }

                }

            }


        return false;



    }


    public int countResources() {


        int count = 0;
        for (int i = 0; i < grid.length; i++)
            for (int j = 0; j < grid[0].length; j++)
                if (grid[i][j].getClass() == Resource.class)
                    count++;

        return count;
    }

    public AgentGenome mostSimilar(AgentGenome agent, ArrayList<AgentGenome> agents) {

        AgentGenome similar = new AgentGenome();
        double max = Double.MIN_VALUE;
        for (AgentGenome agentGenome : agents) {
            if (max < agentGenome.commonElements(agent)) {
                max = agentGenome.commonElements(agent);
                similar = agent;
            }
        }

        return similar;

    }

    public int countDeadAgents() {
        int count = 0;
        for (AgentGenome agent : population)
            if (agent.fitness <= 0) {
                count++;
            }
        return count;
    }

    public void setNetworks() {

        for (AgentGenome agent : population) {
            agent.setNetwork(network);
        }

    }

    public void evaluateFitness() {

        Collections.sort(population);

        population.subList(0, (int) (population.size() * 0.2)).clear();

        breed();
    }



    public void breed() {

        Collections.reverse(population);

        ArrayList<AgentGenome> fittest = new ArrayList<>(population.subList(0, (int) (population.size() * 0.2)));
        population.subList(0, (int) (population.size() * 0.2)).clear();

        Collections.shuffle(fittest);
        population.addAll(0, fittest);



        for (int s = 0; s < population.size(); s++) {


            if (population.size() >= size) {
                break;
            }
            AgentGenome parent1 = population.get(s);
            AgentGenome parent2 = population.get(s + 1);


            AgentGenome[] children = parent1.crossover(parent2);

            population.add(children[0]);
            if (population.size() < size)
                population.add(children[1]);


        }


    }

    public void display() {

        for (AgentGenome genome : population)
            System.out.println(genome + "\n");

    }

    public void playGame(AgentGenome agent1, AgentGenome agent2, Resource resource) {


        Random random = new Random();

        int len = random.nextInt(8) + 3;

        wordLength += len;
        gamesplayed++;

        new Game(agent1, agent2, resource).Start(len);
    }

    public void playGame(ArrayList<AgentGenome> agents, Resource resource) {


        Random random = new Random();

        int len = random.nextInt(8) + 3;

        wordLength += len;
        gamesplayed++;


        Game game = new Game(agents, resource);
        game.StartNAgent(len);
        //Writes words to textfile
//        if (game.agreed != null)
//            statsRecorder.collectData(game.agreed);


    }



    public void playGame(AgentGenome agent1, AgentGenome agent2, Resource resource, boolean generate) {


        Random random = new Random();

        int len = random.nextInt(8) + 3;

        wordLength += len;
        gamesplayed++;

        new Game(agent1, agent2, resource).Start(len);
    }

    public double averageWordLength() {

        return wordLength / gamesplayed;

    }

    public double averageThreshold() {


        double average = 0;
        for (AgentGenome agent : population) {
            average += agent.similarityThreshold;

        }

        return average / size;

    }



    public double minThreshold() {

        double min = Double.MAX_VALUE;
        AgentGenome mostAgreeable = new AgentGenome();
        for (AgentGenome agent : population) {
            if (min > agent.similarityThreshold) {
                min = agent.similarityThreshold;
                mostAgreeable = agent;
            }
        }

        // System.out.println("Min Agent: Similarity Threshold = "+ mostAgreeable.similarityThreshold+" | Fitness = " + mostAgreeable.fitness + " | Win Ratio = "+ mostAgreeable.winRatio());
        return min;
    }

    public double maxThreshold() {

        double max = Double.MIN_VALUE;
        AgentGenome leastAgreeable = new AgentGenome();
        for (AgentGenome agent : population) {
            if (max < agent.similarityThreshold) {
                max = agent.similarityThreshold;
                leastAgreeable = agent;
            }
        }


        return max;
    }

    public AgentGenome fittest() {

        int max = Integer.MIN_VALUE;
        AgentGenome fittest = new AgentGenome();
        for (AgentGenome agent : population) {
            if (max < agent.fitness) {
                fittest = agent;
                max = agent.fitness;
            }


        }


        //System.out.println("Fittest Agent: Similarity Threshold = " + fittest.similarityThreshold + " | Fitness = "+ fittest.fitness + " | Win Ratio = " + fittest.winRatio());
        return fittest;
    }

    public AgentGenome leastfit() {

        int min = Integer.MAX_VALUE;
        AgentGenome leastfit = new AgentGenome();
        for (AgentGenome agent : population) {
            if (min > agent.fitness) {
                leastfit = agent;
                min = agent.fitness;
            }


        }


        return leastfit;
    }

    public String[][] returnArray() {

        String[][] array = new String[grid.length][grid[0].length];

        for (int i = 0; i < grid.length; i++)
            for (int j = 0; j < grid[0].length; j++)
                array[i][j] = grid[i][j].toString();

        return array;
    }

    public ArrayList<Double> similarities() {


        AgentGenome fittest = fittest();
        ArrayList<Double> similarity = new ArrayList<>();

        Collections.sort(population);


        for (int i = 0; i < population.size() - 1; i++) {
            similarity.add(fittest.commonElements(population.get(i)));

        }

        return similarity;


    }

    public double averageFitness() {

        double sum = 0;
        for (AgentGenome agent : population) {
            sum += agent.fitness;
        }

        return sum / population.size();

    }

    public Map<Character, Integer> evaluateSimilarities() {

        ArrayList<Character> combined = new ArrayList<>(languageA);
        Map<Character, Integer> map = new HashMap<>();
        combined.addAll(languageB);
        combined.addAll(languageC);

        int occurrences;

        for (char unit : combined) {
            occurrences = 0;
            for (AgentGenome agent : population) {
                occurrences += Collections.frequency(agent.genome, unit);
            }
            map.put(unit, occurrences);
        }


        return map;


    }

    public void agentSimilarities(int A, int B, int C) {

        float a, b, c;
        a = b = c = 0;

        for (AgentGenome agentGenome : population) {

            if (agentGenome.type == 'A')
                a += agentGenome.similarityThreshold;
            else if (agentGenome.type == 'B')
                b += agentGenome.similarityThreshold;
            else
                c += agentGenome.similarityThreshold;


        }

        //System.out.println("Average A ST: " + a / A + "\nAverage B ST: " + b / B + "\nAverage C ST: " + c / C);


    }

    public void cull() {
        population.removeIf(agentGenome -> agentGenome.fitness == 0);

    }


    public void migrate() {

        //Collections.sort(population);
        //population.subList(0,(population.size() - A + 1)).clear();

        mostSimilarAgents('A');

        for (int i = 0; i < C; i++) {

            population.add(new AgentGenome('C'));

        }

        //System.out.println("Population size: " + population.size());
        //System.out.println("C agents:" + getAgents('C').size());


    }

    public void consumeResource(Resource resource) {

        double totalProportions = 0;

        for (AgentGenome agent : population) {

            totalProportions += agent.commonElements();

        }

        for (AgentGenome agent : population) {

            agent.win((int)Math.ceil((agent.commonElements() / totalProportions) * resource.reward));
            agent.candidateTerm = null;

        }

        consumed++;


    }

    public ArrayList<AgentGenome> getAgents(char type) {

        ArrayList<AgentGenome> agents = new ArrayList<>();

        Collections.sort(population);

        for (AgentGenome agent : population) {
            if (agent.type == type)
                agents.add(agent);
        }


        return agents;
    }

    public void mostSimilarAgents(char type) {

        Collections.sort(population, new sortBySimilarity(type));
        //System.out.println(B);
        population.subList(0, B).clear();

    }


    public synchronized double averageResourcesConsumed(){

        double resourcesConsumed = 0;
        for (AgentGenome agent : population){

            resourcesConsumed += agent.resourcesConsumed;
        }


        return resourcesConsumed/population.size();

    }




    public boolean agreed() {

        Collections.shuffle(population);


        Iterator<AgentGenome> iterator = population.iterator();


        AgentGenome agent = iterator.next();

        while (iterator.hasNext()) {

            if (!iterator.next().checkTerm(agent)) {
                return false;
            }

        }


        return true;


    }


    public int totalFitness(){

        int sum = 0;
        for (AgentGenome agent : population) {
            sum += agent.fitness;
        }

        return sum;

    }

    public int totalConsumed(){

        int resourcesConsumed = 0;
        for (AgentGenome agent : population){

            resourcesConsumed += agent.resourcesConsumed;
        }


        return resourcesConsumed;

    }


    public double[] normalisedData(){


        int max = fittest().fitness;
        int min = leastfit().fitness;

        double[] normalisedFitness = new double[population.size()];

        for (int i = 0; i < population.size(); i++){

            normalisedFitness[i] = normalise(max,min,population.get(i).fitness);

        }

        return normalisedFitness;

    }




    double normalise(int min, int max, int data){

        return (double)(data - min)/(max - min);

    }


    double normalise(int min, int max, double data){

        return (data - min)/(max - min);

    }

    void setStatsRecorder(StatsRecorder statsRecorder){

        this.statsRecorder =statsRecorder;
    }



}
