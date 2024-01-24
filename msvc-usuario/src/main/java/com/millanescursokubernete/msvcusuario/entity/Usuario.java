package com.millanescursokubernete.msvcusuario.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank
    private String nombre;


    @NotBlank
    private String password;


    @NotEmpty(message = "El email no puede ser vacio")
    @Email(message = "No es una dirección de correo bien formada")
    @Column(unique = true, length = 20)
    private String email;


    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
