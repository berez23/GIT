package it.webred.mui.model;
// Generated 1-apr-2009 13.23.55 by Hibernate Tools 3.1.0.beta4

import java.util.HashSet;
import java.util.Set;


/**
 * MiDupUser generated by hbm2java
 */

public class MiDupUser  implements java.io.Serializable {


    // Fields    

     private String login;
     private String password;
     private Set<MiDupUserRole> miDupUserRoles = new HashSet<MiDupUserRole>(0);


    // Constructors

    /** default constructor */
    public MiDupUser() {
    }

	/** minimal constructor */
    public MiDupUser(String login, String password) {
        this.login = login;
        this.password = password;
    }
    
    /** full constructor */
    public MiDupUser(String login, String password, Set<MiDupUserRole> miDupUserRoles) {
        this.login = login;
        this.password = password;
        this.miDupUserRoles = miDupUserRoles;
    }
    

   
    // Property accessors

    public String getLogin() {
        return this.login;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public Set<MiDupUserRole> getMiDupUserRoles() {
        return this.miDupUserRoles;
    }
    
    public void setMiDupUserRoles(Set<MiDupUserRole> miDupUserRoles) {
        this.miDupUserRoles = miDupUserRoles;
    }
   








}