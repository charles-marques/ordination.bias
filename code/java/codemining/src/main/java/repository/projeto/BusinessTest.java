package repository.projeto;

import java.util.Comparator;
import java.util.List;

public class BusinessTest {
    protected void ordenar(List<User> lista) {
		lista.sort(new Comparator<User>() {
			@Override
			public int compare(User user0, User user1) {
				return user0.getNick().compareTo(user1.getNick());
			}
		});
		return;
	}
}