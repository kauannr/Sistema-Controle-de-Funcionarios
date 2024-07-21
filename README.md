# Sistema-Controle-de-Funcionarios
# Sobre o projeto:
Desenvolvi um sistema de controle de funcionários utilizando Spring Security para o gerenciamento de login e papéis, e também para a criptografia das senhas. A interface do sistema varia de acordo com a role do usuário logado:

Manager: Pode cadastrar, editar e excluir usuários. Na tela inicial, managers têm acesso aos botões de "editar" e "excluir" e podem clicar nos nomes dos usuários, que é um link para uma outra pagina, para visualizar e cadastrar telefones, além de acessar informações adicionais como cargo, salário, sexo e endereço completo que foi implementado com o consumo da API do viaCEP.

User: Pode visualizar os usuários cadastrados (colegas de trabalho), mas não possui acesso aos botões de "editar" e "excluir". Ao tentar acessar a página de detalhes de um usuário, são redirecionados para a página do arquivo erro.html com um aviso de acesso negado.

🛠️ Tecnologias Usadas no Servidor:
Java
Spring Boot, Data, MVC e Security
Hibernate
JUnit
Banco de Dados: PostgreSQL

🛠️ Tecnologias Usadas no Front-End:
HTML e CSS
Thymeleaf
jQuery
Materialize

🔍 Principais Funcionalidades:
Interface amigável para usuários finais.
Painel de controle completo para gerentes.
Proteção de dados para usuários comuns.


# Autor

Kauan Ferreira Rodrigues

https://www.linkedin.com/in/kauan-ferreira-922671240/
