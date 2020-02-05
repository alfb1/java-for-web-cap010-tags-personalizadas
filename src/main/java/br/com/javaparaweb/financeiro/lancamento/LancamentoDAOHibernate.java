package br.com.javaparaweb.financeiro.lancamento;

import java.util.*;
import java.math.BigDecimal;

import org.hibernate.*;
import org.hibernate.criterion.*;
import br.com.javaparaweb.financeiro.conta.Conta;


public class LancamentoDAOHibernate implements LancamentoDAO 
{
    private Session session;
   
    public void setSession(Session session) {
    	this.session = session;
    }
    
	@Override
	public void salvar(Lancamento lancamento) {
        this.session.saveOrUpdate(lancamento);
	}

	@Override
	public void excluir(Lancamento lancamento) {
        this.session.delete(lancamento);
	}

	@Override
	public Lancamento carregar(Integer lancamento) {
		return (Lancamento) this.session.get(Lancamento.class, lancamento);
	}

	@Override
	public List<Lancamento> listar(Conta conta, Date dataInicio, Date dataFim) 
	{
		Criteria criteria = this.session.createCriteria(Lancamento.class);
		
		if ( dataInicio != null && dataFim != null) {
			criteria.add(Restrictions.between("data", dataInicio, dataFim));
		}else if ( dataInicio != null) {
			criteria.add(Restrictions.ge("data", dataInicio)); //maior ou igual
		}else if (dataFim != null) {
			criteria.add(Restrictions.le("data", dataFim)); //menor ou igual
		}
		
		criteria.add(Restrictions.eq("conta", conta));
		criteria.addOrder(Order.asc("data"));
		
		return criteria.list();
	}

	@Override
	public float saldo(Conta conta, Date data) 
	{
		StringBuffer sql = new StringBuffer();
		
		sql.append("select sum( l.valor * c.fator)");
		sql.append("from lancamento l, ");
		sql.append(" Categoria c");
		sql.append(" where l.categoria = c.codigo ");
		sql.append("   and l.conta = :conta");
		sql.append("   and l.data <= :data" );
		
		SQLQuery query = this.session.createSQLQuery(sql.toString());
		query.setParameter("conta", conta.getConta());
		query.setParameter("data", data);
		
		BigDecimal saldo = (BigDecimal) query.uniqueResult();
		
		if ( saldo != null ) {
			return saldo.floatValue();
		}
		
		return 0f;
	}

}
