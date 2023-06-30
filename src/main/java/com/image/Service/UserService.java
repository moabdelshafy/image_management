package com.image.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.image.Model.Privilege;
import com.image.Model.Role;
import com.image.Model.User;
import com.image.Repository.PrivilegeRepository;
import com.image.Repository.RoleRepository;
import com.image.Repository.UserRepository;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("This user is not exist");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            role.getPrivileges().forEach(privilege -> {
                authorities.add(new SimpleGrantedAuthority(privilege.getName()));
            });
            authorities.add(new SimpleGrantedAuthority(role.getName()));

        });
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);

    }

    public User save(User user) {
        Role roleUser = roleRepository.findByName("ROLE_USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(roleUser);
        userRepository.save(user);
        return user;
    }


    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public void createAdmin() {
        User admin = null;
        List<Privilege> AllPrivileges = Arrays.asList(new Privilege("ADD"), new Privilege("UPDATE"),
                new Privilege("DELETE"), new Privilege("VIEW"));
        List<Privilege> adminPrivileges = privilegeRepository.saveAll(AllPrivileges);
        List<Privilege> userPrivileges = privilegeRepository.findByNameIn(Arrays.asList("ADD","VIEW"));

        Role adminRoles = new Role("ROLE_ADMIN", adminPrivileges);
        Role userRoles = new Role("ROLE_USER", userPrivileges);
        List<Role> roles = Arrays.asList(adminRoles, userRoles);
        roleRepository.saveAll(roles);

        admin = new User("admin@gmail.com", passwordEncoder.encode("admin"), List.of(adminRoles));
        userRepository.save(admin);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
