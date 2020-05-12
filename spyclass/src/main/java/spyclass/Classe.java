package spyclass;

public class Classe {
	private static final String COLUNM_SEPARATOR = "'; '";
	private static final String ASPAS_SIMPLES = "'";
	private String className;
	private String classPath;
	private Boolean classRepresentative;

	public Classe(String className, String classPath, Boolean classRepresentative) {
		this.className = className;
		this.classPath = classPath;
		this.classRepresentative = classRepresentative;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassPath() {
		return classPath;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}

	public Boolean getClassRepresentative() {
		return classRepresentative;
	}

	public void setClassRepresentative(Boolean classRepresentative) {
		this.classRepresentative = classRepresentative;
	}

	@Override
	public String toString() {
		return ASPAS_SIMPLES + className + COLUNM_SEPARATOR + classPath + COLUNM_SEPARATOR
				+ (classRepresentative ? "Sim" : "Não") + ASPAS_SIMPLES;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + className.hashCode();
		hash = 31 * hash + (classPath == null ? 0 : classPath.hashCode());
		hash = 31 * hash + (classRepresentative == null ? 0 : classRepresentative.hashCode());
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
		return className.equals(classe.className)
				&& (classPath.equals(classe.classPath) && classRepresentative.equals(classe.classRepresentative));
	}
}
