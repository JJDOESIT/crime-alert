package com.crimealert.CrimeAlert.Controller;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crimealert.CrimeAlert.Model.CrimeModel;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/crime")
public class CrimeController {

    @GetMapping("/")
    public ArrayList<CrimeModel> Get() {
        return new ArrayList<>();
    }
}
