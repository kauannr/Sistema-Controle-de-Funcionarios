package com.example.springthymeleaf.model.enums;

public enum Cargo {
    
    DEV_JUNIOR("Júnior"),
    DEV_PLENO("Pleno"),
    DEV_SENIOR("Senior");
    
    private String nome;
    
    
    public String getNome() {
        return nome;
    }
    
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    
    Cargo(String nome) {
        this.nome = nome;
    }

    
    
    
}
