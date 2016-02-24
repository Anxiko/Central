package frbs;

import nrc.fuzzy.*;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by jcozar on 26/01/16.
 */
public class FRBS {

    //This class is in charge of the valve control
    private int valveTemp, valvePres;
    //These are the database and the rulebase of the FRBS
    private Database db;
    private Rulebase rb;

    //Variable used to print the inference process on the window
    private ArrayList<String> lastInferenceProcess;

    public ArrayList<String> getLastInferenceProcess(){
        return lastInferenceProcess;
    }

    public FRBS(){
        //Set the initial value for each valve
        valveTemp=50;
        valvePres=50;
        db = new Database();
        rb = new Rulebase(db);
        lastInferenceProcess = new ArrayList<String>();
    }

    public int getValveTemperature(){
        return valveTemp;
    }

    public int getValvePressure(){
        return valvePres;
    }

    public void control(float temperature, float pressure){
        //Use Singleto Fuzzification Interface
        try {
            FuzzyValue inputTemp = new FuzzyValue(db.getVariable(Database.TEMPERATURE), new SingletonFuzzySet(temperature));
            FuzzyValue inputPres = new FuzzyValue(db.getVariable(Database.PRESSURE), new SingletonFuzzySet(pressure));
            FuzzyValue[] fuzzy_input = new FuzzyValue[]{inputTemp, inputPres};

            try {
                lastInferenceProcess.clear();
                lastInferenceProcess.add("<h3>Values for input variables: temperature = "+temperature+" and pressure = "+pressure+"</h3>");
                lastInferenceProcess.add("<h2>Inference process for output Valve Temperature:</h2>");
                double predictionValveTemperature = FITA(rb.getRulesForValveTemperature(), fuzzy_input);
                lastInferenceProcess.add("<h2>Inference process for output Valve Pressure:</h2>");
                double predictionValvePressure = FITA(rb.getRulesForValvePressure(), fuzzy_input);
                valveTemp = Double.isNaN(predictionValveTemperature)?50:(int) Math.round(predictionValveTemperature);
                valvePres = Double.isNaN(predictionValvePressure)?50:(int) Math.round(predictionValvePressure);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (XValueOutsideUODException e) {
            e.printStackTrace();
        } catch (XValuesOutOfOrderException e) {
            e.printStackTrace();
        }
    }

    //We use FITA inference process
    private double FITA(ArrayList<FuzzyRule> rules, FuzzyValue[] fuzzy_input) throws XValuesOutOfOrderException, InvalidDefuzzifyException, IncompatibleRuleInputsException {
        //Some variables for statistics
        double ruleOutputVal;
        double fireLevel;
        double globalOutput = 0;
        double sumFireLevels = 0;
        int numFiredRules=0;

        DecimalFormat formatter = new DecimalFormat("#.00");

        FuzzyValue ruleOutput;
        //If rules is null, no rule can be fired so it returns a NaN
        if (rules==null){
            return Double.NaN;
        }

        for (int i=0;i<rules.size();i++){
            // If any rule is null, it continues
            if (rules.get(i)==null){
                System.out.println("ERROR: Attempt to use a null object as a FuzzyRule.");
                continue;
            }

            //Inference
            ruleOutput = rules.get(i).fireRule(fuzzy_input);
            // If ruleOutput is not null, we know it as a valid output
            if (ruleOutput!=null && ruleOutput.getMaxY()>0){
                fireLevel = ruleOutput.getMaxY(); // The cut level
                ruleOutputVal = ruleOutput.momentDefuzzify(); //Defuzzify
                globalOutput+= ruleOutputVal*fireLevel;
                sumFireLevels+=fireLevel;
                numFiredRules++;
                lastInferenceProcess.add(rules.get(i).toString()+" <i>(fire level: "+formatter.format(fireLevel)+"; rule output: "+formatter.format(ruleOutputVal)+")</i><br>");
            }
        }

        // If no rules has been fired, it returns a NaN
        if (numFiredRules==0)
            return Double.NaN;
        lastInferenceProcess.add("<h3>Global output: "+formatter.format(globalOutput/sumFireLevels)+"</h3>");
        // Else, it returns the average of all the individual
        return globalOutput/sumFireLevels;
    }
}
