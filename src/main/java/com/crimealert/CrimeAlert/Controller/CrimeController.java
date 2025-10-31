//request is going to go here
//going to call logic

package com.crimealert.CrimeAlert.Controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crimealert.CrimeAlert.Config.DaoConfig;
import com.crimealert.CrimeAlert.Logic.CrimeLogic;
import com.crimealert.CrimeAlert.Model.CrimeModel;
import com.j256.ormlite.dao.Dao;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/crime")
public class CrimeController {

    private final CrimeLogic _crimeLogic;


    public CrimeController(CrimeLogic crimeLogic) {
        _crimeLogic = crimeLogic;
    }

    @GetMapping("/")
    public ArrayList<CrimeModel> get() {
        return new ArrayList<>();
    }
}
