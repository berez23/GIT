/**
 * UserEntitiesBean.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.webred.diogene.client;

public class UserEntitiesBean  implements java.io.Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1623323960329763625L;
	/**
	 * 
	 */

	private java.lang.Object[] userEntities;

    public UserEntitiesBean() {
    }

    public UserEntitiesBean(
           java.lang.Object[] userEntities) {
           this.userEntities = userEntities;
    }


    /**
     * Gets the userEntities value for this UserEntitiesBean.
     * 
     * @return userEntities
     */
    public java.lang.Object[] getUserEntities() {
        return userEntities;
    }


    /**
     * Sets the userEntities value for this UserEntitiesBean.
     * 
     * @param userEntities
     */
    public void setUserEntities(java.lang.Object[] userEntities) {
        this.userEntities = userEntities;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UserEntitiesBean)) return false;
        UserEntitiesBean other = (UserEntitiesBean) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.userEntities==null && other.getUserEntities()==null) || 
             (this.userEntities!=null &&
              java.util.Arrays.equals(this.userEntities, other.getUserEntities())));
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
        if (getUserEntities() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getUserEntities());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getUserEntities(), i);
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
        new org.apache.axis.description.TypeDesc(UserEntitiesBean.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://127.0.0.1:8080/diogenedb/services/UserEntities", "UserEntitiesBean"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userEntities");
        elemField.setXmlName(new javax.xml.namespace.QName("http://127.0.0.1:8080/diogenedb/services/UserEntities", "userEntities"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://127.0.0.1:8080/diogenedb/services/UserEntities", "item"));
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
