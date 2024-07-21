package com.example.springthymeleaf.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.time.Year;
import java.time.ZoneId;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
public class Pessoa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Preencha o nome corretamente")
    @NotEmpty(message = "Nome não pode estar vazio")
    private String nome;

    @NotNull(message = "Preencha o sobrenome corretamente")
    @NotEmpty(message = "Sobrenome não pode estar vazio")
    private String sobrenome;

    @Min(value = 10, message = "Idade mínima: 10 anos")
    @Max(value = 100, message = "Idade máxima: 100 anos")//AQUI
    private Integer idade;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;

    @NotNull(message = "Preencha o cargo corretamente")
    @NotEmpty(message = "Preencha o cargo do funcionário")
    private String cargo;

    @Min(value = 1200, message = "Salario minimo: 1200")
    @NotNull(message = "Preencha o salário")
    private Double salario;

    @NotEmpty(message = "Preencha o cep")
    @NotNull(message = "Preencha o cep corretamente")
    private String cep;

    @NotEmpty(message = "Preencha a rua")
    @NotNull(message = "Preencha a rua corretamente")
    private String rua;
    private String bairro;
    private String cidade;
    private String uf;
    private String ibge;
    private String sexo;

    @OneToMany(mappedBy = "pessoa", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Telefone> listaTelefones = new ArrayList<>();

    public Pessoa(String nome, String sobrenome, Integer idade) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.idade = calcularIdade();
    }

    public Pessoa() {
    }

    @SuppressWarnings("deprecation")
    public Integer calcularIdade() {
        // Date data = new Date();

        /*
         * Integer anoNascimento = (getDataNascimento().getYear() + 1900);
         * idade = (data.getYear() + 1900) - anoNascimento;
         * if ((data.getMonth() + 1) < (getDataNascimento().getMonth()+1)) {
         * idade--;
         * } else if ((data.getMonth() + 1) == (getDataNascimento().getMonth()+1) &&
         * data.getDate() < dataNascimento.getDate()) {
         * idade--;
         * }else if((getDataNascimento().getMonth()+1) == (data.getMonth() + 1) &&
         * getDataNascimento().getDate() == data.getDate()){
         * idade++;
         * }
         */
        LocalDate dataNascimento = getDataNascimento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate dataLocal = LocalDate.now();

        Integer anos = Period.between(dataNascimento, dataLocal).getYears();

        return anos;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public List<Telefone> getListaTelefones() {
        return listaTelefones;
    }

    public void adicionarNaLista(Telefone telefone) {
        listaTelefones.add(telefone);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getbairro() {
        return bairro;
    }

    public void setbairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getIbge() {
        return ibge;
    }

    public void setIbge(String ibge) {
        this.ibge = ibge;
    }

    public void setListaTelefones(List<Telefone> listaTelefones) {
        this.listaTelefones = listaTelefones;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pessoa other = (Pessoa) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
