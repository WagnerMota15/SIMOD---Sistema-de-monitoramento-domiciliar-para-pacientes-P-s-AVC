package com.SIMOD.SIMOD.domain.usuario;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)

//coluna no bd que separa os tipos de usuários(paciente,cuidador,profissional)
@DiscriminatorColumn(name = "tipo_usuario")

public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String CPF;
    private String nomeCompleto;
    private String email;
    private String password;
    private String telephone;
}