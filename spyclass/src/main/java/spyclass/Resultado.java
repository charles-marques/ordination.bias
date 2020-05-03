package spyclass;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class Resultado {

	private static final String APP = "APP: ";
	private static final String CLASSES_ENCONTRADAS = "Classes Encontradas:";
	private static final String CLASSES_NAO_ENCONTRADAS = "Classes não encontradas";
	private static final String JAVA = ".java";
	private static final CharSequence VAZIO = "";

	protected static List<String> classesList = new ArrayList<String>();
	protected static List<Classe> selectedClassesList = new ArrayList<Classe>();

	public static List<String> getClassesList() {
		return classesList;
	}

	public static void setClassesList(List<String> classesList) {
		Resultado.classesList = classesList;
	}

	public static List<Classe> getSelectedClassesList() {
		return selectedClassesList;
	}

	public static void setSelectedClassesList(List<Classe> selectedClassesList) {
		Resultado.selectedClassesList = selectedClassesList;
	}

	public void addClass(String absolutePath) {
		if (classesList.stream().parallel().anyMatch(f -> f.contains(absolutePath)))
			return;
		classesList.add(absolutePath);
	}

	public void addSelectedClass(String projeto, String className, String codigo) {
		if (selectedClassesList.stream().parallel()
				.anyMatch(f -> f.getNome().contains(className.replace(JAVA, VAZIO)) && f.getCodigo().equals(codigo)))
			return;
		selectedClassesList.add(new Classe(projeto, className, codigo));
	}

	public void printFoundClasses(String projeto) {
		System.out.println("******************************************");
		System.out.println(APP + projeto);
		if (selectedClassesList.isEmpty()) {
			System.out.println(CLASSES_NAO_ENCONTRADAS);
		} else {
			System.out.println(CLASSES_ENCONTRADAS);
			selectedClassesList.forEach(c -> System.out.println(c.getNome()));
		}
		System.out.println("******************************************");
	}

	public void writeCsvFiles() {
		PrintWriter writerClasses;
		PrintWriter writerSelected;
		try {
			writerClasses = new PrintWriter("classes.csv");
			classesList.forEach(c -> writerClasses.write(c + System.lineSeparator()));
			writerClasses.close();
			writerSelected = new PrintWriter("selected_classes.csv");
			selectedClassesList.forEach(s -> writerSelected.write(s.getNome() + System.lineSeparator()));
			writerSelected.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	public void writeJsonFile() {
		PrintWriter writer = null;
		Gson gson = new Gson();
		try {
			String json = gson.toJson(this.getClassesList());
			writer = new PrintWriter("resultado.json");
			writer.write(json);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			writer.close();
		}
	}
}
