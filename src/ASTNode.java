package compilerProj;

// Class For AST Node 
public class ASTNode {
	
	public  enum ASTNodeType 
	{ 
		Undefined,OperatorPlus,OperatorMinus,OperatorMul,NumberValue; 
	}
	
	public ASTNodeType Type;
	public int Value;
	public ASTNode	Left;
	public ASTNode	Right;
	
	// Constructor to initialize with default values
	ASTNode(){
		  Type = ASTNodeType.Undefined;
	      Value = 0;
	      Left = null;
	      Right = null;
	}
}
