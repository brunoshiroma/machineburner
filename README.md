#Machine Burner
Stress Test for machines running Java 13+

## Tests

 * Memory - Allocate memory
 * CPU - Run CPU Thread - 1, load
 * GPU - Run GPU computing load
 
### Usage example
```
java -Xms8G -Xmx8G -jar machineburner-version.jar -cpu -mem -gpu
```
This will run all test on machine

##### Know issues:
 * On hybrid GPU system, it´s probably will run on integrated GPU