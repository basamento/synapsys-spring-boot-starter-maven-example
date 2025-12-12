package com.synapsys.user.example.service.neurons;

import com.synapsys.springboot.starter.SynapsysNeuron;
import com.synapsys.springboot.starter.SynapsysNeuronContract;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SynapsysNeuron
public class TestNeuron5 implements SynapsysNeuronContract {

    @Override
    public String getName() {
        return "test neuron 5";
    }

    @Override
    public void start() {
        log.info("Test Neuron: the process has started");
    }

}
