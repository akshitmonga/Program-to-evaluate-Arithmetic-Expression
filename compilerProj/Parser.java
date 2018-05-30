package compilerProj;

import static compilerProj.Scanner.Kind.*;

import java.util.ArrayList;

import compilerProj.ASTNode.*;
import compilerProj.Scanner.Kind;
import compilerProj.Scanner.Token;

public class Parser {

	@SuppressWarnings("serial")
	public static class SyntaxException extends Exception {
		Token t;

		public SyntaxException(Token t, String message) {
			super(message);
			this.t = t;
		}

	}
	
	Scanner scanner;
	Token t;

	Parser(Scanner scanner) {
		this.scanner = scanner;
		t = scanner.nextToken();
	}

	// Parser For Given LL(1) Grammar
	
	/*
	* S ::= E eof

	  E ::= T (+ | - T)*

	  T ::= F (* F)*

	  F ::= num | lparen E rparen
	 */

	public ASTNode parse() throws SyntaxException {
		ASTNode eval = E();
		matchEOF();
		return eval;
	}
	
	public ASTNode E() throws SyntaxException {
		ASTNode left = T();
		ASTNode right = null;
		while(isKind(Kind.OP_PLUS) || isKind(Kind.OP_MINUS) ) {
			if(isKind(Kind.OP_PLUS)) {
				consume();
				right = T();
				left =  CreateNode(ASTNodeType.OperatorPlus, left, right);
			}
			else {
				consume();
				right = T();
				left =  CreateNode(ASTNodeType.OperatorMinus, left, right);
			}	
		}
		return left;
	}
	
	public ASTNode T() throws SyntaxException {
		ASTNode right = null;
		ASTNode left = F();
		while(isKind(Kind.OP_TIMES)) {
			consume();
			right = F();
			left =  CreateNode(ASTNodeType.OperatorMul, left, right);
		}
		return left;
	}
	
	public ASTNode F() throws SyntaxException {
		ASTNode exp = null;
		if(isKind(NUM)){
			int value = t.intVal();
			consume();
			return CreateNodeNumber(value);
		}
		else if(isKind(Kind.LPAREN)){
			consume();
			exp = E();
			match(RPAREN);
			return exp;
		}
		else {
			throw new SyntaxException(t,"Syntax Error at pos : " + (t.pos+1));
		}
	}
	
	// Function to create Node for Abstract Syntax Tree
	 ASTNode CreateNode(ASTNodeType type, ASTNode left, ASTNode right)
	   {
	      ASTNode node = new ASTNode();
	      node.Type = type;
	      node.Left = left;
	      node.Right = right;
	      return node;
	   }
	 
	 // Function for Creating Node for number in AST
	 ASTNode CreateNodeNumber(int value)
	   {
	      ASTNode node = new ASTNode();
	      node.Type = ASTNodeType.NumberValue;
	      node.Value = value;
	 
	      return node;
	   }
	 
	protected boolean isKind(Kind kind) {
		return t.kind == kind;
	}

	// Checks for the current token, if it matches then it moves to the next token else throws an error	
	private Token match(Kind kind) throws SyntaxException {
		Token tmp = t;
		if (isKind(kind)) {
			consume();
			return tmp;
		}
		throw new SyntaxException(t,"Syntax Error" + " " + "expected " + kind + " At line : " + tmp.line() +" Pos : " + tmp.posInLine()  ); 
	}

	// Moves to the next token
	private Token consume() throws SyntaxException {
		Token tmp = t;
		if (isKind( EOF)) {
			throw new SyntaxException(t,"Syntax Error");  
			
		}
		t = scanner.nextToken();
		return tmp;
	}

	private Token matchEOF() throws SyntaxException {
		if (isKind(EOF)) {
			return t;
		}
		throw new SyntaxException(t,"Syntax Error at pos: " + (t.pos+1) ); 
	}


}

