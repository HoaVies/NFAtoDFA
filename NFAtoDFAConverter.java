import java.io.*;
import java.util.*;

public class NFAtoDFAConverter {

    // Class to represent an NFA
    static class NFA {
        int numStates;
        char[] alphabet;
        Map<Integer, Map<Character, Set<Integer>>> transitions;
        int startState;
        Set<Integer> acceptStates;

        NFA(int numStates, char[] alphabet) {
            this.numStates = numStates;
            this.alphabet = alphabet;
            this.transitions = new HashMap<>();
            this.acceptStates = new HashSet<>();
        }

        void addTransition(int fromState, char input, int toState) {
            transitions.putIfAbsent(fromState, new HashMap<>());
            transitions.get(fromState).putIfAbsent(input, new HashSet<>());
            transitions.get(fromState).get(input).add(toState);
        }
    }

    // Class to represent a DFA
    static class DFA {
        Set<Set<Integer>> states;
        Map<Set<Integer>, Map<Character, Set<Integer>>> transitions;
        Set<Integer> startState;
        Set<Set<Integer>> acceptStates;

        DFA() {
            this.states = new HashSet<>();
            this.transitions = new HashMap<>();
            this.acceptStates = new HashSet<>();
        }
    }

    // Read NFA from a file
    public static NFA readNFAFromFile(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;

        // Read number of states
        line = br.readLine();
        int numStates = Integer.parseInt(line.trim());

        // Read alphabet
        line = br.readLine();
        String[] alphabetStrings = line.trim().split("\\s+");
        char[] alphabet = new char[alphabetStrings.length];
        for (int i = 0; i < alphabetStrings.length; i++) {
            alphabet[i] = alphabetStrings[i].charAt(0);
        }

        NFA nfa = new NFA(numStates, alphabet);

        // Read start state
        line = br.readLine();
        nfa.startState = Integer.parseInt(line.trim());

        // Read accept states
        line = br.readLine();
        String[] acceptStatesStr = line.trim().split("\\s+");
        for (String s : acceptStatesStr) {
            if (!s.isEmpty()) {
                nfa.acceptStates.add(Integer.parseInt(s));
            }
        }

        // Read transitions
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue; // Skip empty lines
            String[] parts = line.split("\\s+");
            if (parts.length < 2) continue; // Invalid transition
            int fromState = Integer.parseInt(parts[0]);
            char input = parts[1].charAt(0);
            if (parts.length > 2) {
                for (int i = 2; i < parts.length; i++) {
                    int toState = Integer.parseInt(parts[i]);
                    nfa.addTransition(fromState, input, toState);
                }
            }
        }

        br.close();
        return nfa;
    }

    // Convert NFA to DFA using subset construction
    public static DFA convertNFAtoDFA(NFA nfa) {
        DFA dfa = new DFA();
        Queue<Set<Integer>> queue = new LinkedList<>();
        Map<Set<Integer>, Map<Character, Set<Integer>>> dfaTransitions = new HashMap<>();

        // Initialize start state
        Set<Integer> startSet = new HashSet<>();
        startSet.add(nfa.startState);
        queue.add(startSet);
        dfa.states.add(startSet);

        while (!queue.isEmpty()) {
            Set<Integer> currentSet = queue.poll();

            // Initialize transition map for current DFA state
            Map<Character, Set<Integer>> transitionMap = new HashMap<>();

            for (char c : nfa.alphabet) {
                Set<Integer> newSet = new HashSet<>();
                for (int state : currentSet) {
                    if (nfa.transitions.containsKey(state) && nfa.transitions.get(state).containsKey(c)) {
                        newSet.addAll(nfa.transitions.get(state).get(c));
                    }
                }

                // If the new set is not empty and not already in DFA states, add it to the queue
                if (!newSet.isEmpty() && !dfa.states.contains(newSet)) {
                    dfa.states.add(newSet);
                    queue.add(newSet);
                }

                transitionMap.put(c, newSet);
            }

            dfaTransitions.put(currentSet, transitionMap);

            // Check if current DFA state is an accept state
            for (int state : currentSet) {
                if (nfa.acceptStates.contains(state)) {
                    dfa.acceptStates.add(currentSet);
                    break;
                }
            }
        }

        dfa.startState = startSet;
        dfa.transitions = dfaTransitions;

        return dfa;
    }

    // Write DFA to a file and print to console
    public static void writeDFA(DFA dfa, char[] alphabet, String filename) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(filename));

        // Write number of DFA states
        bw.write(String.valueOf(dfa.states.size()));
        bw.newLine();

        // Write alphabet
        for (int i = 0; i < alphabet.length; i++) {
            bw.write(alphabet[i] + (i < alphabet.length - 1 ? " " : ""));
        }
        bw.newLine();

        // Write start state
        bw.write(dfa.startState.toString());
        bw.newLine();

        // Write accept states
        List<String> acceptStatesList = new ArrayList<>();
        for (Set<Integer> acceptState : dfa.acceptStates) {
            acceptStatesList.add(acceptState.toString());
        }
        bw.write(String.join(" ", acceptStatesList));
        bw.newLine();

        // Write transitions
        for (Set<Integer> state : dfa.transitions.keySet()) {
            Map<Character, Set<Integer>> trans = dfa.transitions.get(state);
            for (char c : trans.keySet()) {
                Set<Integer> nextState = trans.get(c);
                if (!nextState.isEmpty()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(state.toString()).append(" ").append(c).append(" ").append(nextState.toString());
                    bw.write(sb.toString());
                    bw.newLine();
                }
            }
        }

        bw.close();

        // Print DFA to console
        System.out.println("DFA States: " + dfa.states);
        System.out.println("DFA Start State: " + dfa.startState);
        System.out.println("DFA Accept States: " + dfa.acceptStates);
        System.out.println("DFA Transitions:");
        for (Set<Integer> state : dfa.transitions.keySet()) {
            Map<Character, Set<Integer>> trans = dfa.transitions.get(state);
            for (char c : trans.keySet()) {
                Set<Integer> nextState = trans.get(c);
                if (!nextState.isEmpty()) {
                    System.out.println(state + " --" + c + "--> " + nextState);
                }
            }
        }
    }

    public static void main(String[] args) {
        String inputFile = "nfa_input2.txt";
        String outputFile = "dfa_output2.txt";

        try {
            // Read NFA from file
            NFA nfa = readNFAFromFile(inputFile);

            // Convert NFA to DFA
            DFA dfa = convertNFAtoDFA(nfa);

            // Write DFA to file and print to console
            writeDFA(dfa, nfa.alphabet, outputFile);

            System.out.println("\nDFA has been successfully written to " + outputFile);
        } catch (IOException e) {
            System.err.println("Error reading or writing files: " + e.getMessage());
        }
    }
}
