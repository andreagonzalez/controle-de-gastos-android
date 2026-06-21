# Padrões de Projeto e Decisões Arquiteturais

## 📋 Visão Geral
Este documento descreve os padrões de projeto aplicados e as decisões arquiteturais tomadas no desenvolvimento do ControleDeGastos.

## 🏗️ Decisões Arquiteturais Principais

### **ADR-001: Escolha da Arquitetura MVP**
**Data**: Junho 2026  
**Status**: Aprovado  
**Contexto**: Necessidade de uma arquitetura simples para um aplicativo de pequeno porte com equipe reduzida.

**Decisão**:
Adotar Model-View-Presenter (MVP) como arquitetura principal.

**Consequências**:
- ✅ Simplicidade de implementação
- ✅ Separação clara de responsabilidades
- ✅ Fácil aprendizado para novos desenvolvedores
- ⚠️ Maior acoplamento entre View e Presenter
- ⚠️ Dificuldade para testes unitários completos

**Alternativas Consideradas**:
1. **MVVM**: Rejeitada devido à complexidade inicial
2. **MVC**: Rejeitada por falta de separação adequada no Android
3. **Clean Architecture**: Rejeitada por over-engineering para o escopo atual

---

### **ADR-002: Estratégia de Persistência**
**Data**: Junho 2026  
**Status**: Aprovado  
**Contexto**: Necessidade de armazenamento local simples sem requisitos complexos de consulta.

**Decisão**:
Usar SharedPreferences com serialização JSON via Gson.

**Consequências**:
- ✅ Implementação rápida
- ✅ Sem necessidade de migração de schema
- ✅ Funcional para volumes pequenos de dados
- ⚠️ Performance degradada com muitos registros
- ⚠️ Falta de queries complexas
- ⚠️ Serialização/deserialização completa a cada operação

**Alternativas Consideradas**:
1. **Room Database**: Considerada para versão futura
2. **SQLite Direct**: Rejeitada por complexidade de manutenção
3. **Firebase**: Rejeitada por dependência de internet

---

### **ADR-003: Estratégia de UI/UX**
**Data**: Junho 2026  
**Status**: Aprovado  
**Contexto**: Aplicativo focado em funcionalidade com interface simples.

**Decisão**:
Usar Activities tradicionais com layouts XML e RecyclerView para listas.

**Consequências**:
- ✅ Compatibilidade com versões antigas do Android
- ✅ Desenvolvimento rápido
- ✅ Comunidade grande com muitos recursos
- ⚠️ Boilerplate code para listas
- ⚠️ Gerenciamento manual de estado

**Alternativas Consideradas**:
1. **Jetpack Compose**: Considerada para refatoração futura
2. **Fragments**: Rejeitada por complexidade desnecessária
3. **Views customizadas**: Rejeitada por tempo de desenvolvimento

---

## 🧩 Padrões de Projeto Implementados

### **1. Adapter Pattern**
**Objetivo**: Adaptar dados do domínio para exibição em RecyclerView

**Implementação**:
```java
public class GastoAdapter extends RecyclerView.Adapter<GastoAdapter.GastoViewHolder> {
    private ArrayList<Gasto> listaGastos;
    
    // Construtor recebe dados
    public GastoAdapter(ArrayList<Gasto> listaGastos) {
        this.listaGastos = listaGastos;
    }
    
    // Adapta dados para ViewHolder
    @Override
    public void onBindViewHolder(@NonNull GastoViewHolder holder, int position) {
        Gasto gasto = listaGastos.get(position);
        holder.textDescricao.setText(gasto.getDescricao());
        holder.textValor.setText(formatarMoeda(gasto.getValor()));
    }
}
```

**Benefícios**:
- Separação entre dados e apresentação
- Reutilização do adapter em múltiplas Activities
- Fácil manutenção e extensão

**Padrões Relacionados**:
- ViewHolder Pattern (otimização de performance)
- Observer Pattern (notificação de mudanças)

---

### **2. Observer Pattern**
**Objetivo**: Notificar mudanças nos dados para atualização da UI

**Implementação**:
```java
// No adapter
adapter.notifyDataSetChanged(); // Todos os itens
adapter.notifyItemChanged(position); // Item específico
adapter.notifyItemInserted(position); // Novo item
adapter.notifyItemRemoved(position); // Item removido

// No listener de clique longo
public interface OnItemLongClickListener {
    void onItemLongClick(int position);
}
```

**Benefícios**:
- Atualização automática da UI
- Baixo acoplamento entre componentes
- Suporte a múltiplos observadores

**Variações**:
- **Push Observer**: Notificação imediata de mudanças
- **Pull Observer**: Consulta periódica por mudanças

---

### **3. Repository Pattern (Parcial)**
**Objetivo**: Abstrair acesso a dados e operações de persistência

