package com.example.fabricmsg;

import org.hyperledger.fabric.gateway.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeoutException;

@SpringBootApplication
public class FabricMsgApplication {
  
    public static void main(String[] args) throws InterruptedException, CertificateException, IOException, InvalidKeyException, ContractException, TimeoutException {
        SpringApplication.run(FabricMsgApplication.class, args);
    }   
}
