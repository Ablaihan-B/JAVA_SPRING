package kz.iitu.csse.group34.controllers;



import kz.iitu.csse.group34.entities.Users;
import kz.iitu.csse.group34.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class BaseController {

    @Autowired
    private UserRepository userRepository;

    public Users getUserData(){
        Users userData = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            User secUser = (User)authentication.getPrincipal();
            userData = userRepository.findByDeletedAtNullAndEmail(secUser.getUsername());
        }
        return userData;
    }

}
