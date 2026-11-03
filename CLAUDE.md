@AGENTS.md

## Claude Code

- Para corrigir um exercício, siga a seção "Ao corrigir um exercício" do `AGENTS.md`. Exemplos de pedido: "corrija playground/modulo-03/Ex2.java", "corrija o commit abc123 do clinic-billing-api", "revise o branch feature/m16-charges".
- Rode testes só pelos comandos da seção "Ambiente" do `AGENTS.md`. No `clinic-billing-api`, a partir da Fase 3, sempre com `SPRING_PROFILES_ACTIVE=test`.
- Nunca rode `sudo` nem comandos `docker`. Se o MySQL estiver fora do ar, peça ao dono para subir.
- Não suba a aplicação nem deixe processos rodando no fundo. O servidor do módulo 8 só com `timeout`.
- Se um comando pedir aprovação, explique em uma linha o que ele vai rodar e por que não altera o banco de desenvolvimento.
- Ao explicar algo, siga o padrão do dono: resumo no início, fundamento em passos atômicos e fontes com links.
