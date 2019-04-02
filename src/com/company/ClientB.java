package com.company;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

public class ClientB extends Collegue {

    public ClientB(Mediator server, String ID) {
        this.server = server;

        ElgamelFactoryKey factoryKey = new ElgamelFactoryKey();
        key = (ElGamelKey) factoryKey.getKey();
        this.ID = ID;
        record = new Record(key.getPublicKey(), "ELGAMEL");

        server.addCollegue(this, record);
    }

    @Override
    public void receive(ArrayList<BigInteger> message) {
        //
        //Decrypt message
        //
        BigInteger brmodp = (message).get(1);
        BigInteger modP = (key.getPublicKey()).get(2);
        BigInteger EC = (message).get(0);

        BigInteger privateKey = key.getPrivateKey();
        BigInteger crmodp = brmodp.modPow(privateKey, modP);
        BigInteger d = crmodp.modInverse(modP);
        BigInteger ad = d.multiply(EC).mod(modP);

        String receveidMessage = new String(ad.toByteArray());
        System.out.println(ID + " decodes: " + receveidMessage);

    }

    @Override
    public String getID() {
        return ID;
    }
}

class ElgamelFactoryKey implements FactoryKey {

    private BigInteger privateKey;
    private BigInteger modP;
    private BigInteger g;
    private BigInteger beta;
    private ArrayList<BigInteger> publicKey = new ArrayList<>();


    @Override
    public void createKey() {
        //
        //Create ELGAMAL key
        //
        Random sc = new SecureRandom();
        privateKey = new BigInteger("99999999999999999999999999999999");

        modP = BigInteger.probablePrime(128, sc);
        g = new BigInteger("3");
        beta = g.modPow(privateKey, modP);

        publicKey.add(beta);
        publicKey.add(g);
        publicKey.add(modP);

    }

    @Override
    public Key getKey() {
        createKey();
        return new ElGamelKey(publicKey, privateKey);
    }
}


class ElGamelKey implements Key {

    protected ArrayList<BigInteger> publicKey = new ArrayList();
    protected BigInteger privateKey;

    public ElGamelKey(ArrayList<BigInteger> publicKey, BigInteger privateKey) {
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

class ElGamelStrategy implements Strategy {

    SecureRandom random = new SecureRandom();

    @Override
    public ArrayList<BigInteger> encrypt(String message, Record recordKey) {
        //
        //Encrypt message
        //
        byte[] bytes = message.getBytes();
        BigInteger X = new BigInteger(bytes);
        BigInteger r = new BigInteger(128, random);
        BigInteger beta = (recordKey.getPublicKey()).get(0);
        BigInteger modP = (recordKey.getPublicKey()).get(2);
        BigInteger g = (recordKey.getPublicKey()).get(1);

        BigInteger EC = X.multiply(beta.modPow(r, modP)).mod(modP);
        BigInteger brmodp = g.modPow(r, modP);

        ArrayList<BigInteger> encryptedMessage = new ArrayList<>();
        encryptedMessage.add(EC);
        encryptedMessage.add(brmodp);

        return encryptedMessage;
    }
}

