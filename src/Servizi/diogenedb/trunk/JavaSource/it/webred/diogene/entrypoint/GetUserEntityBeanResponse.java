/**
 * GetUserEntityBeanResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.webred.diogene.entrypoint;

public class GetUserEntityBeanResponse  implements java.io.Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -8549908407509960992L;
	private it.webred.diogene.client.UserEntityBeanSer getUserEntityBeanReturn;

    public GetUserEntityBeanResponse() {
    }

    public GetUserEntityBeanResponse(
           it.webred.diogene.client.UserEntityBeanSer getUserEntityBeanReturn) {
           this.getUserEntityBeanReturn = getUserEntityBeanReturn;
    }


    /**
     * Gets the getUserEntityBeanReturn value for this GetUserEntityBeanResponse.
     * 
     * @return getUserEntityBeanReturn
     */
    public it.webred.diogene.client.UserEntityBeanSer getGetUserEntityBeanReturn() {
        return getUserEntityBeanReturn;
    }


    /**
     * Sets the getUserEntityBeanReturn value for this GetUserEntityBeanResponse.
     * 
     * @param getUserEntityBeanReturn
     */
    public void setGetUserEntityBeanReturn(it.webred.diogene.client.UserEntityBeanSer getUserEntityBeanReturn) {
        this.getUserEntityBeanReturn = getUserEntityBeanReturn;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetUserEntityBeanResponse)) return false;
        GetUserEntityBeanResponse other = (GetUserEntityBeanResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getUserEntityBeanReturn==null && other.getGetUserEntityBeanReturn()==null) || 
             (this.getUserEntityBeanReturn!=null &&
              this.getUserEntityBeanReturn.equals(other.getGetUserEntityBeanReturn())));
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
        if (getGetUserEntityBeanReturn() != null) {
            _hashCode += getGetUserEntityBeanReturn().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetUserEntityBeanResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://entrypoint.diogene.webred.it", ">getUserEntityBeanResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getUserEntityBeanReturn");
        elemField.setXmlName(new javax.xml.namespace.QName("http://entrypoint.diogene.webred.it", "getUserEntityBeanReturn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://127.0.0.1:8080/diogenedb/services/UserEntities", "UserEntityBeanSer"));
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
