package edu.coursera.concurrent;

import edu.rice.pcdp.Actor;

import java.util.HashSet;
import java.util.Set;

import static edu.rice.pcdp.PCDP.finish;

/**
 * An actor-based implementation of the Sieve of Eratosthenes.
 * <p>
 * TODO Fill in the empty SieveActorActor actor class below and use it from
 * countPrimes to determin the number of primes <= limit.
 */
public final class SieveActor extends Sieve {
    /**
     * {@inheritDoc}
     * <p>
     * TODO Use the SieveActorActor class to calculate the number of primes <=
     * limit in parallel. You might consider how you can model the Sieve of
     * Eratosthenes as a pipeline of actors, each corresponding to a single
     * prime number.
     */
    @Override
    public int countPrimes(final int limit) {
        SieveActorActor sieveActor = new SieveActorActor();
        int maxPrime = (int) Math.sqrt(limit);
        finish(() -> {
            for (int i = 2; i <= limit; i++) {
                sieveActor.send(new SieveData(i, maxPrime));
            }
            sieveActor.send(new SieveData(0, maxPrime));
        });

        int primes = 0;
        for (SieveActorActor iter = sieveActor; iter != null; iter = iter.nextActor) {
            primes += iter.numLocalPrimes();
        }
        return primes;
    }

    /**
     * An actor class that helps implement the Sieve of Eratosthenes in
     * parallel.
     */
    public static final class SieveActorActor extends Actor {
        private Set<Integer> localPrimes = new HashSet<>();
        private SieveActorActor nextActor;

        /**
         * Process a single message sent to this actor.
         * <p>
         * TODO complete this method.
         *
         * @param msg Received message
         */
        @Override
        public void process(final Object msg) {
            SieveData data = (SieveData) msg;

            Integer numberToCheck = data.getWorkingNumber();

            if (numberToCheck <= 0) {
                if (this.nextActor != null) {
                    this.nextActor.send(msg);
                }
            } else {
                boolean locallyPrime = isLocalPrime(numberToCheck);

                if (locallyPrime) {
                    if (this.localPrimes.size() < data.getMaxLocalPrimes()) {
                        this.localPrimes.add(numberToCheck);
                    } else {
                        if (nextActor == null) {
                            nextActor = new SieveActorActor();
                        }

                        nextActor.send(msg);
                    }
                }
            }
        }

        boolean isLocalPrime(int candidate) {
            for (Integer prime : localPrimes) {
                if (candidate % prime == 0) {
                    return false;
                }
            }
            return true;
        }

        public int numLocalPrimes() {
            return this.localPrimes.size();
        }
    }
}
