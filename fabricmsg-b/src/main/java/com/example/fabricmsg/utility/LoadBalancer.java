package com.example.fabricmsg.utility;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LoadBalancer {
    private List<ContractConfig> backendServers;

    public LoadBalancer() {
        backendServers = new ArrayList<>();

        ContractConfig contractConfig_1 = new ContractConfig();
        contractConfig_1.setNetworkConfigPath(Paths.get("/home/eric/mywork", "connection.json"));
        contractConfig_1.setCredentialPath(Paths.get("/home/eric/mywork/vars", "keyfiles", "peerOrganizations", "org1.example.com", "users", "Admin@org1.example.com", "msp"));
        contractConfig_1.setChannel("mychannel");
        contractConfig_1.setUserPem("Admin@org1.example.com-cert.pem");
        contractConfig_1.setX509Id("org1-example-com");
        //contractConfig_1.setContractName("BlockchianML2");
        contractConfig_1.setContractName("BlockchianML4");

//        ContractConfig contractConfig_2 = new ContractConfig();
//        contractConfig_2.setNetworkConfigPath(Paths.get("/home/eric/mywork", "connection.json"));
//        contractConfig_2.setCredentialPath(Paths.get("/home/eric/mywork/vars", "keyfiles", "peerOrganizations", "org0.example.com", "users", "Admin@org0.example.com", "msp"));
//        contractConfig_2.setChannel("mychannel");
//        contractConfig_2.setUserPem("Admin@org0.example.com-cert.pem");
//        contractConfig_2.setX509Id("org0-example-com");
//        contractConfig_2.setContractName("BlockchianML3");
        backendServers.add(contractConfig_1);
        //backendServers.add(contractConfig_2);
    }

    public ContractConfig getBackendServer() {
        int randomIndex = ThreadLocalRandom.current().nextInt(backendServers.size());
        return backendServers.get(randomIndex);
    }
}
