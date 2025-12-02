package com.crimealert.CrimeAlert.Controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crimealert.CrimeAlert.Logic.CrimeLogic;
import com.crimealert.CrimeAlert.Model.CrimeModel;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/crime")
public class CrimeController {

    private final CrimeLogic _crimeLogic;

    public CrimeController(CrimeLogic crimeLogic) {
        _crimeLogic = crimeLogic;
    }

    @GetMapping("/")
    public ArrayList<CrimeModel> get(@RequestParam int radius) {
        return _crimeLogic.getCrimes(radius);
    }

    @PostMapping("/")
    public void post(@RequestBody HashMap<String, String> params) {
        String description = params.get("description");
        _crimeLogic.postCrime(description);
    }
}
