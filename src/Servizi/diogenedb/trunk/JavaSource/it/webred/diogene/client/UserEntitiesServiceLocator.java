/**
 * UserEntitiesServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.webred.diogene.client;

public class UserEntitiesServiceLocator extends org.apache.axis.client.Service implements it.webred.diogene.client.UserEntitiesService {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4422709010490544739L;

	public UserEntitiesServiceLocator() {
    }


    public UserEntitiesServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public UserEntitiesServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for UserEntities
    private java.lang.String UserEntities_address = "http://127.0.0.1:8080/diogenedb/services/UserEntities";

    public java.lang.String getUserEntitiesAddress() {
        return UserEntities_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String UserEntitiesWSDDServiceName = "UserEntities";

    public java.lang.String getUserEntitiesWSDDServiceName() {
        return UserEntitiesWSDDServiceName;
    }

    public void setUserEntitiesWSDDServiceName(java.lang.String name) {
        UserEntitiesWSDDServiceName = name;
    }

    public it.webred.diogene.client.UserEntities getUserEntities() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(UserEntities_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getUserEntities(endpoint);
    }

    public it.webred.diogene.client.UserEntities getUserEntities(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            it.webred.diogene.client.UserEntitiesSoapBindingStub _stub = new it.webred.diogene.client.UserEntitiesSoapBindingStub(portAddress, this);
            _stub.setPortName(getUserEntitiesWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setUserEntitiesEndpointAddress(java.lang.String address) {
        UserEntities_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (it.webred.diogene.client.UserEntities.class.isAssignableFrom(serviceEndpointInterface)) {
                it.webred.diogene.client.UserEntitiesSoapBindingStub _stub = new it.webred.diogene.client.UserEntitiesSoapBindingStub(new java.net.URL(UserEntities_address), this);
                _stub.setPortName(getUserEntitiesWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("UserEntities".equals(inputPortName)) {
            return getUserEntities();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://127.0.0.1:8080/diogenedb/services/UserEntities", "UserEntitiesService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://127.0.0.1:8080/diogenedb/services/UserEntities", "UserEntities"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("UserEntities".equals(portName)) {
            setUserEntitiesEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
