/**
 * UserEntities.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.webred.diogene.client;

public interface UserEntities extends java.rmi.Remote {
    public it.webred.diogene.client.UserEntitiesBean getUserEntities() throws java.rmi.RemoteException;
    public it.webred.diogene.client.UserEntityBeanSer getUserEntityBean(long entityId) throws java.rmi.RemoteException;
}
