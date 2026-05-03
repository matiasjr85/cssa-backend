package br.com.clubedossargentos.dto;

import br.com.clubedossargentos.enums.StatusSocio;
import jakarta.validation.constraints.NotNull;

public class StatusSocioRequestDTO {

    @NotNull(message = "Status é obrigatório.")
    private StatusSocio status;

    public StatusSocio getStatus() {
        return status;
    }

    public void setStatus(StatusSocio status) {
        this.status = status;
    }
}