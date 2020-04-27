import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.neat.NEATNetwork;

import java.util.ArrayList;

public class NEATController {


    private NEATNetwork network;


    public NEATController(NEATNetwork network){

    this.network = network;

    }

    public int control(ArrayList<AgentGenome> agents, double[] networkInputs)
    {


        int amount = agents.size();
        MLData networkOutput = network.compute(getInputs(networkInputs));
        return selectAgentIndex(networkOutput, amount);



    }

    private BasicMLData getInputs(double[] networkInputs) {
        return new BasicMLData(networkInputs);

    }

    private int selectAgentIndex(MLData networkOutput, int amount){
        /**
         * @param amount the number of agents in a given radius.
         */
        double[] outputs = networkOutput.getData();

        int largest = 0;
        for ( int i = 1; i < amount; i++ )
        {
            if ( outputs[i] > outputs[largest] ) largest = i;
        }



        return largest;


    }


    }
