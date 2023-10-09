package com.example.fabricmsg.utility;

import java.nio.file.Path;

public class ContractConfig {
    private Path networkConfigPath ;
    private Path credentialPath ;
    private String userPem;
    private String channel ;
    private String x509Id;
    private String contractName;

    public String getUserPem() {
        return userPem;
    }

    public void setUserPem(String userPem) {
        this.userPem = userPem;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getX509Id() {
        return x509Id;
    }

    public void setX509Id(String x509Id) {
        this.x509Id = x509Id;
    }

    public Path getCredentialPath() {
        return credentialPath;
    }

    public void setCredentialPath(Path credentialPath) {
        this.credentialPath = credentialPath;
    }

    public Path getNetworkConfigPath() {
        return networkConfigPath;
    }

    public void setNetworkConfigPath(Path networkConfigPath) {
        this.networkConfigPath = networkConfigPath;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }
}
