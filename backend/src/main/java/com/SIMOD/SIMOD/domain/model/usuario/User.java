package com.SIMOD.SIMOD.domain.model.usuario;

import com.SIMOD.SIMOD.domain.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.util.UUID;

@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)

//coluna no bd que separa os tipos de usuários(paciente,cuidador,profissional)
@DiscriminatorColumn(name = "user_type")

public abstract class User {

    public UUID getId() {
        return idUser;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idUser;
    @CPF(message = "CPF inválido")
    private String cpf;
    private String nameComplete;
    private String email;
    private String password;
    private String telephone;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

}