# Diagramas UML - ControleDeGastos

## 1. Diagrama de Casos de Uso

```mermaid
graph TB
    subgraph "Ator Principal"
        A[Usuário]
    end
    
    subgraph "Casos de Uso do Sistema"
        B[Registrar Entrada]
        C[Registrar Gasto]
        D[Visualizar Extrato]
        E[Consultar Saldo]
        F[Calcular Saldo por Período]
        G[Editar Registro]
        H[Excluir Registro]
        I[Exportar Dados]
        J[Configurar Salário]
    end
    
    A --> B
    A --> C
    A --> D
    A --> E
    A --> F
    A --> G
    A --> H
    A --> I
    A --> J
    
    D --> B
    D --> C
    E --> B
    E --> C
    F --> B
    F --> C
    G --> B
    G --> C
    H --> B
    H --> C
```

## 2. Diagrama de Classes Detalhado

```mermaid
classDiagram
    %% Entidades Principais
    class Entrada {
        -String descricao
        -double valor
        -String data
        -timestamp criadoEm
        +Entrada(descricao, valor, data)
        +getDescricao() String
        +getValor() double
        +getData() String
        +setDescricao(String)
        +setValor(double)
        +setData(String)
        +calcularImposto() double
        +validar() boolean
    }
    
    class Gasto {
        -String descricao
        -double valor
        -String categoria
        -String formaPagamento
        -String data
        -timestamp criadoEm
        +Gasto(descricao, valor, categoria, formaPagamento, data)
        +getDescricao() String
        +getValor() double
        +getCategoria() String
        +getFormaPagamento() String
        +getData() String
        +setDescricao(String)
        +setValor(double)
        +setCategoria(String)
        +setFormaPagamento(String)
        +setData(String)
        +categorizarAutomaticamente() String
        +validar() boolean
    }
    
    class Movimento {
        -String descricao
        -double valor
        -String tipo
        -String data
        -timestamp criadoEm
        +Movimento(descricao, valor, tipo, data)
        +getDescricao() String
        +getValor() double
        +getTipo() String
        +getData() String
        +formatarParaExibicao() String
        +eDespesa() boolean
        +eReceita() boolean
    }
    
    %% Activities
    class MainActivity {
        -SharedPreferences preferences
        -ArrayList~Gasto~ listaGastos
        -ArrayList~Entrada~ listaEntradas
        -ArrayList~Movimento~ listaMovimentos
        -double totalGasto
        -double totalEntrada
        -TextView textTotalGasto
        -TextView textSaldoRestante
        -Button btnAdicionarGasto
        -Button btnAdicionarEntrada
        +onCreate(Bundle)
        +onResume()
        +onPause()
        +configurarListeners()
        +adicionarGasto()
        +adicionarEntrada()
        +recalcularSaldo()
        +atualizarSaldo(double)
        +consolidarMovimentos()
        +formatarMoeda(double) String
        +salvarListaGastos()
        +salvarListaEntradas()
        +recuperarListaGastos()
        +recuperarListaEntradas()
    }
    
    class ListaGastosActivity {
        -RecyclerView recyclerGastos
        -GastoAdapter adapter
        -ArrayList~Gasto~ listaGastos
        -SharedPreferences preferences
        +onCreate(Bundle)
        +configurarRecyclerView()
        +mostrarOpcoesGasto(int)
        +editarGasto(int)
        +removerGasto(int)
        +salvarListaGastos()
        +recuperarListaGastos()
    }
    
    class ListaEntradasActivity {
        -RecyclerView recyclerEntradas
        -EntradaAdapter adapter
        -ArrayList~Entrada~ listaEntradas
        -SharedPreferences preferences
        +onCreate(Bundle)
        +configurarRecyclerView()
        +mostrarOpcoesEntrada(int)
        +editarEntrada(int)
        +removerEntrada(int)
        +salvarListaEntradas()
        +recuperarListaEntradas()
    }
    
    class GastoActivity {
        -EditText editDescricao
        -EditText editValor
        -Spinner spinnerCategoria
        -Spinner spinnerFormaPagamento
        -Button btnSalvar
        +onCreate(Bundle)
        +configurarSpinners()
        +validarFormulario() boolean
        +salvarGasto()
    }
    
    class EntradaActivity {
        -EditText editDescricao
        -EditText editValor
        -Button btnSalvar
        +onCreate(Bundle)
        +validarFormulario() boolean
        +salvarEntrada()
    }
    
    class ExtratoActivity {
        -RecyclerView recyclerMovimentos
        -MovimentoAdapter adapter
        -ArrayList~Movimento~ listaMovimentos
        +onCreate(Bundle)
        +carregarMovimentos()
        +ordenarPorData()
        +filtrarPorPeriodo(Date, Date)
        +calcularTotalPeriodo() double
    }
    
    class SaldoPeriodoActivity {
        -DatePicker dateInicio
        -DatePicker dateFim
        -TextView textResultado
        -Button btnCalcular
        +onCreate(Bundle)
        +calcularSaldoPeriodo()
        +validarPeriodo() boolean
        +formatarResultado(double) String
    }
    
    %% Adapters
    class GastoAdapter {
        <<interface>>
        +OnItemLongClickListener
        -ArrayList~Gasto~ listaGastos
        -OnItemLongClickListener longClickListener
        +GastoAdapter(ArrayList~Gasto~)
        +setOnItemLongClickListener(OnItemLongClickListener)
        +onCreateViewHolder(ViewGroup, int) GastoViewHolder
        +onBindViewHolder(GastoViewHolder, int)
        +getItemCount() int
        +formatarMoeda(double) String
    }
    
    class EntradaAdapter {
        <<interface>>
        +OnItemLongClickListener
        -ArrayList~Entrada~ listaEntradas
        -OnItemLongClickListener longClickListener
        +EntradaAdapter(ArrayList~Entrada~)
        +setOnItemLongClickListener(OnItemLongClickListener)
        +onCreateViewHolder(ViewGroup, int) EntradaViewHolder
        +onBindViewHolder(EntradaViewHolder, int)
        +getItemCount() int
        +formatarMoeda(double) String
    }
    
    class MovimentoAdapter {
        -ArrayList~Movimento~ listaMovimentos
        +MovimentoAdapter(ArrayList~Movimento~)
        +onCreateViewHolder(ViewGroup, int) MovimentoViewHolder
        +onBindViewHolder(MovimentoViewHolder, int)
        +getItemCount() int
        +formatarMoeda(double) String
        +obterCorPorTipo(String) int
    }
    
    %% ViewHolders
    class GastoViewHolder {
        -TextView textDescricao
        -TextView textCategoria
        -TextView textFormaPagamento
        -TextView textValor
        -TextView textData
        +GastoViewHolder(View)
    }
    
    class EntradaViewHolder {
        -TextView textDescricao
        -TextView textValor
        -TextView textData
        +EntradaViewHolder(View)
    }
    
    class MovimentoViewHolder {
        -TextView textDescricao
        -TextView textValor
        -TextView textTipo
        -TextView textData
        +MovimentoViewHolder(View)
    }
    
    %% Relacionamentos
    MainActivity --> Entrada
    MainActivity --> Gasto
    MainActivity --> Movimento
    ListaGastosActivity --> GastoAdapter
    ListaEntradasActivity --> EntradaAdapter
    ExtratoActivity --> MovimentoAdapter
    GastoAdapter --> Gasto
    GastoAdapter --> GastoViewHolder
    EntradaAdapter --> Entrada
    EntradaAdapter --> EntradaViewHolder
    MovimentoAdapter --> Movimento
    MovimentoAdapter --> MovimentoViewHolder
    GastoActivity --> Gasto
    EntradaActivity --> Entrada
```

