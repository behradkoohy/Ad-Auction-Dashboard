//Common package where classes used by all levels of the system can be stored
package common;

//Data class which holds all the information to populate a UI update
public class Data {

    private int numImpressions;
    private int numClicks;
    private int numBounces;
    private int numConversions;
    private double totalCost;
    private double CTR;
    private double CPA;
    private double CPC;
    private double CPM;

    public Data(int numImpressions, int numClicks, int numBounces,
                int numConversions, double totalCost, double CTR, double CPA,
                double CPC, double CPM){

        this.numImpressions = numImpressions;
        this.numClicks = numClicks;
        this.numBounces = numBounces;
        this.numConversions = numConversions;
        this.totalCost = totalCost;
        this.CTR = CTR;
        this.CPA = CPA;
        this.CPC = CPC;
        this.CPM = CPM;

    }

    public int getNumImpressions() {
        return numImpressions;
    }

    public void setNumImpressions(int numImpressions) {
        this.numImpressions = numImpressions;
    }

    public int getNumClicks() {
        return numClicks;
    }

    public void setNumClicks(int numClicks) {
        this.numClicks = numClicks;
    }

    public int getNumBounces() {
        return numBounces;
    }

    public void setNumBounces(int numBounces) {
        this.numBounces = numBounces;
    }

    public int getNumConversions() {
        return numConversions;
    }

    public void setNumConversions(int numConversions) {
        this.numConversions = numConversions;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public double getCTR() {
        return CTR;
    }

    public void setCTR(double CTR) {
        this.CTR = CTR;
    }

    public double getCPA() {
        return CPA;
    }

    public void setCPA(double CPA) {
        this.CPA = CPA;
    }

    public double getCPC() {
        return CPC;
    }

    public void setCPC(double CPC) {
        this.CPC = CPC;
    }

    public double getCPM() {
        return CPM;
    }

    public void setCPM(double CPM) {
        this.CPM = CPM;
    }

}