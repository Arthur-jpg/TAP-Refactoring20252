#!/bin/bash

# Script para consultas rápidas no banco universidade_db
# Uso: ./consulta_db.sh [comando]

DB_HOST="localhost"
DB_PORT="3306"
DB_NAME="universidade_db"
DB_USER="root"
DB_PASS="admin"

MYSQL_CMD="mysql -u $DB_USER -p$DB_PASS -h $DB_HOST -P $DB_PORT $DB_NAME"

case "$1" in
    "tabelas")
        echo "=== TABELAS DO BANCO ==="
        $MYSQL_CMD -e "SHOW TABLES;"
        ;;
    "alunos")
        echo "=== TODOS OS ALUNOS ==="
        $MYSQL_CMD -e "SELECT matricula, nome, estado_civil, idade, curso_codigo FROM alunos;"
        ;;
    "cursos")
        echo "=== TODOS OS CURSOS ==="
        $MYSQL_CMD -e "SELECT * FROM cursos;"
        ;;
    "disciplinas")
        echo "=== TODAS AS DISCIPLINAS ==="
        $MYSQL_CMD -e "SELECT * FROM disciplinas;"
        ;;
    "turmas")
        echo "=== TODAS AS TURMAS ==="
        $MYSQL_CMD -e "SELECT * FROM turmas;"
        ;;
    "inscricoes")
        echo "=== TODAS AS INSCRIÇÕES ==="
        $MYSQL_CMD -e "SELECT * FROM inscricoes;"
        ;;
    "estrutura")
        echo "=== ESTRUTURA DE TODAS AS TABELAS ==="
        for table in alunos cursos disciplinas turmas inscricoes aluno_telefones; do
            echo "--- Tabela: $table ---"
            $MYSQL_CMD -e "DESCRIBE $table;"
            echo ""
        done
        ;;
    "relatorio")
        echo "=== RELATÓRIO COMPLETO ==="
        echo "--- Contadores ---"
        $MYSQL_CMD -e "
        SELECT 
            (SELECT COUNT(*) FROM alunos) as total_alunos,
            (SELECT COUNT(*) FROM cursos) as total_cursos,
            (SELECT COUNT(*) FROM disciplinas) as total_disciplinas,
            (SELECT COUNT(*) FROM turmas) as total_turmas,
            (SELECT COUNT(*) FROM inscricoes) as total_inscricoes;
        "
        echo ""
        echo "--- Alunos por Curso ---"
        $MYSQL_CMD -e "
        SELECT c.nome as curso, COUNT(a.matricula) as total_alunos
        FROM cursos c 
        LEFT JOIN alunos a ON c.codigo = a.curso_codigo 
        GROUP BY c.codigo, c.nome;
        "
        ;;
    *)
        echo "Uso: $0 [comando]"
        echo ""
        echo "Comandos disponíveis:"
        echo "  tabelas     - Lista todas as tabelas"
        echo "  alunos      - Lista todos os alunos"
        echo "  cursos      - Lista todos os cursos"
        echo "  disciplinas - Lista todas as disciplinas"
        echo "  turmas      - Lista todas as turmas"
        echo "  inscricoes  - Lista todas as inscrições"
        echo "  estrutura   - Mostra estrutura de todas as tabelas"
        echo "  relatorio   - Relatório completo com contadores e estatísticas"
        echo ""
        echo "Exemplo: $0 alunos"
        ;;
esac