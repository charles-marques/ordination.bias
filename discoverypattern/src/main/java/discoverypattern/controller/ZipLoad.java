package discoverypattern.controller;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class ZipLoad {

	private static final String SEPARATOR = File.separator;
	private static final String PONTO = "\\.";
	private static final String SRC = "src";
	private static final String MAIN = "main";
	private static final String JAVA = "java";
	private static final String RECURSO = SRC + SEPARATOR + MAIN + SEPARATOR + JAVA;
	private static final String EXT_JAVA = ".java";

	public boolean contains(String path, String canonicalName) {
		try {
			File meuRecurso = new File(path);
			ZipFile zipFile = new ZipFile(meuRecurso);
			Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
			while (zipEntries.hasMoreElements()) {
				ZipEntry zipEntry = (ZipEntry) zipEntries.nextElement();
				if (zipEntry.getName().contains(RECURSO)) {
					System.out.println(zipEntry.getName());
				}
			}

			String[] caminho = path.split(SEPARATOR);
			String root = caminho[caminho.length - 1].replace(".zip", "");
			String canonicalPath = canonicalName.replaceAll(PONTO, SEPARATOR);
			String caminhoCompleto = root + SEPARATOR + RECURSO + SEPARATOR + canonicalPath + EXT_JAVA;
			ZipEntry entry = zipFile.getEntry(caminhoCompleto);

			Boolean resultado = (entry != null) ? true : false;
			zipFile.close();
			return resultado;
		} catch (ZipException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args) {
		String path = "/home/suporte/Workspace/Teste/spyclass.zip";
		String nomeClasse = "spyclass.controller.SpyClass";
		System.out.println("Class: " + nomeClasse);

		ZipLoad zipLoad = new ZipLoad();
		boolean contains = zipLoad.contains(path, nomeClasse);
		System.out.println("Contains: " + contains);
	}
}
