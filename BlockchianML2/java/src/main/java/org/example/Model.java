package org.example;

import com.yahoo.labs.samoa.instances.*;
import moa.core.*;
import moa.evaluation.BasicClassificationPerformanceEvaluator;
import org.example.rose.classifiers.meta.imbalanced.ROSE;
import org.example.rose.evaluation.WindowAUCMultiClassImbalancedPerformanceEvaluator;
import org.example.rose.evaluation.WindowImbalancedClassificationPerformanceEvaluator;
import moa.classifiers.Classifier;
import moa.evaluation.LearningEvaluation;
import moa.evaluation.LearningPerformanceEvaluator;
import moa.evaluation.preview.LearningCurve;
import org.apache.commons.csv.CSVFormat;
import weka.classifiers.meta.Bagging;
import weka.core.SerializationHelper;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.csv.CSVPrinter;

public class Model {
    public static Status status;
    public static Classifier learner;
    public static Result result;
    public static DriftDetection driftDetect;
    private static LearningPerformanceEvaluator<Example<Instance>> evaluator;

    public Model() {
        InstancesHeader header = buildHeader();
        //learner = reloadPersistModel();
        //if (learner == null) learner = algoModelLayerCreateLearner(header);
        learner = algoModelLayerCreateLearner(header);
        result = algoModelCreateResult();
        driftDetect = new DriftDetection(header);
        //evaluator = new WindowAUCMultiClassImbalancedPerformanceEvaluator();
        evaluator = new WindowImbalancedClassificationPerformanceEvaluator();
        System.out.println("===Learner Class Init===");
    }

