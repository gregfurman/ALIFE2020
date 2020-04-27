import java.util.Arrays;

public class test {


    static void testNeuroEvolution(int exp) {


        new Neuroevolution(1,exp);


    }

    static void testNeuroEvolution(String[] experiments) {


        for (String exp : experiments)
            for (int i = 0; i < 20; i ++) {
                Thread thread = new Thread(new Neuroevolution(i+1,Integer.valueOf(exp)));
                thread.start();
            }

    }

    public static void main(String[] args) {


        System.out.println(new AgentGenome('A').getInfo());
        System.out.println(new AgentGenome('B').getInfo());
        System.out.println(new AgentGenome('C').getInfo());


        if (args.length > 0) {

            testNeuroEvolution(args);


        } else {

            boolean[] moving = {true,false};
            boolean[][] gameTypes = {{false, false}};
            int[][] sizeArr = {{50,50,50},{20,65,65},{110,20,20}};
            int[] fitness = {5,15};
            double[] radii = {0.25};

            int count =  0;

            for (boolean stationary : moving)
                for (int[] sizes : sizeArr) {

                    for (boolean[] gameType : gameTypes) {

                        for (double radius : radii) {

                            for (int fitnessValue : fitness) {

                                count++;
                                System.out.println(count + ": " + stationary + " " + Arrays.toString(sizes) + " " + fitnessValue);

                            }
                        }

                    }
                }


            System.out.println("No experiment indexes supplied.");
        }


    }


}
