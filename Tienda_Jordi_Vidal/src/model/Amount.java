package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;

public class Amount {
    private double value;
    private String currency;

    private final String defaultCurrency = "€";

    // Constructor
    public Amount() {
    	this.currency = defaultCurrency;
    }

    public Amount(double value) {
        this.value = value;
        this.currency = defaultCurrency; 
    }

    @XmlValue
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @XmlAttribute(name = "currency")
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
    	if ("Euro".equals(currency)) {
            currency =  "€";
        }
        this.currency = currency;
    }

    @XmlTransient  
    public String getFormattedCurrency() {
        if ("Euro".equals(this.currency)) {
            return "€";
        }
        return this.currency;  
    }

    @Override
    public String toString() {
        return value + " " + getFormattedCurrency();
    }
}
