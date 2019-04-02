package com.company;

import java.util.ArrayList;
import java.util.HashMap;

class Server implements Mediator {
    private HashMap collegues = new HashMap();

    @Override
    public void send(ArrayList message, String sourceID, String receiverID) {

        for (Object key : collegues.keySet()) {  //keySet() return the set of all key of collegues
            Collegue temp = (Collegue) key;
            if (temp.getID().equals(receiverID)) {

                System.out.println("--------------Server: start forwording message--------------");
                System.out.println("source: " + sourceID);
                System.out.println("receiver: " + receiverID);
                System.out.println("message: " + message.get(0));
                System.out.println("encoding type: " + ((Record) (collegues.get(temp))).getType());

                temp.receive(message);

                System.out.println("--------------Server: forwording done!--------------");
                System.out.println();
            }
        }

    }

    @Override
    public Record getKeyRecord(String receiverID) {

        for (Object key : collegues.keySet()) {  //keySet() return the set of all key of collegues
            Collegue temp = (Collegue) key;
            if (temp.getID().equals(receiverID)) {
                return (Record) collegues.get(temp);
            }

        }

        return null;
    }

    @Override
    public void addCollegue(Collegue collegue, Record record) {
        collegues.put(collegue, record);

    }
}