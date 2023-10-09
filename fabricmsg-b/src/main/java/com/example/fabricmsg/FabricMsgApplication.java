package com.example.fabricmsg;

import com.example.fabricmsg.controller.Sensor;

import com.example.fabricmsg.utility.ContractHelper;
import moa.core.InstanceExample;
import org.hyperledger.fabric.gateway.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeoutException;

@SpringBootApplication
public class FabricMsgApplication {
  
    public static void main(String[] args) throws InterruptedException, CertificateException, IOException, InvalidKeyException, ContractException, TimeoutException {
        //StartNotOnChain();
        //Start();
        SpringApplication.run(FabricMsgApplication.class, args);
    }
    public static void StartNotOnChain() throws InterruptedException, CertificateException, IOException, InvalidKeyException {
        Contract contract = ContractHelper.getContract();
        Sensor sensor = new Sensor();
        int skip = 8532;
        for (int i = 0; i < 86995; i++) {
            try {
                InstanceExample instance = sensor.Post();
                if (i < skip) continue;
                String instanceStr = instance.toString();
                instanceStr = instanceStr.substring(0, instanceStr.length() - 1);

                Transaction tx = contract.createTransaction("startNotOnChain");
                byte[] result = tx.evaluate(instanceStr);
                //byte[] result = tx.submit(instanceStr);
                skip = i + 1;
                String resultStr = new String(result, StandardCharsets.UTF_8);
                //Result resultObj = Result.fromJSONString(resultStr);
                //System.out.println("===num:" + "===" + resultObj.getTotalNumber());
                System.out.println("info txid:" + tx.getTransactionId());
                System.out.println("result:" + resultStr);
                Thread.sleep(30);
            } catch (Exception e) {
                e.printStackTrace();
                Thread.sleep(2 * 1000);
            }
        }
    }
    public static void Start() throws ContractException, CertificateException, IOException, InvalidKeyException, InterruptedException, TimeoutException {
        Sensor sensor = new Sensor();
        Contract contract = ContractHelper.getContract();
        int skip = 0;
        for (int i = 0; i < 86995; i++) {
            try {
                InstanceExample instance = sensor.Post();
                if (i < skip) continue;
                String instanceStr = instance.toString();
                instanceStr = instanceStr.substring(0, instanceStr.length() - 1);

                Transaction tx = contract.createTransaction("start");
                //byte[] result = tx.evaluate();
                skip = i + 1;
                byte[] result = tx.submit(instanceStr);
                String resultStr = new String(result, StandardCharsets.UTF_8);
                //Result resultObj = Result.fromJSONString(resultStr);
                //System.out.println("===num:" + "===" + resultObj.getTotalNumber());
                System.out.println("info txid:" + tx.getTransactionId());
                System.out.println("result:" + skip + resultStr);
            } catch (Exception e) {
                e.printStackTrace();
                Thread.sleep(2 * 1000);
            }
        }
    }
}
