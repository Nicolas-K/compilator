package compilator;

public class Operator {

    private Token operatorExpression;
    private int priority;

    public Operator(Token operator, int priorityOperator) {
        this.operatorExpression = operator;
        this.priority = priorityOperator;
    }

    public void setOperator(Token operator) {
        this.operatorExpression = operator;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Token getOperator() {
        return this.operatorExpression;
    }

    public int getPriority() {
        return this.priority;
    }
}
