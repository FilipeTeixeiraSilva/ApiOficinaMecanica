package br.com.apioficina.dto;

import br.com.apioficina.model.enums.StatusOS;
import lombok.Data;

@Data
public class StatusUpdateDTO {
    private StatusOS novoStatus;
}
