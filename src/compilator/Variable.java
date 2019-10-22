package compilator;

public class Variable extends Symbol {

    private String type;
    private String memory;

    public Variable() {
        super();
        type = null;
        memory = null;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getType() {
        return this.type;
    }

    public String getMemory() {
        return this.memory;
    }

    public void printVariable() {
        super.printSymbol("Variable");
        System.out.printf("[Variable] | Type: %s\n", getType());
        System.out.printf("[Variable] | Memory: %s\n", getMemory());
    }
}
