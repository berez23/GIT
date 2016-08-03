package it.webred.successioni.output;

	public class Indirizzo<E,F>{
	    private E sed;
	    private F ind;

	    public Indirizzo(E sed, F ind){
	        this.ind = ind;
	        this.sed = sed;
	    }

	    public F getInd(){return ind;}
	    public E getSed(){return sed;}

	    public String toString(){
	        return "("+ ind + "," + sed + ")";
	    }
	}