PHONY: run
run:
	mvn clean compile
	mvn exec:java -Dexec.mainClass="edu.ntnu.stud.boardgame.BoardGameMain"

PHONY: test
test:
	mvn clean test jacoco:report
	open target/site/jacoco/index.html