package duty.models;

public class Duty {
    private int pienakums_ID;
    private String nosaukums;
    private String apraksts;
    private boolean paveikts;

    public int getPienakums_ID() {
        return pienakums_ID;
    }

    public void setPienakums_ID(int pienakums_ID) {
        this.pienakums_ID = pienakums_ID;
    }

    public String getNosaukums() {
        return nosaukums;
    }

    public void setNosaukums(String nosaukums) {
        this.nosaukums = nosaukums;
    }

    public String getApraksts() {
        return apraksts;
    }

    public void setApraksts(String apraksts) {
        this.apraksts = apraksts;
    }

    public boolean isPaveikts() {
        return paveikts;
    }

    public void setPaveikts(boolean paveikts) {
        this.paveikts = paveikts;
    }
}