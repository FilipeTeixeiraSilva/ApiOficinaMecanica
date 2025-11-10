package br.com.apioficina.kafka; // Ou seu pacote kafka

import br.com.apioficina.config.KafkaTopicConfig; // Importa o nome do tópico
import br.com.apioficina.dto.OrcamentoEmailDTO; // Importa o DTO
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    // Esta anotação mágica faz o método "ouvir" o tópico
    @KafkaListener(
            topics = KafkaTopicConfig.TOPIC_NAME, // O tópico que definimos
            groupId = "grupo-orcamento", // O grupo que definimos no properties
            containerFactory = "kafkaListenerContainerFactory" // O nome padrão do Spring
    )
    public void escutarTopicoOrcamentos(@Payload OrcamentoEmailDTO orcamento) {

        // --- SIMULAÇÃO DO ENVIO DE E-MAIL ---
        // Aqui é onde o código real de envio de e-mail (com JavaMail, etc.) iria

        System.out.println("=============================================");
        System.out.println("=== 📧 SIMULANDO ENVIO DE E-MAIL 📧 ===");
        System.out.println("=============================================");
        System.out.println("Destinatário: " + orcamento.getEmailCliente());
        System.out.println("Cliente: " + orcamento.getNomeCliente());
        System.out.println("Assunto: Orçamento da OS #" + orcamento.getOsId());
        System.out.println("---------------------------------------------");
        System.out.println("Olá, " + orcamento.getNomeCliente() + "!");
        System.out.println("Seu orçamento para o veículo " + orcamento.getPlacaVeiculo() + " está pronto.");
        System.out.println("Valor Total: R$ " + orcamento.getValorTotal());
        System.out.println("Acesse nosso app para aprovar.");
        System.out.println("=============================================");
    }
}