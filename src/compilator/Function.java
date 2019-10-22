package compilator;

public class Function extends Symbol {

    private String type;

    public Function() {
        super();
        this.type = null;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void printFunction() {
        super.printSymbol("Function");
        //System.out.printf("[Function] | Type: %s\n", getType());
    }
}
