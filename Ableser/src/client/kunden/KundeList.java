package client.kunden;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonIgnore;

import client.Service;
import client.Util;
import dataEntities.Kunde;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.Getter;

//Wrapper für die verwendete Liste, außerdem verantwortlich für den Export/Import mit den Methoden "export<Dateiformat>()
public class KundeList {
	@Getter
	private ArrayList<Kunde> liste = new ArrayList<>();

	@JsonIgnore
	private Service service;

	@JsonIgnore
	private ArrayList<Function<ArrayList<Kunde>, Boolean>> onChange;

	public KundeList(Service service) {
		super();
		this.service = service;
		liste = new ArrayList<>();
		onChange = new ArrayList<>();
		refresh();
	}

	/**
	 * @param e
	 * @return boolean
	 */
	public boolean add(Kunde e) {
		Response res = service.post(Service.endpointKunden, e);

		if (res.getStatus() != Status.CREATED.getStatusCode()) {
			Util.errorMessage(res.getStatus() + " - " + res.readEntity(String.class));
			return false;
		}

		Kunde serverKunde = res.readEntity(Kunde.class);

		if (liste.add(serverKunde)) {
			callOnChanged();
			return true;
		} else {
			return false;
		}
	}

	public void clear() {
		liste.clear();
	}

	public boolean update(Kunde oldK, Kunde newK) {

		switch (checkChanged(oldK)) {
		case noSave:
			return false;
		case addNew:
			liste.remove(oldK);
			return add(newK);
		case doSave:
			break;
		default:
			break;
		}

		Response res = service.put(Service.endpointKunden, newK);

		if (res.getStatus() != Status.OK.getStatusCode()) {
			Util.errorMessage(res.getStatus() + " - " + res.readEntity(String.class));
			return false;
		}
		oldK.setName(newK.getName());
		oldK.setVorname(newK.getVorname());
		callOnChanged();
		return true;
	}

	/**
	 * @param index
	 * @return Kunde
	 */
	public Kunde get(int index) {
		return liste.get(index);
	}

	public Kunde getById(UUID id) {
		for (Kunde k : liste) {
			if (k.getId().equals(id)) {
				return k;
			}
		}
		return null;
	}

	public Kunde[] getArray() {
		return liste.toArray(new Kunde[0]);
	}

	/**
	 * @param Kunde
	 * @return int
	 */
	public int indexOf(Kunde k) {
		return liste.indexOf(k);
	}

	/**
	 * @param Kunde
	 * @return boolean
	 */
	public boolean remove(Kunde k) {
		if (k == null) {
			return false;
		}
		Response delRes = service.delete(Service.endpointKunden+"/" + k.getId().toString());

		if (delRes.getStatus() != Status.OK.getStatusCode()) {
			if (delRes.getStatus() == Status.NOT_FOUND.getStatusCode()) {
				Util.errorMessage("Datensatz wurde bereits gelöscht\n" + delRes.getStatus() + " -"
						+ delRes.readEntity(String.class));
			} else {
				Util.errorMessage(
						"Löschen fehlgeschlagen\n" + delRes.getStatus() + " -" + delRes.readEntity(String.class));
				return false;
			}
		}

		int index = indexOf(k);
		if (index < 0) {
			return false;
		}
		if (remove(index) != null) {
			callOnChanged();
			return true;
		}
		{
			return false;
		}
	}

	/**
	 * @param index
	 * @return Kunde
	 */
	private Kunde remove(int index) {
		return liste.remove(index);
	}

	/**
	 * @return int
	 */
	public int size() {
		return liste.size();
	}

	/**
	 * @return Stream<KundeList>
	 */
	public Stream<Kunde> stream() {
		return liste.stream();
	}

	/**
	 * @return String
	 */
	@Override
	public String toString() {
		final StringBuilder buf = new StringBuilder();
		liste.stream().forEach(en -> buf.append(en.toString()));
		return buf.toString();
	}


	public boolean refresh() {
		Response res = service.get(Service.endpointKunden);

		if (res.getStatus() != 200) {
			System.out.println(
					"Laden der Kunden fehlgeschlagen\n" + res.getStatus() + " - " + res.readEntity(String.class));
			Util.errorMessage(res.readEntity(String.class));
			return false;
		}

		liste = res.readEntity(new GenericType<ArrayList<Kunde>>() {
		});
		callOnChanged();
		return true;
	}

	private enum ChangedState {
		noSave, doSave, addNew;
	}

	private ChangedState checkChanged(Kunde k) {
		Response res = service.get(Service.endpointKunden+"/" + k.getId().toString());

		switch (res.getStatus()) {
		case 200:
			Kunde kServer = res.readEntity(Kunde.class);
			if (!k.equals(kServer)) {
				/*System.out.println("DIFFTOOL");
				System.out.println(k.toString());
				System.out.println(kServer.toString());
				System.out.println("--------");//*/
				if (Util.optionMessage("Kunde hat sich geändert \nTrotzdem speichern?")) {
					return ChangedState.doSave;
				} else {
					return ChangedState.noSave;
				}
			}
			return ChangedState.doSave;
		case 404:
			if (Util.optionMessage(
					"404 - Ablesung nicht gefunden, wurde der Kunde gelöscht?\nTrotzdem speichern?")) {
				return ChangedState.addNew;
			} else {
				return ChangedState.noSave;
			}

		default:
			if (Util.optionMessage(res.getStatus() + " - " + res.readEntity(String.class) + "\nTrotzdem speichern?")) {
				return ChangedState.doSave;
			} else {
				return ChangedState.noSave;
			}
		}
	}

	public void addChangeListener(Function<ArrayList<Kunde>, Boolean> newF) {

		onChange.add(newF);
	}

	private void callOnChanged() {
		for (Function<ArrayList<Kunde>, Boolean> f : onChange) {
			f.apply(liste);
		}
	}

}
