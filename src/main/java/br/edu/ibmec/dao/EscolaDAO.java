package br.edu.ibmec.dao;

/**
 * Aplicação com serviços REST para gestão de cursos.
 *
 * @author  Thiago Silva de Souza
 * @version 1.0
 * @since   2012-02-29
 */

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.collections4.map.MultiKeyMap;

import entity.Aluno;
import entity.Curso;
import entity.Data;
import entity.Disciplina;
import entity.EstadoCivil;
import entity.Inscricao;
import entity.Turma;
import exception.DaoException;

public class EscolaDAO {

    private Map<Integer, Aluno> alunos;
    private Map<Integer, Curso> cursos;
    private Map<Integer, Disciplina> disciplinas;
    private MultiKeyMap turmas;
    private MultiKeyMap inscricoes;

    private static EscolaDAO instance;

    public EscolaDAO() {
        alunos = new HashMap<Integer, Aluno>();
        cursos = new HashMap<Integer, Curso>();
        disciplinas = new HashMap<Integer, Disciplina>();
        turmas = new MultiKeyMap();
        inscricoes = new MultiKeyMap();

        Curso curso = new Curso(99, "Computacao");
        try {
            this.addCurso(curso);
        } catch (DaoException e) {
            e.printStackTrace();
        }

        Disciplina disciplina = new Disciplina(123, "UML 2", curso);

        Vector<String> telefones = new Vector<String>();
        telefones.add("2177776666");
        telefones.add("3177776669");
        telefones.add("6177778889");

        try {
            this.addDisciplina(disciplina);
        } catch (DaoException e) {
            e.printStackTrace();
        }

        Data data = new Data(10, 10, 1990);
        Aluno aluno = new Aluno(11, "Joao da Silva", data, true,
                EstadoCivil.solteiro, curso, telefones);
        curso.getAlunos().add(aluno);

        try {
            this.addAluno(aluno);
        } catch (DaoException e) {
            e.printStackTrace();
        }

        Turma turma = new Turma(123, 2010, 1, disciplina);
        Turma turma2 = new Turma(321, 2010, 1, disciplina);

        try {
            this.addTurma(turma);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        // System.out.println(turmas.get(123, 2010, 1));

        turmas.put(turma2.getCodigo(), turma2.getAno(), turma2.getSemestre(),
                turma2); // ao incluir deve-se colocar as chaves e por ultimo o
        // valor
        System.out.println(turmas.get(321, 2010, 1)); // ao buscar deve-se
        // passar todas as chaves

        try {
            System.out.println(this.getTurmas());
        } catch (DaoException e) {
            e.printStackTrace();
        }

        Inscricao inscricao = new Inscricao(6.0f, 8.0f, 10, "aprovado", aluno,
                turma2);
        inscricoes.put(aluno.getMatricula(), turma2.getCodigo(), turma2
                .getAno(), turma2.getSemestre(), inscricao);
        System.out.println(inscricoes);

    }

    public static EscolaDAO getInstance() {
        if (instance == null) {
            instance = new EscolaDAO();
        }
        return instance;
    }

    // ok
    public void addAluno(Aluno a) throws DaoException {
        alunos.put(a.getMatricula(), a);
        getAluno(a.getMatricula());
    }

    // ok
    public Aluno updateAluno(Aluno alunoNovo) throws DaoException {
        alunos.put(alunoNovo.getMatricula(), alunoNovo);
        return alunoNovo;
    }

    // ok
    public Collection<Aluno> getAlunos() {
        return alunos.values();
    }

    // ok
    public Aluno getAluno(int codAluno) throws DaoException {
        if (alunos.get(codAluno) == null) {
            throw new DaoException("");
        }
        return alunos.get(codAluno);
    }

    // ok
    public void removeAluno(int codAluno) throws DaoException {
        if (alunos.get(codAluno) == null) {
            throw new DaoException("");
        }
        alunos.remove(codAluno);
    }

    // ok
    public void addCurso(Curso c) throws DaoException {
        cursos.put(c.getCodigo(), c);
        getCurso(c.getCodigo());
    }

    // ok
    public Curso updateCurso(Curso cursoNovo) throws DaoException {
        cursos.put(cursoNovo.getCodigo(), cursoNovo);
        return cursoNovo;
    }

    // ok
    public Curso getCurso(int codCurso) throws DaoException {
        if (cursos.get(codCurso) == null) {
            throw new DaoException("");
        }
        Curso curso=  cursos.get(codCurso);
        System.out.println(curso.getAlunos());
        return curso;

    }

    // ok
    public Collection<Curso> getCursos() {
        return cursos.values();
    }

    // ok
    public void removeCurso(int codCurso) throws DaoException {
        if (cursos.get(codCurso) == null) {
            throw new DaoException("");
        }
        Curso curso = getCurso(codCurso);
        if (curso.getAlunos().isEmpty()) {
            System.out.println(curso.getAlunos().size());
            cursos.remove(codCurso);
        } else {
            throw new DaoException("");
        }
    }

    // ok
    public void addDisciplina(Disciplina d) throws DaoException {
        disciplinas.put(d.getCodigo(), d);
        getDisciplina(d.getCodigo());
    }

    // ok
    public Disciplina updateDisciplina(Disciplina disciplinaNova)
            throws DaoException {
        disciplinas.put(disciplinaNova.getCodigo(), disciplinaNova);
        return disciplinaNova;
    }

    // ok
    public Disciplina getDisciplina(int codDisciplina) throws DaoException {
        if (disciplinas.get(codDisciplina) == null) {
            throw new DaoException("");
        }
        return disciplinas.get(codDisciplina);
    }

    // ok
    public Collection<Disciplina> getDisciplinas() {
        return disciplinas.values();
    }

    // ok
    public void removeDisciplina(int codDisciplina) throws DaoException {
        if (disciplinas.get(codDisciplina) == null) {
            throw new DaoException("");
        }
        disciplinas.remove(codDisciplina);
    }

    // ok
    public void addTurma(Turma t) throws DaoException {
        turmas.put(t.getCodigo(), t.getAno(), t.getSemestre(), t);
    }

    // ok
    public Turma updateTurma(Turma turmaNova) throws DaoException {
        turmas.put(turmaNova.getCodigo(), turmaNova.getAno(), turmaNova
                .getSemestre(), turmaNova);
        return turmaNova;
    }

    // ok
    public Turma getTurma(int codTurma, int ano, int semestre)
            throws DaoException {
        if (turmas.get(codTurma, ano, semestre) == null) {
            throw new DaoException("");
        }
        return (Turma) turmas.get(codTurma, ano, semestre);
    }

    public Collection<Turma> getTurmas() throws DaoException {
        return turmas.values();
    }

    // ok
    public void removeTurma(int codTurma, int ano, int semestre)
            throws DaoException {
        if (turmas.get(codTurma, ano, semestre) == null) {
            throw new DaoException("");
        }
        turmas.removeMultiKey(codTurma, ano, semestre);
    }

    // ok
    public void addInscricao(Inscricao i) throws DaoException {
        inscricoes.put(i.getTurma().getCodigo(), i.getTurma().getAno(), i
                .getTurma().getSemestre(), i.getAluno().getMatricula(), i);
    }

    // ok
    public Inscricao updateInscricao(Inscricao inscricaoNova)
            throws DaoException {
        inscricoes.put(inscricaoNova.getTurma().getCodigo(), inscricaoNova
                        .getTurma().getAno(), inscricaoNova.getTurma().getSemestre(),
                inscricaoNova.getAluno().getMatricula(), inscricaoNova);
        return inscricaoNova;
    }

    // ok
    public Inscricao getInscricao(int matricula, int codigo, int ano,
                                  int semestre) throws DaoException {
        System.out.println("campos:" + matricula + "-" + codigo + "-" + ano
                + "-" + semestre);
        // if (inscricoes.get(getAluno(matricula).getMatricula(),
        // getTurma(codigo, ano, semestre)) == null) {
        Inscricao inscricao = (Inscricao) inscricoes.get(matricula, codigo,
                ano, semestre);
        if (inscricao == null) {
            System.out.println("null");
            throw new DaoException("");
        }
        return inscricao;
    }

    public Collection<Inscricao> getInscricoes() throws DaoException {
        return inscricoes.values();
    }

    // ok
    public void removeInscricao(int matricula, int codigo, int ano, int semestre)
            throws DaoException {
        if (inscricoes.get(matricula, codigo, ano, semestre) == null) {
            throw new DaoException("");
        }
        inscricoes.removeMultiKey(matricula, codigo, ano, semestre);
    }

}