**Implementação Atual**:
```java
// Na MainActivity
private void salvarListaGastos() {
    Gson gson = new Gson();
    String json = gson.toJson(listaGastos);
    SharedPreferences.Editor editor = preferences.edit();
    editor.putString("lista_gastos", json);
    editor.apply();
}

private void recuperarListaGastos() {
    Gson gson = new Gson();
    String json = preferences.getString("lista_gastos", null);
    if (json != null) {
        Type type = new TypeToken<ArrayList<Gasto>>() {}.getType();
        listaGastos = gson.fromJson(json, type);
    }
}
```

**Implementação Sugerida (Futuro)**:
```java
public interface GastoRepository {
    LiveData<List<Gasto>> obterTodos();
    LiveData<Gasto> obterPorId(int id);
    void inserir(Gasto gasto);
    void atualizar(Gasto gasto);
    void excluir(Gasto gasto);
    LiveData<Double> obterTotal();
}
```

**Benefícios**:
- Isolamento da lógica de persistência
- Fácil troca de implementação (ex: SharedPreferences → Room)
- Testabilidade

---

### **4. Builder Pattern (Implícito)**
**Objetivo**: Construção flexível de objetos complexos

**Implementação**:
```java
public class Gasto {
    private String descricao;
    private double valor;
    private String categoria;
    private String formaPagamento;
    private String data;
    
    // Construtor com todos os parâmetros
    public Gasto(String descricao, double valor, String categoria, 
                 String formaPagamento, String data) {
        this.descricao = descricao;
        this.valor = valor;
        this.categoria = categoria;
        this.formaPagamento = formaPagamento;
        this.data = data;
    }
}

// Uso
Gasto gasto = new Gasto("Supermercado", 350.50, "Alimentação", "Cartão", "20/06/2026");
```

**Padrão Sugerido (Futuro)**:
```java
public class GastoBuilder {
    private String descricao;
    private double valor;
    private String categoria = "Outros";
    private String formaPagamento = "Dinheiro";
    private String data;
    
    public GastoBuilder setDescricao(String descricao) {
        this.descricao = descricao;
        return this;
    }
    
    public GastoBuilder setValor(double valor) {
        this.valor = valor;
        return this;
    }
    
    // ... outros setters
    
    public Gasto build() {
        return new Gasto(descricao, valor, categoria, formaPagamento, data);
    }
}
```

---

### **5. Factory Method Pattern**
**Objetivo**: Criação flexível de objetos relacionados

**Implementação Sugerida**:
```java
public class MovimentoFactory {
    public static Movimento criarDeEntrada(Entrada entrada) {
        return new Movimento(
            entrada.getDescricao(),
            entrada.getValor(),
            "ENTRADA",
            entrada.getData()
        );
    }
    
    public static Movimento criarDeGasto(Gasto gasto) {
        return new Movimento(
            gasto.getDescricao(),
            gasto.getValor(),
            "GASTO",
            gasto.getData()
        );
    }
}
```

**Benefícios**:
- Encapsulamento da lógica de criação
- Fácil extensão para novos tipos
- Código mais limpo no cliente

---

### **6. Strategy Pattern (Potencial)**
**Objetivo**: Algoritmos intercambiáveis para cálculos

**Implementação Sugerida**:
```java
public interface CalculoStrategy {
    double calcular(List<Movimento> movimentos);
}

public class CalculoSaldoTotal implements CalculoStrategy {
    @Override
    public double calcular(List<Movimento> movimentos) {
        double total = 0;
        for (Movimento movimento : movimentos) {
            if (movimento.getTipo().equals("ENTRADA")) {
                total += movimento.getValor();
            } else {
                total -= movimento.getValor();
            }
        }
        return total;
    }
}

public class CalculoMediaMensal implements CalculoStrategy {
    @Override
    public double calcular(List<Movimento> movimentos) {
        // Implementação específica
        return 0;
    }
}
```

---

## 🔄 Padrões de Navegação

### **1. Navigation Pattern - Simple Stack**
```java
// Navegação básica entre Activities
Intent intent = new Intent(MainActivity.this, ListaGastosActivity.class);
startActivity(intent);
```

**Padrão Sugerido (Futuro)**:
```java
// Com Navigation Component
NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
navController.navigate(R.id.action_main_to_gastos);
```

### **2. Data Passing Pattern**
```java
// Passagem simples de dados (atual)
// Dados compartilhados via SharedPreferences

// Padrão sugerido (futuro)
Bundle bundle = new Bundle();
bundle.putSerializable("gasto", gasto);
intent.putExtras(bundle);
```

---

## 🛡️ Padrões de Segurança

### **1. Input Validation Pattern**
```java
private boolean validarFormularioGasto() {
    if (editDescricao.getText().toString().trim().isEmpty()) {
        editDescricao.setError("Descrição obrigatória");
        return false;
    }
    
    if (editValor.getText().toString().trim().isEmpty()) {
        editValor.setError("Valor obrigatório");
        return false;
    }
    
    try {
        double valor = Double.parseDouble(editValor.getText().toString());
        if (valor <= 0) {
            editValor.setError("Valor deve ser positivo");
            return false;
        }
    } catch (NumberFormatException e) {
        editValor.setError("Valor inválido");
        return false;
    }
    
    return true;
}
```