    private Classifier reloadPersistModel() {
        String filename = "learner.model";
        String currentPath = System.getProperty("user.dir");
        String fileString = currentPath + "/" + filename;
        File file = new File(fileString);
        if (!file.exists()) return null;
        try {
            Classifier learner = (ROSE) SerializationHelper.read(fileString);
            return learner;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Result algoModelCreateResult() {
        Result result = new Result();
        //result.setCorrectNumber(0);
        //result.setTotalNumber(0);
        result.setLearningCurve(new LearningCurve("learning evaluation instances"));
        return result;
    }

    private Classifier algoModelLayerCreateLearner(InstancesHeader header) {
        //Classifier learner = new HoeffdingTree();
        Classifier learner = new ROSE();
        learner.setModelContext(header);
        learner.prepareForUse();
        return learner;
    }

    public InstancesHeader buildHeader() {
        ArrayList<Attribute> attributes = new ArrayList<>();

        Attribute attribute0 = new Attribute("X");
        Attribute attribute1 = new Attribute("Y");
        Attribute attribute2 = new Attribute("Z");

        List<String> classLabels = new ArrayList<>();
        classLabels.add("0");
        classLabels.add("1");
        classLabels.add("2");
        Attribute classAtt = new Attribute("class", classLabels);
        attributes.add(attribute0);
        attributes.add(attribute1);
        attributes.add(attribute2);
        attributes.add(classAtt);

        InstancesHeader header = new InstancesHeader(new Instances("elevator", attributes, 0));
        header.setClassIndex(header.numAttributes() - 1);
        return header;
    }

    public void controllerLayerSetStatus(Status status) {
        Model.status = status;
    }

    public Status controllerLayerGetStatus() {
        return Model.status;
    }

    public boolean securityLayerCheck(String instance) {
        //Simulation check code
        return true;
    }

    public InstanceExample adaptLayerAdapt(String instanceString) {
        //Simulation adapt code
        InstancesHeader header = buildHeader();
        Instance instance = new DenseInstance(header.numAttributes());
        String[] attrs = instanceString.split(",");
        instance.setValue(0, Double.parseDouble(attrs[0]));
        instance.setValue(1, Double.parseDouble(attrs[1]));
        instance.setValue(2, Double.parseDouble(attrs[2]));
        instance.setDataset(header);
        if (status == Status.Train) instance.setClassValue(Double.parseDouble(attrs[3]));
        return new InstanceExample(instance);
    }

    public Result algoModelLayerRun(InstanceExample instance) {
        feedbackLayerPre();
        if (status == Status.Train) {
            //System.out.println("test");
            result = algoModelLayerTrain(instance);
        }
        if (status == Status.Apply) {
            //System.out.println("apply");
            result = algoModelLayerWork(instance);
        }
        return result;
    }

    private Result algoModelLayerTrain(InstanceExample instance) {
        //Bagging ensembleModel = new Bagging();

        System.out.println("===algoModelLayerTrain===");
        if (learner.correctlyClassifies(instance.instance)) {
            result.setCorrectNumber(result.getCorrectNumber() + 1);
        }
        double[] prediction = learner.getVotesForInstance(instance);

        learner.trainOnInstance(instance);//区别
        driftDetect.trainOnInstance(instance);
        if (driftDetect.isChangeDetected()) result.setVersionNumber(result.getVersionNumber() + 1);

        evaluator.addResult(instance, prediction);
        result.setDateTime(ZonedDateTime.now());
        return result;
    }

    private Result algoModelLayerWork(InstanceExample instance) {
        //learner.trainOnInstance(instance);
        System.out.println("===algoModelLayerWork===");
        driftDetect.trainOnInstance(instance);
        if (driftDetect.isChangeDetected()) result.setVersionNumber(result.getVersionNumber() + 1);
        double[] prediction = learner.getVotesForInstance(instance);
        evaluator.addResult(instance, prediction);
        result.setDateTime(ZonedDateTime.now());
        result.setPrediction(prediction);
        return result;
    }

    private void feedbackLayerPre() {
        System.out.println("===feedbackLayerPre===");
        //result.setRAMHours(0.0);
        result.setTotalNumber(result.getTotalNumber() + 1);
        result.setVersionNumber(1.0);
    }

    public void feedbackLayerOut(Result result) {
        System.out.println("====feedback====");
        //CPU time
        long evaluateTime = TimingUtils.getNanoCPUTimeOfCurrentThread();
        double time = TimingUtils.nanoTimeToSeconds(evaluateTime - result.getEvaluateStartTime());
        double timeIncrement = TimingUtils.nanoTimeToSeconds(evaluateTime - result.getLastEvaluateStartTime());

        //ramhours
//        double RAMHoursIncrement = learner.measureByteSize() / (1024.0); //KBs
//        RAMHoursIncrement *= (timeIncrement / 3600.0); //hours
//        result.setRAMHours(result.getRAMHours() + RAMHoursIncrement);
//        result.setLastEvaluateStartTime(evaluateTime);
        //LearningCurve

        LearningCurve learningCurve = result.getLearningCurve();
        learningCurve.insertEntry(new LearningEvaluation(
                new Measurement[]{
                        new Measurement("learning evaluation instances", result.getTotalNumber()),
                        new Measurement("evaluation time (" + (result.getPreciseCPUTiming() ? "cpu " : "") + "seconds)", time)
//                        new Measurement("model cost (RAM-Hours)", result.getRAMHours())
                },
                evaluator,
                learner));
        //print
        System.out.println("********" + driftDetect.isChangeDetected() + "********");
        //print head
        String csvFile = "feedback.csv";
        if (result.isFirstDump()) {
            System.out.print("Learner,stream,randomSeed,");
            System.out.println(learningCurve.headerToString());
            writeToCsv(learningCurve.headerToString(), csvFile, learningCurve, result.isFirstDump());
            result.setFirstDump(false);
        }
        //information of inst
        System.out.println(learningCurve.entryToString(learningCurve.numEntries() - 1));
        writeToCsv(learningCurve.entryToString(learningCurve.numEntries() - 1), csvFile, learningCurve, result.isFirstDump());
        //
        DateTimeFormatter zhFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        System.out.println(learner.hashCode() + "," + result.getVersionNumber() + "," + zhFormatter.format(result.getDateTime()));
        System.out.println("====feedback end====");
    }

    private void writeToCsv(String content, String fileName, LearningCurve learningCurve, boolean header) {
        try {
            CSVPrinter printer = new CSVPrinter(new FileWriter(fileName, true), CSVFormat.EXCEL);
            if (header) {
                String[] reportHeader = learningCurve.headerToString().split(",");
                System.out.println("===" + reportHeader[1].toString() + "===");
                System.out.println(learningCurve.headerToString());
                printer.printRecords((Object[]) reportHeader);
            }
            printer.printRecord((Object[]) content.split(","));
            printer.flush();
            printer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Result outputLayerNotOnChain(Result result) {

        System.out.println("====on chain====");
        System.out.println("correctNum:" + result.getCorrectNumber());
        String filename = "learner.model";
        String currentPath = System.getProperty("user.dir");
        String fileString = currentPath + "/" + filename;
        String hashFileString = currentPath + "/" + result.getCurrentKeyOnChain() + "_" + filename;
        try {
            SerializationHelper.write(filename, learner);
            if (result.getTotalNumber() % 10000 == 0) SerializationHelper.write(hashFileString, learner);
            //System.out.println(currentPath);
            System.out.println(fileString);
            //getModel
            //getModelHash
            //setModelHashOnChain
            String hash = calculateMD5(filename);
            //ctx.getStub().putState(hash, (hash+"_"+fileString).getBytes(StandardCharsets.UTF_8));
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String currentDatetimeStr = currentDateTime.format(formatter);
            result.setCurrentKeyOnChain(hash);
            result.setCurrentValueOnChain("{datetime:" + currentDatetimeStr + "}," + "{model:" + fileString + "}");
            //
//            String value = buildValue(instanceString, result);
//            ctx.getStub().putStringState(result.getCurrentKeyOnChain(), value);

            System.out.println("===" + hash + "===");
            System.out.println("====on chain end====");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public Result outputLayerOnChain(AIContext ctx, String instanceString,Result result) {
        result= outputLayerNotOnChain(result);
        String value = buildValue(instanceString, result);
        ctx.getStub().putStringState(result.getCurrentKeyOnChain(), value);
        return result;
    }
     private String buildValue(String instanceString, Result result) {
        String type = "";
        if (AIContext.Model.controllerLayerGetStatus() == Status.Apply) {
            double[] prediction = result.getPrediction();
            int classIndex = Utils.maxIndex(prediction);
            switch (classIndex) {
                case 0:
                    type = "0:still";
                    break;
                case 1:
                    type = "1:start";
                    break;
                case 2:
                    type = "2:boom select";
                    break;
                default:
                    type = "other";
            }
            type = "{class:" + type + "},";
            System.out.println("{class:" + type + "}");
        }
        return "{{data:" + instanceString + "}," + type + result.getCurrentValueOnChain() + "}";
    }
    public static String calculateMD5(String filePath) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        try (InputStream is = new FileInputStream(filePath)) {
            byte[] buffer = new byte[8192];
            int read = 0;
            while ((read = is.read(buffer)) > 0) {
                md.update(buffer, 0, read);
            }
        }
        byte[] md5Bytes = md.digest();
        BigInteger bigInt = new BigInteger(1, md5Bytes);
        String md5 = bigInt.toString(16);
        while (md5.length() < 32) {
            md5 = "0" + md5;
        }
        return md5;
    }
}
