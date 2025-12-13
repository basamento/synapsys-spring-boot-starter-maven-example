package com.synapsys.user.example.service.neurons;

import com.synapsys.springboot.starter.SynapsysNeuron;
import com.synapsys.springboot.starter.SynapsysNeuronContract;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SynapsysNeuron
public class TestNeuron8 implements SynapsysNeuronContract {

    @Override
    public String getName() {
        return "test neuron 8";
    }

    @Override
    public void start() {
        int milliseconds = 10000;
        log.info("{}: start entered", this.getName());
        try {
            log.info("{}: sleeping for {} milliseconds",this.getName(), milliseconds);
            Thread.sleep(milliseconds);
            log.info(" {}: awakening", this.getName());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
