package br.com.apioficina.model;
import br.com.apioficina.model.enums.TipoUsuario;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "usuario")
public class UsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario usuario;

    @Column(unique = true, length = 11, nullable = false)
    private String cpf;

    @Column(nullable = false)
    private String senha;
}
