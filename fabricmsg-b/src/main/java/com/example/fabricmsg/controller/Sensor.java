package com.example.fabricmsg.controller;

import com.yahoo.labs.samoa.instances.InstancesHeader;
import moa.core.InstanceExample;
import moa.streams.ArffFileStream;

public class Sensor {
    static String Path = "/home/eric/mywork/data.arff";
    //static String Path = Sensor.class.getClassLoader().getResource("still-86995.arff").getPath();
    static int index = -1;
    static ArffFileStream stream = new ArffFileStream(Path, index);
    public Sensor() {
        stream.prepareForUse();
    }
    public InstanceExample Post() {
        //stream file
        return stream.nextInstance();
    }
    public static InstancesHeader getHeader() {
        return stream.getHeader();
    }
}
