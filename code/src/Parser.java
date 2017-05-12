import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.*;
import javax.swing.JFileChooser;

/**
 * The parser and interpreter. The top level parse function, a main method for
 * testing, and several utility methods are provided. You need to implement
 * parseProgram and all the rest of the parser.
 */
public class Parser {

	/**
	 * Top level parse method, called by the World
	 */
	static RobotProgramNode parseFile(File code) {
		Scanner scan = null;
		try {
			scan = new Scanner(code);

			// the only time tokens can be next to each other is
			// when one of them is one of (){},;
			scan.useDelimiter("\\s+|(?=[{}(),;])|(?<=[{}(),;])");

			RobotProgramNode n = parseProgram(scan); // You need to implement this!!!

			scan.close();
			return n;
		} catch (FileNotFoundException e) {
			System.out.println("Robot program source file not found");
		} catch (ParserFailureException e) {
			System.out.println("Parser error:");
			System.out.println(e.getMessage());
			scan.close();
		}
		return null;
	}

	/** For testing the parser without requiring the world */

	public static void main(String[] args) {
		if (args.length > 0) {
			for (String arg : args) {
				File f = new File(arg);
				if (f.exists()) {
					System.out.println("Parsing '" + f + "'");
					RobotProgramNode prog = parseFile(f);
					System.out.println("Parsing completed ");
					if (prog != null) {
						System.out.println("================\nProgram:");
						System.out.println(prog);
					}
					System.out.println("=================");
				} else {
					System.out.println("Can't find file '" + f + "'");
				}
			}
		} else {
			while (true) {
				JFileChooser chooser = new JFileChooser(".");// System.getProperty("user.dir"));
				int res = chooser.showOpenDialog(null);
				if (res != JFileChooser.APPROVE_OPTION) {
					break;
				}
				RobotProgramNode prog = parseFile(chooser.getSelectedFile());
				System.out.println("Parsing completed");
				if (prog != null) {
					System.out.println("Program: \n" + prog);
				}
				System.out.println("=================");
			}
		}
		System.out.println("Done");
	}

	// Useful Patterns

	static Pattern NUMPAT = Pattern.compile("-?[1-9][0-9]*|0");
	static Pattern OPENPAREN = Pattern.compile("\\(");
	static Pattern CLOSEPAREN = Pattern.compile("\\)");
	static Pattern OPENBRACE = Pattern.compile("\\{");
	static Pattern CLOSEBRACE = Pattern.compile("\\}");
	static Pattern CONDITIONPAT = Pattern.compile("and|or|not|lt|gt|eq");
	static Pattern BOOLCONDITIONPAT = Pattern.compile("and|or|not");
	static Pattern NUMERICCONDITIONPAT = Pattern.compile("lt|gt|eq");
	static Pattern SENSORPAT = Pattern.compile("fuelLeft|oppLR|oppFB|numBarrels|barrelLR|barrelFB|wallDist");
	static Pattern OPERATORPAT = Pattern.compile("add|sub|mul|div");
	static Pattern VARPAT = Pattern.compile("\\$[A-Za-z][A-Za-z0-9]*");

	/**
	 * PROG ::= STMT+
	 */
	static RobotProgramNode parseProgram(Scanner s) {
		ProgramNode programNode = new ProgramNode();
		while (s.hasNext()) {
			programNode.children.add(parseStatement(s));
		}

		return programNode;
	}
	
	/**
	 * STMT  ::= ACT ; | LOOP | IF | WHILE | ASSGN ;
	 */
	static RobotProgramNode parseStatement(Scanner s) {
		if (s.hasNext("loop")) return parseLoop(s);
		else if (s.hasNext("if")) return parseIf(s);
		else if (s.hasNext("while")) return parseWhile(s);
		else if (s.hasNext(VARPAT)) return parseAssignment(s);
		else return parseAction(s);
	}
	
