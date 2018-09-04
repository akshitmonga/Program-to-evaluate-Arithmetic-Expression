package compilerProj;

import compilerProj.ASTNode.ASTNodeType;
import compilerProj.Parser.SyntaxException;
import compilerProj.Scanner.Token;

// Class to visit AST Node and to Evaluate the Expression

public class Evaluator {

	public static int EvaluateSubtree(ASTNode ast) throws SyntaxException
	   {
	      if(ast == null) 
	    	  	throw new SyntaxException(null,"Incorrect syntax tree!");
//	         throw SyntaxException("Incorrect syntax tree!");
	 
	      if(ast.Type == ASTNodeType.NumberValue)
	         return ast.Value;
	      else 
	      {
	         int v1 = EvaluateSubtree(ast.Left);
	         int v2 = EvaluateSubtree(ast.Right);
	         switch(ast.Type)
	         {
	         case OperatorPlus:  return v1 + v2;
	         case OperatorMinus: return v1 - v2;
	         case OperatorMul:   return v1 * v2;
			default:
				break;
	         }
	      }
	 
	      throw new SyntaxException(null,"Incorrect syntax tree!");
	   }
	
	public static int Evaluate(ASTNode ast) throws SyntaxException
	   {
	      if(ast == null)
	    	  	throw new SyntaxException(null,"Incorrect abstract syntax tree!");
	 
	      return EvaluateSubtree(ast);
	   }
	
	
}
