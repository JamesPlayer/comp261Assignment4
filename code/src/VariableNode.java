import java.util.HashMap;
import java.util.Map;

public class VariableNode extends ExpressionNode {
	
	public static Map<String, Integer> vars = new HashMap<String, Integer>();
	public String varName = null;		

	public VariableNode(String varName) {
		this.varName = varName;
	}

	@Override
	public int getValue(Robot robot) {
		return vars.get(varName);
	}

	@Override
	public String toString() {
		return varName;
	}

}
