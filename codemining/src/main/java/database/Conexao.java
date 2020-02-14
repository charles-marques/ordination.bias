package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexao {
	private String local;
	private String user;
	private String senha;
	private Connection c;
	private Statement statment;
	private String str_conexao;
	private String driverjdbc;

	public Conexao(String local, String porta, String banco, String user, String senha) {
		setStr_conexao("jdbc:postgresql://" + local + ":" + porta + "/" + banco);
		setLocal(local);
		setSenha(senha);
		setUser(user);
		setDriverjdbc("org.postgresql.Driver");
	}

	public void configUser(String user, String senha) {
		setUser(user);
		setSenha(senha);
	}

	public void configLocal(String banco) {
		setLocal(banco);
	}

	// Conexão com o Banco de Dados
	public void conect() {
		try {
			Class.forName(getDriverjdbc());
			setC(DriverManager.getConnection(getStr_conexao(), getUser(), getSenha()));
			setStatment(getC().createStatement());
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}

	public void disconect() {
		try {
			getC().close();
		} catch (SQLException ex) {
			System.err.println(ex);
			ex.printStackTrace();
		}
	}

	public ResultSet select(String query) {
		try {
			return getStatment().executeQuery(query);
		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public void insert(String query) throws SQLException {
		// statment.executeQuery(query);
		statment.execute(query);
		// getStatment().executeQuery(query);
		return;
	}

	// GETs AND SETs
	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Connection getC() {
		return c;
	}

	public void setC(Connection c) {
		this.c = c;
	}

	public Statement getStatment() {
		return statment;
	}

	public void setStatment(Statement statment) {
		this.statment = statment;
	}

	public String getStr_conexao() {
		return str_conexao;
	}

	public void setStr_conexao(String str_conexao) {
		this.str_conexao = str_conexao;
	}

	public String getDriverjdbc() {
		return driverjdbc;
	}

	public void setDriverjdbc(String driverjdbc) {
		this.driverjdbc = driverjdbc;
	}

}