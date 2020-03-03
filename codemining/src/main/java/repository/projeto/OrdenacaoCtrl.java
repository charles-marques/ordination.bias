package repository.projeto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OrdenacaoCtrl {
	List<Client> pessoas = new ArrayList<Client>();
	protected void ordenar() {
		Collections.sort(pessoas, new Comparator<Client>() {
			@Override
			public int compare(Client o1, Client o2) {
				return o1.getNome().compareTo(o2.getNome());
			}
		});
	}
}
