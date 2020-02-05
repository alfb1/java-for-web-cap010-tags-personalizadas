package br.com.javaparaweb.financeiro.web;

import java.util.List;
import javax.faces.bean.*;
import br.com.javaparaweb.financeiro.conta.Conta;
import br.com.javaparaweb.financeiro.conta.ContaRN;

@ManagedBean
@RequestScoped
public class ContaBean {
	private Conta contaSelecionada = new Conta();
	private List<Conta> lista = null;

	@ManagedProperty(value = "#{contextoBean}")
	private ContextoBean contextoBean;

	public String salvar() 
	{
		this.contaSelecionada.setUsuario(this.contextoBean.getUsuarioLogado());
		
		//usando a camada de regra de negocios para salvar o objeto
		ContaRN contaRN = new ContaRN();
		contaRN.salvar(this.contaSelecionada);
		
		this.contaSelecionada = new Conta();
		// força reload da lista
		this.lista = null;
		return null;
	}

	public String excluir() 
	{
		ContaRN contaRN = new ContaRN();
		contaRN.excluir(this.contaSelecionada);
		
		this.contaSelecionada = new Conta();
		this.lista = null;
		return null;
	}
	
	public List<Conta> getLista() 
	{
		if ( this.lista == null ) {
			System.out.println(" this.lista != null");
			ContaRN contaRN = new ContaRN();
			this.lista = contaRN.listar(this.contextoBean.getUsuarioLogado());
		}
		
		System.out.println("get Lista method");
		System.out.println("logged user " + this.contextoBean.getUsuarioLogado().getNome());
		
		if ( this.lista != null ) {
		System.out.println("get lista" + this.lista.size());
		}
		return this.lista;
	}
	
	public String tornarFavorita() 
	{
		ContaRN contaRN = new ContaRN();
		contaRN.tornarFavorita(this.contaSelecionada);
		
		this.contaSelecionada = new Conta();
		return null;
	}

	public Conta getContaSelecionada() {
		return contaSelecionada;
	}

	public void setContaSelecionada(Conta contaSelecionada) {
		this.contaSelecionada = contaSelecionada;
	}



	public ContextoBean getContextoBean() {
		return contextoBean;
	}

	public void setContextoBean(ContextoBean contextoBean) {
		this.contextoBean = contextoBean;
	}

}
