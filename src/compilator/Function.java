package compilator;

public class Function extends Symbol {

    private String type;
    private int label;
    private boolean hasReturn;

    public Function() {
        super();
        this.type = null;
        this.label = -1;
        hasReturn = false;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public int getLabel() {
        return this.label;
    }

    public void setReturnFunction(boolean returnFunction) {
        this.hasReturn = returnFunction;
    }

    public boolean getReturnFunction() {
        return this.hasReturn;
    }
}
