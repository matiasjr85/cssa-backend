package br.com.clubedossargentos.dto;

import br.com.clubedossargentos.enums.EspacoLocacao;
import br.com.clubedossargentos.enums.TipoPagamento;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AluguelEspacoRequestDTO {

    @NotNull(message = "Informe se é sócio ou não.")
    private Boolean socio;

    private Long pessoaId;

    @NotBlank(message = "Nome do responsável é obrigatório.")
    private String nomeResponsavel;

    @NotBlank(message = "CPF do responsável é obrigatório.")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 números.")
    private String cpfResponsavel;

    @NotBlank(message = "Endereço do responsável é obrigatório.")
    private String enderecoResponsavel;

    @NotNull(message = "Data do evento é obrigatória.")
    private LocalDate dataEvento;

    @NotNull(message = "Hora inicial é obrigatória.")
    private LocalTime horaInicio;

    @NotNull(message = "Hora final é obrigatória.")
    private LocalTime horaFim;

    @NotEmpty(message = "Selecione pelo menos 1 espaço para locação.")
    private List<EspacoLocacao> espacosSelecionados;

    @NotNull(message = "Quantidade de parcelas é obrigatória.")
    @Min(value = 1, message = "A quantidade de parcelas deve ser no mínimo 1.")
    private Integer parcelas;

    @NotNull(message = "Valor pago é obrigatório.")
    private BigDecimal valorPago;

    private BigDecimal valorCustom;

    @NotNull(message = "Tipo de pagamento é obrigatório.")
    private TipoPagamento tipoPagamento;

    private String observacao;

    public Boolean getSocio() { return socio; }
    public Long getPessoaId() { return pessoaId; }
    public String getNomeResponsavel() { return nomeResponsavel; }
    public String getCpfResponsavel() { return cpfResponsavel; }
    public String getEnderecoResponsavel() { return enderecoResponsavel; }
    public LocalDate getDataEvento() { return dataEvento; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public LocalTime getHoraFim() { return horaFim; }
    public List<EspacoLocacao> getEspacosSelecionados() { return espacosSelecionados; }
    public Integer getParcelas() { return parcelas; }
    public BigDecimal getValorPago() { return valorPago; }
    public BigDecimal getValorCustom() { return valorCustom; }
    public TipoPagamento getTipoPagamento() { return tipoPagamento; }
    public String getObservacao() { return observacao; }

    public void setSocio(Boolean socio) { this.socio = socio; }
    public void setPessoaId(Long pessoaId) { this.pessoaId = pessoaId; }
    public void setNomeResponsavel(String nomeResponsavel) { this.nomeResponsavel = nomeResponsavel; }
    public void setCpfResponsavel(String cpfResponsavel) { this.cpfResponsavel = cpfResponsavel; }
    public void setEnderecoResponsavel(String enderecoResponsavel) { this.enderecoResponsavel = enderecoResponsavel; }
    public void setDataEvento(LocalDate dataEvento) { this.dataEvento = dataEvento; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
    public void setHoraFim(LocalTime horaFim) { this.horaFim = horaFim; }
    public void setEspacosSelecionados(List<EspacoLocacao> espacosSelecionados) { this.espacosSelecionados = espacosSelecionados; }
    public void setParcelas(Integer parcelas) { this.parcelas = parcelas; }
    public void setValorPago(BigDecimal valorPago) { this.valorPago = valorPago; }
    public void setValorCustom(BigDecimal valorCustom) { this.valorCustom = valorCustom; }
    public void setTipoPagamento(TipoPagamento tipoPagamento) { this.tipoPagamento = tipoPagamento; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}
