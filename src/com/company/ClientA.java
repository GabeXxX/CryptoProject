package com.company;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;

public class ClientA extends Collegue {

    public ClientA(Mediator server, String ID) {
        this.server = server;
        this.ID = ID;

        RsaFactoryKey factoryKey = new RsaFactoryKey();
        key = (RsaKey) factoryKey.getKey();

        record = new Record(key.getPublicKey(), "RSA");


        server.addCollegue(this, record);
    }

    @Override
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

    @Override
    public void receive(ArrayList<BigInteger> message) {
        //
        //Decrypt message
        //
        BigInteger decryptMessage = ((message).get(0)).modPow(key.getPrivateKey(), ((message).get(1)));

        String receveidMessage = new String(decryptMessage.toByteArray());

        System.out.println(ID + " decodes: " + receveidMessage);

    }

    public String getID() {
        return ID;
    }

}

class RsaFactoryKey implements FactoryKey {
    private final static BigInteger ONE = new BigInteger("1");
    private final static SecureRandom RANDOM_NUMBER = new SecureRandom();
    private BigInteger modulus;
    private ArrayList<BigInteger> publicKey = new ArrayList<>();
    private BigInteger privateKey;

    @Override
    public void createKey() {
        //
        //Create RSA key
        //
        int length = 100;
        BigInteger p = BigInteger.probablePrime(length / 2, RANDOM_NUMBER);
        BigInteger q = BigInteger.probablePrime(length / 2, RANDOM_NUMBER);
        BigInteger phi = (p.subtract(ONE)).multiply(q.subtract(ONE));

        modulus = p.multiply(q);

        publicKey.add(new BigInteger("65537"));     //prime number 2^16 + 1
        publicKey.add(modulus);
        privateKey = (publicKey.get(0)).modInverse(phi);

    }

    public Key getKey() {
        createKey();
        return new RsaKey(publicKey, privateKey);
    }
}

class RsaKey implements Key {

    protected ArrayList<BigInteger> publicKey = new ArrayList();
    protected BigInteger privateKey;

    public RsaKey(ArrayList<BigInteger> publicKey, BigInteger privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public ArrayList<BigInteger> getPublicKey() {

        return publicKey;
    }

    public BigInteger getPrivateKey() {

        return privateKey;
    }

}

class RsaStrategy implements Strategy {

    @Override
    public ArrayList<BigInteger> encrypt(String message, Record keyRecord) {
        //
        //Encrypt message
        //
        byte[] bytes = message.getBytes();
        BigInteger encrypted = new BigInteger(bytes);

        encrypted = encrypted.modPow((keyRecord.getPublicKey()).get(0), (keyRecord.getPublicKey()).get(1));

        ArrayList<BigInteger> encryptedMessage = new ArrayList<>();
        encryptedMessage.add(encrypted);
        encryptedMessage.add((keyRecord.getPublicKey()).get(1));

        return encryptedMessage;

    }

}
