[![Build status](https://travis-ci.org/demid5111/lingvo-dss.svg?branch=master)](https://travis-ci.org/demid5111/lingvo-dss?branch=master)
 
[![Quality Gate](https://sonarqube.com/api/badges/gate?key=dss.lingvo)](https://sonarqube.com/dashboard/index?id=dss.lingvo)


### How to run

You need to provide the path to the input JSON file, containing problem description

Example launch:

1. macOS:

   ```bash
   ./gradlew run -PappArgs="['-i', 'PATH_TO_PROJECT_ROOT/src/main/resources/description_multilevel.json', '-o', 'run.log']"
   ```

2. Windows:
   ```bash
   .\gradlew run --args='-i PATH_TO_PROJECT_ROOT\src\main\resources\description_multilevel.json -o run.log'
   ```

### How to use as a jar

1. Build the jar with core logic and all dependencies: `./gradlew shadowJar`
2. [Optional] build the jar with sources: `./gradlew sourcesJar`
3. Both jars are placed in **build/libs** folder. *lingvo-dss-all.jar* represents core logic with dependencies.
While *lingvo-dss-sources.jar* contains sources of the library.
4. Run the jar on the given description:
   ```bash
   java -jar .\build\libs\lingvo-dss-all.jar -i .\src\main\resources\description_multilevel.json -o run.log
   ```
