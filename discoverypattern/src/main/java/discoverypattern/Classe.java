package discoverypattern;

public class Classe {
	private static final String COLUNM_SEPARATOR = "'; '";
	private static final String ASPAS_SIMPLES = "'";
	private String projetName;
	private String interestedClass;
	private String localSortClass;
	private Integer line;
	private String variableScope;
	private String statement;

	public Classe() {
		super();
	}

	public Classe(String projetName, String interestedClass, String localSortClass, Integer line, String variableScope,
			String statement) {
		this();
		this.projetName = projetName;
		this.interestedClass = interestedClass;
		this.localSortClass = localSortClass;
		this.line = line;
		this.variableScope = variableScope;

	}

	public String getProjetName() {
		return projetName;
	}

	public void setProjetName(String projetName) {
		this.projetName = projetName;
	}

	public String getInterestedClass() {
		return interestedClass;
	}

	public void setInterestedClass(String interestedClass) {
		this.interestedClass = interestedClass;
	}

	public String getLocalSortClass() {
		return localSortClass;
	}

	public void setLocalSortClass(String localSortClass) {
		this.localSortClass = localSortClass;
	}

	public Integer getLine() {
		return line;
	}

	public void setLine(Integer line) {
		this.line = line;
	}

	public String getVariableScope() {
		return variableScope;
	}

	public void setVariableScope(String variableScope) {
		this.variableScope = variableScope;
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	@Override
	public String toString() {
		return ASPAS_SIMPLES + projetName + COLUNM_SEPARATOR + interestedClass + COLUNM_SEPARATOR + localSortClass
				+ COLUNM_SEPARATOR + line + COLUNM_SEPARATOR + variableScope + COLUNM_SEPARATOR + statement
				+ ASPAS_SIMPLES;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + projetName.hashCode();
		hash = 31 * hash + interestedClass.hashCode();
		hash = 31 * hash + localSortClass.hashCode();
		hash = 31 * hash + line.hashCode();
		hash = 31 * hash + variableScope.hashCode();
		hash = 31 * hash + statement.hashCode();
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		Classe classe = (Classe) obj;
		return projetName.equals(classe.projetName) && interestedClass.equals(classe.interestedClass)
				&& localSortClass.equals(classe.localSortClass) && line.equals(classe.line)
				&& variableScope.equals(classe.variableScope);
	}
}