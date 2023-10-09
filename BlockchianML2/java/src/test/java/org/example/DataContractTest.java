/*
 * SPDX-License-Identifier: Apache License 2.0
 */

package org.example;

import moa.core.InstanceExample;
import moa.core.TimingUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;


public final class DataContractTest {
//    @Train
//    void start2() {
//        DataContract contract = new DataContract();
//        TestContext ctx = mock(TestContext.class);
//        ChaincodeStub stub = mock(ChaincodeStub.class);
//        contract.createContext(stub);
//        when(ctx.getStub()).thenReturn(stub);
//
//
//        Sensor sensor = new Sensor();
//
//        for (int i = 0; i < 10; i++) {
//            InstanceExample instance = sensor.Post();
//            String instanceStr = instance.toString();
//
//            String result = contract.start2(ctx, instanceStr);
//            Result r= Result.fromJSONString(result);
//            System.out.println(r.getTotalNumber()+"===" + r.getTotalNumber() + "===");
//            assertNotNull(r);
//        }
//    }
//
@Test
void use() throws Exception {
    //learner.model
    //Classifier learner = (ROSE) SerializationHelper.read("learner.model");
    Model model = new Model();
    Sensor sensor = new Sensor();
    Model.status = Status.Train;
    Model.result.setFirstDump(true);
    Model.result.setRAMHours(0.0);
    Model.result.setPreciseCPUTiming(TimingUtils.enablePreciseTiming());
    Model.result.setEvaluateStartTime(TimingUtils.getNanoCPUTimeOfCurrentThread());
    Model.result.setLastEvaluateStartTime(org.example.Model.result.getEvaluateStartTime());

    Result result = null;
    int skip = 0;
    for (int i = 0; i < 86994; i++) {
        try {
            InstanceExample instance = sensor.Post();
            if (i < skip) continue;

            String instanceStr = instance.toString();
            instanceStr = instanceStr.substring(0, instanceStr.length() - 1);
            boolean isChecked= model.securityLayerCheck(instanceStr);
            if(isChecked){
                InstanceExample instanceExample = model.adaptLayerAdapt(instanceStr);
                result = model.algoModelLayerRun(instance);
                model.feedbackLayerOut(result);
                result = model.outputLayerNotOnChain(result);
                System.out.println("==="+result.getTotalNumber());
                System.out.println("---^^^^^^^^^^---");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Thread.sleep(2 * 1000);
        }
    }

    System.out.println("===");
}

    @Test
    void ping() {
        DataContract contract = new DataContract();
        AIContext ctx = mock(AIContext.class);

        String r=contract.ping(ctx);
        assertEquals("ping",r);
    }
}
