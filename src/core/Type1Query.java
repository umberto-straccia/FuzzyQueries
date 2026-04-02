package core;
import org.semanticweb.owlapi.model.IRI;

import DataProviders.FuzzyDataProvider;

abstract class Type1Query extends FuzzyQuery{

    Quantifier q;
    IRI x;  // Class
    IRI L;  // Fuzzy Datatype
    IRI d;  // Numeric Property

    public Type1Query(FuzzyDataProvider fdp) {
        super(fdp);
    }

    // Constructor with query parameters built-in
    public Type1Query(FuzzyDataProvider fdp, Quantifier q, IRI x, IRI L, IRI d) {
        super(fdp);
        this.q = q;
        this.x = x;
        this.L = L;
        this.d = d;
    }

    public Quantifier getQ() {
        return q;
    }

    public void setQ(Quantifier q) {
        this.q = q;
    }

    public IRI getX() {
        return x;
    }

    public void setX(IRI x) {
        this.x = x;
    }

    public IRI getL() {
        return L;
    }

    public void setL(IRI l) {
        this.L = l;
    }
    
    public String toString(){
        String xString;
        String gString;

        //  Some IRIs dont have fragments
        if (x.getFragment().equals("")) {
            xString = x.toString();
        } else {
            xString = x.getFragment();
        }

        if (L.getFragment().equals("")) {
            gString = L.toString();
        } else {
            gString = L.getFragment();
        }

        return q + " of " + xString + " are " + gString;
    }
}
