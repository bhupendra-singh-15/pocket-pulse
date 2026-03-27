package app.pocketpulse.repository;

import app.pocketpulse.entity.ProfileEntity;
import app.pocketpulse.entity.TransactionEntity;
import app.pocketpulse.entity.enums.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    // GET ALL (sorted - FIXED)
    List<TransactionEntity> findByProfileOrderByDateDescCreatedAtDesc(
            ProfileEntity profile
    );

    //  GET PROFILE
    List<TransactionEntity> findByProfileAndDate(
            ProfileEntity profile,
            LocalDate date
    );

    //  FILTER BY TYPE (FIXED)
    List<TransactionEntity> findByProfileAndTypeOrderByDateDescCreatedAtDesc(
            ProfileEntity profile,
            CategoryType type
    );

    //  FILTER BY DATE RANGE (FIXED)
    List<TransactionEntity> findByProfileAndDateBetweenOrderByDateDescCreatedAtDesc(
            ProfileEntity profile,
            LocalDate startDate,
            LocalDate endDate
    );

    //  FILTER BY TYPE + DATE RANGE (FIXED)
    List<TransactionEntity> findByProfileAndTypeAndDateBetweenOrderByDateDescCreatedAtDesc(
            ProfileEntity profile,
            CategoryType type,
            LocalDate startDate,
            LocalDate endDate
    );

    //  OPTIONAL (RECOMMENDED) → LIMIT DASHBOARD DATA
    List<TransactionEntity> findTop10ByProfileAndDateBetweenOrderByDateDescCreatedAtDesc(
            ProfileEntity profile,
            LocalDate startDate,
            LocalDate endDate
    );

    @Query("""
    SELECT t FROM TransactionEntity t
    WHERE t.profile = :profile
    AND (:type IS NULL OR t.type = :type)
    AND (:startDate IS NULL OR t.date >= :startDate)
    AND (:endDate IS NULL OR t.date <= :endDate)
    AND (
        :keyword IS NULL OR
        LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
        LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
        LOWER(t.category.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
    )
""")
    List<TransactionEntity> searchTransactions(
            @Param("profile") ProfileEntity profile,
            @Param("type") CategoryType type,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("keyword") String keyword,
            Sort sort
    );

    //  FIND ONE (SECURITY SAFE)
    Optional<TransactionEntity> findByIdAndProfile(Long id, ProfileEntity profile);

    //  SUM FOR DASHBOARD
    @Query("""
        SELECT COALESCE(SUM(t.amount), 0)
        FROM TransactionEntity t
        WHERE t.profile = :profile AND t.type = :type
    """)
    BigDecimal sumAmountByType(
            @Param("profile") ProfileEntity profile,
            @Param("type") CategoryType type
    );
}