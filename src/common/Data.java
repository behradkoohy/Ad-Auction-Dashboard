//Common package where classes used by all levels of the system can be stored
package common;

//Data class which holds all the information to populate a UI update
public class Data {

    private int numImpressions;
    private int numClicks;
    private int numUniques;
    private int numBounces;
    private int numConversions;
    private double totalCost;
    private double CTR;
    private double CPA;
    private double CPC;
    private double CPM;
    private double bounceRate;

    /**
     *
     * Default constructor for the Data object
     *
     * @param numImpressions
     * @param numClicks
     * @param numBounces
     * @param numConversions
     * @param totalCost
     * @param CTR
     * @param CPA
     * @param CPC
     * @param CPM
     * @param bounceRate
     */
    public Data(int numImpressions, int numClicks, int numUniques, int numBounces,
                int numConversions, double totalCost, double CTR, double CPA,
                double CPC, double CPM, double bounceRate){

        this.numImpressions = numImpressions;
        this.numClicks = numClicks;
        this.numUniques = numUniques;
        this.numBounces = numBounces;
        this.numConversions = numConversions;
        this.totalCost = totalCost;
        this.CTR = CTR;
        this.CPA = CPA;
        this.CPC = CPC;
        this.CPM = CPM;
        this.bounceRate = bounceRate;

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

    public int getNumUniques() {
        return numUniques;
    }

    public void setNumUniques(int numUniques) {
        this.numUniques = numUniques;
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

    public double getBounceRate() {
        return bounceRate;
    }

    public void setBounceRate(double bounceRate) {
        this.bounceRate = bounceRate;
    }

}