package br.com.apioficina.model; // Ou o seu pacote model

import br.com.apioficina.model.enums.StatusOS; // Importa o Enum
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime; // Usamos LocalDateTime para data+hora
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ordem_de_servico")
@Data
public class OrdemDeServicoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_entrada")
    private LocalDateTime dataEntrada;

    @Column(name = "data_saida")
    private LocalDateTime dataSaida;

    // Converte o Enum para String no banco
    @Enumerated(EnumType.STRING)
    private StatusOS status;

    @Column(name = "valor_total")
    private BigDecimal valorTotal;

    // --- Relacionamentos Simples (ManyToOne) ---
    // Muitas OS podem ser de UM Cliente
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private ClienteEntity cliente;

    // Muitas OS podem ser de UM Veículo
    @ManyToOne
    @JoinColumn(name = "veiculo_id")
    private VeiculoEntity veiculo;

    // --- Relacionamentos Muitos-para-Muitos ---

    // Lista de serviços na OS
    @ManyToMany
    @JoinTable(
            name = "os_servicos", // Tabela join que criamos
            joinColumns = @JoinColumn(name = "ordem_id"), // Lado "dono" (OS)
            inverseJoinColumns = @JoinColumn(name = "servico_id") // Lado "outro" (Serviço)
    )
    private List<ServicoEntity> servicos = new ArrayList<>();

    // Lista de peças na OS
    @ManyToMany
    @JoinTable(
            name = "os_pecas", // Tabela join que criamos
            joinColumns = @JoinColumn(name = "ordem_id"), // Lado "dono" (OS)
            inverseJoinColumns = @JoinColumn(name = "peca_id") // Lado "outro" (Peça)
    )
    private List<PecaEntity> pecas = new ArrayList<>();
}