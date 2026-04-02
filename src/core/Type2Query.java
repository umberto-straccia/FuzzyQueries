package core;
import org.semanticweb.owlapi.model.IRI;

import DataProviders.FuzzyDataProvider;

abstract class Type2Query extends FuzzyQuery{

    Quantifier q;
    IRI x; // Class
    IRI L1; // Fuzzy Datatype of G
    IRI d1; // Numeric Property of G
    IRI L2; // Fuzzy Datatype of F
    IRI d2; // Numeric Property of F

    public Type2Query(FuzzyDataProvider fdp, Quantifier q, IRI x, IRI L1, IRI d1, IRI L2, IRI d2) {
        super(fdp);
        this.q = q;
        this.x = x;
        this.L1 = L1;
        this.d1 = d1;
        this.L2 = L2;
        this.d2 = d2;
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

    public IRI getL1() {
        return L1;
    }

    public void setL1(IRI l1) {
        this.L1 = l1;
    }

    public IRI getD1() {
        return d1;
    }

    public void setD1(IRI d1) {
        this.d1 = d1;
    }

    public IRI getL2() {
        return L2;
    }

    public void setL2(IRI l2) {
        L2 = l2;
    }

    public IRI getD2() {
        return d2;
    }

    public void setD2(IRI d2) {
        this.d2 = d2;
    }

    public String toString(){
        String fString;
        String gString;

        //  Some IRIs dont have fragments
        if (L2.getFragment().equals("")) {
            fString = L2.toString();
        } else {
            fString = L2.getFragment();
        }
        if (L1.getFragment().equals("")) {
            gString = L1.toString();
        } else {
            gString = L1.getFragment();
        }

        return q + " of " + fString + " are " + gString;
    }
}
