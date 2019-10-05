package compilator;

public class Variable extends Symbol {

    private String type;
    private String memory;

    public Variable() {
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
}
