package victor.training.oo.structural.adapter.infra;

import lombok.RequiredArgsConstructor;
import victor.training.oo.structural.adapter.domain.ExternalUserService;
import victor.training.oo.structural.adapter.domain.User;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class LdapServiceAdapter implements ExternalUserService {
   private final LdapUserWebserviceClient wsClient;

   @Override
   public List<User> searchByUsername(String username) {
      return wsClient.search(username.toUpperCase(), null, null)
          .stream().map(this::convert).collect(toList());
   }

   private User convert(LdapUser ldapUser) {
      String fullName = ldapUser.getfName() + " " + ldapUser.getlName().toUpperCase();
      return new User(ldapUser.getuId(), fullName, ldapUser.getWorkEmail());
   }

}
