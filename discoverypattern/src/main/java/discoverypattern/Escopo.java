package discoverypattern;

public enum Escopo {
	CLASSE("Classe"), METODO("Método"), PARAMETRO("Parâmetro");

	private Escopo(String label) {
		this.label = label;
	}

	private String label;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
