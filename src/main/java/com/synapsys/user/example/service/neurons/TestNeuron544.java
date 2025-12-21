package com.synapsys.user.example.service.neurons;

import lombok.extern.slf4j.Slf4j;
import com.synapsys.springboot.starter.core.SynapsysNeuron;
import com.synapsys.springboot.starter.core.AbstractSynapsysNeuron;

@Slf4j
@SynapsysNeuron(name = "test neuron 544")
public class TestNeuron544 extends AbstractSynapsysNeuron {

    @Override
    public void run() {
        int milliseconds = 10000;
        log.info("{}: start entered", this.getName());
        try {
            log.info("{}: sleeping for {} milliseconds", this.getName(), milliseconds);
            Thread.sleep(milliseconds);
            log.info("{}: awakening", this.getName());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onStop() {
        log.info("{}: on stop entered", this.getName());
    }
}
