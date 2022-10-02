package com.example.agattiprg3;

public class myList {
    private int value;
    private String englishText;
    private Object myList = null;

    /**
     * Constructor for a myList object
     * @param value
     * @param s
     */
    public myList(int value, String s) {
        this.value = value;
        this.englishText = s;
    }

    /**
     * Copy constructor
     * @param toCopy
     */
    public myList(myList toCopy) {
        myList = toCopy;
        englishText = toCopy.getEnglishText();
        value = toCopy.getValue();
    }

    /**
     * Getter for index of list object
     * @return
     */
    public int getValue() { return this.value;}

    /**
     * Getter for name of list object
     * @return
     */
    public String getEnglishText() {
        return this.englishText;
    }
}