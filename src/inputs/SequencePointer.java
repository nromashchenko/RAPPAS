/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inputs;

/**
 * allow controlled parsing of a proteome
 * @author linard
 */
public interface SequencePointer {
    public StringBuffer nextSequenceAsFasta();
    public int getContentSize();
    public void closePointer();
    public void resetPointer();
    public void setPointerPosition(int fastaNumber);
}
