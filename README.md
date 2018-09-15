# trinary-logic
Small, simple project that presents a step-by-step reduction of an expression involving 3-valued logic

Language: Java

2 classes

Gets input as an argument in the command line if possible. If no argument is provided, asks for input.

### Sample execution:

  TrinaryLogic> (T->?)&!(F|T)<->T&(F&?)

  (T→?)&!(F|T)↔T&(F&?)

  (T→?)&!T↔T&(F&?)

  (T→?)&F↔T&(F&?)

  (T→?)&F&(F&?)

  (T→?)&F&F

  (T→?)&F

  ?&F

  F
