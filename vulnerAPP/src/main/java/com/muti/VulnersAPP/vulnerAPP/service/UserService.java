package com.muti.VulnersAPP.vulnerAPP.service;

import com.muti.VulnersAPP.vulnerAPP.model.Role;
import com.muti.VulnersAPP.vulnerAPP.model.User;
import com.muti.VulnersAPP.vulnerAPP.repository.RoleRepository;
import com.muti.VulnersAPP.vulnerAPP.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getUsersByEnabledIsTrue() {
        return userRepository.getUsersByEnabledIsTrue();
    }

    public Role getRoleFromUser(User user){
        String url = "jdbc:h2:mem:mydb";
        String userdb = "sa";
        String passwd = "";

        try (Connection con = DriverManager.getConnection(url,userdb,passwd);

             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * from PERSONAL_ROLES where USER_ID='"+user.getId()+"';")) {
            if (rs.isBeforeFirst()) {
                rs.next();
                String role_id= rs.getString("ROLE_ID");
                Role role = roleRepository.findById(Long.parseLong(role_id));
                return role;
            }

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(UserService.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return roleRepository.findByRole("USER");
    }


    public User getUserFromSession(String session){
        byte [] session_byte = Base64.getDecoder().decode(session);
        session = new String(session_byte);
        String username = session.split(":")[0];
        String password = session.split(":")[1];
        String name = authentication(username,password);
        User user = findByUsername(name);
        return user;
    }
    public String authentication(String username, String password) {
        String url = "jdbc:h2:mem:mydb";
        String user = "sa";
        String passwd = "";

        try (Connection con = DriverManager.getConnection(url,user,passwd);

             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * from PERSONAL where username='"+username+"' and password='"+password+"';")) {
            if (rs.isBeforeFirst()) {
                rs.next();
                String uname= rs.getString("USERNAME");

                return uname;
            }

        } catch (SQLException ex) {

            Logger lgr = Logger.getLogger(UserService.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return "";
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void saveUser(User user) {
        user.setRoles(Arrays.asList(roleRepository.findByRole("USER")));
        user.setEnabled(true);
        userRepository.save(user);
    }
}