	/**
	 * LOOP  ::= loop BLOCK 
	 */
	static LoopNode parseLoop(Scanner s) {
		require("loop", "Missing 'loop'", s);
		return new LoopNode(parseBlock(s));
	}
	
	/**
	 * BLOCK  ::= { STMT+ }
	 */
	static BlockNode parseBlock(Scanner s) {
		BlockNode blockNode = new BlockNode();
		require("\\{", "Block: Missing '{'", s);
		while (s.hasNext() && !s.hasNext("\\}")) {
			blockNode.children.add(parseStatement(s));
		}
		require("\\}", "Block: Missing '}'", s);
		
		if (blockNode.children.isEmpty()) fail("Empty Block", s);
		
		return blockNode;
	}
	
	/**
	 * ACT  ::= move [ ( EXP ) ] | turnL | turnR | turnAround | shieldOn |
     *    shieldOff | takeFuel | wait
	 */
	static RobotProgramNode parseAction(Scanner s) {
		
		RobotProgramNode node = null;
		
		if (checkFor("move", s)) {
			// Optional argument
			if (s.hasNext(OPENPAREN)) {
				require(OPENPAREN, "Move: No (", s);
				node = new MoveNode(parseExpression(s));
				require(CLOSEPAREN, "Move: No )", s);
			} else {				
				node = new MoveNode();
			}
		}
		else if (checkFor("turnL", s)) node = new TurnLNode();
		else if (checkFor("turnR", s)) node = new TurnRNode();
		else if (checkFor("turnAround", s)) node = new TurnAroundNode();
		else if (checkFor("shieldOn", s)) node = new ShieldOnNode();
		else if (checkFor("shieldOff", s)) node = new ShieldOffNode();
		else if (checkFor("takeFuel", s)) node = new TakeFuelNode();
		else if (checkFor("wait", s)) {
			// Optional argument
			if (s.hasNext(OPENPAREN)) {
				require(OPENPAREN, "Wait: No (", s);
				node = new WaitNode(parseExpression(s));
				require(CLOSEPAREN, "Wait: No )", s);
			} else {				
				node = new WaitNode();
			}
		}
		
		if (node != null) {
			require(";", "Missing ';'", s);
			return node;
		}
		
		fail("Action was not one of turnL, turnR, turnAround, shieldOn, shieldOff, takeFuel, wait", s);
		return null;
	}
	
	/**
	 * IF  ::= if ( COND ) BLOCK [elif ( COND ) BLOCK]* [else BLOCK]
	 */
	static IfNode parseIf(Scanner s) {
		require("if", "No 'if'", s);
		require(OPENPAREN, "Expected (", s);
		ConditionNode condition = parseCondition(s);
		require(CLOSEPAREN, "Expected )", s);
		BlockNode block = parseBlock(s);
		Map<ConditionNode, BlockNode> elifs = new HashMap<ConditionNode, BlockNode>();
		BlockNode elseBlock = null;
		
		// Optional elif statements
		while (s.hasNext("elif")) {
			elifs.put(parseCondition(s), parseBlock(s));
		}
		
		
		// Optional else statement
		if (s.hasNext("else")) {
			require("else", "If: No 'else'", s);
			elseBlock = parseBlock(s);
		}				
		
		return new IfNode(condition, block, elifs, elseBlock);
			
	}
	
	/**
	 * WHILE ::= while ( COND ) BLOCK
	 */
	static WhileNode parseWhile(Scanner s) {
		require("while", "No 'while'", s);
		require(OPENPAREN, "While: Expected (", s);
		ConditionNode condition = parseCondition(s);
		require(CLOSEPAREN, "While: Expected )", s);
		BlockNode block = parseBlock(s);
		return new WhileNode(condition, block);
	}
	
