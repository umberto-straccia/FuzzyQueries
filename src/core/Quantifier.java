package core;
import FuzzyFunctions.FuzzyConcreteConcept;
import FuzzyFunctions.LeftConcreteConcept;
import FuzzyFunctions.RightConcreteConcept;
import FuzzyFunctions.TriangularConcreteConcept;

public class Quantifier {
    private String desc;
    private FuzzyConcreteConcept func;
    private QuantifierType qType;
    public enum StdQuantifier{FEW,MOST,ALMOST_HALF}

    public Quantifier(String desc, FuzzyConcreteConcept func, QuantifierType qType) {
        this.desc = desc;
        this.func = func;
        this.qType = qType;
    }

    // Posiblemente se acabe quitando el soporte a los Quantificadores Estandar (predeterminados)
    public Quantifier(StdQuantifier q){
        switch (q){
            case FEW:
                this.desc = "Few";
                this.func = new LeftConcreteConcept(0.25,0.5);
                this.qType = QuantifierType.RELATIVE;
                //this.qType = QuantifierType.ABSOLUTE;
                break;
            case MOST:
                this.desc = "Most";
                this.func = new RightConcreteConcept(0.5,0.75);
                this.qType = QuantifierType.RELATIVE;
                break;
            case ALMOST_HALF:
                this.desc = "Almost half";
                this.func = new TriangularConcreteConcept(0.25,0.5,0.75);
                this.qType = QuantifierType.RELATIVE;
                //this.qType = QuantifierType.ABSOLUTE;
                break;
        }
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public FuzzyConcreteConcept getFunc() {
        return func;
    }

    public void setFunc(FuzzyConcreteConcept func) {
        this.func = func;
    }

    public String toString() {
        return this.desc;
    }

    public QuantifierType getqType() {
        return qType;
    }

    public void setqType(QuantifierType qType) {
        this.qType = qType;
    }

}
