package spyclass;

public class Classe {
	private String projeto;
	private String nome;
	private String codigo;

	public Classe() {
		super();
	}

	public Classe(String projeto, String nome, String codigo) {
		this();
		this.projeto = projeto;
		this.nome = nome;
		this.codigo = codigo;
	}

	public String getProjeto() {
		return projeto;
	}

	public void setProjeto(String projeto) {
		this.projeto = projeto;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

}
