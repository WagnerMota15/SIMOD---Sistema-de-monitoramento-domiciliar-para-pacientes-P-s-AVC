package com.SIMOD.SIMOD.dto.vinculo;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDateTime;

// Fiz tudo numa só, pode e é aconselhavel separar depois

public record SolicitarVinculoRequest(
        @NotBlank (message = "CPF é obrigatório!")
        @CPF (message = "CPF invalido")
        String cpf,
        String observacao
        )
{

// listagem de pendentes
public record SolicitacaoResponse(
        String cpf,
        String nomeSolicitante,
        String emailSolicitante,
        String tipoUsuario,
        LocalDateTime dataSolicitacao,
        String observacao
) {}

// listagem de vínculos ativos
public record VinculoResponse(
        String cpf,
        String nome,
        String email,
        String telefone,
        LocalDateTime dataVinculo,
        String status
) {}}
