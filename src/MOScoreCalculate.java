import org.encog.ml.CalculateScore;
import org.encog.ml.MLMethod;
import org.encog.ml.fitness.MultiObjectiveFitness;
import org.encog.neural.neat.NEATNetwork;

public class MOScoreCalculate extends MultiObjectiveFitness {

    public NEATNetwork network;


    @Override
    public void addObjective(double weight, CalculateScore fitnessFunction) {
        super.addObjective(weight, fitnessFunction);
    }

    @Override
    public double calculateScore(MLMethod method) {
        return super.calculateScore(method);
    }


    public MOScoreCalculate() {
        ;
    }

    @Override
    public boolean shouldMinimize() {
        return super.shouldMinimize();
    }

    @Override
    public boolean requireSingleThreaded() {
        return super.requireSingleThreaded();
    }
}
