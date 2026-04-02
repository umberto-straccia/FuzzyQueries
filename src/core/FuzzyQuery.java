package core;
import DataProviders.FuzzyDataProvider;

public abstract class FuzzyQuery {

    FuzzyDataProvider fdp;
    String method = null;

    public FuzzyQuery(FuzzyDataProvider fdp) {
        this.fdp = fdp;
    }

    public abstract double run(boolean verbose) throws Exception;

    public double run() throws Exception {
        return run(false);
    }
    
    public abstract String toString();

    public String getMethod(){
        return this.method;
    }

    public FuzzyDataProvider getFdp() {
        return fdp;
    }

    public void setFdp(FuzzyDataProvider fdp) {
        this.fdp = fdp;
    }
}
