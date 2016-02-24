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
        /********************************************************/
        /** COMPLETAR CÓDIGO: INFERENCIA ************************/
        /********************************************************/
        /** NOTA: La variable antecedents es un HashSet que se **/
        /** utiliza para saber si un Conjunto difuso dentro de **/
        /** la variable en que se ha definido pertenece a los ***/
        /** antecedentes de la regla o no. El nombre de la ******/
        /** variable en la que se define un Valor difuso se *****/
        /** saber mediante: varFuzzyValue.getFuzzyVariable().getName() **/
        /********************************************************/

        return null;
    }
}
