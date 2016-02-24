package frbs;

import nrc.fuzzy.*;
import reactor.Behaviour;

/**
 * Created by jcozar on 27/01/16.
 */
public class Database {

    private FuzzyVariable fuzzyTemp;
    private FuzzyVariable fuzzyPres;
    private FuzzyVariable fuzzyValveTemp;
    private FuzzyVariable fuzzyValvePres;

    public final static int TEMPERATURE = 0;
    public final static int PRESSURE = 1;
    public final static int VALVE_TEMPERATURE = 2;
    public final static int VALVE_PRESSURE = 3;

    public FuzzyVariable getVariable(int variable){
        switch(variable){
            case TEMPERATURE:
                return fuzzyTemp;
            case PRESSURE:
                return fuzzyPres;
            case VALVE_TEMPERATURE:
                return fuzzyValveTemp;
            default:
                return fuzzyValvePres;
        }
    }


    public Database(){


        //Define the fuzzy variables and fuzzy partitions
        try {
            //Create the input fuzzy variabales
            fuzzyTemp = new FuzzyVariable("Temperature", 450f, 600f, "C");
            fuzzyPres = new FuzzyVariable("Pressure", 135f, 165f, "A");
            //Create the output fuzzy variabales
            fuzzyValveTemp = new FuzzyVariable("Valve Temperature", 0, 100);
            fuzzyValvePres = new FuzzyVariable("Valve Pressure", 0, 100);


            /************************************************************/
            /** Definición de la base de datos. Se puede cambiar este ***/
            /** código para la segunda parte de la práctica *************/
            /************************************************************/

            //Create the fuzzy sets and add them to the fuzzy variables
            fuzzyTemp.addTerm("Low", new TriangleFuzzySet(450,450,525));
            fuzzyTemp.addTerm("Medium", new TriangleFuzzySet(450,525,600));
            fuzzyTemp.addTerm("High", new TriangleFuzzySet(525,600,600));

            fuzzyPres.addTerm("Low", new TriangleFuzzySet(135,135,150));
            fuzzyPres.addTerm("Medium", new TriangleFuzzySet(135,150,165));
            fuzzyPres.addTerm("High", new TriangleFuzzySet(150,165,165));



            //Create the fuzzy sets and add them to the fuzzy variables
            fuzzyValveTemp.addTerm("VeryLow", new TriangleFuzzySet(0,0,25));
            fuzzyValveTemp.addTerm("Low", new TriangleFuzzySet(0,25,50));
            fuzzyValveTemp.addTerm("Medium", new TriangleFuzzySet(25,50,75));
            fuzzyValveTemp.addTerm("High", new TriangleFuzzySet(50,75,100));
            fuzzyValveTemp.addTerm("VeryHigh", new TriangleFuzzySet(75,100,100));

            fuzzyValvePres.addTerm("VeryLow", new TriangleFuzzySet(0,0,25));
            fuzzyValvePres.addTerm("Low", new TriangleFuzzySet(0,25,50));
            fuzzyValvePres.addTerm("Medium", new TriangleFuzzySet(25,50,75));
            fuzzyValvePres.addTerm("High", new TriangleFuzzySet(50,75,100));
            fuzzyValvePres.addTerm("VeryHigh", new TriangleFuzzySet(75,100,100));


        } catch (InvalidFuzzyVariableNameException e) {
            e.printStackTrace();
        } catch (InvalidUODRangeException e) {
            e.printStackTrace();
        } catch (XValuesOutOfOrderException e) {
            e.printStackTrace();
        } catch (XValueOutsideUODException e) {
            e.printStackTrace();
        } catch (InvalidFuzzyVariableTermNameException e) {
            e.printStackTrace();
        }
    }
}
