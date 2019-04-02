package com.company;


//---------------------INTERFACE-----------------------
public class Main {

    public static void main(String[] args) {
	// write your code here
        Server server = new Server();
        ClientA gianni = new ClientA(server, "Gianni");
        ClientB anna = new ClientB(server, "Anna");
        ClientA gabriele = new ClientA(server, "Gabriele");

        gianni.send("Ciao come va?", "Anna");
        anna.send("Benissimo", "Gianni");
        gabriele.send("Esci un po?", "Gianni");
        gabriele.send("Ciao", "Anna");
        gabriele.send("Ti va di uscire?", "Anna");
        gianni.send("No", "Gabriele");
        anna.send("No", "Gabriele");

        System.out.println("#PoorGabriele");

    }
}


