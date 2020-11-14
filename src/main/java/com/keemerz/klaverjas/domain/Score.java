package com.keemerz.klaverjas.domain;

public class Score {

    private int scoreNS = 0;
    private int scoreEW = 0;
    private String remarkNS;
    private String remarkEW;

    public Score(int scoreNS, int scoreEW) {
        this.scoreNS = scoreNS;
        this.scoreEW = scoreEW;
    }

    public int getScoreNS() {
        return scoreNS;
    }

    public void setScoreNS(int scoreNS) {
        this.scoreNS = scoreNS;
    }

    public int getScoreEW() {
        return scoreEW;
    }

    public void setScoreEW(int scoreEW) {
        this.scoreEW = scoreEW;
    }

    public String getRemarkNS() {
        return remarkNS;
    }

    public void setRemarkNS(String remarkNS) {
        this.remarkNS = remarkNS;
    }

    public String getRemarkEW() {
        return remarkEW;
    }

    public void setRemarkEW(String remarkEW) {
        this.remarkEW = remarkEW;
    }
}
