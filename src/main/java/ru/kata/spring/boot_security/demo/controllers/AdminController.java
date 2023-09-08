package ru.kata.spring.boot_security.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Set;


@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String showAllUsers(Model model, Principal principal) {
        List<Role> roles = roleService.allRoles();
        model.addAttribute("allUsers", userService.allUsers());
        model.addAttribute("roles", roles);
        model.addAttribute("user",userService.getUserByUsername(principal.getName()));
        model.addAttribute("currentUser", userService.getUserByUsername(principal.getName()));
        return "allUsers";
    }

    @GetMapping("/addUser")
    public String newUser(@ModelAttribute("user") User user,
                          Model model, Principal principal) {
        model.addAttribute("user",userService.getUserByUsername(principal.getName()));
        model.addAttribute("roles", roleService.allRoles());
        return "/addUser";
    }

    @PostMapping("/new")
    public String addUser(@ModelAttribute("user") User user, @RequestParam("roles") Set<Long> roleIds) {
        user.setRoles(roleService.getRolesByIdIn(roleIds));
        userService.addUser(user);
        return "redirect:/admin/";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable("id") Long id, @RequestParam("roles") Set<Long> roleIds) {
        model.addAttribute("user", userService.getUser(id));
        model.addAttribute("roles", roleService.allRoles());
        return "edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@ModelAttribute("user") User user, BindingResult bindingResult,
                         @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "redirect:/admin/";
        }
        userService.updateUser(user);
        return "redirect:/admin/";
    }

    @GetMapping("/user")
    public String pageForAuthenticatedUsers(Model model, Principal principal) {
        model.addAttribute("user", userService.getUserByUsername(principal.getName()));
        return "user";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/";
    }

}
