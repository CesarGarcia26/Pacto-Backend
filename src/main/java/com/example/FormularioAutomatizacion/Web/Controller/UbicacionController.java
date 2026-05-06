package com.example.FormularioAutomatizacion.Web.Controller;

import com.example.FormularioAutomatizacion.Dto.CiudadDto;
import com.example.FormularioAutomatizacion.Dto.DepartamentoDto;
import com.example.FormularioAutomatizacion.Dto.EnfermedaDto;
import com.example.FormularioAutomatizacion.Service.EnfermedadService;
import com.example.FormularioAutomatizacion.Service.iServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/ubicacion")

public class UbicacionController {

    @Autowired
    private iServiceImpl ubicacionService;

    @Autowired
    private EnfermedadService enfermedadService;

    // -------------------- UBICACIÓN --------------------

    @GetMapping("/departamentos")
    public List<DepartamentoDto> obtenerDepartamentos() {
        return ubicacionService.obtenerTodosDepartamentos();
    }

    @GetMapping("/ciudades/{departamentoId}")
    public List<CiudadDto> obtenerCiudadesPorDepartamento(
            @PathVariable Long departamentoId) {
        return ubicacionService.obtenerCiudadesPorDepartamento(departamentoId);
    }

    @GetMapping("/departamento/{id}")
    public DepartamentoDto obtenerDepartamento(@PathVariable Long id) {
        return ubicacionService.obtenerDepartamentoPorId(id);
    }

    // -------------------- ENFERMEDADES --------------------

    @GetMapping("/enfermedades")
    public List<EnfermedaDto> obtenerEnfermedades() {
        return enfermedadService.obtenerEnfermedades();
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body("No autenticado");
        }
        return ResponseEntity.ok(authentication.getName());
    }
}
