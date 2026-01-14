//package com.SIMOD.SIMOD.controller;
//
//import com.SIMOD.SIMOD.domain.model.paciente.Patient;
//import com.SIMOD.SIMOD.dto.auth.RegisterPatientRequest;
//import com.SIMOD.SIMOD.services.PatientService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/dto")
//
//
//public class PatientController {
//
//    @Autowired
//    PatientService patientService;
//
//
//    @PostMapping
//    public ResponseEntity<Patient> create(@RequestBody RegisterPatientRequest body){
//        Patient paciente = this.patientService.criarPaciente(body);
//        return ResponseEntity.ok(paciente);
//    }
//}
