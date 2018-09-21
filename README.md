# trinary-logic
Small, simple project that presents a step-by-step reduction of an expression involving 3-valued logic

Programmed in Java; 2 classes

Uses the argument in the command line as input if it is provided; otherwise, prompts the user for input

##### Sample execution:

  TrinaryLogic> (T->?)&!(F|T)<->T&(F&?)

  (T→?)&!(F|T)↔T&(F&?)

  (T→?)&!T↔T&(F&?)

  (T→?)&F↔T&(F&?)

  (T→?)&F&(F&?)

  (T→?)&F&F

  (T→?)&F

  ?&F

  F
