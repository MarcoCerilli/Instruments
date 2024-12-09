package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@Controller
public class MyController {

    @Autowired
    private JdbcTemp jdbcTemp;

    // Mostra la pagina del modulo
    @GetMapping("/form")
    public String getForm(Model model) {
        ArrayList<strumento> lista = jdbcTemp.getLista();
        model.addAttribute("lista", lista);
        return "form";
    }

    // Gestisce l'inserimento di un nuovo strumento
    @ResponseBody
    @PostMapping("/submit")
    public String gestisciForm(@RequestParam("nome") String nome, 
                                @RequestParam("marca") String marca,
                                @RequestParam("prezzo") double prezzo,
                                @RequestParam("url") String url) {
        jdbcTemp.insertstrumento(nome, marca, prezzo, url);
        return nome + " aggiunto con successo";
    }

    // Aggiorna il prezzo di uno strumento
    @ResponseBody
    @PostMapping("/update")
    public String updatePrezzo(@RequestParam("nome") String nome,
                                @RequestParam("prezzo") double prezzo) {
        jdbcTemp.setPrezzo(nome, prezzo);
        return nome + " aggiornato con successo";
    }
    @ResponseBody
	@PostMapping("/updateP")
	public String updatePezzi(@RequestParam("nome") String nome,
			@RequestParam("pezzi") int pezzi
			) {
		
		jdbcTemp.aggiungipezzi(nome, pezzi);
		return nome + " aggiornato con successo";
		
	}
    // Cancella uno strumento
    @ResponseBody
    @PostMapping("/delete")
    public String delete(@RequestParam("nome") String nome) {
        jdbcTemp.delete(nome);
        return nome + " cancellato con successo";
    }

    // Mostra la pagina principale del negozio
    @GetMapping("/")
    public String getStore(Model model) {
        ArrayList<strumento> lista = jdbcTemp.getLista();
        model.addAttribute("lista", lista);
        return "store";
    }

    // Gestisce l'acquisto di strumenti selezionati
    @PostMapping("/buy")
    public String recap(@RequestParam("selected") ArrayList<Integer> selected, Model model) {
        int somma = 0;
        ArrayList<strumento> lista = jdbcTemp.getLista();
        ArrayList<strumentoA> listaP = new ArrayList<>();

        for (int i = 0; i < lista.size(); i++) {
            if (selected.get(i) > 0) {
                somma += selected.get(i) * lista.get(i).getPrezzo();
                strumentoA acquistato = new strumentoA(
                        lista.get(i).getNome(),
                        lista.get(i).getMarca(),
                        lista.get(i).getPrezzo() * selected.get(i),
                        lista.get(i).getUrl(),
                        selected.get(i)
                );
                listaP.add(acquistato);
                jdbcTemp.change(lista.get(i).getNome(), selected.get(i));
            }
        }

        model.addAttribute("lista", listaP);
        model.addAttribute("somma", somma);
        return "recap";
    }

    }

