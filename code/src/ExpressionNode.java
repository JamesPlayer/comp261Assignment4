import java.util.ArrayList;

public abstract class ExpressionNode extends Node {

	@Override
	public void execute(Robot robot) {
		// Does nothing
	}

	public abstract int getValue(Robot robot);
}
