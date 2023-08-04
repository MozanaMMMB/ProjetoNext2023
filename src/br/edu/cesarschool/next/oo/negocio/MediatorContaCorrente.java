package br.edu.cesarschool.next.oo.negocio;

import java.util.Arrays;
import java.util.List;

import br.edu.cesarschool.next.oo.dao.DAOContaCorrente;
import br.edu.cesarschool.next.oo.entidade.ContaCorrente;
import br.edu.cesarschool.next.oo.entidade.ContaPoupanca;

public class MediatorContaCorrente {
	DAOContaCorrente daoContaCorrente = new DAOContaCorrente();

	public MediatorContaCorrente() {

	}

	public String incluir(ContaCorrente conta) {
		if (stringNulaOuVazia(conta.getNumero())) {
			return "Número de conta inválido!";
		} else if (conta.getNumero().length() < 5 || conta.getNumero().length() > 8) {
			return "Número de caracteres inválidos";
		} else if (conta.getSaldo() < 0) {
			return "Valor do saldo não pode ser menor que zero";
		} else if (stringNulaOuVazia(conta.getNomeCorrentista())) {
			return "Nome inválido!";
		} else if (conta.getNomeCorrentista().length() > 60) {
			return "O nome do correntista não pode ter mais de 60 caracteres!";
		} else if (conta instanceof ContaPoupanca) {
			if (((ContaPoupanca) conta).getPercentualBonus() < 0) {
				return "O percentual de bônus não pode ser menordo que zero!";
			} else {
				boolean ret = daoContaCorrente.incluir(conta);
				if (!ret) {
					return "Conta já existente!";
				}
			}

		} else {
			boolean ret = daoContaCorrente.incluir(conta);
			if (!ret) {
				return "Conta já existente!";
			}
		}
		return null;
	}
	public String creditar(double valor, String numero) {
		if (stringNulaOuVazia(numero)) {
			return "Número de conta inválido!";
		} else if (valor < 0) {
			return "Valor não pode ser menor que zero!";
		} else {
			ContaCorrente conta = daoContaCorrente.buscar(numero);
			if (conta == null) {
				return "Conta não existente!";
			} else {
				conta.creditar(valor);
				daoContaCorrente.alterar(conta);
				return null;
			}
		}
	}

	public String debitar(double valor, String numero) {
		if (stringNulaOuVazia(numero)) {
			return "Número de conta inválido!";
		} else if (valor < 0) {
			return "Valor não pode ser menor que zero!";
		} else {
			ContaCorrente conta = daoContaCorrente.buscar(numero);
			if (conta == null) {
				return "Conta não existente!";
			} else {
				if (conta.getSaldo() < valor) {
					return "Saldo insuficiente!";
				} else {
					conta.debitar(valor);
					daoContaCorrente.alterar(conta);

					return null;
				}
			}
		}
	}

	public ContaCorrente buscar(String numero) {
		if (stringNulaOuVazia(numero)) {
			return null;
		} else {
			return daoContaCorrente.buscar(numero);
		}
	}

	public List<ContaCorrente> gerarRelatorioGeral() {
		ContaCorrente[] contas = daoContaCorrente.buscarTodos();
		List<ContaCorrente> listaContas = Arrays.asList(contas);
		listaContas.sort(new ComparadorContaCorrentisteSaldo());
		return listaContas;
	}

	private boolean stringNulaOuVazia(String numero){
		return numero == null || numero.trim().equals("");
	}
}
