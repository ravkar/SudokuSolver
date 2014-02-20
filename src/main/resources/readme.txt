0. prerequsites
  java 1.4 or higher
  maven 1.02

1. building project

cd $SUDOKU_HOME
$SUDOKU_HOME> maven jar

2. calling application: 
cd $SUDOKU_HOME/target
target> java -cp sudoku-1.0.jar Main ../src/test/resources/test_12k.txt

3. Example input data file format:
0,3,2,4,5,0,0,0,7
0,0,7,0,0,0,5,6,1
0,5,0,0,0,0,0,0,0 
5,0,0,0,6,0,0,2,0 
0,4,0,2,8,7,0,5,0
0,8,0,0,3,0,0,0,9
0,0,0,0,0,0,0,3,0 
7,1,5,0,0,0,8,0,0
3,0,0,0,7,9,2,1,0