/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simgui;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Simulationrun {

    String runtitle;
    String simtype;
    String numusers;
    String wanroundtripms;
    String requestmsgbytes;
    String responsemsgbytes;
    String thinksecs;

    public Simulationrun() {
        setruntitle("");
        setsimtype("");
        setnumusers("0");
        setwanroundtripms("0");
        setrequestmsgbytes("0");
        setresponsemsgbytes("0");
        setthinksecs("0");
    }

    public Simulationrun(String[] values) {
        setruntitle(values[0]);
        setsimtype(values[1]);
        setnumusers(values[2]);
        setwanroundtripms(values[3]);
        setrequestmsgbytes(values[4]);
        setresponsemsgbytes(values[5]);
        setthinksecs(values[6]);
    }

    public String getruntitle() {
        return this.runtitle;
    }

    public String getsimtype() {
        return this.simtype;
    }

    public String getnumusers() {
        return this.numusers;
    }

    public String getwanroundtripms() {
        return this.wanroundtripms;
    }

    public String getrequestmsgbytes() {
        return this.requestmsgbytes;
    }

    public String getresponsemsgbytes() {
        return this.responsemsgbytes;
    }

    public String getthinksecs() {
        return this.thinksecs;
    }

    @XmlElement
    public void setruntitle(String runtitle) {
        this.runtitle = runtitle;
    }

    @XmlElement
    public void setsimtype(String simtype) {
        this.simtype = simtype;
    }

    @XmlElement
    public void setnumusers(String numusers) {
        this.numusers = numusers;
    }

    @XmlElement
    public void setwanroundtripms(String wanroundtripms) {
        this.wanroundtripms = wanroundtripms;
    }

    @XmlElement
    public void setrequestmsgbytes(String requestmsgbytes) {
        this.requestmsgbytes = requestmsgbytes;
    }

    @XmlElement
    public void setresponsemsgbytes(String responsemsgbytes) {
        this.responsemsgbytes = responsemsgbytes;
    }

    @XmlElement
    public void setthinksecs(String thinksecs) {
        this.thinksecs = thinksecs;
    }
    
    
}
