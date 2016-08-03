package it.webred.diogene.client;

public class UserEntitiesProxy implements it.webred.diogene.client.UserEntities {
  private String _endpoint = null;
  private it.webred.diogene.client.UserEntities userEntities = null;
  
  public UserEntitiesProxy() {
    _initUserEntitiesProxy();
  }
  
  public UserEntitiesProxy(String endpoint) {
    _endpoint = endpoint;
    _initUserEntitiesProxy();
  }
  
  private void _initUserEntitiesProxy() {
    try {
      userEntities = (new it.webred.diogene.client.UserEntitiesServiceLocator()).getUserEntities();
      if (userEntities != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)userEntities)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)userEntities)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (userEntities != null)
      ((javax.xml.rpc.Stub)userEntities)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  

  
  public it.webred.diogene.client.UserEntitiesBean getUserEntities() throws java.rmi.RemoteException{
    if (userEntities == null)
      _initUserEntitiesProxy();
    return userEntities.getUserEntities();
  }
  
  public it.webred.diogene.client.UserEntityBeanSer getUserEntityBean(long entityId) throws java.rmi.RemoteException{
    if (userEntities == null)
      _initUserEntitiesProxy();
    return userEntities.getUserEntityBean(entityId);
  }
  
  
}