package br.com.clubedossargentos.dto;

import br.com.clubedossargentos.enums.TipoSocio;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public class PessoaRequestDTO {
    @NotBlank(message = "Nome é obrigatório.")
    private String nome;

    @NotBlank(message = "CPF é obrigatório.")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 números.")
    private String cpf;

    @NotBlank(message = "Endereço é obrigatório.")
    private String endereco;

    @NotNull(message = "Tipo do sócio é obrigatório.")
    private TipoSocio tipoSocio;

    private Boolean isento = false;
    private Boolean sinal = false;
    private Boolean semMargem = false;
    private Long socioTitularId;

    @DecimalMin(value = "0.00", message = "Valor mensal do sócio não pode ser negativo.")
    private BigDecimal valorMensalidadeCustom;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public TipoSocio getTipoSocio() { return tipoSocio; }
    public void setTipoSocio(TipoSocio tipoSocio) { this.tipoSocio = tipoSocio; }
    public Boolean getIsento() { return isento; }
    public void setIsento(Boolean isento) { this.isento = isento; }
    public Boolean getSinal() { return sinal; }
    public void setSinal(Boolean sinal) { this.sinal = sinal; }
    public Boolean getSemMargem() { return semMargem; }
    public void setSemMargem(Boolean semMargem) { this.semMargem = semMargem; }
    public Long getSocioTitularId() { return socioTitularId; }
    public void setSocioTitularId(Long socioTitularId) { this.socioTitularId = socioTitularId; }
    public BigDecimal getValorMensalidadeCustom() { return valorMensalidadeCustom; }
    public void setValorMensalidadeCustom(BigDecimal valorMensalidadeCustom) { this.valorMensalidadeCustom = valorMensalidadeCustom; }
}