## 3. Diagrama de Estados - Ciclo de Vida de um Registro

```mermaid
stateDiagram-v2
    [*] --> NaoRegistrado
    
    state NaoRegistrado {
        [*] --> AguardandoDados
        AguardandoDados --> Validando : Usuário preenche formulário
        Validando --> DadosInvalidos : Falha validação
        Validando --> DadosValidos : Sucesso validação
        DadosInvalidos --> AguardandoDados : Corrigir dados
        DadosValidos --> Registrado : Salvar
    }
    
    state Registrado {
        [*] --> Ativo
        Ativo --> Editando : Usuário edita
        Ativo --> Excluindo : Usuário exclui
        Editando --> Ativo : Salvar edição
        Editando --> Ativo : Cancelar edição
        Excluindo --> [*] : Confirmar exclusão
        Excluindo --> Ativo : Cancelar exclusão
    }
    
    NaoRegistrado --> Registrado : Registro salvo
    Registrado --> NaoRegistrado : Excluído permanentemente
```

## 4. Diagrama de Atividades - Fluxo de Adição de Gasto

```mermaid
graph TD
    A[Início] --> B[Usuário clica Adicionar Gasto]
    B --> C[Abrir GastoActivity]
    C --> D[Exibir formulário]
    D --> E[Usuário preenche dados]
    E --> F{Validar dados}
    F -->|Válidos| G[Criar objeto Gasto]
    F -->|Inválidos| H[Exibir mensagem erro]
    H --> D
    G --> I[Adicionar à lista]
    I --> J[Serializar para JSON]
    J --> K[Salvar em SharedPreferences]
    K --> L[Atualizar UI]
    L --> M[Exibir mensagem sucesso]
    M --> N[Fechar Activity]
    N --> O[Retornar à MainActivity]
    O --> P[Atualizar saldo total]
    P --> Q[Fim]
```

