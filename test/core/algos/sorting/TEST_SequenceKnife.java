/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.algos.sorting;

import core.DNAStatesShifted;
import core.QueryWord;
import core.algos.SequenceKnife;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author ben
 */
public class TEST_SequenceKnife {
    public static void main(String[] args) {
        SequenceKnife knife=new SequenceKnife(7, 4, new DNAStatesShifted(), SequenceKnife.SAMPLING_SEQUENTIAL);
        knife.init("ATCGCTGATCGATCGA");
        System.out.println(Arrays.toString(knife.getMerOrder()));
        
        knife=new SequenceKnife(7, 4, new DNAStatesShifted(), SequenceKnife.SAMPLING_STOCHASTIC);
        knife.forceSeed(12345);
        knife.init("ATCGCTGATCGATCGA");
        System.out.println(Arrays.toString(knife.getMerOrder()));
        
        knife=new SequenceKnife(7, 4, new DNAStatesShifted(), SequenceKnife.SAMPLING_LINEAR);
        knife.init("ATCGCTGATCGATCGA");
        System.out.println(Arrays.toString(knife.getMerOrder()));
        
        byte[] w=null;
        while ((w=knife.getNextByteWord())!=null) {
            System.out.println(w);
        }
    }
}
