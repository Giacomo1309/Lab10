package it.polito.tdp.bar.model;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.PriorityQueue;
import java.util.Random;

import it.polito.tdp.bar.model.Event.EventType;

public class Simulator {
	// CODA DEGLI EVENTI
	private PriorityQueue<Event> queue = new PriorityQueue<>();

	// PARAMETRI DI SIMULAZIONE
	private int tavoliDa10 = 2; // number of tavoli
	private int tavoliDa8 = 4;
	private int tavoliDa6 = 4;
	private int tavoliDa4 = 5;
	private Duration Intervallo_Tra_Clienti; // intervallo tra i clienti
	private float tolleranza;

	private final LocalTime oraApertura = LocalTime.of(8, 00);
	private final LocalTime oraChiusura = LocalTime.of(17, 00);

	// MODELLO DEL MONDO
	private int disponibiliDa10; // tavoli disponibili nel bar (tra 0 e max sopra)
	private int disponibiliDa8;
	private int disponibiliDa6;
	private int disponibiliDa4;

	// VALORI DA CALCOLARE
	private int clienti;
	private int insoddisfatti;
	private int soddisfatti;
	private int bancone;

	// METODI PER IMPOSTARE I PARAMETRI
	public void tolleranza(int tolleranza) {
		this.tolleranza = tolleranza;
	}

	public void setClientFrequency(Duration d) {
		this.Intervallo_Tra_Clienti = d;
	}

	// METODI PER RESTITUIRE I RISULTATI
	public int getClienti() {
		return clienti;
	}

	public int getInsoddisfatti() {
		return insoddisfatti;
	}

	public int getSoddisfatti() {
		return soddisfatti;
	}

	public int getBancone() {
		return bancone; // ogni volta che li mando al bancone aumento, poi in base alla percentuale
						// faccio i calcoli alla fine
						// se la probabilità è zero li metto tutti insoddisfatti , se la probabilità e
						// 90 percento faccio la proporzione
	}

	// SIMULAZIONE VERA E PROPRIA

	public void run() {
		// preparazione iniziale (mondo + coda eventi)

		// MONDO

		this.disponibiliDa10 = this.tavoliDa10;
		this.disponibiliDa8 = this.tavoliDa8;
		this.disponibiliDa6 = this.tavoliDa6;
		this.disponibiliDa4 = this.tavoliDa4;
		this.clienti = this.insoddisfatti = this.soddisfatti = this.bancone = 0;

		this.queue.clear();

		// CODA EVENTI

		LocalTime oraArrivoCliente = this.oraApertura;
		do {
			int numPersone = (int) (Math.random() * 10);
			int min = 60; // numero minimo
			int max = 120; // numero massimo
			Random rand = new Random();
			int durata = rand.nextInt((max - min) + 1) + min;
			Duration durata1 = Duration.of(durata, ChronoUnit.MINUTES);

			Event e = new Event(oraArrivoCliente, EventType.ARRIVO_GRUPPO_CLIENTI, numPersone, durata1);
			this.queue.add(e);

			oraArrivoCliente = oraArrivoCliente.plus(this.Intervallo_Tra_Clienti);
		} while (oraArrivoCliente.isBefore(this.oraChiusura));

		// esecuzione del ciclo di simulazione
		while (!this.queue.isEmpty()) {
			Event e = this.queue.poll();
//				System.out.println(e);
			processEvent(e);
		}
	}

	private void processEvent(Event e) {
		switch (e.getType()) {

		case ARRIVO_GRUPPO_CLIENTI:

			this.clienti = this.clienti + e.getNumPersone();
			// clienti da 2 o 1
			if (e.getNumPersone() < 3) {
				if (this.disponibiliDa4 > 0) {
					clientiServiti4(e);
				} else {
					this.bancone = this.bancone + e.getNumPersone();
				}

				// clienti da 3 indirizzo verso 4 o verso 6
			} else if (e.getNumPersone() <= 4) {
				if (this.disponibiliDa4 > 0) {
					clientiServiti4(e);
				} else if (this.disponibiliDa6 > 0) {
					clientiServiti6(e);
				} else {
					this.bancone = this.bancone + e.getNumPersone();
				}

				// clienti da 4 indirizzo verso 6 o verso 8
				// clienti da 5 // indirizzo verso verso 6 o verso 8 o verso 10

			} else if (e.getNumPersone() == 5) {

				// clienti da 6 o 7 // indirizzo verso 6 o verso 8 o verso 10
			} else if (e.getNumPersone() < 8) {
				// clienti da 8 // indirizzo verso 6 o verso 8 o verso 10
			} else if (e.getNumPersone() == 8) {
				// indirizzo verso 8
			} else if (e.getNumPersone() == 10) {
				// indirizzo verso 10
			}
			if (this.disponibiliDa4 > 0) {

				this.disponibiliDa4--;

			}

			else {// NON VA BENE!
					// cliente insoddisfatto
				this.clienti = this.clienti + e.getNumPersone();
				this.insoddisfatti = this.insoddisfatti + e.getNumPersone();
			}

			break;

		case TAVOLO_LIBERATO: // torno ad aumentare i tavoli disponibili
			if (e.getNumeroTavolo() == 10)
				this.tavoliDa10++;
			if (e.getNumeroTavolo() == 8)
				this.tavoliDa8++;
			if (e.getNumeroTavolo() == 6)
				this.tavoliDa6++;
			if (e.getNumeroTavolo() == 4)
				this.tavoliDa4++;

			break;
		}
	}

	private void clientiServiti4(Event e) {

		// 1. aggiorna modello del mondo
		this.disponibiliDa4--;
		// 2. aggiorna i risultati
		this.soddisfatti = this.soddisfatti + +e.getNumPersone();
		// 3. genera nuovi eventi
		int numeroTavoloOccupato = 4;
		Event nuovo = new Event(e.getTime().plus(e.getDurata()), EventType.TAVOLO_LIBERATO, 0, numeroTavoloOccupato);
		this.queue.add(nuovo);
	}

	private void clientiServiti6(Event e) {

		// 1. aggiorna modello del mondo
		this.disponibiliDa6--;
		// 2. aggiorna i risultati
		this.soddisfatti = this.soddisfatti + +e.getNumPersone();
		// 3. genera nuovi eventi
		int numeroTavoloOccupato = 6;
		Event nuovo = new Event(e.getTime().plus(e.getDurata()), EventType.TAVOLO_LIBERATO, 0, numeroTavoloOccupato);
		this.queue.add(nuovo);
	}

	private void clientiServiti8(Event e) {

		// 1. aggiorna modello del mondo
		this.disponibiliDa8--;
		// 2. aggiorna i risultati
		this.soddisfatti = this.soddisfatti + +e.getNumPersone();
		// 3. genera nuovi eventi
		int numeroTavoloOccupato = 8;
		Event nuovo = new Event(e.getTime().plus(e.getDurata()), EventType.TAVOLO_LIBERATO, 0, numeroTavoloOccupato);
		this.queue.add(nuovo);
	}

	private void clientiServiti10(Event e) {

		// 1. aggiorna modello del mondo
		this.disponibiliDa10--;
		// 2. aggiorna i risultati
		this.soddisfatti = this.soddisfatti + +e.getNumPersone();
		// 3. genera nuovi eventi
		int numeroTavoloOccupato = 10;
		Event nuovo = new Event(e.getTime().plus(e.getDurata()), EventType.TAVOLO_LIBERATO, 0, numeroTavoloOccupato);
		this.queue.add(nuovo);
	}

}
