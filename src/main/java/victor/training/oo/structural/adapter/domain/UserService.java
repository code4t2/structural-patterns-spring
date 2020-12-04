package victor.training.oo.structural.adapter.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.oo.structural.adapter.external.LdapUser;
import victor.training.oo.structural.adapter.external.LdapUserWebserviceClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
// Domain Service
// PEace, harmony, ZEN
public class UserService {
	private final LdapUserWebserviceClient wsClient;

	public void importUserFromLdap(String username) {
		List<User> list = searchByUsername(username);
		if (list.size() != 1) {
			throw new IllegalArgumentException("There is no single user matching username " + username);
		}
		User user = list.get(0);

		if (user.getWorkEmail() != null) {
			log.debug("Send welcome email to " + user.getWorkEmail());
		}
		log.debug("Insert user in my database");
	}

	//200 lines below
	public List<User> searchUserInLdap(String username) {
		return searchByUsername(username);
	}


	//` -=-==== a line ================================================================`
	/// extracted duplication

	private User convert(LdapUser ldapUser) {
		String fullName = ldapUser.getfName() + " " + ldapUser.getlName().toUpperCase();
		return new User(ldapUser.getuId(), fullName, ldapUser.getWorkEmail());
	}

	private List<User> searchByUsername(String username) {
		return wsClient.search(username.toUpperCase(), null, null).stream()
			.map(this::convert)
			.collect(Collectors.toList());
	}

}
