package edu.coursera.concurrent;

import edu.rice.pcdp.Actor;

import java.util.HashSet;
import java.util.Set;

import static edu.coursera.concurrent.SieveData.FINISH;
import static edu.rice.pcdp.PCDP.finish;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

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
            sieveActor.send(FINISH);
        });

        int primes = 0;
        for (SieveActorActor iter = sieveActor; iter != null; iter = iter.nextActor) {
            primes += iter.localPrimes.size();
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
            if (msg == FINISH && nonNull(nextActor)) {
                nextActor.send(FINISH);
            } else {
                sieve((SieveData) msg);
            }
        }

        private void sieve(SieveData sieveData) {
            Integer numberToCheck = sieveData.getNumberToCheck();
            if (isLocalPrime(sieveData.getNumberToCheck())) {
                addToLocalPrimes(sieveData);
            }
        }

        private void addToLocalPrimes(SieveData sieveData) {
            if (isLocalPrime(sieveData)) {
                localPrimes.add(sieveData.getNumberToCheck());
            } else {
                sendToNextActor(sieveData);
            }
        }

        private boolean isLocalPrime(SieveData data) {
            return localPrimes.size() < data.getMaxLocalPrimes();
        }

        private void sendToNextActor(Object msg) {
            if (isNull(nextActor)) {
                nextActor = new SieveActorActor();
            }
            nextActor.send(msg);
        }

        boolean isLocalPrime(int number) {
            return localPrimes.stream().parallel().noneMatch(prime -> number % prime == 0);
        }
    }
}
