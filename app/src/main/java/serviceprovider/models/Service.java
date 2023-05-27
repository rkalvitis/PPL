package serviceprovider.models;

public class Service {
    private int servisasniedzejs_ID;
    private String nosaukums;
    private String epasts;
    private String pilseta;
    private String adrese;
    private String telefons;
    private String parole;
    private Integer valstsKods;
    private Integer reitings;
    private Integer reitinguSkaits;

    public int getServisasniedzejs_ID() {
        return servisasniedzejs_ID;
    }

    public void setServisasniedzejs_ID(int servisasniedzejs_ID) {
        this.servisasniedzejs_ID = servisasniedzejs_ID;
    }

    public String getNosaukums() {
        return nosaukums;
    }

    public void setNosaukums(String nosaukums) {
        this.nosaukums = nosaukums;
    }

    public String getEpasts() {
        return epasts;
    }

    public void setEpasts(String epasts) {
        this.epasts = epasts;
    }

    public String getPilseta() {
        return pilseta;
    }

    public void setPilseta(String pilseta) {
        this.pilseta = pilseta;
    }

    public String getAdrese() {
        return adrese;
    }

    public void setAdrese(String adrese) {
        this.adrese = adrese;
    }

    public String getTelefons() {
        return telefons;
    }

    public void setTelefons(String telefons) {
        this.telefons = telefons;
    }

    public String getParole() {
        return parole;
    }

    public void setParole(String parole) {
        this.parole = parole;
    }

    public Integer getValstsKods(){
        return valstsKods;
    }
    public void setValstsKods(Integer valstsKods){
        this.valstsKods = valstsKods;
    }

    public Integer getReitings() {
        return reitings;
    }

    public void setReitings(Integer reitings) {
        this.reitings = reitings;
    }

    public Integer getReitinguSkaits() {
        return reitinguSkaits;
    }

    public void setReitinguSkaits(Integer reitinguSkaits) {
        this.reitinguSkaits = reitinguSkaits;
    }
}
