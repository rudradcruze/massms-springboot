package ac.dia.massms.config;

import ac.dia.massms.model.User;
import ac.dia.massms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		User user = userRepository.getUserByUsername(username);

		if (user == null) {
			throw new UsernameNotFoundException("Could not find user");
		}

		return new MyUserDetails(user);
	}

	public User getById(long id) {
		return userRepository.getUserById(id);
	}

	public void save(User user) {
		userRepository.save(user);
	}

	public User getByUserName(String userName) {
		return userRepository.getUserByUsername(userName);
	}

	public List<User> listALl() {
		return (List<User>) userRepository.findAll();
	}

	public List<User> listByRollName(String role) {
		return userRepository.findByRolesName(role);
	}

	public void updateMassList(User user) {
		User newUser = userRepository.getUserById(user.getId());
		newUser.setMassList(user.getMassList());
	}
}


