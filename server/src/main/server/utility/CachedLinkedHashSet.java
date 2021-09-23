package server.utility;
import Lab5.common.data.StudyGroup;
import sun.reflect.generics.tree.Tree;

import java.util.LinkedHashSet;
import java.util.TreeSet;

/**
 * Modified LinkedHashSet, which knows which element was last.
 * @param <StudyGroup> LinkedHashSet of StudyGroup Object.
 */
public class CachedLinkedHashSet<StudyGroup> extends LinkedHashSet<StudyGroup> {
    private StudyGroup last = null;
    @Override
    public boolean add(StudyGroup studyGroup){
        last = studyGroup;
        return super.add(studyGroup);
    }


    public StudyGroup getLast(){
        return last;
    }


}
