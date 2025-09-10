
  package com.veterinarska.stanica.controller;
  
  import org.springframework.security.core.Authentication; import
  org.springframework.security.core.GrantedAuthority; import
  org.springframework.web.bind.annotation.*;
  
  import java.util.*;
  
  @RestController
  
  @RequestMapping("/api") public class WhoAmIController {
  
  public static record WhoAmIResponse(String email, List<String> roles,
  List<Map<String,String>> authorities) {}
  
  @GetMapping("/whoami") public WhoAmIResponse whoami(Authentication auth){
  String email = auth.getName(); List<String> roles =
  auth.getAuthorities().stream() .map(GrantedAuthority::getAuthority)
  .toList(); List<Map<String,String>> authorities = roles.stream() .map(r ->
  Map.of("authority", r)) .toList(); return new WhoAmIResponse(email, roles,
  authorities); } }
 