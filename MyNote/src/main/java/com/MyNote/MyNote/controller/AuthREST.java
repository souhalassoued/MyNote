package com.MyNote.MyNote.controller;

import com.MyNote.MyNote.model.RefreshToken;
import com.MyNote.MyNote.model.Employe;
import com.MyNote.MyNote.DTO.LoginDTO;
import com.MyNote.MyNote.DTO.SignupDTO;
import com.MyNote.MyNote.DTO.TokenDTO;
import com.MyNote.MyNote.jwt.JwtHelper;
import com.MyNote.MyNote.repository.RefreshTokenRepository;
import com.MyNote.MyNote.repository.MyNoteRepository;
import com.MyNote.MyNote.service.MyNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthREST {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    MyNoteRepository EmployeRepository;
    @Autowired
    JwtHelper jwtHelper;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    MyNoteService MyNoteService;

    @PostMapping("/login")
    @Transactional
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO dto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Employe Employe = (Employe) authentication.getPrincipal();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setOwner(Employe);
        refreshTokenRepository.save(refreshToken);

        String accessToken = jwtHelper.generateAccessToken(Employe);
        String refreshTokenString = jwtHelper.generateRefreshToken(Employe, refreshToken);

        return ResponseEntity.ok(new TokenDTO(Employe.getId(), accessToken, refreshTokenString));
    }

    @PostMapping("signup")
    @Transactional
    public ResponseEntity<?> signup(@Valid @RequestBody SignupDTO dto) {
        Employe Employe = new Employe(dto.getUsername(), dto.getEmail(), passwordEncoder.encode(dto.getPassword()));
        EmployeRepository.save(Employe);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setOwner(Employe);
        refreshTokenRepository.save(refreshToken);

        String accessToken = jwtHelper.generateAccessToken(Employe);
        String refreshTokenString = jwtHelper.generateRefreshToken(Employe, refreshToken);

        return ResponseEntity.ok(new TokenDTO(Employe.getId(), accessToken, refreshTokenString));
    }

    @PostMapping("logout")
    public ResponseEntity<?> logout(@RequestBody TokenDTO dto) {
        String refreshTokenString = dto.getRefreshToken();
        if (jwtHelper.validateRefreshToken(refreshTokenString) && refreshTokenRepository.existsById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString))) {
            // valid and exists in db
            refreshTokenRepository.deleteById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString));
            return ResponseEntity.ok().build();
        }

        throw new BadCredentialsException("invalid token");
    }

    @PostMapping("logout-all")
    public ResponseEntity<?> logoutAll(@RequestBody TokenDTO dto) {
        String refreshTokenString = dto.getRefreshToken();
        if (jwtHelper.validateRefreshToken(refreshTokenString) && refreshTokenRepository.existsById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString))) {
            // valid and exists in db

            refreshTokenRepository.deleteByOwner_Id(jwtHelper.getUserIdFromRefreshToken(refreshTokenString));
            return ResponseEntity.ok().build();
        }

        throw new BadCredentialsException("invalid token");
    }

    @PostMapping("access-token")
    public ResponseEntity<?> accessToken(@RequestBody TokenDTO dto) {
        String refreshTokenString = dto.getRefreshToken();
        if (jwtHelper.validateRefreshToken(refreshTokenString) && refreshTokenRepository.existsById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString))) {
            // valid and exists in db

            Employe Employe = MyNoteService.findById(jwtHelper.getUserIdFromRefreshToken(refreshTokenString));
            String accessToken = jwtHelper.generateAccessToken(Employe);

            return ResponseEntity.ok(new TokenDTO(Employe.getId(), accessToken, refreshTokenString));
        }

        throw new BadCredentialsException("invalid token");
    }

    @PostMapping("refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody TokenDTO dto) {
        String refreshTokenString = dto.getRefreshToken();
        if (jwtHelper.validateRefreshToken(refreshTokenString) && refreshTokenRepository.existsById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString))) {
            // valid and exists in db

            refreshTokenRepository.deleteById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString));

            Employe Employe = MyNoteService.findById(jwtHelper.getUserIdFromRefreshToken(refreshTokenString));

            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setOwner(Employe);
            refreshTokenRepository.save(refreshToken);

            String accessToken = jwtHelper.generateAccessToken(Employe);
            String newRefreshTokenString = jwtHelper.generateRefreshToken(Employe, refreshToken);

            return ResponseEntity.ok(new TokenDTO(Employe.getId(), accessToken, newRefreshTokenString));
        }

        throw new BadCredentialsException("invalid token");
    }
}