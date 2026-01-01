package com.synapsys.user.example.service.neurons;

import lombok.extern.slf4j.Slf4j;
import com.synapsys.springboot.starter.core.SynapsysNeuron;
import com.synapsys.springboot.starter.core.AbstractSynapsysNeuron;

@Slf4j
@SynapsysNeuron(name = "test neuron 7")
public class TestNeuron7 extends AbstractSynapsysNeuron {

    @Override
    public void run() {
        int steps = 100;
        long workPerStep = 10_000_00L; // increase to slow it down more

        log.info("{}: start entered", this.getName());

        for (int step = 1; step <= steps; step++) {

            // Heavy computation chunk
            double sink = 0;
            for (long i = 0; i < workPerStep; i++) {
                sink += Math.sqrt(i) * Math.sin(i);
            }

            // Same-line console update
            System.out.print(
                "\r" + this.getName() + ": computing... " + step + "% complete"
            );
            System.out.flush();
        }

        System.out.println();
        log.info("{}: computation finished", this.getName());
    }

    @Override
    public void onStop() {
        log.info("{}: on stop entered", this.getName());
    }
}
