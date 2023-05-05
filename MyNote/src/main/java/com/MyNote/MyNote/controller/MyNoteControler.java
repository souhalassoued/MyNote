package com.MyNote.MyNote.controller;

import com.MyNote.MyNote.model.Employe;
import com.MyNote.MyNote.repository.MyNoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/Employe")
public class MyNoteControler {
    @Autowired
    private MyNoteRepository EmployeRepository;

    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal Employe Employe) {
        return ResponseEntity.ok(Employe);
    }

    @GetMapping("/{id}")
    @PreAuthorize("#user.id == #id")
    public ResponseEntity<?> me(@AuthenticationPrincipal Employe Employe, @PathVariable String id) {
        return ResponseEntity.ok(EmployeRepository.findById(id));
    }
}
