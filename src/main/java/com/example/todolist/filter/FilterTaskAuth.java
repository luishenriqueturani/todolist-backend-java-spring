package com.example.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.todolist.user.IUserRepository;
import com.example.todolist.user.User;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

  @Autowired
  private IUserRepository userRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    String servletPath = request.getServletPath();

    if(servletPath.startsWith("/task/")){
      //pega o basic authorization
      String auth = request.getHeader("Authorization");
      String authEncode = auth.substring("Basic".length()).trim();

      byte[] authDecode = Base64.getDecoder().decode(authEncode);

      String authStr = new String(authDecode);

      String[] credentials = authStr.split(":");

      //buscar usuário
      User user = this.userRepository.findByUserName(credentials[0]);
      if(user == null){
        response.sendError(HttpStatus.UNAUTHORIZED.value());
        return;
      }

      //validar senha
      var passwordVerify = BCrypt.verifyer().verify(credentials[1].toCharArray(), user.getPassword());

      if (!passwordVerify.verified){
        response.sendError(HttpStatus.UNAUTHORIZED.value());
        return;
      }

      request.setAttribute("userId", user.getId());
    }

    filterChain.doFilter(request, response);
  }
}
