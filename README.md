
# This project is part of a work end of Master to impliment eLTL logic, UMA University (Spain).

A *Flink* application project using *Scala* and *SBT* to implement **eLTL logic**

To run and test your application locally, you can just execute `sbt run` then select the main class that contains the Flink job . 

You can also package the application into a fat jar with `sbt assembly`, then submit it as usual, with something like: 

```
flink run -c org.example.WordCount /path/to/your/project/my-app/target/scala-2.11/testme-assembly-0.1-SNAPSHOT.jar
```


You can also run your application from within IntelliJ:  select the classpath of the 'mainRunner' module in the run/debug configurations.
Simply open 'Run -> Edit configurations...' and then select 'mainRunner' from the "Use classpath of module" dropbox. 

## eLTL formulae
- State formula p ∈ F evaluated on single states. An event e ∈ L is also a state formula
- Interval formula φ ∈ Φ, φ ∶ I → {true, f alse} describe the expected behaviour of continuous variables
  - [t<sub>p</sub>, t<sub>q</sub>] ∈ I time intervals in which variables must be observed
  - Event intervals [p, q], p, q ∈ F determine intervals of states in the trace

Given p, q ∈ F, and φ ∈ Φ, the formulae of eLTL logic are recursively constructed as follows:
<p align="center">
  ψ ∶∶= φ ∣ ¬ψ ∣ ψ<sub>1</sub> ∨ ψ<sub>2</sub> ∣ ψ<sub>1</sub>U<sub>[p,q]</sub>ψ<sub>2</sub> ∣ ψ<sub>1</sub>U<sub>p</sub>ψ<sub>2</sub>
</p>
The rest of the temporal operators are accordingly defined as:
<p align="center">
  ◇<sub>[p,q]</sub> ψ ≡ True U<sub>[p,q]</sub>]ψ
</p>
<p align="center">  
  ◇<sub>p</sub> ψ ≡ True U<sub>p</sub> ψ
</p>
<p align="center">
  ◻<sub>[p,q]</sub> ψ ≡ ¬(◇<sub>[p,q]</sub>¬ψ)
</p>
<p align="center">
  ◻<sub>p</sub> ψ ≡ ¬(◇<sub>p</sub> ¬ψ)
</p>

<sub>p</sub>
Normal Text

<p align="center">
 ....
</p>

Normal text
