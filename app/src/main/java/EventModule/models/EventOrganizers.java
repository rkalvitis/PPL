package EventModule.models;

public class EventOrganizers {
    private int pasakuma_ID;
    private int lietotaja_ID;
    private boolean irRikotajs;
    private boolean irPaligs;

    public int getPasakuma_ID() { return pasakuma_ID; }

    public void setPasakuma_ID(int pasakuma_ID) { this.pasakuma_ID = pasakuma_ID; }

    public int getLietotaja_ID() { return lietotaja_ID; }

    public void setLietotaja_ID(int lietotaja_ID) { this.lietotaja_ID = lietotaja_ID; }

    public boolean getIrRikotajs() { return irRikotajs; }

    public void setIrRikotajs(boolean irRikotajs) { this.irRikotajs = irRikotajs; }

    public boolean getIrPaligs() { return irPaligs; }

    public void setIrPaligs(boolean irPaligs) { this.irPaligs = irPaligs; }

}
