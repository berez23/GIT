//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0-b52-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.04.05 at 12:57:11 PM CEST 
//


package it.webred.ct.service.jaxb.verificaCoordinate.response;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for mappale complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="mappale">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="foglio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="particella" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dataInizioVal" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="zonaDecentramento" type="{}zonaDecentramento" minOccurs="0"/>
 *         &lt;element name="datiPrg" type="{}datiPrg" minOccurs="0"/>
 *         &lt;element name="vincoli" type="{}vincoliType" minOccurs="0"/>
 *         &lt;element name="datiAttesi" type="{}datiAttesi" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="cartografia" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="censTerreni" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="censUrbano" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mappale", propOrder = {
    "foglio",
    "particella",
    "dataInizioVal",
    "zonaDecentramento",
    "datiPrg",
    "vincoli",
    "datiAttesi"
})
public class Mappale
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    protected String foglio;
    protected String particella;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    protected Long dataInizioVal;
    protected ZonaDecentramento zonaDecentramento;
    protected DatiPrg datiPrg;
    protected VincoliType vincoli;
    protected DatiAttesi datiAttesi;
    @XmlAttribute
    protected String cartografia;
    @XmlAttribute
    protected String censTerreni;
    @XmlAttribute
    protected String censUrbano;

    /**
     * Gets the value of the foglio property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFoglio() {
        return foglio;
    }

    /**
     * Sets the value of the foglio property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFoglio(String value) {
        this.foglio = value;
    }

    public boolean isSetFoglio() {
        return (this.foglio!= null);
    }

    /**
     * Gets the value of the particella property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParticella() {
        return particella;
    }

    /**
     * Sets the value of the particella property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParticella(String value) {
        this.particella = value;
    }

    public boolean isSetParticella() {
        return (this.particella!= null);
    }

    /**
     * Gets the value of the dataInizioVal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Long getDataInizioVal() {
        return dataInizioVal;
    }

    /**
     * Sets the value of the dataInizioVal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataInizioVal(Long value) {
        this.dataInizioVal = value;
    }

    public boolean isSetDataInizioVal() {
        return (this.dataInizioVal!= null);
    }

    /**
     * Gets the value of the zonaDecentramento property.
     * 
     * @return
     *     possible object is
     *     {@link ZonaDecentramento }
     *     
     */
    public ZonaDecentramento getZonaDecentramento() {
        return zonaDecentramento;
    }

    /**
     * Sets the value of the zonaDecentramento property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZonaDecentramento }
     *     
     */
    public void setZonaDecentramento(ZonaDecentramento value) {
        this.zonaDecentramento = value;
    }

    public boolean isSetZonaDecentramento() {
        return (this.zonaDecentramento!= null);
    }

    /**
     * Gets the value of the datiPrg property.
     * 
     * @return
     *     possible object is
     *     {@link DatiPrg }
     *     
     */
    public DatiPrg getDatiPrg() {
        return datiPrg;
    }

    /**
     * Sets the value of the datiPrg property.
     * 
     * @param value
     *     allowed object is
     *     {@link DatiPrg }
     *     
     */
    public void setDatiPrg(DatiPrg value) {
        this.datiPrg = value;
    }

    public boolean isSetDatiPrg() {
        return (this.datiPrg!= null);
    }

    /**
     * Gets the value of the vincoli property.
     * 
     * @return
     *     possible object is
     *     {@link VincoliType }
     *     
     */
    public VincoliType getVincoli() {
        return vincoli;
    }

    /**
     * Sets the value of the vincoli property.
     * 
     * @param value
     *     allowed object is
     *     {@link VincoliType }
     *     
     */
    public void setVincoli(VincoliType value) {
        this.vincoli = value;
    }

    public boolean isSetVincoli() {
        return (this.vincoli!= null);
    }

    /**
     * Gets the value of the datiAttesi property.
     * 
     * @return
     *     possible object is
     *     {@link DatiAttesi }
     *     
     */
    public DatiAttesi getDatiAttesi() {
        return datiAttesi;
    }

    /**
     * Sets the value of the datiAttesi property.
     * 
     * @param value
     *     allowed object is
     *     {@link DatiAttesi }
     *     
     */
    public void setDatiAttesi(DatiAttesi value) {
        this.datiAttesi = value;
    }

    public boolean isSetDatiAttesi() {
        return (this.datiAttesi!= null);
    }

    /**
     * Gets the value of the cartografia property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCartografia() {
        return cartografia;
    }

    /**
     * Sets the value of the cartografia property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCartografia(String value) {
        this.cartografia = value;
    }

    public boolean isSetCartografia() {
        return (this.cartografia!= null);
    }

    /**
     * Gets the value of the censTerreni property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCensTerreni() {
        return censTerreni;
    }

    /**
     * Sets the value of the censTerreni property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCensTerreni(String value) {
        this.censTerreni = value;
    }

    public boolean isSetCensTerreni() {
        return (this.censTerreni!= null);
    }

    /**
     * Gets the value of the censUrbano property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCensUrbano() {
        return censUrbano;
    }

    /**
     * Sets the value of the censUrbano property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCensUrbano(String value) {
        this.censUrbano = value;
    }

    public boolean isSetCensUrbano() {
        return (this.censUrbano!= null);
    }

}