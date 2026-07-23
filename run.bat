@echo off
javac -cp "lib\mysql-connector-j-9.7.0.jar" -d bin src\db\*.java src\model\*.java src\dao\*.java src\util\*.java src\gui\*.java src\Main.java
java -cp "bin;lib\mysql-connector-j-9.7.0.jar" Main
pause