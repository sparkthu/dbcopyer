# dbcopyer

## Usage 
    java -jar dbcopyer.jar --conf task.yml,task2.yml [--task task1]

## Tips
    Column config: name is used for dest table, indexInResult is the 1-based order in result set of querySql
    Groovy is supported to convert some value before insert, you MUST use def convert(x){xxx} , the result will be passed by x, and the return value of convert(x) will be inserted into new table.
