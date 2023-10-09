package com.example.fabricmsg.controller;

import com.example.fabricmsg.utility.ContractHelper;
import moa.core.InstanceExample;
import org.hyperledger.fabric.gateway.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@RestController
public class IndexController {
    


    @RequestMapping("/ping")
    public String Ping() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
        return "ping ok " + date;
    }

    @RequestMapping("/getStatus")
    public void GetStatus() {
        try {
            Contract contract = ContractHelper.getContract();
            byte[] result = contract.evaluateTransaction("getStatus");
            System.out.println("result of contract：" + new String(result, StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/setWorkStatus")
    public void SetWorkStatus() {
        try {
            Contract contract = ContractHelper.getContract();
            contract.submitTransaction("setWorkStatus");
            System.out.println("end");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @RequestMapping("/setTestStatus")
    public void SetTestStatus() {
        try {
            Contract contract = ContractHelper.getContract();
            contract.submitTransaction("setTestStatus");
            System.out.println("end");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/work")
    public void Work() throws InterruptedException {
        Sensor sensor = new Sensor();
        int skip = 70000;
        for (int i = 0; i < 80000; i++) {
            try {
                InstanceExample instance = sensor.Post();
                if (i < skip) continue;
                String instanceStr = instance.toString();
                instanceStr = instanceStr.substring(0, instanceStr.length() - 3);

                Contract contract = ContractHelper.getContract();
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

    @PostMapping("/add")
    public String Add(@RequestBody PostRequestData postData) throws CertificateException, IOException, InvalidKeyException, ContractException, InterruptedException, TimeoutException {
        System.out.println(postData.valueStr);
        //
        Contract contract = ContractHelper.getContract();
        UUID uuid = UUID.randomUUID();

        Transaction tx = contract.createTransaction("createMessage");
        byte[] createMsgResult = tx.submit(String.valueOf(uuid), postData.valueStr);
        System.out.println("result" + new String(createMsgResult, StandardCharsets.UTF_8));
        System.out.println("info:" + tx.getTransactionId());
        return tx.getTransactionId();
    }


    @RequestMapping("/start")
    public void Start() throws ContractException, CertificateException, IOException, InvalidKeyException, InterruptedException, TimeoutException {
        Sensor sensor = new Sensor();
        Contract contract = ContractHelper.getContract();
        int skip = 0;
        for (int i = 0; i < 86994; i++) {//86994
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
                Thread.sleep(30);
            } catch (Exception e) {
                e.printStackTrace();
                Thread.sleep(2 * 1000);
            }
        }
    }


    @RequestMapping("/off")
    public void StartNotOnChain() throws InterruptedException, CertificateException, IOException, InvalidKeyException {
        Sensor sensor = new Sensor();
        Contract contract = ContractHelper.getContract();
        int skip = 0;
        for (int i = 0; i < 86994; i++) {//86994
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
                System.out.println("result:" +i+ resultStr);
                Thread.sleep(30);
            } catch (Exception e) {
                e.printStackTrace();
                Thread.sleep(2 * 1000);
            }
        }
    }
    @RequestMapping("/contract/ping")
    public void PingContract() throws InterruptedException, CertificateException, IOException, InvalidKeyException, ContractException, TimeoutException {
        Contract contract = ContractHelper.getContract();
        Transaction tx = contract.createTransaction("ping");
        byte[] result = tx.evaluate();

        String resultStr = new String(result, StandardCharsets.UTF_8);
        System.out.println("result:" + resultStr);
    }

}
