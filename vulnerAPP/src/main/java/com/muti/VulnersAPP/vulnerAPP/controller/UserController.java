package com.muti.VulnersAPP.vulnerAPP.controller;

import com.muti.VulnersAPP.vulnerAPP.model.Role;
import com.muti.VulnersAPP.vulnerAPP.model.User;
import com.muti.VulnersAPP.vulnerAPP.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.Convert;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.sql.*;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


@Controller
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String saveRegisterPage(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        model.addAttribute("user", user);

        if (result.hasErrors()) {
            return "register";
        } else {
            userService.saveUser(user);

        }
        return "login";
    }


    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/index")
    public String index2() {
        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value= "/login", method = RequestMethod.POST)
    public String loginn(Model model, HttpServletRequest request,
                         HttpServletResponse response, @Valid @ModelAttribute("username") String username, @Valid @ModelAttribute("password") String password ) {
        String name = userService.authentication(username,password);
        if (name !="" ) {
            if(name.contains("script")) {
                return "login";
            }else {
                String session = name + ":" + password;
                session = Base64.getEncoder().encodeToString(session.getBytes());
                Cookie myCookie = new Cookie("session", session);
                response.addCookie(myCookie);

                User user = userService.findByUsername(name);
                Role role = userService.getRoleFromUser(user);
                model.addAttribute("role", role);
                model.addAttribute("user", user);
                return "secure";
            }
        }
        else{
            model.addAttribute("error", true);
            model.addAttribute("username", "");
            model.addAttribute("password", "");
            return "login";
        }
    }

    @RequestMapping("/profile")
    public String profile(Model model, HttpServletRequest request, @RequestParam(required = false) String id){
        if(id==null) {
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("session")) {
                    String session = cookie.getValue();
                    User user = userService.getUserFromSession(session);
                    id = Long.toString(user.getId());
                    return "redirect:/profile?id="+id;
                }
            }
            return "index";
        }
        String url = "jdbc:h2:mem:mydb";
        String user = "sa";
        String passwd = "";

        try (Connection con = DriverManager.getConnection(url,user,passwd);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * from PERSONAL where id='"+id+"';")) {
            if (rs.isBeforeFirst()) {
                rs.next();
                String username= rs.getString("USERNAME");
                User useri = userService.findByUsername(username);
                model.addAttribute("user", useri);
            }

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(UserService.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return "profile";
    }

    @RequestMapping("/secure")
    public String secure(Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("session")) {
                String session=cookie.getValue();
                User user = userService.getUserFromSession(session);
                Role role = userService.getRoleFromUser(user);
                model.addAttribute("role", role);
                model.addAttribute("user", user);
                return "secure";
            }
        }
        return "index";
    }

    @RequestMapping("userlist")
    public String userslist(Model model, HttpServletRequest request, HttpServletResponse response) {
        List<User> users = userService.getUsersByEnabledIsTrue();
        model.addAttribute("users", users);
        return "userlist";
    }

    @RequestMapping("logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("session", "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "index";
    }
}


