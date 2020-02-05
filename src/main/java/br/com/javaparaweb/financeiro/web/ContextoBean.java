package br.com.javaparaweb.financeiro.web;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.faces.event.ValueChangeEvent;

import br.com.javaparaweb.financeiro.conta.*;
import br.com.javaparaweb.financeiro.usuario.*;

@ManagedBean
//your life time is while the user is logged
@SessionScoped
public class ContextoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4693275947797122717L;
	private int codigoContaAtiva       = 0;
	private Conta	contaAtiva	       = null;
	
	public Usuario getUsuarioLogado() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();

		String login = externalContext.getRemoteUser();
        System.out.println("login = " + login);
        
		if (login != null) {
			UsuarioRN usuarioRN = new UsuarioRN();
			return usuarioRN.buscarPorLogin(login);
		}

		return null;
	}

	public Conta getContaAtiva() {
		if (this.contaAtiva == null) {
			Usuario usuario = this.getUsuarioLogado();

			ContaRN contaRN = new ContaRN();
			this.contaAtiva = contaRN.buscarFavorita(usuario);
			
			
			if (this.contaAtiva == null) {
				System.out.println("conta ativa is null");
				List<Conta> contas = contaRN.listar(usuario);
				if (contas != null) {
					System.out.println("user has accounts");
					for (Conta conta : contas) {
						this.contaAtiva = conta;
						System.out.println("chose accout :" + conta.getDescricao());
						break;
					}
				}
			}
		}
		System.out.println("conta ativa is not null" + this.contaAtiva.getDescricao());
		return this.contaAtiva;
	}


	public void setContaAtiva(ValueChangeEvent event) {

		Integer conta = (Integer) event.getNewValue();

		ContaRN contaRN = new ContaRN();
		this.contaAtiva = contaRN.carregar(conta);
	}

}
