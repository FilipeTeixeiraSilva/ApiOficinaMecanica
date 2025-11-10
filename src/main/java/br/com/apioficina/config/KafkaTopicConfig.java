package br.com.apioficina.config; // Ou seu pacote de config

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    // Define o nome do nosso tópico
    public static final String TOPIC_NAME = "orcamentos-email";

    @Bean
    public NewTopic orcamentoTopic() {
        // Cria o tópico no Kafka quando a aplicação iniciar
        return TopicBuilder.name(TOPIC_NAME)
                .partitions(1) // 1 partição é suficiente
                .replicas(1)   // 1 réplica (padrão para desenvolvimento)
                .build();
    }
}