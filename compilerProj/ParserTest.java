
package compilerProj;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import compilerProj.Parser;
import compilerProj.Scanner;
import compilerProj.Parser.SyntaxException;
import compilerProj.Scanner.LexicalException;

public class ParserTest {

	//set Junit to be able to catch exceptions
	@Rule
	public ExpectedException thrown = ExpectedException.none();
 
	
	//To make it easy to print objects and turn this output on and off
	static final boolean doPrint = true;
	private void show(Object input) {
		if (doPrint) {
			System.out.println(input.toString());
		}
	} 


	//creates and returns a parser for the given input.
	private Parser makeParser(String input) throws LexicalException {
		show(input);
		Scanner scanner = new Scanner(input).scan(); 
		show(scanner);  
		Parser parser = new Parser(scanner);
		return parser;
	}
	
	@Test
	public void testEmpty() throws LexicalException, SyntaxException {
		String input = "";  //The input is the empty string.  
		thrown.expect(SyntaxException.class);
		Parser parser = makeParser(input);
		thrown.expect(SyntaxException.class);
		parser.parse();
	}
	
	@Test
	public void testSmallest() throws LexicalException, SyntaxException {
		String input = "2+3-(2*4)+4";  
		Parser parser = makeParser(input);
		try {
			ASTNode ast = parser.parse();
			try {
				int val = Evaluator.Evaluate(ast);
				System.out.println(val);
			}
			catch(SyntaxException ex)
			{
				System.out.println("ERRORR !!!!"); 
			}
		}
		catch(SyntaxException ex)
		{
			System.out.println("ERRORR !!!!"); 
		}
	}
	
	@Test
	public void testSmallest22() throws LexicalException, SyntaxException {
		String input = "2-(3+1-8)*2";  
		Parser parser = makeParser(input);
		try {
			ASTNode ast = parser.parse();
			try {
				int val = Evaluator.Evaluate(ast);
				System.out.println(val);
			}
			catch(SyntaxException ex)
			{
				System.out.println("ERRORR !!!!"); 
			}
		}
		catch(SyntaxException ex)
		{
			System.out.println("ERRORR !!!!"); 
		}
	}
}
