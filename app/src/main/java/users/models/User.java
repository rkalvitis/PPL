package users.models;

public class User {
    private int lietotajs_ID;
    private String vards;
    private String uzvards;
    private String epasts;
    private Integer telefons;
    private String parole;
    private Integer isAdmin;
    private Integer valstsKods;

    public int getLietotajs_ID() {
        return lietotajs_ID;
    }
    public void setLietotajs_ID(int lietotajs_ID) {
        this.lietotajs_ID = lietotajs_ID;
    }

    public String getVards() {
        return vards;
    }
    public void setVards(String vards) {
        this.vards = vards;
    }

    public String getUzvards() {
        return uzvards;
    }
    public void setUzvards(String uzvards) {
        this.uzvards = uzvards;
    }

    public String getEpasts() {
        return epasts;
    }
    public void setEpasts(String epasts) {
        this.epasts = epasts;
    }

    public Integer getTelefons() {
        return telefons;
    }
    public void setTelefons(Integer telefons) {
        this.telefons = telefons;
    }

    public String getParole() {
        return parole;
    }
    public void setParole(String parole) {
        this.parole = parole;
    }

    public Integer getIsAdmin(){
       return isAdmin;
    }
    public void setIsAdmin(Integer isAdmin){
        this.isAdmin = isAdmin;
    }

    public Integer getValstsKods(){
        return valstsKods;
    }
    public void setValstsKods(Integer valstsKods){
        this.valstsKods = valstsKods;
    }
}
