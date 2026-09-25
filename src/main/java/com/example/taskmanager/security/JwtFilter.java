package com.example.taskmanager.security;
import com.example.taskmanager.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
@Component public class JwtFilter extends OncePerRequestFilter {
 private final JwtService jwt;private final UserRepository users;
 public JwtFilter(JwtService jwt,UserRepository users){this.jwt=jwt;this.users=users;}
 @Override protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain chain)throws ServletException,IOException{
  String auth=req.getHeader("Authorization");
  if(auth!=null&&auth.startsWith("Bearer ")&&SecurityContextHolder.getContext().getAuthentication()==null){
   try {String email=jwt.subject(auth.substring(7));users.findByEmail(email).ifPresent(u->SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(u.id,null,List.of())));}
   catch(JwtException|IllegalArgumentException ignored){SecurityContextHolder.clearContext();}
  }
  chain.doFilter(req,res);
 }
}
