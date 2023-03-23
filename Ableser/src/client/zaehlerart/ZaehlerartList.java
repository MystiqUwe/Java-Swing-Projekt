package client.zaehlerart;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonIgnore;

import client.Service;
import client.Util;
import dataEntities.Zaehlerart;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.Getter;

//Wrapper für die verwendete Liste, außerdem verantwortlich für den Export/Import mit den Methoden "export<Dateiformat>()
public class ZaehlerartList {
	@Getter
	private ArrayList<Zaehlerart> liste = new ArrayList<>();

	@JsonIgnore
	private Service service;

	@JsonIgnore
	private ArrayList<Function<ArrayList<Zaehlerart>, Boolean>> onChange;

	public ZaehlerartList(Service service) {
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
	public boolean add(Zaehlerart e) {
		Response res = service.post(Service.endpointZaehlerarten, e);

		if (res.getStatus() != Status.CREATED.getStatusCode()) {
			Util.errorMessage(res.getStatus() + " - " + res.readEntity(String.class));
			return false;
		}

		Zaehlerart serverZaehlerarten = res.readEntity(Zaehlerart.class);

		if (liste.add(serverZaehlerarten)) {
			callOnChanged();
			return true;
		} else {
			return false;
		}
	}

	public void clear() {
		liste.clear();
	}

	public boolean update(Zaehlerart oldZ, Zaehlerart newZ) {

		switch (checkChanged(oldZ)) {
		case noSave:
			return false;
		case addNew:
			liste.remove(oldZ);
			return add(newZ);
		case doSave:
			break;
		default:
			break;
		}

		Response res = service.put(Service.endpointZaehlerarten, newZ);

		if (res.getStatus() != Status.OK.getStatusCode()) {
			Util.errorMessage(res.getStatus() + " - " + res.readEntity(String.class));
			return false;
		}
		oldZ.setName(newZ.getName());
		oldZ.setWarnValue(newZ.getWarnValue());
		callOnChanged();
		return true;
	}

	/**
	 * @param index
	 * @return Zaehlerart
	 */
	public Zaehlerart get(int index) {
		return liste.get(index);
	}

	public Zaehlerart getById(int id) {
		for (Zaehlerart z : liste) {
			if (z.getId()==id) {
				return z;
			}
		}
		return null;
	}

	public Zaehlerart[] getArray() {
		return liste.toArray(new Zaehlerart[0]);
	}

	/**
	 * @param Zaehlerart
	 * @return int
	 */
	public int indexOf(Zaehlerart z) {
		return liste.indexOf(z);
	}

	/**
	 * @param Kunde
	 * @return boolean
	 */
	public boolean remove(Zaehlerart z) {
		if (z == null) {
			return false;
		}
		Response delRes = service.delete(Service.endpointZaehlerarten+"/" + z.getId());

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

		int index = indexOf(z);
		if (index < 0) {
			return false;
		}
		if (remove(index) != null) {
			callOnChanged();
			Util.infoMessage("Löschen erfolgreich");
			return true;
		}
		{
			return false;
		}
	}

	/**
	 * @param index
	 * @return Zaehlerart
	 */
	private Zaehlerart remove(int index) {
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
	public Stream<Zaehlerart> stream() {
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
		Response res = service.get(Service.endpointZaehlerarten);

		if (res.getStatus() != 200) {
			System.out.println(
					"Laden der Zaehlerarten fehlgeschlagen\n" + res.getStatus() + " - " + res.readEntity(String.class));
			Util.errorMessage(res.readEntity(String.class));
			return false;
		}

		liste = res.readEntity(new GenericType<ArrayList<Zaehlerart>>() {
		});
		callOnChanged();
		return true;
	}

	private enum ChangedState {
		noSave, doSave, addNew;
	}

	private ChangedState checkChanged(Zaehlerart z) {
		Response res = service.get(Service.endpointZaehlerarten+"/" + z.getId());

		switch (res.getStatus()) {
		case 200:
			Zaehlerart zServer = res.readEntity(Zaehlerart.class);
			if (!z.equals(zServer)) {
				/*System.out.println("DIFFTOOL");
				System.out.println(z.toString());
				System.out.println(zServer.toString());
				System.out.println("--------");//*/
				if (Util.optionMessage("Zaehlerart hat sich geändert \nTrotzdem speichern?")) {
					return ChangedState.doSave;
				} else {
					return ChangedState.noSave;
				}
			}
			return ChangedState.doSave;
		case 404:
			if (Util.optionMessage(
					"404 - Zaehlerart nicht gefunden, wurde die Zaehlerart gelöscht?\nTrotzdem speichern?")) {
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

	public void addChangeListener(Function<ArrayList<Zaehlerart>, Boolean> newF) {

		onChange.add(newF);
	}

	private void callOnChanged() {
		for (Function<ArrayList<Zaehlerart>, Boolean> f : onChange) {
			f.apply(liste);
		}
	}

	public String getNameById(int id) {
		Zaehlerart z=getById(id);
		if (z!=null) {
			return z.getName();
		} else {
			return "";
		}
	}
}
