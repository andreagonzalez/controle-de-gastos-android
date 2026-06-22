# 📚 Documentação do Projeto ControleDeGastos

Bem-vindo à documentação completa do projeto ControleDeGastos. Esta documentação serve como fonte única de verdade para desenvolvedores, arquitetos e stakeholders do projeto.

## 🗂️ Estrutura da Documentação

```
docs/
├── README.md                          # Este arquivo
├── arquitetura/
│   ├── ARQUITETURA.md                 # Documentação arquitetural completa
│   └── DECISION_RECORDS.md            # Registro de decisões arquiteturais
├── diagramas/
│   ├── DIAGRAMAS_UML.md              # Diagramas UML completos
│   ├── DIAGRAMA_ER.md                # Diagrama Entidade-Relacionamento
│   └── FLUXOS_SISTEMA.md             # Fluxos do sistema
├── patterns/
│   ├── PADROES_E_DECISOES.md         # Padrões de projeto aplicados
│   └── GUIDELINES_IMPLEMENTACAO.md   # Guias de implementação
├── decision-records/
│   ├── ADR-001-ARQUITETURA.md        # Decisão arquitetural 001
│   ├── ADR-002-PERSISTENCIA.md       # Decisão arquitetural 002
│   └── ADR-003-UI-UX.md              # Decisão arquitetural 003
└── guias/
    ├── CONTRIBUICAO.md               # Guia de contribuição
    ├── DEPLOYMENT.md                 # Guia de deployment
    └── TESTES.md                     # Guia de testes
```

## 🎯 Propósito da Documentação

Esta documentação tem como objetivos:

1. **📋 Documentar** a arquitetura e design do sistema
2. **🎓 Educar** novos membros da equipe
3. **🔍 Facilitar** manutenção e evolução do código
4. **📈 Suportar** tomada de decisões técnicas
5. **🧪 Garantir** consistência no desenvolvimento

## 🏗️ Visão Arquitetural

### Arquitetura Atual
- **Padrão**: MVP (Model-View-Presenter)
- **Persistência**: SharedPreferences + Gson JSON
- **UI**: Activities + XML Layouts
- **Navegação**: Intent-based

### Stack Tecnológico
- **Linguagem**: Java 8+
- **Min SDK**: Android 5.0 (API 21)
- **Target SDK**: Android 14 (API 34)
- **Build Tool**: Gradle
- **Dependências**: Gson, AndroidX

## 📖 Como Usar Esta Documentação

### Para Novos Desenvolvedores
1. Comece pelo [ARQUITETURA.md](./arquitetura/ARQUITETURA.md)
2. Revise os [DIAGRAMAS_UML.md](./diagramas/DIAGRAMAS_UML.md)
3. Entenda os [padrões aplicados](./patterns/PADROES_E_DECISOES.md)
4. Consulte os [guias de contribuição](./guias/CONTRIBUICAO.md)

### Para Tomada de Decisões
1. Consulte os [registros de decisão](./decision-records/)
2. Revise [padrões estabelecidos](./patterns/PADROES_E_DECISOES.md)
3. Analise o [impacto arquitetural](./arquitetura/ARQUITETURA.md)

### Para Manutenção
1. Verifique [guidelines de implementação](./patterns/GUIDELINES_IMPLEMENTACAO.md)
2. Consulte [fluxos do sistema](./diagramas/FLUXOS_SISTEMA.md)
3. Revise [diagramas relevantes](./diagramas/DIAGRAMAS_UML.md)

## 🔄 Ciclo de Vida da Documentação

### Atualização
1. **Quando**: Após mudanças arquiteturais significativas
2. **Quem**: Lead Developer ou Arquitetos
3. **Como**: Atualizar documentos relevantes e versionar

### Revisão
1. **Frequência**: Trimestralmente
2. **Participantes**: Equipe técnica completa
3. **Resultado**: Atualização ou confirmação da documentação

