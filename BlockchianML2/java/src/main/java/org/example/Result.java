package org.example;

import com.alibaba.fastjson2.JSON;
import moa.evaluation.preview.LearningCurve;

import java.time.ZonedDateTime;

public class Result {
    private String currentKeyOnChain;
    private String currentValueOnChain;
    private int correctNumber;
    private int totalNumber;
    private LearningCurve learningCurve;
    private long evaluateStartTime;
    private long lastEvaluateStartTime;
    private double RAMHours;
    private boolean preciseCPUTiming;
    private boolean firstDump;
    private double versionNumber;
    private double[] prediction;
    private ZonedDateTime dateTime;

    public int getCorrectNumber() {
        return correctNumber;
    }

    public void setCorrectNumber(int correctNumber) {
        this.correctNumber = correctNumber;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public LearningCurve getLearningCurve() {
        return learningCurve;
    }

    public void setLearningCurve(LearningCurve learningCurve) {
        this.learningCurve = learningCurve;
    }

    public long getEvaluateStartTime() {
        return evaluateStartTime;
    }

    public void setEvaluateStartTime(long evaluateStartTime) {
        this.evaluateStartTime = evaluateStartTime;
    }

    public long getLastEvaluateStartTime() {
        return lastEvaluateStartTime;
    }

    public void setLastEvaluateStartTime(long lastEvaluateStartTime) {
        this.lastEvaluateStartTime = lastEvaluateStartTime;
    }

    public double getRAMHours() {
        return RAMHours;
    }

    public void setRAMHours(double RAMHours) {
        this.RAMHours = RAMHours;
    }

    public boolean getPreciseCPUTiming() {
        return preciseCPUTiming;
    }

    public void setPreciseCPUTiming(boolean preciseCPUTiming) {
        this.preciseCPUTiming = preciseCPUTiming;
    }

    public boolean isFirstDump() {
        return firstDump;
    }

    public void setFirstDump(boolean firstDump) {
        this.firstDump = firstDump;
    }

    public double getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(double versionNumber) {
        this.versionNumber = versionNumber;
    }

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }
    public String getCurrentKeyOnChain() {
        return currentKeyOnChain;
    }

    public void setCurrentKeyOnChain(String currentKeyOnChain) {
        this.currentKeyOnChain = currentKeyOnChain;
    }

    public String getCurrentValueOnChain() {
        return currentValueOnChain;
    }

    public void setCurrentValueOnChain(String currentValueOnChain) {
        this.currentValueOnChain = currentValueOnChain;
    }
    public double[] getPrediction() {
        return prediction;
    }

    public void setPrediction(double[] prediction) {
        this.prediction = prediction;
    }

    public String toJSONString() {

        return  JSON.toJSONString(this);
    }

    public static Result fromJSONString(String json) {
        return JSON.parseObject(json, Result.class);
    }

}
