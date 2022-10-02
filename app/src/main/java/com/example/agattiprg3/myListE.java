package com.example.agattiprg3;

public class myListE {
    private int value;
    private String englishText;
    private Object myListE = null;

    /**
     * constructor for an element object
     * @param value
     * @param s
     */
    public myListE(int value, String s) {
        this.value = value;
        this.englishText = s;
    }

    /**
     * Copy constructor
     * @param toCopy
     */
    public myListE(myListE toCopy) {
        myListE = toCopy;
        englishText = toCopy.getEnglishText();
        value = toCopy.getValue();
    }

    /**
     * Getter for the index of the element
     * @return
     */
    public int getValue() { return this.value;}

    /**
     * Getter for the name of the element
     * @return
     */
    public String getEnglishText() {
        return this.englishText;
    }
}
