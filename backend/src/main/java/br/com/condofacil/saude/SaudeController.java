package br.com.condofacil.saude;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/saude")
public class SaudeController {


    @GetMapping
    public RespostaDeSaude consultarSaude() {
        return new RespostaDeSaude("ONLINE", "CondoFacil");
    }
}
