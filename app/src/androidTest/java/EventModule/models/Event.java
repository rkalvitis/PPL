package EventModule.models;

import java.sql.Timestamp;

public class Event {
    private int pasakums_ID;
    private String nosaukums;
    private Timestamp sakumaLaiks;
    private String lokacija;
    private String linksUzFoto;

    public int getPasakums_ID() { return pasakums_ID; }

    public void setPasakums_ID(int pasakums_ID) {this.pasakums_ID = pasakums_ID;}

    public String getNosaukums() { return nosaukums; }

    public void setNosaukums(String nosaukums) { this.nosaukums = nosaukums; }

    public Timestamp getSakumaLaiks() { return sakumaLaiks; }

    public void setSakumaLaiks(Timestamp sakumaLaiks) { this.sakumaLaiks = sakumaLaiks; }

    public String getLokacija() { return lokacija; }

    public void setLokacija(String lokacija) { this.lokacija = lokacija; }

    public String getLinksUzFoto() { return linksUzFoto; }

    public void setLinksUzFoto(String linksUzFoto) { this.linksUzFoto = linksUzFoto; }
}
