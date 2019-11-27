package compilator;

public class ProcedureProgram extends Symbol {

    private int label;

    public ProcedureProgram() {
        super();
        label = -1;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public int getLabel() {
        return this.label;
    }
}
