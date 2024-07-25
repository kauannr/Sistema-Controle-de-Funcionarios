package com.example.springthymeleaf.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.example.springthymeleaf.model.Pessoa;
import com.example.springthymeleaf.model.Telefone;
import com.example.springthymeleaf.model.enums.Cargo;
import com.example.springthymeleaf.reposiories.PessoaRepository;
import com.example.springthymeleaf.reposiories.TelefoneRepository;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PessoaController {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private TelefoneRepository telefoneRepository;

    @RequestMapping(value = "**/teladeinicio", method = RequestMethod.GET)
    public ModelAndView telaDeInicio() {
        ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa.html");

        modelAndView.addObject("listaPessoasFront",
                pessoaRepository.findAll(PageRequest.of(0, 2, Sort.by("id"))));

        modelAndView.addObject("objPessoa", new Pessoa());
        modelAndView.addObject("cargos", Cargo.values());

        return modelAndView;
    }

    @RequestMapping(value = "/pessoaspag", method = RequestMethod.GET)
    public ModelAndView carregarPessoaPorPaginacao(@PageableDefault(size = 2) Pageable pageable, ModelAndView modelAndView) {

        Page<Pessoa> pagePessoa = pessoaRepository.findAll(pageable);
        modelAndView.addObject("listaPessoasFront", pagePessoa);

        modelAndView.addObject("objPessoa", new Pessoa());
        modelAndView.setViewName("cadastro/cadastropessoa.html");

        return modelAndView;
    }

    @RequestMapping(value = "**/salvarpessoa", method = RequestMethod.POST, consumes = "multipart/form-data")
    public ModelAndView salvar(@Valid Pessoa pessoa, BindingResult bindingResult, @RequestParam MultipartFile file)
            throws IOException {

        // PARA AS MENSAGENS DE ERRO DOS ATRIBUTOS:
        if (bindingResult.hasErrors()) {
            // VOLTAR PRA TELA COM OS DADOS DA PESSOA:
            ModelAndView modelAndViewErro = new ModelAndView("cadastro/cadastropessoa.html");
            modelAndViewErro.addObject("objPessoa", pessoa);

            // PRA LISTA DE PESSOAS CONTINUAR NA TELA:
            modelAndViewErro.addObject("listaPessoasFront",
                    pessoaRepository.findAll(PageRequest.of(0, 2, Sort.by("id"))).getContent());

            List<String> listaMensagensErro = new ArrayList<>();
            for (ObjectError objectError : bindingResult.getAllErrors()) {
                listaMensagensErro.add(objectError.getDefaultMessage()); // Mensagem que vem do @NotNull
            }
            modelAndViewErro.addObject("msgPraIterar", listaMensagensErro);

            return modelAndViewErro;
        }

        // SALVAMENTO:
        ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa.html");
        System.out.println("ID recebido: " + pessoa.getId());
        String msgRetornadaPraTela = pessoa.getId() == null ? "Usuário salvo com sucesso!"
                : "Usuário atualizado com sucesso!";

        pessoa.setIdade(pessoa.calcularIdade());
        pessoaRepository.save(pessoa);

        modelAndView.addObject("msgPraIterar", msgRetornadaPraTela);

        modelAndView.addObject("listaPessoasFront",
                pessoaRepository.findAll(PageRequest.of(0, 2, Sort.by("id"))).getContent());

        modelAndView.addObject("objPessoa", new Pessoa());
        modelAndView.addObject("cargos", Cargo.values());

        return modelAndView;

    }

    @RequestMapping(value = "**/listartodos", method = RequestMethod.GET)
    public ModelAndView listarTodos() {

        ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");

        modelAndView.addObject("listaPessoasFront",
                pessoaRepository.findAll(PageRequest.of(0, 2, Sort.by("id"))).getContent());
        modelAndView.addObject("objPessoa", new Pessoa());
        return modelAndView;
    }

    @RequestMapping(value = "**/atualizarpessoa/{id}", method = RequestMethod.GET)
    public ModelAndView editar(@PathVariable("id") Long id) {

        Optional<Pessoa> pessoa = pessoaRepository.findById(id);

        ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
        modelAndView.addObject("objPessoa", pessoa.get());
        return modelAndView;
    }

    @RequestMapping(value = "**/deletar/{id}", method = RequestMethod.GET)
    public ModelAndView deletar(@PathVariable("id") Long id) {
        // deleto
        Pessoa pessoa = pessoaRepository.findById(id).get();
        pessoaRepository.delete(pessoa);

        ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");

        // adiciono a lista completa sem a pessoa deletada
        modelAndView.addObject("listaPessoasFront",
                pessoaRepository.findAll(PageRequest.of(0, 2, Sort.by("id"))).getContent());

        // pessoa vazia pro formulário de inicio
        modelAndView.addObject("objPessoa", new Pessoa());
        modelAndView.addObject("msgPraIterar", "Usuário deletado com sucesso");

        return modelAndView;
    }

    @RequestMapping(value = "**/pesquisar", method = RequestMethod.GET)
    public ModelAndView pesquisarPorNome(@RequestParam("nome") String nome, @RequestParam("idade") Integer idade) {

        List<Pessoa> listaPessoas = new ArrayList<>();

        // Se colocar o sexo na pesquisa, chama o consultaPorSexo
        if (nome.equalsIgnoreCase("MASCULINO") || nome.equalsIgnoreCase("FEMININO")) {
            listaPessoas = pessoaRepository.consultarPorSexo(nome.toLowerCase().trim());
        } else if (!nome.isEmpty()) {
            listaPessoas = pessoaRepository.consultarPornNome(nome.toLowerCase().trim());
        }

        // Se selecionar a idade
        if (idade != null) {
            listaPessoas = pessoaRepository.consultarPorIdade(idade);
        }

        ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa.html");
        modelAndView.addObject("listaPessoasFront", listaPessoas);

        modelAndView.addObject("objPessoa", new Pessoa());

        return modelAndView;
    }

    @RequestMapping(value = "**/telefones/{idPessoa}", method = RequestMethod.GET)
    public ModelAndView telefones(@PathVariable("idPessoa") Long idPessoa) {

        Optional<Pessoa> pessoa = pessoaRepository.findById(idPessoa);

        ModelAndView modelAndView = new ModelAndView("cadastro/telefones.html");

        // DADOS DA PESSOA NA TELA:
        modelAndView.addObject("objPessoa", pessoa.get());

        // PRA MOSTRAR A LISTA DE TELEFONES:
        modelAndView.addObject("listaTelefones", pessoa.get().getListaTelefones());

        return modelAndView;
    }

    @RequestMapping(value = "**/cadastrartelefone/{idPessoa}", method = RequestMethod.POST)
    public ModelAndView cadastrartelefone(@PathVariable("idPessoa") Long idPessoa, @Valid Telefone telefone,
            BindingResult bindingResult) {

        ModelAndView modelAndView = new ModelAndView("cadastro/telefones.html");

        Optional<Pessoa> pessoa = pessoaRepository.findById(idPessoa);
        telefone.setPessoa(pessoa.get());
        telefoneRepository.save(telefone);

        pessoa.get().adicionarNaLista(telefone);
        modelAndView.addObject("msgPraIterar", "Telefone adicionado com sucesso!");

        // PRA MOSTRAR A LISTA DE TELEFONES:
        modelAndView.addObject("listaTelefones", pessoa.get().getListaTelefones());
        // PRA CONTINUAR COM OS DADOS DA PESSOA NA TELA
        modelAndView.addObject("objPessoa", pessoa.get());
        return modelAndView;
    }

    @RequestMapping(value = "**/deletarTelefone/{idTelefone}", method = RequestMethod.GET)
    public ModelAndView requestMethodName(@PathVariable("idTelefone") Long idTelefone) {

        ModelAndView modelAndView = new ModelAndView("cadastro/telefones.html");

        Optional<Telefone> telefone = telefoneRepository.findById(idTelefone);
        telefoneRepository.delete(telefone.get());

        // DADOS DA PESSOA NA TELA:
        modelAndView.addObject("objPessoa", telefone.get().getPessoa());

        // MOSTRAR LISTA DE TELEFONES
        modelAndView.addObject("listaTelefones", telefone.get().getPessoa().getListaTelefones());
        modelAndView.addObject("msgPraIterar", "Telefone deletado com sucesso!");

        return modelAndView;
    }

}
