package com.synapsys.user.example.service.neurons;

import com.synapsys.springboot.starter.SynapsysNeuron;
import com.synapsys.springboot.starter.SynapsysNeuronContract;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SynapsysNeuron
public class TestNeuron2 implements SynapsysNeuronContract {

    @Override
    public String getName() {
        return "test neuron 2";
    }

    @Override
    public void start() {
        log.info("Test Neuron: the process has started");
    }

}
