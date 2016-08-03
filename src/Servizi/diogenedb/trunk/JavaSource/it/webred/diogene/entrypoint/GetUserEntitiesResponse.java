/**
 * GetUserEntitiesResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.webred.diogene.entrypoint;

public class GetUserEntitiesResponse  implements java.io.Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5820182924807002304L;
	private it.webred.diogene.client.UserEntitiesBean getUserEntitiesReturn;

    public GetUserEntitiesResponse() {
    }

    public GetUserEntitiesResponse(
           it.webred.diogene.client.UserEntitiesBean getUserEntitiesReturn) {
           this.getUserEntitiesReturn = getUserEntitiesReturn;
    }


    /**
     * Gets the getUserEntitiesReturn value for this GetUserEntitiesResponse.
     * 
     * @return getUserEntitiesReturn
     */
    public it.webred.diogene.client.UserEntitiesBean getGetUserEntitiesReturn() {
        return getUserEntitiesReturn;
    }


    /**
     * Sets the getUserEntitiesReturn value for this GetUserEntitiesResponse.
     * 
     * @param getUserEntitiesReturn
     */
    public void setGetUserEntitiesReturn(it.webred.diogene.client.UserEntitiesBean getUserEntitiesReturn) {
        this.getUserEntitiesReturn = getUserEntitiesReturn;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetUserEntitiesResponse)) return false;
        GetUserEntitiesResponse other = (GetUserEntitiesResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getUserEntitiesReturn==null && other.getGetUserEntitiesReturn()==null) || 
             (this.getUserEntitiesReturn!=null &&
              this.getUserEntitiesReturn.equals(other.getGetUserEntitiesReturn())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getGetUserEntitiesReturn() != null) {
            _hashCode += getGetUserEntitiesReturn().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetUserEntitiesResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://entrypoint.diogene.webred.it", ">getUserEntitiesResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getUserEntitiesReturn");
        elemField.setXmlName(new javax.xml.namespace.QName("http://entrypoint.diogene.webred.it", "getUserEntitiesReturn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://127.0.0.1:8080/diogenedb/services/UserEntities", "UserEntitiesBean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
