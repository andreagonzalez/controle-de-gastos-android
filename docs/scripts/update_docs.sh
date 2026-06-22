#!/bin/bash

# Script para atualizar e validar a documentação do projeto ControleDeGastos
# Autor: Equipe de Arquitetura
# Data: Junho 2026

set -e

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}=== Atualização de Documentação - ControleDeGastos ===${NC}\n"

# Configurações
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
DOCS_DIR="$PROJECT_ROOT/docs"
TIMESTAMP=$(date '+%Y-%m-%d %H:%M:%S')

# Funções de utilidade
print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

check_file_exists() {
    if [ ! -f "$1" ]; then
        print_error "Arquivo não encontrado: $1"
        return 1
    fi
    return 0
}

# 1. Atualizar timestamp nos documentos principais
update_timestamps() {
    echo "1. Atualizando timestamps..."
    
    # ARQUITETURA.md
    if check_file_exists "$DOCS_DIR/arquitetura/ARQUITETURA.md"; then
        sed -i '' "s/|Última Atualização.*|/|Última Atualização | $TIMESTAMP |/g" "$DOCS_DIR/arquitetura/ARQUITETURA.md"
        sed -i '' "s/**Última Atualização**:.*/**Última Atualização**: $TIMESTAMP/" "$DOCS_DIR/arquitetura/ARQUITETURA.md"
        print_success "Timestamp atualizado em ARQUITETURA.md"
    fi
    
    # DIAGRAMAS_UML.md
    if check_file_exists "$DOCS_DIR/diagramas/DIAGRAMAS_UML.md"; then
        sed -i '' "s/*Documentação gerada em:.*/*Documentação gerada em: $TIMESTAMP/" "$DOCS_DIR/diagramas/DIAGRAMAS_UML.md"
        print_success "Timestamp atualizado em DIAGRAMAS_UML.md"
    fi
    
    # PADROES_E_DECISOES.md
    if check_file_exists "$DOCS_DIR/patterns/PADROES_E_DECISOES.md"; then
        sed -i '' "s/**Última Atualização**:.*/**Última Atualização**: $TIMESTAMP/" "$DOCS_DIR/patterns/PADROES_E_DECISOES.md"
        print_success "Timestamp atualizado em PADROES_E_DECISOES.md"
    fi
}

# 2. Validar estrutura de arquivos
validate_structure() {
    echo -e "\n2. Validando estrutura de arquivos..."
    
    required_files=(
        "$DOCS_DIR/README.md"
        "$DOCS_DIR/arquitetura/ARQUITETURA.md"
        "$DOCS_DIR/diagramas/DIAGRAMAS_UML.md"
        "$DOCS_DIR/patterns/PADROES_E_DECISOES.md"
        "$DOCS_DIR/.gitignore"
    )
    
    all_files_exist=true
    for file in "${required_files[@]}"; do
        if [ -f "$file" ]; then
            print_success "Arquivo encontrado: $(basename "$file")"
        else
            print_error "Arquivo faltando: $(basename "$file")"
            all_files_exist=false
        fi
    done
    
    if [ "$all_files_exist" = true ]; then
        print_success "Todos os arquivos obrigatórios existem"
    else
        print_warning "Alguns arquivos obrigatórios estão faltando"
    fi
}

# 3. Verificar links quebrados (básico)
check_broken_links() {
    echo -e "\n3. Verificando links internos..."
    
    local errors=0
    
    # Verificar referências entre documentos
    if grep -q "\./decision-records/" "$DOCS_DIR/arquitetura/ARQUITETURA.md" && [ ! -d "$DOCS_DIR/decision-records" ]; then
        print_warning "ARQUITETURA.md referencia decision-records/ mas diretório não existe"
        ((errors++))
    fi
    
    if grep -q "\./guias/" "$DOCS_DIR/README.md" && [ ! -d "$DOCS_DIR/guias" ]; then
        print_warning "README.md referencia guias/ mas diretório não existe"
        ((errors++))
    fi
    
    if [ $errors -eq 0 ]; then
        print_success "Nenhum link interno quebrado encontrado"
    else
        print_warning "Encontrados $errors links quebrados"
    fi
}

# 4. Gerar resumo de status
generate_status_report() {
    echo -e "\n4. Gerando relatório de status..."
    
    local total_files=$(find "$DOCS_DIR" -name "*.md" -type f | wc -l | tr -d ' ')
    local total_lines=$(find "$DOCS_DIR" -name "*.md" -type f -exec wc -l {} + | tail -1 | awk '{print $1}')
    local last_updated=$(find "$DOCS_DIR" -name "*.md" -type f -exec stat -f "%Sm" {} + | sort -r | head -1)
    
    echo "📊 Estatísticas da Documentação:"
    echo "   • Total de arquivos Markdown: $total_files"
    echo "   • Total de linhas: $total_lines"
    echo "   • Última modificação: $last_updated"
    
    # Verificar se há ADRs
    local adr_count=$(find "$DOCS_DIR/decision-records" -name "ADR-*.md" -type f 2>/dev/null | wc -l | tr -d ' ')
    echo "   • ADRs (Architectural Decision Records): $adr_count"
    
    # Verificar se há diagramas
    local diagram_count=$(find "$DOCS_DIR/diagramas" -name "*.md" -type f 2>/dev/null | wc -l | tr -d ' ')
    echo "   • Documentos de diagramas: $diagram_count"
}

# 5. Verificar consistência
check_consistency() {
    echo -e "\n5. Verificando consistência..."
    
    local inconsistencies=0
    
    # Verificar se todos os documentos têm cabeçalho
    for md_file in $(find "$DOCS_DIR" -name "*.md" -type f); do
        if ! head -1 "$md_file" | grep -q "^#"; then
            print_warning "Documento sem cabeçalho H1: $md_file"
            ((inconsistencies++))
        fi
    done
    
    # Verificar se há referências a padrões implementados vs sugeridos
    if grep -q "🔄 Sugerido" "$DOCS_DIR/patterns/PADROES_E_DECISOES.md"; then
        print_success "Padrões sugeridos identificados (para evolução futura)"
    fi
    
    if [ $inconsistencies -eq 0 ]; then
        print_success "Documentação consistente"
    else
        print_warning "Encontradas $inconsistencies inconsistências"
    fi
}

# Menu principal
main() {
    echo "Selecione uma operação:"
    echo "  1) Atualizar timestamps apenas"
    echo "  2) Validar estrutura completa"
    echo "  3) Executar todas as verificações"
    echo "  4) Sair"
    echo -n "Opção: "
    
    read -r option
    
    case $option in
        1)
            update_timestamps
            ;;
        2)
            validate_structure
            check_broken_links
            generate_status_report
            check_consistency
            ;;
        3)
            update_timestamps
            validate_structure
            check_broken_links
            generate_status_report
            check_consistency
            echo -e "\n${GREEN}✅ Todas as verificações concluídas!${NC}"
            ;;
        4)
            echo "Saindo..."
            exit 0
            ;;
        *)
            print_error "Opção inválida"
            exit 1
            ;;
    esac
    
    echo -e "\n${GREEN}=== Operação concluída ===${NC}"
}

# Executar menu
if [[ "${BASH_SOURCE[0]}" == "${0}" ]]; then
    main "$@"
fi