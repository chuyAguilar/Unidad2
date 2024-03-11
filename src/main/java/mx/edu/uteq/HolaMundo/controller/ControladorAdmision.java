/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Controller.java to edit this template
 */
package mx.edu.uteq.HolaMundo.controller;

import jakarta.validation.Valid;
import static java.lang.Math.log;
import static java.lang.System.console;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;
import mx.edu.uteq.HolaMundo.entity.Admision;
import mx.edu.uteq.HolaMundo.repository.AdmisionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author fonse
 */
@Controller
public class ControladorAdmision {

    private static final Logger log = LoggerFactory.getLogger(ControladorAdmision.class);

    @Autowired
    private AdmisionRepo repo;
    String menuInicio = "";
    String menuOferta = "";
    String menuAdmisiones = "";

    @GetMapping("/admisiones")
    public String page(Model model) {
        menuInicio = "nav-link";
        menuOferta = "nav-link";
        menuAdmisiones = "nav-link active fw-bold";
        model.addAttribute("styleInicio", menuInicio);
        model.addAttribute("styleOferta", menuOferta);
        model.addAttribute("styleAdminiones", menuAdmisiones);
        return "admisiones";
    }

    @RequestMapping(value = "/api/admisiones", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List obtenerAdmisiones() {
        List<Admision> lista = (List<Admision>) repo.findAll();
        return lista;
    }

    @RequestMapping(value = "/guardar-admision",
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<?> guardar(@Valid @RequestBody Admision admision,
            Errors error) throws Exception {

        if (error.hasErrors()) {
            throw new Exception(error.toString());
        }
        Admision admisionGuardada;
        Long admisi = admision.getId();
        if (admisi != null && admisi != 0) {
            Admision admisionExistente = repo.findById(admision.getId()).orElse(null);
            if (admisionExistente != null) {
                admisionExistente.setNombreAdmision(admision.getNombreAdmision());
                admisionExistente.setActivo(admision.isActivo());
                admisionGuardada = repo.save(admisionExistente);

            } else {
                throw new Exception("No se encontró la admisión con ID: " + admision.getId());
            }
        } else {
            admisionGuardada = repo.save(admision);
        }
        return new ResponseEntity<>(admisionGuardada, HttpStatus.CREATED);
    }

    @GetMapping(value = "/api/admisiones-editar/{id}", produces = "application/json")
    public @ResponseBody
    Admision buscar(Admision admision, Model model) {
        admision = repo.findById(Long.valueOf(admision.getId())).orElseThrow();
        return admision;
    }

    @RequestMapping(value = "/admisiones-borrar/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> borrar(@PathVariable Long id) {
        try {
            Admision admisionExistente = repo.findById(id).orElse(null);

            if (admisionExistente != null) {
                repo.deleteById(id);
                return new ResponseEntity<>("Registro eliminado correctamente", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No se encontró la admisión con ID: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Hubo un error al intentar eliminar el registro: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }
}
