package spyclass.projeto;

public class User implements Comparable<User> {
	private String nick;

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	@Override
	public int compareTo(User arg0) {
		return 0;
	}
}
