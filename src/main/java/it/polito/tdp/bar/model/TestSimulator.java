package it.polito.tdp.bar.model;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import it.polito.tdp.bar.model.*;

public class TestSimulator {

	public static void main(String args[]) {
		Simulator sim = new Simulator();
		
		
		sim.tolleranza(0);
		sim.setClientFrequency(Duration.of(10, ChronoUnit.MINUTES));
		
		sim.run() ;
		
		int totClients = sim.getClienti() ;
		int dissatisfied = sim.getInsoddisfatti() ;
		
		System.out.format("Arrived %d clients, %d were dissatisfied\n", 
				totClients, dissatisfied);
	}
	
}