## 5. Diagrama de Componentes - Visão de Infraestrutura

```mermaid
graph TB
    subgraph "Dispositivo Android"
        subgraph "Aplicativo ControleDeGastos"
            A[Activities]
            B[Adapters]
            C[Models]
            D[SharedPreferences]
            E[Gson Serializer]
        end
    end
    
    subgraph "Sistema Android"
        F[Android Runtime]
        G[File System]
        H[UI Framework]
    end
    
    subgraph "Serviços Futuros"
        I[API REST]
        J[Cloud Storage]
        K[Sincronização]
    end
    
    A --> H
    D --> G
    E --> F
    A --> B
    B --> C
    C --> E
    E --> D
    
    %% Conexões futuras
    A -.-> I
    D -.-> J
    E -.-> K
```

## 6. Diagrama de Sequência - Cálculo de Saldo

```mermaid
sequenceDiagram
    participant U as Usuário
    participant MA as MainActivity
    participant SP as SharedPreferences
    participant GS as Gson
    participant CAL as Calculator
    
    U->>MA: Abrir aplicativo
    MA->>SP: Recuperar lista_entradas
    SP-->>MA: JSON entradas
    MA->>GS: Deserializar entradas
    GS-->>MA: Lista<Entrada>
    
    MA->>SP: Recuperar lista_gastos
    SP-->>MA: JSON gastos
    MA->>GS: Deserializar gastos
    GS-->>MA: Lista<Gasto>
    
    MA->>CAL: Calcular totalEntradas
    CAL-->>MA: totalEntradas
    
    MA->>CAL: Calcular totalGastos
    CAL-->>MA: totalGastos
    
    MA->>CAL: Calcular saldo
    Note right of CAL: saldo = totalEntradas - totalGastos
    CAL-->>MA: saldo
    
    MA->>MA: Atualizar TextView saldo
    MA->>MA: Aplicar cor (vermelho/verde)
    MA-->>U: Exibir saldo atualizado
```

## 7. Diagrama de Pacotes (Package Diagram)

```mermaid
graph TB
    subgraph "br.inf.andreagonzalez.controledegastos"
        subgraph "ui"
            P1[MainActivity]
            P2[EntradaActivity]
            P3[GastoActivity]
            P4[ListaEntradasActivity]
            P5[ListaGastosActivity]
            P6[ExtratoActivity]
            P7[SaldoPeriodoActivity]
        end
        
        subgraph "adapter"
            P8[EntradaAdapter]
            P9[GastoAdapter]
            P10[MovimentoAdapter]
        end
        
        subgraph "model"
            P11[Entrada]
            P12[Gasto]
            P13[Movimento]
        end
        
        subgraph "util"
            P14[DateUtils]
            P15[CurrencyFormatter]
            P16[Validator]
        end
        
        subgraph "repository"
            P17[EntradaRepository]
            P18[GastoRepository]
            P19[MovimentoRepository]
        end
    end
    
    P1 --> P11
    P1 --> P12
    P1 --> P13
    P2 --> P11
    P3 --> P12
    P4 --> P8
    P5 --> P9
    P6 --> P10
    P8 --> P11
    P9 --> P12
    P10 --> P13
    P14 --> P11
    P14 --> P12
    P14 --> P13
    P15 --> P8
    P15 --> P9
    P15 --> P10
    P16 --> P2
    P16 --> P3
    P17 --> P11
    P18 --> P12
    P19 --> P13
```

