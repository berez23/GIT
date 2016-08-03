/**
 * UserEntityBeanSer.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.webred.diogene.client;

public class UserEntityBeanSer  implements java.io.Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4296408929532399498L;
	private byte[] userEntityBean;

    public UserEntityBeanSer() {
    }

    public UserEntityBeanSer(
           byte[] userEntityBean) {
           this.userEntityBean = userEntityBean;
    }


    /**
     * Gets the userEntityBean value for this UserEntityBeanSer.
     * 
     * @return userEntityBean
     */
    public byte[] getUserEntityBean() {
        return userEntityBean;
    }


    /**
     * Sets the userEntityBean value for this UserEntityBeanSer.
     * 
     * @param userEntityBean
     */
    public void setUserEntityBean(byte[] userEntityBean) {
        this.userEntityBean = userEntityBean;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UserEntityBeanSer)) return false;
        UserEntityBeanSer other = (UserEntityBeanSer) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.userEntityBean==null && other.getUserEntityBean()==null) || 
             (this.userEntityBean!=null &&
              java.util.Arrays.equals(this.userEntityBean, other.getUserEntityBean())));
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
        if (getUserEntityBean() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getUserEntityBean());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getUserEntityBean(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UserEntityBeanSer.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://127.0.0.1:8080/diogenedb/services/UserEntities", "UserEntityBeanSer"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userEntityBean");
        elemField.setXmlName(new javax.xml.namespace.QName("http://127.0.0.1:8080/diogenedb/services/UserEntities", "userEntityBean"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setNillable(true);
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
