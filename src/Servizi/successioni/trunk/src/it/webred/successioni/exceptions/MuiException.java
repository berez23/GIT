package it.webred.successioni.exceptions;

  public class MuiException extends java.lang.Exception {

     private String messaggioErrore_;

     public MuiException() {
      super();
    }

     public MuiException(String messaggioErrore) {
        super();
        messaggioErrore_ = messaggioErrore;
      }

     public String getMessaggioErrore()
     {
       return messaggioErrore_;
     }

  }
