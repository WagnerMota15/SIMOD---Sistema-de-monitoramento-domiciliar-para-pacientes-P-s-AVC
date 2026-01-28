package com.SIMOD.SIMOD.controller.plansTreatment;

import com.SIMOD.SIMOD.domain.model.dieta.Diet;
import com.SIMOD.SIMOD.domain.model.medicamentos.Medicines;
import com.SIMOD.SIMOD.dto.plansTreatment.DietRequest;
import com.SIMOD.SIMOD.dto.plansTreatment.DietResponse;
import com.SIMOD.SIMOD.dto.plansTreatment.MedicinesRequest;
import com.SIMOD.SIMOD.dto.plansTreatment.MedicinesResponse;
import com.SIMOD.SIMOD.services.DietService;
import com.SIMOD.SIMOD.services.MedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/medicamentos")
@RequiredArgsConstructor
public class MedicinesController {
    private final MedicineService medicineService;

    // ====== MÉDICO ======

    @PostMapping("/medicos/prescrever/{patientId}")
    public ResponseEntity<MedicinesResponse> prescreverMedicamento(
            Authentication authentication,
            @PathVariable UUID patientId,
            @RequestBody MedicinesRequest request
    ) {
        Medicines medicines = medicineService.prescreverMedicamento(authentication, patientId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapToResponse(medicines));
    }

    @PutMapping("/medicos/editar/{medicineId}")
    public ResponseEntity<MedicinesResponse> editarMedicamento(
            Authentication authentication,
            @PathVariable UUID medicineId,
            @RequestBody MedicinesRequest request
    ) {
        MedicinesResponse response = medicineService.editarMedicamento(authentication, medicineId, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/medicos/inativar/{medicineId}")
    public ResponseEntity<Void> inativarDieta(
            Authentication authentication,
            @PathVariable UUID medicineId
    ) {
        medicineService.inativarMedicamento(authentication, medicineId);
        return ResponseEntity.noContent().build();
    }

    // ====== CONSULTAS ======

    @GetMapping("/usuarios/listar-medicamentos")
    public Page<MedicinesResponse> listarMedicamentos(
            Authentication authentication,
            Pageable pageable
    ) {
        return medicineService.listarMedicamentos(authentication, pageable);
    }

    @GetMapping("/usuarios/listar-medicamentos-ativos")
    public Page<MedicinesResponse> listarMedicamentosAtivos(
            Authentication authentication,
            Pageable pageable
    ) {
        return medicineService.listarMedicamentosAtivos(authentication, pageable);
    }

    @GetMapping("/usuarios/listar-medicamentos-inativos")
    public Page<MedicinesResponse> listarMedicamentosInativos(
            Authentication authentication,
            Pageable pageable
    ) {
        return medicineService.listarMedicamentosInativos(authentication, pageable);
    }

    @GetMapping("/usuarios/listar-medicamentos-paciente/{patientId}")
    public Page<MedicinesResponse> listarPorPaciente(
            Authentication authentication,
            @PathVariable UUID patientId,
            Pageable pageable
    ) {
        return medicineService.listarMedicamentosPorPaciente(authentication, patientId, pageable);
    }

    @GetMapping("/usuarios/listar-medicamento/{medicineId}")
    public MedicinesResponse buscarMedicamento(
            Authentication authentication,
            @PathVariable UUID medicineId
    ) {
        return medicineService.buscarMedicamento(authentication, medicineId);
    }

    private MedicinesResponse mapToResponse(Medicines medicines) {
        return new MedicinesResponse(
                medicines.getIdMedicine(),
                medicines.getName(),
                medicines.getDosage(),
                medicines.getUnity(),
                medicines.getFrequency(),
                medicines.getDescription(),
                medicines.getStatus(),
                medicines.getPatient().getIdUser(),
                medicines.getProfessional().getIdUser()
        );
    }
}
