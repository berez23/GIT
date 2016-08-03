package it.webred.diogene.db.model;
// Generated 22-nov-2007 11.42.26 by Hibernate Tools 3.1.0.beta4



/**
 * DvLinkClasse generated by hbm2java
 */

public class DvLinkClasse  implements java.io.Serializable {


    // Fields    

     private Long id;
     private DvLink dvLink;
     private DvClasse dvClasseByFkDvClasse1;
     private DvClasse dvClasseByFkDvClasse2;
     private String name;


    // Constructors

    /** default constructor */
    public DvLinkClasse() {
    }

	/** minimal constructor */
    public DvLinkClasse(Long id) {
        this.id = id;
    }
    
    /** full constructor */
    public DvLinkClasse(Long id, DvLink dvLink, DvClasse dvClasseByFkDvClasse1, DvClasse dvClasseByFkDvClasse2, String name) {
        this.id = id;
        this.dvLink = dvLink;
        this.dvClasseByFkDvClasse1 = dvClasseByFkDvClasse1;
        this.dvClasseByFkDvClasse2 = dvClasseByFkDvClasse2;
        this.name = name;
    }
    

   
    // Property accessors

    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public DvLink getDvLink() {
        return this.dvLink;
    }
    
    public void setDvLink(DvLink dvLink) {
        this.dvLink = dvLink;
    }

    public DvClasse getDvClasseByFkDvClasse1() {
        return this.dvClasseByFkDvClasse1;
    }
    
    public void setDvClasseByFkDvClasse1(DvClasse dvClasseByFkDvClasse1) {
        this.dvClasseByFkDvClasse1 = dvClasseByFkDvClasse1;
    }

    public DvClasse getDvClasseByFkDvClasse2() {
        return this.dvClasseByFkDvClasse2;
    }
    
    public void setDvClasseByFkDvClasse2(DvClasse dvClasseByFkDvClasse2) {
        this.dvClasseByFkDvClasse2 = dvClasseByFkDvClasse2;
    }

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
   








}