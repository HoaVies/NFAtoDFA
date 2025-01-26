# Converting NFA to DFA

## Objective
The goal of this task is to convert a **Non-deterministic Finite Automaton (NFA)** into a **Deterministic Finite Automaton (DFA)** using the **subset construction** method. This eliminates non-determinism and ensures that each DFA state has a single transition for every input symbol.

## Algorithm: Subset Construction

### Steps:
1. **Initialization**:
   - Start with the NFA's start state.
   - The DFA's start state is a set containing the NFA's start state.
   - Use a queue to process DFA states iteratively.

2. **Transitions**:
   - For each DFA state (a set of NFA states) and for each input symbol:
     - Compute the set of NFA states reachable via the input symbol.
     - Add this set as a new DFA state if it hasn't been added before.
     - Record the transition between the current DFA state and the new state.

3. **Accept States**:
   - Any DFA state containing at least one NFA accept state becomes a DFA accept state.

4. **Termination**:
   - Repeat until no new DFA states are generated.

---

## Data Structures

### NFA Representation
- **States**: `int`
- **Alphabet**: `char[]`
- **Transitions**: `Map<Integer, Map<Character, Set<Integer>>>`
- **Start State**: `int`
- **Accept States**: `Set<Integer>`

### DFA Representation
- **States**: `Set<Set<Integer>>`
- **Transitions**: `Map<Set<Integer>, Map<Character, Set<Integer>>>`
- **Start State**: `Set<Integer>`
- **Accept States**: `Set<Set<Integer>>`

---

## Example Execution

### Input NFA (Example 1):
File: `nfa_input.txt`
```
4                      # Number of states in the NFA: 0, 1, 2, 3 (q0, q1, q2, q3)
a b                    # Alphabet of the NFA: 'a' and 'b'
0                      # Start state: 0 (q0)
3                      # Accept state: 3 (q3)
0 a 0 1                # From q0, on 'a', transitions to q0 and q1
0 b 0                  # From q0, on 'b', transitions to q0
1 a 2                  # From q1, on 'a', transitions to q2
2 b 3                  # From q2, on 'b', transitions to q3
```

Output saved in `dfa_output.txt`.

---

### Example Execution 
Below is a visual representation of the conversion process from NFA to DFA:

![NFA to DFA Conversion Example](https://github.com/HoaVies/NFAtoDFA/blob/master/1stExample.png)
![Code Executed]https://github.com/HoaVies/NFAtoDFA/blob/master/Ex1code.png
---

## References

- [Automata Conversion from NFA to DFA - Javatpoint](https://www.javatpoint.com/automata-conversion-from-nfa-to-dfa)
```
