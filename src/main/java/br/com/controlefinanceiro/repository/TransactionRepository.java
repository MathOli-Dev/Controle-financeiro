package br.com.controlefinanceiro.repository;

import br.com.controlefinanceiro.dto.CategoryTotal;
import br.com.controlefinanceiro.model.Transaction;
import br.com.controlefinanceiro.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("select t from Transaction t join fetch t.category order by t.date desc, t.id desc")
    List<Transaction> findAllWithCategory();

    @Query("""
           select t from Transaction t join fetch t.category
           where t.date between :start and :end
           order by t.date desc, t.id desc
           """)
    List<Transaction> findByPeriod(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("""
           select coalesce(sum(t.amount), 0) from Transaction t
           where t.type = :type and t.date between :start and :end
           """)
    BigDecimal sumByTypeAndPeriod(@Param("type") TransactionType type,
                                  @Param("start") LocalDate start,
                                  @Param("end") LocalDate end);

    @Query("""
           select new br.com.controlefinanceiro.dto.CategoryTotal(c.name, sum(t.amount))
           from Transaction t join t.category c
           where t.type = :type and t.date between :start and :end
           group by c.name
           order by sum(t.amount) desc
           """)
    List<CategoryTotal> totalsByCategory(@Param("type") TransactionType type,
                                         @Param("start") LocalDate start,
                                         @Param("end") LocalDate end);
}
