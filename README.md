
# This project is part of a master's thesis to implement eLTL logic, UMA University (Spain).
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
  ◇<sub>[p,q]</sub> ψ ≡ True U<sub>[p,q]</sub>ψ
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

## Project layout
```
│   build.sbt
├───csv
.
.
.
├───src
│   ├───main
.
.
.
│   │   │
│   │   └───scala
│   │       └───org
│   │           └───uma
│   │                   Ejemplos.scala
│   │                   eLTL.scala
│   │                   Rendimiento.scala
│   │
│   └───test
│       └───scala
│               eLTLtest.scala
│               timeTest.scala
```
## Implementation: eLTL.scala
Operators     |eLTL         | Scala
-------------|------------- | -------------
Until        | U            | U
Always       | ◻           | A  / A2
Eventually   | ◇           | E  / E2
Neg          | ¬            | Neg
Until        | U            | Or

## Example
For all intervals that begin with "1" and end with "3" must contain at least one "2":
<p align="center">
  ◻<sub>[p,q]</sub> ◇<sub>[r]</sub> True
</p>
<p align="center">
  p:{e==1}, q:{e==3} and r:{e==2} 
</p>
The file csv/10.csv meets this condition, values are pairs with the forme (index: Long, Data: T), in this case T is en integer:

```
1;1
2;0
3;0
4;0
5;2
6;0
7;0
8;0
9;0
10;3
```


Applying this formula the result has to give true:

```
...
override type T = (Long, Int)
type TT = Int
val path = os.pwd / "csv"
...     
val _10      = benv.readCsvFile[T](path+"/10.csv", fieldDelimiter = ";")
...
def Start: (TT => Boolean) = (e: TT) => {e == 1}
def Stop : (TT => Boolean) = (e: TT) => {e == 3}
def Cond : (TT => Boolean) = (e: TT) => {e == 2}
...
println(A(Start, Stop) (E(Cond) (True)) (_10, 0, Long.MaxValue))
```
More examples have been implemented in *Ejemplos.scala*.

Check out this Jupyter notebook for more fun.
<https://github.com/AbuAwn/eLTL-master/blob/1.1/Rendimiento.ipynb>
