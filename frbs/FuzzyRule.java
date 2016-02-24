package frbs;

import nrc.fuzzy.*;

import java.util.HashSet;

/**
 * Created by jcozar on 27/01/16.
 */
public class FuzzyRule extends nrc.fuzzy.FuzzyRule{

    //We use this variable just to know the variable in the antecedent
    HashSet<String> antecedents;

    public FuzzyRule(FuzzyRuleExecutor executor){
        super(executor);
        antecedents = new HashSet<String>();
    }

    public String toString(){
        String text = "If ";

        FuzzyValueVector antecedents = getAntecedents();
        for(int i = 0; i<antecedents.size();i++){
            FuzzyValue fv = antecedents.fuzzyValueAt(i);
            String varName = fv.getFuzzyVariable().getName();
            String label = fv.getLinguisticExpression();
            text += "<b>"+varName + "</b> is <b>" + label + "</b> ";
            if(i<antecedents.size()-1){
                text += "and ";
            }
        }
        text += "then ";
        FuzzyValueVector conclusions = getConclusions();
        for(int i = 0; i<conclusions.size();i++){
            FuzzyValue fv = conclusions.fuzzyValueAt(i);
            String varName = fv.getFuzzyVariable().getName();
            String label = fv.getLinguisticExpression();
            text += "<b>"+varName + "</b> is <b>" + label + "</b> ";
            if(i<conclusions.size()-1){
                text += "and ";
            }
        }

        return text;
    }

    @Override
    public void addAntecedent(FuzzyValue value){
        super.addAntecedent(value);
        antecedents.add(value.getFuzzyVariable().getName());
    }

    @Override
    public void addConclusion(FuzzyValue value){
        super.addConclusion(value);
    }

    public FuzzyValue fireRule(FuzzyValue inputs[]) throws IncompatibleRuleInputsException {
        // Delete all the inputs added in previous inferences
        removeAllInputs();
        // Add new inputs
        for (FuzzyValue input : inputs){
            if(antecedents.contains(input.getFuzzyVariable().getName()))
                addInput(input);
        }


        // If there is a match, make the inference
        if (testRuleMatching()){
            FuzzyValue value =  new FuzzyValue(execute().fuzzyValueAt(0)); //Center of mass
            try{
                value.momentDefuzzify();
                return value;
            }
            catch(Exception e){
                try {
                    value.momentDefuzzify();
                    return value;
                } catch (Exception e1) {
                    e1.printStackTrace();
                    return null;
                }
            }
        }
        // If there is not a match, we return null
        else
            return null;

    }
}