## 8. Diagrama de Objetos - Estado do Sistema

```mermaid
graph LR
    subgraph "Objetos em Memória - Sessão Ativa"
        A[mainActivity: MainActivity]
        B[listaGastos: ArrayList&lt;Gasto&gt;]
        C[listaEntradas: ArrayList&lt;Entrada&gt;]
        D[preferences: SharedPreferences]
        
        subgraph "Gasto Objects"
            E[gasto1: Gasto]
            F[gasto2: Gasto]
            G[gasto3: Gasto]
        end
        
        subgraph "Entrada Objects"
            H[entrada1: Entrada]
            I[entrada2: Entrada]
        end
        
        subgraph "Calculated Values"
            J[totalGasto: 1250.75]
            K[totalEntrada: 3000.00]
            L[saldo: 1749.25]
        end
    end
    
    A --> B
    A --> C
    A --> D
    A --> J
    A --> K
    A --> L
    B --> E
    B --> F
    B --> G
    C --> H
    C --> I
    
    %% Atributos dos objetos
    E[descricao: Supermercado<br/>valor: 350.50<br/>categoria: Alimentação<br/>data: 20/06/2026]
    F[descricao: Combustível<br/>valor: 200.00<br/>categoria: Transporte<br/>data: 19/06/2026]
    G[descricao: Farmácia<br/>valor: 700.25<br/>categoria: Saúde<br/>data: 18/06/2026]
    H[descricao: Salário<br/>valor: 2500.00<br/>data: 15/06/2026]
    I[descricao: Freelance<br/>valor: 500.00<br/>data: 17/06/2026]
```

## 9. Diagrama de Comunicação - Exclusão de Item

```mermaid
sequenceDiagram
    participant U as Usuário
    participant RV as RecyclerView
    participant AD as Adapter
    participant AC as Activity
    participant DL as AlertDialog
    participant SP as SharedPreferences
    
    U->>RV: Clique longo no item
    RV->>AD: Notificar clique longo
    AD->>AC: onItemLongClick(position)
    
    AC->>DL: Criar AlertDialog
    DL-->>U: Exibir opções Editar/Excluir
    
    U->>DL: Selecionar "Excluir"
    DL->>AC: Mostrar confirmação
    
    AC->>DL: Criar diálogo confirmação
    DL-->>U: Exibir confirmação
    
    U->>DL: Clicar "Remover"
    DL->>AC: Notificar confirmação
    
    AC->>AC: Remover item da lista
    AC->>AD: notifyItemRemoved(position)
    AC->>SP: Atualizar SharedPreferences
    
    SP-->>AC: Confirmação salvamento
    AC->>AC: Recalcular totais
    AC->>U: Exibir Toast "Removido com sucesso"
```

## 10. Diagrama de Tempo - Ciclo de Vida da Activity

```mermaid
timeline
    title Ciclo de Vida - MainActivity
    section onCreate
        Inflar layout        : 0-50ms
        Inicializar views    : 50-100ms
        Configurar listeners : 100-150ms
        Carregar dados       : 150-300ms
    
    section onResume
        Atualizar UI         : 0-50ms
        Calcular saldo       : 50-100ms
    
    section Interação Usuário
        Adicionar gasto      : 0-200ms
        Adicionar entrada    : 0-150ms
        Navegar para lista   : 0-100ms
    
    section onPause
        Salvar dados         : 0-100ms
        Liberar recursos     : 100-150ms
```

---

## 📊 Legenda dos Diagramas

| Símbolo | Significado |
|---------|-------------|
| `<<interface>>` | Interface Java |
| `-` | Atributo privado |
| `+` | Método público |
| `#` | Método protegido |
| `~` | Atributo package-private |
| `-->` | Associação |
| `--|>` | Herança |
| `--o` | Agregação |
| `--*` | Composição |
| `-->` | Dependência |

## 🔧 Como Manter os Diagramas Atualizados

1. **Atualizar após mudanças arquiteturais**
2. **Revisar diagramas durante code reviews**
3. **Sincronizar com mudanças no código-fonte**
4. **Usar ferramentas como PlantUML para geração automática**

---

*Documentação gerada em: Junho 2026*  
*Última revisão: Versão 1.0*  
*Responsável: Arquitetura de Software - ControleDeGastos*