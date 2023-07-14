package service;

import java.util.ArrayList;
import java.util.List;

public class NumberServiceImpl implements NumberService {
    @Override
    public List<Integer> getNumberSequence(int beginSeq, int endSeq) {
        var sequence = new ArrayList<Integer>();
        for (int i = beginSeq; i <= endSeq; i++) {
            sequence.add(i);
        }

        return sequence;
    }
}