	/**
	 * COND  ::= and ( COND, COND ) | or ( COND, COND ) | not ( COND )  | 
          lt ( EXP, EXP )  | gt ( EXP, EXP )  | eq ( EXP, EXP ) 
	 */
	static ConditionNode parseCondition(Scanner s) {
		String operator = null;
		
		try {			
			operator = s.next(CONDITIONPAT);
		} catch (InputMismatchException e) {
			fail("Condition: Operator was not one and, or, not, lt, gt, eq", s);
		}
		
		ExpressionNode e1 = null, e2 = null;
		ConditionNode c1 = null, c2 = null;
		
		require(OPENPAREN, "Condition: expected '('", s);
		
		if (operator.matches(BOOLCONDITIONPAT.toString())) {
			c1 = parseCondition(s);
		} else if (operator.matches(NUMERICCONDITIONPAT.toString())) {
			e1 = parseExpression(s);
		} else {
			fail("Condition: Expected first condition statement", s);
		}
		
		if (!operator.equals("not")) {			
			require(",", "Condition: expected ','", s);
			
			if (operator.matches(BOOLCONDITIONPAT.toString())) {
				c2 = parseCondition(s);
			} else if (operator.matches(NUMERICCONDITIONPAT.toString())) {
				e2 = parseExpression(s);
			} else {
				fail("Condition: Expected second condition statement", s);
			}
		}
	
		require(CLOSEPAREN, "Condition: expected ')'", s);
		
		switch (operator) {
			case "and":
				return new AndNode(c1, c2);
			case "or":
				return new OrNode(c1, c2);
			case "not":
				return new NotNode(c1);
			case "lt":
				return new LtNode(e1, e2);
			case "gt":
				return new GtNode(e1, e2);
			case "eq":
				return new EqNode(e1, e2);
			default:
				fail("Expected one of add, sub, mul, div", s);
		}
		
		return null;
	}
	
	/**
	 * OP    ::= add (EXP, EXP) | sub (EXP, EXP) | mul (EXP, EXP) | div (EXP, EXP)
	 */
	static OperatorNode parseOperator(Scanner s) {
		String operator = s.next(OPERATORPAT);
		require(OPENPAREN, "Operator: expected '('", s);
		ExpressionNode e1 = parseExpression(s);
		require(",", "Operator: exptected ','", s);
		ExpressionNode e2 = parseExpression(s);
		require(CLOSEPAREN, "Operator: expected ')'", s);
		
		switch (operator) {
			case "add":
				return new AddNode(e1, e2);
			case "sub":
				return new SubNode(e1, e2);
			case "mul":
				return new MulNode(e1, e2);
			case "div":
				return new DivNode(e1, e2);
			default:
				fail("Expected one of add, sub, mul, div", s);
		}
		
		return null;
	}
	
	static NumberNode parseNumber(Scanner s) {
		if (s.hasNext(NUMPAT)) return new NumberNode(Integer.parseInt(s.next(NUMPAT)));
		fail("Not a number", s);
		return null;
	}
	
