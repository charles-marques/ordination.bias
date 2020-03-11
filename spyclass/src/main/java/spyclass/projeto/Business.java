package spyclass.projeto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Business {
	private List<String> cadastros;
	private List<Client> clientes;

	protected void checar() {
		Collections.sort(cadastros);
		cadastros.forEach(System.out::println);
		List<Client> novosClientes;

		clientes.sort(new Comparator<Client>() {
			@Override
			public int compare(Client cliente1, Client cliente2) {
				return cliente1.getNome().compareTo(cliente2.getNome());
			}
		});
		novosClientes = new ArrayList<>();
		clientes.addAll(novosClientes);
	}
}
