package pe.edu.upc.qhurinet.servicesinterfaces;

import pe.edu.upc.qhurinet.entities.Faq;

import java.util.List;
import java.util.Optional;

public interface IFaqService {
    public List<Faq> list();
    public Faq insert(Faq f);
    public Optional<Faq> listId(Long id);
    public void update(Faq f);
    public void delete(Long id);
    public List<Faq> activos();
}
