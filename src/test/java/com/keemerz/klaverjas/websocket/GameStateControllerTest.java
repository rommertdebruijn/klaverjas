package com.keemerz.klaverjas.websocket;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class GameStateControllerTest {

    @Test
    void testSolutions() {
        Set<Solution> solutions = generateAllSolutions();

        List<Solution> viableSolutions = solutions.stream()
                .filter(s -> s.a != 0)
                .filter(s -> s.d != 0)
                .filter(s -> s.a != 3 || s.b == 2)
                .filter(s -> s.d != 3 || s.a == 2)
                .filter(s -> s.b != 3)
                .filter(s -> s.a != 3)
                .collect(Collectors.toList());

        System.out.println(viableSolutions);
    }

    private Set<Solution> generateAllSolutions() {
        Set<Solution> solutions = new HashSet<>();
        List<Integer> values = Arrays.asList(0,1,2,3);
        while (solutions.size() < 24) { // you can hate me later :)
            Collections.shuffle(values);
            solutions.add(new Solution(values.get(0), values.get(1), values.get(2), values.get(3)));
        }
        return solutions;
    }

    class Solution {
        private int a;
        private int b;
        private int c;
        private int d;

        public Solution(int a, int b, int c, int d) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Solution solution = (Solution) o;
            return a == solution.a &&
                    b == solution.b &&
                    c == solution.c &&
                    d == solution.d;
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b, c, d);
        }

        @Override
        public String toString() {
            return "[" +
                    "a=" + a +
                    ", b=" + b +
                    ", c=" + c +
                    ", d=" + d +
                    "]\n";
        }
    }

}