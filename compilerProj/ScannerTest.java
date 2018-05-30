
package compilerProj;

import static compilerProj.Scanner.Kind.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import compilerProj.Scanner.LexicalException;
import compilerProj.Scanner.Token;

public class ScannerTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	
	//To make it easy to print objects and turn this output on and off
	static boolean doPrint = true;
	private void show(Object input) {
		if (doPrint) {
			System.out.println(input.toString());
		}
	}
	
	Token checkNextIsEOF(Scanner scanner) {
		Scanner.Token token = scanner.nextToken();
		assertEquals(Scanner.Kind.EOF, token.kind);
		assertFalse(scanner.hasTokens());
		return token;
	}
	
	// To check for Next Token
	Token checkNext(Scanner scanner, Scanner.Kind kind, int pos, int length, int line, int pos_in_line) {
		Token t = scanner.nextToken();
		assertEquals(kind, t.kind);
		assertEquals(pos, t.pos);
		assertEquals(length, t.length);
		assertEquals(line, t.line());
		assertEquals(pos_in_line, t.posInLine());
		return t;
	}

	// Retrieves the next token and checks that its kind and length match the given parameters.
	Token checkNext(Scanner scanner, Scanner.Kind kind, int length) {
		Token t = scanner.nextToken();
		assertEquals(kind, t.kind);
		assertEquals(length, t.length);
		return t;
	}
	
	@Test
	public void testEmpty() throws LexicalException {
		String input = "";  
		show(input);         
		Scanner scanner = new Scanner(input).scan();  
		show(scanner);   
		checkNextIsEOF(scanner);  
	}
	
	@Test
	public void failIllegalChar() throws LexicalException {
		String input = ";;~";
		show(input);
		thrown.expect(LexicalException.class);  
		try {
			new Scanner(input).scan();
		} catch (LexicalException e) {  
			show(e);                    
			assertEquals(0,e.getPos()); 
			throw e;                    
		}
	}

	@Test
	public void testParens() throws LexicalException {
		String input = "()";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, LPAREN, 0, 1, 1, 1);
		checkNext(scanner, RPAREN, 1, 1, 1, 2);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testExpression() throws LexicalException {
		String input = "1234+1234";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, NUM, 0, 4, 1, 1);
		checkNext(scanner, OP_PLUS, 4, 1, 1, 5);
		checkNext(scanner, NUM, 5, 4, 1, 6);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testExpression2() throws LexicalException  {
		String input = "2+3-(2*4)+4";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, NUM, 0, 1, 1, 1);
		checkNext(scanner, OP_PLUS, 1, 1, 1, 2);
		checkNext(scanner, NUM, 2, 1, 1, 3);
		checkNext(scanner, OP_MINUS, 3, 1, 1, 4);
		checkNext(scanner, LPAREN, 4, 1, 1, 5);
		checkNext(scanner, NUM, 5, 1, 1, 6);
		checkNext(scanner, OP_TIMES, 6, 1, 1, 7);
		checkNext(scanner, NUM, 7, 1, 1, 8);
		checkNext(scanner, RPAREN, 8, 1, 1, 9);
		checkNext(scanner, OP_PLUS, 9, 1, 1, 10);
		checkNext(scanner, NUM, 10, 1, 1, 11);
		checkNextIsEOF(scanner);
	}
	

	@Test
	public void testExpression3() throws LexicalException {
		String input = "1234&1543";
		show(input);
		thrown.expect(LexicalException.class);
		try {
			new Scanner(input).scan();
		} catch (LexicalException e) { 
			show(e);                    
			assertEquals(4,e.getPos()); 
			throw e;                    
		}
	}		
}
