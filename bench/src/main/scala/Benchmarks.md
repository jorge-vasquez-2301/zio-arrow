# ZArrow benchmark results

`ZArrow` was benchmarked on the following setup:

## Hardware

* CPU: Intel(R) Core(TM) i7-6700HQ CPU @ 2.60GHz 
* Memory: DDR4 64GB
* SSD Drive 

## Software
* Ubuntu: 19.10.1 64Bit
* Kernel: 5.3.0-40-generic
* Java: OpenJDK 11.0.6
* Compiler: Scala 2.13.1
* JMH : 1.21

## API Benchmark Results ( by method, the higher the better )
```bash
[info] Benchmark               Mode  Cnt          Score   Error  Units
[info] asEffect  thrpt        39291338.197          ops/s
[info] choice    thrpt        82481642.103          ops/s
[info] compose   thrpt        76284520.477          ops/s
[info] endThen   thrpt        83402268.332          ops/s
[info] first     thrpt        68216801.935          ops/s
[info] id        thrpt       117017373.778          ops/s
[info] left      thrpt        98232953.255          ops/s
[info] lift      thrpt       219555267.469          ops/s
[info] merge     thrpt        25538627.153          ops/s
[info] right     thrpt        95448060.160          ops/s
[info] second    thrpt        66618998.459          ops/s
[info] split     thrpt        83030147.647          ops/s
[info] test      thrpt        18945081.040          ops/s
[info] zipWith   thrpt        83923062.648          ops/s
```