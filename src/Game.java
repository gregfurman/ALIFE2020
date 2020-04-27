
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class Game {


    AgentGenome agent1;
    AgentGenome agent2;
    Population agents;
    Resource resource;

    AgentGenome agreed;

    public Game(AgentGenome agent1,AgentGenome agent2, Resource resource){

        this.agent1 = agent1;
        this.agent2 = agent2;
        this.resource = resource;

    }


    public Game(ArrayList<AgentGenome> agents, Resource resource){

        this.resource = resource;
        this.agents = new Population(agents);


    }

    //
    public synchronized void StartNAgent(int wordLength){


        //System.out.println("Before game: " + agents.averageFitness());

        Collections.shuffle(agents.population);

        AgentGenome speaker, hearer;

        speaker = agents.population.get(0);
        agents.population.remove(0);



//        System.out.println("Start Game");
        do{

            //ANN
            hearer = speaker.choose(agents.population);

            if (hearer == null){
                break;
            } else {
                Start(wordLength, speaker, hearer);
                speaker = hearer;
            }



        }while (!agents.agreed());


        agreed = new AgentGenome(agents.population.get(0));

        agents.consumeResource(resource);

    }

    public void setAgent(AgentGenome agent){

    }



    public String Start(int wordLength){

        String word;

        AgentGenome speaker = new AgentGenome(agent1);
        AgentGenome hearer = new AgentGenome(agent2);

        word = speaker.generateWord(wordLength);


        do{


            if (hearer.wordRatio(word) >= hearer.similarityThreshold || speaker.fitness <= speaker.talkingThreshold){



                    agent1.win((int)agent1.wordRatio(word)*resource.reward);
                    agent2.win((int)agent2.wordRatio(word)*resource.reward);


                    agent1.addMorphemes(word);
                    agent2.addMorphemes(word);



                return word;

                } else{

                AgentGenome temp = new AgentGenome(speaker);
                speaker = new AgentGenome(hearer);
                hearer = temp;

                word = speaker.mutateWord(word);



            }


        } while (true);


    }


    public StatsRecorder getStatsRecorder(StatsRecorder statsRecorder){
        return statsRecorder;

    }

    public void Start(int wordLength, AgentGenome agent1,AgentGenome agent2){

        String word;

        AgentGenome speaker = agent1;
        AgentGenome hearer = agent2;

        if (speaker.candidateTerm == null)

            word = speaker.generateWord(wordLength);
        else
            word = speaker.candidateTerm;




        do{

            if (hearer.wordRatio(word) >= hearer.similarityThreshold || speaker.fitness <= speaker.talkingThreshold){


                agent1.addMorphemes(word);
                agent2.addMorphemes(word);


                agent1.setCandidateTerm(word);
                agent2.setCandidateTerm(word);
                return;
            } else{

                AgentGenome temp = speaker;
                speaker = (hearer);

                hearer = (temp);


                word = speaker.mutateWord(word);


            }


        } while (true);


    }




}
