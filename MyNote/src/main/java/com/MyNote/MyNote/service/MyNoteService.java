package com.MyNote.MyNote.service;

import com.MyNote.MyNote.model.Employe;
import com.MyNote.MyNote.repository.MyNoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class MyNoteService implements UserDetailsService {
@Autowired

MyNoteRepository MyNoteRepository;
    @Override
    public Employe loadUserByUsername(String username) throws UsernameNotFoundException {
        return MyNoteRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));
    }

    public Employe findById(String id) {
        return MyNoteRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("user id not found"));
    }

}
