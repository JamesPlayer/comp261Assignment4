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

	static Pattern NUMPAT = Pattern.compile("-?\\d+"); // ("-?(0|[1-9][0-9]*)");
	static Pattern OPENPAREN = Pattern.compile("\\(");
	static Pattern CLOSEPAREN = Pattern.compile("\\)");
	static Pattern OPENBRACE = Pattern.compile("\\{");
	static Pattern CLOSEBRACE = Pattern.compile("\\}");
	static Pattern SENSORPAT = Pattern.compile("fuelLeft|oppLR|oppFB|numBarrels|barrelLR|barrelFB|wallDist");

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
	 * STMT  ::= ACT ; | LOOP 
	 */
	static RobotProgramNode parseStatement(Scanner s) {
		if (s.hasNext("loop")) return parseLoop(s);
		else if (s.hasNext("if")) return parseIf(s);
		else if (s.hasNext("while")) return parseWhile(s);
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
		
		fail("Action was not one of move, turnL, turnR, takeFuel, wait", s);
		return null;
	}
	
	/**
	 * IF  ::= if ( COND ) BLOCK [ else BLOCK ]
	 */
	static RobotProgramNode parseIf(Scanner s) {
		require("if", "No 'if'", s);
		require(OPENPAREN, "Expected (", s);
		ConditionNode condition = parseCondition(s);
		require(CLOSEPAREN, "Expected )", s);
		BlockNode block = parseBlock(s);
		
		// Optional else statement
		if (s.hasNext("else")) {
			require("else", "If: No 'else'", s);
			return new IfNode(condition, block, parseBlock(s));
		} else {				
			return new IfNode(condition, block);
		}	
	}
	
	/**
	 * WHILE ::= while ( COND ) BLOCK
	 */
	static RobotProgramNode parseWhile(Scanner s) {
		require("while", "No 'while'", s);
		require(OPENPAREN, "Expected (", s);
		ConditionNode condition = parseCondition(s);
		require(CLOSEPAREN, "Expected )", s);
		BlockNode block = parseBlock(s);
		return new WhileNode(condition, block);
	}
	
	/**
	 * COND  ::= lt ( SEN, NUM )  | gt ( SEN, NUM )  | eq ( SEN, NUM )
	 */
	static ConditionNode parseCondition(Scanner s) {
		if (s.hasNext("lt")) return parseLt(s);
		else if (s.hasNext("gt")) return parseGt(s);
		else if (s.hasNext("eq")) return parseEq(s);
		fail("Operator was not one of 'lt', 'gt', 'eq'", s);
		return null;
	}
	
	/**
	 * lt ( SEN, NUM )
	 */
	static ConditionNode parseLt(Scanner s) {
		
		SensorNode sensorVal = null;
		NumberNode number = null;
		
		require("lt", "Expected lt", s);
		require(OPENPAREN, "Expected (", s);
		sensorVal = parseSensor(s);
		require(",", "Expected ,", s);
		number = parseNumber(s);
		
		require(CLOSEPAREN, "Expected )", s);
		return new LtNode(sensorVal, number);	
	}
	
	/**
	 * gt ( SEN, NUM )
	 */
	static ConditionNode parseGt(Scanner s) {
		
		SensorNode sensorVal = null;
		NumberNode number = null;
		
		require("gt", "Expected gt", s);
		require(OPENPAREN, "Expected (", s);
		sensorVal = parseSensor(s);
		require(",", "Expected ,", s);
		number = parseNumber(s);
		
		require(CLOSEPAREN, "Expected )", s);
		return new GtNode(sensorVal, number);	
	}
	
	/**
	 * eq ( SEN, NUM )
	 */
	static ConditionNode parseEq(Scanner s) {
		
		SensorNode sensorVal = null;
		NumberNode number = null;
		
		require("eq", "Expected eq", s);
		require(OPENPAREN, "Expected (", s);
		sensorVal = parseSensor(s);
		require(",", "Expected ,", s);
		number = parseNumber(s);
		
		require(CLOSEPAREN, "Expected )", s);
		return new EqNode(sensorVal, number);	
	}
	
	static NumberNode parseNumber(Scanner s) {
		if (s.hasNext(NUMPAT)) return new NumberNode(Integer.parseInt(s.next(NUMPAT)));
		fail("Not a number", s);
		return null;
	}
	
	/**
	 * EXP  ::= NUM | SEN | OP ( EXP, EXP )
	 */
	static ExpressionNode parseExpression(Scanner s) {
		if (s.hasNext(NUMPAT)) return parseNumber(s);
		if (s.hasNext(SENSORPAT)) return parseSensor(s);
		
		fail("Expression is not a number or sensor", s);
		return null;
	}
	
	
	/**
	 * SEN   ::= fuelLeft | oppLR | oppFB | numBarrels |
     *    barrelLR | barrelFB | wallDist
	 */
	static SensorNode parseSensor(Scanner s) {
		
		SensorNode node = null;
		
		if (checkFor("fuelLeft", s)) node = new FuelLeftNode();
		else if (checkFor("oppLR", s)) node = new OppLRNode();
		else if (checkFor("oppFB", s)) node = new OppFBNode();
		else if (checkFor("numBarrels", s)) node = new NumBarrelsNode();
		else if (checkFor("barrelLR", s)) node = new BarrelLRNode();
		else if (checkFor("barrelFB", s)) node = new BarrelFBNode();
		else if (checkFor("wallDist", s)) node = new WallDistNode();
		
		if (node != null) {
			return node;
		}
		
		fail("Sensor was not one of fuelLeft, oppLR, oppFB, numBarrels, barrelLR, barrelFB, wallDist", s);
		return null;
		
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
