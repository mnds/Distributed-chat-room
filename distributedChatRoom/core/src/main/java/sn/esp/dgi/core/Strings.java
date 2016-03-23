/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sn.esp.dgi.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mnds
 */
@XmlRootElement
public class Strings extends ArrayList<String> {
    
    public Strings() {
        super();
    }
    
    public Strings( Collection<? extends String> s ) {
        super(s);
    }
    
    @XmlElement(name = "string")
    public List<String> getStrings() {
        return this;
    }
    
    public void setStrings( List<String> strings ) {
        this.addAll(strings);
    }
}
