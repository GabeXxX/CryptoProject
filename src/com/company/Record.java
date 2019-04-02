package com.company;

import java.math.BigInteger;
import java.util.ArrayList;

class Record {
    private ArrayList<BigInteger> publicKey;
    private String type;

    public Record(ArrayList<BigInteger> key, String type) {
        publicKey = key;
        this.type = type;
    }

    public ArrayList<BigInteger> getPublicKey() {
        return publicKey;
    }

    public String getType() {

        return type;
    }

}
