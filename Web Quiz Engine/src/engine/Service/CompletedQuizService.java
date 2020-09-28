package engine.Service;

import engine.DAO.CompletedQuizRepository;
import engine.Model.CompletedQuizRecord;
import engine.Model.Quiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class CompletedQuizService{
    @Autowired
    CompletedQuizRepository completedQuizRepository;

    public CompletedQuizRecord save(CompletedQuizRecord record) {
        return completedQuizRepository.save(record);
    }
    public List<CompletedQuizRecord> findAll() {
        return completedQuizRepository.findAll();
    }

    public Page<CompletedQuizRecord> findAllPaged(Integer page, Integer size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Page<CompletedQuizRecord> pagedResult = completedQuizRepository.findAll(pageable);

        return pagedResult;
    }

    public void deleteAll() {
        completedQuizRepository.deleteAll();
    }

    public Page<CompletedQuizRecord> findAllByUserMailPaged(Integer page, Integer size, String sortBy, String userMail) {
        Pageable pageable = PageRequest.of(page, size,  Sort.by("completedAt").descending());

        Page<CompletedQuizRecord> pagedResult = completedQuizRepository.findAllByUserMail(pageable, userMail);

        return pagedResult;
    }
}
