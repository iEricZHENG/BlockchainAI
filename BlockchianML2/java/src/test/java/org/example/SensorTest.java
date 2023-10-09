package org.example;

import moa.core.InstanceExample;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SensorTest {

    @Test
    void post() {
        Sensor s = new Sensor();
        for (int i = 0; i < 10; i++) {
            InstanceExample instance = s.Post();
            assertNotNull(instance);
            System.out.println(String.valueOf(instance.instance.toString()));
        }
    }
}