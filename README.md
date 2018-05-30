# Program-to-evaluate-Arithmetic-Expression
A program in java that will parse and evaluate an arithmetic expression.Using tokenization, ast construction, and visitor pattern to evaluate the expression.
Order of precedence of the arithmetic expression needs to be taken into account. The inputs will be ints and the output is expected to be an int
The LL(1) grammar in EBNF for is the following
S ::= E eof
E ::= T (+|- T)*
T ::= F (* F)*
F ::= num | lparen E rparen

SOLUTION - 

There are two test files :-
 
ScannerTest.Java
ParserTest.Java  
 

ScannerTest file was used to check for the test cases for tokenizer and scanning part.
Whereas, ParserTest file is used for both checking for syntactical errors for the grammar and also to evaluate the expression using AST creation and evaluation.

There are 6 Files in the folder:-

Scanner.Java - This is used for generating tokens for the input.
Parser.Java - This is used for parsing the LL(1) grammar and also for AST creation.
ASTNode.Java - This is used to define the structure for ASTNode class which is used for AST creation.
Evaluator.Java - This is used to visit the Abstract syntax tree and evaluating the expression and finally, returning the calculated value.
ScannerTest.Java
ParserTest.Java

 Also, the output consists of all tokens that are generated from the input and then the calculated value of the expression, if there are no errors in the input string. To avoid viewing token generation and if to only see the output, there is a variable in ParserTest.java namely doPrint, changing it's value to false will make the program display only the output of the expression.

ParserTest.java file would generate the calculated value of the expression and also the associated tokens generated for that input.

