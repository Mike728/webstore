package info.mike.webstorev1.controllers;

import info.mike.webstorev1.commands.UserCommand;
import info.mike.webstorev1.domain.Role;
import info.mike.webstorev1.domain.User;
import info.mike.webstorev1.repository.UserRepository;
import info.mike.webstorev1.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;

@Controller
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/registration")
    public String showRegistrationForm(Model model, HttpServletRequest httpServletRequest) {
        if(httpServletRequest.getUserPrincipal() != null)
            return "redirect:/index";
        else {
            model.addAttribute("user", new UserCommand());
            return "registration";
        }
    }

    @PostMapping("/registration")
    public String registerUserAccount(@ModelAttribute("user") @Valid UserCommand userCommand,
                                      BindingResult bindingResult){

        userRepository.findByEmail(userCommand.getEmail()).ifPresent(u -> {
            bindingResult.rejectValue("email", null, "Podany email już istnieje.");
            });

        if(bindingResult.hasErrors()) {
            return "/registration";
        }
        userService.save(userCommand);
        return "redirect:/index";
    }

    @RequestMapping("/profile")
    public String viewProfile(Model model, HttpServletRequest httpServletRequest){
        User user = userRepository.findByEmail(httpServletRequest.getRemoteUser()).get();
        model.addAttribute("user", user);
        return "user/profile";
    }

    @RequestMapping("/profile/edit")
    public String editProfile(Model model, HttpServletRequest httpServletRequest){
        User user = userRepository.findByEmail(httpServletRequest.getRemoteUser()).get();
        model.addAttribute("user", user);
        return "user/editProfile";
    }

    @PostMapping("editedUser")
    public String saveEditedUser(@ModelAttribute("user") User user, BindingResult bindingResult, HttpServletRequest httpServletRequest) {
        User userFromDb = userRepository.findByEmail(httpServletRequest.getRemoteUser()).get();
        user.setPassword(userFromDb.getPassword());
        user.setEmail(userFromDb.getEmail());
        user.getAddress().setId(userFromDb.getAddress().getId());
        user.setRoles(Arrays.asList(new Role("ROLE_USER")));
        userRepository.save(user);
        return "redirect:/index";
    }

}
