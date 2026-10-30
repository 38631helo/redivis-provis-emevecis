package escola.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import escola.model.Sala;

public class EscolaRepository {
    public interface EscolaRepository extends JpaRepository<Sala, Long>{}
}
