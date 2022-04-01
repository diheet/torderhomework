package com.homework.torder.controller;

import com.homework.torder.dto.JwtResponse;
import com.homework.torder.dto.UserDto;
import com.homework.torder.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody UserDto userDto) throws Exception {
        System.out.println(userDto.getUsername() + userDto.getPassword());
        System.out.println(passwordEncoder.encode(userDto.getPassword()));

        authenticate(userDto.getUsername(), userDto.getPassword());

        final UserDetails userDetails = userDetailsService.loadUserByUsername(userDto.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token, userDetails.getUsername()));
    }
//로그인 토큰 발급

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
//있는지 없는지 판별리