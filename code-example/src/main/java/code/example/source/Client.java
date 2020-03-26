package code.example.source;

import java.util.ArrayList;
import java.util.List;

public class Client {
	protected String nome = "text";
	protected List<String> contato = new ArrayList<String>(0);
//	protected List contato;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
