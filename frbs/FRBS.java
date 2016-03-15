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
        /**Completar Código*/

        /** INFORMACIÓN ÚTIL PARA COMPLETAR ESTA FUNCIÓN**/

        /** 1) AL DISPARAR UNA REGLA SE OBTIENE UN FuzzyValue OBJECT.
         * SE DEBE UTILIZAR LA FUNCIÓN "getMaxY()" PARA OBTENER
         * EL NIVEL DE CORTE
         * SE DEBE UTILZIAR LA FUNCIÓN "momentDefuzzify()" PARA
         * OBTER EL VALOR REAL RESULTANTE DE LA DEFUZZIFICAZIÓN**/
        /**
         * 2) LA VARIABLE "lastInferenceProcess" ES UN ARRAY DE STRINGS QUE
         * CONTIENE LÍNEAS DE CÓDIGO HTML. ESTA INFOTMACIÓN SERÁ IMPRIMIDA
         * EN LA INTERFAZ COMO AYUDA SOBRE EL PROCESO DE INFERENCIA.
         * ==================================================================
         * PARA QUE MUESTRE QUÉ REGLAS SE HAN DISPARADO, HAY QUE INCLUIR EL
         * SIGUIENTE CÓDIGO (¡SOLO PARA AQUELLAS REGLAS QUE SE DISPARAN!):
         * lastInferenceProcess.add(rules.get(i).toString();
         *
         * SI ADEMÁS QUERÉIS QUE SE MUESTRE EL NIVEL DE CORTE Y LA SALIDA INDIVIDUAL
         * DE ESTAS REGLAS HAY QUE INCLUIR EL SIGUIENTE CÓDIGO. COMO ESTOS VALORES
         * SON NÚMEROS REALES, SE UTILIZA UN DecimalFormatter PARA IMPRIMIR LOS NÚMEROS
         * CON UNA PRECISIÓN DE DOS DECIMALES. HAY QUE CREAR PARA ELLO UN OBJETO DE ESTE TIPO
         * DecimalFormat formatter = new DecimalFormat("#.00");. EL CÓDIGO QUEDARÍA ASÍ:
         * lastInferenceProcess.add(rules.get(i).toString()+
         * " <i>(fire level: "+formatter.format(fireLevel)+
         * "; rule output: "+formatter.format(ruleOutputVal)+")</i><br>");
         *
         * PARA ADEMAS AÑADIR LA INFORMACIÓN SOBRE LA SALIDA GLOBAL DEL SISTEMA,
         * ANTES DEL RETURN DE LA FUNCIÓN PONER EL SIGUIENTE CÓDIGO (REEMPLAZAR
         * salidaGlobal POR LA EXPRESIÓN/VARIABLE CORRESPONDIENTE):
         * lastInferenceProcess.add("<h3>Global output: "+
         * formatter.format(salidaGlobal+"</h3>");
         */
        
        //Si no hay reglas, devuelve NaN
        if (rules==null)
            return Double.NaN;
        
        DecimalFormat formatter = new DecimalFormat("#.00");//Formateador
        double pesoTotal=0.0;//Peso de todas las reglas disparadas
        double valorTotal=0.0;//Valor total de la regla
        try
        {
            for (FuzzyRule rule : rules)//Dispara cada una de las reglas
            {
                if (rule!=null)
                {
                    FuzzyValue conj_dif=rule.fireRule(fuzzy_input);//Dispara la regla y obten el conjunto difuso resultante
                    if (conj_dif!=null&&conj_dif.getMaxY()>0)//Si la salida es mayor a cero, considerala
                    {
                        double peso=conj_dif.getMaxY();//Peso de salida de la regla
                        double valor=conj_dif.momentDefuzzify();//Valor de salida de la válvula
                        valorTotal+=peso*valor;//Añade la salida al total
                        pesoTotal+=peso;//Añade el peso al total
                        lastInferenceProcess.add(rule.toString()+" <i>(Peso: "+formatter.format(peso)+"; valor de la válvula: "+formatter.format(valor)+")</i><br>");
                    }
                }
            }
        }
        catch (NullPointerException ne)
        {
            System.out.println("Error! Desreferenciado puntero a null, "+ne);
        }
        
        if (pesoTotal==0)//Si no se ha disparado ninguna regla
            return Double.NaN;
        return valorTotal/pesoTotal;//Devuelve la media ponderada
    }
}
