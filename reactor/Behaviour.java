package reactor;

import java.util.Random;

/**
 * Created by jcozar on 26/01/16.
 */
public class Behaviour {

    public final static int STATUS1=1;
    public final static int STATUS2=2;

    private Random r;

    private int status;

    private float temp, pres;

    private float valveTempStep, valvePresStep;//How strong is the change with the change of the valves
    private float tempStep, presStep;//How much one measure affects to the other

    public Behaviour(Random r){
        this.r = r;
        temp = r.nextFloat()*(600f-450f)+450f;
        pres = r.nextFloat()*(165f-135f)+135f;
        changeStatus(STATUS1);
    }

    public float getTemperature(){
        return temp;
    }

    public float getPressure(){
        return pres;
    }

    public int getStatus(){ return status;}

    public void changeStatus(int status){
        this.status=status;
        switch (status){
            case 2:
                valveTempStep = 250;
                valvePresStep = 250;
                tempStep = 1f;
                presStep = 1f;
                System.out.println("Modo = 2");
                break;
            default://1
                valveTempStep = 250;
                valvePresStep = 250;
                tempStep = 10;
                presStep = 10;
                System.out.println("Modo = 1");
                break;

        }
    }

    public void update(int tempValve, int presValve) {
        //Depending on the status (which affects to the parameters valveTemp,
        //valvePres, tempStep and presStep) and the valves, it changes the values
        //of temperature and pressure
        float newTemp, newPres;

        float Vvt,Vvp; //Variation produced by the valve temperature or pressure
        float Vt,Vp; //Variation produced by the previous temperature or pressure
        float rangeTemp = 600-450;
        float rangePres = 165-135;
        float meanTemp = 450 + rangeTemp/2;
        float meanPres = 135 + rangePres/2;

        Vvt = (tempValve-50)*rangeTemp/valveTempStep;
        Vvt = Math.abs(Vvt)*(float)Math.sqrt(Math.abs(Vvt))*Math.signum(Vvt);
        Vvp = (presValve-50)*rangePres/valvePresStep;
        Vvp = Math.abs(Vvp)*(float)Math.sqrt(Math.abs(Vvp))*Math.signum(Vvp);

        Vt = (temp-meanTemp)/rangeTemp * rangePres / presStep;
        Vp = (pres-meanPres)/rangeTemp * rangeTemp / tempStep;

        //Obtain the new value for temperature
        newTemp = temp + Vvt + Vp;// + (r.nextFloat()-0.5f)*(600f-450f)/10;
        boolean randomTemp = r.nextFloat()>0.85;
        if(randomTemp)
            newTemp = r.nextFloat()*(600f-450f) + 450f;
        //And ensure it is in the defined range of values
        newTemp = newTemp > 600f ? 600f : newTemp;
        newTemp = newTemp < 450f ? 450f : newTemp;

        //Obtain the new value for pressure
        newPres = pres + Vvp + Vt;// + (r.nextFloat()-0.5f)*(165f-135f)/10;
        boolean randomPres = r.nextFloat()>0.85;
        if(randomPres)
            newPres = r.nextFloat()*(165-135) + 135;
        //And ensure it is in the defined range of values
        newPres = newPres > 165f ? 165f : newPres;
        newPres = newPres < 135f ? 135f : newPres;

        if(randomTemp)
            System.out.println("Random temperature!: " + newTemp);
        else
            System.out.println("Previous temperature value: "+temp+"\n\tValue of the temperature valve: "+tempValve+
                "\n\tChange by valve: "+Vvt+ "\n\tChange by pressure: "+Vp+"\n\tNew value for temperature: "+newTemp);

        if(randomPres)
            System.out.println("Random pressure!: "+ newPres);
        else
            System.out.println("Previous pressure value: "+pres+"\n\tValue of the pressure valve: "+presValve +
                "\n\tChange by valve: "+Vvp+ "\n\tChange by temperature: "+Vt+"\n\tNew value for pressure: "+newPres);

        temp = newTemp;
        pres = newPres;
    }
}
