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
    String reqwansecs;
    String reqwanquesecs;
    String reqlbsecs;
    String reqlbquesecs;
    String reqwebsecs;
    String reqwebquesecs;
    String reqmidsecs;
    String reqmidquesecs;
    String reqappsecs;
    String reqappquesecs;
    String reqdbsecs;
    String reqdbquesecs;

    String rspappsecs;
    String rspappquesecs;
    String rspmidsecs;
    String rspmidquesecs;
    String rspwebsecs;
    String rspwebquesecs;
    String rsplbsecs;
    String rsplbquesecs;

    public Simulationrun() {
        setruntitle("");
        setsimtype("");
        setnumusers("0");
        setwanroundtripms("0");
        setrequestmsgbytes("0");
        setresponsemsgbytes("0");
        setthinksecs("0");
        setreqwansecs("0");
        setreqwanquesecs("0");
        setreqlbsecs("0");
        setreqlbquesecs("0");
        setreqwebsecs("0");
        setreqwebquesecs("0");
        setreqmidsecs("0");
        setreqmidquesecs("0");
        setreqappsecs("0");
        setreqappquesecs("0");
        setreqdbsecs("0");
        setreqdbquesecs("0");
        setrspappsecs("0");
        setrspappquesecs("0");
        setrspmidsecs("0");
        setrspmidquesecs("0");
        setrspwebsecs("0");
        setrspwebquesecs("0");
        setrsplbsecs("0");
        setrsplbquesecs("0");
    }

    public Simulationrun(String runtitle,
                         String simtype,
                         String numusers,
                         String wanroundtripms,
                         String requestmsgbytes,
                         String responsemsgbytes,
                         String thinksecs,
                         String reqwansecs,
                         String reqwanquesecs,
                         String reqlbsecs,
                         String reqlbquesecs,
                         String reqwebsecs,
                         String reqwebquesecs,
                         String reqmidsecs,
                         String reqmidquesecs,
                         String reqappsecs,
                         String reqappquesecs,
                         String reqdbsecs,
                         String reqdbquesecs,
                         String rspappsecs,
                         String rspappquesecs,
                         String rspmidsecs,
                         String rspmidquesecs,
                         String rspwebsecs,
                         String rspwebquesecs,
                         String rsplbsecs,
                         String rsplbquesecs) {

        setruntitle(runtitle);
        setsimtype(simtype);
        setnumusers(numusers);
        setwanroundtripms(wanroundtripms);
        setrequestmsgbytes(requestmsgbytes);
        setresponsemsgbytes(responsemsgbytes);
        setthinksecs(thinksecs);
        setreqwansecs(reqwansecs);
        setreqwanquesecs(reqwanquesecs);
        setreqlbsecs(reqlbsecs);
        setreqlbquesecs(reqlbquesecs);
        setreqwebsecs(reqwebsecs);
        setreqwebquesecs(reqwebquesecs);
        setreqmidsecs(reqmidsecs);
        setreqmidquesecs(reqmidquesecs);
        setreqappsecs(reqappsecs);
        setreqappquesecs(reqappquesecs);
        setreqdbsecs(reqdbsecs);
        setreqdbquesecs(reqdbquesecs);
        setrspappsecs(rspappsecs);
        setrspappquesecs(rspappquesecs);
        setrspmidsecs(rspmidsecs);
        setrspmidquesecs(rspmidquesecs);
        setrspwebsecs(rspwebsecs);
        setrspwebquesecs(rspwebquesecs);
        setrsplbsecs(rsplbsecs);
        setrsplbquesecs(rsplbquesecs);
    }

    public Simulationrun(String[] values) {
        setruntitle(values[0]);
        setsimtype(values[1]);
        setnumusers(values[2]);
        setwanroundtripms(values[3]);
        setrequestmsgbytes(values[4]);
        setresponsemsgbytes(values[5]);
        setthinksecs(values[6]);
        setreqwansecs(values[7]);
        setreqwanquesecs(values[8]);
        setreqlbsecs(values[9]);
        setreqlbquesecs(values[10]);
        setreqwebsecs(values[11]);
        setreqwebquesecs(values[12]);
        setreqmidsecs(values[13]);
        setreqmidquesecs(values[14]);
        setreqappsecs(values[15]);
        setreqappquesecs(values[16]);
        setreqdbsecs(values[17]);
        setreqdbquesecs(values[18]);
        setrspappsecs(values[19]);
        setrspappquesecs(values[20]);
        setrspmidsecs(values[21]);
        setrspmidquesecs(values[22]);
        setrspwebsecs(values[23]);
        setrspwebquesecs(values[24]);
        setrsplbsecs(values[25]);
        setrsplbquesecs(values[26]);
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

    public String getreqwansecs() {
        return this.reqwansecs;
    }

    public String getreqwanquesecs() {
        return this.reqwanquesecs;
    }

    public String getreqlbsecs() {
        return this.reqlbsecs;
    }

    public String getreqlbquesecs() {
        return this.reqlbquesecs;
    }

    public String getreqwebsecs() {
        return this.reqwebsecs;
    }

    public String getreqwebquesecs() {
        return this.reqwebquesecs;
    }

    public String getreqmidsecs() {
        return this.reqmidsecs;
    }

    public String getreqmidquesecs() {
        return this.reqmidquesecs;
    }

    public String getreqappsecs() {
        return this.reqappsecs;
    }

    public String getreqappquesecs() {
        return this.reqappquesecs;
    }

    public String getreqdbsecs() {
        return this.reqdbsecs;
    }

    public String getreqdbquesecs() {
        return this.reqdbquesecs;
    }

    public String getrspappsecs() {
        return this.rspappsecs;
    }

    public String getrspappquesecs() {
        return this.rspappquesecs;
    }

    public String getrspmidsecs() {
        return this.rspmidsecs;
    }

    public String getrspmidquesecs() {
        return this.rspmidquesecs;
    }

    public String getrspwebsecs() {
        return this.rspwebsecs;
    }

    public String getrspwebquesecs() {
        return this.rspwebquesecs;
    }

    public String getrsplbsecs() {
        return this.rsplbsecs;
    }

    public String getrsplbquesecs() {
        return this.rsplbquesecs;
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

    @XmlElement
    public void setreqwansecs(String reqwansecs) {
        this.reqwansecs = reqwansecs;
    }

    @XmlElement
    public void setreqwanquesecs(String reqwanquesecs) {
        this.reqwanquesecs = reqwanquesecs;
    }

    @XmlElement
    public void setreqlbsecs(String reqlbsecs) {
        this.reqlbsecs = reqlbsecs;
    }

    @XmlElement
    public void setreqlbquesecs(String reqlbquesecs) {
        this.reqlbquesecs = reqlbquesecs;
    }

    @XmlElement
    public void setreqwebsecs(String reqwebsecs) {
        this.reqwebsecs = reqwebsecs;
    }

    @XmlElement
    public void setreqwebquesecs(String reqwebquesecs) {
        this.reqwebquesecs = reqwebquesecs;
    }

    @XmlElement
    public void setreqmidsecs(String reqmidsecs) {
        this.reqmidsecs = reqmidsecs;
    }

    @XmlElement
    public void setreqmidquesecs(String reqmidquesecs) {
        this.reqmidquesecs = reqmidquesecs;
    }

    @XmlElement
    public void setreqappsecs(String reqappsecs) {
        this.reqappsecs = reqappsecs;
    }

    @XmlElement
    public void setreqappquesecs(String reqappquesecs) {
        this.reqappquesecs = reqappquesecs;
    }

    @XmlElement
    public void setreqdbsecs(String reqdbsecs) {
        this.reqdbsecs = reqdbsecs;
    }

    @XmlElement
    public void setreqdbquesecs(String reqdbquesecs) {
        this.reqdbquesecs = reqdbquesecs;
    }

    @XmlElement
    public void setrspappsecs(String rspappsecs) {
        this.rspappsecs = rspappsecs;
    }

    @XmlElement
    public void setrspappquesecs(String rspappquesecs) {
        this.rspappquesecs = rspappquesecs;
    }

    @XmlElement
    public void setrspmidsecs(String rspmidsecs) {
        this.rspmidsecs = rspmidsecs;
    }

    @XmlElement
    public void setrspmidquesecs(String rspmidquesecs) {
        this.rspmidquesecs = rspmidquesecs;
    }

    @XmlElement
    public void setrspwebsecs(String rspwebsecs) {
        this.rspwebsecs = rspwebsecs;
    }

    @XmlElement
    public void setrspwebquesecs(String rspwebquesecs) {
        this.rspwebquesecs = rspwebquesecs;
    }

    @XmlElement
    public void setrsplbsecs(String rsplbsecs) {
        this.rsplbsecs = rsplbsecs;
    }

    @XmlElement
    public void setrsplbquesecs(String rsplbquesecs) {
        this.rsplbquesecs = rsplbquesecs;
    }
}
