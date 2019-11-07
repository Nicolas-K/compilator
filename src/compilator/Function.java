package compilator;

public class Function extends Symbol {

    private String type;
    private int label;

    public Function() {
        super();
        this.type = null;
        this.label = -1;
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
}
