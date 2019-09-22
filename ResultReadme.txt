How to read results:
- All the csv files uses tab character as delimiter.
- data.csv  and readableFormat.txt shows the result of the simulation.
- convergence.csv shows how the simulation convergence (or not)
- paths.csv shows all the path elaborated by the simulator.

Path.csv:
file format:
Commands:
1-
PR - Prepare path
  Parameters: "NULL" DESTINATION PATH
RQ - Request path
  Parameters: (SUCC|FAIL) DISTANCE PATH
RS - Response path
 Parameters: (SUCC|FAIL) DISTANCE PATH
DT - Data path
 Parameters: (true|false) NULL NULL
 true - the master receives data
 false - the master do not receive nothing.