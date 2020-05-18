package it.polito.tdp.bar.model;

import java.time.Duration;
import java.time.LocalTime;




public class Event implements Comparable<Event> {

	public enum EventType{
		ARRIVO_GRUPPO_CLIENTI , TAVOLO_LIBERATO
	}
	
	private LocalTime time ;
	private EventType type ;
	private int numPersone;
	private Duration durata;
	private int numeroTavolo;
	
	public Event(LocalTime time, EventType type, int numPersone, Duration durata) {
		super();
		this.time = time;
		this.type = type;
		this.numPersone = numPersone;
		this.durata = durata;
		
	}
	
	public Event(LocalTime time, EventType type, int numPersone,int numeroTavolo ) {
		
		this.time = time;
		this.type = type;
		this.numPersone = numPersone;
		this.numeroTavolo = numeroTavolo;
		
	}
	

	
	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public int getNumPersone() {
		return numPersone;
	}

	public void setNumPersone(int numPersone) {
		this.numPersone = numPersone;
	}

	public Duration getDurata() {
		return durata;
	}

	public void setDurata(Duration durata) {
		this.durata = durata;
	}
	public int getNumeroTavolo() {
		return this.numeroTavolo;
	}

	public void setNumeroTavolo(int numeroTavolo) {
		this.numeroTavolo = numeroTavolo;
	}

	
	@Override
	public String toString() {
		return "Event [time=" + time + ", type=" + type + ", numPersone=" + numPersone + ", durata=" + durata
				+ "]";
	}

	@Override
	public int compareTo(Event other) {
		// TODO Auto-generated method stub
		return this.time.compareTo(other.time);
	}
	
	
	
	
}
