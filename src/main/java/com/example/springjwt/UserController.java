package com.example.springjwt;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import
        org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller

public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTUtil jwtUtil;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(User user, RedirectAttributes redirectAttributes, HttpSession session) {
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null && user.getPassword().equals(existingUser.getPassword())) {
            String token = jwtUtil.generateToken(existingUser.getEmail());
            session.setAttribute("token", token); // Store token in session
            String userType = existingUser.getType();
            if (userType != null) {
                if (userType.equals("user")) {
                    return "redirect:/user-detail";
                } else if (userType.equals("admin")) {
                    return "redirect:/admin-detail";
                }
            }
        }
        return "redirect:/login?error";
    }


    @GetMapping("/user-detail")
    public String userDetail(Model model, HttpSession session) {
        String token = (String) session.getAttribute("token");
        if (token != null && jwtUtil.validateToken(token)) {
            // You can add additional logic here if needed
            return "user-detail";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/admin-detail")
    public String adminDetail(Model model, HttpSession session) {
        String token = (String) session.getAttribute("token");
        if (token != null && jwtUtil.validateToken(token)) {
            // You can add additional logic here if needed
            return "admin-detail";
        } else {
            return "redirect:/login";
        }
    }
}


