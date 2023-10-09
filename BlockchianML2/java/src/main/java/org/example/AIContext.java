package org.example;

import moa.core.TimingUtils;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;

public class AIContext extends Context {
    /**
     * Creates new client identity and sets it as a property of the stub.
     *
     * @param stub Instance of the {@link ChaincodeStub} to use
     */
    public static org.example.Model Model;
    public static boolean IsInit =true;
    public AIContext(ChaincodeStub stub) {
        super(stub);
        if(IsInit){
            System.out.println("===TestContext Init===");
            Model = new Model();
            org.example.Model.result.setFirstDump(true);
            org.example.Model.result.setRAMHours(0.0);
            org.example.Model.result.setPreciseCPUTiming(TimingUtils.enablePreciseTiming());
            org.example.Model.result.setEvaluateStartTime(TimingUtils.getNanoCPUTimeOfCurrentThread());
            org.example.Model.result.setLastEvaluateStartTime(org.example.Model.result.getEvaluateStartTime());
            Model.controllerLayerSetStatus(Status.Train);
            IsInit =false;
        }
    }
}