### **2. Error Handling Pattern**
```java
public class AppExceptionHandler {
    public static void handle(Exception e, Context context) {
        if (e instanceof NumberFormatException) {
            Toast.makeText(context, "Formato numérico inválido", Toast.LENGTH_SHORT).show();
        } else if (e instanceof IOException) {
            Toast.makeText(context, "Erro de leitura/gravação", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Erro inesperado: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        
        // Log para analytics/crash reporting
        Log.e("AppException", "Erro ocorrido", e);
    }
}
```

---

## 📈 Padrões de Performance

### **1. ViewHolder Pattern**
```java
public static class GastoViewHolder extends RecyclerView.ViewHolder {
    TextView textDescricao;
    TextView textCategoria;
    TextView textValor;
    
    public GastoViewHolder(@NonNull View itemView) {
        super(itemView);
        textDescricao = itemView.findViewById(R.id.textDescricao);
        textCategoria = itemView.findViewById(R.id.textCategoria);
        textValor = itemView.findViewById(R.id.textValor);
    }
}
```

**Benefícios**:
- Reutilização de views
- Redução de findViewById()
- Melhoria de scrolling performance

### **2. Lazy Loading Pattern**
**Implementação Sugerida**:
```java
public class LazyImageLoader {
    public static void loadImage(String url, ImageView imageView) {
        // Carregamento assíncrono com cache
        // Implementação com Glide/Picasso no futuro
    }
}
```

---

## 🧪 Padrões de Testabilidade

### **1. Dependency Injection Pattern (Futuro)**
```java
// Com Dagger/Hilt
@Module
@InstallIn(ActivityComponent.class)
public class AppModule {
    @Provides
    @Singleton
    public Gson provideGson() {
        return new Gson();
    }
    
    @Provides
    public SharedPreferences provideSharedPreferences(@ApplicationContext Context context) {
        return context.getSharedPreferences("dados", Context.MODE_PRIVATE);
    }
}
```

### **2. Test Double Pattern**
```java
// Repository fake para testes
public class FakeGastoRepository implements GastoRepository {
    private List<Gasto> gastos = new ArrayList<>();
    
    @Override
    public LiveData<List<Gasto>> obterTodos() {
        MutableLiveData<List<Gasto>> liveData = new MutableLiveData<>();
        liveData.setValue(gastos);
        return liveData;
    }
    
    @Override
    public void inserir(Gasto gasto) {
        gastos.add(gasto);
    }
}
```

---

## 🔮 Padrões para Evolução Futura

### **1. Migration Pattern - MVP para MVVM**
```java
// Passo 1: Criar ViewModels
public class GastoViewModel extends ViewModel {
    private GastoRepository repository;
    private MutableLiveData<List<Gasto>> gastos = new MutableLiveData<>();
    
    public LiveData<List<Gasto>> getGastos() {
        return gastos;
    }
    
    public void carregarGastos() {
        gastos.setValue(repository.obterTodos());
    }
}

// Passo 2: Migrar Activities gradualmente
// Passo 3: Remover lógica de negócio das Activities
```

### **2. Feature Flag Pattern**
```java
public class FeatureFlags {
    public static boolean isNovaFuncionalidadeHabilitada() {
        return BuildConfig.DEBUG || 
               PreferencesManager.getBoolean("nova_funcionalidade", false);
    }
}

// Uso
if (FeatureFlags.isNovaFuncionalidadeHabilitada()) {
    // Novo código
} else {
    // Código legado
}
```

---

## 📋 Checklist de Aplicação de Padrões

| Padrão | Status | Prioridade | Responsável |
|--------|--------|------------|-------------|
| Adapter Pattern | ✅ Implementado | Alta | Todos |
| Observer Pattern | ✅ Implementado | Alta | Todos |
| Repository Pattern | ⚠️ Parcial | Média | Backend |
| Builder Pattern | ⚠️ Implícito | Baixa | Todos |
| Factory Method | 🔄 Sugerido | Média | Core |
| Strategy Pattern | 🔄 Sugerido | Baixa | Core |
| Dependency Injection | 🔄 Futuro | Alta | Arquitetura |
| Feature Flag | 🔄 Futuro | Média | DevOps |

---

## 📚 Referências

1. **Design Patterns**: Gamma et al. - "Design Patterns: Elements of Reusable Object-Oriented Software"
2. **Android Patterns**: Google Android Documentation
3. **Clean Architecture**: Robert C. Martin
4. **MVVM Guide**: Android Architecture Components

---

**Última Atualização**: Junho 2026  
**Versão**: 1.0  
**Responsável**: Arquitetura de Software  

*Este documento deve ser revisado a cada nova feature significativa ou mudança arquitetural.*