	/**
	 * EXP   ::= NUM | SEN | VAR | OP ( EXP, EXP )
	 */
	static ExpressionNode parseExpression(Scanner s) {
		if (s.hasNext(NUMPAT)) return parseNumber(s);
		if (s.hasNext(VARPAT)) return parseVariable(s);
		if (s.hasNext(SENSORPAT)) return parseSensor(s);
		if (s.hasNext(OPERATORPAT)) return parseOperator(s);
		
		fail("Expression is not a number, sensor, variable or operator", s);
		return null;
	}
	
	
	/**
	 * SEN   ::= fuelLeft | oppLR | oppFB | numBarrels | barrelLR [( EXP )] | barrelFB [ ( EXP ) ] | wallDist
	 */
	static SensorNode parseSensor(Scanner s) {
		
		SensorNode node = null;
		
		if (checkFor("barrelLR", s)) {
			// Optional argument
			if (s.hasNext(OPENPAREN)) {
				require(OPENPAREN, "barrelLR: No (", s);
				node = new BarrelLRNode(parseExpression(s));
				require(CLOSEPAREN, "barrelLR: No )", s);
			} else {				
				node = new BarrelLRNode(null);
			}
		} 
		else if (checkFor("barrelFB", s)) {
			// Optional argument
			if (s.hasNext(OPENPAREN)) {
				require(OPENPAREN, "barrelFB: No (", s);
				node = new BarrelFBNode(parseExpression(s));
				require(CLOSEPAREN, "barrelFB: No )", s);
			} else {				
				node = new BarrelFBNode(null);
			}
		}
		else if (checkFor("fuelLeft", s)) node = new FuelLeftNode();
		else if (checkFor("oppLR", s)) node = new OppLRNode();
		else if (checkFor("oppFB", s)) node = new OppFBNode();
		else if (checkFor("numBarrels", s)) node = new NumBarrelsNode();
		else if (checkFor("wallDist", s)) node = new WallDistNode();
		
		if (node != null) {
			return node;
		}
		
		fail("Sensor was not one of fuelLeft, oppLR, oppFB, numBarrels, barrelLR, barrelFB, wallDist", s);
		return null;
		
	}
	
	/**
	 * VAR   ::= "\\$[A-Za-z][A-Za-z0-9]*"
	 */
	static VariableNode parseVariable(Scanner s) {
		
		String varName = "";
		
		try {			
			varName = s.next(VARPAT);
		} catch (InputMismatchException e) {
			fail("Condition: Variable has incorrect format", s);
		}
					
		return new VariableNode(varName);
	}
	
	/**
	 * ASSGN ::= VAR = EXP
	 */
	static AssignmentNode parseAssignment(Scanner s) {
		VariableNode varNode = parseVariable(s);
		require("=", "Assignment: expecting =", s);
		ExpressionNode exp = parseExpression(s);
		require(";", "Assignment: expecting ;", s);
		return new AssignmentNode(varNode, exp);
	}

	// utility methods for the parser

	/**
	 * Report a failure in the parser.
	 */
	static void fail(String message, Scanner s) {
		String msg = message + "\n   @ ...";
		for (int i = 0; i < 5 && s.hasNext(); i++) {
			msg += " " + s.next();
		}
		throw new ParserFailureException(msg + "...");
	}

	/**
	 * Requires that the next token matches a pattern if it matches, it consumes
	 * and returns the token, if not, it throws an exception with an error
	 * message
	 */
	static String require(String p, String message, Scanner s) {
		if (s.hasNext(p)) {
			return s.next();
		}
		fail(message, s);
		return null;
	}

	static String require(Pattern p, String message, Scanner s) {
		if (s.hasNext(p)) {
			return s.next();
		}
		fail(message, s);
		return null;
	}

	/**
	 * Requires that the next token matches a pattern (which should only match a
	 * number) if it matches, it consumes and returns the token as an integer if
	 * not, it throws an exception with an error message
	 */
	static int requireInt(String p, String message, Scanner s) {
		if (s.hasNext(p) && s.hasNextInt()) {
			return s.nextInt();
		}
		fail(message, s);
		return -1;
	}

	static int requireInt(Pattern p, String message, Scanner s) {
		if (s.hasNext(p) && s.hasNextInt()) {
			return s.nextInt();
		}
		fail(message, s);
		return -1;
	}

	/**
	 * Checks whether the next token in the scanner matches the specified
	 * pattern, if so, consumes the token and return true. Otherwise returns
	 * false without consuming anything.
	 */
	static boolean checkFor(String p, Scanner s) {
		if (s.hasNext(p)) {
			s.next();
			return true;
		} else {
			return false;
		}
	}

	static boolean checkFor(Pattern p, Scanner s) {
		if (s.hasNext(p)) {
			s.next();
			return true;
		} else {
			return false;
		}
	}

}

// You could add the node classes here, as long as they are not declared public (or private)
