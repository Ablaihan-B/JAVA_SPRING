package kz.iitu.csse.group34.services.impl;


import kz.iitu.csse.group34.entities.Users;
import kz.iitu.csse.group34.repositories.UserRepository;
import kz.iitu.csse.group34.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Users user = userRepository.findByEmail(s);
        if((user.getIsActive())){
            User secUser = new User(user.getEmail(), user.getPassword(), user.getRoles());
            return secUser;
        }
        return null;
    }

}