/*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Controller.java to edit this template
 */
package mx.edu.uteq.HolaMundo.controller;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import mx.edu.uteq.HolaMundo.entity.OcupacionProfesional;
import mx.edu.uteq.HolaMundo.entity.OfertaEducativa;
import mx.edu.uteq.HolaMundo.repository.OcupacionRepo;
import mx.edu.uteq.HolaMundo.repository.OfertaEducativaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author fonse
 */
@Controller
public class ControladorOfertaEducativa {

    @Autowired
    private OfertaEducativaRepo repo;
    @Autowired
    private OcupacionRepo ocupacionRepo;

    String menuInicio = "";
    String menuOferta = "";
    String menuAdmisiones = "";

    @GetMapping("/ofertaeducativa")
    public String paginaOfertaEducativa(Model model) {
        menuInicio = "nav-link";
        menuOferta = "nav-link active fw-bold";
        menuAdmisiones = "nav-link";
        List<OfertaEducativa> datos = (List<OfertaEducativa>) repo.findAll();

        model.addAttribute("styleInicio", menuInicio);
        model.addAttribute("styleOferta", menuOferta);
        model.addAttribute("styleAdminiones", menuAdmisiones);
        model.addAttribute("datos", datos);

        return "ofertaeducativa";
    }

    @RequestMapping(value = "/api/ofertaeducativa", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List obtenerOfertas() {
        List<OfertaEducativa> lista = (List<OfertaEducativa>) repo.findAll();
        return lista;
    }

    @GetMapping("/agregarOferta")
    public String mostrarPaginaAgregarOferta(Model model) {
        model.addAttribute("oferta", new OfertaEducativa());
        model.addAttribute("styleInicio", menuInicio);
        model.addAttribute("styleOferta", menuOferta);
        model.addAttribute("styleAdminiones", menuAdmisiones);
        return "agregarOferta";
    }

    @PostMapping("/guardar-oferta")
    public String guardarOferta(@Valid @ModelAttribute("oferta") OfertaEducativa oferta, Errors errores) {
        if (errores.hasErrors()) {
            return "agregarOferta";
        }
        repo.save(oferta);
        return "redirect:/ofertaeducativa";
    }

    //Para traer el id
    @GetMapping(value = "/modificarOferta-js/{id}", produces = "application/json")
    public @ResponseBody
    OfertaEducativa buscar(OfertaEducativa oferta, Model model) {
        oferta = repo.findById(Long.valueOf(oferta.getId())).orElseThrow();
        return oferta;
    }
//Para los datos en el formulario e ir al html

    @GetMapping("/modificarOferta")
    public String mostrarPaginaModificarOferta(@RequestParam Long id, Model model) {
        model.addAttribute("styleInicio", menuInicio);
        model.addAttribute("styleOferta", menuOferta);
        model.addAttribute("styleAdminiones", menuAdmisiones);
        OfertaEducativa oferta = repo.findById(id).orElse(null);

        if (oferta != null) {
            model.addAttribute("oferta", oferta);
            return "modificarOferta";
        } else {
            return "redirect:/ofertaeducativa";
        }
    }

//Para guardar la oferta
    @PostMapping("/guardar-modificacion-oferta")
    public String guardar(@Valid @ModelAttribute("oferta") OfertaEducativa oferta,
            @RequestParam(name = "ocupa", required = false) String[] ocupaciones,
            @RequestParam(name = "idOc", required = false) String[] idOc, Errors errores) {

        if (errores.hasErrors()) {
            return "redirect:/modificarOferta";
        }
        if (ocupaciones != null) {
            List<OcupacionProfesional> listaO = new ArrayList<>();
            for (int i = 0; i < ocupaciones.length; i++) {
                OcupacionProfesional o = new OcupacionProfesional();
                o.setId(Integer.parseInt(idOc[i]));
                o.setOcupacion(ocupaciones[i]);
                listaO.add(o);
            }
            oferta.setOcupaciones(listaO);
        }
        repo.save(oferta);
        return "redirect:/ofertaeducativa";
    }

    @GetMapping("/eliminarOferta")
    public String eliminarOferta(@RequestParam Long id) {
        repo.deleteById(id);
        return "redirect:/ofertaeducativa";
    }

}
