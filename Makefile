PHONY: run
run:
	mvn clean compile
	mvn exec:java -Dexec.mainClass="edu.ntnu.stud.boardgame.BoardGameMain"