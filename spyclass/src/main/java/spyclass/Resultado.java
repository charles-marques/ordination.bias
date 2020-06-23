package spyclass;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class Resultado {

	private final String FILE_NAME = "classes.csv";
	private final String FILE_COLUNMS = "project_name, insterested_class_name, local_sorting_classe, line, variable_scope";
	private List<Classe> classesList = new ArrayList<Classe>();

	public Resultado() {
		this.classesList = new ArrayList<Classe>();
	}

	public List<Classe> getClassesList() {
		return classesList;
	}

	public void setClassesList(List<Classe> classesList) {
		this.classesList = classesList;
	}

	public void addClass(Classe classe) {
		if (classesList.stream().parallel().anyMatch(f -> f.equals(classe)))
			return;
		classesList.add(classe);
	}

	public void writeCsvFiles() {
		PrintWriter writerClasses;
		try {
			writerClasses = new PrintWriter(FILE_NAME);
			writerClasses.write(FILE_COLUNMS);
			classesList.forEach(c -> writerClasses.write(c + System.lineSeparator()));
			writerClasses.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	public void writeJsonFile() {
		PrintWriter writer = null;
		Gson gson = new Gson();
		try {
			String json = gson.toJson(this);
			writer = new PrintWriter("resultado.json");
			writer.write(json);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			writer.close();
		}
	}
}