### Versionamento
- **Formato**: `vX.Y.Z` (Semantic Versioning)
- **Armazenamento**: Git junto com código-fonte
- **Histórico**: Changelog mantido

## 🧪 Qualidade da Documentação

### Critérios de Qualidade
- ✅ **Completa**: Cobre todos os aspectos importantes
- ✅ **Atualizada**: Reflete o estado atual do sistema
- ✅ **Clara**: Linguagem acessível e direta
- ✅ **Consistente**: Terminologia padronizada
- ✅ **Útil**: Resolve problemas reais da equipe

### Métricas de Qualidade
- **Cobertura**: 90%+ dos componentes documentados
- **Atualidade**: Última atualização ≤ 30 dias
- **Acessibilidade**: Links funcionais e navegação clara
- **Utilidade**: Feedback positivo da equipe

## 📈 Roadmap de Evolução

### Fase 1: Estabelecimento (Atual)
- ✅ Documentação arquitetural básica
- ✅ Diagramas UML essenciais
- ✅ Padrões de projeto identificados

### Fase 2: Aprimoramento (Próximo)
- 🔄 ADRs (Architectural Decision Records)
- 🔄 Guias de implementação detalhados
- 🔄 Documentação de APIs internas

### Fase 3: Automatização (Futuro)
- 🔄 Geração automática de diagramas
- 🔄 Integração com CI/CD
- 🔄 Documentação executável

## 🤝 Contribuição

### Como Contribuir
1. Faça fork do repositório
2. Crie uma branch para sua contribuição
3. Atualize a documentação relevantemente
4. Envie um Pull Request com descrição clara

### Guidelines de Contribuição
- Mantenha consistência com estilo existente
- Use linguagem clara e técnica apropriada
- Inclua exemplos quando útil
- Atualize todos os documentos relacionados

### Template de Mudanças
```markdown
## [Data]
### [Tipo: Adição/Correção/Remoção/Melhoria]
- **Arquivo**: [Nome do arquivo]
- **Descrição**: [O que foi mudado e por quê]
- **Impacto**: [Impacto na arquitetura/equipe]
- **Referências**: [Links para issues/PRs relacionados]
```

## 🔗 Documentação Relacionada

### Interna
- [Código-fonte](../app/src/main/java/)
- [Build configuration](../app/build.gradle.kts)
- [Layouts XML](../app/src/main/res/layout/)

### Externa
- [Android Developer Documentation](https://developer.android.com)
- [Gson Documentation](https://github.com/google/gson)
- [MVP Pattern Guide](https://en.wikipedia.org/wiki/Model–view–presenter)

## 📞 Suporte e Contato

### Canais de Suporte
- **Issues GitHub**: Para bugs e melhorias na documentação
- **Discussions**: Para dúvidas e discussões técnicas
- **Code Reviews**: Para validação de mudanças

### Responsáveis
- **Arquitetura**: [Nome do Arquiteto]
- **Documentação**: [Nome do Tech Writer]
- **Desenvolvimento**: Equipe de Desenvolvimento

---

## 📊 Status da Documentação

| Área | Status | Última Atualização | Responsável |
|------|--------|-------------------|-------------|
| Arquitetura | ✅ Completa | Junho 2026 | Arquitetura |
| Diagramas | ✅ Completa | Junho 2026 | Arquitetura |
| Padrões | ✅ Completa | Junho 2026 | Desenvolvimento |
| ADRs | ⚠️ Parcial | Junho 2026 | Arquitetura |
| Guias | 🔄 Em andamento | - | Desenvolvimento |
| APIs | 🔄 Pendente | - | Backend |

**Última Revisão Geral**: Junho 2026  
**Próxima Revisão Programada**: Setembro 2026  
**Versão da Documentação**: 1.0.0  

---

*Esta documentação é um documento vivo e deve evoluir junto com o projeto. Contribuições são bem-vindas!*