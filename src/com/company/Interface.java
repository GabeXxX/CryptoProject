package com.company;

import java.math.BigInteger;
import java.util.ArrayList;

abstract class Collegue {
    protected Mediator server;
    protected String receivedMessage;
    protected Key key;
    protected Strategy encryptAlgorithm;
    protected String ID;
    protected Record record;

    public void send(String message, String receveirID) {
        Record keyRecord = server.getKeyRecord(receveirID);

        if (keyRecord.getType().equals("RSA")) {
            encryptAlgorithm = new RsaStrategy();
        } else if (keyRecord.getType().equals("ELGAMEL")) {
            encryptAlgorithm = new ElGamelStrategy();

        }

        ArrayList encryptedMessage = encryptAlgorithm.encrypt(message, keyRecord);
        server.send(encryptedMessage, ID, receveirID);

    }

    public abstract void receive(ArrayList<BigInteger> message);

    public String getID() {

        return ID;
    }
}

interface Mediator {
    public void send(ArrayList<BigInteger> message, String sourceID, String destinationID);

    public Record getKeyRecord(String destinationID);

    public void addCollegue(Collegue collegue, Record publicKey);
}

interface FactoryKey {
    public void createKey();

    public Key getKey();
}

interface Key {
    public ArrayList<BigInteger> getPublicKey();

    public BigInteger getPrivateKey();
}

interface Strategy {
    public ArrayList encrypt(String message, Record recordKey);

}