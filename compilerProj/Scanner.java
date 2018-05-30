
package compilerProj;

import java.util.ArrayList;
import java.util.Arrays;


//	Scanner For Given LL(1) Grammar

	/*
	* S ::= E eof

	  E ::= T (+ | - T)*

	  T ::= F (* F)*

	  F ::= num | lparen E rparen
	 */


public class Scanner {

	@SuppressWarnings("serial")
	public static class LexicalException extends Exception {

		int pos;

		public LexicalException(String message, int pos) {
			super(message);
			this.pos = pos;
		}

		public int getPos() {
			return pos;
		}
	}

	public static enum Kind {
		NUM, OP_PLUS, OP_MINUS, OP_TIMES, OP_DIV, LPAREN, RPAREN, EOF, ERR;
	}
	
	String temp = "";   // Used for building token of number
	
	public boolean checkForDigit(char a) {
		return ((a>='0' && a<='9'))? true:false;
	}
	
	public class Token {
		public final Kind kind;
		public final int pos; // position of first character of this token in the input. Counting starts at 0
								// and is incremented for every character.
		public final int length; // number of characters in this token

		public Token(Kind kind, int pos, int length) {
			super();
			this.kind = kind;
			this.pos = pos;
			this.length = length;
		}

		public String getText() {
			return String.copyValueOf(chars, pos, length);
		}

		public int intVal() {
			assert kind == Kind.NUM;
			return Integer.valueOf(String.copyValueOf(chars, pos, length));
		}

		public int line() {
			return Scanner.this.line(pos) + 1;
		}

		// To get position of char in that line
		public int posInLine(int line) {
			return Scanner.this.posInLine(pos, line - 1) + 1;
		}

		// Returns the position in the line of this Token in the input.
		public int posInLine() {
			return Scanner.this.posInLine(pos) + 1;
		}

		public String toString() {
			int line = line();
			return "[" + kind + "," + String.copyValueOf(chars, pos, length) + "," + pos + "," + length + "," + line
					+ "," + posInLine(line) + "]";
		}

	}// Token

	int[] lineStarts;

	int[] initLineStarts() {
		ArrayList<Integer> lineStarts = new ArrayList<Integer>();
		int pos = 0;

		for (pos = 0; pos < chars.length; pos++) {
			lineStarts.add(pos);
			char ch = chars[pos];
			while (ch != EOFChar && ch != '\n' && ch != '\r') {
				pos++;
				ch = chars[pos];
			}
			if (ch == '\r' && chars[pos + 1] == '\n') {
				pos++;
			}
		}
		// convert arrayList<Integer> to int[]
		return lineStarts.stream().mapToInt(Integer::valueOf).toArray();
	}

	int line(int pos) {
		int line = Arrays.binarySearch(lineStarts, pos);
		if (line < 0) {
			line = -line - 2;
		}
		return line;
	}

	public int posInLine(int pos, int line) {
		return pos - lineStarts[line];
	}

	public int posInLine(int pos) {
		int line = line(pos);
		return posInLine(pos, line);
	}

	// To represent End of File
	static final char EOFChar = 128;

	//The list of tokens created by the scan method.
	final ArrayList<Token> tokens;

	// An array of characters representing the input.
	final char[] chars;
	
	
	// position of the next token to be returned by a call to nextToken
	private int nextTokenPos = 0;

	Scanner(String inputString) {
		int numChars = inputString.length();
		this.chars = Arrays.copyOf(inputString.toCharArray(), numChars + 1); // input string terminated with null char
		chars[numChars] = EOFChar;
		tokens = new ArrayList<Token>();
		lineStarts = initLineStarts();
	}

	private enum State {START,IN_OPERATOR,IN_DIGIT}; 

	public Scanner scan() throws LexicalException {
		int pos = 0;
		State state = State.START;
		int startPos = 0;
		while (pos < chars.length) {
			char ch = chars[pos];
			switch(state) {
				case START: {
					startPos = pos;
					switch (ch) {
						case ' ':
						case '\n':
						case '\r':
						case '\t':
						case '\f': {
							pos++;
						}
						break;
						case EOFChar: {
							tokens.add(new Token(Kind.EOF, startPos, 0));
							pos++; 
						}
						break;
						case '(': {
							tokens.add(new Token(Kind.LPAREN, startPos, pos - startPos + 1));
							pos++;
						}
						break;
						case ')': {
							tokens.add(new Token(Kind.RPAREN, startPos, pos - startPos + 1));
							pos++;
						}
						break;
						case '+': {
							tokens.add(new Token(Kind.OP_PLUS, startPos, pos - startPos + 1));
							pos++;
							state = State.IN_OPERATOR;
						}
						break;
						case '*': {
							tokens.add(new Token(Kind.OP_TIMES, startPos, pos - startPos + 1));
							pos++;
							state = State.IN_OPERATOR;
						}
						break;
						case '-': {
							tokens.add(new Token(Kind.OP_MINUS, startPos, pos - startPos + 1));
							pos++;
							state = State.IN_OPERATOR;
						}
						break;
						default: {
							if(checkForDigit(ch)) {
								temp += ch;
								state = State.IN_DIGIT;
								break;
							}
							else {
								error(pos, line(pos), posInLine(pos), "illegal char");
							}
						}
					}//switch ch
				}
				break;
				
				case IN_OPERATOR: {
					if(ch == '+' || ch == '-' || ch == '*') {
						error(pos, line(pos), posInLine(pos), "illegal char");
					}
					state = state.START;	
				}
				break;
				case IN_DIGIT:{
					if(checkForDigit(ch)) {
						pos++;
					}
					else
					{
						state = State.START;
						try {
							Token temp = new Token(Kind.NUM, startPos, pos - startPos);	
							Integer.parseInt(temp.getText());
							tokens.add(temp);
							}
							catch(NumberFormatException e) {
//								System.out.println("Reached catch of int");
								error(pos,line(pos), posInLine(pos), "Integer out of range");
							}	
					}
				}
				break;
				default: {
					error(pos, 0, 0, "undefined state");
				}
			}// switch state
		} // while
			
		return this;
	}


	private void error(int pos, int line, int posInLine, String message) throws LexicalException {
		String m = (line + 1) + ":" + (posInLine + 1) + " " + message;
		throw new LexicalException(m, pos);
	}

	// Returns true if the internal iterator has more Tokens
	public boolean hasTokens() {
		return nextTokenPos < tokens.size();
	}

	// Returns the next Token with updating internally
	public Token nextToken() {
		return tokens.get(nextTokenPos++);
	}

	// Returns the next token without any updation
	public Token peek() {
		return tokens.get(nextTokenPos);
	}

	
	public void reset() {
		nextTokenPos = 0;
	}

	// Returns a String representation of the list of Tokens and line starts
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Tokens:\n");
		for (int i = 0; i < tokens.size(); i++) {
			sb.append(tokens.get(i)).append('\n');
		}
		sb.append("Line starts:\n");
		for (int i = 0; i < lineStarts.length; i++) {
			sb.append(i).append(' ').append(lineStarts[i]).append('\n');
		}
		return sb.toString();
	}

}
