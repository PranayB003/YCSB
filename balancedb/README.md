# Quick Start

## 1. Start the balanceDB instance
## 2. Install Java and Maven
## 3. Set up YCSB

Git clone YCSB and compile:
```bash
git clone http://github.com/brianfrankcooper/YCSB.git
cd YCSB
mvn -pl site.ycsb:balancedb-binding -am clean package
```

## 4. Running in Shell mode
If you want to interact with the system manually instead of running a workload,
execute the following command:
```bash
./bin/ycsb shell balancedb -p balance.hosts=IP1:PORT1[,IP2:PORT2,IP3:PORT3...] 
```

## 5. Provide connection parameters
Set hosts, and port in the workload you plan to run.
- `balance.hosts`: a comma separated list of http-enabled BalanceDB server
hostnames/IP addresses and ports.

Alternatively, set configs with the following shell command:
```bash
./bin/ycsb load balancedb -s -P workloads/workloada -p "balance.hosts=127.0.0.1:8080" > outputLoad.txt
```

## 6. Load data and run tests
Load the data:
```bash
./bin/ycsb load balancedb -s -P workloads/workloada -p "balance.hosts=127.0.0.1:8080" > outputLoad.txt
```

Run the workload test:
```bash
./bin/ycsb run balancedb -s -P workloads/workloada -p "balance.hosts=127.0.0.1:8080" > outputRun.txt
```
