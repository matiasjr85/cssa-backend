package br.com.clubedossargentos.dto;

public class StatusPublicoDTO {
    private boolean possuiUsuarios;
    public StatusPublicoDTO() {}
    public StatusPublicoDTO(boolean possuiUsuarios) { this.possuiUsuarios = possuiUsuarios; }
    public boolean isPossuiUsuarios() { return possuiUsuarios; }
    public void setPossuiUsuarios(boolean possuiUsuarios) { this.possuiUsuarios = possuiUsuarios; }
}
