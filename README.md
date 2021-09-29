
# This project is part of a work end of Master to impliment eLTL logic.

A Flink application project using Scala and SBT to implement eLTL logic

To run and test your application locally, you can just execute `sbt run` then select the main class that contains the Flink job . 

You can also package the application into a fat jar with `sbt assembly`, then submit it as usual, with something like: 

```
flink run -c org.example.WordCount /path/to/your/project/my-app/target/scala-2.11/testme-assembly-0.1-SNAPSHOT.jar
```


You can also run your application from within IntelliJ:  select the classpath of the 'mainRunner' module in the run/debug configurations.
Simply open 'Run -> Edit configurations...' and then select 'mainRunner' from the "Use classpath of module" dropbox. 

## eLTL formulae

<img src="https://render.githubusercontent.com/render/math?math=\psi ::= \phi \,|\, \lnot \psi \,|\, \psi_1 \vee \psi_2 \,|\, \psi_1 U_{[p,q]} \phi \,|\, \psi_1 U_p \psi_2">

