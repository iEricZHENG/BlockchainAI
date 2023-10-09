/*
 * SPDX-License-Identifier: Apache-2.0
 */
package org.example;

import moa.core.InstanceExample;

import moa.core.Utils;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.shim.ChaincodeStub;

import java.util.Random;

@Contract(name = "DataContract",
        info = @Info(title = "Data contract",
                description = "My Smart org.example.Contract",
                version = "0.0.1",
                license =
                @License(name = "Apache-2.0",
                        url = ""),
                contact = @Contact(email = "BlockchianML@example.com",
                        name = "BlockchianML",
                        url = "http://BlockchianML.me")))
@Default
public class DataContract implements ContractInterface {

    @Override
    public Context createContext(ChaincodeStub stub) {
        return new AIContext(stub);
    }

    public DataContract() {
    }

    @Transaction()
    public String start(AIContext ctx, String instanceString) {
        Result result = null;
        boolean isChecked = AIContext.Model.securityLayerCheck(instanceString);//
        if (isChecked) {
            InstanceExample instance = AIContext.Model.adaptLayerAdapt(instanceString);
            result = AIContext.Model.algoModelLayerRun(instance);
            AIContext.Model.feedbackLayerOut(result);
            AIContext.Model.outputLayerOnChain(ctx, instanceString, result);
        }
//        String value = buildValue(instanceString, result);
//        ctx.getStub().putStringState(result.getCurrentKeyOnChain(), value);
        return "ok";
    }


    @Transaction()
    public String startNotOnChain(AIContext ctx, String instanceString) {
        Result result = null;
        boolean isChecked = AIContext.Model.securityLayerCheck(instanceString);//
        if (isChecked) {
            InstanceExample instance = AIContext.Model.adaptLayerAdapt(instanceString);
            result = AIContext.Model.algoModelLayerRun(instance);
            AIContext.Model.feedbackLayerOut(result);
            AIContext.Model.outputLayerNotOnChain(result);
        }
        //String value = buildValue(instanceString, result);
        //ctx.getStub().putStringState(result.getCurrentKeyOnChain(), value);
        return "ok";
    }

    @Transaction()
    public String ping(AIContext ctx) {
        Random random = new Random();
        int r = random.nextInt(100);
        System.out.println("===>ping-" + r + "<===");
        return "ping";
    }

    @Transaction()
    public String getStatus(AIContext ctx) {
        return "===ping===" + AIContext.Model.controllerLayerGetStatus();
    }

    @Transaction()
    public void setTrainStatus(AIContext ctx) {
        AIContext.Model.controllerLayerSetStatus(Status.Train);
    }

    @Transaction()
    public void setWorkStatus(AIContext ctx) {
        AIContext.Model.controllerLayerSetStatus(Status.Apply);
    }

}
