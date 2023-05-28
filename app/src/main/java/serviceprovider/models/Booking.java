package serviceprovider.models;

import java.util.Date;

public class Booking {
    private int rezervacijas_ID;
    private Date datumsNo;
    private Date datumsLidz;
    private int servisasniedzejs_ID;

    public int getRezervacijas_ID() {
        return rezervacijas_ID;
    }

    public void setRezervacijas_ID(int rezervacijas_ID) {
        this.rezervacijas_ID = rezervacijas_ID;
    }

    public Date getDatumsNo() {
        return datumsNo;
    }

    public void setDatumsNo(Date datumsNo) {
        this.datumsNo = datumsNo;
    }

    public Date getDatumsLidz() {
        return datumsLidz;
    }

    public void setDatumsLidz(Date datumsLidz) {
        this.datumsLidz = datumsLidz;
    }

    public int getServisasniedzejs_ID() {
        return servisasniedzejs_ID;
    }

    public void setServisasniedzejs_ID(int servisasniedzejs_ID) {
        this.servisasniedzejs_ID = servisasniedzejs_ID;
    }
}
