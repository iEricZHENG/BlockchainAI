package com.example.fabricmsg.utility;

import org.hyperledger.fabric.gateway.*;
import org.hyperledger.fabric.gateway.impl.GatewayImpl;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import static com.example.fabricmsg.utility.Helper.getPrivateKey;
import static com.example.fabricmsg.utility.Helper.readX509Certificate;

public class ContractHelper {

    public static Contract getContract() throws IOException, CertificateException, InvalidKeyException {
        LoadBalancer loadBalancer = new LoadBalancer();
        ContractConfig contractConfig =loadBalancer.getBackendServer();
        System.out.println("==="+ contractConfig.getX509Id()+"===");

        PrivateKey privateKey;
        Gateway gateway;
        X509Certificate certificate;

        Wallet wallet = Wallets.newInMemoryWallet();

        Path certificatePath = contractConfig.getCredentialPath().resolve(Paths.get("signcerts", contractConfig.getUserPem()));
        certificate = readX509Certificate(certificatePath);

        Path privateKeyPath = contractConfig.getCredentialPath().resolve(Paths.get("keystore", "priv_sk"));
        privateKey = getPrivateKey(privateKeyPath);

        wallet.put("Admin", Identities.newX509Identity(contractConfig.getX509Id(), certificate, privateKey));
        
        GatewayImpl.Builder builder = (GatewayImpl.Builder) Gateway.createBuilder();
        builder.identity(wallet, "Admin").networkConfig(contractConfig.getNetworkConfigPath());
        //get gateway
        gateway = builder.connect();
        //getChannel
        Network network = gateway.getNetwork(contractConfig.getChannel());
        //getContract
        Contract contract = network.getContract(contractConfig.getContractName());
        return contract;
    }
}
